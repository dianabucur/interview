package com.interview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableCaching
@EnableFeignClients
@EnableJpaAuditing
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

//    public static void main(String[] args) {
//        String raw = "marypassword";
//        //String encoded = "$2a$10$9T2zkXlUmkXizFiXyFhdBeM5PIEQIoeLvhCnLtnTw.6qVKhNqoqT2";
//
//        var encoder = new BCryptPasswordEncoder();
//        System.out.println("Matches: " + encoder.encode(raw));
//    }
}
