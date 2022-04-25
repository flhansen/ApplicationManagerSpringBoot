package com.florianhansen.applicationmanager.model;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.florianhansen.applicationmanager.model")
@EntityScan("com.florianhansen.applicationmanager.model")
@ComponentScan("com.florianhansen.applicationmanager.model")
public class ModuleConfiguration {

}
