package ResourceSystem;

import Serialization.Serializator;
import VirtualFileSystem.VFSImpl;
import utils.Resource;
import utils.VFS;

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
        this.homePath = "data";
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
        try {
            return Serializator.deserializeXmlFile(path);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Resource getReadResource(String path) {
        try {
            return Serializator.deserializeXmlFile(path);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
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
                try {
                    resources.put(next, Serializator.deserializeXmlFile(next));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
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
