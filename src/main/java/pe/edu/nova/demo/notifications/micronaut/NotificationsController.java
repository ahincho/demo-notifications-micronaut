package pe.edu.nova.demo.notifications.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import java.util.HashMap;
import java.util.Map;
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
 *
 * <p>The controller returns a {@code Map<String, Object>} rather than the
 * library's {@code NotificationResult} directly because Micronaut Serde
 * (the default JSON serializer on the demo side) requires classes to be
 * annotated with {@code @Serdeable.Serializable} for JSON conversion. The
 * library is framework-agnostic and cannot depend on Micronaut Serde, so
 * the demo wraps the library's result in a simple map. The shape of the
 * response is the same as what {@code NotificationResult.toJson()} would
 * produce (it is a simple record with a handful of string / boolean /
 * optional fields).
 */
@Controller("/api/notifications")
public class NotificationsController {

    private final NotificationFacade facade;

    public NotificationsController(NotificationFacade facade) {
        this.facade = facade;
    }

    @Get("/email/welcome")
    public Map<String, Object> sendWelcomeEmail() {
        EmailNotification email = EmailNotification.builder()
                .from(new EmailAddress("no-reply@example.com"))
                .to(new EmailAddress("customer@example.com"))
                .subject(new Subject("Welcome"))
                .body(new MessageBody("Thanks for signing up to Nova."))
                .build();
        NotificationResult result = facade.send(email);
        Map<String, Object> body = new HashMap<>();
        body.put("sent", result.isSent());
        body.put("providerMessageId", result.providerMessageId().orElse(null));
        body.put("channel", result.channel().toString().toLowerCase());
        body.put("errorCode", result.errorCode().map(Enum::name).orElse(null));
        body.put("errorMessage", result.errorMessage().orElse(null));
        return body;
    }
}
