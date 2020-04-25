package com.dataflow.example.pipelines;

import com.dataflow.example.dtos.Record;
import com.dataflow.example.pipelines.functions.filters.FilterHeader;
import com.dataflow.example.pipelines.functions.transformers.LineToRecord;
import com.dataflow.example.pipelines.options.ReadFilesAndInsertIntoDatabaseOptions;
import com.dataflow.example.utils.PipelineUtils;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.extensions.gcp.util.gcsfs.GcsPath;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.jdbc.JdbcIO;
import org.apache.beam.sdk.transforms.Filter;
import org.apache.beam.sdk.transforms.MapElements;

public class ReadFilesAndInsertIntoDatabasePipeline {

    public static Pipeline create(ReadFilesAndInsertIntoDatabaseOptions options) {
        String fullPath = GcsPath.fromComponents(options.getBucket(), options.getFilename()).toString();
        Pipeline pipeline = Pipeline.create(options.getPipelineOptions());
        pipeline.apply("Reading file", TextIO.read().from(fullPath))
                .apply("Removing header", Filter.by(new FilterHeader()))
                .apply("Mapping line to record dto", MapElements.via(new LineToRecord()))
                .apply("Inserting records into database", JdbcIO.<Record> write()
                        .withDataSourceConfiguration(options.getDataSourceConfiguration())
                        .withStatement("insert into beam (first_name, last_name, company_name, address, city, " +
                                "country, postal, phone1, phone2, email, web) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")
                        .withPreparedStatementSetter((PipelineUtils::getPreparedStatement)));
        return pipeline;
    }
}