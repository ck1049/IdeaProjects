package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class LeyouGoodsApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeyouGoodsApplication.class);
    }
}