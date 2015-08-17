package org.code_house.swagger.core.internal;

import io.swagger.jaxrs.Reader;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.models.Swagger;
import org.apache.cxf.Bus;
import org.apache.cxf.configuration.ConfiguredBeanLocator;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import java.util.*;

public class JaxrsServiceTrackerCustomizer implements ServiceTrackerCustomizer<Bus, List<String>> {

    private final BundleContext context;
    private final SwaggerHolder holder;
    private final Map<String, Set<Class<?>>> classMap = new LinkedHashMap<>();

    public JaxrsServiceTrackerCustomizer(BundleContext context, SwaggerHolder holder) {
        this.context = context;
        this.holder = holder;
    }

    @Override
    public List<String> addingService(ServiceReference<Bus> reference) {
        List<String> addresses = new ArrayList<>();
        Bus bus = context.getService(reference);

        ConfiguredBeanLocator locator = bus.getExtension(ConfiguredBeanLocator.class);
        Collection<? extends JAXRSServerFactoryBean> factoryBeans = locator.getBeansOfType(JAXRSServerFactoryBean.class);

        for (JAXRSServerFactoryBean factoryBean : factoryBeans) {
            Set<Class<?>> classes = new HashSet<>();
            for (Class<?> clazz : factoryBean.getResourceClasses()) {
                classes.add(clazz);
                classes.addAll(Arrays.asList(clazz.getInterfaces()));
            }

            String uri = factoryBean.getAddress();
            System.out.println(uri);
            addresses.add(uri);

            classMap.put(uri, classes);
        }

        holder.setClasses(classMap);
        context.ungetService(reference);

        return addresses.isEmpty() ? null : addresses;
    }

    @Override
    public void modifiedService(ServiceReference<Bus> reference,  List<String> service) {

    }

    @Override
    public void removedService(ServiceReference<Bus> reference,  List<String> service) {
        // reinitialize ?!
        for (String address : service) {
            classMap.remove(address);
        }
        context.ungetService(reference);

        holder.setClasses(classMap);
    }

}
