package com.github.kleesup.kleegdx.client.asset;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.utils.Disposable;

import java.util.function.Supplier;

/**
 * A class that holds one specific resource. Different implementations receive values differently, see
 * {@link LoadAsset} and {@link BuildAsset}. The asset is designed to be stored (statically) in a variable
 * somewhere to not refer to IDs.
 * <p><pre>Tip: For static asset classes use interfaces as they reduce Javas boilerplate as variables defined in interfaces
 * are <code>public static final</code> by default.<pre>
 * {@code public interface Assets{
 *     Asset<Texture> asset = myAssetLoader.qL("myasset.png", Texture.class);
 *     ...
 * } }
 * </pre></p>
 * @param <T> The type of the asset.
 */
public class Asset<T> implements Disposable {

    T value;
    public T get(){
        return value;
    }
    void deploy(T value){
        this.value = value;
    }
    public boolean isLoaded(){
        return value != null;
    }
    protected boolean needsDispose(){
        return value instanceof Disposable;
    }
    @Override
    public void dispose() {
        if(isLoaded() && needsDispose()) ((Disposable) value).dispose();
    }

    /* -- Different asset implementations -- */

    /**
     * An implementation of {@link Asset} that will handle assets that require a load via an
     * {@link com.badlogic.gdx.assets.AssetManager} therefore holding a {@link AssetDescriptor} object.
     * @param <T> The type of the asset.
     */
    static final class LoadAsset<T> extends Asset<T>{
        AssetDescriptor<T> descriptor;
        LoadAsset(AssetDescriptor<T> descriptor){
            this.descriptor = descriptor;
        }

        @Override
        void deploy(T value) {
            super.deploy(value);
            //free memory
            descriptor = null;
        }
    }

    /**
     * An implementation of {@link Asset} that will handle assets that require a custom builder function
     * to be loaded. Furthermore, it can have dependencies for more build customization.
     * @param <T> The type of the asset.
     */
    static final class BuildAsset<T> extends Asset<T>{
        Supplier<T> builder;
        Asset<?>[] dependencies;
        public BuildAsset(Supplier<T> builder, Asset<?>... dependencies) {
            this.builder = builder;
            this.dependencies = dependencies;
        }

        boolean hasDependencies(){
            return dependencies != null && dependencies.length != 0;
        }

        boolean readyToBuild(){
            if(!hasDependencies())return true;
            for(Asset<?> asset : dependencies){
                if(asset == null)continue;
                if(!asset.isLoaded())return false;
            }
            return true;
        }

        @Override
        void deploy(T value) {
            super.deploy(value);
            //free memory
            this.builder = null;
            this.dependencies = null;
        }
    }

}
