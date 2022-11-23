package com.serverApp.server.thread;

import com.serverApp.voting.Voting;

public class VotingThread extends Thread {
    private Voting voting;
    private long votingTimeout;

    public VotingThread(Voting voting, long votingTimeout) {
        this.voting = voting;
        this.votingTimeout = votingTimeout;
    }

    @Override
    public void run() {
        while (!voting.isClosed()) {
            try {
                sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            voting.setResult(votingTimeout);
        }
    }
}
