package com.mii.komi.dto.sysmon;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vinch
 */
public class TCPServer {
    
    private int activeConnections;
    
    private String lastTransactions = "";
    
    private List<TCPServerConnection> connections = new ArrayList<>();

    /**
     * @return the activeConnections
     */
    public int getActiveConnections() {
        return activeConnections;
    }

    /**
     * @param activeConnections the activeConnections to set
     */
    public void setActiveConnections(int activeConnections) {
        this.activeConnections = activeConnections;
    }

    /**
     * @return the lastTransactions
     */
    public String getLastTransactions() {
        return lastTransactions;
    }

    /**
     * @param lastTransactions the lastTransactions to set
     */
    public void setLastTransactions(String lastTransactions) {
        this.lastTransactions = lastTransactions;
    }

    /**
     * @return the connections
     */
    public List<TCPServerConnection> getConnections() {
        return connections;
    }

    /**
     * @param connections the connections to set
     */
    public void setConnections(List<TCPServerConnection> connections) {
        this.connections = connections;
    }
    
}
