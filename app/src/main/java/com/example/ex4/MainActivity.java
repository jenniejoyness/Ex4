package com.example.ex4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        TcpClient.Instance().disConnect();
    }

    public void connect(View view) {
        EditText ip = (EditText) findViewById(R.id.ip);
        final String ipStr =  ip.getText().toString();
        EditText port = (EditText) findViewById(R.id.port);
        final int portInt = Integer.parseInt(port.getText().toString());

        TcpClient.Instance().connectToServer(ipStr, portInt);
        Intent intent = new Intent(this, JoyStickActivity.class);
        startActivity(intent);

    }

}
