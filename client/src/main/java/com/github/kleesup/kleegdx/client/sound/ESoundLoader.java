package com.github.kleesup.kleegdx.client.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

public class ESoundLoader extends AsynchronousAssetLoader<ESound, ESoundLoader.ESoundParameter> {

    private ESound sound;

    public ESoundLoader (FileHandleResolver resolver) {
        super(resolver);
    }

    /** Returns the {@link ESound} instance currently loaded by this {@link ESoundLoader}.
     *
     * @return the currently loaded {@link ESound}, otherwise {@code null} if no {@link ESound} has been loaded yet. */
    protected ESound getLoadedSound () {
        return sound;
    }

    @Override
    public void loadAsync (AssetManager manager, String fileName, FileHandle file, ESoundParameter parameter) {
        sound = new ESound(Gdx.audio.newSound(file));
    }

    @Override
    public ESound loadSync (AssetManager manager, String fileName, FileHandle file, ESoundParameter parameter) {
        ESound sound = this.sound;
        this.sound = null;
        return sound;
    }

    @Override
    public Array<AssetDescriptor> getDependencies (String fileName, FileHandle file, ESoundParameter parameter) {
        return null;
    }

    static public class ESoundParameter extends AssetLoaderParameters<ESound> {
    }

}
