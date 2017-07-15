package graf.server.Utils.ResourceSystem;

import graf.server.Base.Resource;
import graf.server.Base.VFS;
import graf.server.Utils.Serialization.Serializator;
import graf.server.VFS.VFSImpl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ResourceFactory {
    private static ResourceFactory resourceFactory;
    Map<String, Resource> resources = new HashMap<>();
    String homePath;
    VFS vfs;

    private ResourceFactory() {
        homePath = System.getProperty("user.dir") + "\\src\\main\\resources";
        vfs = new VFSImpl(homePath);
        loadAll();
        System.out.println("");
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
        return resources.get(path);
    }

    public Resource getReadResource(String path) {
        return Serializator.deserializeXmlFile(path);
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
}
