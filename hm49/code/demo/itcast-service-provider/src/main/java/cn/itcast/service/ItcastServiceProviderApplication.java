package cn.itcast.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("cn.itcast.service.mapper") // Mapper接口的包扫描
@EnableDiscoveryClient // 启用eureka客户端 @EnableEurekaClient
public class ItcastServiceProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItcastServiceProviderApplication.class, args);
    }

}
