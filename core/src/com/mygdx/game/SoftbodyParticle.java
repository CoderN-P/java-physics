package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;

public class SoftbodyParticle {
    Vector2 position;
    Vector2 velocity;

    public Spring[] springs; // Springs connected to this particle
    public int addedSprings = 0;

    PhysicsTest physicsTest;
    static final float GRAVITY = -9.81f; // m/s^2
    public static final double MASS = 1; // kg
    public static final double RADIUS = 0.05; // meters


    public SoftbodyParticle(double x, double y, Vector2 velocity, PhysicsTest physicsTest) {
        this.position = new Vector2((float) x, (float) y);
        this.velocity = velocity;
        this.physicsTest = physicsTest;
        this.springs = new Spring[8]; // Maximum of 8 springs
    }

    void update() {
       //  velocity.x += (float) ((springForce.x / MASS) * Gdx.graphics.getDeltaTime());
        // velocity.y += (float) ((GRAVITY + springForce.y / MASS) * Gdx.graphics.getDeltaTime());
        // velocity.y += (float) (GRAVITY * Gdx.graphics.getDeltaTime());
        Vector2 springForces = new Vector2(0, 0);

        for (int i = 0; i < addedSprings; i++){
            Vector2[] forces = springs[i].getSpringForces();
            if (springs[i].p1 == this){
                springForces.add(forces[0].scl(0.2f));
            } else {
                springForces.add(forces[1].scl(0.2f));
            }
        }
        this.velocity.add(springForces.cpy().scl(Gdx.graphics.getDeltaTime()));
        this.velocity.y += GRAVITY * Gdx.graphics.getDeltaTime();
        position.add(velocity.cpy().scl(Gdx.graphics.getDeltaTime()));

    }

    void addSpring(Spring spring){
        springs[addedSprings] = spring;
        addedSprings++;
    }

    void render(){
        physicsTest.shapeRenderer.circle(position.x * physicsTest.scale, position.y* physicsTest.scale, (float) RADIUS * physicsTest.scale);
    }
}
