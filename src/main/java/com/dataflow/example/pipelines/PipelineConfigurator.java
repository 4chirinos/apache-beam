package com.dataflow.example.pipelines;

import com.dataflow.example.utils.PipelineRunnerType;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.extern.slf4j.Slf4j;
import org.apache.beam.runners.dataflow.DataflowRunner;
import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.runners.direct.DirectRunner;
import org.apache.beam.sdk.extensions.gcp.util.gcsfs.GcsPath;
import org.apache.beam.sdk.io.jdbc.JdbcIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
public class PipelineConfigurator {

    @Value("${example.project}")
    private String project;

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

    @Value("${example.runner}")
    private String runner;

    @Value("${example.subscriber}")
    private String subscriber;

    @Value("${example.region}")
    private String region;

    @Value("${example.product.fixedMonitoringWindow}")
    private int fixedWindowDuration;

    @Value("${example.credentials}")
    private String credentialsLocation;

    @Value("${example.cloudAuthenticationAddress}")
    private String cloudAuthenticationAddress;

    public PipelineOptions createPipelineOptions() {
        PipelineOptions pipelineOptions;
        if (runner.equals(PipelineRunnerType.DirectRunner.toString())) {
            pipelineOptions = getDefaultPipelineOptions();
        } else {
            pipelineOptions = getDataFlowPipelineOptions();
        }
        pipelineOptions.setTempLocation(GcsPath.fromComponents(bucket, "temp").toString());
        return pipelineOptions;
    }

    public JdbcIO.DataSourceConfiguration getDataSourceConfig() {
        return JdbcIO.DataSourceConfiguration
                .create(dbDriver, getDatabaseConnectionUrl())
                .withUsername(dbUsername)
                .withPassword(dbPassword);
    }

    private PipelineOptions getDefaultPipelineOptions() {
        PipelineOptions pipelineOptions = PipelineOptionsFactory.create();
        pipelineOptions.setRunner(DirectRunner.class);
        return pipelineOptions;
    }

    private PipelineOptions getDataFlowPipelineOptions() {
        DataflowPipelineOptions dataflowPipelineOptions = PipelineOptionsFactory.as(DataflowPipelineOptions.class);
        dataflowPipelineOptions.setRunner(DataflowRunner.class);
        dataflowPipelineOptions.setProject(project);
        dataflowPipelineOptions.setRegion(region);
        setCredentials(dataflowPipelineOptions);
        return dataflowPipelineOptions;
    }

    private void setCredentials(DataflowPipelineOptions dataflowPipelineOptions) {
        try {
            File credentialsFile = ResourceUtils.getFile(String.format("classpath:%s", credentialsLocation));
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new FileInputStream(credentialsFile))
                    .createScoped(Arrays.asList(cloudAuthenticationAddress));
            dataflowPipelineOptions.setGcpCredential(googleCredentials);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private String getDatabaseConnectionUrl() {
        return String.format("jdbc:mysql://%s:%s/%s", dbHost, dbPort, dbName);
    }
}
