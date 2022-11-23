package com.serverApp.server.thread;

import com.serverApp.command.CommandFactory;
import com.serverApp.command.CommandInterface;
import com.serverApp.command.exception.VotingAlreadyExistsException;
import com.serverApp.repository.exception.NodeNotFoundException;
import com.serverApp.voting.exception.VotingNotFoundException;

import java.io.IOException;
import java.net.Socket;

public class ServerRequestThread extends Thread {
    private String inputString;
    private Socket socket;

    private CommandFactory cmdFactory;


    public ServerRequestThread(String input, Socket socket, CommandFactory cmdFactory) {
        this.inputString = input;
        this.socket = socket;
        this.cmdFactory = cmdFactory;
    }

    @Override
    public void run() {
        CommandInterface cmdParse = this.cmdFactory.create(inputString, socket);
        try {
            cmdParse.execute();
        } catch (IOException | NodeNotFoundException | VotingAlreadyExistsException | VotingNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
