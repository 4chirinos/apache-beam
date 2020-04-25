package com.dataflow.example.pipelines.functions.transformers;

import com.dataflow.example.dtos.Record;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.transforms.SimpleFunction;

import java.io.IOException;

public class PubSubToRecord extends SimpleFunction<PubsubMessage, Record> {

    private ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public Record apply(PubsubMessage message) {
        Record record = new Record();
        String data = new String(message.getPayload());
        try {
            record = mapper.readValue(data, Record.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return record;
    }
}
