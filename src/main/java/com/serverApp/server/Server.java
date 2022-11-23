package com.serverApp.server;

import com.serverApp.Init;
import com.serverApp.command.CommandFactory;
import com.serverApp.repository.NodeRepository;
import com.serverApp.repository.NodeRepositoryInterface;
import com.serverApp.repository.VotingRepository;
import com.serverApp.repository.exception.NodeNotFoundException;
import com.serverApp.server.connectionNode.ConnectionNode;
import com.serverApp.server.thread.ConnectionThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final NodeRepositoryInterface nodeRepository;
    private final long timeoutClient;
    private final long timeoutVoting;
    private final int port;
    private final CommandFactory commandFactory;

    public Server() {
        this.timeoutClient = Init.CLIENT_TIMEOUT;
        this.timeoutVoting = Init.VOTING_TIMEOUT;
        this.nodeRepository = new NodeRepository();
        this.port = Init.PORT;
        this.commandFactory = new CommandFactory(this.nodeRepository, new VotingRepository());
    }

    public void runServer() {

            try (ServerSocket serverSocket = new ServerSocket(this.port)) {
                while (true) {
                    waitForNewConnection(serverSocket);
                }
            } catch (IOException e){
                stopServer();
            }
    }

    public void stopServer() {
        for(ConnectionNode node : nodeRepository.findAllActive()) {
                try {
                    node.deactivateNode();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        }

    }

    private void waitForNewConnection(ServerSocket serverSocket) throws IOException {
        boolean isRegistered = false;
        ConnectionNode node = null;
        Socket socket = serverSocket.accept();
        try {
            node = nodeRepository.getByAddress(socket.getInetAddress());
            isRegistered = true;
        } catch (NodeNotFoundException e) {

        }

        if (isRegistered) {
            node.reactivateNode(socket);
            ConnectionThread connectionThread = new ConnectionThread(commandFactory, node);
            connectionThread.start();
        } else {
            ConnectionNode newNode = new ConnectionNode(socket);
            this.nodeRepository.add(newNode);
            ConnectionThread connectionThread = new ConnectionThread(commandFactory, newNode);
            connectionThread.start();
        }
    }
}
