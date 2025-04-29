package com.github.kleesup.kleegdx.client.asset.json;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.github.kleesup.kleegdx.client.asset.AssetLoader;

import java.util.function.Function;

/**
 * The main part of the json serialization addition in KleeGDX. This factory supports multiple-type json config reading
 * and instance loading.
 * <p>
 * The reason why this was created is that there are often json files containing paths to other assets (therefore
 * bundled assets) which then need to be loaded as json file and further loaded by an {@link AssetLoader}.
 * The {@link JsonAsset} class is supposed to represent one of these bundled assets. It provides a config which is then
 * loaded in this factory.
 * </p>
 * For simpler single-case targeted implementation see {@link SimplisticJsonAssetFactory}.
 */
public class JsonAssetFactory {

    protected final AssetLoader loader;
    protected final JsonReader reader;
    protected final Json json;
    public JsonAssetFactory(AssetLoader loader){
        this.loader = loader;
        this.reader = new JsonReader();
        this.json = new Json();
        registerJson(json);
    }

    /**
     * Overridable method that can be used to append possible serializers or other settings to the internal
     * {@link Json} instance.
     * @param json The json instance to register further settings to.
     */
    protected void registerJson(Json json){}

    /**
     * Builds an asset instance based of the config file, its class type and an instance builder function that takes
     * in the loaded json from file and returns the build instance. This instance is than loaded through the asset loader
     * and finally returned. Note that queueing into the asset loader doesn't necessarily load the content instantly.
     * @param configClass The class type of the config represented as serialized json file.
     * @param file The json file config.
     * @param instanceBuilder The builder function taking the loaded config returning an unloaded instance.
     * @return The load-queued instance.
     * @param <C> The type of the config.
     * @param <T> The type of the json asset.
     */
    public <C, T extends JsonAsset<C>> T build(Class<C> configClass,
                                               FileHandle file, Function<C, T> instanceBuilder){
        JsonValue root = reader.parse(file);
        C config = json.readValue(configClass, root);
        T instance = instanceBuilder.apply(config);
        instance.load(loader);
        return instance;
    }

}
