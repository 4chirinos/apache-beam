package com.dataflow.example.pipelines.options;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.beam.sdk.options.PipelineOptions;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StreamingFilesAndInsertIntoDatabaseOptions {
    private PipelineOptions pipelineOptions;
    private String monitoringPath;
    private int fixedMonitoringWindow;
    private int maxPipelineIdleHours;
    private String filename;
    private String dbHost;
    private String dbPort;
    private String dbName;
    private String dbUsername;
    private String dbPassword;
    private String dbDriver;
    private int dbConnectionMaximumPoolSize;
    private int dbConnectionMinimumIdle;
    private long dbConnectionIdleTimeout;
    private long dbConnectionMaxLifetime;
    private String dbPoolName;
}
