package com.example.peredachapaketovandreygritsakovichgr493;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import  java.net.DatagramSocket;
import java.net.DatagramPacket;
import  java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Date;


public class MainActivity extends AppCompatActivity
{
    TextView tx_ipaddres;
    TextView tx_time;
    TextView tx_message;

     byte[] send_buffer=new byte[350];
     byte[] receive_buffer=new byte[350];
     DatagramSocket socket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tx_message=findViewById(R.id.tx_message);
        tx_ipaddres=findViewById(R.id.tx_address);
        tx_time=findViewById(R.id.txt_time);

        try {
            InetAddress local_network= InetAddress.getByName("0.0.0.0");
            SocketAddress local_address= new InetSocketAddress(local_network,9000);
            socket = new DatagramSocket(null);
            socket.bind(local_address);
            socket.setBroadcast(true); //
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }

        Runnable receiver=new Runnable() {
            @Override
            public void run()
            {
                Log.e("Test","Received 1");
                DatagramPacket received_packet= new DatagramPacket(receive_buffer,receive_buffer.length);
                while(true)
                {
                    try {
                        socket.receive(received_packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String s = new String (receive_buffer,0,received_packet.getLength()); //
                    String adress = received_packet.getAddress().toString();
                    Date date =new Date();

                    tx_ipaddres.setText("IP Адрес:" + adress.toString());
                    tx_time.setText("Время:" + date.toString());
                    tx_message.setText("Cообщение:" + String.valueOf(s));


                }
            }
        };

        Thread receiving_thread= new Thread(receiver);
        receiving_thread.start();
    }

    DatagramPacket send_packet;

    public void Clear_OnClick(View v)
    {
        tx_ipaddres.setText("IP Адрес:");
        tx_time.setText("Время:");
        tx_message.setText("Сообщение:");
    }
    public void Onclick(View v)
    {
        EditText ta=findViewById(R.id.txt_address);
        String ip=ta.getText().toString();

        EditText tp=findViewById(R.id.txt_port);
        int port= Integer.parseInt(tp.getText().toString());

        EditText ed_message=findViewById(R.id.edit_message);
        String message=ed_message.getText().toString();

        try
        {
            InetAddress remote_address= InetAddress.getByName(ip);
            send_packet = new DatagramPacket(send_buffer,send_buffer.length,remote_address,port);
        }
        catch ( UnknownHostException e)
        {
            e.printStackTrace();
        }

        //СООБЩЕНИЕ

        send_buffer=message.getBytes();


        Runnable r=new Runnable()
        {
            @Override
            public void run()
            {
                Log.e("Test","Sending");
                try {
                    socket.send(send_packet);
                    send_packet.getAddress().getHostAddress();
                    Log.e("Test","Sending");
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        };

        Thread sending_thread= new Thread(r);
        sending_thread.start();

    }
    public void Visible_OnClick(View v) throws InterruptedException {

        Button send_but= findViewById(R.id.button_option);
        View l1 =findViewById(R.id.ly_text_ip);
        View l2 =findViewById(R.id.ly_buttons);
        View l3 =findViewById(R.id.ly_text_port);
        View l4 = findViewById(R.id.ly_message);

        if (l1.getVisibility()==View.VISIBLE)
        {
            send_but.setText("Открыть найстройки");
            l1.setVisibility(View.GONE);
            l2.setVisibility(View.GONE);
            l3.setVisibility(View.GONE);
            l4.setVisibility(View.GONE);

        }
        else
        {
            send_but.setText("Закрыть найстройки");
            l1.setVisibility(View.VISIBLE);
            l2.setVisibility(View.VISIBLE);
            l3.setVisibility(View.VISIBLE);
            l4.setVisibility(View.VISIBLE);
        }
    }
}