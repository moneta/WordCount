package io;

import java.util.List;

/**
 * Created by Vietlc on 6/3/16.
 */
public interface FileHandle {
    public List<String> ReadAllLines();

    public void WriteAllLines(List<String> lines);

    public  List<String> ReadNextNLines(Integer n);

    public void writeSingleLine(String line);
}
