package com.dataflow.example.pipelines.functions.SimpleFunction;

import com.dataflow.example.dtos.Record;
import org.apache.beam.sdk.transforms.SimpleFunction;

public class RecordToString extends SimpleFunction<Record, String> {
    @Override
    public String apply(Record record) {
        return record.toString();
    }
}
