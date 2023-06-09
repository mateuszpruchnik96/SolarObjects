package com.pruchnik.SolarObjects.connection;

import com.pruchnik.SolarObjects.simulation.SimulationThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.StompSubProtocolHandler;

import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class SubscribeEventListener implements ApplicationListener<ApplicationEvent> {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    private Map<String, SimulationThread> simulationMap = new HashMap<>();

    @EventListener
    public void handleWebSocketDisconnect(SessionDisconnectEvent event) {
        StompSubProtocolHandler session = (StompSubProtocolHandler) event.getSource();
        System.out.println(session.toString());

        Principal principal = event.getUser();
        String userName = event.getUser().getName();

        simulationMap.remove(userName);
        simulationMap.forEach((k, v) -> {
            System.out.println("key: " + k + "; value.getName(): " + v.getName());
        });
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof SessionSubscribeEvent) {
            SessionSubscribeEvent sessionSubscribeEvent = (SessionSubscribeEvent) applicationEvent;
            StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionSubscribeEvent.getMessage());


            Object sessionId = headerAccessor.getSessionId();
            if (sessionId != null) {
                System.out.println("Session ID: " + sessionId);
                SimulationThread simulationThread = new SimulationThread(messagingTemplate, headerAccessor.getUser());
                simulationMap.put(headerAccessor.getUser().getName(), simulationThread);
                simulationMap.forEach((k, v) -> {
                    System.out.println("key: " + k + "; value.getName(): " + v.getName());
                });
                simulationThread.start();
            } else {
                System.out.println("Session ID is not set");
            }
        }
    }
}
