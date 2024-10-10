package com.github.kleesup.kleegdx.server.io;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.File;

/**
 * Copied version of LwjglFileHandle for file handling on desktop for servers (Linux/Mac/Windows).
 */
public final class StandaloneFileHandle extends FileHandle {

    public StandaloneFileHandle(String fileName, Files.FileType type) {
        super(fileName, type);
    }

    public StandaloneFileHandle(File file, Files.FileType type) {
        super(file, type);
    }

    public FileHandle child(String name) {
        return this.file.getPath().length() == 0 ?
                new StandaloneFileHandle(new File(name), this.type) :
                new StandaloneFileHandle(new File(this.file, name), this.type);
    }

    public FileHandle sibling(String name) {
        if (this.file.getPath().length() == 0) {
            throw new GdxRuntimeException("Cannot get the sibling of the root.");
        } else {
            return new StandaloneFileHandle(new File(this.file.getParent(), name), this.type);
        }
    }

    public FileHandle parent() {
        File parent = this.file.getParentFile();
        if (parent == null) {
            if (this.type == Files.FileType.Absolute) {
                parent = new File("/");
            } else {
                parent = new File("");
            }
        }

        return new StandaloneFileHandle(parent, this.type);
    }

    public File file() {
        if (this.type == Files.FileType.External) {
            return new File(StandaloneFiles.externalPath, this.file.getPath());
        } else {
            return this.type == Files.FileType.Local ? new File(StandaloneFiles.localPath, this.file.getPath()) : this.file;
        }
    }

}
