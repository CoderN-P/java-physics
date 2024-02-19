package com.mygdx.game;
import java.util.ArrayList;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static java.lang.Math.sqrt;

public class PhysicsTest extends ApplicationAdapter {
	public final int WIDTH = 800;
	public final int HEIGHT = 480;
	ShapeRenderer shapeRenderer;
	Particle particle;
	OrthographicCamera camera = new OrthographicCamera();
	FitViewport viewport;

	float scale = 80; // 80 pixels per meter
	public boolean paused = false;

	ArrayList<Particle> particles = new ArrayList<Particle>();

	
	@Override
	public void create () {
		GameInputProcessor inputProcessor = new GameInputProcessor(this);
		Gdx.input.setInputProcessor(inputProcessor);
		shapeRenderer = new ShapeRenderer();
		viewport = new FitViewport(WIDTH, HEIGHT, camera);
		camera.setToOrtho(false, WIDTH, HEIGHT);


	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 0);
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.setColor(0, 0, 1, 1);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

		// Draw particles



			for (int i = 0; i < particles.size(); i++) {
				if (!paused){
					for (int j = i + 1; j < particles.size(); j++) {
						Particle cur = particles.get(i);
						Particle p = particles.get(j);
						float dx = cur.x - p.x;
						float dy = cur.y - p.y;
						double d = sqrt(dx * dx + dy * dy);

						if (d <= cur.radius + p.radius) {
							// m1 = mass, m2 = p.mass

							// Calculate angles
							double vtheta = Math.atan2(cur.velocityY, cur.velocityX);
							double dtheta = Math.atan2(dy, dx);

							// Calculate relative velocity
							double vrel = (cur.velocityX - p.velocityX) * (dx) + (cur.velocityY - p.velocityY) * (dy);

							// Conservation of linear momentum
							double m1 = cur.mass;
							double m2 = p.mass;
							double newVrel = p.bounceCoefficient * (2 * m2 * vrel) / (m1 + m2);

							// Update velocities for linear momentum conservation
							cur.velocityX -= (float) (newVrel * Math.cos(dtheta));
							cur.velocityY -= (float) (newVrel * Math.sin(dtheta));
							p.velocityX += (float) (newVrel * Math.cos(dtheta));
							p.velocityY += (float) (newVrel * Math.sin(dtheta));

							// Conservation of kinetic energy
							double curKE = 0.5 * m1 * (cur.velocityX * cur.velocityX + cur.velocityY * cur.velocityY);
							double pKE = 0.5 * m2 * (p.velocityX * p.velocityX + p.velocityY * p.velocityY);

							double totalKE_before = (curKE + pKE);

							// Update velocities for kinetic energy conservation
							double curFactor = 0.5 * cur.mass * newVrel * newVrel / curKE;
							double pFactor = 0.5 * p.mass * newVrel * newVrel / pKE;

							cur.velocityX *= curFactor;
							cur.velocityY *= curFactor;

							p.velocityX *= pFactor;
							p.velocityY *= pFactor;

							double totalKE_after = 0.5 * m1 * (cur.velocityX * cur.velocityX + cur.velocityY * cur.velocityY)
									+ 0.5 * m2 * (p.velocityX * p.velocityX + p.velocityY * p.velocityY);

							// Adjust velocities to ensure conservation of kinetic energy
							double keFactor = Math.sqrt(totalKE_before / totalKE_after);
							cur.velocityX *= keFactor;
							cur.velocityY *= keFactor;
							p.velocityX *= keFactor;
							p.velocityY *= keFactor;


						}
					}


					particles.get(i).update(Gdx.graphics.getDeltaTime());

				}

				shapeRenderer.circle(particles.get(i).x*scale, particles.get(i).y*scale, particles.get(i).radius*scale, 100);
		}

		shapeRenderer.end();

	}
	
	@Override
	public void dispose () {
		shapeRenderer.dispose();


	}
}
