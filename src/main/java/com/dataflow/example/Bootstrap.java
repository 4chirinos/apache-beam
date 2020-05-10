package com.dataflow.example;

import com.dataflow.example.runners.ProductStreamingRunner;
import com.dataflow.example.runners.TestingRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Bootstrap implements CommandLineRunner {

    @Autowired
    private TestingRunner testingRunner;

    @Autowired
    private ProductStreamingRunner productStreamingRunner;

    @Override
    public void run(String... args) throws Exception {
        log.info("Triggering pipeline");
        testingRunner.run();
        //productStreamingRunner.run();
    }
}
