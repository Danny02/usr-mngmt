package dev.nullzwo.user.app.adapter;

import dev.nullzwo.user.domain.spi.UserPort;
import dev.nullzwo.user.domain.spi.UserPortTest;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@SpringBootTest(webEnvironment = NONE)
@Transactional
public class SpringUserAdapterTest implements UserPortTest {

    @Autowired
    SpringUserAdapter adapter;

    @Autowired
    JpaUserRepository repository;

    @AfterEach
    void cleanDb() {
        repository.deleteAll();
    }

    @Override
    public UserPort port() {
        return adapter;
    }
}
