package com.github.kleesup.kleegdx.client.asset.json;

import com.badlogic.gdx.utils.Disposable;
import com.github.kleesup.kleegdx.client.asset.Loadable;

public abstract class JsonAsset<Config extends JsonAssetConfig> implements Loadable, Disposable {

    protected Config config;
    public JsonAsset(Config config){
        this.config = config;
    }

    public void disposeConfig(){
        if(config instanceof Disposable)((Disposable) config).dispose();
        config = null;
    }

}
