package com.myPackage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.Queue;


public class Level3Screen implements Screen {
    // World dimensions in meters
    private static final float WORLD_WIDTH = 16f;
    private static final float WORLD_HEIGHT = 9.6f;


    private Main game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;

    private Texture pauseButtonTexture;
    private Rectangle pauseButtonBounds;

    private World world; // Box2D world
    private Body groundBody;


    private BlackBird redBird;
    private YellowBird yellowBird;
    private BlackBird blackBird;
    private Queue<BirdClass> birdsQueue;
    private BirdClass currentBird;


    private Structure3 structure;


    private Texture groundTexture;
    private Texture bgSlingshotTexture; // Back Yolk and Body
    private Texture fSlingshotTexture;  // Front Yolk
    private Box2DDebugRenderer debugRenderer;
    private Texture backgroundTexture;
    private Array<PigClass> pigs;


    private float bgSlingshotX = 3f;
    private float bgSlingshotY = 2.4f;


    private float fSlingshotX = 2.4f; // Adjusted to align with back slingshot
    private float fSlingshotY = 2.2f;


    // Desired slingshot dimensions in world units
    private float desiredSlingshotWidth = 1.0f;
    private float desiredSlingshotHeight = 2.0f;


    // Bird initial position
    private Vector2 birdInitialPosition = new Vector2(2.3f, 1.8f);


    // Dragging variables
    private boolean isDragging = false;
    private Vector2 dragStart = new Vector2();
    private Vector2 dragCurrent = new Vector2();


    // Constraints
    private float maxAngle = 150f;       // Maximum launch angle in degrees
    private float minAngle = 30f;        // Minimum launch angle in degrees
    private float maxIntensity = 35f;    // Maximum launch strength
    private float minIntensity = 12f;    // Minimum launch strength


    // Slingshot base (Anchor point)
    private Body slingshotBaseBody;
    private MouseJoint mouseJoint;
    private WeldJoint birdJoint; // Joint to hold the bird in place
    // Trajectory renderer
    private ShapeRenderer shapeRenderer;

    private float timer = 40f; // Time limit in seconds
    private BitmapFont font; // To display the timer

    private int currentLevel=3;
    private float playerX, playerY;

    public Level3Screen(Main game) {
        this.game = game;
        initialize();

    }


