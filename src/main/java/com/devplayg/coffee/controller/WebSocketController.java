package com.devplayg.coffee.controller;

import com.devplayg.coffee.entity.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketController {

    @MessageMapping("/hello") // /messaging/hello
    @SendTo("/topic/system")
    public Message broadcast(Message message) throws Exception {
        Thread.sleep(100); // delay
        return message;
    }
}
