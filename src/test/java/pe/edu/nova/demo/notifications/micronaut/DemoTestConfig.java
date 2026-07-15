package pe.edu.nova.demo.notifications.micronaut;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Replaces;
import jakarta.inject.Singleton;
import pe.edu.nova.java.libs.notifications.application.facade.NotificationFacade;
import pe.edu.nova.java.libs.notifications.domain.vo.EmailAddress;
import pe.edu.nova.java.libs.notifications.infrastructure.configuration.EmailConfiguration;
import pe.edu.nova.java.libs.notifications.infrastructure.configuration.EmailProvider;
import pe.edu.nova.java.libs.notifications.infrastructure.configuration.NotificationConfiguration;
import pe.edu.nova.java.libs.notifications.infrastructure.configuration.ResilienceConfiguration;

/**
 * Test-only {@link Factory} that produces a {@link NotificationConfiguration}
 * bean for the demo's integration test, replacing the one that would
 * otherwise be built by the starter's {@code NotificationsFactory}.
 *
 * <p>See {@link DemoApplicationTest} for the rationale on bypassing the
 * starter's auto-configuration here. The {@code @Replaces} annotation
 * ensures the test bean wins over the starter's bean when both are
 * discovered; Micronaut raises a startup error otherwise (multiple beans
 * of the same type without an unambiguous primary).
 */
@Factory
class DemoTestConfig {

    @Singleton
    @Replaces(NotificationConfiguration.class)
    NotificationConfiguration notificationConfiguration() {
        return NotificationConfiguration.builder()
                .email(EmailConfiguration.builder()
                        .provider(EmailProvider.SENDGRID)
                        .apiKey("test-api-key-demo")
                        .defaultSender(new EmailAddress("no-reply@example.com"))
                        .build())
                .resilience(ResilienceConfiguration.disabled())
                .build();
    }
}