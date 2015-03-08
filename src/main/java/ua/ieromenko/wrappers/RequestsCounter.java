package ua.ieromenko.wrappers;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author Alexandr Ieromenko on 06.03.15.
 *
 * Wrapper for counting requests
 */
public class RequestsCounter {
    private String IP;
    private Set<String> requests = new HashSet<>();
    private int connectionCounter;
    private Date lastConnection;

    public RequestsCounter(String IP, String request) {
        this.IP = IP;
        requests.add(request);
        this.connectionCounter = 1;
        this.lastConnection = new Date();
    }

    public synchronized RequestsCounter addRequest(String request){
        requests.add(request);
        this.connectionCounter++;
        lastConnection = new Date();
        return this;
    }

    public String getIP() {
        return IP;
    }

    public int getCountOfUniqueRequests() {
        return requests.size();
    }

    public int getConnectionsCounter() {
        return connectionCounter;
    }

    public Date getLastConnectionDate() {
        return lastConnection;
    }
}
