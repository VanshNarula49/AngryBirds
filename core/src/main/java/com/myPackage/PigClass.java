package com.myPackage;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class PigClass {
    private Body body;
    private int health = 1000;
    private int maxHealth = health;
    private Texture texture;
    private boolean isDead = false;
    private float radius = 0.5f;
    private float worldWidth;
    private float worldHeight;

    public PigClass(World world, float x, float y, float worldWidth, float worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;


        texture = new Texture("./pigs/bigPig.png");


        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);


        body = world.createBody(bodyDef);


        CircleShape shape = new CircleShape();
        shape.setRadius(radius);


        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 100000000000f;
        fixtureDef.restitution = 0.2f;


        body.createFixture(fixtureDef);


        shape.dispose();


        body.setUserData(this);
    }

    public void setTexture(String texturePath){
        this.texture = new Texture(texturePath);
    }
    public void takeDamage(int damage) {

        health -= damage;
        System.out.println("Pig health: " + health);


        if (health <= 0) {
            isDead = true;
        }
    }

    public void update() {
        if (isDead) {

            if (body != null && body.getWorld() != null) {
                body.getWorld().destroyBody(body);
                body = null;
            }
        } else {

            Vector2 position = body.getPosition();
            float clampedX = MathUtils.clamp(position.x, radius, worldWidth - radius);
            float clampedY = MathUtils.clamp(position.y, radius, worldHeight - radius);
            if (position.x != clampedX || position.y != clampedY) {
                body.setTransform(clampedX, clampedY, body.getAngle());
                body.setLinearVelocity(0, 0);
            }
        }
    }

    public void render(SpriteBatch batch) {
        if (!isDead && body != null) {

            float x = body.getPosition().x - radius;
            float y = body.getPosition().y - radius;
            batch.draw(texture, x, y, radius * 2, radius * 2);
        }
    }


    public void renderHealthBar(ShapeRenderer shapeRenderer) {
        if (!isDead && body != null) {
            Vector2 position = body.getPosition();
            float healthPercentage = (float) health / maxHealth;
            float healthBarWidth = 1f;
            float healthBarHeight = 0.3f;
            float x = position.x - healthBarWidth / 2;
            float y = position.y + radius + 0.2f;


            shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1);
            shapeRenderer.rect(x, y, healthBarWidth, healthBarHeight);


            shapeRenderer.setColor(1 - healthPercentage, healthPercentage, 0, 1);
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
