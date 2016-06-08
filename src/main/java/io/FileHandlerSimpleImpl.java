package io;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by Vietlc on 6/3/16.
 */
public class FileHandlerSimpleImpl implements FileHandle {
    private String filename;
    private String charset;

    public FileHandlerSimpleImpl(String fileName, String charSet) {
        this.filename = fileName;
        this.charset = charSet;
    }

    /*
     *  This implementation is just good for the small size files.
     */
    @Override
    public List<String> ReadAllLines() {
        Path path = Paths.get(this.filename);
        Charset charset = Charset.forName(this.charset);

        try {
            return Files.readAllLines(path, charset);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void WriteAllLines(List<String> lines) {
        Path path = Paths.get(this.filename + ".counted.1");
        Charset charset = Charset.forName(this.charset);

        try {
            Files.write(path, lines, charset);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> ReadNextNLines(Integer n) {
        return null;
    }

    @Override
    public void writeSingleLine(String line) { }
}
