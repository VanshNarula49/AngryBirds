package com.myPackage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class WoodBlock {
    private World world;
    private Body body;
    private Texture texture;
    private float width;
    private float height;

    public WoodBlock(World world, float x, float y, float width, float height, float rotationDegrees, String texturePath) {
        this.world = world;
        this.width = width;
        this.height = height;

        // Load the wood block texture
        this.texture = new Texture(Gdx.files.internal(texturePath));

        createBody(x, y, rotationDegrees);
    }

    private void createBody(float x, float y, float rotationDegrees) {
        // Convert rotation to radians for Box2D
        float rotationRadians = (float) Math.toRadians(rotationDegrees);

        // Define body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody; // Dynamic body allows movement
        bodyDef.position.set(x, y);
        bodyDef.angle = rotationRadians;

        // Create body in the world
        body = world.createBody(bodyDef);

        // Define shape as a rectangle
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2f, height / 2f);

        // Create fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f; // Adjust density as needed
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.5f; // Slight bounce

        body.createFixture(fixtureDef);

        // Clean up shape
        shape.dispose();
    }

    public void render(SpriteBatch batch) {
        Vector2 position = body.getPosition();
        float angleDegrees = (float) Math.toDegrees(body.getAngle());

        batch.draw(
            texture,
            position.x - width / 2f,
            position.y - height / 2f,
            width / 2f, height / 2f, // Origin for rotation (center)
            width, height,
            1f, 1f,                  // Scale
            angleDegrees,            // Rotation angle in degrees
            0, 0,
            texture.getWidth(), texture.getHeight(),
            false, false
        );
    }

    public void dispose() {
        texture.dispose();
        world.destroyBody(body);
    }
}
