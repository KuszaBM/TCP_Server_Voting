package com.serverApp.voting;

public enum VoteValue {
    Y, N;

    @Override
    public String toString()
    {
        return this.name();
    }
}
