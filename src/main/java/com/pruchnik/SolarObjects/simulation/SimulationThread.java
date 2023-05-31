package com.pruchnik.SolarObjects.simulation;

import lombok.Getter;
import lombok.Setter;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.security.Principal;

@Getter
@Setter
public class SimulationThread extends Thread {

    private SimpMessagingTemplate messagingTemplate;

    private Principal principal;

    public SimulationThread( SimpMessagingTemplate simpMessagingTemplate, Principal principal){
        this.messagingTemplate = simpMessagingTemplate;
        this.principal = principal;
    }


    public void run(){

        String message = "Thread with principal name " + this.principal.getName() + " has started!";

        messagingTemplate.convertAndSendToUser("1","/sim/transfer", "test", createHeaders("1"));

        messagingTemplate.convertAndSendToUser(principal.getName(), "/sim/transfer", message + " 2!");

        NBodySimulator nbodysimulation = new NBodySimulator(messagingTemplate, principal);
        nbodysimulation.run();

    };

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

}
