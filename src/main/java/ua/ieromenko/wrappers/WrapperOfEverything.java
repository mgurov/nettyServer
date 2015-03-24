package ua.ieromenko.wrappers;

import ua.ieromenko.util.LoggingQueue;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author Alexandr Ieromenko on 05.03.15.
 *
 * Container for all statistics available at the moment
 */
public class WrapperOfEverything {

    private int connectionsCounter;
    private int activeConnectionsCounter;

    private final ConcurrentHashMap<String, RequestsCounter> requestsCounter; //IP, unique req per ip
    private final ConcurrentHashMap<String, Integer> redirectionPerURL; //URL, count

    private final LoggingQueue<ConnectionLogUnit> log; //IP, URI, timestamp, sentB, receivedB, speedB/s //

    public WrapperOfEverything(ConcurrentHashMap<String, Integer> redirectionPerURL,
                               LoggingQueue<ConnectionLogUnit> log,
                               ConcurrentHashMap<String, RequestsCounter> requestsCounter,
                               int activeConnectionsCounter,
                               int connectionsCounter) {
        this.redirectionPerURL = redirectionPerURL;
        this.log = log;
        this.requestsCounter = requestsCounter;
        this.activeConnectionsCounter = activeConnectionsCounter;
        this.connectionsCounter = connectionsCounter;
    }

    public int getConnectionsCounter() {
        return connectionsCounter;
    }

    public int getActiveConnectionsCounter() {
        return activeConnectionsCounter;
    }

    public ConcurrentHashMap<String, RequestsCounter> getRequestsCounter() {
        return requestsCounter;
    }

    public ConcurrentHashMap<String, Integer> getRedirectionPerURL() {
        return redirectionPerURL;
    }

    public LoggingQueue<ConnectionLogUnit> getLog() {
        return log;
    }
}
