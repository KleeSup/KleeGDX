package com.github.kleesup.kleegdx.core.chat;

/**
 * Interface for entities that can execute commands. The implementing entity must be able to receive and
 * send messages as well as being able to execute commands.
 */
public interface CommandSender {

    /**
     * Checks whether the command sender has a specific permission granted.
     * @param perm The permission to check for. For {@code null} or where {@link String#isEmpty()} returns
     *             {@code true}, this method should return {@code true} as they represent 'empty' or no permissions.
     * @return {@code true} if the command sender has the specified permission, {@code false} otherwise.
     */
    boolean hasPermission(String perm);

    /**
     * Sends a message to the implementing entity. This can be a chat or console message, depending on what this
     * entity represents.
     * @param message The message to send.
     */
    void sendMessage(String message);

    /**
     * Forces a message to be sent from this entity. Therefore, it will trigger a chat input with the origin of the
     * entity this method was called on.
     * @param message The message to force send.
     */
    void forceSend(String message);

    /**
     * Forces a command execution for this entity. However, this does not implicit that the execution will never
     * fail as this method should typically not avoid permission checks.
     * @param command The command to force execute.
     */
    void forceExecute(Command command);


}
