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

public class RaspberryActivity extends AppCompatActivity {

    TextView showText;
    Button Button_send;
    EditText editText_massage;
    Handler msghandler;

    Button greenBtn;
    Button yellowBtn;
    Button blueBtn;
    Button pinkBtn;

    Boolean greenLed = false;
    Boolean yellowLed = false;
    Boolean blueLed = false;
    Boolean pinkLed = false;

    Integer greenPwm = 10;
    Integer yellowPwm = 10;
    Integer bluePwm = 10;
    Integer pinkPwm = 10;

    Button greenUpBtn;
    Button greenDownBtn;
    Button yellowUpBtn;
    Button yellowDownBtn;
    Button blueUpBtn;
    Button blueDownBtn;
    Button pinkUpBtn;
    Button pinkDownBtn;

    Button setBtn;
    Button clearBtn;

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
        setContentView(R.layout.activity_raspberry);

        showText = (TextView) findViewById(R.id.showText_TextView);
        editText_massage = (EditText) findViewById(R.id.editText_massage);
        Button_send = (Button) findViewById(R.id.Button_send);
        threadList = new LinkedList<SocketClient>();

        greenBtn = (Button) findViewById(R.id.Green_Button);
        greenDownBtn = (Button) findViewById(R.id.Green_Down_Button);
        greenUpBtn = (Button) findViewById(R.id.Green_Up_Button);
        yellowBtn = (Button) findViewById(R.id.Yellow_Button);
        yellowDownBtn = (Button) findViewById(R.id.Yellow_Down_Button);
        yellowUpBtn = (Button) findViewById(R.id.Yellow_Up_Button);
        blueBtn = (Button) findViewById(R.id.Blue_Button);
        blueDownBtn = (Button) findViewById(R.id.Blue_Down_Button);
        blueUpBtn = (Button) findViewById(R.id.Blue_Up_Button);
        pinkBtn = (Button) findViewById(R.id.Pink_Button);
        pinkDownBtn = (Button) findViewById(R.id.Pink_Down_Button);
        pinkUpBtn = (Button) findViewById(R.id.Pink_Up_Button);

        setBtn = (Button) findViewById(R.id.GPIO_Set_Button);
        clearBtn = (Button) findViewById(R.id.GPIO_Clean_Button);

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

        greenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(greenLed) {
                    editText_massage.setText("GREEN LED OFF");
                    greenPwm = 10;
                    greenBtn.setText("GREEN LED ON");
                    greenLed = false;
                }

                else {
                    editText_massage.setText("GREEN LED ON");
                    greenBtn.setText("GREEN LED OFF");
                    greenLed = true;
                }

                send = new SendThread(socket);
                send.start();
                editText_massage.setText("");
            }
        });

        yellowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(yellowLed) {
                    editText_massage.setText("YELLOW LED OFF");
                    yellowPwm = 10;
                    yellowBtn.setText("YELLOW LED ON");
                    yellowLed = false;
                }

                else {
                    editText_massage.setText("YELLOW LED ON");
                    yellowBtn.setText("YELLOW LED OFF");
                    yellowLed = true;
                }

                send = new SendThread(socket);
                send.start();
                editText_massage.setText("");
            }
        });

        blueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(blueLed) {
                    editText_massage.setText("BLUE LED OFF");
                    bluePwm = 10;
                    blueBtn.setText("BLUE LED ON");
                    blueLed = false;
                }

                else {
                    editText_massage.setText("BLUE LED ON");
                    blueBtn.setText("BLUE LED OFF");
                    blueLed = true;
                }

                send = new SendThread(socket);
                send.start();
                editText_massage.setText("");
            }
        });

        pinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pinkLed) {
                    editText_massage.setText("PINK LED OFF");
                    pinkPwm = 10;
                    pinkBtn.setText("PINK LED ON");
                    pinkLed = false;
                }

                else {
                    editText_massage.setText("PINK LED ON");
                    pinkBtn.setText("PINK LED OFF");
                    pinkLed = true;
                }

                send = new SendThread(socket);
                send.start();
                editText_massage.setText("");
            }
        });


        greenUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(greenPwm < 90) {
                    greenPwm += 10;

                    editText_massage.setText("GREEN LED UP");

                    send = new SendThread(socket);
                    send.start();
                    editText_massage.setText("");
                }

                else {
                    setToast("더 이상 높일 수 없습니다.");
                }
            }
        });

        greenDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(greenPwm > 10) {
                    greenPwm -= 10;

                    editText_massage.setText("GREEN LED DOWN");

                    send = new SendThread(socket);
                    send.start();
                    editText_massage.setText("");
                }

                else {
                    setToast("더 이상 낮출 수 없습니다.");
                }
            }
        });

        yellowUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(yellowPwm < 90) {
                    yellowPwm += 10;

                    editText_massage.setText("YELLOW LED UP");

                    send = new SendThread(socket);
                    send.start();
                    editText_massage.setText("");
                }

                else {
                    setToast("더 이상 높일 수 없습니다.");
                }
            }
        });

        yellowDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(yellowPwm > 10) {
                    yellowPwm -= 10;

                    editText_massage.setText("YELLOW LED DOWN");

                    send = new SendThread(socket);
                    send.start();
                    editText_massage.setText("");
                }

                else {
                    setToast("더 이상 낮출 수 없습니다.");
                }
            }
        });

        blueUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bluePwm < 90) {
                    bluePwm += 10;

                    editText_massage.setText("BLUE LED UP");

                    send = new SendThread(socket);
                    send.start();
                    editText_massage.setText("");
                }

                else {
                    setToast("더 이상 높일 수 없습니다.");
                }
            }
        });

        blueDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bluePwm > 10) {
                    bluePwm -= 10;

                    editText_massage.setText("BLUE LED DOWN");

                    send = new SendThread(socket);
                    send.start();
                    editText_massage.setText("");
                }

                else {
                    setToast("더 이상 낮출 수 없습니다.");
                }
            }
        });

        pinkUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pinkPwm < 90) {
                    pinkPwm += 10;

                    editText_massage.setText("PINK LED UP");

                    send = new SendThread(socket);
                    send.start();
                    editText_massage.setText("");
                }

                else {
                    setToast("더 이상 높일 수 없습니다.");
                }
            }
        });

        pinkDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pinkPwm > 10) {
                    pinkPwm -= 10;

                    editText_massage.setText("PINK LED DOWN");

                    send = new SendThread(socket);
                    send.start();
                    editText_massage.setText("");
                }

                else {
                    setToast("더 이상 낮출 수 없습니다.");
                }
            }
        });

        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_massage.setText("GPIO Set");

                send = new SendThread(socket);
                send.start();
                editText_massage.setText("");
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_massage.setText("GPIO Clear");

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
