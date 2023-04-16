package com.pruchnik.SolarObjects.connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExtendedWebSocketHandler implements WebSocketHandler {



    ThreadLocal<WebSocketSession> sessionThreadLocal = new ThreadLocal<>();

    private final Map<String, WebSocketSession> idToActiveSession = new HashMap<>();
//    @Bean
//    public ThreadLocal<WebSocketSession> sessionThreadLocal(){
//        ThreadLocal<WebSocketSession> sessionThreadLocal = new ThreadLocal<>();
//        return sessionThreadLocal;
//    };

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println(session.getId());

        idToActiveSession.put(session.getId(), session);
        sessionThreadLocal.set(session);
        System.out.println(sessionThreadLocal.get().toString());
        System.out.println(idToActiveSession.get(0).toString());
        // Perform any other necessary initialization here
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    // Implement the other methods of the WebSocketHandler interface here

}
