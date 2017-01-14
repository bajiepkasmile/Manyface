package com.nodomain.manyface.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class FileReader {

    public byte[] readFileToByteArray(String filePath) throws IOException {
        byte[] buffer;
        InputStream in = null;

        try {
            File file = new File(filePath);
            in = new FileInputStream(file);
            buffer = new byte[in.available()];
            while (in.read(buffer) != -1);
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
