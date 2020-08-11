package com.logreposit.pwsinterceptorservice.configurations

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "app")
class ApplicationConfiguration {
    var weatherUndergroundForwardingEnabled: Boolean? = false
    var logrepositApiBaseUrl: String? = "https://api.logreposit.com"
    var logrepositDeviceToken: String? = "INVALID"
}
