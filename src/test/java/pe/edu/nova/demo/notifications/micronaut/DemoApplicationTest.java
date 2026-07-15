package pe.edu.nova.demo.notifications.micronaut;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.util.Map;
import org.junit.jupiter.api.Test;
import pe.edu.nova.java.libs.notifications.application.facade.NotificationFacade;

/**
 * Integration test for the Micronaut demo. Boots the full demo application
 * via {@code @MicronautTest}, then issues a real HTTP request through an
 * {@link HttpClient} against the {@code /api/notifications/email/welcome}
 * endpoint exposed by {@link NotificationsController}.
 *
 * <p>The {@link DemoTestConfig} (a separate test-only Factory in the same
 * package) provides a {@code NotificationConfiguration} bean for the test
 * context, replacing the one that would otherwise be built by the
 * starter's {@code NotificationsFactory}. The starter's binding path is
 * covered by {@code MicronautExtensionIntegrationTest} in the starter
 * module. The integration coverage here focuses on what the DEMO does:
 * wire the {@code NotificationFacade} into the controller and serve a
 * real HTTP request end-to-end.
 */
@MicronautTest
class DemoApplicationTest {

    @Inject
    NotificationFacade facade;

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void moduleAutoWiresNotificationFacadeIntoTheDemo() {
        assertNotNull(facade);
    }

    @Test
    void getWelcomeEmailEndpointReturns200AndSentResult() {
        HttpResponse<Map> response = client.toBlocking()
                .exchange(HttpRequest.GET("/api/notifications/email/welcome"),
                        Map.class);

        assertEquals(HttpStatus.OK, response.getStatus());
        Map body = response.body();
        assertNotNull(body);
        assertEquals(Boolean.TRUE, body.get("sent"));
        assertNotNull(body.get("providerMessageId"));
    }
}