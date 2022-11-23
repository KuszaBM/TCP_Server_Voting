import com.serverApp.command.CommandFactory;
import com.serverApp.server.connectionNode.ConnectionNode;
import com.serverApp.repository.VotingRepository;
import com.serverApp.repository.VotingRepositoryInterface;
import com.serverApp.repository.NodeRepository;
import com.serverApp.repository.NodeRepositoryInterface;
import com.serverApp.server.Server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static java.lang.Thread.sleep;

@RunWith(MockitoJUnitRunner.class)
public class ServerTest {

    @Mock
    private Socket socket;

    @Mock
    private OutputStream myOutputStream;

    @Mock
    private InputStream myInputStream;

    @Before
    public void setup() {
    }

    @After
    public void tearDown() {
    }


    @Test
    public void shouldBeDeactivated() throws IOException {

        //Arrange
        Mockito.when(socket.getOutputStream()).thenReturn(myOutputStream);
        Mockito.when(socket.getInputStream()).thenReturn(myInputStream);

        ConnectionNode node1 = new ConnectionNode(socket);

        //Act
        node1.deactivateNode();

        //Assert
        Assertions.assertFalse(node1.isActive());
    }

    @Test
    public void shouldBeReactivated() throws IOException {

        //Arrange
        Socket socket1 = Mockito.mock(Socket.class);
        Mockito.when(socket1.getOutputStream()).thenReturn(myOutputStream);
        Mockito.when(socket1.getInputStream()).thenReturn(myInputStream);
        Mockito.when(socket.getOutputStream()).thenReturn(myOutputStream);
        Mockito.when(socket.getInputStream()).thenReturn(myInputStream);

        ConnectionNode node1 = new ConnectionNode(socket);

        //Act
        node1.deactivateNode();
        node1.reactivateNode(socket1);

        //Assert
        Assertions.assertTrue(node1.isActive());
    }
}
