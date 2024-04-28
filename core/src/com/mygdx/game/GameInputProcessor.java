package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;

public class GameInputProcessor implements InputProcessor {
    PhysicsTest physicsTest;

    private Vector2 lastTouch = new Vector2();
    private Particle lastParticle;

    public GameInputProcessor(PhysicsTest physicsTest){
        this.physicsTest = physicsTest;
    }
    @Override
    public boolean touchDown (int x, int y, int pointer, int button) {

        if (button == Input.Buttons.LEFT) {
            Particle newParticle = new Particle(x/physicsTest.scale, (physicsTest.HEIGHT-y)/physicsTest.scale, 0, 0, 0, 0, physicsTest.WIDTH/physicsTest.scale, physicsTest.HEIGHT/physicsTest.scale, 0.5f, 0.6f, 1f, physicsTest);
            physicsTest.particles.add(newParticle);
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.P){
            physicsTest.paused = !physicsTest.paused;
        }
        return true;
    }

    public boolean keyUp (int keycode) {
        return false;
    }

    public boolean keyTyped (char character) {
        return false;
    }


    public boolean touchUp (int x, int y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean touchDragged (int x, int y, int pointer) {
        // check if mouse click is right mouse click

        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
            for (Particle p: physicsTest.particles){
                float adjustedX = x/physicsTest.scale - p.x;
                float adjustedY = (physicsTest.HEIGHT - y)/physicsTest.scale - p.y;

                if (adjustedX*adjustedX + adjustedY*adjustedY < p.radius*p.radius){
                    // mouse is in circle
                    if (p != lastParticle){
                        lastTouch.x = 0;
                        lastTouch.y = 0;
                        lastParticle = p;
                    }
                    // Check if lastTouch is not empty
                    if (!(lastTouch.x == 0 && lastTouch.y == 0)){
                        float dx = x/physicsTest.scale - lastTouch.x;
                        float dy = (physicsTest.HEIGHT - y)/physicsTest.scale - lastTouch.y;
                        float dt = Gdx.graphics.getDeltaTime();

                        Vector2 velocity = new Vector2(dx/dt, dy/dt);
                        p.velocityX = velocity.x;
                        p.velocityY = velocity.y;
                    }
                    lastTouch = new Vector2(x/physicsTest.scale, (physicsTest.HEIGHT - y)/physicsTest.scale);
                }

            }

        } else if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
            System.out.println("LEFT");
            Particle last = physicsTest.particles.get(physicsTest.particles.size()-1);
            last.velocityY = 0;
            last.velocityX = 0;
            float dx = x/physicsTest.scale - last.x;
            float dy = (physicsTest.HEIGHT - y)/physicsTest.scale - last.y;
            last.radius = (float) Math.sqrt(dx*dx + dy*dy);
            float MASS_SCALE = 2; // 2kg per 1 meter radius
            last.mass = last.radius*MASS_SCALE;
        }
        return true;
    }

    public boolean mouseMoved (int x, int y) {
        return false;
    }

    public boolean scrolled (float amountX, float amountY) {
        return false;
    }
}
