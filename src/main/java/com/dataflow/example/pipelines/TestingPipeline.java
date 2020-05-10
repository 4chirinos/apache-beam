package com.dataflow.example.pipelines;

import com.dataflow.example.pipelines.functions.DoFn.PerformIO;
import com.dataflow.example.pipelines.functions.DoFn.MoveFileToLocation;
import com.dataflow.example.pipelines.functions.SimpleFunction.LineToRecord;
import com.dataflow.example.pipelines.options.MoveFileToLocationOptions;
import com.dataflow.example.pipelines.options.PerformIOOptions;
import com.dataflow.example.pipelines.options.StreamingFilesAndInsertIntoDatabaseOptions;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.FileIO;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.Watch;
import org.joda.time.Duration;

public class TestingPipeline {

    public static Pipeline create(StreamingFilesAndInsertIntoDatabaseOptions options) {
        Pipeline pipeline = Pipeline.create(options.getPipelineOptions());
        pipeline.apply(FileIO.match()
                .filepattern(options.getMonitoringPath())
                .continuously(Duration.standardSeconds(options.getFixedMonitoringWindow()),
                        Watch.Growth.afterTimeSinceNewOutput(Duration
                                .standardHours(options.getMaxPipelineIdleHours()))))
                .apply("Moving files to processing location", ParDo.of(new MoveFileToLocation(
                        getMoveFileOptions(options))))
                .apply("Reading files", FileIO.matchAll())
                .apply("Reading matched files", FileIO.readMatches())
                .apply("Reading file", TextIO.readFiles())
                .apply("Mapping line to record dto", MapElements.via(new LineToRecord()))
                .apply("Performing I/O operations", ParDo.of(new PerformIO(getPerformIOOptions(options))));
        return pipeline;
    }

    private static PerformIOOptions getPerformIOOptions(StreamingFilesAndInsertIntoDatabaseOptions options) {
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

    private static MoveFileToLocationOptions getMoveFileOptions(StreamingFilesAndInsertIntoDatabaseOptions options) {
        return MoveFileToLocationOptions.builder()
                .project("trusty-banner-271900")
                .bucket("gcpapachebeam")
                .targetLocation("processing")
                .build();
    }
}
