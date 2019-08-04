package com.devplayg.coffee.controller;

import com.devplayg.coffee.entity.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@Slf4j
public class WebSocketController {

    @MessageMapping("/talk") // /messaging/hello
    @SendTo("/topic/public")
    public Message broadcast( Message message, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        Thread.sleep(100); // delay
        Principal principal = headerAccessor.getUser();
        log.debug("# header: {}", headerAccessor);
        message.setUsername(principal.getName());
        return message;
    }

    @MessageMapping("/join")
    @SendTo("/topic/public")
    public Message addUser(@Payload Message message,
                               SimpMessageHeaderAccessor headerAccessor) {

        log.debug("# header: {}", headerAccessor);
//        Member member = (Member)headerAccessor.getUser();
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", "who are you");
        return message;
    }
}
