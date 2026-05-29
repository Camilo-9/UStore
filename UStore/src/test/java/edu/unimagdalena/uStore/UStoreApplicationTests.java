
package edu.unimagdalena.uStore;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.junit.jupiter.api.Test;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class UStoreApplicationTests{
    @Test
    void contextLoads(){}
}
