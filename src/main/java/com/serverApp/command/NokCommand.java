package com.serverApp.command;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class NokCommand extends AbstractCommand {
    public final String reason;

    public NokCommand(Socket socket, String reason)
    {
        super(socket);
        this.reason = reason;
    }

    @Override
    public void execute() throws IOException
    {
        PrintWriter writer = new PrintWriter(this.socket.getOutputStream(), true);
        writer.println("NOK" + " " + this.reason);
    }

    @Override
    public boolean isEqual(CommandInterface other)
    {
        return other instanceof NokCommand
                && ((NokCommand)other).reason.equals(this.reason)
                ;
    }
}
