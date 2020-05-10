package com.dataflow.example.runners;

import com.dataflow.example.pipelines.PipelineConfigurator;
import com.dataflow.example.pipelines.TestingPipeline;
import com.dataflow.example.pipelines.options.StreamingFilesAndInsertIntoDatabaseOptions;
import com.dataflow.example.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.extensions.gcp.util.gcsfs.GcsPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestingRunner {

    @Value("${example.bucket}")
    private String bucket;

    @Value("${example.dbHost}")
    private String dbHost;

    @Value("${example.dbPort}")
    private String dbPort;

    @Value("${example.dbName}")
    private String dbName;

    @Value("${example.dbUsername}")
    private String dbUsername;

    @Value("${example.dbPassword}")
    private String dbPassword;

    @Value("${example.dbDriver}")
    private String dbDriver;

    @Value("${example.dbConnectionMaximumPoolSize}")
    private int dbConnectionMaximumPoolSize;

    @Value("${example.dbConnectionMinimumIdle}")
    private int dbConnectionMinimumIdle;

    @Value("${example.dbConnectionIdleTimeout}")
    private int dbConnectionIdleTimeout;

    @Value("${example.dbConnectionMaxLifetime}")
    private int dbConnectionMaxLifetime;

    @Value("${example.dbPoolName}")
    private String dbPoolName;

    @Value("${example.product.pendingLocation}")
    private String pendingLocation;

    @Value("${example.product.processingLocation}")
    private String processingLocation;

    @Value("${example.product.monitoringLocation}")
    private String monitoringLocation;

    @Value("${example.product.fixedMonitoringWindow}")
    private int fixedMonitoringWindow;

    @Value("${example.product.maxPipelineIdleHours}")
    private int maxPipelineIdleHours;

    @Autowired
    private PipelineConfigurator pipelineConfigurator;

    public void run() {
        StreamingFilesAndInsertIntoDatabaseOptions streamingFilesAndInsertIntoDatabaseOptions =
                getStreamingFilesAndInsertIntoDatabaseOptions();
        Pipeline pipeline = TestingPipeline.create(streamingFilesAndInsertIntoDatabaseOptions);
        log.info("Starting runStreamingFilesAndInsertIntoDatabasePipeline");
        pipeline.run();
        log.info("runStreamingFilesAndInsertIntoDatabasePipeline was triggered");
    }

    private StreamingFilesAndInsertIntoDatabaseOptions getStreamingFilesAndInsertIntoDatabaseOptions() {
        return StreamingFilesAndInsertIntoDatabaseOptions.builder()
                .pipelineOptions(pipelineConfigurator.createPipelineOptions())
                .dbDriver(dbDriver)
                .dbHost(dbHost)
                .dbPort(dbPort).dbName(dbName)
                .dbUsername(dbUsername)
                .dbPassword(dbPassword)
                .dbConnectionMaximumPoolSize(dbConnectionMaximumPoolSize)
                .dbConnectionMinimumIdle(dbConnectionMinimumIdle)
                .dbConnectionIdleTimeout(dbConnectionIdleTimeout * Constants.MS_PER_MINUTE)
                .dbConnectionMaxLifetime(dbConnectionMaxLifetime * Constants.MS_PER_MINUTE)
                .dbPoolName(dbPoolName)
                .maxPipelineIdleHours(maxPipelineIdleHours)
                .fixedMonitoringWindow(fixedMonitoringWindow)
                .monitoringPath(GcsPath.fromComponents(bucket, monitoringLocation).toString())
                .build();
    }
}
