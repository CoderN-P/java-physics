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
	public int c = 0;
	ShapeRenderer shapeRenderer;
	Particle particle;
	OrthographicCamera camera = new OrthographicCamera();
	FitViewport viewport;

	float scale = 40; // 80 pixels per meter
	public boolean paused = false;

	ArrayList<Particle> particles = new ArrayList<Particle>();

	public GravitationalSystem gravitationalSystem;

	public SoftbodyMass[] masses = new SoftbodyMass[0];

	
	@Override
	public void create () {
		GameInputProcessor inputProcessor = new GameInputProcessor(this);
		Gdx.input.setInputProcessor(inputProcessor);
		shapeRenderer = new ShapeRenderer();
		viewport = new FitViewport(WIDTH, HEIGHT, camera);
		camera.setToOrtho(false, WIDTH, HEIGHT);


		gravitationalSystem = new GravitationalSystem(2, 1, this);
		// masses[0] = new SoftbodyMass(1f, 2, WIDTH/(2*scale), HEIGHT/(2*scale)-1, this);

		// masses[1] = new SoftbodyMass(1f, 2, WIDTH/(2*scale), HEIGHT/(2*scale)+2, this);

	}

	@Override
	public void render () {

		ScreenUtils.clear(0, 0, 0, 0);
		shapeRenderer.setProjectionMatrix(camera.combined);

		shapeRenderer.setColor(0, 0, 1, 1);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);


		for (int i = 0; i < particles.size(); i++) {
			if (!paused){
				for (int j = i + 1; j < particles.size(); j++) {
					Particle p1 = particles.get(i);
					Particle p2 = particles.get(j);

					float dx = (p1.x - p2.x);
					float dy = (p1.y - p2.y);
					float d = (float) sqrt(dx*dx + dy*dy);

					if (d <= p1.radius + p2.radius){
						float m1 = p1.mass;
						float m2 = p2.mass;

						Vector2 vrel = new Vector2(p1.velocityX-p2.velocityX, p1.velocityY-p2.velocityY);
						Vector2 dir = new Vector2(dx, dy);
						double dot = vrel.dot(dir.nor());

						float changeX = (float) (2*m2/(m1 + m2)*dot*dir.x);
						float changeY = (float) (2*m2/(m1 + m2)*dot*dir.y);
						float newV1X = p1.velocityX - changeX;
						float newV1Y = p1.velocityY - changeY;

						float newV2X = p2.velocityX + changeX;
						float newV2Y = p2.velocityY + changeY;

						p1.velocityX = newV1X;
						p1.velocityY = newV1Y;
						p2.velocityX = newV2X;
						p2.velocityY = newV2Y;
					}
				}


				particles.get(i).update(Gdx.graphics.getDeltaTime());

			}
			Particle p = particles.get(i);
			shapeRenderer.setColor(p.color.x, p.color.y, p.color.z, 1);
			shapeRenderer.circle(p.x*scale, p.y*scale, p.radius*scale, 100);
		}

		// Draw particles
        for (SoftbodyMass mass : masses) {
            mass.update(paused);
        }


		gravitationalSystem.render();






		shapeRenderer.end();

	}
	
	@Override
	public void dispose () {
		shapeRenderer.dispose();


	}
}



// Code for rigid body collisions


