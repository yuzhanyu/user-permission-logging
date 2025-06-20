package whh.userservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Base64;

@SpringBootTest
class UserServiceApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(Base64.getEncoder().encodeToString("your-secret-key-here".getBytes()));

    }

}
