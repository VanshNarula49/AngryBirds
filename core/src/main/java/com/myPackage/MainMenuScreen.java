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


    private Rectangle playButtonBounds, newGameButtonBounds, exitButtonBounds;


    private boolean isPlayHovered, isNewGameHovered, isExitHovered;

    public MainMenuScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();


        backgroundTexture = new Texture(Gdx.files.internal("./MainMenuScreen/MainMenu.png"));


        playTexture = new Texture(Gdx.files.internal("./MainMenuScreen/b_Load_Game.png"));
        newGameTexture = new Texture(Gdx.files.internal("./MainMenuScreen/b_New_Game.png"));
        exitTexture = new Texture(Gdx.files.internal("./MainMenuScreen/b_exit.png"));


        playHoverTexture = new Texture(Gdx.files.internal("./MainMenuScreen/h_b_Load_Game.png"));
        newGameHoverTexture = new Texture(Gdx.files.internal("./MainMenuScreen/h_b_New_Game.png"));
        exitHoverTexture = new Texture(Gdx.files.internal("./MainMenuScreen/h_b_exit.png"));
        clickSound = Gdx.audio.newSound(Gdx.files.internal("./MainMenuScreen/s_buttonchime.mp3"));


        playButtonBounds = new Rectangle(112, 141, playTexture.getWidth(), playTexture.getHeight());
        newGameButtonBounds = new Rectangle(112, 240, newGameTexture.getWidth(), newGameTexture.getHeight());
        exitButtonBounds = new Rectangle(25, 30, exitTexture.getWidth(), exitTexture.getHeight());
    }

    @Override
    public void render(float delta) {

        float touchX = Gdx.input.getX();
        float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

        isPlayHovered = playButtonBounds.contains(touchX, touchY);
        isNewGameHovered = newGameButtonBounds.contains(touchX, touchY);
        isExitHovered = exitButtonBounds.contains(touchX, touchY);


        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        batch.draw(isPlayHovered ? playHoverTexture : playTexture, playButtonBounds.x, playButtonBounds.y);
        batch.draw(isNewGameHovered ? newGameHoverTexture : newGameTexture, newGameButtonBounds.x, newGameButtonBounds.y);
        batch.draw(isExitHovered ? exitHoverTexture : exitTexture, exitButtonBounds.x, exitButtonBounds.y);

        batch.end();


        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (playButtonBounds.contains(touchX, touchY)) {
                clickSound.play();
                game.setScreen(new LevelScreen(game));
            } else if (newGameButtonBounds.contains(touchX, touchY)) {
                clickSound.play();
                game.setScreen(new Level1Screen(game));
            } else if (exitButtonBounds.contains(touchX, touchY)) {
                clickSound.play();
                Gdx.app.exit();
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
        playTexture.dispose();
        newGameTexture.dispose();
        exitTexture.dispose();
        playHoverTexture.dispose();
        newGameHoverTexture.dispose();
        exitHoverTexture.dispose();
    }
}
