package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class GravitationalParticle {
    public float minX, minY, maxX, maxY, radius, mass;
    public Vector2 velocity, position, curForce;
    PhysicsTest physicsTest;


    public GravitationalParticle(Vector2 position, Vector2 velocity, float minX, float minY, float maxX, float maxY, float radius, float mass, PhysicsTest physicsTest){
        this.position = position;
        this.velocity = velocity;
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.radius = radius;
        this.mass = mass;
        this.physicsTest = physicsTest;
        this.curForce = new Vector2(0, 0);
    }

    public void update(){
        this.velocity.add(curForce.cpy().scl(1/mass).scl(Gdx.graphics.getDeltaTime()));
        this.position.add(this.velocity.cpy().scl(Gdx.graphics.getDeltaTime()));
    }

    public void render(){
        update();
        this.curForce = new Vector2(0, 0);
        physicsTest.shapeRenderer.circle(position.x*physicsTest.scale, position.y*physicsTest.scale, radius*physicsTest.scale);
    }
}
