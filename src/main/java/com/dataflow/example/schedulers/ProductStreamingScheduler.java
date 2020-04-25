package com.dataflow.example.schedulers;

import com.dataflow.example.runners.ProductStreamingRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class ProductStreamingScheduler {

    @Autowired
    private ProductStreamingRunner productStreamingRunner;

    @Scheduled(fixedRate = 60000000)
    public void scheduled() {
        log.info(String.format("Triggering product streaming pipeline at %s", new Date().toString()));
        productStreamingRunner.run();
    }
}
