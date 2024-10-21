package com.myPackage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class NewScreen implements Screen {
    public NewScreen(Main game){
        this.game = game;
    }

    private Main game;
    private Texture backgroundTexture;
    private Texture playTexture, newGameTexture;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    // Define button bounds for manual click detection
    private Rectangle playButtonBounds, newGameButtonBounds;
    private float exitButtonX = 100, exitButtonY = 350, exitButtonRadius = 50; // For round button

    @Override
    public void show() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont(); // Use the default font, can be customized

        // Load the background texture
        backgroundTexture = new Texture(Gdx.files.internal("./MainMenuScreen/MainMenu.png"));

        // Load button images
        playTexture = new Texture(Gdx.files.internal("./MainMenuScreen/b_Load_Game.png"));
        newGameTexture = new Texture(Gdx.files.internal("./MainMenuScreen/b_New_Game.png"));

        // Define button positions and sizes (x, y, width, height)
        playButtonBounds = new Rectangle(100, 600-100, 200, 50);
        newGameButtonBounds = new Rectangle(100, 525-100, 200, 50);
    }

    @Override
    public void render(float delta) {
        // Render the background, scaled to the current window size
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());  // Scale background dynamically
        batch.draw(playTexture, playButtonBounds.x, playButtonBounds.y, playButtonBounds.width, playButtonBounds.height);
        batch.draw(newGameTexture, newGameButtonBounds.x, newGameButtonBounds.y, newGameButtonBounds.width, newGameButtonBounds.height);
        batch.end();

        // Draw the round exit button with ShapeRenderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);  // For the border
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.circle(exitButtonX, exitButtonY, exitButtonRadius); // White border circle
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);  // For the filled button
        shapeRenderer.setColor(Color.CLEAR);  // Clear color, just showing the border
        shapeRenderer.circle(exitButtonX, exitButtonY, exitButtonRadius);
        shapeRenderer.end();

        // Draw the "Exit" text in the middle of the circle
        batch.begin();
        font.setColor(Color.WHITE);
        font.draw(batch, "Exit", exitButtonX - 20, exitButtonY + 5);  // Adjust the text position
        batch.end();

        // Handle input for button clicks
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();  // Flip Y coordinate since input origin is top-left

            if (playButtonBounds.contains(touchX, touchY)) {
                System.out.println("Play Game Clicked");

            } else if (newGameButtonBounds.contains(touchX, touchY)) {
                System.out.println("New Game Clicked");

            } else if (isTouchInsideCircle(touchX, touchY, exitButtonX, exitButtonY, exitButtonRadius)) {
                Gdx.app.exit();  // Exit the application
            }
        }
    }

    // Helper method to check if touch is inside the circle (round button)
    private boolean isTouchInsideCircle(float touchX, float touchY, float centerX, float centerY, float radius) {
        float dx = touchX - centerX;
        float dy = touchY - centerY;
        return dx * dx + dy * dy <= radius * radius;  // Check if within circle radius
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
        // Dispose of assets to avoid memory leaks
        batch.dispose();
        backgroundTexture.dispose();
        playTexture.dispose();
        newGameTexture.dispose();
        shapeRenderer.dispose();
        font.dispose();
    }
}
