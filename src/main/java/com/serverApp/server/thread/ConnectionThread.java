package com.serverApp.server.thread;

import com.serverApp.command.CommandFactory;
import com.serverApp.server.connectionNode.ConnectionNode;
import com.serverApp.Init;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

public class ConnectionThread extends Thread {
    private PrintWriter output;
    private BufferedReader input;
    private Scanner scanner;
    private CommandFactory cmdFactory;
    private ConnectionNode node;


    public ConnectionThread(CommandFactory cmdFactory, ConnectionNode node) {
        this.scanner = new Scanner(System.in);
        this.cmdFactory = cmdFactory;
        this.node = node;
        try {
            this.output = new PrintWriter(node.getSocket().getOutputStream(), true);

            this.input = new BufferedReader(new InputStreamReader(node.getSocket().getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public void run() {
            boolean disconnectPossibility = false;
        while(node.isActive()) {
            try {
                if (input != null) {
                    String outputString = input.readLine();

                    ServerRequestThread serverRequestThread =
                            new ServerRequestThread(outputString, node.getSocket(), cmdFactory);
                    serverRequestThread.start();
                }
            } catch (IOException e) {
                if (!disconnectPossibility) {
                    CheckNodeConnectionThread checkNodeConnectionThread =
                            new CheckNodeConnectionThread(Init.CLIENT_TIMEOUT, node);
                    checkNodeConnectionThread.start();
                    disconnectPossibility = true;
                }
            }
        }
    }
}
