package dev.nullzwo.user.domain.api;

import dev.nullzwo.user.domain.service.ChangeLogger;
import dev.nullzwo.user.domain.spi.ChangePort;
import dev.nullzwo.user.domain.spi.FakeChangeAdapter;
import dev.nullzwo.user.domain.spi.FakeUserAdapter;
import dev.nullzwo.user.domain.spi.UserPort;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static java.time.ZoneOffset.UTC;

public class TestContext {
    private final ChangePort changePort;
    private final UserPort userPort;
    private final ChangeLogger changeLogger;
    public final MockClock clock;
    public final UserService userService;
    public final ChangeService changeService;

    public TestContext() {
        clock = new MockClock();
        changePort = new FakeChangeAdapter();
        changeLogger = new ChangeLogger(clock, changePort);
        userPort = new FakeUserAdapter();
        userService = new UserService(userPort, changeLogger);
        changeService = new ChangeService(changePort, userPort);
    }

    public static class MockClock extends Clock {

        private Clock clock;

        public MockClock() {
            this.clock = Clock.fixed(Instant.now(), UTC);
        }

        @Override
        public ZoneId getZone() {
            return clock.getZone();
        }

        @Override
        public Clock withZone(ZoneId zone) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Instant instant() {
            return clock.instant();
        }

        public void tick() {
            clock = Clock.fixed(instant().plusSeconds(1), UTC);
        }
    }
}
