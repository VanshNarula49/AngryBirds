package com.myPackage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input;

import com.badlogic.gdx.math.Rectangle;

// Bird class definition
class Bird {
    private String name;
    private Texture image;

    public Bird(String name, Texture image) {
        this.name = name;
        this.image = image;
    }

    public Texture getImage() {
        return image;
    }
}

// Birds repository
class Birds {
    public static Bird red = new Bird("Red", new Texture("birds/red.png"));
    public static Bird yellow = new Bird("Yellow", new Texture("birds/yellow.png"));
    public static Bird black = new Bird("Black", new Texture("birds/black.png"));
}

// Pig class definition
class Pig {
    private String type;
    private Texture image;

    public Pig(String type, Texture image) {
        this.type = type;
        this.image = image;
    }

    public Texture getImage() {
        return image;
    }
}

// Pigs repository
class Pigs {
    public static Pig smallPig = new Pig("Small Pig", new Texture("pigs/smallPig.png"));
    public static Pig bigPig = new Pig("Big Pig", new Texture("pigs/bigPig.png"));
}

// Obstacle class definition with added width and height
class Obstacle {
    private String type;
    private Texture image;
    private String orientation;
    private int width, height;

    public Obstacle(String type, Texture image, String orientation, int width, int height) {
        this.type = type;
        this.image = image;
        this.orientation = orientation;
        this.width = width;
        this.height = height;
    }

    public Texture getImage() {
        return image;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

// Obstacles repository with multiple obstacles for the contraption
class Obstacles {
    public static Obstacle horizontalObstacle = new Obstacle("Block", new Texture("obstacles/horizontal.png"), "horizontal", 150, 20);
    public static Obstacle verticalObstacle = new Obstacle("Block", new Texture("obstacles/vertical.png"), "vertical", 50, 150);
    public static  Obstacle tiltedObstacle = new Obstacle("Block", new Texture("obstacles/tilted.png"), "tilted", 50, 150);
    public static  Obstacle ltiltedObstacle = new Obstacle("Block", new Texture("obstacles/ltilted.png"), "ltiled", 50, 150);

}

// Slingshot class definition
class Slingshot {
    private Texture bgimage;
    private Texture fimage;

    public Slingshot(Texture bgimage, Texture fimage) {
        this.bgimage = bgimage;
        this.fimage = fimage;
    }

    public Texture getBgimage() {
        return bgimage;
    }

    public Texture getFimage() {
        return fimage;
    }
}

// GameElements repository
class GameElements {
    public static Slingshot slingshot = new Slingshot(new Texture("Level1/bgslingshot.png"),new Texture("Level1/fslingshot.png"));
}





public class Level1Screen implements Screen {
    private final Main game;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Bird redBird, yellowBird, blackBird;
    private Pig smallPig, bigPig;
    private Obstacle horizontalObstacle, verticalObstacle, tiltedObstacle, ltiltedObstacle;
    private Texture pauseButtonTexture;
    private Rectangle pauseButtonBounds;

    public Level1Screen(Main game) {
        this.game = game;

        batch = new SpriteBatch();

        backgroundTexture = new Texture("Level1/bg_Level1.png");

        // Initialize Birds
        redBird = Birds.red;
        yellowBird = Birds.yellow;
        blackBird = Birds.black;

        // Initialize Pigs
        smallPig = Pigs.smallPig;
        bigPig = Pigs.bigPig;

        // Initialize Obstacles
        horizontalObstacle = Obstacles.horizontalObstacle;
        verticalObstacle = Obstacles.verticalObstacle;
        tiltedObstacle = Obstacles.tiltedObstacle;
        ltiltedObstacle = Obstacles.ltiltedObstacle;

        // Initialize Pause Button
        pauseButtonTexture = new Texture("Level1/pause.png");
        pauseButtonBounds = new Rectangle(20, 400, pauseButtonTexture.getWidth(), pauseButtonTexture.getHeight()); // position and size
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        batch.begin();

        // Draw background
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw slingshot
        batch.draw(GameElements.slingshot.getBgimage(), 100, 63);

        // Draw birds
        batch.draw(redBird.getImage(), 75, 130);
        batch.draw(yellowBird.getImage(), 50, 63);
        batch.draw(blackBird.getImage(), 0, 63);
        batch.draw(GameElements.slingshot.getFimage(), 73, 110);

        // Draw pigs
        batch.draw(smallPig.getImage(), 603, 182);
        batch.draw(bigPig.getImage(), 600, 80);

        // Draw obstacles
        batch.draw(horizontalObstacle.getImage(), 550, 63, horizontalObstacle.getWidth(), horizontalObstacle.getHeight());
        batch.draw(verticalObstacle.getImage(), 550, 81, verticalObstacle.getWidth(), 85);
        batch.draw(verticalObstacle.getImage(), 685, 81, verticalObstacle.getWidth(), 85);
        batch.draw(tiltedObstacle.getImage(), 570 + 10, 160, tiltedObstacle.getWidth(), tiltedObstacle.getHeight());
        batch.draw(ltiltedObstacle.getImage(), 607 + 10, 158, ltiltedObstacle.getWidth(), ltiltedObstacle.getHeight());
        batch.draw(horizontalObstacle.getImage(), 550, 163, horizontalObstacle.getWidth(), horizontalObstacle.getHeight());

        // Draw pause button
        batch.draw(pauseButtonTexture, pauseButtonBounds.x, pauseButtonBounds.y);

        batch.end();

        // Check for pause button click
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();
            if (pauseButtonBounds.contains(touchX, touchY)) {
                game.setScreen(new PauseScreen(game)); // Switch to PauseScreen
            }
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        backgroundTexture.dispose();
        GameElements.slingshot.getBgimage().dispose();
        pauseButtonTexture.dispose();
    }
}
