package com.dataflow.example.utils;

import com.dataflow.example.dtos.Record;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class PipelineUtils {

    public static List<String> getFileNames(Page<Blob> blobs) {
        List<String> fileNames = new LinkedList<>();
        blobs.iterateAll().forEach(blob -> {
            if (isFile(blob.getName())) {
                fileNames.add(blob.getName());
            }
        });
        return fileNames;
    }

    public static void getPreparedStatement(Record record, PreparedStatement preparedStatement) throws SQLException {
        // Given that preparedStatement index starts from 1, let's add 1 to our constants indexes
        preparedStatement.setString(Constants.FIRST_NAME + 1, record.getFirstName());
        preparedStatement.setString(Constants.LAST_NAME + 1, record.getLastName());
        preparedStatement.setString(Constants.COMPANY_NAME + 1, record.getCompanyName());
        preparedStatement.setString(Constants.ADDRESS + 1, record.getAddress());
        preparedStatement.setString(Constants.CITY + 1, record.getCity());
        preparedStatement.setString(Constants.COUNTRY + 1, record.getCountry());
        preparedStatement.setString(Constants.POSTAL + 1, record.getPostal());
        preparedStatement.setString(Constants.PRIMARY_PHONE_NUMBER + 1, record.getPrimaryPhoneNumber());
        preparedStatement.setString(Constants.SECONDARY_PHONE_NUMBER + 1, record.getSecondaryPhoneNumber());
        preparedStatement.setString(Constants.EMAIL + 1, record.getEmail());
        preparedStatement.setString(Constants.WEB + 1, record.getWeb());
    }

    private static boolean isFile(String path) {
        return path.lastIndexOf(".") > 0 && path.contains("csv/");
    }
}
