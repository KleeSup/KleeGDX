package com.github.kleesup.kleegdx.client.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import lombok.AllArgsConstructor;

public abstract class ResizeListener implements EventListener {


    @Override
    public boolean handle(Event event) {
        if(!(event instanceof ResizeEvent))return false;
        resized(event.getTarget(), ((ResizeEvent) event).width, ((ResizeEvent) event).height);
        return false;
    }

    public abstract void resized(Actor target, int width, int height);

    @AllArgsConstructor
    public static class ResizeEvent extends Event{

        public int width, height;

    }

}
