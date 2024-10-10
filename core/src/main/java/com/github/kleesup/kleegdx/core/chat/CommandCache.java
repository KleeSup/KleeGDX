package com.github.kleesup.kleegdx.core.chat;

import com.badlogic.gdx.utils.CharArray;
import com.github.kleesup.kleegdx.core.util.Verify;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple register for commands. On this cache chat input can be performed and checked.
 */
public class CommandCache {

    /** Default interpreter used if none is applied in {@link #performInput(CommandSender, String)}. */
    public static final CommandInterpreter DEFAULT = new CommandInterpreter();

    /**
     * A class which holds information that is used in
     * {@link #performInput(CommandSender, String, CommandInterpreter)} to differentiate what input is a command
     * and what just plain text. Furthermore, {@link CommandInterpreter#setSendAsTextIfNotCommand(boolean)} decides
     * whether a command sender should {@link CommandSender#forceSend(String)} the text input if it was no command.
     */
    public static class CommandInterpreter{
        private boolean sendAsTextIfNotCommand = true;
        private final CharArray identifiers = new CharArray(3);
        public CommandInterpreter(boolean sendAsTextIfNotCommand) {
            this.sendAsTextIfNotCommand = sendAsTextIfNotCommand;
        }
        public CommandInterpreter() {}

        public boolean hasIdentifiers(){
            return !identifiers.isEmpty();
        }
        public CommandInterpreter addIdentifier(char c){
            identifiers.add(c);
            return this;
        }
        public CommandInterpreter setSendAsTextIfNotCommand(boolean sendAsTextIfNotCommand) {
            this.sendAsTextIfNotCommand = sendAsTextIfNotCommand;
            return this;
        }
    }

    /* -- Commands -- */

    public CommandCache(Map<String, Command> commands){
        this.commands.putAll(commands);
    }
    public CommandCache(){}

    private final HashMap<String, Command> commands = new HashMap<>();

    /**
     * Performs any chat input as command. Returns a result based on what the input resulted in.
     * @param sender The sender of the text.
     * @param text The text to file as command.
     * @param interpreter Interpreter for what differentiates a command from plain text and what to do with
     *                    plain text.
     * @return The result of this input.
     */
    public PerformResult performInput(CommandSender sender, String text, CommandInterpreter interpreter){
        String[] split = text.split(" ");
        String name = split[0];
        if(interpreter.hasIdentifiers()){ //if identifiers are registered, check for correct command.
            boolean found = false;
            for (char c : interpreter.identifiers.items){
                if(name.charAt(0) == c) { //at least one char has to be found.
                    found = true;
                    break;
                }
            }
            if(found){
                name = name.substring(1); //remove first char
                split[0] = name; //replace in label
            } else {
                if(interpreter.sendAsTextIfNotCommand)sender.forceSend(text);
                return PerformResult.JUST_TEXT;
            }
        }
        //finally, command handling.
        Command command = getCommand(name);
        if (command == null)return PerformResult.NO_COMMAND;
        if(command.needsPermission() && !sender.hasPermission(command.getPermission()))
            return PerformResult.NO_PERMISSION;
        command.onExecute(sender,split);
        return PerformResult.EXECUTION;
    }
    /** See {@link #performInput(CommandSender, String, CommandInterpreter)} */
    public PerformResult performInput(CommandSender sender, String text){
        return performInput(sender,text,DEFAULT);
    }

    /**
     * Registers a command to this cache.
     * @param command The command to register.
     */
    public void registerCommand(Command command){
        Verify.nonNullArg(command,"Command cannot be null!");
        Verify.nonNullArg(command.getName(), "Command name cannot be null!");
        String name = f(command.getName().toLowerCase());
        if(commands.containsKey(name))throw new IllegalArgumentException("Command '"+name+"' already registered!");
        commands.put(name, command);
        if(command.hasAliases()){
            for(String alias : command.getAliases()){
                if(alias == null || alias.isEmpty())continue;
                commands.put(f(alias), command);
            }
        }
    }

    /**
     * Unregisters a command from this cache.
     * @param command The command to unregister.
     */
    public void unregisterCommand(Command command){
        if(command == null)return;
        if(command.getName() == null)return;
        String name = f(command.getName().toLowerCase());
        if(commands.get(name).equals(command))commands.remove(name);
        if(command.hasAliases()){
            for(String alias : command.getAliases()){
                String correct = f(alias.toLowerCase());
                if(commands.get(correct).equals(command))commands.remove(correct);
            }
        }
    }

    /**
     * Checks if a command is registered.
     * @param command The command to check for.
     * @return {@code true} if the command is registered in this cache, {@code false} otherwise.
     */
    public boolean isRegistered(Command command){
        return command != null && command.getName() != null && commands.containsKey(f(command.getName()));
    }

    /**
     * Checks if a command by name or an alias is registered.
     * @param nameOrAlias The name or alias of a command that could be registered.
     * @return {@code true} if there is a command registered with that name or alias, {@code false} otherwise.
     */
    public boolean isRegistered(String nameOrAlias){
        return nameOrAlias != null && !nameOrAlias.isEmpty() && commands.containsKey(f(nameOrAlias));
    }

    /**
     * Checks whether a command name is already registered.
     * @param name The name to check for.
     * @return {@code true} if the name is already registered, {@code false} otherwise.
     */
    protected boolean isRegisteredName(String name){
        if(name == null || name.isEmpty())return false;
        Command command = getCommand(name);
        if(command == null)return false;
        return command.getName().equals(f(name));
    }

    /**
     * Retrieves a command from the cache.
     * @param nameOrAlias The name or alias of the command to receive.
     * @return The command fitting the name or alias, or {@code null} if there is none.
     */
    public Command getCommand(String nameOrAlias){
        return nameOrAlias != null && !nameOrAlias.isEmpty() ? commands.get(f(nameOrAlias)) : null;
    }

    /**
     * Formats a command name or alias correctly. See {@link Command#formatCorrectly(String)}.
     * @param n The name or alias to format.
     * @return Returns the correct name or alias.
     */
    private String f(String n){
        return Command.formatCorrectly(n);
    }

    /**
     * The result returned by {@link #performInput(CommandSender, String, CommandInterpreter)}.
     */
    public enum PerformResult{
        /** Success state, command was executed. */
        EXECUTION,
        /** Returned when the input was just text and no valid command.
         *  This can only happen when the {@link CommandInterpreter} strictly sets command identifiers. */
        JUST_TEXT,
        /** The command could not have been found. */
        NO_COMMAND,
        /** The command sender does not have the permission to perform this command. */
        NO_PERMISSION;
        public int exitCode(){return ordinal();}
        public boolean isFailed(){return exitCode() > JUST_TEXT.exitCode();}
    }



}
