package org.code_house.swagger.core.internal;

import org.apache.cxf.Bus;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import java.util.List;

public class JaxrsServiceTracker extends ServiceTracker<Bus, List<String>> {

    public JaxrsServiceTracker(BundleContext context, SwaggerHolder holder) {
        super(context, Bus.class, new JaxrsServiceTrackerCustomizer(context, holder));
    }

}
