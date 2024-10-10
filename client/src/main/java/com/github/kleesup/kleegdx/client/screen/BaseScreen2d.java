package com.github.kleesup.kleegdx.client.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.kleesup.kleegdx.client.Game2d;

/**
 * An implementation of {@link BaseScreen} which can be used for 2d screen creation.
 * Needs a viewport on creation and then does all the rendering and management.
 */
public abstract class BaseScreen2d<Main extends Game2d> extends BaseScreen<Main> {

    protected Viewport viewport;
    protected BaseScreen2d(Main main, Viewport viewport) {
        super(main);
        this.viewport = viewport;
    }

    /* -- Coordinate translation -- */

    protected final Vector2 _trs = new Vector2();
    /**
     * Translates a screen coordinate to its worlds coordinate.
     * @param x The x-value of the screen coordinate.
     * @param y The y-value of the screen coordinate.
     * @return The transformed world coordinate.
     */
    protected Vector2 unproject(float x, float y){
        return viewport.unproject(_trs.set(x,y));
    }
    /** See {@link #unproject(float, float)} */
    protected Vector2 unproject(Vector2 position){
        return unproject(position.x, position.y);
    }
    /**
     * Translates the cursor coordinate to its worlds coordinate.
     * @return The transformed world coordinate.
     */
    protected Vector2 unprojectMouse(){
        return unproject(Gdx.input.getX(), Gdx.input.getY());
    }

    /**
     * Translates a world coordinate to its screen coordinate.
     * @param x The x-value of the world coordinate.
     * @param y The y-value of the world coordinate.
     * @return The transformed screen coordinate.
     */
    protected Vector2 project(float x, float y){
        return viewport.project(_trs.set(x,y));
    }
    /** See {@link #project(float, float)} */
    protected Vector2 project(Vector2 position){
        return project(position.x, position.y);
    }
    /**
     * Translates the cursor coordinate to its screen coordinate.
     * @return The transformed screen coordinate.
     */
    protected Vector2 projectMouse(){
        return project(Gdx.input.getX(), Gdx.input.getY());
    }

    /* -- Stage -- */
    protected void drawStage(){
        Stage stage = main.getStage();
        stage.act();
        stage.draw();
    }

    /* -- Implementation from super -- */

    @Override
    public void render(float delta) {
        super.render(delta); //clear color
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //clear color buffer bit
        viewport.getCamera().update();
        beforeRender(delta);
        Batch batch = main.getBatch();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        onRender(delta);
        batch.end();
        afterRender(delta);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height,true);
    }
}
