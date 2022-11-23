package com.serverApp.command;

import java.net.Socket;

abstract public class AbstractCommand implements CommandInterface{
    protected final Socket socket;
    public AbstractCommand(Socket socket)
    {
        this.socket = socket;
    }
}
