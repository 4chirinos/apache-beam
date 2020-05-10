package com.dataflow.example.pipelines.functions.SimpleFunction;

import com.dataflow.example.dtos.Record;
import com.dataflow.example.utils.Constants;
import org.apache.beam.sdk.transforms.SimpleFunction;

public class LineToRecord extends SimpleFunction<String, Record> {

    @Override
    public Record apply(String input) {
        String[] fields = input.split(Constants.DELIMITER);
        return Record.builder()
                .firstName(fields[Constants.FIRST_NAME])
                .lastName(fields[Constants.LAST_NAME])
                .companyName(fields[Constants.COMPANY_NAME])
                .address(fields[Constants.ADDRESS])
                .city(fields[Constants.CITY])
                .country(fields[Constants.COUNTRY])
                .postal(fields[Constants.POSTAL])
                .primaryPhoneNumber(fields[Constants.PRIMARY_PHONE_NUMBER])
                .secondaryPhoneNumber(fields[Constants.SECONDARY_PHONE_NUMBER])
                .email(fields[Constants.EMAIL])
                .web(fields[Constants.WEB])
                .build();
    }
}
