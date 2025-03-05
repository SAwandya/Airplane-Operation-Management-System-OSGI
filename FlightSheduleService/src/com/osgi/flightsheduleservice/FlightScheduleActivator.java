package com.osgi.flightsheduleservice;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlightScheduleActivator implements BundleActivator {
//    private static final Logger logger = LoggerFactory.getLogger(FlightScheduleActivator.class);
    private FlightScheduleServiceImpl service;
    private ServiceRegistration<FlightScheduleService> registration;

    @Override
    public void start(BundleContext context) {
        service = new FlightScheduleServiceImpl();
        registration = context.registerService(FlightScheduleService.class, service, null);
//        logger.info("FlightScheduleService registered");
        System.out.println("Flight S");
    }

    @Override
    public void stop(BundleContext context) {
        if (registration != null) {
            registration.unregister();
        }
        if (service != null) {
            service.shutdown();
        }
//        logger.info("FlightScheduleService unregistered and shut down");
    }
}