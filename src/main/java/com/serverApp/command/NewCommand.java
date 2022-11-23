package com.serverApp.command;

import com.serverApp.command.exception.VotingAlreadyExistsException;
import com.serverApp.server.connectionNode.ConnectionNode;
import com.serverApp.repository.VotingRepositoryInterface;
import com.serverApp.repository.NodeRepositoryInterface;
import com.serverApp.repository.exception.NodeNotFoundException;
import com.serverApp.Init;
import com.serverApp.server.thread.VotingThread;
import com.serverApp.voting.VoteValue;
import com.serverApp.voting.Voting;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class NewCommand extends AbstractCommand
{
    public static final String NAME = "NEW";
    public static final String WRONG_SYNTAX = "Invalid NEW command, expected syntax:" +
            "'NEW <name:string> <initalVote:YN> <description:string>'";

    public final String name;
    public final String description;
    public final VoteValue initialVote;

    private final NodeRepositoryInterface nodeRepository;
    private final VotingRepositoryInterface votingRepository;

    public NewCommand(
            Socket socket,
            String name,
            String description,
            VoteValue initialVote,
            NodeRepositoryInterface nodeRepository,
            VotingRepositoryInterface votingRepository
    ) {
        super(socket);
        this.name = name;
        this.description = description;
        this.initialVote = initialVote;

        this.nodeRepository = nodeRepository;
        this.votingRepository = votingRepository;
    }

    @Override
    public void execute() throws NodeNotFoundException, VotingAlreadyExistsException, IOException {
        ConnectionNode node = this.nodeRepository.getBySocket(this.socket);

        if (node.getNodeName() == null) {
            NokCommand nok = new NokCommand(node.getSocket(),
                    "Node have no name define! Define name before starting voting");
            nok.execute();
            throw new NodeNotFoundException();
        }

        if (this.votingRepository.hasWithName(this.name)) {
            NokCommand nok = new NokCommand(node.getSocket(),
                    "Voting with this name already exist!");
            nok.execute();
            throw new VotingAlreadyExistsException();
        }

        Voting voting = new Voting(
                this.nodeRepository,
                node,
                this.name,
                this.description,
                this.initialVote
        );

        try {
            toAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        VotingThread votingThread = new VotingThread(voting, Init.VOTING_TIMEOUT);
        votingThread.start();
        this.votingRepository.add(voting);
    }

    public String asString()
    {
        return NewCommand.NAME + " " + this.name + " " + this.initialVote + " " + this.description;
    }

    public String response() {
        try {
            return NewCommand.NAME + " " + nodeRepository.getBySocket(socket).getNodeName() + " " + this.name +  " " + this.description;
        } catch (NodeNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void toAll() throws IOException {
        for(ConnectionNode Cn : nodeRepository.findAllActive()) {
                PrintWriter output = new PrintWriter(Cn.getSocket().getOutputStream(), true);
                output.println(response());
        }
    }

    @Override
    public boolean isEqual(CommandInterface other)
    {
        return other instanceof NewCommand
                && ((NewCommand)other).asString().equals(this.asString())
                ;
    }
}