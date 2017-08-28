/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.negimiso;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@EnableScheduling
@Controller
@SpringBootApplication
@LineMessageHandler
public class SwitchCheckerApplication {

    @Autowired
    private ShopCheckService shopService;

    @Autowired
    private LineApiService lineService;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SwitchCheckerApplication.class, args);
    }

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        System.out.println("event: " + event);
        return new TextMessage(event.getMessage().getText());
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }

    @RequestMapping("/")
    String index() {
        return "index";
    }

    /**
     * ソケットメソッド。
     *
     * @return テンプレートのパス
     */
    @RequestMapping("/switch")
    public String switchCheck() {
        return "switch";
    }

    @MessageMapping("/socket") // エンドポイントの指定
    @SendTo("/topic/greetings") // メッセージの宛先を指定
    public ShopInfoBean greeting() {
        ShopInfoBean bean = new ShopInfoBean();
        bean.setShopName(Static.DUMMY);
        bean.setUrl(Static.DUMMY_URL);
        bean.setSaleFlg(true);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        bean.setDateStr(sdf.format(bean.getDate()));
        bean.setMemo("誰かのいたずら");
        try {
            lineService.push(bean);
        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
        return bean;
    }

    @Scheduled(fixedDelay = 10000)
    public void nintendo() throws InterruptedException, IOException {
        ShopInfoBean bean = new ShopInfoBean();
        bean.setShopName(Static.NINTENDO);
        bean.setUrl(Static.NINTENDO_URL);
        bean.setSaleFlg(false);
        shopService.check(bean);
    }

    @Scheduled(fixedDelay = 10000)
    public void amazon() throws InterruptedException, IOException {
        ShopInfoBean bean = new ShopInfoBean();
        bean.setShopName(Static.AMAZON);
        bean.setUrl(Static.AMAZON_URL);
        bean.setSaleFlg(false);
        shopService.check(bean);
    }

    @Scheduled(fixedDelay = 10000)
    public void yodobashi() throws InterruptedException, IOException {
        ShopInfoBean bean = new ShopInfoBean();
        bean.setMemo("グレー");
        bean.setShopName(Static.YODOBASHI);
        bean.setUrl(Static.YODOBASHI_URL);
        bean.setSaleFlg(false);
        shopService.check(bean);
    }

    @Scheduled(fixedDelay = 10000)
    public void yodobashi2() throws InterruptedException, IOException {
        ShopInfoBean bean = new ShopInfoBean();
        bean.setMemo("ネオンブルー");
        bean.setShopName(Static.YODOBASHI);
        bean.setUrl(Static.YODOBASHI_URL2);
        bean.setSaleFlg(false);
        shopService.check(bean);
    }

    @Scheduled(fixedDelay = 10000)
    public void rakuten() throws InterruptedException, IOException {
        ShopInfoBean bean = new ShopInfoBean();
        bean.setMemo("グレー");
        bean.setShopName(Static.RAKUTEN);
        bean.setUrl(Static.RAKUTEN_URL);
        bean.setSaleFlg(false);
        shopService.check(bean);
    }

    @Scheduled(fixedDelay = 10000)
    public void rakuten2() throws InterruptedException, IOException {
        ShopInfoBean bean = new ShopInfoBean();
        bean.setMemo("ネオンブルー");
        bean.setShopName(Static.RAKUTEN);
        bean.setUrl(Static.RAKUTEN_URL2);
        bean.setSaleFlg(false);
        shopService.check(bean);
    }

    //    @Scheduled(fixedDelay = 10000)
    //    public void toysrus() throws InterruptedException, IOException {
    //        ShopInfoBean bean = new ShopInfoBean();
    //        bean.setMemo("グレー");
    //        bean.setShopName(Static.TOYSRUS);
    //        bean.setUrl(Static.TOYSRUS_URL);
    //        bean.setSaleFlg(false);
    //        shopService.check(bean);
    //    }

    // @Scheduled(fixedDelay = 5000)
    // public void dummy() throws InterruptedException, IOException {
    // ShopInfoBean bean = new ShopInfoBean();
    // bean.setShopName(Static.DUMMY);
    // bean.setUrl(Static.DUMMY_URL);
    // bean.setSaleFlg(false);
    // // Randomクラスのインスタンス化
    // Random rnd = new Random();
    // int ran = rnd.nextInt(2);
    // if (ran == 0) {
    // bean.setSaleFlg(true);
    // }
    // service.check(bean);
    // }

}
