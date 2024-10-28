package com.github.kleesup.kleegdx.server.util;

import com.github.kleesup.kleegdx.core.util.AbstractLogger;

/**
 * Implementation of {@link AbstractLogger} that logs into the {@link System#out} or {@link System#err} stream,
 * depending on logging type. Useful for headless non-gdx applications such as a server.
 */
public class ConsoleLogger extends AbstractLogger {

    /**
     * Formatting modes for log tag and color.
     */
    enum Mode{
        INFO("", "[I]"),            //no color
        DEBUG("\u001B[34m", "[D]"), //blue color
        ERROR("\u001B[31m", "[E]"); //red color
        private final String tag, color;
        Mode(String color, String tag) {
            this.tag = tag;
            this.color = color;
        }

        /**
         * Transforms a log tag with a message into a fully formatted loggable message.
         * @param logTag The tag of the logger.
         * @param msg The message to log.
         * @return The formatted string.
         */
        public String format(String logTag, String msg){
            return color + logTag + tag + " " + msg + "\u001B[0m";
        }
    }

    /**
     * Converts a {@link com.github.kleesup.kleegdx.core.util.AbstractLogger.LogType} into an internal
     * {@link Mode} to use it for formatting.
     * @param type The type for logging.
     * @return The converted mode.
     */
    Mode convertToMode(LogType type){
        Mode mode;
        switch (type){
            default:
            case ERROR:
                mode = Mode.ERROR;
                break;
            case INFO:
                mode = Mode.INFO;
                break;
            case DEBUG:
                mode = Mode.DEBUG;
                break;
        }
        return mode;
    }

    /* -- Implementation of AbstractLogger -- */

    private final String tag;
    public ConsoleLogger(String tag) {
        super(null);
        this.tag = tag;
    }

    @Override
    protected void successfulLog(LogType type, String msg) {
        if(type == LogType.ERROR)System.err.println(convertToMode(type).format(tag,msg));
        else System.out.println(convertToMode(type).format(tag,msg));
    }

    @Override
    protected void logException(LogType type, Exception exception) {
        exception.printStackTrace(type == LogType.ERROR ? System.err : System.out);
    }


}
