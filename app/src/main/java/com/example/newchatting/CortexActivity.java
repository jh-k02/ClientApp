package com.example.newchatting;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

public class CortexActivity extends AppCompatActivity {

    TextView showText;
    Button Button_send;
    EditText editText_massage;
    Handler msghandler;

    Button led1Btn;
    Button led2Btn;
    Button led3Btn;
    Button led4Btn;
    Button led5Btn;
    Button led6Btn;
    Button led7Btn;
    Button led8Btn;
    Button allOnBtn;
    Button allOffBtn;
    Button MotorOnBtn;
    Button MotorOffBtn;

    Boolean Led1 = false;
    Boolean Led2 = false;
    Boolean Led3 = false;
    Boolean Led4 = false;
    Boolean Led5 = false;
    Boolean Led6 = false;
    Boolean Led7 = false;
    Boolean Led8 = false;

    SocketClient client;
    ReceiveThread receive;
    SendThread send;
    Socket socket;
    String nickname = "";
    LinkedList<SocketClient> threadList;

    String ip;
    String port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cortex);

        showText = (TextView) findViewById(R.id.showText_TextView);
        editText_massage = (EditText) findViewById(R.id.editText_massage);
        Button_send = (Button) findViewById(R.id.Button_send);
        threadList = new LinkedList<SocketClient>();

        led1Btn = (Button) findViewById(R.id.LED1_Button);
        led2Btn = (Button) findViewById(R.id.LED2_Button);
        led3Btn = (Button) findViewById(R.id.LED3_Button);
        led4Btn = (Button) findViewById(R.id.LED4_Button);
        led5Btn = (Button) findViewById(R.id.LED5_Button);
        led6Btn = (Button) findViewById(R.id.LED6_Button);
        led7Btn = (Button) findViewById(R.id.LED7_Button);
        led8Btn = (Button) findViewById(R.id.LED8_Button);
        allOnBtn = (Button) findViewById(R.id.LEDALL_ON_Button);
        allOffBtn = (Button) findViewById(R.id.LEDALL_OFF_Button);
        MotorOnBtn = (Button) findViewById(R.id.Motor_ON_Button);
        MotorOffBtn = (Button) findViewById(R.id.Motor_OFF_Button);

        Intent intent = getIntent();

        ip = intent.getStringExtra("ip");
        port = intent.getStringExtra("port");
        nickname = intent.getStringExtra("nickname");

        client = new SocketClient(ip, port);
        threadList.add(client);
        client.start();

        msghandler = new Handler() {
            @Override
            public void handleMessage(Message hdmsg) {
                showText.append(hdmsg.obj.toString() + "\n");
            }
        };

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

        led1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Led1) {
                    editText_massage.setText("<L10>");
                    led1Btn.setText("LED1 ON");
                    Led1 = false;
                }

                else {
                    editText_massage.setText("<L11>");
                    led1Btn.setText("LED1 OFF");
                    Led1 = true;
                }

                send = new SendThread(socket);
                send.start();
                editText_massage.setText("");
            }
        });

        led2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Led2) {
                    editText_massage.setText("<L20>");
                    led2Btn.setText("LED2 ON");
                    Led2 = false;
                }

                else {
                    editText_massage.setText("<L21>");
                    led2Btn.setText("LED2 OFF");
                    Led2 = true;
                }

                send = new SendThread(socket);
                send.start();
                editText_massage.setText("");
            }
        });

        led3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Led3) {
                    editText_massage.setText("<L30>");
                    led3Btn.setText("LED3 ON");
                    Led3 = false;
                }

                else {
                    editText_massage.setText("<L31>");
                    led3Btn.setText("LED3 OFF");
                    Led3 = true;
                }

                send = new SendThread(socket);
                send.start();
                editText_massage.setText("");
            }
        });

        led4Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Led4) {
                    editText_massage.setText("<L40>");
                    led4Btn.setText("LED4 ON");
                    Led4 = false;
                }

                else {
                    editText_massage.setText("<L41>");
                    led4Btn.setText("LED4 OFF");
                    Led4 = true;
                }

                send = new SendThread(socket);
                send.start();
                editText_massage.setText("");
            }
        });

        led5Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Led5) {
                    editText_massage.setText("<L50>");
                    led5Btn.setText("LED5 ON");
                    Led5 = false;
                }

                else {
                    editText_massage.setText("<L51>");
                    led5Btn.setText("LED5 OFF");
                    Led5 = true;
                }

                send = new SendThread(socket);
                send.start();
                editText_massage.setText("");
            }
        });

        led6Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Led6) {
                    editText_massage.setText("<L60>");
                    led6Btn.setText("LED6 ON");
                    Led6 = false;
                }

                else {
                    editText_massage.setText("<L61>");
                    led6Btn.setText("LED6 OFF");
                    Led6 = true;
                }

                send = new SendThread(socket);
                send.start();
                editText_massage.setText("");
            }
        });

        led7Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Led7) {
                    editText_massage.setText("<L70>");
                    led7Btn.setText("LED7 ON");
                    Led7 = false;
                }

                else {
                    editText_massage.setText("<L71>");
                    led7Btn.setText("LED7 OFF");
                    Led7 = true;
                }

                send = new SendThread(socket);
                send.start();
                editText_massage.setText("");
            }
        });

        led8Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Led8) {
                    editText_massage.setText("<L80>");
                    led8Btn.setText("LED8 ON");
                    Led8 = false;
                }

                else {
                    editText_massage.setText("<L81>");
                    led8Btn.setText("LED8 OFF");
                    Led8 = true;
                }

                send = new SendThread(socket);
                send.start();
                editText_massage.setText("");
            }
        });

        allOnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_massage.setText("<LA1>");

                send = new SendThread(socket);
                send.start();
                editText_massage.setText("");
            }
        });

        allOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_massage.setText("<LA0>");

                send = new SendThread(socket);
                send.start();
                editText_massage.setText("");
            }
        });

        MotorOnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_massage.setText("<M1>");

                send = new SendThread(socket);
                send.start();
                editText_massage.setText("");
            }
        });

        MotorOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_massage.setText("<M0>");

                send = new SendThread(socket);
                send.start();
                editText_massage.setText("");
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
                    }
                }

                receive.start();
            } catch (IOException e) {
                e.printStackTrace();
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
}
