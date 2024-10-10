package com.github.kleesup.kleegdx.core.chat;

import lombok.Getter;

/**
 * A class representing a single command with a call-name, permission and aliases.
 * <l>
 *    <li>The {@code name} represents the main call identifier of this command (i.e. <code>help</code>}).</li>
 *    <li>The {@code aliases} represent the other call identifiers possible for this command
 *    (i.e. <code> ?, helpme, list, options</code>). This is optional.</li>
 *    <li>The {@code permission} represents the permission needed to execute this command. This is optional.</li>
 * </l>
 * Depending on implementation, a command could technically be used for multiple commands by adding all the commands
 * to one alias list and then creating a switch statement in the {@link #onExecute(CommandSender, String[])} to handle
 * all different commands.
 */
@Getter
public abstract class Command {

    /** Empty array to avoid {@code null} aliases. */
    private static final String[] EMPTY_ALIASES = new String[]{};

    /**
     * Formats a command name or alias correctly by:
     * <l>
     *     <li>Lowercasing it so it can be found in all maps without importance of input.</li>
     *     <li>Trimming empty spaces from left/right.</li>
     * </l>
     * @param nameOrAlias The name or alias of a command.
     * @return The formatted name or alias.
     */
    public static String formatCorrectly(String nameOrAlias){
        return nameOrAlias.toLowerCase().trim();
    }

    private final String name;
    private final String permission;
    private final String[] aliases;
    public Command(String name, String permission, String... aliases){
        this.name = name;
        this.permission = permission;
        this.aliases = aliases == null ? EMPTY_ALIASES : aliases;
    }
    public Command(String name, String... aliases){
        this(name,null,aliases);
    }
    public Command(String name, String permission){
        this(name,permission,EMPTY_ALIASES);
    }

    /**
     * Executes this commands function.
     * @param sender The command sender who executed this command.
     * @param label The full input for the command. This therefore combines the name or an alias with all the
     *              parameters given. Therefore, a label with the size of 3 could look like
     *              <code>["teleport", "playerA", "location"]</code> whereas <code>teleport</code> marks the commands
     *              name or one of the aliases.
     *              Furthermore, if the same command was called with different alias, the label would look like
     *              <code>["tp", "playerA", "location"]</code>.
     */
    public abstract void onExecute(CommandSender sender, String[] label);

    public boolean needsPermission(){
        return permission != null && !permission.isEmpty();
    }

    public boolean hasAliases(){
        return aliases != null && aliases.length != 0;
    }

}
