package com.myPackage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
//
//public class PauseScreen implements Screen {
//    private Main game;
//    private Texture backgroundTexture;
//    private Texture resumeTexture, mainMenuTexture, exitTexture;
//    private Texture resumeHoverTexture, mainMenuHoverTexture, exitHoverTexture;
//    private SpriteBatch batch;
//    private Sound clickSound;
//    private Screen currentLevel;
//
//
//    private Rectangle resumeButtonBounds, mainMenuButtonBounds, exitButtonBounds;
//
//
//    private boolean isResumeHovered, isMainMenuHovered, isExitHovered;
//
//    public PauseScreen(Main game, Screen currentLevel) {
//        this.game = game;
//        this.currentLevel = currentLevel;  // Store the current level screen
//    }
//    @Override
//    public void show() {
//        batch = new SpriteBatch();
//
//
//        backgroundTexture = new Texture(Gdx.files.internal("./PauseScreen/pauseMenu.png"));
//
//
//        resumeTexture = new Texture(Gdx.files.internal("./PauseScreen/b_resume.png"));
//        mainMenuTexture = new Texture(Gdx.files.internal("./PauseScreen/b_MainMenu.png"));
//        exitTexture = new Texture(Gdx.files.internal("./PauseScreen/b_exit.png"));
//
//
//        resumeHoverTexture = new Texture(Gdx.files.internal("./PauseScreen/h_b_resume.png"));
//        mainMenuHoverTexture = new Texture(Gdx.files.internal("./PauseScreen/h_b_MainMenu.png"));
//        exitHoverTexture = new Texture(Gdx.files.internal("./PauseScreen/h_b_exit.png"));
//
//
//        clickSound = Gdx.audio.newSound(Gdx.files.internal("./PauseScreen/s_buttonchime.mp3"));
//
//
//        resumeButtonBounds = new Rectangle(277, 146, resumeTexture.getWidth(), resumeTexture.getHeight());
//        mainMenuButtonBounds = new Rectangle(277, 245, mainMenuTexture.getWidth(), mainMenuTexture.getHeight());
//        exitButtonBounds = new Rectangle(370, 80, exitTexture.getWidth(), exitTexture.getHeight());
//    }
//
//    @Override
//    public void render(float delta) {
//
//        float touchX = Gdx.input.getX();
//        float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();
//
//        isResumeHovered = resumeButtonBounds.contains(touchX, touchY);
//        isMainMenuHovered = mainMenuButtonBounds.contains(touchX, touchY);
//        isExitHovered = exitButtonBounds.contains(touchX, touchY);
//
//
//        batch.begin();
//        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//
//
//        batch.draw(isResumeHovered ? resumeHoverTexture : resumeTexture, resumeButtonBounds.x, resumeButtonBounds.y);
//        batch.draw(isMainMenuHovered ? mainMenuHoverTexture : mainMenuTexture, mainMenuButtonBounds.x, mainMenuButtonBounds.y);
//        batch.draw(isExitHovered ? exitHoverTexture : exitTexture, exitButtonBounds.x, exitButtonBounds.y);
//
//        batch.end();
//
//
//        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
//            if (resumeButtonBounds.contains(touchX, touchY)) {
//                clickSound.play();
//                // Resume the correct level based on the current level
//                game.setScreen(currentLevel); // This will resume the current level
//            } else if (mainMenuButtonBounds.contains(touchX, touchY)) {
//                clickSound.play();
//                game.setScreen(new MainMenuScreen(game));
//            } else if (exitButtonBounds.contains(touchX, touchY)) {
//                clickSound.play();
//                Gdx.app.exit();
//            }
//        }
//    }
//
//    @Override
//    public void resize(int width, int height) {}
//
//    @Override
//    public void pause() {}
//
//    @Override
//    public void resume() {}
//
//    @Override
//    public void hide() {}
//
//    @Override
//    public void dispose() {
//
//        batch.dispose();
//        backgroundTexture.dispose();
//        resumeTexture.dispose();
//        mainMenuTexture.dispose();
//        exitTexture.dispose();
//        resumeHoverTexture.dispose();
//        mainMenuHoverTexture.dispose();
//        exitHoverTexture.dispose();
//        clickSound.dispose();
//    }
//}

