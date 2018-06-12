package utils.ResourceSystem;

import base.utils.Resource;
import base.utils.VFS;
import utils.Serialization.SerializerHelper;
import utils.VirtualFileSystem.VFSImpl;
import utils.logger.LoggerImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ResourceFactory {
    private static ResourceFactory resourceFactory;
    private Map<String, Resource> resources = new HashMap<>();
    private String homePath;
    private VFS vfs;

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
            Resource resource = SerializerHelper.deserializeXmlFile(path);
            this.resources.put(path, resource);
            return resource;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LoggerImpl.getLogger().error(e);
        }
        return null;
    }

    public Resource getReadResource(String path) {
        try {
            return SerializerHelper.deserializeXmlFile(path);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LoggerImpl.getLogger().error(e);
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
                    LoggerImpl.getLogger().error(e);
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
                    resources.put(next, SerializerHelper.deserializeXmlFile(next));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    LoggerImpl.getLogger().error(e);
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
