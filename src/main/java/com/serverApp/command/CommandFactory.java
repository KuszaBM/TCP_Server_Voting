package com.serverApp.command;

import com.serverApp.command.exception.SyntaxException;
import com.serverApp.repository.VotingRepositoryInterface;
import com.serverApp.repository.NodeRepositoryInterface;
import com.serverApp.voting.VoteValue;

import java.net.Socket;

public class CommandFactory {
        NodeRepositoryInterface nodeRepository;
        VotingRepositoryInterface votingRepository;

        public CommandFactory(
                NodeRepositoryInterface nodeRepository,
                VotingRepositoryInterface votingRepository
        ) {
            this.nodeRepository = nodeRepository;
            this.votingRepository = votingRepository;
        }

        public CommandInterface create(String input, Socket socket)
        {
            String type = input.split(" ")[0];

            try {
                switch (type) {
                    case NodeCommand.NAME:
                        return this.createNodeCommand(input, socket);
                    case NewCommand.NAME:
                        return this.createNewCommand(input, socket);
                    case VoteCommand.NAME:
                        return this.createVoteCommand(input, socket);
                    case PingCommand.NAME:
                        return this.createPingCommand(input, socket);
                    case PongCommand.NAME:
                        return this.createPongCommand(input, socket);
                    default:
                        return new NokCommand(socket, SyntaxException.UNRECOGNIZED);
                }
            } catch (SyntaxException exception) {
                return new NokCommand(socket, exception.reason);
            } catch (IllegalArgumentException exception) {
                return new NokCommand(socket, "something went wrong");
            }
        }

        private NodeCommand createNodeCommand(String input, Socket socket) throws SyntaxException
        {
            String[] parts = input.split(" ");

            if (parts.length != 2) {
                throw new SyntaxException(NodeCommand.WRONG_SYNTAX);
            }

            return new NodeCommand(
                    socket,
                    parts[1],
                    this.nodeRepository
            );
        }

        private NewCommand createNewCommand(String input, Socket socket) throws SyntaxException
        {
            String[] parts = input.split(" ");

            if (parts.length < 4) {
                throw new SyntaxException(NewCommand.WRONG_SYNTAX);
            }

            String name = parts[1];

            VoteValue initialVote = VoteValue.valueOf(parts[2]);

            String description = parts[3];

            for (int i=4; i<parts.length;i++) {
                description += " " + parts[i];
            }

            return new NewCommand(
                    socket,
                    name,
                    description,
                    initialVote,
                    this.nodeRepository,
                    this.votingRepository
            );
        }

        private VoteCommand createVoteCommand(String input, Socket socket) throws SyntaxException
        {
            String[] parts = input.split(" ");

            if (parts.length < 3) {
                throw new SyntaxException(VoteCommand.WRONG_SYNTAX);
            }

            String name = parts[1];

            VoteValue vote = VoteValue.valueOf(parts[2]);

            return new VoteCommand(
                    socket,
                    name,
                    vote,
                    this.nodeRepository,
                    this.votingRepository

            );
        }

        private PingCommand createPingCommand(String input, Socket socket) throws SyntaxException
        {
            String[] parts = input.split(" ");

            if (parts.length != 1) {
                throw new SyntaxException(PingCommand.WRONG_SYNTAX);
            }

            return new PingCommand(socket);
        }

        private PongCommand createPongCommand(String input, Socket socket) throws SyntaxException
        {
            String[] parts = input.split(" ");

            if (parts.length != 1) {
                throw new SyntaxException(PongCommand.WRONG_SYNTAX);
            }

            return new PongCommand(socket);
        }
    }
