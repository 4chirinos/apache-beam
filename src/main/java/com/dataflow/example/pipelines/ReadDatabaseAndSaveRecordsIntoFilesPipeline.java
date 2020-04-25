package com.dataflow.example.pipelines;

import com.dataflow.example.dtos.Record;
import com.dataflow.example.pipelines.functions.mappers.RecordRowMapper;
import com.dataflow.example.pipelines.functions.transformers.RecordToString;
import com.dataflow.example.pipelines.options.ReadDatabaseAndSaveRecordsIntoFilesOptions;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.AvroCoder;
import org.apache.beam.sdk.extensions.gcp.util.gcsfs.GcsPath;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.jdbc.JdbcIO;
import org.apache.beam.sdk.transforms.MapElements;

public class ReadDatabaseAndSaveRecordsIntoFilesPipeline {

    public static Pipeline create(ReadDatabaseAndSaveRecordsIntoFilesOptions options) {
        Pipeline pipeline = Pipeline.create(options.getPipelineOptions());
        pipeline.apply("Reading database", JdbcIO.<Record> read()
                .withDataSourceConfiguration(options.getDataSourceConfiguration())
                .withQuery("select * from beam")
                .withRowMapper(new RecordRowMapper())
                .withCoder(AvroCoder.of(Record.class)))
                .apply("Mapping records to string", MapElements.via(new RecordToString()))
                .apply("Writing to file", TextIO.write()
                        .to(GcsPath.fromComponents(options.getBucket(), options.getFilePath()).toString())
                        //.withoutSharding() generates only one file
                        .withSuffix(options.getSuffix()));
        return pipeline;
    }
}
