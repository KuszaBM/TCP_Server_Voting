package com.serverApp.voting;

import com.serverApp.server.connectionNode.ConnectionNode;
import com.serverApp.repository.NodeRepositoryInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Voting {
    private ConnectionNode owner;
    private final ArrayList<ConnectionNode> activeNodesBeforeStart;
    private ArrayList<NodeVote> votes;
    private VoteValue result;
    private final String name;
    final private String description;
    final private LocalDateTime startDate;
    private boolean isClosed;

    public String getName() {
        return name;
    }

    public Voting(
            NodeRepositoryInterface nodeRepository,
            ConnectionNode owner,
            String name,
            String description,
            VoteValue initialVote
    ) {
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.result = null;
        this.startDate = LocalDateTime.now();
        this.votes = new ArrayList<>();
        this.isClosed = false;
        this.activeNodesBeforeStart = new ArrayList<>();
        for(ConnectionNode cN : nodeRepository.findAll()) {
            if(cN.isActive()) {
                activeNodesBeforeStart.add(cN);
            }
        }
        this.votes.add(new NodeVote(owner, initialVote));
    }

    public boolean isClosed() {
        return isClosed;
    }

    private void removeNodeVote(ConnectionNode node)
    {
        for (NodeVote vote: this.votes) {
            if (vote.node.getNodeName().equals(node.getNodeName())) {
                this.votes.remove(vote);
                return;
            }
        }
    }

    public void leaveVote(ConnectionNode voter, VoteValue vote) {
        removeNodeVote(voter);
        votes.add(new NodeVote(voter, vote));
    }

    public boolean canVote(ConnectionNode voter) {
        if(isClosed) {
            return false;
        }
        if(voter.getNodeName() == null) {
            return false;
        }
        for (ConnectionNode cN : activeNodesBeforeStart) {
            if (cN.getNodeName().equals(voter.getNodeName())) {
                return false;
            }
        }
        return true;
    }
    public int countYesVotes() {
        int counter = 0;

        for (NodeVote vote: this.votes) {
            if (vote.vote.equals(VoteValue.Y)) {
                counter++;
            }
        }
        return counter;
    }
    public int contNoVotes() {
        return countVoters() - countYesVotes();
    }
    public int countVoters() {
        return votes.size();
    }
    public int countAvailable() {
        return activeNodesBeforeStart.size();
    }
    public void closeVoting() {
        this.isClosed = true;
        for(ConnectionNode cN : activeNodesBeforeStart) {
            try {
                PrintWriter output = new PrintWriter(cN.getSocket().getOutputStream(), true);
                output.println("RESULT " + this.name + " " + this.result);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public long countDuration() {
        Duration duration = Duration.between(
                this.startDate,
                LocalDateTime.now()
        );
        return duration.getSeconds();
    }
    public void setResult(long timeout) {
        if(!isClosed) {
            if (countDuration() > timeout) {
                if(countYesVotes() > contNoVotes()) {
                    this.result = VoteValue.Y;
                } else {
                    this.result = VoteValue.N;
                }
                closeVoting();
            } else if(countYesVotes() > countAvailable()/2 ) {
                this.result = VoteValue.Y;
                closeVoting();
            } else if(contNoVotes() > countAvailable()/2) {
                this.result = VoteValue.N;
                closeVoting();
            }
        }
    }
}
