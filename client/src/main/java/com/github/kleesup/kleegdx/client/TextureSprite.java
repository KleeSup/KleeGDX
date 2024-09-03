package com.github.kleesup.kleegdx.client;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * A wrapper class for {@link Texture} and {@link TextureRegion} that can manage and render both.
 * Switching freely between them is always possible via {@link #setTexture(Texture)} or
 * {@link #setRegion(TextureRegion)} depending on what is wanted. Further modifications will then be done via
 * multiple public attributes or via {@link #reset()}.
 */
public class TextureSprite {

    //attributes
    public float x, y, width, height, scaleX = 1, scaleY = 1, originX, originY, rotation = 0;
    private boolean flipX, flipY;

    private TextureRegion region;
    private Texture texture;
    public TextureSprite(Texture texture){
        this.texture = texture;
    }
    public TextureSprite(TextureRegion region){
        this.region = region;
    }

    public void setTexture(Texture texture){
        this.texture = texture;
        this.region = null;
    }
    public void setRegion(TextureRegion region){
        this.texture = null;
        this.region = region;
    }

    public void reset(){
        if(isFullTexture()){
            width = texture.getWidth();
            height = texture.getHeight();
        }else{
            width = region.getRegionWidth();
            height = region.getRegionHeight();
        }
        scaleX = 1;
        scaleY = 1;
        originX = width * .5f;
        originY = height * .5f;
        rotation = 0;
        setFlip(false, false);
    }

    public TextureSprite copy(){
        TextureSprite sprite = isFullTexture() ? new TextureSprite(texture) : new TextureSprite(region);
        sprite.x = x;
        sprite.y = y;
        sprite.width = width;
        sprite.height = height;
        sprite.originX = originX;
        sprite.originY = originY;
        sprite.scaleX = scaleX;
        sprite.scaleY = scaleY;
        sprite.setFlip(flipX, flipY);
        sprite.rotation = rotation;
        return sprite;
    }

    public boolean isFullTexture(){
        return texture != null;
    }

    public Texture getAsTexture(){
        return isFullTexture() ? texture : region.getTexture();
    }
    public TextureRegion getAsRegion(){
        return isFullTexture() ? new TextureRegion(texture) : region;
    }

    public void draw(Batch batch, float x, float y, float width, float height){
        if(isFullTexture()){
            batch.draw(texture, x, y, originX, originY, width, height, scaleX, scaleY, rotation, 0, 0,
                texture.getWidth(), texture.getHeight(), flipX, flipY);
        }else{
            batch.draw(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
        }
    }

    public void draw(Batch batch, float x, float y){
        draw(batch, x, y, width, height);
    }

    public void draw(Batch batch){
        draw(batch, x, y, width, height);
    }

    public void setFlip(boolean flipX, boolean flipY){
        setFlipX(flipX);
        setFlipY(flipY);
    }
    public void setFlipX(boolean flipX){
        if(isFullTexture())this.flipX = flipX;
        else if(region.isFlipX() != flipX) region.flip(true, false);
    }
    public void setFlipY(boolean flipY){
        if(isFullTexture())this.flipY = flipY;
        else if(region.isFlipY() != flipY) region.flip(false, true);
    }

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
    }
    public void setPosition(Vector2 position){
        this.x = position.x;
        this.y = position.y;
    }


}
