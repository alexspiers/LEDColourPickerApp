package com.alexspiers.ledcolourpicker;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient implements Runnable {

    private final GradientView gradientView;
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    InetAddress serverAddr;

    public int ONPAUSE= 0;
    public int ONRESUME = 1;

    public SocketClient(GradientView gradientView) {
        this.gradientView = gradientView;
        try {
            this.serverAddr = InetAddress.getByName("192.168.0.101");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            socket = new Socket(serverAddr, 25522);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeString(String string){
        try {
            if(dos == null)
                return;

            this.dos.writeUTF(string);
            this.dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
