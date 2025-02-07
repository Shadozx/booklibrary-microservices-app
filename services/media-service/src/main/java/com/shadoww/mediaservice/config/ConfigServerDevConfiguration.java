package com.shadoww.mediaservice.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("config-dev")
@EnableDiscoveryClient
public class ConfigServerDevConfiguration {
}
