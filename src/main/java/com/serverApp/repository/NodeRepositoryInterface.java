package com.serverApp.repository;

import com.serverApp.server.connectionNode.ConnectionNode;
import com.serverApp.repository.exception.NodeNotFoundException;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public interface NodeRepositoryInterface {
    public void add(ConnectionNode voter);

    public ConnectionNode getByName(String name) throws NodeNotFoundException;

    public ConnectionNode getBySocket(Socket socket) throws NodeNotFoundException;

    public ArrayList<ConnectionNode> findAll();
    public ArrayList<ConnectionNode> findAllActive();
    ConnectionNode getByAddress(InetAddress address) throws NodeNotFoundException;

    public int countActive();
}
