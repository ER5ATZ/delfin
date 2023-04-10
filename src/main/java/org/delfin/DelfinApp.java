package org.delfin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Andreas Ersch <andreas.ersch@gmail.com>
 */
@SpringBootApplication
//@PropertySource(value = "${config.location:application.properties}")
public class DelfinApp {

    public static void main(String[] args) {
        SpringApplication.run(DelfinApp.class, args);
    }

}
