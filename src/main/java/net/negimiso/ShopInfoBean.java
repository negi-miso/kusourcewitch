package net.negimiso;

import java.util.Date;

public class ShopInfoBean {

	// チェック対象店舗名
	private String shopName;

	// チェック対象ＵＲＬ
	private String url;

	// 販売フラグ：trueならば在庫アリ
	private boolean saleFlg = false;

	// HTTPステータスコード
	private int httpStatus;

	// 処理開始時刻
	private Date date = new Date();

	// 処理開始時刻(文字列)
	private String dateStr;

	// メモ（備考）
	private String memo = "";

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isSaleFlg() {
		return saleFlg;
	}

	public void setSaleFlg(boolean saleFlg) {
		this.saleFlg = saleFlg;
	}

	public int getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}
