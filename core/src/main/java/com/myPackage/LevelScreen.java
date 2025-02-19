package com.myPackage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class LevelScreen implements Screen {
    private Main game;
    private Texture backgroundTexture;
    private Texture level1Texture, level2Texture, level3Texture, exitTexture;
    private Texture level1HoverTexture, level2HoverTexture, level3HoverTexture, exitHoverTexture;
    private SpriteBatch batch;
    private Sound clickSound;


    private Rectangle level1ButtonBounds, level2ButtonBounds, level3ButtonBounds, exitButtonBounds;


    private boolean isLevel1Hovered, isLevel2Hovered, isLevel3Hovered, isExitHovered;

    public LevelScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();


        backgroundTexture = new Texture(Gdx.files.internal("./LevelScreen/LevelScreen.png"));


        level1Texture = new Texture(Gdx.files.internal("./LevelScreen/b_Level1.png"));
        level2Texture = new Texture(Gdx.files.internal("./LevelScreen/b_Level2.png"));
        level3Texture = new Texture(Gdx.files.internal("./LevelScreen/b_Level3.png"));
        exitTexture = new Texture(Gdx.files.internal("./LevelScreen/b_exit.png"));


        level1HoverTexture = new Texture(Gdx.files.internal("./LevelScreen/h_b_Level1.png"));
        level2HoverTexture = new Texture(Gdx.files.internal("./LevelScreen/h_b_Level2.png"));
        level3HoverTexture = new Texture(Gdx.files.internal("./LevelScreen/h_b_Level3.png"));
        exitHoverTexture = new Texture(Gdx.files.internal("./LevelScreen/h_b_exit.png"));


        clickSound = Gdx.audio.newSound(Gdx.files.internal("./LevelScreen/s_buttonchime.mp3"));


        level1ButtonBounds = new Rectangle(85, 211, level1Texture.getWidth(), level1Texture.getHeight());
        level2ButtonBounds = new Rectangle(191, 211, level2Texture.getWidth(), level2Texture.getHeight());
        level3ButtonBounds = new Rectangle(297, 211, level3Texture.getWidth(), level3Texture.getHeight());
        exitButtonBounds = new Rectangle(25, 30, exitTexture.getWidth(), exitTexture.getHeight());
    }

    @Override
    public void render(float delta) {

        float touchX = Gdx.input.getX();
        float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

        isLevel1Hovered = level1ButtonBounds.contains(touchX, touchY);
        isLevel2Hovered = level2ButtonBounds.contains(touchX, touchY);
        isLevel3Hovered = level3ButtonBounds.contains(touchX, touchY);
        isExitHovered = exitButtonBounds.contains(touchX, touchY);


        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        batch.draw(isLevel1Hovered ? level1HoverTexture : level1Texture, level1ButtonBounds.x, level1ButtonBounds.y);
        batch.draw(isLevel2Hovered ? level2HoverTexture : level2Texture, level2ButtonBounds.x, level2ButtonBounds.y);
        batch.draw(isLevel3Hovered ? level3HoverTexture : level3Texture, level3ButtonBounds.x, level3ButtonBounds.y);
        batch.draw(isExitHovered ? exitHoverTexture : exitTexture, exitButtonBounds.x, exitButtonBounds.y);

        batch.end();


        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (level1ButtonBounds.contains(touchX, touchY)) {
                clickSound.play();
                game.setScreen(new Level1Screen(game));

            } else if (level2ButtonBounds.contains(touchX, touchY)) {
                clickSound.play();
                game.setScreen(new Level2Screen(game));

            } else if (level3ButtonBounds.contains(touchX, touchY)) {
                clickSound.play();
                game.setScreen(new Level3Screen(game));

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
        level1Texture.dispose();
        level2Texture.dispose();
        level3Texture.dispose();
        exitTexture.dispose();
        level1HoverTexture.dispose();
        level2HoverTexture.dispose();
        level3HoverTexture.dispose();
        exitHoverTexture.dispose();
        clickSound.dispose();
    }
}
