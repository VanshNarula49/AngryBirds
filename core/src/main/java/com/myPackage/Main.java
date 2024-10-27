package com.myPackage;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    @Override
    public void create() {
        setScreen(new MainMenuScreen(this));
    }
}

