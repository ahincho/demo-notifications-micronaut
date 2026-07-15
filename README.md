# demo-notifications-micronaut

Example Micronaut 5.0.4 application consuming
[`nova-java-notifications-micronaut-module`](../../java/nova-java-notifications-micronaut-module).
A single REST endpoint that triggers a simulated email send through
the library's `NotificationFacade`.

## What it demonstrates

- DI injection of the `NotificationFacade` bean (produced by the
  module's `NotificationsFactory`).
- A real HTTP request end-to-end through the Micronaut HTTP stack
  (Netty server, micronaut-serde-jackson for JSON).
- The Micronaut module's no-op facade behavior under
  `nova.notifications.enabled=false`.

## Prerequisites

- JDK 25
- The pure library and the module must be installed in
  `~/.m2/repository` (the demo consumes them via `mavenLocal`):

  ```bash
  cd ../java/nova-java-notifications && ./mvnw install -DskipTests
  cd ../java/nova-java-notifications-micronaut-module && ./gradlew publishToMavenLocal
  ```

## Run

```bash
./gradlew run
```

The app starts on `http://localhost:8080` (configured in
`application.yml`). The default config sets the email channel to
`sendgrid` with `test-api-key-demo` (suitable for local smoke tests).

For a production build (shadow JAR with all dependencies):

```bash
./gradlew shadowJar
java -jar build/libs/demo-notifications-micronaut-1.0.0-SNAPSHOT-all.jar
```

## Endpoints

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/notifications/email/welcome` | Triggers a simulated welcome email send and returns the result as JSON. |

Example:

```bash
curl http://localhost:8080/api/notifications/email/welcome
# {"sent":true,"providerMessageId":"<uuid>","channel":"email",...}
```

## Configuration

`src/main/resources/application.yml`:

```yaml
nova.notifications:
  enabled: true
  email:
    provider: sendgrid
    api-key: test-api-key-demo
    default-sender: no-reply@example.com
  resilience:
    max-attempts: 1
micronaut:
  server:
    port: 8080
```

Override any property at runtime via env vars (Micronaut picks them
up automatically) or via external config sources.

## Test

```bash
./gradlew test
```

The demo ships with one integration test class (`DemoApplicationTest`,
2 tests) that boots the demo via `@MicronautTest` and exercises the
real HTTP endpoint through Micronaut's `HttpClient`. The
`NotificationConfiguration` bean is provided by a separate
`DemoTestConfig` `@Factory` that uses `@Replaces` to take precedence
over the starter's bean (see the test class's Javadoc for the
rationale; the binding path is independently verified by the
module's own integration tests).

## Docker

The demo ships with a production-ready multi-stage `Dockerfile`
(non-root UID 1001, tini + netcat for healthchecks, OCI labels, JVM
ergonomics). Build with:

```bash
docker buildx build --build-context hostm2=$env:USERPROFILE\.m2\repository -t demo-notifications-micronaut:1.0.0-SNAPSHOT .
docker run --rm -p 8080:8080 demo-notifications-micronaut:1.0.0-SNAPSHOT
```

## Versioning

- `1.0.0-SNAPSHOT` — aligned with module and library `1.0.0`.
- Java 25 toolchain.
- Micronaut 5.0.4.

## Related

- [`nova-java-notifications`](../../java/nova-java-notifications) — pure library.
- [`nova-java-notifications-micronaut-module`](../../java/nova-java-notifications-micronaut-module) — Micronaut colloquial module (this demo's dependency).
- [`examples/demo-notifications-spring-boot`](../demo-notifications-spring-boot) — same demo on Spring Boot.
- [`examples/demo-notifications-quarkus`](../demo-notifications-quarkus) — same demo on Quarkus.

---

## AI Assistance Attribution

This work was created through human-AI collaboration. The human author
(Angel Eduardo Hincho Jove, `ahincho@unsa.edu.pe`, UNSA) retains full
responsibility for the final artifact.

**AI tools used**: GitHub Copilot (Claude Opus 4.8, Sonnet 5), MiniMax
(MiniMax-M3 via paid Token Plan), OpenCode (the interactive CLI
harness used to host the session), NotebookLM, Perplexity.
Methodology: OpenSpec spec-driven development.

**Important legal note**: this artifact is **not an "AI system"** under
Article 3(1) of Regulation (EU) 2024/1689 (the EU AI Act). Article 50
transparency obligations therefore do not directly apply. This
disclosure is made voluntarily, aligned with UNESCO Principle 6
(transparency and explainability) and the R-AI requirement of the
originating challenge.

The canonical, full AI-ATTRIBUTION.md (covering the entire Nova
Platform workspace) lives at the workspace root:
[`../../AI-ATTRIBUTION.md`](../../AI-ATTRIBUTION.md).
