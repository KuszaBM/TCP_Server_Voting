package com.serverApp.command;

import com.serverApp.server.connectionNode.ConnectionNode;
import com.serverApp.repository.VotingRepositoryInterface;
import com.serverApp.repository.NodeRepositoryInterface;
import com.serverApp.repository.exception.NodeNotFoundException;
import com.serverApp.voting.VoteValue;
import com.serverApp.voting.exception.VotingNotFoundException;
import com.serverApp.voting.Voting;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class VoteCommand extends AbstractCommand
{
    public static final String NAME = "VOTE";
    public static final String WRONG_SYNTAX = "Invalid VOTE command, expected syntax:'VOTE <name:string> <vote:YN>'";

    public final String name;
    public final VoteValue vote;

    private final NodeRepositoryInterface nodeRepository;
    private final VotingRepositoryInterface votingRepository;

    public VoteCommand(
            Socket socket,
            String name,
            VoteValue vote,
            NodeRepositoryInterface nodeRepository,
            VotingRepositoryInterface votingRepository
    ) {
        super(socket);
        this.name = name;
        this.vote = vote;

        this.nodeRepository = nodeRepository;
        this.votingRepository = votingRepository;
    }

    @Override
    public void execute() throws NodeNotFoundException, IOException {
        ConnectionNode node = this.nodeRepository.getBySocket(this.socket);

        if (node.getNodeName() == null) {
            NokCommand nok = new NokCommand(node.getSocket(),
                    "Node have no name define! Define name before starting voting");
            nok.execute();
        }

        Voting voting = null;
        try {
            voting = this.votingRepository.getByName(this.name);
        } catch (VotingNotFoundException e) {
            NokCommand nok = new NokCommand(node.getSocket(),
                    "You can't leave vote here! Voting not exist");
            nok.execute();
        }

        if(voting.canVote(node)) {
            voting.leaveVote(node, this.vote);
            toAll();
        } else {
            NokCommand nok = new NokCommand(node.getSocket(),
                    "You can't leave vote here! Voting is closed or You have no permission to vote");
                nok.execute();
        }
    }

    public String asString()
    {
        return VoteCommand.NAME + " " + this.name + " " + this.vote;
    }

    public void toAll() {

        for(ConnectionNode Cn : nodeRepository.findAllActive()) {
                try (PrintWriter output = new PrintWriter(Cn.getSocket().getOutputStream(), true)) {
                    output.println(asString());
                } catch (IOException e) {
                    System.out.println("Cannot reach client");
                }
        }
    }
    @Override
    public boolean isEqual(CommandInterface other)
    {
        return other instanceof VoteCommand && ((VoteCommand)other).asString().equals(this.asString());
    }
}
