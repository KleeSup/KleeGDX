package com.github.kleesup.kleegdx.client.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.kleesup.kleegdx.client.Game2d;

/**
 * An implementation of {@link BaseScreen} that focuses on Scene2d. Therefore, it differs from {@link BaseScreen2d}
 * because it doesn't require a {@link com.badlogic.gdx.utils.viewport.Viewport} and further also doesn't render to any
 * batch.
 */
public abstract class BaseUIScreen2d<Main extends Game2d> extends BaseScreen<Main> {

    protected final Stage stage;
    protected Table rootTable;
    protected BaseUIScreen2d(Main main) {
        super(main);
        stage = main.getStage();
    }

    /**
     * Builds a root table for the stage to push objects onto. Through generics, it can be any type.
     * @param table The table instance to make the root table.
     * @return The root table.
     */
    protected <T extends Table> T buildRootTable(T table){
        if(rootTable != null)removeRoot();
        table.setFillParent(true);
        stage.addActor(table);
        rootTable = table;
        return table;
    }

    /**
     * Does the same as {@link #buildRootTable(Table)} but builds with Scene2d default {@link Table}.
     * @return The root table.
     */
    protected Table buildRootTable(){
        return buildRootTable(new Table());
    }

    /**
     * Removes the current root table.
     */
    protected void removeRoot(){
        main.getStage().getActors().removeValue(rootTable, true);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        main.getStage().act();
        main.getStage().draw();
    }

    /* -- Empty implemented methods -- */

    @Override
    protected void beforeRender(float delta) {

    }

    @Override
    protected void onRender(float delta) {

    }

    @Override
    protected void afterRender(float delta) {

    }

    @Override
    public void resize(int i, int i1) {
        stage.getViewport().update(i,i1,true);
    }

    @Override
    public void hide() {
        removeRoot();
    }

    @Override
    public void dispose() {
        removeRoot();
    }
}
