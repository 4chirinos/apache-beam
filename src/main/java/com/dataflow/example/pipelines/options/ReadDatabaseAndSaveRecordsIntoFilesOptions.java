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
public class ReadDatabaseAndSaveRecordsIntoFilesOptions {
    private PipelineOptions pipelineOptions;
    private JdbcIO.DataSourceConfiguration dataSourceConfiguration;
    private String bucket;
    private String filePath;
    private String suffix;
}
