package net.negimiso;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ShopCheckService {

	@Autowired
	private SimpMessagingTemplate template;

	public void check(ShopInfoBean bean) throws InterruptedException, IOException {

		Document doc = getDoc(bean);

		if (doc != null) {
			switch (bean.getShopName()) {
			case Static.NINTENDO:
				bean = nintendo(doc, bean);
				break;
			case Static.AMAZON:
				bean = amazon(doc, bean);
				break;
			case Static.YODOBASHI:
				bean = yodobashi(doc, bean);
				break;
			case Static.RAKUTEN:
				bean = rakuten(doc, bean);
				break;
			case Static.TOYSRUS:
				bean = toysrus(doc, bean);
				break;
			case Static.DUMMY:
				break;
			default:
				break;
			}
		}

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		bean.setDateStr(sdf.format(bean.getDate()));
		this.template.convertAndSend("/topic/greetings", bean);

	}

	public Document getDoc(ShopInfoBean bean) throws IOException {
		try {
			Connection con = Jsoup.connect(bean.getUrl());
			con.userAgent(Static.UA);
			con.timeout(5000);
			return con.get();
		} catch (HttpStatusException e) {
			bean.setHttpStatus(e.getStatusCode());
			bean.setMemo("HttpStatusException");
		} catch (SocketTimeoutException e) {
			bean.setMemo("タイムアウト");
		}

		return null;
	}

	public ShopInfoBean nintendo(Document doc, ShopInfoBean bean) throws IOException {
		Elements elements = doc.select(".items");
		bean.setHttpStatus(200);
		for (Element element : elements) {
			if (element.text().startsWith("HAC_S_KAYAA")) {
				if ("HAC_S_KAYAA/-".equals(element.text())) {
					bean.setSaleFlg(false);
				} else {
					bean.setSaleFlg(true);
				}
			}
		}
		return bean;
	}

	public ShopInfoBean amazon(Document doc, ShopInfoBean bean) throws IOException {
		Elements elements = doc.select(".a-spacing-none.olpSellerName");
		bean.setHttpStatus(200);
		bean.setSaleFlg(false);
		for (Element element : elements) {
			if (element.child(0).tagName().equals("img")) {
				bean.setSaleFlg(true);
			}
		}
		return bean;
	}

	public ShopInfoBean yodobashi(Document doc, ShopInfoBean bean) throws IOException {
		Elements elements = doc.select(".yBtn.yBtnPrimary.yBtnMedium");
		bean.setHttpStatus(200);
		bean.setSaleFlg(false);
		if (elements.size() > 0) {
			bean.setSaleFlg(true);
		}
		return bean;
	}

	public ShopInfoBean rakuten(Document doc, ShopInfoBean bean) throws IOException {
		Elements elements = doc.select(".new_addToCart");
		bean.setHttpStatus(200);
		bean.setSaleFlg(false);
		if (elements.size() > 0) {
			bean.setSaleFlg(true);
		}
		return bean;
	}

	public ShopInfoBean toysrus(Document doc, ShopInfoBean bean) throws IOException {
		Element element = doc.getElementById("cart_button");
		bean.setHttpStatus(200);
		bean.setSaleFlg(false);
		if (!element.hasAttr("hidden")) {
			bean.setSaleFlg(true);
		}
		return bean;
	}

}
