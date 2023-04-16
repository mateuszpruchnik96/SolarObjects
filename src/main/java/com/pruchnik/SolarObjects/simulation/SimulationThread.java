package com.pruchnik.SolarObjects.simulation;

import lombok.Getter;
import lombok.Setter;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Getter
@Setter
public class SimulationThread extends Thread {

    private String connectionSessionId;

    private SimpMessagingTemplate messagingTemplate;

    public SimulationThread(String connectionSessionId, SimpMessagingTemplate simpMessagingTemplate){
        this.connectionSessionId = connectionSessionId;
        this.messagingTemplate = simpMessagingTemplate;
    }


    public void run(){
        System.out.println("Thread with sessionId " + this.connectionSessionId + " has started!");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // send response to the client
        String message = "Thread with sessionId " + this.connectionSessionId + " has started!";
        messagingTemplate.convertAndSendToUser(connectionSessionId, "/sim/transfer", message);

    };


}
