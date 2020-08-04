package com.mux.stats.sdk.muxstats.mockup.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class SimpleHTTPServer extends Thread {

    private boolean isRunning;
    private int port;
    private int bandwidthLimit;

    private ServerSocket server;
    ArrayList<ConnectionWorker> workers = new ArrayList<>();

    /*
     * Run a server on localhost:port
     */
    public SimpleHTTPServer(int port, int bandwidthLimit) throws IOException {
        this.port = port;
        this.bandwidthLimit = bandwidthLimit;
        server = new ServerSocket(port);
    }

    public void run() {
        isRunning = true;
        while (isRunning) {
            try {
                acceptConnection();
//            } catch (InterruptedException e) {
//                // Ignore, sombody killed the server
            } catch (IOException e) {
                // TODO handle this
                e.printStackTrace();
            }
        }
        for (ConnectionWorker worker : workers) {
            worker.kill();
        }
    }

    public void kill() {
        isRunning = false;
        interrupt();
    }

    /*
     * Process HTTP connections
     */
    private void acceptConnection() throws IOException {
        Socket clientSocket = server.accept();
        workers.add(new ConnectionWorker(clientSocket, bandwidthLimit));
    }
}