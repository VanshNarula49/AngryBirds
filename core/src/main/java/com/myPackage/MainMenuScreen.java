package com.myPackage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class MainMenuScreen implements Screen {
    private Main game;
    private Texture backgroundTexture;
    private Texture playTexture, newGameTexture, exitTexture;
    private Texture playHoverTexture, newGameHoverTexture, exitHoverTexture;
    private SpriteBatch batch;
    private Sound clickSound;

    // Define button bounds for manual click detection
    private Rectangle playButtonBounds, newGameButtonBounds, exitButtonBounds;

    // Track hover states
    private boolean isPlayHovered, isNewGameHovered, isExitHovered;

    public MainMenuScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        // Load the background texture
        backgroundTexture = new Texture(Gdx.files.internal("./MainMenuScreen/MainMenu.png"));

        // Load button images
        playTexture = new Texture(Gdx.files.internal("./MainMenuScreen/b_Load_Game.png"));
        newGameTexture = new Texture(Gdx.files.internal("./MainMenuScreen/b_New_Game.png"));
        exitTexture = new Texture(Gdx.files.internal("./MainMenuScreen/b_exit.png"));

        // Load hover button images
        playHoverTexture = new Texture(Gdx.files.internal("./MainMenuScreen/h_b_Load_Game.png"));
        newGameHoverTexture = new Texture(Gdx.files.internal("./MainMenuScreen/h_b_New_Game.png"));
        exitHoverTexture = new Texture(Gdx.files.internal("./MainMenuScreen/h_b_exit.png"));
        clickSound = Gdx.audio.newSound(Gdx.files.internal("./MainMenuScreen/s_buttonchime.mp3")); // or your preferred format

        // Define button positions and sizes (x, y, width, height)
        playButtonBounds = new Rectangle(112, 141, playTexture.getWidth(), playTexture.getHeight());
        newGameButtonBounds = new Rectangle(112, 240, newGameTexture.getWidth(), newGameTexture.getHeight());
        exitButtonBounds = new Rectangle(25, 30, exitTexture.getWidth(), exitTexture.getHeight());
    }

    @Override
    public void render(float delta) {
        // Update hover states based on mouse position
        float touchX = Gdx.input.getX();
        float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();  // Flip Y coordinate

        isPlayHovered = playButtonBounds.contains(touchX, touchY);
        isNewGameHovered = newGameButtonBounds.contains(touchX, touchY);
        isExitHovered = exitButtonBounds.contains(touchX, touchY);

        // Render the screen
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw buttons based on hover state
        batch.draw(isPlayHovered ? playHoverTexture : playTexture, playButtonBounds.x, playButtonBounds.y);
        batch.draw(isNewGameHovered ? newGameHoverTexture : newGameTexture, newGameButtonBounds.x, newGameButtonBounds.y);
        batch.draw(isExitHovered ? exitHoverTexture : exitTexture, exitButtonBounds.x, exitButtonBounds.y);

        batch.end();

        // Handle input for button clicks
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (playButtonBounds.contains(touchX, touchY)) {
                clickSound.play(); // Play chime sound
                game.setScreen(new NewScreen(game));
            } else if (newGameButtonBounds.contains(touchX, touchY)) {
                clickSound.play(); // Play chime sound
                System.out.println("New Game Clicked");
            } else if (exitButtonBounds.contains(touchX, touchY)) {
                clickSound.play(); // Play chime sound
                Gdx.app.exit();  // Exit the application
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
        // Dispose of assets to avoid memory leaks
        batch.dispose();
        backgroundTexture.dispose();
        playTexture.dispose();
        newGameTexture.dispose();
        exitTexture.dispose();
        playHoverTexture.dispose();
        newGameHoverTexture.dispose();
        exitHoverTexture.dispose();
    }
}
