package com.justinwu.lineaichatbot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.justinwu.lineaichatbot.service.CWAService;
import com.justinwu.lineaichatbot.service.OpenAIService;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@LineMessageHandler
public class MessageController {

    private final Logger log = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private OpenAIService openAIService;

    @Autowired
    private CWAService cwaService;

    //處理line官方帳號收到的文字訊息event
    @EventMapping
    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws JsonProcessingException {
        log.info("event: " + event);
        //解析event，取得text
        final String originalMessageText = event.getMessage().getText();
        String responseText;

        switch(originalMessageText){
            case "今日天氣":
                responseText = cwaService.getWeather(1);
                break;
            case "今明天氣":
                responseText = cwaService.getWeather(2);
                break;
            default:
                //將解析出的文字交給openAIService處理，並取得openAI的回覆
                responseText = openAIService.getChatGPTResponse(originalMessageText);
        }

        //line官方帳號回傳訊息給用戶
        return new TextMessage(responseText);
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }
}
