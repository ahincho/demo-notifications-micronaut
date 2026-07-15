package pe.edu.nova.demo.notifications.micronaut;

import io.micronaut.runtime.Micronaut;

/**
 * Micronaut entry point. The {@code nova-notifications-micronaut-module} is on
 * the classpath, so {@code NotificationFacade} is exposed as a @Bean and
 * ready to inject.
 */
public class DemoApplication {

    public static void main(String[] args) {
        Micronaut.run(DemoApplication.class, args);
    }
}
