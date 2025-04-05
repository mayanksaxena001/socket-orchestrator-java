package com.myludoapp.socketorchestrator.socket;

import com.myludoapp.socketorchestrator.socket.thread.ServerThread;
import org.springframework.beans.factory.annotation.Value;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Mayank_Saxena
 */
@Deprecated
public class SocketServer {

    @Value("${server.port}")
    private static final int SERVER_PORT = 8001;

    private ServerSocket serverSocket = null;

    private Map<Socket, OutputStream> outputStreams = new HashMap<Socket, OutputStream>();

    private ExecutorService executorService;

    public SocketServer(ExecutorService executorService) {
        System.out.println(" SocketServer");
        try {
            this.executorService= executorService;
            listen(SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listen(int port) throws IOException {
        InetAddress addr = InetAddress.getLocalHost();
        serverSocket = new ServerSocket(port, 100, addr);

        String message = "Listening on " + serverSocket;
        System.out.println(message);// display on console
        try {
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {

                System.out.println("connected to " +socket+"\n");
//                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
//                //convert ObjectInputStream object to String
//                String msg = (String) ois.readObject();
//                System.out.println("Message Received: " + msg);
                // Create a DataOutputStream for writing data to the
                // other side
                DataOutputStream dout = new DataOutputStream(
                        socket.getOutputStream());
                // Save this stream so we don't need to make it again
                outputStreams.put(socket, dout);
                // Create a new thread for this connection, and then forget
                // about it
                Runnable serverThread=new ServerThread(this, socket);
                CompletableFuture.runAsync(serverThread,this.executorService);
                socket = null;
            }
        } catch (Exception e) {
            System.out.println("Server Socket connection closed");
        } finally {
            serverSocket.close();
        }
    }

    public void sendToAll(String message) {
        // We synchronize on this because another thread might be
        // calling removeConnection() and this would screw us up
        // as we tried to walk through the list
        synchronized (outputStreams) {
            // For each client ...
            for (Map.Entry<Socket, OutputStream> map : getOutputStreams().entrySet()) {
                // ... get the output stream ...
                DataOutputStream dout = (DataOutputStream) map.getValue();
                // ... and send the message
                try {
                    dout.writeUTF(message);
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            }
        }
    }

    public void removeConnection(Socket socket) {
        // Synchronize so we don't mess up sendToAll() while it walks
        // down the list of all output streams
        synchronized (outputStreams) {
            // Tell the world
            String message = "Removing connection to " + socket;
            System.out.println(message);
            // Remove it from our hashtable/list
            outputStreams.remove(socket);
            for (Map.Entry<Socket, OutputStream> map : getOutputStreams().entrySet()) {
                // ... get the output stream ...
                DataOutputStream dout = (DataOutputStream) map.getValue();
                // ... and send the message
                try {
                    dout.writeUTF("The socket " + socket + " has logged out");
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            }
            // Make sure it's closed
            try {
                socket.close();
            } catch (IOException ie) {
                System.out.println("Error closing " + socket);
                ie.printStackTrace();
            }
        }

    }

    // Get an enumeration of all the OutputStreams, one for each client
    // connected to us
    Map<Socket, OutputStream> getOutputStreams() {
        return outputStreams;
    }
}
