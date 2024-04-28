package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

import static java.lang.Math.sqrt;

// Represents a spring between two particles
public class Spring {
    SoftbodyParticle p1;
    SoftbodyParticle p2;

    double stiffness;
    double damping;
    double restLength;

    PhysicsTest physicsTest;

    // Hooke's law
    // F = kx
    // x = displacement from rest length
    // k = stiffness
    // F = force
    // x = sqrt((p1.x - p2.x)^2 + (p1.y - p2.y)^2) - restLength

    public Spring(SoftbodyParticle p1, SoftbodyParticle p2, double stiffness, double damping, double restLength, PhysicsTest physicsTest) {
        this.p1 = p1;
        this.p2 = p2;
        this.stiffness = stiffness;
        this.damping = damping;
        this.restLength = restLength;
        this.physicsTest = physicsTest;
    }

     Vector2[] getSpringForces(){
        Vector2 d = p2.position.cpy().sub(p1.position);
        double displacement = d.len() - restLength;
        d = d.nor();
        Vector2 velocityDiff = p2.velocity.cpy().sub(p1.velocity);
        double dampingForce = velocityDiff.dot(d)*damping;
        double springForce = displacement*stiffness;
        double totalSpringForce = springForce + dampingForce;
        Vector2 p1Force = d.cpy().scl((float) totalSpringForce);
        Vector2 p2Force = p1.position.cpy().sub(p2.position).nor().scl((float) totalSpringForce);
        return new Vector2[]{p1Force, p2Force};
    }

    void render(){
        physicsTest.shapeRenderer.line( p1.position.x*physicsTest.scale, p1.position.y*physicsTest.scale, p2.position.x*physicsTest.scale, p2.position.y*physicsTest.scale);
    }



}
