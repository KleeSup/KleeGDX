package com.github.kleesup.kleegdx.client.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

public class EMusicLoader extends AsynchronousAssetLoader<EMusic, EMusicLoader.EMusicParameter> {

    private EMusic music;

    public EMusicLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    /**
     * Returns the {@link EMusic} instance currently loaded by this {@link EMusicLoader}.
     * @return the currently loaded {@link EMusic}, otherwise {@code null} if no {@link EMusic} has been loaded yet.
     */
    protected EMusic getLoadedMusic() {
        return music;
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, EMusicParameter parameter) {
        music = new EMusic(Gdx.audio.newMusic(file));
    }

    @Override
    public EMusic loadSync(AssetManager manager, String fileName, FileHandle file, EMusicParameter parameter) {
        EMusic music = this.music;
        AudioEngine.getInstance().loadMusic(music);
        this.music = null;
        return music;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, EMusicParameter parameter) {
        return null;
    }

    static public class EMusicParameter extends AssetLoaderParameters<EMusic> {}

}
