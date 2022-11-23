package com.serverApp.server.connectionNode;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ConnectionNode {
    private boolean isActive;
    private String nodeName;
    private Socket socket;
    private InetAddress clientAddress;

    public ConnectionNode(Socket socket) {
        this.socket = socket;
        this.clientAddress = socket.getInetAddress();
        this.isActive = true;
        this.nodeName = null;
    }
    public void deactivateNode() throws IOException {
        this.socket.close();
        this.isActive = false;

    }
    public void reactivateNode(Socket socket) {
        this.socket = socket;
        this.isActive = true;
    }
    public boolean isActive() {
        return isActive;
    }
    public String getNodeName() {
        return nodeName;
    }
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
    public Socket getSocket() {
        return socket;
    }
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

}
