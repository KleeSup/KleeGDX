package com.github.kleesup.kleegdx.client.asset.json;

import com.badlogic.gdx.files.FileHandle;
import com.github.kleesup.kleegdx.client.asset.AssetLoader;

import java.util.function.Function;

/**
 * A simple implementation of {@link JsonAssetFactory} that can produce json assets for simple single target use cases.
 * @param <C> The type of the config class.
 * @param <T> The type of the asset implementation.
 */
public abstract class SimplisticJsonAssetFactory<C, T extends JsonAsset<C>> extends JsonAssetFactory {

    private final Class<C> configClass;
    private final Function<C, T> builder;
    public SimplisticJsonAssetFactory(Class<C> configClass, AssetLoader loader) {
        super(loader);
        this.configClass = configClass;
        this.builder = this::buildInstance;
    }

    /**
     * Calls the super {@link JsonAssetFactory#build(Class, FileHandle, Function)} with the given {@link #configClass}
     * and {@link #builder}.
     * @param file The json file to build from.
     * @return The created and possibly loaded instance.
     */
    public T build(FileHandle file){
        return super.build(configClass, file, builder);
    }

    /**
     * Called whenever {@link JsonAssetFactory#build(Class, FileHandle, Function)} calls the function.
     * @param config The json config instance.
     * @return The built instance.
     */
    protected abstract T buildInstance(C config);


}
