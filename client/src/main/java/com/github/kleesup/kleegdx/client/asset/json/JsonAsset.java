package com.github.kleesup.kleegdx.client.asset.json;

import com.badlogic.gdx.utils.Disposable;
import com.github.kleesup.kleegdx.client.asset.AssetLoader;
import com.github.kleesup.kleegdx.client.asset.Loadable;

/**
 * A simple class representing a Json asset which has a config class used for Json serialization.
 * For more information see
 * <a href="libgdx.com/wiki/utils/reading-and-writing-json#customizing-serialization">LibGDX Json Documentation</a>.
 */
public abstract class JsonAsset<Config> implements Loadable, Disposable {

    protected Config config;
    public JsonAsset(Config config){
        this.config = config;
    }

    /**
     * Can be called at the end of {@link #load(AssetLoader)} in case the config isn't needed any longer.
     */
    public void disposeConfig(){
        if(config instanceof Disposable)((Disposable) config).dispose();
        config = null;
    }

}
