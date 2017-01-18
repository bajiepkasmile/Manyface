package com.nodomain.manyface.utils;


import com.nodomain.manyface.domain.exeptions.FileReadingException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;


public class FileReader {

    @Inject
    public FileReader() {
    }

    public byte[] readFileToByteArray(String filePath) throws FileReadingException {
        byte[] buffer;
        InputStream in = null;

        try {
            File file = new File(filePath);
            in = new FileInputStream(file);
            buffer = new byte[in.available()];
            while (in.read(buffer) != -1);
        } catch (IOException e) {
            throw new FileReadingException();
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return buffer;
    }
}
