package com.github.kleesup.kleegdx.client.screen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Base implementation of {@link Screen} that implements basic methods such as {@link #pause()} and adds
 * a render order. This class is reimplemented in 2d and 3d.
 */
public abstract class BaseScreen<Main extends ApplicationListener> implements Screen {

    protected final Main main;
    protected final AtomicBoolean paused = new AtomicBoolean(false);
    protected Color clearColor = new Color(Color.BLACK);
    protected BaseScreen(Main main) {
        this.main = main;
    }

    /* -- Rendering -- */

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(clearColor.r,clearColor.g,clearColor.b,clearColor.a);
        //The rest like render order and glClear() is implemented differently in BaseScreen2d and BaseScreen3d
    }

    protected abstract void beforeRender(float delta);
    protected abstract void onRender(float delta);
    protected abstract void afterRender(float delta);

    /* -- Implementation -- */

    @Override
    public void pause() {
        paused.set(true);
    }

    @Override
    public void resume() {
        paused.set(false);
    }

    @Override
    public void hide() {}

    public boolean isPaused(){
        return paused.get();
    }

}
