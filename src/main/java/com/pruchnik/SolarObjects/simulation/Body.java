package com.pruchnik.SolarObjects.simulation;

import com.pruchnik.SolarObjects.objects.Vector3D;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Body {
    private static final double G = 6.67408e-11;

    private double mass;
    private double radius;
    private Vector3D position;
    private Vector3D velocity;
    private Vector3D force;

    public Body(double mass, double radius, Vector3D position, Vector3D velocity) {
        this.mass = mass;
        this.radius = radius;
        this.position = position;
        this.velocity = velocity;
        this.force = new Vector3D(0, 0, 0);
    }

    public void resetForce() {
        force.setX(0);
        force.setY(0);
        force.setZ(0);
    }

    public void addForce(double[] f) {
        force.setX(force.getX()+f[0]);
        force.setY(force.getY()+f[1]);
        force.setZ(force.getZ()+f[2]);
    }

    public void update(double dt) {
        // Update position using velocity
        position.setX(position.getX() + velocity.getX() * dt);
        position.setY(position.getY() + velocity.getY() * dt);
        position.setZ(position.getZ() + velocity.getZ() * dt);

        // Update velocity using force
        velocity.setX(velocity.getX() + force.getX() / mass * dt);
        velocity.setY(velocity.getY() + force.getY() / mass * dt);
        velocity.setZ(velocity.getZ() + force.getZ() / mass * dt);
    }

    public double distanceTo(Body other) {
            double dx = position.getX() - other.position.getX();
            double dy = position.getY() - other.position.getY();
            double dz = position.getZ() - other.position.getZ();
            return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public double[] forceFrom(Body other) {
        double dist = distanceTo(other);
        double[] unitVector = new double[] {(other.position.getX() - position.getX()) / dist, (other.position.getY() - position.getY()) / dist, (other.position.getZ() - position.getZ()) / dist};
        double magnitude = G * mass * other.mass / (dist * dist);
        return new double[] {magnitude * unitVector[0], magnitude * unitVector[1], magnitude * unitVector[2]};
    }
}