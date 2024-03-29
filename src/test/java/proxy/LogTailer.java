package proxy;

import org.apache.commons.io.input.TailerListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTailer extends TailerListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogTailer.class);

    @Override
    public void handle(String line) {
        if (line.contains("WARN")) {
            LOGGER.warn(line);
        }
    }
}
