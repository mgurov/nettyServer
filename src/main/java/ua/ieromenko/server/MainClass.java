package ua.ieromenko.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;

/**
 * @Author Alexandr Ieromenko on 07.03.15.
 */
public class MainClass extends Thread {
    private static int port;
    private static Server server;

    public static void main(String[] args) throws Exception {

        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Error parsing port number. Using default port 8080");
                port = 8080;
            }
        } else {
            port = 8080;
        }
        new MainClass().start();
        System.err.println("Server starting at port " + port + "...");
        System.err.println("- To stop the server type <stop> or <s> -");
        try {
            server = new Server(port);
            server.start();
        } catch (BindException e) {
            System.err.println("Failed to start! Port " + port + " is busy");
            System.exit(1);
        }


    }

    //Listening to the console input for the command to stop the server
    @Override
    public void run() {
        for (; ; ) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                String request = null;
                try {
                    request = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (request != null) {
                    if (request.equalsIgnoreCase("stop") || request.equalsIgnoreCase("s")) {
                        System.err.println("Stopping the server...");
                        server.stop();
                        System.err.println("Stopped");
                        System.exit(0);
                    }
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
