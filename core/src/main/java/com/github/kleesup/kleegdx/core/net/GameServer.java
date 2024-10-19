package com.github.kleesup.kleegdx.core.net;

import com.badlogic.gdx.utils.Logger;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.github.kleesup.kleegdx.core.util.Updateable;
import lombok.Getter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Abstract expansion of {@link Server} from KryoNet which supports server binding, logging and updating.
 */
public abstract class GameServer extends Server {

    /* -- Server content -- */
    @Getter protected final Logger logger;
    @Getter private int port = -1;
    protected final AtomicBoolean running = new AtomicBoolean(false);
    protected final AtomicBoolean socketOpen = new AtomicBoolean(false);
    protected boolean useUDP;
    protected volatile boolean updatesAutomatically = true;
    public GameServer(boolean useUDP){
        this.logger = buildLogger();
        if(this.logger != null)this.logger.setLevel(Logger.DEBUG);
        this.useUDP = useUDP;
    }

    /* -- Logging -- */
    protected abstract Logger buildLogger();

    public void log(String msg){
        logger.info(msg);
    }

    /* -- Kryo Updating -- */

    /**
     * This method is useful to decide whether KryoNet {@link Server#update(int)} should also call
     * {@link #update(float)} from the game server. By default, this is set to {@code true}.
     * @param enable {@code true} enables the automatic updating through KryoNet, {@code false} otherwise.
     */
    protected synchronized void setUpdateAutomatically(boolean enable){
        updatesAutomatically = enable;
        if(enable)time = System.currentTimeMillis();
    }
    protected boolean doesUpdateAutomatically(){
        return updatesAutomatically;
    }

    // implementation
    private long time;
    @Override
    public void update(int timeout) throws IOException {
        super.update(timeout);
        if(updatesAutomatically){
            update((System.currentTimeMillis() - time) * Updateable.MILLIS_TO_SEC);
            time = System.currentTimeMillis();
        }
    }

    /* -- Lifecycle -- */

    @Override
    public void start() {
        if(isServerRunning())return;
        log("Starting server...");
        super.start();
        running.set(true);
        log("Server started!");
    }

    @Override
    public void stop() {
        if(!isServerRunning())return;
        log("Stopping server...");
        super.stop();
        running.set(false);
        log("Server stopped!");
    }

    /**
     * Updates the server based on the given delta value.
     * @param delta The time since the last update in seconds.
     */
    protected abstract void update(float delta);

    /* -- Socket -- */

    /**
     * Binds the server to a random generated port between {@link NetUtil#MIN_REC_PORT}
     * and {@link NetUtil#MAX_PORT}. If it fails the first time, it will try again to find a better port until
     * connection is possible.
     */
    public void bind(){
        if(!isServerRunning())start();
        log("Starting to bind server...");
        Random random = new Random();
        boolean noPort = true;
        do{
            this.port = NetUtil.generateRandomPort(random);
            try {
                if(useUDP) bind(port, port);
                else bind(port);
                noPort = false;
            }catch (IOException ignored){} //usually thrown when port is already given.
        }while (noPort);
        log("Server socket opened under port "+port+"!");
    }

    @Override
    public void bind(int tcpPort) throws IOException {
        super.bind(tcpPort);
        socketOpen.set(true);
    }
    @Override
    public void bind(int tcpPort, int udpPort) throws IOException {
        super.bind(tcpPort, udpPort);
        socketOpen.set(true);
    }
    @Override
    public void bind(InetSocketAddress tcpPort, InetSocketAddress udpPort) throws IOException {
        super.bind(tcpPort, udpPort);
        socketOpen.set(true);
    }

    @Override
    public void close() {
        log("Closing socket...");
        super.close();
        socketOpen.set(false);
        log("Socket closed!");
    }

    /* -- Saving and Disposing -- */

    @Override
    public void dispose() {
        log("Disposing server...");
        try {
            super.dispose();
            log("Server disposed!");
        } catch (IOException e) {
            logger.error(
                "An error was thrown while disposing the "+getClass().getSimpleName()+"!", e);
        }
    }

    /* -- Getter -- */

    public boolean isServerRunning(){
        return running.get();
    }

    public boolean hasSocketOpen(){
        return socketOpen.get();
    }

}
