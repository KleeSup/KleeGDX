package com.github.kleesup.kleegdx.core.concurrent;

import com.github.kleesup.kleegdx.core.net.GameServer;
import lombok.Getter;

/**
 * A simple thread updating a server. Usually activated when automatic updating is enabled in {@link GameServer}.
 */
public class ServerUpdateThread extends Thread {

    private final GameServer server;
    private final float inv;
    private final long max;
    @Getter
    private boolean running = true;
    public ServerUpdateThread(GameServer server, int ticks){
        super("ServerTick");
        this.server = server;
        this.inv = 1f / 1000f;
        this.max = 1000 / ticks;
    }

    private long last;

    @Override
    public synchronized void start() {
        super.start();
        last = System.currentTimeMillis();
    }

    /**
     * Terminates this thread without the need to call {@link #stop()} (safe killing thread).
     */
    public synchronized void terminate(){
        running = false;
    }

    @Override
    public void run() {
        while (running){
            long delta = System.currentTimeMillis() - last;
            if(delta >= max){
                server.update(delta * inv);
                last = System.currentTimeMillis();
            }
        }
    }
}
