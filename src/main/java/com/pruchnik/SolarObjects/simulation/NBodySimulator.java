package com.pruchnik.SolarObjects.simulation;

import com.pruchnik.SolarObjects.objects.Vector3D;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.pruchnik.SolarObjects.objects.CelestialCoordinates;
import org.json.simple.parser.ParseException;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.FileReader;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

public class NBodySimulator {

    private static final double G = 6.67408e-11;

    List<Body> bodies = new ArrayList<>();
    List<CelestialCoordinates> coordinates = new ArrayList<>();
    SimpMessagingTemplate simpMessagingTemplate;
    Principal principal;

    public NBodySimulator(SimpMessagingTemplate simpMessagingTemplate, Principal principal){
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.principal = principal;
    }

    JSONParser parser = new JSONParser();

    Object obj;

    // Run simulation
    double dt = 0.05;
    double t = 0;


    public void run() {
        // Add bodies to the system
        planetsCartesianDecoder();

        while (true) {
            // Compute forces on each body
            BarnesHutTree3D tree = new BarnesHutTree3D(0, 0, 0, 1);
            for (Body body : bodies) {
                tree.insert(body);
            }
            for (Body body : bodies) {
                body.resetForce();
            }

            // Update positions and velocities of each body
            for (Body body : bodies) {
                body.update(dt);
                System.out.println(body.getMass()+" "+body.getPosition().getX());
            }

            simpMessagingTemplate.convertAndSendToUser(principal.getName(), "/sim/transfer", bodies);

            // Increment time
            t += dt;
        }
    }

    private void planetsCartesianDecoder(){
        try {
            obj = parser.parse(new FileReader("A:\\SolarObjects\\src\\main\\java\\com\\pruchnik\\SolarObjects\\objects\\planetsCartesian.json"));

            JSONObject jsonObject = (JSONObject) obj;
            JSONObject planets = (JSONObject) jsonObject.get("planets");

            planets.forEach((key, value) -> {
                JSONObject planet = (JSONObject) value;
                JSONArray positionJson = (JSONArray) planet.get("position");
                JSONArray velocityJson = (JSONArray) planet.get("velocity");

                Vector3D position = new Vector3D(Double.valueOf(positionJson.get(0).toString()), Double.valueOf(positionJson.get(1).toString()), Double.valueOf(positionJson.get(2).toString()));

                Vector3D velocity = new Vector3D(Double.valueOf(velocityJson.get(0).toString()), Double.valueOf(velocityJson.get(1).toString()), Double.valueOf(velocityJson.get(2).toString()));

                Double mass = Double.valueOf(planet.get("mass").toString());
                Double radius = Double.valueOf(planet.get("radius").toString());

                Body body = new Body(mass, radius, position ,velocity);
                bodies.add(body);
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("Bodies size: " + bodies.size());
    }
}