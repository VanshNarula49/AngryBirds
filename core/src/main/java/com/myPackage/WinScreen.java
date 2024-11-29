package com.myPackage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class WinScreen implements Screen {
    private Main game;
    private Texture backgroundTexture;
    private Texture mainMenuTexture, nextTexture, restartTexture;
    private SpriteBatch batch;
    private Sound clickSound;

    private Rectangle mainMenuButtonBounds, nextButtonBounds, restartButtonBounds;

    private int currentLevel;  // This will keep track of the current level

    public WinScreen(Main game, int currentLevel) {
        this.game = game;
        this.currentLevel = currentLevel;  // Set the current level when the WinScreen is initialized
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        // Texture paths updated to the provided ones
        backgroundTexture = new Texture(Gdx.files.internal("./winScreen/winScreen.png"));
        mainMenuTexture = new Texture(Gdx.files.internal("./winScreen/b_MainMenu.png"));
        nextTexture = new Texture(Gdx.files.internal("./winScreen/b_next.png"));
        restartTexture = new Texture(Gdx.files.internal("./winScreen/b_restart.png"));

        clickSound = Gdx.audio.newSound(Gdx.files.internal("./winScreen/s_buttonchime.mp3"));

        // Button positions updated as per your code
        mainMenuButtonBounds = new Rectangle(120, 11, mainMenuTexture.getWidth(), mainMenuTexture.getHeight());
        nextButtonBounds = new Rectangle(320, -5, nextTexture.getWidth(), nextTexture.getHeight());
        restartButtonBounds = new Rectangle(540, 11, restartTexture.getWidth(), restartTexture.getHeight());
    }

    @Override
    public void render(float delta) {
        float touchX = Gdx.input.getX();
        float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch.draw(mainMenuTexture, mainMenuButtonBounds.x, mainMenuButtonBounds.y);
        batch.draw(nextTexture, nextButtonBounds.x, nextButtonBounds.y);
        batch.draw(restartTexture, restartButtonBounds.x, restartButtonBounds.y);

        batch.end();

        // Check if a button is clicked
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (mainMenuButtonBounds.contains(touchX, touchY)) {
                clickSound.play();
                game.setScreen(new MainMenuScreen(game));  // Go to Main Menu
            } else if (nextButtonBounds.contains(touchX, touchY)) {
                clickSound.play();
                // Navigate to the next level based on the current level
                if (currentLevel == 1) {
                    game.setScreen(new Level2Screen(game));  // Go to Level 2
                } else if (currentLevel == 2) {
                    game.setScreen(new Level3Screen(game));  // Go to Level 3
                } else {
                    // Optional: You could show a "You completed the game!" message
                    game.setScreen(new MainMenuScreen(game));  // Go to Main Menu after Level 3
                }
            } else if (restartButtonBounds.contains(touchX, touchY)) {
                clickSound.play();
                // Restart the current level
                if (currentLevel == 1) {
                    game.setScreen(new Level1Screen(game));  // Restart Level 1
                } else if (currentLevel == 2) {
                    game.setScreen(new Level2Screen(game));  // Restart Level 2
                } else if (currentLevel == 3) {
                    game.setScreen(new Level3Screen(game));  // Restart Level 3
                }
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
        mainMenuTexture.dispose();
        nextTexture.dispose();
        restartTexture.dispose();
        clickSound.dispose();
    }
}
