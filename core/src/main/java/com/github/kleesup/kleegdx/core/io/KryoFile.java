package com.github.kleesup.kleegdx.core.io;

import com.badlogic.gdx.files.FileHandle;
import com.esotericsoftware.kryo.io.Output;
import com.github.kleesup.kleegdx.core.util.Saveable;
import com.github.kleesup.kleegdx.core.util.Verify;

import java.io.*;

/**
 * A simple file manager class that uses a Kryo {@link Output} to write to and read from the file.
 */
public class KryoFile extends Output implements Saveable {

    private final File _file;
    public KryoFile(File file){
        Verify.nonNullArg(file, "File cannot be null!");
        Verify.checkArg(file.isDirectory(), "File cannot be a directory!");
        this._file = file;
        if(!_file.exists())return;
        try(FileInputStream stream = new FileInputStream(_file)) {
            byte[] load = new byte[(int) _file.length()];
            stream.read(load);
            setBuffer(load);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public KryoFile(FileHandle fileHandle){
        this(fileHandle.file());
    }

    @Override
    public void save() {
        if(!_file.exists()){
            _file.getParentFile().mkdirs();
            try {
                _file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try(FileOutputStream stream = new FileOutputStream(_file)) {
            stream.write(toBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
