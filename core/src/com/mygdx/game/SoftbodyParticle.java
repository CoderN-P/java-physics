package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class SoftbodyParticle {
    Vector2 position;
    Vector2 velocity;

    public Spring springs[]; // Springs connected to this particle
    public int addedSprings = 0;

    PhysicsTest physicsTest;
    static final float GRAVITY = -9.81f; // m/s^2
    public static final double MASS = 1; // kg
    public static final double RADIUS = 0.05; // meters

    public Vector2 springForce = new Vector2(0, 0);


    public SoftbodyParticle(double x, double y, Vector2 velocity, PhysicsTest physicsTest) {
        this.position = new Vector2((float) x, (float) y);
        this.velocity = velocity;
        this.physicsTest = physicsTest;
        this.springs = new Spring[8]; // Maximum of 8 springs
    }

    public Vector2 acceleration(Vector2 position, Vector2 velocity, float t) {
        // Calculate the acceleration based on the current state and time.
        // This will depend on the forces acting on the particle.
        // For now, let's assume it's just gravity.
        springForce = new Vector2(0, 0);
        Vector2 curPosition = this.position.cpy();
        Vector2 curVelocity = this.velocity.cpy();
        this.position = position;
        this.velocity = velocity;

        for (int i = 0; i < addedSprings; i++){
            Vector2[] springForces = springs[i].getSpringForces();
            if (springs[i].p1 == this){
                springForce.add(springForces[0]);
            } else {
                springForce.add(springForces[1]);
            }
        }
        this.position = curPosition;
        this.velocity = curVelocity;
        Vector2 acceleration = new Vector2(0, GRAVITY).add(springForce);
        springForce = new Vector2(0, 0);
        return acceleration;
    }

    public void rk4Integration(float dt, boolean changePosition, boolean changeVelocity) {

        Vector2 a1 = acceleration(position, velocity, 0);
        Vector2 k1v = new Vector2(a1).scl(dt);
        Vector2 k1p = new Vector2(velocity).scl(dt);

        Vector2 a2 = acceleration(new Vector2(position).add(k1p.scl(0.5f)), new Vector2(velocity).add(k1v.scl(0.5f)), dt / 2);
        Vector2 k2v = new Vector2(a2).scl(dt);
        Vector2 k2p = new Vector2(velocity).add(k1v.scl(0.5f)).scl(dt);

        Vector2 a3 = acceleration(new Vector2(position).add(k2p.scl(0.5f)), new Vector2(velocity).add(k2v.scl(0.5f)), dt / 2);
        Vector2 k3v = new Vector2(a3).scl(dt);
        Vector2 k3p = new Vector2(velocity).add(k2v.scl(0.5f)).scl(dt);

        Vector2 a4 = acceleration(new Vector2(position).add(k3p), new Vector2(velocity).add(k3v), dt);
        Vector2 k4v = new Vector2(a4).scl(dt);
        Vector2 k4p = new Vector2(velocity).add(k3v).scl(dt);

        if (changePosition) {
            position.add(new Vector2(k1p).add(k2p.scl(2)).add(k3p.scl(2)).add(k4p).scl(1 / 6.0f));
        }
        if (changeVelocity){
            velocity.add(new Vector2(k1v).add(k2v.scl(2)).add(k3v.scl(2)).add(k4v).scl(1 / 6.0f));
        }
    }

    void update() {
       //  velocity.x += (float) ((springForce.x / MASS) * Gdx.graphics.getDeltaTime());
        // velocity.y += (float) ((GRAVITY + springForce.y / MASS) * Gdx.graphics.getDeltaTime());
        // velocity.y += (float) (GRAVITY * Gdx.graphics.getDeltaTime());

        position.x += velocity.x * Gdx.graphics.getDeltaTime();
        position.y += velocity.y * Gdx.graphics.getDeltaTime();

        springForce = new Vector2(0, 0);
    }

    void addSpring(Spring spring){
        springs[addedSprings] = spring;
        addedSprings++;
    }

    void render(){
        physicsTest.shapeRenderer.circle(position.x*physicsTest.scale,  position.y*physicsTest.scale, (float) RADIUS*physicsTest.scale);
    }
}
