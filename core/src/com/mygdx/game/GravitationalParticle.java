package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class GravitationalParticle {
    public float minX, minY, maxX, maxY, radius, mass;
    public Vector2 velocity, position, curForce;
    public boolean renderInCenter;
    public Vector3 color;
    PhysicsTest physicsTest;


    public GravitationalParticle(Vector2 position, Vector2 velocity, float minX, float minY, float maxX, float maxY, float radius, float mass, PhysicsTest physicsTest, boolean renderInCenter){
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
        this.renderInCenter = renderInCenter;
        this.color = new Vector3((float) Math.random(), (float) Math.random(), (float) Math.random());
    }

    public void update(){
        this.velocity.add(curForce.cpy().scl(1/mass).scl(Gdx.graphics.getDeltaTime()));
        this.position.add(this.velocity.cpy().scl(Gdx.graphics.getDeltaTime()));
    }

    public void render(Vector2 reference){
        update();
        this.curForce = new Vector2(0, 0);

        if (renderInCenter){
            physicsTest.shapeRenderer.circle((float) physicsTest.WIDTH/2,  (float) physicsTest.HEIGHT/2, radius*physicsTest.scale);
        } else {
            physicsTest.shapeRenderer.circle((position.x - reference.x + physicsTest.WIDTH/(2*physicsTest.scale)) * physicsTest.scale, (position.y - reference.y + physicsTest.HEIGHT/(2*physicsTest.scale )) * physicsTest.scale, (float) radius * physicsTest.scale);
        }
    }
}