    private void initialize() {
        // Initialize Box2D
        Box2D.init();


        // Create Box2D world with gravity
        world = new World(new Vector2(0, -9.8f), true);


        // Initialize debug renderer (optional)
        debugRenderer = new Box2DDebugRenderer();
        pauseButtonTexture = new Texture("Level1/pause.png");
        pauseButtonBounds = new Rectangle(20, 400, pauseButtonTexture.getWidth(), pauseButtonTexture.getHeight());
        // Set up camera and viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        camera.update();

        font = new BitmapFont(); // Default font
        font.getData().setScale(0.05f); // Scale the font size

        // Create SpriteBatch
        batch = new SpriteBatch();


        // Create ShapeRenderer for trajectory
        shapeRenderer = new ShapeRenderer();


        // Load textures
        groundTexture = new Texture(Gdx.files.internal("Level1/ground.png"));
        bgSlingshotTexture = new Texture(Gdx.files.internal("Level1/bgslingshot.png"));
        fSlingshotTexture = new Texture(Gdx.files.internal("Level1/fslingshot.png"));
        backgroundTexture = new Texture(Gdx.files.internal("Level1/bg_Level1.png")); // Replace with your background image path
        birdsQueue = new LinkedList<>();


        pigs = new Array<>();


        // Add pigs at specific positions
        PigClass pig1 = new PigClass(world,13.1f,1f,WORLD_WIDTH,WORLD_HEIGHT);
        PigClass pig2 = new PigClass(world,13.1f,4f,WORLD_WIDTH,WORLD_HEIGHT);
        pig2.setTexture("pigs/smallPig.png");
        pig1.setTexture("pigs/bigPig.png");
        pig2.setHealth(3000);
        pig1.setHealth(3000);
        pigs.add(pig1);
        pigs.add(pig2);




        // Create ground body
        createGroundBody();
        createWall(WORLD_WIDTH, WORLD_HEIGHT / 2f); // Right wall
        createWall(0, WORLD_HEIGHT / 2f);


        // Create slingshot base (anchor point)
        createSlingshotBase();


        // Create red bird
        redBird = new BlackBird(world, birdInitialPosition.x, birdInitialPosition.y, 0.5f);
        yellowBird = new YellowBird(world,birdInitialPosition.x, birdInitialPosition.y, 0.5f);
        blackBird = new BlackBird(world,birdInitialPosition.x, birdInitialPosition.y, 0.5f);
        birdsQueue.add(redBird);
        birdsQueue.add(yellowBird);
        birdsQueue.add(blackBird);
        // Attach the bird to the slingshot
//        attachBirdToSlingshot();


        if (!birdsQueue.isEmpty()) {
            loadNextBird();
        }
        else {
            currentBird = null; // No birds available
        }




        // Create structure
        createStructure();


        // Set input processor for handling mouse events
        Gdx.input.setInputProcessor(new InputHandler());






        // Consolidate ContactListener
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Object userDataA = contact.getFixtureA().getBody().getUserData();
                Object userDataB = contact.getFixtureB().getBody().getUserData();


                // Handle pig boundary collision
                if (userDataA instanceof PigClass || userDataB instanceof PigClass) {
                    PigClass pig = (userDataA instanceof PigClass) ? (PigClass) userDataA : (PigClass) userDataB;
                    Vector2 pigPosition = pig.getBody().getPosition();


                    // If pig is out of bounds, apply damage
                    if (pigPosition.x <= 5.0f || pigPosition.x >= WORLD_WIDTH ||
                        pigPosition.y <= 0 || pigPosition.y >= WORLD_HEIGHT) {
                        pig.takeDamage(1); // Example: take 1 damage for going out of bounds
                    }
                }
            }




            @Override
            public void endContact(Contact contact) {}


            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {}


            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
                Object userDataA = contact.getFixtureA().getBody().getUserData();
                Object userDataB = contact.getFixtureB().getBody().getUserData();


                if (userDataA instanceof PigClass || userDataB instanceof PigClass) {
                    PigClass pig = (userDataA instanceof PigClass) ? (PigClass) userDataA : (PigClass) userDataB;


                    float maxImpulse = 0f;
                    for (float normalImpulse : impulse.getNormalImpulses()) {
                        maxImpulse = Math.max(maxImpulse, normalImpulse);
                    }


                    int damage = (int) Math.min(maxImpulse * 10, 20);
                    pig.takeDamage(damage);
                }
            }
        });
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    // Getter for playerX
    public float getPlayerX() {
        return playerX;
    }

    // Getter for playerY
    public float getPlayerY() {
        return playerY;
    }

    private void createGroundBody() {
        // Define ground dimensions and position in meters
        float groundWidth = WORLD_WIDTH;
        float groundHeight = 1f;
        float groundX = (WORLD_WIDTH / 2f) + 3.5f; // Ground starts after slingshot
        float groundY = groundHeight / 2f;


        // Define body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(groundX, groundY);


        // Create ground body in the world
        groundBody = world.createBody(bodyDef);


        // Define shape for the ground
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(groundWidth / 2f, groundHeight / 2f);


        // Create fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0f;


        groundBody.createFixture(fixtureDef);


        // Dispose shape
        shape.dispose();
    }


    private void createSlingshotBase() {
        // Define slingshot base as a static body
        BodyDef baseBodyDef = new BodyDef();
        baseBodyDef.type = BodyDef.BodyType.StaticBody;
        baseBodyDef.position.set(bgSlingshotX, bgSlingshotY);


        slingshotBaseBody = world.createBody(baseBodyDef);


        // Optionally, add a small fixture if needed for the joint
        CircleShape baseShape = new CircleShape();
        baseShape.setRadius(0.1f); // Small radius


        FixtureDef baseFixtureDef = new FixtureDef();
        baseFixtureDef.shape = baseShape;
        baseFixtureDef.isSensor = true; // Make it a sensor to avoid physical collisions


        slingshotBaseBody.createFixture(baseFixtureDef);


        // Dispose shape
        baseShape.dispose();
    }


    /**
     * Attaches the bird to the slingshot using a WeldJoint.
     */
