package com.resort.platform.backnode;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
@EncryptablePropertySource("application.properties")
public class BacknodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BacknodeApplication.class, args);
    }

}
