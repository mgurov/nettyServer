package ua.ieromenko.util;

import java.util.Date;

/**
 * @Author Alexandr Ieromenko on 04.03.15.
 *
 * Wrapper of the one connection statistics
 */
public class ConnectionLogUnit {
    private String IP;
    private String URI;
    private Date timeStamp;
    private int sentBytes;
    private int receivedBytes;
    private long speed;

    public ConnectionLogUnit(String requestIP, Date timeStamp) {
        this.IP = requestIP;
        this.timeStamp = timeStamp;
    }

    public String getIP() {
        return IP;
    }

    public String getURI() {
        return URI;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public long getSentBytes() {
        return sentBytes;
    }

    public long getReceivedBytes() {
        return receivedBytes;
    }

    public long getSpeed() {
        return speed;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public void setReceivedBytes(int receivedBytes) {
        this.receivedBytes = receivedBytes;
    }

    public void setSentBytes(int sentBytes) {
        this.sentBytes = sentBytes;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "ConnectionLogUnit{" +
                "IP='" + IP + '\'' +
                ", URI='" + URI + '\'' +
                ", timeStamp=" + timeStamp +
                ", sentBytes=" + sentBytes +
                ", receivedBytes=" + receivedBytes +
                ", speed=" + speed +
                '}';
    }

}
