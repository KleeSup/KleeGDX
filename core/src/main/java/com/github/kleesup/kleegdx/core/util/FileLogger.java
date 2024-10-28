package com.github.kleesup.kleegdx.core.util;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;

import java.io.*;

/**
 * Implementation of {@link AbstractLogger} that writes the logged content into a file.
 */
public class FileLogger extends AbstractLogger implements Disposable, Saveable {

    private final FileHandle handle;
    public FileLogger(FileHandle handle, String tag, int level) throws IOException {
        super(tag, level);
        this.handle = handle;
        init();
    }

    public FileLogger(FileHandle handle, String tag) throws IOException {
        super(tag);
        this.handle = handle;
        init();
    }

    private PrintWriter writer;
    private void init() throws IOException {
        Verify.checkArg(handle.isDirectory(), "Logfile cannot be a directory!");
        if(!handle.exists()){
            handle.parent().mkdirs();
            handle.file().createNewFile();
            writer = new PrintWriter(handle.writer(true));
        }
    }

    @Override
    protected void successfulLog(LogType type, String msg) {
        writer.println(msg);
    }

    @Override
    protected void logException(LogType type, Exception exception) {
        exception.printStackTrace(writer);
    }

    @Override
    public void dispose() {
        save();
        writer.close();
    }

    @Override
    public void save() {
        writer.flush();
    }
}
