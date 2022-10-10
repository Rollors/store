package com.cy.store;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
// MapperScan指定当前项目中的mapper接口路径位置，在项目启动的时候会自动加载所有接口
@MapperScan("com.cy.mapper")
public class StoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreApplication.class, args);
    }


}
