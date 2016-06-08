package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vietlc on 6/4/16.
 */
public class FileHandlerConcurencyImpl implements FileHandle {
    private String filename;
    private String charset;
    private BufferedReader bufferedReader = null;
    private BufferedWriter bufferedWriter = null;

    public FileHandlerConcurencyImpl(String fileName, String outFileName, String charSet) throws IOException {
        this.filename = fileName;
        this.charset = charSet;

        Path path = Paths.get(this.filename);
        Charset charset = Charset.forName(this.charset);

        this.bufferedReader = Files.newBufferedReader(path, charset);
        this.bufferedWriter = Files.newBufferedWriter(Paths.get(outFileName), charset);
    }

    @Override
    public List<String> ReadAllLines() {
        if(bufferedReader == null) return null;

        List<String> lines = new ArrayList<>();

        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }

            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();

        }

        return lines;
    }

    @Override
    public void WriteAllLines(List<String> lines) {
        if(bufferedWriter == null) return;
        try {
            for(String line : lines) {
                bufferedWriter.write(line + "\r\n");
            }

            bufferedWriter.flush();

            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized List<String> ReadNextNLines(Integer N) {
        if(bufferedReader == null) return null;

        List<String> lines = null;
        int lineCount = 0;

        try {
            String line = null;
            while ( lineCount < N && (line = bufferedReader.readLine()) != null )  {
                if(lines == null) lines = new ArrayList<>();

                lines.add(line);
                lineCount++;
            }

            //if(line == null) bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    @Override
    public void writeSingleLine(String line) {

    }

}
