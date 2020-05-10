package com.dataflow.example.pipelines.functions.SimpleFunction;

import com.dataflow.example.dtos.ElementCreatedEvent;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.transforms.SimpleFunction;

import java.io.IOException;

public class PubSubToElementCreatedEvent extends SimpleFunction<PubsubMessage, ElementCreatedEvent> {

    private ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public ElementCreatedEvent apply(PubsubMessage message) {
        ElementCreatedEvent elementCreatedEvent = new ElementCreatedEvent();
        String data = new String(message.getPayload());
        try {
            elementCreatedEvent = mapper.readValue(data, ElementCreatedEvent.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return elementCreatedEvent;
    }
}
