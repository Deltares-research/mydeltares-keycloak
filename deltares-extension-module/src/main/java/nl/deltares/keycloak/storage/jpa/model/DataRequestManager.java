package nl.deltares.keycloak.storage.jpa.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DataRequestManager {

    private Map<String, DataRequest> dataRequests = Collections.synchronizedMap(new HashMap<>());
    private static DataRequestManager requestManager;

    public static DataRequestManager getInstance(){
        if (requestManager == null){
            requestManager = new DataRequestManager();
        }
        return requestManager;
    }

    public DataRequest getDataRequest(String id){
        DataRequest request = dataRequests.get(id);
        if (request == null) return null;
        if (request.getStatus() == DataRequest.STATUS.expired) return null;
        return request;
    }

    public void addDataRequest(DataRequest dataRequest){
        if (dataRequest == null) throw new IllegalArgumentException("dataRequest == null");

        DataRequest existingRequest = dataRequests.get(dataRequest.getId());
        if (existingRequest != null){
            DataRequest.STATUS status = existingRequest.getStatus();
            if (status == DataRequest.STATUS.running) {
                throw new IllegalStateException(String.format("Existing data request %s is still running! Either terminate this request or wait for it to complete.", dataRequest.getId()));
            }
            if (status == DataRequest.STATUS.available){
                throw new IllegalStateException(String.format("Existing data request %s is already available! Retrieve available results from existing request.", dataRequest.getId()));
            }
            removeDataRequest(existingRequest);
        }
        dataRequests.put(dataRequest.getId(), dataRequest);
    }


    public void removeDataRequest(DataRequest dataRequest)  {
        if (dataRequest == null) throw new IllegalArgumentException("dataRequest == null");
        dataRequest.dispose();
        dataRequests.remove(dataRequest.getId());
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
}
