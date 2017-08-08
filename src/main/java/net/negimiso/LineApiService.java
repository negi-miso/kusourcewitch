package net.negimiso;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;

import retrofit2.Response;

@Service
public class LineApiService {

    public void push(ShopInfoBean bean) throws IOException {
        TextMessage textMessage = new TextMessage(bean.getShopName() + "にて動きがあったようです\r\n" + bean.getUrl());
        PushMessage pushMessage = new PushMessage(
                "R914f2f6ed6a4f12f9e9e69bfdc4afcd9",
                textMessage);

        Response<BotApiResponse> response = LineMessagingServiceBuilder
                .create(Static.CHANNEL_ACCESS_TOKEN)
                .build()
                .pushMessage(pushMessage)
                .execute();
        System.out.println(response.code() + " " + response.message());

    }

}
