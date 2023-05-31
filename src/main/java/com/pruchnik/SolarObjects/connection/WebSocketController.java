package com.pruchnik.SolarObjects.connection;

import com.pruchnik.SolarObjects.objects.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.StompSubProtocolHandler;

import java.security.Principal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
public class WebSocketController {

    private final ThreadLocal<WebSocketSession> sessionThreadLocal = new ThreadLocal<>();

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleWebSocketConnect(SessionConnectedEvent event) {
        StompSubProtocolHandler handler = (StompSubProtocolHandler) event.getSource();
//        WebSocketSession session = (WebSocketSession) event.getSource();

//        handler.afterSessionStarted(sess -> session = sess );
//        sessionThreadLocal.set(session);
    }

//    @EventListener
//    public void handleWebSocketDisconnect(SessionDisconnectEvent event) {
//        StompSubProtocolHandler session = (StompSubProtocolHandler) event.getSource();
//        sessionThreadLocal.remove();
//    }

    @MessageMapping("/message")
    @SendTo("/sim/msg")
    @CrossOrigin(origins = "http://localhost:3000")
    private Message receivePublicMessage(@Payload Message message){
        return message;
    }

    @MessageMapping("/transfer")
    @SendToUser("/sim/transfer")
    @CrossOrigin(origins = "http://localhost:3000")
    private String simulationTransfer(@Payload String message, @Header("simpSessionId") String sessionId, final Principal principal){
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            return "result :" + message + " " + principal.getName();
        });

        messagingTemplate.convertAndSendToUser(sessionId, "/sim/transfer", message + " by ID");

        messagingTemplate.convertAndSendToUser(principal.getName(), "/sim/transfer", message + " by principal");

        try {
            String finalResult = future.get();
            return finalResult;

        } catch (InterruptedException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
