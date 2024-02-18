package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class PhysicsTest extends ApplicationAdapter {
	public final int WIDTH = 800;
	public final int HEIGHT = 480;
	ShapeRenderer shapeRenderer;
	Particle particle;
	OrthographicCamera camera = new OrthographicCamera();
	FitViewport viewport;

	float scale = 80; // 80 pixels per meter

	Particle[] particles = new Particle[1];

	
	@Override
	public void create () {
		shapeRenderer = new ShapeRenderer();
		viewport = new FitViewport(WIDTH, HEIGHT, camera);
		camera.setToOrtho(false, WIDTH, HEIGHT);

		particles[0] = new Particle(WIDTH/(2f*scale), HEIGHT/(2f*scale), 3, 0, 0, 0, WIDTH/scale, HEIGHT/scale, 0.5f, 0.9f, 1f, this);
		// particles[1] = new Particle(WIDTH/(2f*scale), HEIGHT/(2f*scale)+ 2*scale, 3, 0, 0, 0, WIDTH/scale, HEIGHT/scale, 0.5f, 0.9f, 1f, this);
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 0);
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.setColor(0, 0, 1, 1);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);


		for (Particle particle : particles) {
			particle.update(Gdx.graphics.getDeltaTime());
			shapeRenderer.circle(particle.x*scale, particle.y*scale, particle.radius*scale, 100);
		}

		shapeRenderer.end();

	}
	
	@Override
	public void dispose () {
		shapeRenderer.dispose();


	}
}
