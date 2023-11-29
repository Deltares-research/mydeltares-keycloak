package nl.deltares.keycloak.storage.jpa.model;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import nl.deltares.keycloak.storage.rest.model.DownloadCallback;
import nl.deltares.keycloak.storage.rest.model.ExportCsvContent;
import org.jboss.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class DataRequestManager {

    private static final Logger logger = Logger.getLogger(DataRequestManager.class);
    private final Map<String, DataRequest> dataRequests = Collections.synchronizedMap(new HashMap<>());
    private static DataRequestManager requestManager;
    private static final Queue<String> pendingRequestsIds = new LinkedList<>();

    public static DataRequestManager getInstance() {
        if (requestManager == null) {
            requestManager = new DataRequestManager();
        }
        return requestManager;
    }

    public DataRequest getDataRequest(String id) {
        return dataRequests.get(id);
    }

    public void removeDataRequest(DataRequest dataRequest) {
        if (dataRequest == null) throw new IllegalArgumentException("dataRequest == null");
        dataRequest.dispose();
        dataRequests.remove(dataRequest.getId());
    }

    public void addToQueue(DataRequest dataRequest) {
        if (dataRequest == null) throw new IllegalArgumentException("dataRequest == null");
        if (dataRequest.getStatus() != DataRequest.STATUS.pending)
            throw new IllegalStateException(String.format("Data request %s has invalid state %s! State must be 'pending' to add to queue.", dataRequest.getId(), dataRequest.getStatus()));

        DataRequest existingRequest = dataRequests.get(dataRequest.getId());
        if (existingRequest != null && existingRequest.getStatus() == DataRequest.STATUS.running) {
            throw new IllegalStateException(String.format("Data request %s is still running! Either terminate this request or wait for it to complete.", dataRequest.getId()));
        }
        dataRequests.put(dataRequest.getId(), dataRequest);
        pendingRequestsIds.add(dataRequest.getId());
        if (existingRequest != null) existingRequest.dispose();
        if (!hasRunningRequests()) {
            startNextRequest();
        }
    }

    private boolean hasRunningRequests() {
        for (DataRequest request : dataRequests.values()) {
            if (request.getStatus() == DataRequest.STATUS.running) return true;
        }
        return false;
    }

    /**
     * Clears cache
     */
    public void clear() {
        for (DataRequest value : dataRequests.values()) {
            value.dispose();
        }
        dataRequests.clear();
    }

    void fireStateChanged(DataRequest changedRequest) {
        DataRequest.STATUS status = changedRequest.getStatus();
        if (status == DataRequest.STATUS.available
                || status == DataRequest.STATUS.terminated) {
            changedRequest.setDataRequestManager(null); //stop listening
            startNextRequest();
        }
    }

    private void startNextRequest() {

        while (pendingRequestsIds.size() > 0) {
            String nextId = pendingRequestsIds.remove();
            DataRequest request = dataRequests.get(nextId);
            if (request == null) continue;
            request.setDataRequestManager(this);
            request.start();
            break;
        }
    }

    public static Response getExportDataResponse(String requestId){
        DataRequestManager instance = DataRequestManager.getInstance();
        DataRequest dataRequest = instance.getDataRequest(requestId);

        if (dataRequest == null){
            return Response.status(Response.Status.BAD_REQUEST).entity(
                    String.format("Processes id %s does not exist",requestId)).type("text/plain").build();
        }
        DataRequest.STATUS status = dataRequest.getStatus();
        if (status == DataRequest.STATUS.available && dataRequest.getDataFile().exists()) {
            DataRequest finalDataRequest = dataRequest;
            DownloadCallback callback = () -> instance.removeDataRequest(finalDataRequest);
            return Response.
                    ok(getStreamingOutput(dataRequest.getDataFile(), callback)).
                    type("text/csv").
                    build();
        } else if (status == DataRequest.STATUS.terminated) {
            instance.removeDataRequest(dataRequest);
            return Response.serverError().entity(dataRequest.getErrorMessage()).build();
        } else {
            return Response.ok(dataRequest.getStatusMessage()).type("application/json").build();
        }
    }

    public static Response getExportDataResponse(ExportCsvContent content, Properties properties, boolean cacheExport) {

        DataRequestManager instance = DataRequestManager.getInstance();
        DataRequest dataRequest = instance.getDataRequest(content.getId());
        if (dataRequest == null || //request does not exist
                dataRequest.getStatus() == DataRequest.STATUS.expired || //request is no longer valid
                (dataRequest.getStatus() == DataRequest.STATUS.available && !dataRequest.getDataFile().exists())) {
            //request is available however data is not
            try {
                dataRequest = new ExportCsvDataRequest(content, properties);
            } catch (IOException e) {
                return Response.serverError().entity(e.getMessage()).build();
            }
            // New or existing Pending requests always added to queue
            if (dataRequest.getStatus() == DataRequest.STATUS.pending) {
                instance.addToQueue(dataRequest);
            } else if (dataRequest.getStatus() == DataRequest.STATUS.available && !cacheExport) {
                //New or existing Available requests with an existing datafile should have data file deleted
                //if we are not interested in cached content
                try {
                    Files.deleteIfExists(dataRequest.getDataFile().toPath());
                    dataRequest = new ExportCsvDataRequest(content, properties);
                } catch (IOException e) {
                    return Response.serverError().entity(e.getMessage()).build();
                }
                instance.addToQueue(dataRequest);
            }

        }
        DataRequest.STATUS status = dataRequest.getStatus();
        if (status == DataRequest.STATUS.available && dataRequest.getDataFile().exists()) {

            DataRequest finalDataRequest = dataRequest;
            DownloadCallback callback = () -> {
                if (!cacheExport) instance.removeDataRequest(finalDataRequest);
            };
            return Response.
                    ok(getStreamingOutput(dataRequest.getDataFile(), callback)).
                    type("text/csv").
                    build();
        } else if (status == DataRequest.STATUS.terminated) {
            instance.removeDataRequest(dataRequest);
            return Response.serverError().entity(dataRequest.getErrorMessage()).build();
        } else {
            return Response.ok(dataRequest.getStatusMessage()).type("application/json").build();
        }

    }

    static StreamingOutput getStreamingOutput(File data, DownloadCallback callback) {
        return os -> {
            try {
                Files.copy(data.toPath(), os);
                os.flush();
            } catch (Exception e) {
                logger.warn("Error serializing csv content: %s", e);
            } finally {
                callback.downloadComplete();
            }
        };
    }
}
