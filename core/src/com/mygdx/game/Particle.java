package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import static java.lang.Math.*;

public class Particle {
    // Radius: meters
    // Position: meters
    // Velocity: meters/second
    // Bounce coefficient: 0-1
    // minX, minY, maxX, maxY: meters
    // Mass: kg

    public float x, y, velocityX, velocityY, minX, minY, maxX, maxY, radius, bounceCoefficient, mass;
    static final float GRAVITY = 9.8f; // m/s^2
    public Vector3 color;
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
        this.color = new Vector3((float) Math.random(), (float) Math.random(), (float) Math.random());
    }

    public float getKE(){
        return .5f*mass*(velocityX*velocityX + velocityY*velocityY);
    }

    public void update(float delta) {
        // Update velocity
        if (abs(velocityY) < 0.1f && y <= radius + 0.1f) {
            velocityY = 0;
            y = radius + 0.05f;
        } else {
            if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                velocityY -= GRAVITY * delta;
            } else {

                float dx = Gdx.input.getX() / physicsTest.scale - x;
                float dy = (physicsTest.HEIGHT - Gdx.input.getY()) / physicsTest.scale - y;
                float d = (float) sqrt(dx * dx + dy * dy);
                if (d > radius){
                    velocityY -= GRAVITY * delta;
                } else {
                    if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
                        velocityX = 0;
                        velocityY = 0;
                    }
                }
            }

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


}
