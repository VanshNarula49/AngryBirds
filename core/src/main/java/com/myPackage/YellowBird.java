package com.myPackage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class YellowBird extends BirdClass {

    public YellowBird(World world, float x, float y, float radius) {
        super(world, x, y, radius, Gdx.files.internal("birds/yellow.png").path());// Adjust the path as needed


    }


    public void activateAbility() {
        // Get the bird's current velocity
        Vector2 currentVelocity = getBody().getLinearVelocity();

        // Double the velocity
        Vector2 boostedVelocity = currentVelocity.scl(2);

        // Apply the new velocity
        getBody().setLinearVelocity(boostedVelocity);

        System.out.println("Special ability activated! Speed doubled.");
    }
}
