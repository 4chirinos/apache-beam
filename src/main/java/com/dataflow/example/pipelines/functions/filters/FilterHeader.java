package com.dataflow.example.pipelines.functions.filters;

import org.apache.beam.sdk.transforms.SerializableFunction;

public class FilterHeader implements SerializableFunction<String, Boolean> {

    private static final String header = "first_name|last_name|company_name|address|city|county|postal|phone1|phone2|email|web";

    @Override
    public Boolean apply(String input) {
        return !header.equals(input);
    }
}
