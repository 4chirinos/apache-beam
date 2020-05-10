package com.dataflow.example.pipelines.functions.DoFn;

import com.dataflow.example.pipelines.options.MoveFileToLocationOptions;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.apache.beam.sdk.extensions.gcp.util.gcsfs.GcsPath;
import org.apache.beam.sdk.io.fs.MatchResult;
import org.apache.beam.sdk.transforms.DoFn;

public class MoveFileToLocation extends DoFn<MatchResult.Metadata, String> {

    private final MoveFileToLocationOptions options;

    public MoveFileToLocation(MoveFileToLocationOptions options) {
        this.options = options;
    }

    @ProcessElement
    public void processElement(ProcessContext context) {
        MatchResult.Metadata metadata = context.element();
        String currentLocation = getCurrentLocation(metadata);
        String targetLocation = getTargetLocation(metadata.resourceId().getFilename());
        Blob blob = moveFile(currentLocation, targetLocation);
        context.output(getFinalLocation(blob));
    }

    Blob moveFile(String currentLocation, String targetLocation) {
        Storage storage = StorageOptions.newBuilder().setProjectId(options.getProject()).build().getService();
        Blob blob = storage.get(options.getBucket(), currentLocation);
        Blob copied = blob.copyTo(options.getBucket(), targetLocation).getResult();
        blob.delete();
        return copied;
    }

    private String getTargetLocation(String filename) {
        return String.format("%s/%s", options.getTargetLocation(), filename);
    }

    private String getCurrentLocation(MatchResult.Metadata metadata) {
        return String.format("%s/%s", metadata.resourceId().getCurrentDirectory().getFilename(),
                metadata.resourceId().getFilename());
    }

    private String getFinalLocation(Blob blob) {
        return GcsPath.fromComponents(options.getBucket(), blob.getName()).toString();
    }
}
