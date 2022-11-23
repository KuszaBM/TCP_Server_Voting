package com.serverApp.repository;

import com.serverApp.voting.exception.VotingNotFoundException;
import com.serverApp.voting.Voting;

import java.util.ArrayList;

public class VotingRepository implements VotingRepositoryInterface {
    public final ArrayList<Voting> items;
    public VotingRepository()
    {
        this.items = new ArrayList();
    }
    @Override
    public void add(Voting voting)
    {
        if (this.items.contains(voting)) {
            return;
        }

        this.items.add(voting);
    }
    @Override
    public Voting getByName(String name) throws VotingNotFoundException
    {
        for (Voting v : this.items) {
            if (v.getName().equals(name)) {
                return v;
            }
        }
        throw new VotingNotFoundException();
    }
    @Override
    public boolean hasWithName(String name)
    {
        for (Voting v : this.items) {
            if (v.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
