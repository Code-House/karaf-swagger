package org.code_house.swagger.core.internal;

import io.swagger.jaxrs.Reader;
import io.swagger.models.*;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Bridge between tracker and api resource listing.
 */
public class SwaggerHolder {

    private final Info info;
    private final String basePath;

    // temporary instance
    private Swagger swagger = new Swagger();

    public SwaggerHolder(String version, String title, String description, String license, String licenseUrl, String contact, String basePath) {
        this.basePath = basePath;
        this.info = new Info()
            .version(version)
            .title(title)
            .description(description)
            .license(new License().name(license).url(licenseUrl))
            .contact(new Contact().name(contact));

        this.swagger.info(this.info).basePath(basePath);
    }

    public void setClasses(Map<String, Set<Class<?>>> classMap) {
        Swagger swagger = new Swagger().info(info).basePath(basePath);

        Reader reader = new Reader(swagger);
        Set<Class<?>> classes = new LinkedHashSet<>();
        for (Map.Entry<String, Set<Class<?>>> resources : classMap.entrySet()) {
            Map<String, Path> oldPaths = new LinkedHashMap<>();
            if (this.swagger.getPaths() != null) {
                oldPaths.putAll(this.swagger.getPaths());
            }

            reader.read(resources.getValue());

            Map<String, Path> allPaths = swagger.getPaths();
            Map<String, Path> mergedPaths = new LinkedHashMap<>();

            if (allPaths != null) {
                for (Map.Entry<String, Path> entry : allPaths.entrySet()) {
                    if (oldPaths.containsKey(entry.getKey())) {
                        mergedPaths.put(entry.getKey(), entry.getValue());
                    } else {
                        mergedPaths.put(resources.getKey() + entry.getKey(), entry.getValue());
                    }
                }
                swagger.paths(mergedPaths);
            }
        }
        this.swagger = swagger;
    }

    public Swagger getSwagger() {
        return swagger;
    }

}
