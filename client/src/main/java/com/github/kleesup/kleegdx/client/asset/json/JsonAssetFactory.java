package com.github.kleesup.kleegdx.client.asset.json;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.github.kleesup.kleegdx.client.asset.AssetLoader;

import java.util.function.Function;

public class JsonAssetFactory {

    protected final AssetLoader loader;
    protected final JsonReader reader;
    protected final Json json;
    public JsonAssetFactory(AssetLoader loader){
        this.loader = loader;
        this.reader = new JsonReader();
        this.json = new Json();
    }

    protected void registerJson(){}

    public <C extends JsonAssetConfig, T extends JsonAsset<C>> T build(Class<C> configClass,
            FileHandle file, Function<C, T> instanceBuilder){
        JsonValue root = reader.parse(file);
        C config = json.readValue(configClass, root);
        T instance = instanceBuilder.apply(config);
        instance.load(loader);
        return instance;
    }

}
