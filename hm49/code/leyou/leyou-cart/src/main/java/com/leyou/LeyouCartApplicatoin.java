package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class LeyouCartApplicatoin {

    public static void main(String[] args) {
        SpringApplication.run(LeyouCartApplicatoin.class, args);
    }
}