//    private void attachBirdToSlingshot() {
//        WeldJointDef jointDef = new WeldJointDef();
//        jointDef.bodyA = slingshotBaseBody;
//        jointDef.bodyB = redBird.getBody();
//        jointDef.localAnchorA.set(0, 0);
//        jointDef.localAnchorB.set(0, 0);
//        birdJoint = (WeldJoint) world.createJoint(jointDef);
//    }






    private void createStructure() {
        // Create a custom structure
        structure = new Structure3(world, "obstacles/images.jpeg"); // Adjust the path as needed
    }


    @Override
    public void render(float delta) {


        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (currentBird instanceof BlackBird) {
                ((BlackBird) currentBird).activateAbility(); // Activate BlackBird's ability
            }
        }
        timer -= delta;

        // Check if time has run out
        if (timer <= 0) {
            game.setScreen(new LoseScreen(game,3)); // Transition to the LoseScreen
            return; // Exit the render method to avoid further processing
        }

        // Step the Box2D world
        world.step(delta, 6, 2);


        // Clear screen with black background
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        // Update camera
        camera.update();


        // Render ground, slingshot, bird, and structure
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);


        // Draw the ground texture
        batch.draw(groundTexture, 0, 0, WORLD_WIDTH, 1f);


        // Draw the back slingshot (centered)
        batch.draw(
            bgSlingshotTexture,
            bgSlingshotX - desiredSlingshotWidth / 2f,
            bgSlingshotY - desiredSlingshotHeight / 2f,
            desiredSlingshotWidth,
            desiredSlingshotHeight
        );


        // Draw the red bird
//        redBird.render(batch);
        if (currentBird != null) {
            currentBird.render(batch);
        }




        // Draw the front slingshot (centered) over the bird
        batch.draw(
            fSlingshotTexture,
            fSlingshotX - desiredSlingshotWidth / 2f,
            fSlingshotY - desiredSlingshotHeight / 2f,
            desiredSlingshotWidth,
            desiredSlingshotHeight
        );


        // Render pigs
        for (PigClass pig : pigs) {
            pig.render(batch);
        }


        // Draw the structure
        structure.render(batch);
        batch.draw(pauseButtonTexture, pauseButtonBounds.x, pauseButtonBounds.y);


        // Adjust font size
