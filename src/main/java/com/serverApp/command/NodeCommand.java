package com.serverApp.command;

import com.serverApp.server.connectionNode.ConnectionNode;
import com.serverApp.repository.NodeRepositoryInterface;
import com.serverApp.repository.exception.NodeNotFoundException;

import java.io.IOException;
import java.net.Socket;

public class NodeCommand extends AbstractCommand
{
    public static final String NAME = "NODE";
    public static final String WRONG_SYNTAX = "Invalid NODE command, expected syntax:'NODE <name>'";

    public final String nodeName;
    private final NodeRepositoryInterface repository;

    public NodeCommand(
            Socket socket,
            String nodeName,
            NodeRepositoryInterface repository
    ) {
        super(socket);
        this.nodeName = nodeName;
        this.repository = repository;

    }

    @Override
    public void execute() throws NodeNotFoundException, IOException {

        boolean alreadyUsed = false;
        ConnectionNode node = repository.getBySocket(socket);
        if (node.getNodeName() == null) {
            for (ConnectionNode cN : repository.findAll()) {
                if (nodeName.equals(cN.getNodeName())) {
                    alreadyUsed = true;
                    NokCommand nok = new NokCommand(node.getSocket(),
                            "Node with this name already exist!");
                    nok.execute();
                }
            }
            if (!alreadyUsed) {
                repository.getBySocket(super.socket).setNodeName(nodeName);
            }
        } else {
            NokCommand nok = new NokCommand(node.getSocket(),
                    "Node already named!");
            nok.execute();
        }


    }

    public String asString()
    {
        return NodeCommand.NAME + " " + this.nodeName;
    }

    @Override
    public boolean isEqual(CommandInterface other)
    {
        return other instanceof NodeCommand
                && ((NodeCommand)other).asString().equals(this.asString())
                ;
    }
}
