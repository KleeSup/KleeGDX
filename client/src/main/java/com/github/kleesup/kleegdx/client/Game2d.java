package com.github.kleesup.kleegdx.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import lombok.Getter;
import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * Implementation of {@link BaseGame} for 2d games. By default, it builds a {@link SpriteBatch}, a {@link ShapeDrawer}
 * and a {@link Stage}.
 */
@Getter
public abstract class Game2d extends BaseGame implements ApplicationListener {

    @Getter
    private static Game2d instance;

    /* -- Instances -- */

    protected Batch batch;
    protected ShapeDrawer shapeDrawer;
    protected Stage stage;

    /* -- ApplicationListener implementation -- */

    @Override
    protected void buildApp() {
        instance = this;
        batch = new SpriteBatch();
        shapeDrawer = buildDrawer();
        stage = new Stage();
        inputManager.addProcessor(stage);
    }

    /**
     * Sets a new stage instance.
     * @param newStage The new stage instance to set.
     * @param disposeOld Whether to dispose the previous one.
     */
    public void setStage(Stage newStage, boolean disposeOld){
        inputManager.removeProcessor(stage);
        if(disposeOld)stage.dispose();
        this.stage = newStage;
        inputManager.addProcessor(stage);
    }
    public void setStage(Stage newStage){
        setStage(newStage, true);
    }

    /**
     * Builds a drawer instance with a 1x1 pixel texture in white.
     * @return The build drawer instance.
     */
    private ShapeDrawer buildDrawer(){
        Pixmap pixmap = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.drawPixel(0,0);
        return new ShapeDrawer(batch, new TextureRegion(new Texture(pixmap)));
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
        batch.dispose();
        shapeDrawer.getRegion().getTexture().dispose();
    }
}
