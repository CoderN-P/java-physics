package com.mygdx.game;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public class Particle {
    // Radius: meters
    // Position: meters
    // Velocity: meters/second
    // Bounce coefficient: 0-1
    // minX, minY, maxX, maxY: meters
    // Mass: kg

    public float x, y, velocityX, velocityY, minX, minY, maxX, maxY, radius, bounceCoefficient, mass;
    static final float GRAVITY = 9.8f; // m/s^2
    PhysicsTest physicsTest;

    public Particle(float x, float y, float velocityX, float velocityY, float minX, float minY, float maxX, float maxY, float radius, float bounceCoefficient, float mass, PhysicsTest physicsTest) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.radius = radius;
        this.bounceCoefficient = bounceCoefficient;
        this.mass = mass;
        this.physicsTest = physicsTest;
    }

    public float getKE(){
        return .5f*mass*(velocityX*velocityX + velocityY*velocityY);
    }

    public void update(float delta) {
        manageCollisions();
        // Update velocity
        if (abs(velocityY) < 0.1f && y <= radius + 0.1f) {
            velocityY = 0;
            y = radius + 0.05f;
        } else {
            velocityY -= GRAVITY * delta;
        }


        restrictPosition();

        // Update position
        x += velocityX * delta;
        y += velocityY * delta;

        if (abs(velocityY) < 0.1f && y <= radius + 0.1f) {
            velocityY = 0;
            y = radius + 0.05f;
        }
    }

    public void restrictPosition() {
        if (x < minX + radius) {
            x = minX + radius;
            velocityX = (float) (-velocityX * sqrt(bounceCoefficient));

        } else if (x > maxX-radius) {
            x = maxX - radius;
            velocityX = (float) (-velocityX * sqrt(bounceCoefficient));
        }
        if (y < minY+radius) {
            y = minY+radius;
            velocityY = (float) (-velocityY * sqrt(bounceCoefficient));

        } else if (y > maxY - radius) {
            y = maxY - radius;
            velocityY = (float) (-velocityY * sqrt(bounceCoefficient));;
        }
    }


    public void manageCollisions(){
        for (Particle p: physicsTest.particles){
            if (p == this) continue;
            float dx = abs(x - p.x);
            float dy = abs(y - p.y);
            double d = sqrt(dx*dx + dy*dy);

            if (d <= radius+p.radius){
                float newVelocityX = (velocityX * (mass - p.mass) + 2 * p.mass * p.velocityX) / (mass + p.mass);
                float newVelocityY = (velocityY * (mass - p.mass) + 2 * p.mass * p.velocityY) / (mass + p.mass);

                // adjust velocities according to the bounce coefficient
                velocityX = (float) (newVelocityX * Math.sqrt(bounceCoefficient));
                velocityY = (float) (newVelocityY * Math.sqrt(bounceCoefficient));

            }
        }

    }

}
