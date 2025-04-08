package com.github.kleesup.kleegdx.client.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.Viewport;

public class UiStage extends Stage {

    protected final Table uiRoot;

    public UiStage() {
        uiRoot = buildUiRoot();
        addActor(uiRoot);
    }

    public UiStage(Viewport viewport) {
        super(viewport);
        uiRoot = buildUiRoot();
        addActor(uiRoot);
    }

    public UiStage(Viewport viewport, Batch batch) {
        super(viewport, batch);
        uiRoot = buildUiRoot();
        addActor(uiRoot);
    }

    // -- Content --

    /**
     * Builds a new instance of {@link Table} which is then used as root table for any ui elements.
     * @return The newly build root table.
     */
    protected Table buildUiRoot(){
        Table root = new Table();
        root.setFillParent(true);
        return root;
    }

    @Override
    public void addActor(Actor actor) {
        getRoot().addActorAt(0,actor);
    }

    /**
     * Adds an actor directly on top of the UI root table.
     * @param actor The actor to add.
     */
    public void addActorOnTop(Actor actor){
        super.addActor(actor);
    }

    /**
     * Checks whether the ui root is still animating. All children will also be checked.
     * @return {@code true} if the root or its children have running actions, {@code false} otherwise.
     */
    public boolean isUiRootAnimating(){
        if(uiRoot.hasActions())return true;
        return checkChildAnimating(getRoot());
    }

    /**
     * Checks if an actor or any of its children has running actions. This function is used recursively.
     * @param actor THe actor to check for actions.
     * @return {@code true} if the actor or its children have running actions, {@code false} otherwise.
     */
    private boolean checkChildAnimating(Actor actor){
        if(actor == null)return false;
        if(actor.hasActions())return true;
        if(actor instanceof Group){
            SnapshotArray<Actor> children = ((Group) actor).getChildren();
            for(Actor child : children){
                boolean animates = checkChildAnimating(child);
                if(animates)return true;
            }
        }
        return false;
    }

    public void resize(int width, int height, boolean centerCamera){
        getViewport().update(width,height,centerCamera);
    }
    public void resize(int width, int height){
        resize(width,height,true);
    }


}
