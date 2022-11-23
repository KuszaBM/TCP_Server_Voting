package com.serverApp.server.thread;

import com.serverApp.server.connectionNode.ConnectionNode;
import java.io.IOException;
import java.net.Socket;

public class CheckNodeConnectionThread extends Thread{
    private long timeout;
    private final ConnectionNode node;
    private final Socket oldSocket;



    public CheckNodeConnectionThread(long timeout, ConnectionNode node) {
        this.timeout = timeout;
        this.node = node;
        this.oldSocket = this.node.getSocket();
    }

    @Override
    public void run() {
            try {
                System.out.println("PINGING");
                sleep(timeout);
                if(oldSocket.equals(node.getSocket())) {
                    node.deactivateNode();
                    System.out.println("Session closed");
                }
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
    }
}
