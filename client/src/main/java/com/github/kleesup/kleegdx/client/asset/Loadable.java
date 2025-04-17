package com.github.kleesup.kleegdx.client.asset;

/**
 * Implemented in classes that have some content that can be or is required to be loaded by an {@link AssetLoader}.
 */
public interface Loadable {

    /**
     * Loads necessary content into memory.
     * @param loader The loader to load the assets.
     */
    void load(AssetLoader loader);

}
