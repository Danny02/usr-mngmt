package dev.nullzwo.user.app.adapter;

import dev.nullzwo.user.domain.spi.ChangePort;
import dev.nullzwo.user.domain.spi.ChangePortTest;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpringChangeAdapterTest implements ChangePortTest {

    @Autowired
    ChangePort port;

    @Autowired
    JpaChangeRepository repository;

    @AfterEach
    void cleanDb() {
        repository.deleteAll();
    }

    @Override
    public ChangePort port() {
        return port;
    }
}
