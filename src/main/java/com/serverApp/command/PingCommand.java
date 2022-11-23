package com.serverApp.command;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class PingCommand extends AbstractCommand
{
    public static final String NAME = "PING";
    public static final String WRONG_SYNTAX = "Invalid PING command, expected syntax:'PING'";

    public PingCommand(Socket socket)
    {
        super(socket);
    }

    @Override
    public void execute() throws IOException
    {
        PrintWriter writer = new PrintWriter(this.socket.getOutputStream(), true);
        writer.println("PONG");
    }

    public String asString()
    {
        return PingCommand.NAME;
    }

    @Override
    public boolean isEqual(CommandInterface other)
    {
        return other instanceof PingCommand;
    }
}
