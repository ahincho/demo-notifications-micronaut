package pe.edu.nova.demo.notifications.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import pe.edu.nova.java.libs.notifications.application.facade.NotificationFacade;
import pe.edu.nova.java.libs.notifications.domain.model.EmailNotification;
import pe.edu.nova.java.libs.notifications.domain.result.NotificationResult;
import pe.edu.nova.java.libs.notifications.domain.vo.EmailAddress;
import pe.edu.nova.java.libs.notifications.domain.vo.MessageBody;
import pe.edu.nova.java.libs.notifications.domain.vo.Subject;

/**
 * HTTP controller that triggers a simulated email send through the Nova
 * Notifications library. The {@code NotificationFacade} is auto-wired by
 * the Micronaut module's @Factory.
 */
@Controller("/api/notifications")
public class NotificationsController {

    private final NotificationFacade facade;

    public NotificationsController(NotificationFacade facade) {
        this.facade = facade;
    }

    @Get("/email/welcome")
    public NotificationResult sendWelcomeEmail() {
        EmailNotification email = EmailNotification.builder()
                .from(new EmailAddress("no-reply@example.com"))
                .to(new EmailAddress("customer@example.com"))
                .subject(new Subject("Welcome"))
                .body(new MessageBody("Thanks for signing up to Nova."))
                .build();
        return facade.send(email);
    }
}
