package com.dataflow.example.schedulers;

import com.dataflow.example.runners.ProductReadingRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class ProductReadingScheduler {

    @Autowired
    private ProductReadingRunner productReadingRunner;

    //@Scheduled(fixedRate = 60000000)
    public void scheduled() {
        log.info(String.format("Triggering product reading pipeline at %s", new Date().toString()));
        productReadingRunner.run();
    }
}
