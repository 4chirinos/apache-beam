package com.dataflow.example.config.pubsub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.integration.AckMode;
import org.springframework.cloud.gcp.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;

@Slf4j
//@Configuration
public class PubSubConfig {

    @Value("${example.subscriber}")
    private String subscriber;

    @Bean
    public PubSubInboundChannelAdapter messageChannelAdapter(
            @Qualifier("pubSubInputChannel") MessageChannel inputChannel,
            PubSubTemplate pubSubTemplate) {
        PubSubInboundChannelAdapter adapter =
                new PubSubInboundChannelAdapter(pubSubTemplate, subscriber);
        adapter.setOutputChannel(inputChannel);
        adapter.setAckMode(AckMode.MANUAL);
        return adapter;
    }

    @Bean
    public MessageChannel pubSubInputChannel() {
        return new DirectChannel();
    }
}
