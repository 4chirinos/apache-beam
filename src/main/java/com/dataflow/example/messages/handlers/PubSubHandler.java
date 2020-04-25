package com.dataflow.example.messages.handlers;

import com.dataflow.example.dtos.ElementCreatedEvent;
import com.dataflow.example.pipelines.PipelineConfigurator;
import com.dataflow.example.pipelines.ReadFilesAndInsertIntoDatabasePipeline;
import com.dataflow.example.pipelines.options.ReadFilesAndInsertIntoDatabaseOptions;
import com.dataflow.example.utils.PipelineStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.Pipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage;
import org.springframework.cloud.gcp.pubsub.support.GcpPubSubHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;

@Slf4j
//@Component
public class PubSubHandler {

    @Autowired
    private PipelineConfigurator pipelineConfigurator;

    @Bean
    @ServiceActivator(inputChannel = "pubSubInputChannel")
    public MessageHandler messageReceiver() {
        return message -> {
            String payload = new String((byte[]) message.getPayload());
            BasicAcknowledgeablePubsubMessage originalMessage =
                    message.getHeaders().get(GcpPubSubHeaders.ORIGINAL_MESSAGE, BasicAcknowledgeablePubsubMessage.class);
            originalMessage.ack();
            log.info("Message arrived! Payload: " + payload);
            /*try {
                processEvent(parsePubSubPayload(payload));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
            }*/
        };
    }

    private void processEvent(ElementCreatedEvent event) {
        PipelineStatus pipelineStatus = runPipeline(event);
        switch (pipelineStatus) {
            case SUCCESS:
                log.info("Success: " + event.toString());
            case FAILED:
                log.error("Failed: " + event.toString());
        }
    }

    private PipelineStatus runPipeline(ElementCreatedEvent event) {
        ReadFilesAndInsertIntoDatabaseOptions readFilesAndInsertIntoDatabaseOptions =
                ReadFilesAndInsertIntoDatabaseOptions.builder()
                        .pipelineOptions(pipelineConfigurator.createPipelineOptions())
                        .bucket(event.getBucket())
                        .filename(event.getFilePath())
                        .dataSourceConfiguration(pipelineConfigurator.getDataSourceConfig())
                        .build();
        try {
            Pipeline pipeline = ReadFilesAndInsertIntoDatabasePipeline.create(readFilesAndInsertIntoDatabaseOptions);
            pipeline.run();
            return PipelineStatus.SUCCESS;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return PipelineStatus.SUCCESS;
    }

    private ElementCreatedEvent parsePubSubPayload(String payload) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(payload, ElementCreatedEvent.class);
    }
}
