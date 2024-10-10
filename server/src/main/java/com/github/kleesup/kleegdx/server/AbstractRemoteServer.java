package com.github.kleesup.kleegdx.server;

import com.badlogic.gdx.Gdx;
import com.github.kleesup.kleegdx.core.net.GameServer;
import com.github.kleesup.kleegdx.server.io.StandaloneFiles;


/**
 * An implementation of {@link GameServer} which implements for standalone servers.
 * It is suggested to call {@link #initFiles()} at server/program start to be able to use {@link Gdx#files} for
 * simple file management.
 */
public abstract class AbstractRemoteServer extends GameServer {

    /**
     * Initialises the {@link Gdx#files} variable to {@link StandaloneFiles} for standalone servers.
     */
    public static void initFiles(){
        Gdx.files = new StandaloneFiles();
    }

    public AbstractRemoteServer(boolean useUDP) {
        super(useUDP);
    }
}
