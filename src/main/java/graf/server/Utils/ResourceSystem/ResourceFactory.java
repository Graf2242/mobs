package graf.server.Utils.ResourceSystem;

import graf.server.Base.Resource;
import graf.server.Base.VFS;
import graf.server.Utils.Serialization.Serializator;
import graf.server.VFS.VFSImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ResourceFactory {
    private static ResourceFactory resourceFactory;
    Map<String, Resource> resources = new HashMap<>();
    String homePath;
    VFS vfs;

    private ResourceFactory() {
        homePath = "src/main/resources/resources";
        vfs = new VFSImpl(homePath);
        loadAll();
    }

    public static ResourceFactory instance() {
        if (resourceFactory == null) {
            resourceFactory = new ResourceFactory();
        }
        return resourceFactory;
    }

    public String getHomePath() {
        return homePath;
    }

    public void setHomePath(String homePath) {
        this.homePath = homePath;
    }

    public Resource getResource(String path) {
        return Serializator.deserializeXmlFile(path);
    }

    public Resource getReadResource(String path) {
        return Serializator.deserializeXmlFile(path);
    }

    public Map<Integer, String[]> readAllTextFiles(String homePath) {
        Map<Integer, String[]> result = new HashMap<>();
        Iterator iterator = vfs.getIterator(homePath);
        List<String> lines = null;
        Integer num = null;
        while (iterator.hasNext()) {
            File file = new File((String) iterator.next());
            if (!file.isDirectory()) {
                try {
                    FileReader fr = new FileReader(file);
                    BufferedReader br = new BufferedReader(fr);
                    lines = new ArrayList<>();
                    String line;
                    while ((line = br.readLine()) != null) {
                        lines.add(line);
                    }
                    num = Integer.parseInt(file.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (lines != null) {
                    String[] s = new String[lines.size()];
                    for (int i = 0; i < lines.size(); i++) {
                        s[i] = lines.get(i);
                    }
                    result.put(num, s);
                }
            }
        }
        return result;
    }

    public void loadAll() {
        Iterator<String> iterator = vfs.getIterator(homePath);
        while (iterator.hasNext()) {
            String next = iterator.next();
            if (!vfs.isDirectory(next)) {
                resources.put(next, Serializator.deserializeXmlFile(next));
            }
        }
    }

    public List<Object> getAllResources(String clazz) {
        List<Object> result = new ArrayList<>();

        resources.forEach((s, resource) -> {
            if (resource.getClass().toString().equals(clazz)) {
                result.add(resource);
            }
        });
        return result;
    }
}
