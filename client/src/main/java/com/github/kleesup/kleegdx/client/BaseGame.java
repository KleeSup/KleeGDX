package com.github.kleesup.kleegdx.client;

import com.badlogic.gdx.*;
import com.badlogic.gdx.utils.Logger;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Base implementation for {@link ApplicationListener} which can be therefore used as ground for a main class
 * in a project. Copies the screen management from {@link Game}.
 */
public abstract class BaseGame implements ApplicationListener {

    @Getter
    protected Screen currentScreen;
    @Setter(value = AccessLevel.PROTECTED)
    protected boolean autoDisposeScreen = true;
    private ApplicationListener screenManager;
    @Getter
    protected Logger logger;
    protected final InputMultiplexer inputManager = new InputMultiplexer();
    protected final AtomicBoolean paused = new AtomicBoolean(false);
    @Setter(value = AccessLevel.PROTECTED)
    protected boolean fixAndroidResume = true;
    private boolean alreadyCreated = false;

    protected void buildLogger(String tag){
        this.logger = new Logger(tag, 3);
    }

    public boolean isPaused(){
        return paused.get();
    }

    public void setScreen(Screen screen){
        if(currentScreen != null){
            currentScreen.hide();
            if(autoDisposeScreen)currentScreen.dispose();
        }
        currentScreen = screen;
        if(currentScreen != null){
            currentScreen.show();
            screenManager = new ScreenManager(currentScreen);
        }else screenManager = NULL_MANAGER;
    }

    /* -- Input -- */
    public void addInputProcessor(InputProcessor processor){
        inputManager.addProcessor(processor);
    }
    public void removeInputProcessor(InputProcessor processor){
        inputManager.removeProcessor(processor);
    }

    /* -- Implementation -- */

    @Override
    public void create() {
        if(alreadyCreated && fixAndroidResume){
            resume();
            return;
        }
        alreadyCreated = true;
        Gdx.input.setInputProcessor(inputManager);
    }

    @Override
    public void resize(int width, int height) {
        screenManager.resize(width,height);
    }

    @Override
    public void render() {
        screenManager.render();
    }

    @Override
    public void pause() {
        paused.set(true);
        screenManager.pause();
    }

    @Override
    public void resume() {
        paused.set(false);
        screenManager.resume();
    }

    @Override
    public void dispose() {
        screenManager.dispose();
    }

    /* -- ScreenManager -- */

    private static final ApplicationListener NULL_MANAGER = new ApplicationListener() {
        @Override
        public void create() {}
        @Override
        public void resize(int width, int height) {}
        @Override
        public void render() {}
        @Override
        public void pause() {}
        @Override
        public void resume() {}
        @Override
        public void dispose() {}
    };

    private static final class ScreenManager implements ApplicationListener{

        private final Screen screen;
        private ScreenManager(Screen screen){
            this.screen = screen;
        }
        @Override
        public void create() {}

        @Override
        public void resize(int width, int height) {
            screen.resize(width,height);
        }
        @Override
        public void render() {
            screen.render(Gdx.graphics.getDeltaTime());
        }
        @Override
        public void pause() {
            screen.pause();
        }
        @Override
        public void resume() {
            screen.resume();
        }
        @Override
        public void dispose() {
            screen.dispose();
        }
    }

}
