package com.github.kleesup.kleegdx.client.asset;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Disposable;
import com.github.kleesup.kleegdx.core.collection.MappedCollection;
import com.github.kleesup.kleegdx.core.util.Verify;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * A class used to load {@link Asset} (and implementations). Internally it uses a {@link AssetManager} but
 * since {@link Asset.BuildAsset} are also supported, this class further expands the loading process.
 */
public class AssetLoader implements Disposable {

    /** The time for a loading timeout */
    private static final long LOAD_DEADLOCK = TimeUnit.SECONDS.toMillis(12);

    private boolean ownsManager;
    private final AssetManager manager;
    /** All assets that needed to be loaded through the {@link AssetManager} */
    private final LinkedList<Asset.LoadAsset<?>> TO_LOAD = new LinkedList<>();
    /** All build assets, separated into whether they have dependencies or not */
    private final MappedCollection<Boolean, Asset.BuildAsset<?>> TO_BUILD =
            MappedCollection.buildHashMapLinkedList();
    /** All assets where {@link Asset#needsDispose()} is {@code true} */
    private final LinkedList<Asset<?>> toDispose = new LinkedList<>();
    public AssetLoader(AssetManager manager){
        Verify.nonNullArg(manager, "AssetManager cannot be null!");
        this.manager = manager;
        this.ownsManager = false;
    }
    public AssetLoader(){
        this(new AssetManager());
        this.ownsManager = true;
    }

    /**
     * See {@link AssetManager#update()}. Further calls {@link #afterFinish()} when finished.
     * @return Whether the loading was finished.
     */
    public boolean update(){
        if(manager.update()){
            afterFinish();
            return true;
        }
        return false;
    }

    /**
     * Forces the manager to finish loading.
     */
    public void finish(){
        manager.finishLoading();
        afterFinish();
    }

    /**
     * Called when the asset loading was finished. Does final asset writing and cleanup.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void afterFinish(){
        //first, write to all assets that are loaded through the AssetManager
        for(Asset.LoadAsset asset : TO_LOAD){
            asset.deploy(manager.get((AssetDescriptor<Object>) asset.descriptor));
        }
        TO_LOAD.clear();
        //now build all build assets without dependencies
        if(TO_BUILD.isEmpty())return;
        for(Asset.BuildAsset asset : TO_BUILD.getCollection(false)){
            asset.deploy(asset.builder.get());
        }
        //and finally build those with dependencies
        long start = System.currentTimeMillis();
        while (!TO_BUILD.getCollectionOrEmpty(true).isEmpty()){
            Iterator<Asset.BuildAsset<?>> itr = TO_BUILD.getCollectionOrEmpty(true).iterator();
            while (itr.hasNext()){
                Asset.BuildAsset asset = itr.next();
                if(asset.readyToBuild()){
                    asset.deploy(asset.builder);
                    itr.remove();
                }
            }
            long time = System.currentTimeMillis() - start;
            if(time >= LOAD_DEADLOCK){
                throw new RuntimeException("Tried to solve for all build assets that have dependencies"+
                        " which took "+time+"ms! Check your build asset dependencies, there might be"+
                        " a deadlock situation!");
            }
        }
        TO_BUILD.clearAll();
        System.gc();
    }

    public boolean isFinished(){
        return manager.isFinished() && TO_BUILD.isEmpty();
    }

    @Override
    public void dispose() {
        for(Asset<?> asset : toDispose){
            asset.dispose();
        }
        if(ownsManager)manager.dispose();
    }

    /* -- Queueing -- */

    public <T> Asset<T> queueLoad(AssetDescriptor<T> descriptor){
        Verify.nonNullArg(descriptor, "AssetDescriptor cannot be null!");
        Asset.LoadAsset<T> asset = new Asset.LoadAsset<>(descriptor);
        TO_LOAD.add(asset);
        if(asset.needsDispose())toDispose.add(asset);
        return asset;
    }
    public <T> Asset<T> queueLoad(String file, Class<T> clazz){
        Verify.nonNullArg(file, "File cannot be null!");
        Verify.nonNullArg(clazz, "Class cannot be null!");
        return queueLoad(new AssetDescriptor<>(file, clazz));
    }
    public <T> Asset<T> qL(String f, Class<T> c){
        return queueLoad(f,c);
    }

    public <T> Asset<T> queueBuild(Supplier<T> builder, Asset<?>... dependencies){
        Verify.nonNullArg(builder, "Builder cannot be null!");
        Asset.BuildAsset<T> asset = new Asset.BuildAsset<>(builder, dependencies);
        TO_BUILD.add(asset.hasDependencies(), asset);
        if(asset.needsDispose())toDispose.add(asset);
        return asset;
    }
    public <T> Asset<T> qB(Supplier<T> b, Asset<?>... d){
        return queueBuild(b,d);
    }

}
