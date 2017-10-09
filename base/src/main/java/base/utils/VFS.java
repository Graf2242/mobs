package base.utils;

import java.io.IOException;
import java.util.Iterator;

public interface VFS {
    boolean isExist(String path);

    boolean isDirectory(String path);

    String getAbsolutePath(String file);

    byte[] getBytes(String file) throws IOException;

    void writeBinResource(Object o, Class<? extends Resource> clazz, String path);

    <T> T readBinResource(String path);

    String getUFT8Text(String file) throws IOException;

    Iterator<String> getIterator(String startDir);
}