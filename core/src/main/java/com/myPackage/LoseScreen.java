package com.myPackage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class LoseScreen implements Screen {
    private Main game;
    private Texture backgroundTexture;
    private Texture mainMenuTexture, restartTexture;
    private SpriteBatch batch;
    private Sound clickSound;

    private Rectangle mainMenuButtonBounds, restartButtonBounds;

    private int currentLevel; // To store the current level the player was on

    // Constructor now takes currentLevel as a parameter
    public LoseScreen(Main game, int currentLevel) {
        this.game = game;
        this.currentLevel = currentLevel;  // Set the current level
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        // Correct texture paths
        backgroundTexture = new Texture(Gdx.files.internal("loseScreen/LoseScreen.png"));
        mainMenuTexture = new Texture(Gdx.files.internal("loseScreen/b_MainMenu.png"));
        restartTexture = new Texture(Gdx.files.internal("loseScreen/b_Restart.png"));

        clickSound = Gdx.audio.newSound(Gdx.files.internal("loseScreen/s_buttonchime.mp3"));

        // Define button bounds for Main Menu and Restart buttons
        mainMenuButtonBounds = new Rectangle(120, 11, mainMenuTexture.getWidth(), mainMenuTexture.getHeight());
        restartButtonBounds = new Rectangle(520, 11, restartTexture.getWidth(), restartTexture.getHeight());
    }

    @Override
    public void render(float delta) {

        float touchX = Gdx.input.getX();
        float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw the buttons
        batch.draw(mainMenuTexture, mainMenuButtonBounds.x, mainMenuButtonBounds.y);
        batch.draw(restartTexture, restartButtonBounds.x, restartButtonBounds.y);

        batch.end();

        // Handle input and button clicks
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (mainMenuButtonBounds.contains(touchX, touchY)) {
                clickSound.play();
                game.setScreen(new MainMenuScreen(game));  // Go to Main Menu
            } else if (restartButtonBounds.contains(touchX, touchY)) {
                clickSound.play();

                // Check which level the player was on and reload the same level
                switch (currentLevel) {
                    case 1:
                        game.setScreen(new Level1Screen(game));  // Restart Level 1
                        break;
                    case 2:
                        game.setScreen(new Level2Screen(game));  // Restart Level 2
                        break;
                    case 3:
                        game.setScreen(new Level3Screen(game));  // Restart Level 3
                        break;
                    default:
                        game.setScreen(new Level1Screen(game));  // Default to Level 1 if unknown
                        break;
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
        restartTexture.dispose();
        clickSound.dispose();
    }
}
