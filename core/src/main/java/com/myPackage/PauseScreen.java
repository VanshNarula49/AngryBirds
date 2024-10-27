package com.myPackage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class PauseScreen implements Screen {
    private Main game;
    private Texture backgroundTexture;
    private Texture resumeTexture, mainMenuTexture, exitTexture;
    private Texture resumeHoverTexture, mainMenuHoverTexture, exitHoverTexture;
    private SpriteBatch batch;
    private Sound clickSound;

    // Define button bounds for manual click detection
    private Rectangle resumeButtonBounds, mainMenuButtonBounds, exitButtonBounds;

    // Track hover states
    private boolean isResumeHovered, isMainMenuHovered, isExitHovered;

    public PauseScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        // Load the background texture
        backgroundTexture = new Texture(Gdx.files.internal("./PauseScreen/pauseMenu.png"));

        // Load button images
        resumeTexture = new Texture(Gdx.files.internal("./PauseScreen/b_resume.png"));
        mainMenuTexture = new Texture(Gdx.files.internal("./PauseScreen/b_MainMenu.png"));
        exitTexture = new Texture(Gdx.files.internal("./PauseScreen/b_exit.png"));

        // Load hover button images
        resumeHoverTexture = new Texture(Gdx.files.internal("./PauseScreen/h_b_resume.png"));
        mainMenuHoverTexture = new Texture(Gdx.files.internal("./PauseScreen/h_b_MainMenu.png"));
        exitHoverTexture = new Texture(Gdx.files.internal("./PauseScreen/h_b_exit.png"));

        // Load sound
        clickSound = Gdx.audio.newSound(Gdx.files.internal("./PauseScreen/s_buttonchime.mp3"));

        // Define button positions and sizes (x, y, width, height)
        resumeButtonBounds = new Rectangle(277, 146, resumeTexture.getWidth(), resumeTexture.getHeight());
        mainMenuButtonBounds = new Rectangle(277, 245, mainMenuTexture.getWidth(), mainMenuTexture.getHeight());
        exitButtonBounds = new Rectangle(370, 80, exitTexture.getWidth(), exitTexture.getHeight());
    }

    @Override
    public void render(float delta) {
        // Update hover states based on mouse position
        float touchX = Gdx.input.getX();
        float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();  // Flip Y coordinate

        isResumeHovered = resumeButtonBounds.contains(touchX, touchY);
        isMainMenuHovered = mainMenuButtonBounds.contains(touchX, touchY);
        isExitHovered = exitButtonBounds.contains(touchX, touchY);

        // Render the screen
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw buttons based on hover state
        batch.draw(isResumeHovered ? resumeHoverTexture : resumeTexture, resumeButtonBounds.x, resumeButtonBounds.y);
        batch.draw(isMainMenuHovered ? mainMenuHoverTexture : mainMenuTexture, mainMenuButtonBounds.x, mainMenuButtonBounds.y);
        batch.draw(isExitHovered ? exitHoverTexture : exitTexture, exitButtonBounds.x, exitButtonBounds.y);

        batch.end();

        // Handle input for button clicks
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (resumeButtonBounds.contains(touchX, touchY)) {
                clickSound.play(); // Play chime sound
                game.setScreen(new Level1Screen(game)); // Replace with the game screen to resume
            } else if (mainMenuButtonBounds.contains(touchX, touchY)) {
                clickSound.play(); // Play chime sound
                game.setScreen(new MainMenuScreen(game)); // Replace with the main menu screen
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
        resumeTexture.dispose();
        mainMenuTexture.dispose();
        exitTexture.dispose();
        resumeHoverTexture.dispose();
        mainMenuHoverTexture.dispose();
        exitHoverTexture.dispose();
        clickSound.dispose();
    }
}
