package com.dataflow.example.pipelines.options;

import com.dataflow.example.utils.PipelineRunnerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfigurationOptions {
    private String project;
    private String bucket;
    private String dbHost;
    private String dbPort;
    private String dbName;
    private String dbUsername;
    private String dbPassword;
    private String dbDriver;
    private String dbConnectionUrl;
    private PipelineRunnerType runner;
    private String region;
    private String subscriber;
    private int fixedWindowDuration;
    private String credentialFilePath;
    private String cloudAuthenticationAddress;
}