import com.myPackage.GameState;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class PauseScreen implements Screen {
    // Other variables
    private Main game;
    private Texture backgroundTexture;
    private Texture resumeTexture, mainMenuTexture, exitTexture;
    private Texture resumeHoverTexture, mainMenuHoverTexture, exitHoverTexture;
    private SpriteBatch batch;
    private Sound clickSound;
    private Screen currentLevel;
    private Rectangle resumeButtonBounds, mainMenuButtonBounds, exitButtonBounds;
    private boolean isResumeHovered, isMainMenuHovered, isExitHovered;

    public PauseScreen(Main game, Screen currentLevel) {
        this.game = game;
        this.currentLevel = currentLevel;  // Store the current level screen
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        backgroundTexture = new Texture(Gdx.files.internal("./PauseScreen/pauseMenu.png"));
        resumeTexture = new Texture(Gdx.files.internal("./PauseScreen/b_resume.png"));
        mainMenuTexture = new Texture(Gdx.files.internal("./PauseScreen/b_MainMenu.png"));
        exitTexture = new Texture(Gdx.files.internal("./PauseScreen/b_exit.png"));
        resumeHoverTexture = new Texture(Gdx.files.internal("./PauseScreen/h_b_resume.png"));
        mainMenuHoverTexture = new Texture(Gdx.files.internal("./PauseScreen/h_b_MainMenu.png"));
        exitHoverTexture = new Texture(Gdx.files.internal("./PauseScreen/h_b_exit.png"));
        clickSound = Gdx.audio.newSound(Gdx.files.internal("./PauseScreen/s_buttonchime.mp3"));

        resumeButtonBounds = new Rectangle(277, 146, resumeTexture.getWidth(), resumeTexture.getHeight());
        mainMenuButtonBounds = new Rectangle(277, 245, mainMenuTexture.getWidth(), mainMenuTexture.getHeight());
        exitButtonBounds = new Rectangle(370, 80, exitTexture.getWidth(), exitTexture.getHeight());
    }

    @Override
    public void render(float delta) {
        float touchX = Gdx.input.getX();
        float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

        isResumeHovered = resumeButtonBounds.contains(touchX, touchY);
        isMainMenuHovered = mainMenuButtonBounds.contains(touchX, touchY);
        isExitHovered = exitButtonBounds.contains(touchX, touchY);

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(isResumeHovered ? resumeHoverTexture : resumeTexture, resumeButtonBounds.x, resumeButtonBounds.y);
        batch.draw(isMainMenuHovered ? mainMenuHoverTexture : mainMenuTexture, mainMenuButtonBounds.x, mainMenuButtonBounds.y);
        batch.draw(isExitHovered ? exitHoverTexture : exitTexture, exitButtonBounds.x, exitButtonBounds.y);
        batch.end();

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (resumeButtonBounds.contains(touchX, touchY)) {
                clickSound.play();
                saveGameState(); // Save the game state when resuming
                game.setScreen(currentLevel); // Resume the current level
            } else if (mainMenuButtonBounds.contains(touchX, touchY)) {
                clickSound.play();
                game.setScreen(new MainMenuScreen(game));
            } else if (exitButtonBounds.contains(touchX, touchY)) {
                clickSound.play();
                Gdx.app.exit();
            }
        }
    }

    private void saveGameState() {
        // Assuming the currentLevel is of a type that has the level, score, and player position
        if (currentLevel instanceof Level1Screen) {  // For example, Level1Screen
            Level1Screen level1Screen = (Level1Screen) currentLevel;
            GameState gameState = new GameState(level1Screen.getCurrentLevel(), level1Screen.getPlayerX(), level1Screen.getPlayerY());

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("gameState.ser"))) {
                oos.writeObject(gameState);
                System.out.println("Game state saved.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (currentLevel instanceof Level2Screen) {  // For example, Level1Screen
            Level2Screen level2Screen = (Level2Screen) currentLevel;
            GameState gameState = new GameState(level2Screen.getCurrentLevel(), level2Screen.getPlayerX(), level2Screen.getPlayerY());

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("gameState.ser"))) {
                oos.writeObject(gameState);
                System.out.println("Game state saved.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (currentLevel instanceof Level2Screen) {  // For example, Level1Screen
            Level3Screen level3Screen = (Level3Screen) currentLevel;
            GameState gameState = new GameState(level3Screen.getCurrentLevel(), level3Screen.getPlayerX(), level3Screen.getPlayerY());

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("gameState.ser"))) {
                oos.writeObject(gameState);
                System.out.println("Game state saved.");
            } catch (IOException e) {
                e.printStackTrace();
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
        resumeTexture.dispose();
        mainMenuTexture.dispose();
        exitTexture.dispose();
        resumeHoverTexture.dispose();
        mainMenuHoverTexture.dispose();
        exitHoverTexture.dispose();
        clickSound.dispose();
    }
}
