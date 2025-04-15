package com.github.kleesup.kleegdx.client.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.kleesup.kleegdx.client.g2d.TextureSprite;
import lombok.Setter;

/**
 * Simple progression bar which supports three colors for background, border and bar.
 * Can be used for testing or simplistic health bars.
 */
@Setter
public class SimpleProgressBar extends Actor {

    private static final TextureSprite sprite;
    static {
        Pixmap pixmap = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.drawPixel(0,0);
        sprite = new TextureSprite(new Texture(pixmap));
    }

    private Color background, bar, border;
    private float max;
    private float value;
    private float borderSize;
    public SimpleProgressBar(float max, float borderSize){
        setMax(max);
        setBorderSize(borderSize);
    }
    public SimpleProgressBar(float max){
        this(max, 0);
    }

    private float borderSize2, borderSize3;
    public void setBorderSize(float borderSize) {
        this.borderSize = Math.max(0, borderSize);
        this.borderSize2 = borderSize * 2;
        this.borderSize3 = borderSize * 3;
    }

    public void setColors(Color background, Color bar, Color border){
        this.background = background;
        this.bar = bar;
        this.border = border;
    }

    /* -- Values -- */

    private float invValue;
    private void calculateInvValue(){
        invValue = value / max;
    }
    public void setMax(float max) {
        this.max = Math.max(0, max);
        calculateInvValue();
    }
    public void setValue(float value) {
        this.value = MathUtils.clamp(value, 0, max);
        calculateInvValue();
    }
    public void drain(float value){
        setValue(this.value - value);
    }
    public void replenish(float value){
        setValue(this.value + value);
    }

    /* -- Rendering -- */

    private final Color writeColor = new Color();
    @Override
    public void draw(Batch batch, float parentAlpha) {
        writeColor.set(batch.getColor());
        sprite.setPosition(getX(), getY());
        sprite.scaleX = getScaleX();
        sprite.scaleY = getScaleY();
        sprite.width = getWidth();
        sprite.height = getHeight();
        sprite.originX = getOriginX();
        sprite.originY = getOriginY();

        float bx = getX(); //pos with border offset
        float by = getY(); //pos with border offset
        if(border != null){
            bx += borderSize;
            by += borderSize;
            sprite.setColor(border.r, border.g, border.b, border.a * parentAlpha);
            sprite.draw(batch);
        }

        //no border width/height
        float nbw = getWidth()-borderSize3; //I have no idea why this is *3 instead of 2 but only way it works.
        float nbh = getHeight()-borderSize2;

        //render background
        if(value != max){ //no render call needed if overridden by value
            sprite.setColor(background.r, background.g, background.b, background.a * parentAlpha);
            sprite.draw(batch, bx, by, nbw, nbh);
        }

        //render value
        if(value != 0){ //no render if there isn't any value
            sprite.setColor(bar.r, bar.g, bar.b, bar.a * parentAlpha);
            sprite.draw(batch, bx, by, (value / max) * nbw, nbh);
        }

        batch.setColor(writeColor);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
