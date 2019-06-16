package com.example.ex4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements Serializable {

    TcpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void connect(View view) {
        EditText ip = (EditText) findViewById(R.id.ip);
        final String ipStr =  ip.getText().toString();
        EditText port = (EditText) findViewById(R.id.port);
        final int portInt = Integer.parseInt(port.getText().toString());

        TcpClient.Instance().connectToServer(ipStr, portInt);
        //TcpClient.Instance().sendMesssage("set controls/flight/rudder -1");

        Intent intent = new Intent(this, JoyStickActivity.class);
        //intent.putExtra("JoyStickActivity", client);
        startActivity(intent);

    }


}
