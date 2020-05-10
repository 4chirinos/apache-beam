package com.dataflow.example.pipelines.functions.RowMapper;

import com.dataflow.example.dtos.Record;
import org.apache.beam.sdk.io.jdbc.JdbcIO;

import java.sql.ResultSet;

public class RecordRowMapper implements JdbcIO.RowMapper<Record> {

    private static final int FIRST_NAME = 1;
    private static final int LAST_NAME = 2;
    private static final int COMPANY_NAME = 3;
    private static final int ADDRESS = 4;
    private static final int CITY = 5;
    private static final int COUNTRY = 6;
    private static final int POSTAL = 7;
    private static final int PRIMARY_PHONE_NUMBER = 8;
    private static final int SECONDARY_PHONE_NUMBER = 9;
    private static final int EMAIL = 10;
    private static final int WEB = 11;

    @Override
    public Record mapRow(ResultSet resultSet) throws Exception {
        return Record.builder()
                .firstName(resultSet.getString(FIRST_NAME))
                .lastName(resultSet.getString(LAST_NAME))
                .companyName(resultSet.getString(COMPANY_NAME))
                .address(resultSet.getString(ADDRESS))
                .city(resultSet.getString(CITY))
                .country(resultSet.getString(COUNTRY))
                .postal(resultSet.getString(POSTAL))
                .primaryPhoneNumber(resultSet.getString(PRIMARY_PHONE_NUMBER))
                .secondaryPhoneNumber(resultSet.getString(SECONDARY_PHONE_NUMBER))
                .email(resultSet.getString(EMAIL))
                .web(resultSet.getString(WEB))
                .build();
    }
}
