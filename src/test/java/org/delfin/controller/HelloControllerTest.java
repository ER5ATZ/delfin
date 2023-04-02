package org.delfin.controller;

import org.delfin.DelfinTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@DelfinTest
public class HelloControllerTest {

    @Value("${greeting.message}")
    private String greetingMessage;

    @Test
    public void testHello() {
        HelloController controller = new HelloController();
        String result = controller.hello();
        assertEquals(greetingMessage, result);
    }

}
