 package com.example.ex4;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class JoyStickActivity extends AppCompatActivity {
    private JoyStickLogic joyStick;
    private final String AileronPath = "set controls/flight/aileron";
    private final String ElevatorPath = "set controls/flight/elevator";
    private static final String UP = "UP";
    private static final String RIGHT = "RIGHT";
    private static final String DOWN = "DOWN";
    private static final String LEFT = "LEFT";
    private RelativeLayout layout_joystick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joy_stick);

        layout_joystick = (RelativeLayout) findViewById(R.id.layout_joystick);
        createJoyStick();
        setListner();

    }

    public void createJoyStick(){

        joyStick = new JoyStickLogic(getApplicationContext(), layout_joystick, R.drawable.image_button);
        joyStick.initJoyStickPosition();
        joyStick.setOffset(110);
        joyStick.setMinimumDistance(50);
        joyStick.setLayoutAlpha(150);
        joyStick.setStickAlpha(100);
        joyStick.setStickSize(150, 150);
        joyStick.setLayoutSize(500, 500);

    }

    public void setListner(){
        layout_joystick.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                joyStick.drawStick(arg1);
                float xPosition = joyStick.getX();
                float yPosition = joyStick.getY();
                String direction = joyStick.getDirection();
                sendData(xPosition, yPosition, direction);
                return true;
            }
        });
    }

    public void sendData(float x, float y, String direction) {
        if (direction.equals(UP) || direction.equals(DOWN)) {
            TcpClient.Instance().sendMesssage(ElevatorPath + " " + String.valueOf(y));
        } else if (direction.equals(RIGHT) || direction.equals(LEFT)) {
            TcpClient.Instance().sendMesssage(AileronPath + " " + String.valueOf(x));
        }
    }


}
