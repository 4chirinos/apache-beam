package com.dataflow.example.runners;

import com.dataflow.example.pipelines.PipelineConfigurator;
import com.dataflow.example.pipelines.ReadFilesAndInsertIntoDatabasePipeline;
import com.dataflow.example.pipelines.options.ReadFilesAndInsertIntoDatabaseOptions;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.CopyWriter;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.Pipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component
public class ProductReadingRunner {

    @Value("${example.project}")
    private String projectId;

    @Value("${example.bucket}")
    private String bucketName;

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

    @Value("${example.product.pendingLocation}")
    private String pendingLocation;

    @Value("${example.product.processingLocation}")
    private String processingLocation;

    @Autowired
    private PipelineConfigurator pipelineConfigurator;

    public void run() {
        List<Blob> pendingFiles = getPendingFiles();
        List<Blob> filesAtProcessingLocation = getFilesAtProcessingLocation(pendingFiles);
        processPendingFiles(filesAtProcessingLocation);
    }

    private void processPendingFiles(List<Blob> files) {
        files.parallelStream().forEach(file -> runReadFilesAndInsertIntoDatabasePipeline(file.getName()));
    }

    private void runReadFilesAndInsertIntoDatabasePipeline(String filename) {
        ReadFilesAndInsertIntoDatabaseOptions readFilesAndInsertIntoDatabaseOptions =
                getReadFileAndInsertIntoDatabaseOptions(filename);
        Pipeline pipeline = ReadFilesAndInsertIntoDatabasePipeline
                .create(readFilesAndInsertIntoDatabaseOptions);
        log.info(String.format("Starting runReadFilesAndInsertIntoDatabasePipeline: %s", filename));
        pipeline.run();
        log.info(String.format("Ending runReadFilesAndInsertIntoDatabasePipeline: %s", filename));
    }

    private List<Blob> getPendingFiles() {
        List<Blob> pendingFiles = new LinkedList<>();
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        Bucket bucket = storage.get(bucketName);
        bucket.list().iterateAll().forEach(file -> {
            if (isPendingFile(file)) {
                pendingFiles.add(file);
                log.info(String.format("%s is at pending location. It will be prepared to be processed", file.getName()));
            }
        });
        return pendingFiles;
    }

    private boolean isPendingFile(Blob blob) {
        String filename = blob.getName();
        return filename.lastIndexOf(".") > 0 && filename.contains(pendingLocation);
    }

    private List<Blob> getFilesAtProcessingLocation(List<Blob> files) {
        List<Blob> filesAtProcessingLocation = new LinkedList<>();
        files.forEach(file -> {
            String targetLocation = getTargetLocation(file.getName(), pendingLocation, processingLocation);
            CopyWriter copyWriter = file.copyTo(bucketName, targetLocation);
            Blob copiedFile = copyWriter.getResult();
            file.delete();
            filesAtProcessingLocation.add(copiedFile);
            log.info(String.format("%s was moved to processing location", file.getName()));
        });
        return filesAtProcessingLocation;
    }

    private String getTargetLocation(String filename, String currentLocation, String targetLocation) {
        return filename.replace(currentLocation, targetLocation);
    }

    private ReadFilesAndInsertIntoDatabaseOptions getReadFileAndInsertIntoDatabaseOptions(String filename) {
        return ReadFilesAndInsertIntoDatabaseOptions.builder()
                .pipelineOptions(pipelineConfigurator.createPipelineOptions())
                .dataSourceConfiguration(pipelineConfigurator.getDataSourceConfig())
                .filename(filename)
                .bucket(bucketName)
                .dbDriver(dbDriver)
                .dbHost(dbHost)
                .dbPort(dbPort)
                .dbName(dbName)
                .dbUsername(dbUsername)
                .dbPassword(dbPassword)
                .build();
    }
}