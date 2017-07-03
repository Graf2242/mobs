package graf.server.Utils.ResourceSystem;

import graf.server.Base.Resource;

import java.util.HashSet;
import java.util.Set;

public class ResourceFactory {
    private static ResourceFactory resourceFactory;
    Set<Resource> resources = new HashSet<>();
    String homePath;

    private ResourceFactory() {
    }

    public static ResourceFactory instance() {
        if (resourceFactory == null) {
            resourceFactory = new ResourceFactory();
        }
        return resourceFactory;
    }

    public Resource getResource(String path) {

        return null;
    }

    public void loadAll() {

    }
}