//        font.getData().setScale(0.05f); // Reduced scale to fit better
//        font.setColor(1f, 1f, 1f, 1f); // White color
//
//        String timerText = MathUtils.ceil(timer) + "s";
//        font.draw(batch, timerText, 10f, viewport.getWorldHeight()-1 );

        font.getData().setScale(0.1f); // Adjust font size
        font.setColor(1f, 1f, 1f, 1f); // Set font color to white
        String timerText = String.valueOf(MathUtils.ceil(timer));
        float timerX = WORLD_WIDTH - 3.7f; // Adjust position to shift timer to the right
        float timerY = WORLD_HEIGHT - 0.7f; // Adjust position to move timer slightly down
        font.draw(batch, timerText, timerX, timerY);

        batch.end();




        // Draw the health bars for the pigs using ShapeRenderer
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (PigClass pig : pigs) {
            pig.renderHealthBar(shapeRenderer);
        }
        shapeRenderer.end();


        // Draw trajectory and elastic bands while dragging
        if (isDragging) {
            drawTrajectory();
            drawElasticBands();
        }


        // Update and remove dead pigs
        for (int i = pigs.size - 1; i >= 0; i--) {
            PigClass pig = pigs.get(i);
            pig.update();
            if (pig.isDead()) {
                pigs.removeIndex(i);
            }
        }


        if (checkWinCondition()) {
            game.setScreen(new WinScreen(game,3)); // Transition to the win screen
        }





        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();
            if (pauseButtonBounds.contains(touchX, touchY)) {
                game.setScreen(new PauseScreen(game,this)); // Switch to PauseScreen
            }
        }
        resetBirdIfNeeded();
}


    private boolean checkWinCondition() {
        return pigs.size == 0;
    }

    /**
     * Draws the predicted trajectory of the bird based on the drag displacement.
     */

    private void drawTrajectory() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line); // Use Line for better visibility
        shapeRenderer.setColor(1, 0, 0, 1); // Red color for trajectory


        // Calculate launch parameters based on drag displacement
        Vector2 displacement = dragStart.cpy().sub(dragCurrent);
        float angleDegrees = displacement.angle();
        float intensity = MathUtils.clamp(displacement.len() * 5f, minIntensity, maxIntensity);


        // Calculate initial velocity
        float velocityMagnitude = intensity;
        float angleRadians = MathUtils.degreesToRadians * angleDegrees;
        Vector2 velocity = new Vector2(
            velocityMagnitude * MathUtils.cos(angleRadians),
            velocityMagnitude * MathUtils.sin(angleRadians)
        );


        // Simulate trajectory
        Vector2 position = currentBird.getPosition().cpy();
        Vector2 gravity = world.getGravity().cpy();


        float timeStep = 0.05f;
        int steps = 60; // Number of steps to simulate


        Vector2 previousPoint = position.cpy();


        for (int i = 1; i <= steps; i++) {
            float t = i * timeStep;
            Vector2 futurePosition = new Vector2(
                position.x + velocity.x * t,
                position.y + velocity.y * t + 0.5f * gravity.y * t * t
            );


            // Draw a line segment from previousPoint to futurePosition
            shapeRenderer.line(previousPoint, futurePosition);


            previousPoint = futurePosition.cpy();
        }


        shapeRenderer.end();
    }


    /**
     * Draws the elastic bands of the slingshot.
     */
    private void drawElasticBands() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.545f, 0.271f, 0.075f, 1); // Brown color for elastic bands


        // Draw left elastic band
        shapeRenderer.rectLine(
            slingshotBaseBody.getPosition().x - 0.1f, slingshotBaseBody.getPosition().y + 0.2f,
            redBird.getPosition().x, redBird.getPosition().y,
            0.05f
        );


        // Draw right elastic band
        shapeRenderer.rectLine(
            slingshotBaseBody.getPosition().x + 0.1f, slingshotBaseBody.getPosition().y + 0.2f,
            redBird.getPosition().x, redBird.getPosition().y,
            0.05f
        );


        shapeRenderer.end();
    }


    /**
     * Resets the bird if it goes out of bounds or its velocity is below a threshold.
     */


    private void resetBirdIfNeeded() {
        Vector2 birdPos = redBird.getPosition();
        if (birdPos.x < 0 || birdPos.x > WORLD_WIDTH || birdPos.y < 0 || birdPos.y > WORLD_HEIGHT) {
            resetBird();
        }


        // Optionally, reset if bird's velocity is below a threshold and it's not being dragged
        if (!isDragging && mouseJoint == null && redBird.getBody().getLinearVelocity().len() < 0.1f) {
            resetBird();
        }
    }


    private void attachBirdToSlingshot() {
        if (currentBird != null) {
            WeldJointDef jointDef = new WeldJointDef();
            jointDef.bodyA = slingshotBaseBody; // The static slingshot base
            jointDef.bodyB = currentBird.getBody(); // The bird's dynamic body
            jointDef.localAnchorA.set(0, 0);
            jointDef.localAnchorB.set(0, 0);
            birdJoint = (WeldJoint) world.createJoint(jointDef);
        }


    }




    private void loadNextBird() {
        if (!birdsQueue.isEmpty()) {
            currentBird = birdsQueue.poll(); // Get the next bird
            currentBird.getBody().setTransform(birdInitialPosition.x, birdInitialPosition.y, 0);
            attachBirdToSlingshot(); // Attach it to the slingshot
        } else {
            currentBird = null; // No birds left
            System.out.println("No birds left in the queue.");
            // Optionally handle end-of-level logic here
        }
        System.out.println("Current bird: " + (currentBird != null ? "Available" : "None"));
    }

    private void resetBird() {
        // Destroy existing bird body
        world.destroyBody(redBird.getBody());

        // Create a new bird
        redBird = new BlackBird(world, birdInitialPosition.x, birdInitialPosition.y, 0.5f);
        // Reattach the bird to the slingshot
        attachBirdToSlingshot();


        // Set isDragging to false
        isDragging = false;





    }
    private void createWall(float wallX, float wallY) {
        // Define wall dimensions
        float wallWidth = 0.1f; // Thin wall
        float wallHeight = WORLD_HEIGHT;

        // Define body
        BodyDef wallBodyDef = new BodyDef();
        wallBodyDef.type = BodyDef.BodyType.StaticBody;
        wallBodyDef.position.set(wallX, wallY);

        // Create wall body in the world
        Body wallBody = world.createBody(wallBodyDef);

        // Define shape for the wall
        PolygonShape wallShape = new PolygonShape();
        wallShape.setAsBox(wallWidth / 2f, wallHeight / 2f);

        // Create fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = wallShape;
        fixtureDef.friction = 0.5f; // Optional friction
        fixtureDef.restitution = 0f; // No bounce

        wallBody.createFixture(fixtureDef);

        // Dispose of the shape
        wallShape.dispose();
    }
