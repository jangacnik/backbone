package com.resort.platform.backnode.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableEncryptableProperties
@EncryptablePropertySource("application.properties")
public class AppConfigJasypt {

}
