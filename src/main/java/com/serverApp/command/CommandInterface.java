package com.serverApp.command;

import com.serverApp.command.exception.VotingAlreadyExistsException;
import com.serverApp.repository.exception.NodeNotFoundException;
import com.serverApp.voting.exception.VotingNotFoundException;

import java.io.IOException;

public interface CommandInterface {

    public boolean isEqual(CommandInterface other);

    public void execute() throws IOException, NodeNotFoundException, VotingAlreadyExistsException, VotingNotFoundException;
}
