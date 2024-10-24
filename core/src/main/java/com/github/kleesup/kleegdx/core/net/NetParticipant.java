package com.github.kleesup.kleegdx.core.net;

/**
 * Classes implementing this interface can receive net objects/packets.
 * Usually this is called from a server instance where the implementing class of this interface holds some sort
 * of connection to the client and will then forward objects.
 * <p>An implementation for a <code>player class</code> could look like this (remote server):</p>
 * <pre>{@code
 *     public class Player implements NetParticipant{
 *         private Connection connection; //could be a {@link com.esotericsoftware.kryonet.Connection} from Kryo.
 *
 *         public void send(Object obj){
 *             connection.send(obj);
 *         }
 *         public boolean isConnected(){
 *             return connection.isConnected();
 *         }
 *     }
 * }</pre>
 * <p>And for an integrated player class the implementation could look like:</p>
 * <pre>{@code
 *     public class ClientPlayer extends Player{
 *          public void send(Object obj){
 *              MyClientServerBridge.getInstance().processFromServer(obj);
 *          }
 *          public boolean isConnected(){
 *              return IntegratedServer.getInstance().isServerRunning();
 *          }
 *     }
 * }</pre>
 * <p>The second further implementation avoids object serialization for integrated servers.</p>
 */
public interface NetParticipant {

    /**
     * Sends an object to the participants end.
     * @param obj The object to use.
     * @param udp Whether to use UDP.
     */
    void send(Object obj, boolean udp);

    /** See {@link #send(Object, boolean)}. */
    default void send(Object obj){
        send(obj, false);
    }

    /**
     * @return {@code true} if this participant is still connected, {@code false} otherwise.
     */
    boolean isConnected();

}
