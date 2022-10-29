package dev.komp15.generatorrecruitmenttask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class GeneratorRecruitmentTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeneratorRecruitmentTaskApplication.class, args);
    }

}
