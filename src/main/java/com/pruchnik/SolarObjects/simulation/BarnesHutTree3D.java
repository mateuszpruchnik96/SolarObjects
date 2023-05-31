package com.pruchnik.SolarObjects.simulation;

import com.pruchnik.SolarObjects.objects.Vector3D;

import java.util.Arrays;
import java.util.stream.Stream;

public class BarnesHutTree3D {
    private static final double THETA = 0.5;

    private double x;
    private double y;
    private double z;
    private double size;
    private Body body;
    private BarnesHutTree3D[] subCells;

    public BarnesHutTree3D(double x, double y, double z, double size) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.size = size;
        this.body = null;
        this.subCells = null;
    }

    public boolean isLeaf() {
        return body != null;
    }

    public void insert(Body newBody) {
        if (isLeaf()) {
            // Subdivide node
            double halfSize = size / 2;
            subCells = new BarnesHutTree3D[8];
            subCells[0] = new BarnesHutTree3D(x - halfSize, y - halfSize, z - halfSize, halfSize);
            subCells[1] = new BarnesHutTree3D(x - halfSize, y - halfSize, z + halfSize, halfSize);
            subCells[2] = new BarnesHutTree3D(x - halfSize, y + halfSize, z - halfSize, halfSize);
            subCells[3] = new BarnesHutTree3D(x - halfSize, y + halfSize, z + halfSize, halfSize);
            subCells[4] = new BarnesHutTree3D(x + halfSize, y - halfSize, z - halfSize, halfSize);
            subCells[5] = new BarnesHutTree3D(x + halfSize, y - halfSize, z + halfSize, halfSize);
            subCells[6] = new BarnesHutTree3D(x + halfSize, y + halfSize, z - halfSize, halfSize);
            subCells[7] = new BarnesHutTree3D(x + halfSize, y + halfSize, z + halfSize, halfSize);

            // Move existing body to appropriate child
            Vector3D bodyPos = body.getPosition();
            if (bodyPos.getX() < x) {
                if (bodyPos.getY() < y) {
                    if (bodyPos.getZ() < z) {
                        subCells[0].insert(body);
                    } else {
                        subCells[1].insert(body);
                    }
                } else {
                    if (bodyPos.getZ() < z) {
                        subCells[2].insert(body);
                    } else {
                        subCells[3].insert(body);
                    }
                }
            } else {
                if (bodyPos.getY() < y) {
                    if (bodyPos.getZ() < z) {
                        subCells[4].insert(body);
                    } else {
                        subCells[5].insert(body);
                    }
                } else {
                    if (bodyPos.getZ() < z) {
                        subCells[6].insert(body);
                    } else {
                        subCells[7].insert(body);
                    }
                }
            }
            body = null;
        }

        // Add new body to appropriate child or subnode
        double dx = newBody.getPosition().getX() - x;
        double dy = newBody.getPosition().getY() - y;
        double dz = newBody.getPosition().getZ() - z;
        if (dx < -size || dx > size || dy < -size || dy > size || dz < -size || dz > size) {
            return;
        }
        if (dx < 0 && dy < 0 && dz < 0) {
            subCells[0].insert(newBody);
        } else if (dx < 0 && dy < 0 && dz >= 0) {
            subCells[1].insert(newBody);
        } else if (dx < 0 && dy >= 0 && dz < 0) {
            subCells[2].insert(newBody);
        } else if (dx < 0 && dy >= 0 && dz >= 0) {
            subCells[3].insert(newBody);
        } else if (dx >= 0 && dy < 0 && dz < 0) {
            subCells[4].insert(newBody);
        } else if (dx >= 0 && dy < 0 && dz >= 0) {
            subCells[5].insert(newBody);
        } else if (dx >= 0 && dy >= 0 && dz < 0) {
            subCells[6].insert(newBody);
        } else {
            subCells[7].insert(newBody);
        }
    }

    public void computeForce(Body body) {
        if (isLeaf() || body == null || body == this.body) {
            return;
        }
        if (isLeaf() && this.body != null && this.body != body) {
            double[] force = body.forceFrom(this.body);
            body.addForce(force);
        } else if (this.body == null || size / body.distanceTo(this.body) < THETA) {
            return;
        } else {
            Stream<BarnesHutTree3D> stream = Arrays.stream(subCells);
            stream.forEach(subCell -> subCell.computeForce(body));
        }
    }
}