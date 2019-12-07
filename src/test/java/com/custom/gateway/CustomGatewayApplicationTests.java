package com.custom.gateway;

import com.netflix.discovery.EurekaClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CustomGatewayApplicationTests {
    private EurekaClient eurekaClient;

    @Test
    public void contextLoads() {
        System.out.println(eurekaClient.getApplications("DEMO-WEB"));
    }

}
