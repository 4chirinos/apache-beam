package com.dataflow.example.pipelines;

import com.dataflow.example.pipelines.functions.SimpleFunction.LineToRecord;
import com.dataflow.example.pipelines.functions.DoFn.PerformIO;
import com.dataflow.example.pipelines.options.PerformIOOptions;
import com.dataflow.example.pipelines.options.StreamingFilesAndInsertIntoDatabaseOptions;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.Watch;
import org.joda.time.Duration;

public class StreamingFilesAndInsertIntoDatabasePipeline {

    public static Pipeline create(StreamingFilesAndInsertIntoDatabaseOptions options) {
        Pipeline pipeline = Pipeline.create(options.getPipelineOptions());
        pipeline.apply(String.format("Monitoring new files at: %s", options.getMonitoringPath()),
                TextIO.read().from(options.getMonitoringPath())
                        .watchForNewFiles(Duration.standardSeconds(options.getFixedMonitoringWindow()),
                                Watch.Growth.afterTimeSinceNewOutput(Duration.standardHours(
                                        options.getMaxPipelineIdleHours()))))
                .apply("Mapping line to record dto", MapElements.via(new LineToRecord()))
                .apply("Inserting records into database", ParDo.of(new PerformIO(
                        getPerformOperationsOptions(options))));
        return pipeline;
    }

    private static PerformIOOptions getPerformOperationsOptions(StreamingFilesAndInsertIntoDatabaseOptions options) {
        return PerformIOOptions.builder()
                .dbDriver(options.getDbDriver())
                .dbHost(options.getDbHost())
                .dbPort(options.getDbPort())
                .dbName(options.getDbName())
                .dbUsername(options.getDbUsername())
                .dbPassword(options.getDbPassword())
                .dbConnectionMaximumPoolSize(options.getDbConnectionMaximumPoolSize())
                .dbConnectionMinimumIdle(options.getDbConnectionMinimumIdle())
                .dbConnectionIdleTimeout(options.getDbConnectionIdleTimeout())
                .dbConnectionMaxLifetime(options.getDbConnectionMaxLifetime())
                .dbPoolName(options.getDbPoolName())
                .build();
    }
}
