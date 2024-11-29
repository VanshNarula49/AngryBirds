//package com.myPackage;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.physics.box2d.*;
//
////public class BlackBird extends BirdClass {
////
////
////    public BlackBird(World world, float x, float y, float radius) {
////        super(world, x, y, radius, Gdx.files.internal("birds/black.png").path());// Adjust the path as needed
////
////    }
////}
//
//
//public class BlackBird extends BirdClass {
//    private boolean abilityActivated = false;
//    private float explosionRadius = 3f; // Radius of the explosion effect
//    private float explosionForce = 100f; // Force applied to nearby objects during the explosion
//
//    public BlackBird(World world, float x, float y, float radius) {
//        super(world, x, y, radius, Gdx.files.internal("birds/black.png").path());
//    }
//
//    // Special ability: Explode and apply a shockwave to nearby objects
//    public void activateAbility() {
//        if (!abilityActivated) {
//            abilityActivated = true;
//            explode();
//            System.out.println("Special ability activated! Black Bird exploded.");
//        }
//    }
//
//    // Explosion logic
//    private void explode() {
//        Vector2 explosionCenter = getBody().getPosition();
//
//        // Query all bodies in the explosion radius
//        getWorld().QueryAABB(
//            (fixture) -> {
//                // Check if the fixture belongs to a body that should be affected by the explosion
//                Body body = fixture.getBody();
//                if (body != getBody() && body.getType() == BodyDef.BodyType.DynamicBody) {
//                    applyExplosionForce(body, explosionCenter);
//                }
//                return true; // Continue querying
//            },
//            explosionCenter.x - explosionRadius, explosionCenter.y - explosionRadius,
//            explosionCenter.x + explosionRadius, explosionCenter.y + explosionRadius
//        );
//
//        // Destroy the Black Bird's body after the explosion
//        getWorld().destroyBody(getBody());
//    }
//
//    // Apply explosion force to a body
//    private void applyExplosionForce(Body body, Vector2 explosionCenter) {
//        Vector2 bodyPosition = body.getPosition();
//        Vector2 forceDirection = bodyPosition.cpy().sub(explosionCenter).nor();
//        float distance = explosionCenter.dst(bodyPosition);
//
//        // Calculate the force magnitude based on distance (closer = stronger)
//        float forceMagnitude = explosionForce / (distance * distance);
//
//        // Apply the force to the body
//        body.applyLinearImpulse(forceDirection.scl(forceMagnitude), bodyPosition, true);
//        System.out.println("Explosion force applied to body at: " + bodyPosition);
//    }
//}
package com.myPackage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class BlackBird extends BirdClass {
    private boolean abilityActivated = false;
    private float explosionRadius = 3f; // Radius of the explosion effect
    private float explosionForce = 100f; // Force applied to nearby objects during the explosion
    private Vector2 initialPosition; // Store the initial slingshot position

    public BlackBird(World world, float x, float y, float radius) {
        super(world, x, y, radius, Gdx.files.internal("birds/black.png").path());
        initialPosition = new Vector2(x, y); // Save the slingshot position
    }

    // Special ability: Explode and reset
    public void activateAbility() {
        if (!abilityActivated) {
            abilityActivated = true;
            explode();
            System.out.println("Special ability activated! Black Bird exploded.");
        }
    }

    // Explosion logic
    private void explode() {
        Vector2 explosionCenter = getBody().getPosition();

        // Query all bodies in the explosion radius
        getWorld().QueryAABB(
            (fixture) -> {
                // Check if the fixture belongs to a body that should be affected by the explosion
                Body body = fixture.getBody();
                if (body != getBody() && body.getType() == BodyDef.BodyType.DynamicBody) {
                    applyExplosionForce(body, explosionCenter);
                }
                return true; // Continue querying
            },
            explosionCenter.x - explosionRadius,
            explosionCenter.y - explosionRadius,
            explosionCenter.x + explosionRadius,
            explosionCenter.y + explosionRadius
        );

        // Destroy the current bird body
        getWorld().destroyBody(getBody());

        // Reset a new Black Bird at the slingshot position
        resetBlackBird();
    }

    // Apply explosion force to nearby bodies
    private void applyExplosionForce(Body body, Vector2 explosionCenter) {
        Vector2 direction = body.getPosition().sub(explosionCenter).nor();
        float distance = body.getPosition().dst(explosionCenter);
        float force = explosionForce / (distance + 1); // Reduce force with distance
        body.applyLinearImpulse(direction.scl(force), body.getWorldCenter(), true);
    }

    // Reset the bird to its initial position
    private void resetBlackBird() {
        // Create a new Black Bird at the initial position
        BlackBird newBlackBird = new BlackBird(getWorld(), initialPosition.x, initialPosition.y, getRadius());
        setBody(newBlackBird.getBody()); // Replace the current bird's body with the new one
        abilityActivated = false; // Reset ability
    }

    // Set a new body for the bird
    public void setBody(Body newBody) {
        // Destroy the current body if it exists
        if (getBody() != null) {
            getWorld().destroyBody(getBody());
        }
        // Assign the new body
        super.setBody(newBody);
    }
}
