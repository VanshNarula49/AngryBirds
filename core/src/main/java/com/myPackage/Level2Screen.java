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
import java.util.LinkedList;
import java.util.Queue;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Level2Screen implements Screen {

    private static final float WORLD_WIDTH = 16f;
    private static final float WORLD_HEIGHT = 9.6f;


    private Main game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;

    private Texture pauseButtonTexture;
    private Rectangle pauseButtonBounds;

    private World world;
    private Body groundBody;


    private YellowBird redBird;
    private YellowBird yellowBird;
    private BlackBird blackBird;
    private Queue<BirdClass> birdsQueue;
    private BirdClass currentBird;


    private Structure2 structure;


    private Texture groundTexture;
    private Texture bgSlingshotTexture;
    private Texture fSlingshotTexture;
    private Box2DDebugRenderer debugRenderer;
    private Texture backgroundTexture;
    private Array<PigClass> pigs;


    private float bgSlingshotX = 3f;
    private float bgSlingshotY = 2.4f;


    private float fSlingshotX = 2.4f;
    private float fSlingshotY = 2.2f;



    private float desiredSlingshotWidth = 1.0f;
    private float desiredSlingshotHeight = 2.0f;



    private Vector2 birdInitialPosition = new Vector2(2.3f, 1.8f);



    private boolean isDragging = false;
    private Vector2 dragStart = new Vector2();
    private Vector2 dragCurrent = new Vector2();



    private float maxAngle = 150f;
    private float minAngle = 30f;
    private float maxIntensity = 35f;
    private float minIntensity = 12f;



    private Body slingshotBaseBody;
    private MouseJoint mouseJoint;
    private WeldJoint birdJoint;

    private ShapeRenderer shapeRenderer;

    private float timer = 25f;
    private BitmapFont font;

    private int currentLevel=2;
    private float playerX, playerY;
    private GameState gameState;


    public Level2Screen(Main game) {
        this.game = game;
        initialize();

    }
    public Level2Screen(Main game, GameState gameState) {
        this.game = game;
        initialize();
        this.gameState = gameState;
    }




    private void initialize() {

        Box2D.init();



        world = new World(new Vector2(0, -9.8f), true);



        debugRenderer = new Box2DDebugRenderer();
        pauseButtonTexture = new Texture("Level1/pause.png");
        pauseButtonBounds = new Rectangle(20, 400, pauseButtonTexture.getWidth(), pauseButtonTexture.getHeight());

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        camera.update();

        font = new BitmapFont();
        font.getData().setScale(0.05f);


        batch = new SpriteBatch();



        shapeRenderer = new ShapeRenderer();



        groundTexture = new Texture(Gdx.files.internal("Level1/ground.png"));
        bgSlingshotTexture = new Texture(Gdx.files.internal("Level1/bgslingshot.png"));
        fSlingshotTexture = new Texture(Gdx.files.internal("Level1/fslingshot.png"));
        backgroundTexture = new Texture(Gdx.files.internal("Level1/bg_Level1.png"));
        birdsQueue = new LinkedList<>();


        pigs = new Array<>();



        PigClass pig1 = new PigClass(world,13.1f,1f,WORLD_WIDTH,WORLD_HEIGHT);
        PigClass pig2 = new PigClass(world,9.2f,1f,WORLD_WIDTH,WORLD_HEIGHT);
        PigClass pig3 = new PigClass(world,11.6f,4f,WORLD_WIDTH,WORLD_HEIGHT);
        pig1.setHealth(2000);
        pig2.setHealth(2000);
        pig3.setHealth(2000);
        pig1.setTexture("pigs/smallPig.png");
        pig2.setTexture("pigs/smallPig.png");
        pig3.setTexture("pigs/bigPig.png");
        pigs.add(pig1);
        pigs.add(pig2);
        pigs.add(pig3);





        createGroundBody();
        createWall(WORLD_WIDTH, WORLD_HEIGHT / 2f);
        createWall(0, WORLD_HEIGHT / 2f);

        createSlingshotBase();



        redBird = new YellowBird(world, birdInitialPosition.x, birdInitialPosition.y, 0.5f);
        yellowBird = new YellowBird(world,birdInitialPosition.x, birdInitialPosition.y, 0.5f);
        blackBird = new BlackBird(world,birdInitialPosition.x, birdInitialPosition.y, 0.5f);
        birdsQueue.add(redBird);
        birdsQueue.add(yellowBird);
        birdsQueue.add(blackBird);




        if (!birdsQueue.isEmpty()) {
            loadNextBird();
        }
        else {
            currentBird = null;
        }





        createStructure();



        Gdx.input.setInputProcessor(new InputHandler());







        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Object userDataA = contact.getFixtureA().getBody().getUserData();
                Object userDataB = contact.getFixtureB().getBody().getUserData();



                if (userDataA instanceof PigClass || userDataB instanceof PigClass) {
                    PigClass pig = (userDataA instanceof PigClass) ? (PigClass) userDataA : (PigClass) userDataB;
                    Vector2 pigPosition = pig.getBody().getPosition();



                    if (pigPosition.x <= 5.0f || pigPosition.x >= WORLD_WIDTH ||
                        pigPosition.y <= 0 || pigPosition.y >= WORLD_HEIGHT) {
                        pig.takeDamage(1);
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


    private void createGroundBody() {

        float groundWidth = WORLD_WIDTH;
        float groundHeight = 1f;
        float groundX = (WORLD_WIDTH / 2f) + 3.5f;
        float groundY = groundHeight / 2f;



        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(groundX, groundY);



        groundBody = world.createBody(bodyDef);



        PolygonShape shape = new PolygonShape();
        shape.setAsBox(groundWidth / 2f, groundHeight / 2f);



        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0f;


        groundBody.createFixture(fixtureDef);



        shape.dispose();
    }
    private void createWall(float wallX, float wallY) {

        float wallWidth = 0.1f;
        float wallHeight = WORLD_HEIGHT;


        BodyDef wallBodyDef = new BodyDef();
        wallBodyDef.type = BodyDef.BodyType.StaticBody;
        wallBodyDef.position.set(wallX, wallY);


        Body wallBody = world.createBody(wallBodyDef);


        PolygonShape wallShape = new PolygonShape();
        wallShape.setAsBox(wallWidth / 2f, wallHeight / 2f);


        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = wallShape;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0f;

        wallBody.createFixture(fixtureDef);


        wallShape.dispose();
    }
    private void createSlingshotBase() {

        BodyDef baseBodyDef = new BodyDef();
        baseBodyDef.type = BodyDef.BodyType.StaticBody;
        baseBodyDef.position.set(bgSlingshotX, bgSlingshotY);


        slingshotBaseBody = world.createBody(baseBodyDef);



        CircleShape baseShape = new CircleShape();
        baseShape.setRadius(0.1f);


        FixtureDef baseFixtureDef = new FixtureDef();
        baseFixtureDef.shape = baseShape;
        baseFixtureDef.isSensor = true;


        slingshotBaseBody.createFixture(baseFixtureDef);



        baseShape.dispose();
    }

















    private void createStructure() {

        structure = new Structure2(world, "obstacles/images.jpeg");
    }


    @Override
    public void render(float delta) {


        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (currentBird instanceof YellowBird) {
                ((YellowBird) currentBird).activateAbility();
            }
        }
        timer -= delta;


        if (timer <= 0) {
            game.setScreen(new LoseScreen(game,2));
            return;
        }


        world.step(delta, 6, 2);



        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        camera.update();



        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);



        batch.draw(groundTexture, 0, 0, WORLD_WIDTH, 1f);



        batch.draw(
            bgSlingshotTexture,
            bgSlingshotX - desiredSlingshotWidth / 2f,
            bgSlingshotY - desiredSlingshotHeight / 2f,
            desiredSlingshotWidth,
            desiredSlingshotHeight
        );




        if (currentBird != null) {
            currentBird.render(batch);
        }





        batch.draw(
            fSlingshotTexture,
            fSlingshotX - desiredSlingshotWidth / 2f,
            fSlingshotY - desiredSlingshotHeight / 2f,
            desiredSlingshotWidth,
            desiredSlingshotHeight
        );



        for (PigClass pig : pigs) {
            pig.render(batch);
        }



        structure.render(batch);
        batch.draw(
            pauseButtonTexture,
            WORLD_WIDTH - 15.4f,
            WORLD_HEIGHT - 1.4f,
            1f,
            1f
        );

        pauseButtonBounds = new Rectangle(
            (WORLD_WIDTH - 15.4f) * (Gdx.graphics.getWidth() / WORLD_WIDTH),
            ((WORLD_HEIGHT - 1.4f) * (Gdx.graphics.getHeight() / WORLD_HEIGHT)),
            1f * (Gdx.graphics.getWidth() / WORLD_WIDTH),
            1f * (Gdx.graphics.getHeight() / WORLD_HEIGHT)
        );;


        font.getData().setScale(0.1f);
        font.setColor(1f, 1f, 1f, 1f);
        String timerText = String.valueOf(MathUtils.ceil(timer));
        float timerX = WORLD_WIDTH - 3.7f;
        float timerY = WORLD_HEIGHT - 0.7f;
        font.draw(batch, timerText, timerX, timerY);

        batch.end();





        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (PigClass pig : pigs) {
            pig.renderHealthBar(shapeRenderer);
        }
        shapeRenderer.end();



        if (isDragging) {
            drawTrajectory();
            drawElasticBands();
        }



        for (int i = pigs.size - 1; i >= 0; i--) {
            PigClass pig = pigs.get(i);
            pig.update();
            if (pig.isDead()) {
                pigs.removeIndex(i);
            }
        }


        if (checkWinCondition()) {
            game.setScreen(new WinScreen(game,2));
        }




        debugRenderer.render(world, camera.combined);

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();
            if (pauseButtonBounds.contains(touchX, touchY)) {
                game.setScreen(new PauseScreen(game,this));
            }
        }

        resetBirdIfNeeded();

    }

    public int getCurrentLevel() {
        return currentLevel;
    }


    public float getPlayerX() {
        return playerX;
    }


    public float getPlayerY() {
        return playerY;
    }

    private boolean checkWinCondition() {
        return pigs.size == 0;
    }

    private void drawTrajectory() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 0, 1);



        Vector2 displacement = dragStart.cpy().sub(dragCurrent);
        float angleDegrees = displacement.angle();
        float intensity = MathUtils.clamp(displacement.len() * 5f, minIntensity, maxIntensity);



        float velocityMagnitude = intensity;
        float angleRadians = MathUtils.degreesToRadians * angleDegrees;
        Vector2 velocity = new Vector2(
            velocityMagnitude * MathUtils.cos(angleRadians),
            velocityMagnitude * MathUtils.sin(angleRadians)
        );



        Vector2 position = currentBird.getPosition().cpy();
        Vector2 gravity = world.getGravity().cpy();


        float timeStep = 0.05f;
        int steps = 60;


        Vector2 previousPoint = position.cpy();


        for (int i = 1; i <= steps; i++) {
            float t = i * timeStep;
            Vector2 futurePosition = new Vector2(
                position.x + velocity.x * t,
                position.y + velocity.y * t + 0.5f * gravity.y * t * t
            );



            shapeRenderer.line(previousPoint, futurePosition);


            previousPoint = futurePosition.cpy();
        }


        shapeRenderer.end();
    }



    private void drawElasticBands() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.545f, 0.271f, 0.075f, 1);



        shapeRenderer.rectLine(
            slingshotBaseBody.getPosition().x - 0.1f, slingshotBaseBody.getPosition().y + 0.2f,
            redBird.getPosition().x, redBird.getPosition().y,
            0.05f
        );



        shapeRenderer.rectLine(
            slingshotBaseBody.getPosition().x + 0.1f, slingshotBaseBody.getPosition().y + 0.2f,
            redBird.getPosition().x, redBird.getPosition().y,
            0.05f
        );


        shapeRenderer.end();
    }





    private void resetBirdIfNeeded() {
        Vector2 birdPos = redBird.getPosition();
        if (birdPos.x < 0 || birdPos.x > WORLD_WIDTH || birdPos.y < 0 || birdPos.y > WORLD_HEIGHT) {
            resetBird();
        }



        if (!isDragging && mouseJoint == null && redBird.getBody().getLinearVelocity().len() < 0.1f) {
            resetBird();
        }
    }


    private void attachBirdToSlingshot() {
        if (currentBird != null) {
            WeldJointDef jointDef = new WeldJointDef();
            jointDef.bodyA = slingshotBaseBody;
            jointDef.bodyB = currentBird.getBody();
            jointDef.localAnchorA.set(0, 0);
            jointDef.localAnchorB.set(0, 0);
            birdJoint = (WeldJoint) world.createJoint(jointDef);
        }


    }




    private void loadNextBird() {
        if (!birdsQueue.isEmpty()) {
            currentBird = birdsQueue.poll();
            currentBird.getBody().setTransform(birdInitialPosition.x, birdInitialPosition.y, 0);
            attachBirdToSlingshot();
        } else {
            currentBird = null;
            System.out.println("No birds left in the queue.");

        }
        System.out.println("Current bird: " + (currentBird != null ? "Available" : "None"));


    }
    private void resetBird() {

        world.destroyBody(redBird.getBody());


        redBird = new YellowBird(world, birdInitialPosition.x, birdInitialPosition.y, 0.5f);

        attachBirdToSlingshot();



        isDragging = false;





    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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



    @Override public void show() {
        if (gameState != null) {
            GameState loadedGameState = loadGameState();
            if (loadedGameState != null) {
                currentLevel = loadedGameState.getLevel();
                playerX = loadedGameState.getPlayerX();
                playerY = loadedGameState.getPlayerY();
            } else {

                currentLevel = 1;
                playerX = 100f;
                playerY = 200f;
            }
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



                if (birdJoint != null) {
                    world.destroyJoint(birdJoint);
                    birdJoint = null;
                }



                MouseJointDef mouseJointDef = new MouseJointDef();
                mouseJointDef.bodyA = groundBody;
                mouseJointDef.bodyB = redBird.getBody();
                mouseJointDef.collideConnected = true;
                mouseJointDef.target.set(worldCoords);
                mouseJointDef.maxForce = 1000.0f * currentBird.getBody().getMass();


                mouseJoint = (MouseJoint) world.createJoint(mouseJointDef);



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



                float maxDragDistance = 3.0f;
                Vector2 dragVector = worldCoords.cpy().sub(birdInitialPosition);
                if (dragVector.len() > maxDragDistance) {
                    dragVector.setLength(maxDragDistance);
                    worldCoords = birdInitialPosition.cpy().add(dragVector);
                }


                mouseJoint.setTarget(worldCoords);



                dragCurrent.set(worldCoords);


                return true;
            }
            return false;
        }


        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            if (isDragging && mouseJoint != null) {
                isDragging = false;



                world.destroyJoint(mouseJoint);
                mouseJoint = null;



                Vector2 displacement = dragStart.cpy().sub(dragCurrent);
                float angleDegrees = displacement.angle();
                float intensity = MathUtils.clamp(displacement.len() * 5f, minIntensity, maxIntensity);



                float velocityMagnitude = intensity;
                float angleRadians = MathUtils.degreesToRadians * angleDegrees;
                Vector2 velocity = new Vector2(
                    velocityMagnitude * MathUtils.cos(angleRadians),
                    velocityMagnitude * MathUtils.sin(angleRadians)
                );



                currentBird.getBody().setLinearVelocity(velocity);


                return true;
            }
            return false;
        }



        private Vector2 screenToWorld(int screenX, int screenY) {
            Vector3 touchPos = new Vector3(screenX, screenY, 0);
            camera.unproject(touchPos, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
            return new Vector2(touchPos.x, touchPos.y);
        }


    }


}
