package com.app.client;

import com.app.DTO.UserDTO;

import java.io.*;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;

public class ClientSocket {
    private static ClientSocket instance;
    private static Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private UserDTO user;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public ClientSocket() {
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public boolean connect() throws InterruptedException, IOException, ClassNotFoundException {
        boolean isConnect = false;
        int trying = 0;
        while (!isConnect && trying < 1) {
            try {
                socket = new Socket("localhost", 1234);
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                isConnect = true;
            } catch (IOException e) {
                trying++;
                Thread.sleep(1000);
            }
        }

        return isConnect;
    }



    public void close() throws IOException {
        send(new Request(Request.RequestType.Exit, ""));
        socket.close();
    }

    public void send(Request request) throws IOException {
        String data = (new JSON<Request>().toJson(request));
        out.writeUTF(data);
    }

    public Request get() throws IOException {
        String data = in.readUTF();
        return (new JSON<Request>()).fromJson(data,Request.class);
    }

    public static ClientSocket getInstance() {
        if (instance == null) {
            instance = new ClientSocket();
        }
        return instance;
    }
}
