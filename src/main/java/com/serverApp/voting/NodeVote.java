package com.serverApp.voting;

import com.serverApp.server.connectionNode.ConnectionNode;

public class NodeVote {
    public final ConnectionNode node;
    public final VoteValue vote;

    public NodeVote(ConnectionNode node, VoteValue vote)
    {
        this.node = node;
        this.vote = vote;
    }
}
