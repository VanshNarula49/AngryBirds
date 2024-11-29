package com.myPackage;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class PigClass {
    private Body body;              // Box2D body for the pig
    private int health = 1000;      // Initial health
    private int maxHealth = health;   // Maximum health for reference
    private Texture texture;        // Texture for the pig
    private boolean isDead = false; // Flag to track if pig is alive
    private float radius = 0.5f;    // Radius of the pig in meters
    private float worldWidth;       // Width of the game world
    private float worldHeight;      // Height of the game world

    public PigClass(World world, float x, float y, float worldWidth, float worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        // Load pig texture
        texture = new Texture("./pigs/bigPig.png");

        // Define the body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        // Create the body in the world
        body = world.createBody(bodyDef);

        // Define the shape
        CircleShape shape = new CircleShape();
        shape.setRadius(radius); // Set radius in meters

        // Define the fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f; // Moderate density
        fixtureDef.friction = 100000000000f; // Friction for stability
        fixtureDef.restitution = 0.2f; // Optional: Adjust for bounciness

        // Attach the fixture to the body
        body.createFixture(fixtureDef);

        // Clean up shape
        shape.dispose();

        // Set user data for collision detection
        body.setUserData(this);
    }

    public void setTexture(String texturePath){
        this.texture = new Texture(texturePath);
    }
    public void takeDamage(int damage) {
        // Reduce health by damage
        health -= damage;
        System.out.println("Pig health: " + health);

        // Check if pig is dead
        if (health <= 0) {
            isDead = true;
        }
    }

    public void update() {
        if (isDead) {
            // If pig is dead, remove the body from the world
            if (body != null && body.getWorld() != null) {
                body.getWorld().destroyBody(body);
                body = null;
            }
        } else {
            // Clamp position to keep the pig within screen bounds
            Vector2 position = body.getPosition();
            float clampedX = MathUtils.clamp(position.x, radius, worldWidth - radius);
            float clampedY = MathUtils.clamp(position.y, radius, worldHeight - radius);
            if (position.x != clampedX || position.y != clampedY) {
                body.setTransform(clampedX, clampedY, body.getAngle());
                body.setLinearVelocity(0, 0); // Stop movement when clamped
            }
        }
    }

    public void render(SpriteBatch batch) {
        if (!isDead && body != null) {
            // Draw the pig texture at the body’s position
            float x = body.getPosition().x - radius; // Center the texture
            float y = body.getPosition().y - radius;
            batch.draw(texture, x, y, radius * 2, radius * 2); // Scale to fit the body
        }
    }

    // Render the health bar above the pig
    public void renderHealthBar(ShapeRenderer shapeRenderer) {
        if (!isDead && body != null) {
            Vector2 position = body.getPosition();
            float healthPercentage = (float) health / maxHealth; // Calculate health as a percentage
            float healthBarWidth = 1f; // Width of the health bar in meters
            float healthBarHeight = 0.3f; // Height of the health bar in meters
            float x = position.x - healthBarWidth / 2; // Position the health bar centered above the pig
            float y = position.y + radius + 0.2f; // Slightly above the pig's body

            // Draw the background of the health bar (in gray)
            shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1); // Gray color
            shapeRenderer.rect(x, y, healthBarWidth, healthBarHeight);

            // Draw the filled part of the health bar (in green to red based on health)
            shapeRenderer.setColor(1 - healthPercentage, healthPercentage, 0, 1); // Gradient from red to green
            shapeRenderer.rect(x, y, healthBarWidth * healthPercentage, healthBarHeight);
        }
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isDead() {
        return isDead;
    }

    public void dispose() {
        texture.dispose();
    }

    public Body getBody() {
        return body;
    }
}
