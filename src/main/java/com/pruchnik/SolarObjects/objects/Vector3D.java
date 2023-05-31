package com.pruchnik.SolarObjects.objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Vector3D {
    private double x;
    private double y;
    private double z;

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D add(Vector3D other) {
        return new Vector3D(x + other.getX(), y + other.getY(), z + other.getZ());
    }

    public Vector3D subtract(Vector3D other) {
        return new Vector3D(x - other.getX(), y - other.getY(), z - other.getZ());
    }

    public Vector3D multiply(double scalar) {
        return new Vector3D(scalar * x, scalar * y, scalar * z);
    }

    public double getMagnitude() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public Vector3D normalize() {
        double magnitude = getMagnitude();
        return new Vector3D(x / magnitude, y / magnitude, z / magnitude);
    }

    public static Vector3D crossProduct(Vector3D a, Vector3D b) {
        double x = a.getY() * b.getZ() - a.getZ() * b.getY();
        double y = a.getZ() * b.getX() - a.getX() * b.getZ();
        double z = a.getX() * b.getY() - a.getY() * b.getX();
        return new Vector3D(x, y, z);
    }

    public static double dotProduct(Vector3D a, Vector3D b) {
        return a.getX() * b.getX() + a.getY() * b.getY() + a.getZ() * b.getZ();
    }
}
