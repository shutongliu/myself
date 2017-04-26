package com.test; /*
/**
 * Created by liust on 2017/2/16.
 */
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import com.test.netty.TimeServer;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application {
    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
        try {
            new TimeServer().bind(8081);
        }catch (Exception e){

        }
    }
}