//    private void resetBirdIfNeeded() {
//        if (currentBird != null) {
//            Vector2 birdPos = currentBird.getPosition();
//
//            // Check if the bird is out of bounds
//            if (birdPos.x < 0 || birdPos.x > WORLD_WIDTH || birdPos.y < 0 || birdPos.y > WORLD_HEIGHT) {
//                slingshotEmpty = true; // Mark slingshot as empty
//            }
//
//            // Check if the bird is stationary and not being dragged
//            if (!isDragging && mouseJoint == null && currentBird.getBody().getLinearVelocity().len() < 0.1f) {
//                slingshotEmpty = true; // Mark slingshot as empty
//            }
//
//            // Transition to the next bird if the slingshot is empty
//            if (slingshotEmpty) {
//                resetBird();
//            }
//        }
//    }
//
//
//
//    private void resetBird() {
//        if (currentBird != null) {
//            // Destroy the current bird's body
//            world.destroyBody(currentBird.getBody());
//            currentBird.dispose();
//        }
//
//        // Load the next bird from the queue
//        if (!birdsQueue.isEmpty()) {
//            currentBird = birdsQueue.poll();
//            System.out.println("Loaded bird: " + currentBird.getClass().getSimpleName());
//
//            // Set bird to the initial slingshot position
//            currentBird.setPosition(birdInitialPosition.x, birdInitialPosition.y);
//            attachBirdToSlingshot(); // Attach the new bird to the slingshot
//
//            slingshotEmpty = false; // Mark slingshot as not empty
//        } else {
//            // No birds left: End game
//            currentBird = null;
//            System.out.println("No birds left. Game Over!");
//        }
//
//        isDragging = false;
//    }






    /**
     * Resets the bird to its initial position and reattaches it to the slingshot.
     */








    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true); // Recalculate viewport
    }


    @Override
    public void dispose() {
        batch.dispose();
        world.dispose();
        redBird.dispose();
        structure.dispose();
        groundTexture.dispose();
        bgSlingshotTexture.dispose();
        fSlingshotTexture.dispose();
        debugRenderer.dispose();
        shapeRenderer.dispose();
        backgroundTexture.dispose();
        font.dispose();


    }


    // Other overridden methods can remain empty if not used
    @Override public void show() {
        GameState loadedGameState = loadGameState();
        if (loadedGameState != null) {
            currentLevel = loadedGameState.getLevel();
            playerX = loadedGameState.getPlayerX();
            playerY = loadedGameState.getPlayerY();
        } else {
            // Start new game state if no saved game state
            currentLevel = 1;
            playerX = 100f;  // Initial player position
            playerY = 200f;
        }
    }


    private GameState loadGameState() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("gameState.ser"))) {
            return (GameState) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}


    /**
     * Handles input events for dragging and launching the bird.
     */
    private class InputHandler extends InputAdapter {
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            if (currentBird == null) {
                System.out.println("No bird available to interact with.");
                return false;
            }


            Vector2 worldCoords = screenToWorld(screenX, screenY);
            Vector2 birdPosition = currentBird.getPosition();


            if (worldCoords.dst(birdPosition) <= currentBird.getRadius() * 2f) {
                isDragging = true;


                // Destroy the WeldJoint if it exists
                if (birdJoint != null) {
                    world.destroyJoint(birdJoint);
                    birdJoint = null;
                }


                // Create a MouseJoint to drag the bird
                MouseJointDef mouseJointDef = new MouseJointDef();
                mouseJointDef.bodyA = groundBody;
                mouseJointDef.bodyB = redBird.getBody();
                mouseJointDef.collideConnected = true;
                mouseJointDef.target.set(worldCoords); // Set to touch position
                mouseJointDef.maxForce = 1000.0f * currentBird.getBody().getMass();


                mouseJoint = (MouseJoint) world.createJoint(mouseJointDef);


                // Record the initial drag position
                dragStart.set(worldCoords);
                dragCurrent.set(worldCoords);


                return true;
            }
            return false;
        }


        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            if (isDragging && mouseJoint != null) {
                Vector2 worldCoords = screenToWorld(screenX, screenY);


                // Limit dragging area
                float maxDragDistance = 3.0f;
                Vector2 dragVector = worldCoords.cpy().sub(birdInitialPosition);
                if (dragVector.len() > maxDragDistance) {
                    dragVector.setLength(maxDragDistance);
                    worldCoords = birdInitialPosition.cpy().add(dragVector);
                }


                mouseJoint.setTarget(worldCoords);


                // Update current drag position
                dragCurrent.set(worldCoords);


                return true;
            }
            return false;
        }


        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            if (isDragging && mouseJoint != null) {
                isDragging = false;


                // Destroy the MouseJoint to release the bird
                world.destroyJoint(mouseJoint);
                mouseJoint = null;


                // Calculate launch parameters based on drag displacement
                Vector2 displacement = dragStart.cpy().sub(dragCurrent);
                float angleDegrees = displacement.angle();
                float intensity = MathUtils.clamp(displacement.len() * 5f, minIntensity, maxIntensity);


                // Calculate initial velocity
                float velocityMagnitude = intensity;
                float angleRadians = MathUtils.degreesToRadians * angleDegrees;
                Vector2 velocity = new Vector2(
                    velocityMagnitude * MathUtils.cos(angleRadians),
                    velocityMagnitude * MathUtils.sin(angleRadians)
                );


                // Apply velocity to the bird
                currentBird.getBody().setLinearVelocity(velocity);


                return true;
            }
            return false;
        }


        /**
         * Converts screen coordinates to world coordinates.
         */
        private Vector2 screenToWorld(int screenX, int screenY) {
            Vector3 touchPos = new Vector3(screenX, screenY, 0);
            camera.unproject(touchPos, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
            return new Vector2(touchPos.x, touchPos.y);
        }


    }


}
