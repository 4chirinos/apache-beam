package com.dataflow.example.pipelines.options;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.beam.sdk.io.jdbc.JdbcIO;
import org.apache.beam.sdk.options.PipelineOptions;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReadFilesAndInsertIntoDatabaseOptions {
    private PipelineOptions pipelineOptions;
    private JdbcIO.DataSourceConfiguration dataSourceConfiguration;
    private String bucket;
    private String filename;
    private String dbHost;
    private String dbPort;
    private String dbName;
    private String dbUsername;
    private String dbPassword;
    private String dbDriver;
}
