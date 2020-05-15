package com.loven.iToken.web.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Created by loven on 2020/5/12.
 */
@SpringBootApplication(scanBasePackages = {"com.loven.iToken"})
@EnableDiscoveryClient
@EnableFeignClients
@EnableHystrixDashboard
public class WebBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebBackendApplication.class, args);
    }
}
