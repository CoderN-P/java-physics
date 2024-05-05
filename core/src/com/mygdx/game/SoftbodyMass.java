package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import static java.lang.Math.sqrt;

public class SoftbodyMass {
    SoftbodyParticle[][] particles;
    Spring[] springs;
    PhysicsTest physicsTest;

    static final double springLength = 0.2; // meters
    static final double springStiffness = 0; // N/m 350
    static final double springDamping = -0.02; // Ns/m 0.02
    static final double collisionRadius = 0.075; // meters
    static final float coefficientOfRestitution = 0.5f;



    public SoftbodyMass(float widthMeters, float heightMeters, float x, float y, PhysicsTest physicsTest){
        // Width is in meters
        // Height is in meters
        // Each particle has a diameter of 0.1 meters
        // Each spring has a rest length of 0.1 meters
        // Particles are arranged in a grid and connected by a truss of springs
        // x and y are in meters
        // x and y are the top left corner of the mass

        this.physicsTest = physicsTest;


        int width = Math.round(widthMeters*5); // width in particles
        int height = Math.round(heightMeters*5); // height in particles
        particles = new SoftbodyParticle[height][width];
        int squares = (width-1)*(height-1);
        int horizontalSprings = width*(height-1);
        int verticalSprings = height*(width-1);
        int totalSprings = squares*2 + horizontalSprings + verticalSprings;
        springs = new Spring[totalSprings];
        double diameter = 0.1; // meters


        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                particles[i][j] = new SoftbodyParticle(x+j*(springLength-0.1)+(j-1)*diameter+diameter/2, y-i*(springLength-0.1) -(i-1)*diameter + diameter/2, new Vector2(0, 0), physicsTest);
            }
        }

        int springsAdded = 0;
        // Connect particles with springs in a truss pattern
        // Horizontal springs
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width-1; j++){
                springsAdded += 1;
                springs[springsAdded-1] = new Spring(particles[i][j], particles[i][j+1], springStiffness, springDamping, springLength, physicsTest);
                particles[i][j].addSpring(springs[springsAdded-1]);
                particles[i][j+1].addSpring(springs[springsAdded-1]);
            }
        }
        // Vertical springs
        for (int i = 0; i < height-1; i++){
            for (int j = 0; j < width; j++){
                springsAdded += 1;
                springs[springsAdded-1] = new Spring(particles[i][j], particles[i+1][j], springStiffness, springDamping, springLength, physicsTest);
                particles[i][j].addSpring(springs[springsAdded-1]);
                particles[i+1][j].addSpring(springs[springsAdded-1]);
            }
        }

        // Diagonal springs
        for (int i = 0; i < height-1; i++){
            for (int j = 0; j < width-1; j++){
                springsAdded += 1;
                springs[springsAdded-1] = new Spring(particles[i][j], particles[i+1][j+1], springStiffness, springDamping, springLength* sqrt(2), physicsTest);
                particles[i][j].addSpring(springs[springsAdded-1]);
                particles[i+1][j+1].addSpring(springs[springsAdded-1]);
                springsAdded += 1;
                springs[springsAdded-1] = new Spring(particles[i][j+1], particles[i+1][j], springStiffness, springDamping, springLength* sqrt(2), physicsTest);
                particles[i][j+1].addSpring(springs[springsAdded-1]);
                particles[i+1][j].addSpring(springs[springsAdded-1]);
            }
        }

        System.out.println("Springs added: " + springsAdded + " Total springs: " + totalSprings);
    }

    void update(boolean paused){
        if (!paused) {

            detectCollisions();


            for (int i = 0; i < particles.length; i++) {
                for (int j = 0; j < particles[i].length; j++) {
                    particles[i][j].update();
                }
            }


        }

        render();
    }

    void detectCollisions(){


        for (int i = 0; i < particles.length; i++){
            for (int j = 0; j < particles[0].length; j++) {
                for (int k = i; k < particles.length; k++) {
                    int lStart = (k > i) ? 0 : j + 1;
                    for (int l = lStart; l < particles[0].length; l++) {
                        SoftbodyParticle cur = particles[i][j];
                        SoftbodyParticle p = particles[k][l];
                        double d = cur.position.dst(p.position);

                        if (d == 0) {
                            continue;
                        }
                        if (d <= collisionRadius * 2) {
                            updateVelocityInCollision(cur, p);
                        }
                    }
                }
            }
        }

        for (int i = 0; i < particles.length; i++){
            for (int j = 0; j < particles[i].length; j++) {
                SoftbodyParticle particle = particles[i][j];
                if (particle.position.x - collisionRadius < 0) {
                    particle.position.x = (float) collisionRadius;
                    particle.velocity.x *= -1*coefficientOfRestitution;
                }
                if (particle.position.x + collisionRadius > physicsTest.WIDTH/physicsTest.scale) {
                    particle.position.x = (float) (physicsTest.WIDTH/physicsTest.scale - collisionRadius);
                    particle.velocity.x *= -1*coefficientOfRestitution;
                }
                if (particle.position.y - collisionRadius < 0) {
                    particle.position.y = (float) collisionRadius;
                    particle.velocity.y *= -1*coefficientOfRestitution;
                }
                if (particle.position.y + collisionRadius > physicsTest.HEIGHT/physicsTest.scale) {
                    particle.position.y = (float) (physicsTest.HEIGHT/physicsTest.scale - collisionRadius);
                    particle.velocity.y *= -1*coefficientOfRestitution;
                }
            }
        }

    }
    void render(){
        physicsTest.shapeRenderer.setColor(1, 1, 1, 1);
        for (Spring spring : springs){
            spring.render();
        }

        physicsTest.shapeRenderer.setColor(1, 0, 0, 1);
        for (SoftbodyParticle[] particle : particles) {
            for (SoftbodyParticle softbodyParticle : particle) {
                softbodyParticle.render();
            }
        }


    }

    public Vector2 getCenterPosition(){
        float x = 0;
        float y = 0;
        for (int i = 0; i < particles.length; i++){
            for (int j = 0; j < particles[i].length; j++){
                x += particles[i][j].position.x;
                y += particles[i][j].position.y;
            }
        }
        x /= particles.length*particles[0].length;
        y /= particles.length*particles[0].length;
        return new Vector2(x, y);
    }

    void updateVelocityInCollision(SoftbodyParticle p1, SoftbodyParticle p2){

            // m1 = mass, m2 = p.mass
        Vector2 dir = p1.position.cpy().sub(p2.position).nor();

        // Reflect p1 velocity about dir
        double dot = p1.velocity.dot(dir);
        float xChange = (float) (2*dot*dir.x);
        float yChange = (float) (2*dot*dir.y);
        p1.velocity.x -= xChange;
        p1.velocity.y -= yChange;



        double overlap = 2 * collisionRadius - p1.position.dst(p2.position);

        // Resolve the overlap by moving the particles apart along the collision normal
        Vector2 separation = dir.scl((float) overlap);
        p1.position.add(separation.scl(0.5f));
        p2.position.sub(separation.scl(0.5f));

    }
}
