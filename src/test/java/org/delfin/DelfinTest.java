package org.delfin;

import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@SpringBootTest(
        properties = {"spring.config.location=classpath:application-test.properties"},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public @interface DelfinTest {
}
