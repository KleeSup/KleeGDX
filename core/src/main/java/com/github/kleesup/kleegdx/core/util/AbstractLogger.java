package com.github.kleesup.kleegdx.core.util;

import com.badlogic.gdx.ApplicationLogger;
import com.badlogic.gdx.utils.Logger;

/**
 * Abstract implementation of LibGDX {@link Logger} to support logging in non-gdx related contexts but keeping
 * the same interface (i.e. abstract remote/local server implementation).
 */
public abstract class AbstractLogger extends Logger {

    protected String tag;
    public AbstractLogger(String tag, int level) {
        super(null, level);
        this.tag = tag;
    }
    public AbstractLogger(String tag) {
        super(null);
        this.tag = tag;
    }

    /**
     * Called when a logging operation was successful. A logging call is successful when the level required for
     * this operation is the same or lower than set by {@link #setLevel(int)}.
     * @param type The type of logging (error/info/debug).
     * @param msg The message to log.
     */
    protected abstract void successfulLog(LogType type, String msg);
    protected void successfulLog(LogType type, String msg, Exception exception){
        successfulLog(type,msg);
        logException(type,exception);
    }
    /**
     * Called when a logging operation was successful, and it came with an exception that now needs to be logged.
     * @param type The type of logging (error/info/debug).
     * @param exception The exception to log.
     */
    protected abstract void logException(LogType type, Exception exception);

    /* -- Implementation of Logger -- */

    public void debug (String message) {
        if (getLevel() >= DEBUG) successfulLog(LogType.DEBUG, message);
    }

    public void debug (String message, Exception exception) {
        if (getLevel() >= DEBUG) successfulLog(LogType.DEBUG, message, exception);
    }

    public void info (String message) {
        if (getLevel() >= INFO) successfulLog(LogType.INFO, message);
    }

    public void info (String message, Exception exception) {
        if (getLevel() >= INFO) successfulLog(LogType.INFO, message, exception);
    }

    public void error (String message) {
        if (getLevel() >= ERROR) successfulLog(LogType.ERROR, message);
    }

    public void error (String message, Throwable exception) {
        if (getLevel() >= ERROR) successfulLog(LogType.ERROR, message, new Exception(exception));
    }

    protected enum LogType{
        ERROR, INFO, DEBUG;
        public int level(){
            return ordinal() + 1;
        }
    }

}
