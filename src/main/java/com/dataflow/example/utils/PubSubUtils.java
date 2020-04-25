package com.dataflow.example.utils;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PubSubUtils {

    private static final String projectId = "trusty-banner-271900";
    public static final String topicId = "test-topic-1";

    public static void pushMessages(int messages) throws IOException, ExecutionException, InterruptedException {
        List<ApiFuture<String>> futures = new LinkedList<>();
        ProjectTopicName topicName = ProjectTopicName.of(projectId, topicId);
        // Create a publisher instance with default settings bound to the topic
        Publisher publisher = Publisher.newBuilder(topicName).build();
        sendMessages(publisher, futures, messages);
        printMessages(futures);
        // When finished with the publisher, shutdown to free up resources.
        publisher.shutdown();
    }

    private static void sendMessages(Publisher publisher, List<ApiFuture<String>> futures, int messages) {
        for (int i = 0; i < messages; i++) {
            String message = "{\"firstName\":\"Aleshia\",\"lastName\":\"Tomkiewicz\",\"companyName\":" +
                    "\"Alan D Rosenburg Cpa Pc\",\"address\":\"14 Taylor St\",\"city\":\"St. Stephens Ward\"," +
                    "\"country\":\"Kent\",\"postal\":\"CT2 7PP\",\"primaryPhoneNumber\":\"01835-703597\"," +
                    "\"secondaryPhoneNumber\":\"01944-369967\",\"email\": \"atomkiewicz@hotmail.com\",\"web\":" +
                    "\"http://www.alandrosenburgcpapc.co.uk\",\"unexpectedfield\":\"hola\"}";
            // convert message to bytes
            ByteString data = ByteString.copyFromUtf8(message);
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();
            // Schedule a message to be published. Messages are automatically batched.
            ApiFuture<String> future = publisher.publish(pubsubMessage);
            futures.add(future);
        }
    }

    private static void printMessages(List<ApiFuture<String>> futures) throws ExecutionException, InterruptedException {
        // Wait on any pending requests
        List<String> messageIds = null;
        messageIds = ApiFutures.allAsList(futures).get();
        for (String messageId : messageIds) {
            System.out.println(messageId);
        }
    }
}
