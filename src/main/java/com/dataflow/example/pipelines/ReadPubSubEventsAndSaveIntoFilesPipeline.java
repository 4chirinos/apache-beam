package com.dataflow.example.pipelines;

import com.dataflow.example.dtos.Record;
import com.dataflow.example.pipelines.functions.transformers.PubSubToRecord;
import com.dataflow.example.pipelines.options.ReadPubSubEventsAndSaveIntoFilesOptions;
import com.dataflow.example.utils.PipelineUtils;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.io.jdbc.JdbcIO;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.joda.time.Duration;

public class ReadPubSubEventsAndSaveIntoFilesPipeline {

    public static Pipeline create(ReadPubSubEventsAndSaveIntoFilesOptions options) {
        Pipeline pipeline = Pipeline.create(options.getPipelineOptions());
        pipeline.apply("Stream input from PubSub", PubsubIO.readMessagesWithAttributes()
                .fromSubscription(options.getSubscription()))
                .apply("Defining fixed window for reading messages",
                        Window.<PubsubMessage> into(FixedWindows
                                .of(Duration.standardSeconds(options.getFixedWindowDuration())))
                                .discardingFiredPanes())
                .apply("Mapping pubSubMessages to record dto", MapElements.via(new PubSubToRecord()))
                .apply("Inserting records into database", JdbcIO.<Record> write()
                        .withDataSourceConfiguration(options.getDataSourceConfiguration())
                        .withStatement("insert into beam (first_name, last_name, company_name, address, city, " +
                                "country, postal, phone1, phone2, email, web) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")
                        .withPreparedStatementSetter((PipelineUtils::getPreparedStatement)));
        return pipeline;
    }
}