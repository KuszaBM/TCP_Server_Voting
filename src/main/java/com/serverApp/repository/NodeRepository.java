package com.serverApp.repository;

import com.serverApp.server.connectionNode.ConnectionNode;
import com.serverApp.repository.exception.NodeNotFoundException;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class NodeRepository implements NodeRepositoryInterface {

    public final ArrayList<ConnectionNode> connectionNodes;

    public NodeRepository() {
        this.connectionNodes = new ArrayList<ConnectionNode>();
    }

    @Override
    public void add(ConnectionNode node) {
        if (this.connectionNodes.contains(node)) {
            return;
        }

        this.connectionNodes.add(node);
    }

    @Override
    public ConnectionNode getByName(String name) throws NodeNotFoundException {
        for (ConnectionNode v : this.connectionNodes) {
            if (v.getNodeName().equals(name)) {
                return v;
            }
        } throw new NodeNotFoundException();
    }

    @Override
    public ConnectionNode getBySocket(Socket socket) throws NodeNotFoundException {
        for (ConnectionNode v : this.connectionNodes) {
            if (v.getSocket().equals(socket)) {
                return v;
            }
        } throw new NodeNotFoundException();
    }

    @Override
    public ArrayList<ConnectionNode> findAll() {
        return this.connectionNodes;
    }

    @Override
    public ArrayList<ConnectionNode> findAllActive() {
        ArrayList<ConnectionNode> activeNodes = new ArrayList<>();
        for(ConnectionNode v : this.connectionNodes) {
            if(v.isActive()) {
                activeNodes.add(v);
            }
        }
        return activeNodes;
    }

    @Override
    public ConnectionNode getByAddress(InetAddress address) throws NodeNotFoundException {
        for (ConnectionNode v : this.connectionNodes) {
            if (v.getSocket().getInetAddress().equals(address)) {
                return v;
            }
        } throw new NodeNotFoundException();
    }

    @Override
    public int countActive() {
        int counter = 0;

        for (ConnectionNode v : this.connectionNodes) {
            if (v.isActive()) {
                counter++;
            }
        }
        return counter;
    }
}
