package com.myPackage;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public abstract class BirdClass {
    private World world;
    private Body body;
    private Texture texture;
    private float radius;

    public BirdClass(World world, float x, float y, float radius, String texturePath) {
        this.world = world;
        this.radius = radius;
        this.texture = new Texture(texturePath);

        createBody(x, y);
    }
    public void setTexture(String texturePath){
        this.texture = new Texture(texturePath);
    }

    public World getWorld() {
        return world;
    }
    public void setBody(Body newBody) {
        this.body = newBody;
    }


    public void setRadius(float r){
        this.radius = r;
    }



    private void createBody(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        // Create the body in the world
        body = world.createBody(bodyDef);

        // Define a circular shape
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        // Define fixture properties
        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = shape;
        fixtureDef.density = 100f; // Increase density to make the bird heavy
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0.3f;

        // Attach the fixture to the body
        body.createFixture(fixtureDef);

        // Clean up the shape
        shape.dispose();
    }

    public void render(SpriteBatch batch) {
        // Draw the bird's texture centered at the body's position
        Vector2 position = body.getPosition();
        float angleDegrees = (float) Math.toDegrees(body.getAngle());

        batch.draw(
            texture,
            position.x - radius,    // Center the texture horizontally
            position.y - radius,    // Center the texture vertically
            radius, radius,         // Origin for rotation (center)
            radius * 2, radius * 2, // Width and height
            1f, 1f,                 // Scale
            angleDegrees,           // Rotation
            0, 0,
            texture.getWidth(), texture.getHeight(),
            false, false
        );
    }

    public void dispose() {
        texture.dispose();
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public float getRadius() {
        return radius;
    }

    public void setPosition(float x, float y) {
        body.setTransform(x, y, body.getAngle());
        body.setLinearVelocity(0, 0);
        body.setAngularVelocity(0);
    }
}
