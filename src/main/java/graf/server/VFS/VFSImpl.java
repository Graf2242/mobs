package graf.server.VFS;

import graf.server.Base.VFS;

import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class VFSImpl implements VFS {
    private final String root;

    public VFSImpl(String root) {
        this.root = root;
    }

    @Override
    public boolean isExist(String path) {
        return new File(root + path).exists();
    }

    @Override
    public boolean isDirectory(String path) {
        return new File(root + path).isDirectory();
    }

    @Override
    public String getAbsolutePath(String file) {
        return new File(root + file).getAbsolutePath();
    }

    @Override
    public byte[] getBytes(String file) throws IOException {
        final String[] result = {""};
        File file1 = new File(root + file);
        FileReader fileReader = new FileReader(file1);
        BufferedReader br = new BufferedReader(fileReader);
        br.lines().forEach(s -> result[0] += s);
        fileReader.close();
        return result[0].getBytes();
    }

    @Override
    public String getUFT8Text(String file) throws IOException {
        FileInputStream fis = new FileInputStream(root + file);
        DataInputStream dis = new DataInputStream(fis);
        InputStreamReader isr = new InputStreamReader(dis, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        StringBuilder resultBuilder = new StringBuilder();
        String s;
        while ((s = br.readLine()) != null) {
            resultBuilder.append(s);
        }
        fis.close();
        return resultBuilder.toString();
    }

    @Override
    public Iterator<String> getIterator(String startDir) {
        return new iter(startDir);
    }

    private class iter implements Iterator<String> {

        private Queue<File> files = new LinkedList<>();

        public iter(String path) {
            files.add(new File(root + path));
        }

        @Override
        public boolean hasNext() {
            return !files.isEmpty();
        }

        @Override
        public String next() {
            File file = files.peek();
            if (file.isDirectory()) {
                if (file.listFiles() != null) {
                    //noinspection ConstantConditions
                    files.addAll(Arrays.asList(file.listFiles()));
                }
            }
            return files.poll().getAbsolutePath();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }
}
