package com.serverApp.command;

import java.net.Socket;

public class PongCommand extends AbstractCommand
{
    public static final String NAME = "PONG";
    public static final String WRONG_SYNTAX = "Invalid PONG command, expected syntax:'PONG'";

    public PongCommand(Socket socket)
    {
        super(socket);
    }

    @Override
    public void execute()
    {
    }

    public String asString()
    {
        return PongCommand.NAME;
    }

    @Override
    public boolean isEqual(CommandInterface other)
    {
        return other instanceof PongCommand;
    }
}

