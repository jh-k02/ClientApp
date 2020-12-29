package com.example.newchatting;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    TextView showText;
    Button connectBtn;
    Button disconnectBtn;
    Button Button_send;
    EditText ip_EditText;
    EditText port_EditText;
    EditText port_NameText;
    EditText editText_massage;
    Handler msghandler;

    SocketClient client;
    DisconnectThread disconnect;
    ReceiveThread receive;
    SendThread send;
    Socket socket;
    String nickname = "";
    LinkedList<SocketClient> threadList;

    Integer raspberryCount = 1;
    Integer cortexCount = 101;

    Boolean serverCheck = false;
    Boolean cortexCheck = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ip_EditText = (EditText) findViewById(R.id.ip_EditText);
        port_EditText = (EditText) findViewById(R.id.port_EditText);
        connectBtn = (Button) findViewById(R.id.connect_Button);
        disconnectBtn = (Button) findViewById(R.id.disconnect_Button);
        showText = (TextView) findViewById(R.id.showText_TextView);
        editText_massage = (EditText) findViewById(R.id.editText_massage);
        Button_send = (Button) findViewById(R.id.Button_send);
        port_NameText = (EditText) findViewById(R.id.port_NameText);
        threadList = new LinkedList<SocketClient>();

        msghandler = new Handler() {
            @Override
            public void handleMessage(Message hdmsg) {
                if (hdmsg.what == 1111) {
                    if (hdmsg.obj.toString().equals("[Cortex] has entered."))
                        cortexCheck = true;

                    if (hdmsg.obj.toString().equals("[Cortex] has left."))
                        cortexCheck = false;

                    showText.append(hdmsg.obj.toString() + "\n");
                }
            }
        };

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickname = port_NameText.getText().toString();
                client = new SocketClient(ip_EditText.getText().toString(), port_EditText.getText().toString());
                threadList.add(client);
                client.start();
            }
        });

        disconnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnect = new DisconnectThread(socket);
                threadList.clear();
                disconnect.start();
            }
        });

        Button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText_massage.getText().toString() != null) {
                    send = new SendThread(socket);
                    send.start();
                    editText_massage.setText("");
                }
            }
        });
    }

    class SocketClient extends Thread {
        boolean threadAlive;
        String ip;
        String port;
        String sendName = nickname;
        DataOutputStream output = null;
        public SocketClient(String ip, String port) {
            threadAlive = true;
            this.ip = ip;
            this.port = port;
        }

        @Override
        public void run() {
            try {
                socket = new Socket(ip, Integer.parseInt(port));
                output = new DataOutputStream(socket.getOutputStream());
                receive = new ReceiveThread(socket);
                if (output != null) {
                    if (sendName != null) {
                        output.write(sendName.getBytes());
                        serverCheck = true;
                    }
                }

                receive.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class DisconnectThread extends Thread {
        private Socket socket;

        public DisconnectThread(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                if (socket != null) {
                    socket.close();
                    msghandler.post(new Runnable() {
                        @Override
                        public void run() {
                            setToast("연결이 종료되었습니다.");
                            showText.setText("");
                            serverCheck = false;
                        }
                    });
                }
            } catch (Exception e) {
                final String recvInput = "연결을 끊는데 실패했습니다.";
                Log.d("Connect", e.getMessage());
                msghandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setToast(recvInput);
                    }
                });
            }
        }
    }

    class ReceiveThread extends Thread {
        private Socket sock = null;
        DataInputStream input;
        public ReceiveThread(Socket socket) {
            this.sock = socket;
            try {
                input = new DataInputStream(sock.getInputStream());
            } catch (Exception e) {

            }
        }

        @Override
        public void run() {
            try {
                while(input != null) {
                    String msg;
                    int count = input.available();
                    byte[] rcv = new byte[count];
                    input.read(rcv);
                    msg = new String(rcv);
                    if (count > 0) {
                        Log.d(ACTIVITY_SERVICE, "test:" + msg);
                        Message hdmsg = msghandler.obtainMessage();
                        hdmsg.what = 1111;
                        hdmsg.obj = msg;
                        msghandler.sendMessage(hdmsg);
                        Log.d(ACTIVITY_SERVICE, hdmsg.obj.toString());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class SendThread extends Thread {
        Socket socket;
        String sendtmp = editText_massage.getText().toString();
        String sendmsg = sendtmp;
        DataOutputStream output;

        public SendThread(Socket socket) {
            this.socket = socket;
            try {
                output = new DataOutputStream(socket.getOutputStream());
            } catch (Exception e) {

            }
        }

        public void run() {
            try {
                Log.d(ACTIVITY_SERVICE, "11111");
                if (output != null) {
                    if (sendmsg != null) {
                        output.write(sendmsg.getBytes());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        }
    }

    void setToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_Camera).setEnabled(serverCheck);
        menu.findItem(R.id.menu_Cortex).setEnabled(cortexCheck);
        menu.findItem(R.id.menu_Raspberry).setEnabled(serverCheck);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_Camera) {
            Intent intent = new Intent(this, CameraActivity.class);
            startActivity(intent);
        }

        if (id == R.id.menu_Cortex) {
            Intent intent = new Intent(this, CortexActivity.class);

            String ip = ip_EditText.getText().toString();
            String port = port_EditText.getText().toString();
            String name = nickname + cortexCount.toString();
            cortexCount++;

            intent.putExtra("ip", ip);
            intent.putExtra("port", port);
            intent.putExtra("nickname", name);

            startActivity(intent);
        }

        if (id == R.id.menu_Raspberry) {
            Intent intent = new Intent(this, RaspberryActivity.class);

            String ip = ip_EditText.getText().toString();
            String port = port_EditText.getText().toString();
            String name = nickname + raspberryCount.toString();
            raspberryCount++;

            intent.putExtra("ip", ip);
            intent.putExtra("port", port);
            intent.putExtra("nickname", name);

            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
