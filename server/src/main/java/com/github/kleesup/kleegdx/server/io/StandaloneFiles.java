package com.github.kleesup.kleegdx.server.io;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;

import java.io.File;

/**
 * Copied version of LwjglFiles for files on desktop for servers (Linux/Mac/Windows).
 */
public class StandaloneFiles implements Files {

    static final String externalPath;
    static final String localPath;
    static {
        externalPath = System.getProperty("user.home") + File.separator;
        localPath = (new File("")).getAbsolutePath() + File.separator;
    }

    public FileHandle getFileHandle(String fileName, Files.FileType type) {
        return new StandaloneFileHandle(fileName, type);
    }

    public FileHandle classpath(String path) {
        return new StandaloneFileHandle(path, FileType.Classpath);
    }

    public FileHandle internal(String path) {
        return new StandaloneFileHandle(path, FileType.Internal);
    }

    public FileHandle external(String path) {
        return new StandaloneFileHandle(path, FileType.External);
    }

    public FileHandle absolute(String path) {
        return new StandaloneFileHandle(path, FileType.Absolute);
    }

    public FileHandle local(String path) {
        return new StandaloneFileHandle(path, FileType.Local);
    }

    public String getExternalStoragePath() {
        return externalPath;
    }

    public boolean isExternalStorageAvailable() {
        return true;
    }

    public String getLocalStoragePath() {
        return localPath;
    }

    public boolean isLocalStorageAvailable() {
        return true;
    }
}
