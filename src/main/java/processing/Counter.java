package processing;

import java.util.List;
import java.util.Map;

/**
 * Created by Vietlc on 6/3/16.
 */
public interface Counter {
    public void count(List<String> lines);

    public void write();

    public Map<String, Long> partialCount(List<String> lines);

    public void update(Map<String, Long> partial);
}
