package com.pruchnik.connection;

import com.pruchnik.objects.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
public class WebSocketController {

    @MessageMapping("/message")
    @SendTo("/app")
    @CrossOrigin(origins = "http://localhost:3000")
    private Message receivePublicMessage(@Payload Message message){
        return message;
    }

}
