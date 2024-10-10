package com.github.kleesup.kleegdx.core.net;

import java.util.Random;

/**
 * Static utility class for networking related things.
 * @author KleeSup
 * @version 1.0
 * @since 1.0.0
 */
public final class NetUtil {
    private NetUtil(){}

    /* -- Constants -- */
    public static final int MIN_REC_PORT = 49151;
    public static final int MAX_PORT = 65535;

    /* -- Port -- */
    private static final Random portRandom = new Random();
    public static int generateRandomPort(Random portRandom){
        return portRandom.nextInt(MAX_PORT - MIN_REC_PORT) + MIN_REC_PORT;
    }
    public static int generateRandomPort(){
        return generateRandomPort(portRandom);
    }

    public static boolean isValidPort(int port){
        return port >= 0 && port <= MAX_PORT;
    }
    public static boolean isValidRecommendedPort(int port){
        return port >= MIN_REC_PORT && port <= MAX_PORT;
    }

}
