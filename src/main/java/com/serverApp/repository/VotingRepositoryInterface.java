package com.serverApp.repository;

import com.serverApp.voting.exception.VotingNotFoundException;
import com.serverApp.voting.Voting;

public interface VotingRepositoryInterface {
    public void add(Voting voting);
    public boolean hasWithName(String name);
    public Voting getByName(String name) throws VotingNotFoundException;
}
