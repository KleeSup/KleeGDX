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
    public void create() {
        super.create();
        instance = this;
        batch = new SpriteBatch();
        shapeDrawer = buildDrawer();
        stage = new Stage();
    }

    private ShapeDrawer buildDrawer(){
        Pixmap pixmap = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.drawPixel(0,0);
        return new ShapeDrawer(batch, new TextureRegion(new Texture(pixmap)));
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width,height);
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
        batch.dispose();
        shapeDrawer.getRegion().getTexture().dispose();
    }
}
