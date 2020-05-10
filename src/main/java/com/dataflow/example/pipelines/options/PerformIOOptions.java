package com.dataflow.example.pipelines.options;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PerformIOOptions implements Serializable {
    private String dbDriver;
    private String dbHost;
    private String dbPort;
    private String dbName;
    private String dbUsername;
    private String dbPassword;
    private int dbConnectionMaximumPoolSize;
    private int dbConnectionMinimumIdle;
    private long dbConnectionIdleTimeout;
    private long dbConnectionMaxLifetime;
    private String dbPoolName;
}
