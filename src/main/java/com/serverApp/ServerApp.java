package com.serverApp;

import com.serverApp.server.Server;

public class ServerApp {

    public static void main(String[] args)  {
        System.out.println("Voting Server app");
        Server server = new Server();
        server.runServer();
    }

}
