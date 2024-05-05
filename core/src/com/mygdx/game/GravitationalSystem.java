package com.mygdx.game;
import java.lang.Math;
import java.util.Arrays;

import com.badlogic.gdx.math.Vector2;

public class GravitationalSystem {
    public static final float G = (float) (6.667f*Math.pow(10, -11));
    public static final float bigMass = (float) (6*Math.pow(10, 15));
    public GravitationalParticle[] particles;
    public PhysicsTest physicsTest;

    public GravitationalSystem(int width, int height, PhysicsTest physicsTest){
        particles = new GravitationalParticle[3];
        this.physicsTest = physicsTest;

        particles[1] = new GravitationalParticle(new Vector2((float) (physicsTest.WIDTH)/(2*physicsTest.scale), (float) physicsTest.HEIGHT/(2*physicsTest.scale)), new Vector2(1, 1), 0, 0, physicsTest.WIDTH, physicsTest.HEIGHT, 0.5f, bigMass, physicsTest, false);
        particles[0] = new GravitationalParticle(new Vector2((float) (physicsTest.WIDTH)/(2*physicsTest.scale)+4, (float) physicsTest.HEIGHT/(2*physicsTest.scale)), new Vector2(0, 0), 0, 0, physicsTest.WIDTH, physicsTest.HEIGHT, 1f, bigMass*50, physicsTest, true);
        particles[2] = new GravitationalParticle(new Vector2((float) (physicsTest.WIDTH)/(2*physicsTest.scale)+8, (float) physicsTest.HEIGHT/(2*physicsTest.scale)), new Vector2(1, 1), 0, 0, physicsTest.WIDTH, physicsTest.HEIGHT, 0.75f, bigMass*25, physicsTest, false);
        /*for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                Vector2 position = new Vector2((float) j*(physicsTest.WIDTH-80)/(width*physicsTest.scale)+0.5f, (float) physicsTest.HEIGHT - i*(physicsTest.HEIGHT-80)/(height*physicsTest.scale)-0.5f);
                particles[i*height+j] = new GravitationalParticle(position, new Vector2(0, 0), 0, 0, physicsTest.WIDTH, physicsTest.HEIGHT, 0.5f, earthMass/(i*height+j+1), physicsTest);
            }
        }*/

    }

    public void update(){
        for (int i = 0; i < particles.length; i++){
            for (int j = i+1; j < particles.length; j++){
                GravitationalParticle p1 = particles[i];

                GravitationalParticle p2 = particles[j];

                if (p1.position.dst(p2.position) == 0){
                    continue;
                }

                Vector2 p1Dir = p2.position.cpy().sub(p1.position).nor();
                Vector2 p2Dir = p1.position.cpy().sub(p2.position).nor();
                float force = (float) (G * (p1.mass*p2.mass/(Math.pow(p1.position.dst(p2.position)*1000, 2))));
                p1.curForce.add(p1Dir.cpy().scl(force));
                p2.curForce.add(p2Dir.cpy().scl(force));
            }
        }
    }

    public void render(){



        for (GravitationalParticle particle : particles){
            physicsTest.shapeRenderer.setColor(particle.color.x, particle.color.y, particle.color.z, 1);
            particle.render(particles[0].position);
        }

        update();
    }
}
