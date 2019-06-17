package com.example.ex4;

import android.content.Context;
import android.view.ViewGroup;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class JoyStickLogic {

    private static final String STICK_NONE = "NONE";
    private static final String UP = "UP";
    private static final String RIGHT = "RIGHT";
    private static final String DOWN = "DOWN";
    private static final String LEFT = "LEFT";

    private float position_x = 0, position_y = 0, distance = 0, angle = 0;
    private int min_distance = 0;

    private int STICK_ALPHA = 200;
    private int LAYOUT_ALPHA = 200;
    private int OFFSET = 0;

    private Context mContext;
    private ViewGroup mLayout;
    private LayoutParams params;
    private int stick_width, stick_height;

    private DrawCanvas draw;
    private Paint paint;
    private Bitmap stick;

    private boolean touch_state = true;

    public JoyStickLogic(Context context, ViewGroup layout, int stick_res_id) {
        mContext = context;

        stick = BitmapFactory.decodeResource(mContext.getResources(), stick_res_id);

        stick_width = stick.getWidth();
        stick_height = stick.getHeight();

        draw = new DrawCanvas(mContext);
        paint = new Paint();
        mLayout = layout;
        params = mLayout.getLayoutParams();
    }

    public void initJoyStickPosition() {
        draw.position(params.width / 2, params.height / 2);
        draw();
    }

    public void drawStick(MotionEvent motionEvent) {
        
        position_y = (int) (motionEvent.getY() - (params.height / 2));
        position_x = (int) (motionEvent.getX() - (params.width / 2));
        distance = (float) Math.sqrt(Math.pow(position_x, 2) + Math.pow(position_y, 2));
        
        angle = (float) cal_angle(position_x, position_y);
        
         if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            draw.position(params.width / 2, params.height / 2);
            draw();
        }
        else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            if (distance <= (params.width / 2) - OFFSET) {
                draw.position(motionEvent.getX(), motionEvent.getY());
                draw();
                touch_state = true;
            }
        }else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE && touch_state) {
            if (distance <= (params.width / 2) - OFFSET) {
                draw.position(motionEvent.getX(), motionEvent.getY());
                draw();
            } else if (distance > (params.width / 2) - OFFSET) {
                float x = (float) (Math.cos(Math.toRadians(cal_angle(position_x, position_y)))
                        * ((params.width / 2) - OFFSET));
                float y = (float) (Math.sin(Math.toRadians(cal_angle(position_x, position_y)))
                        * ((params.height / 2) - OFFSET));
                x += (params.width / 2);
                y += (params.height / 2);
                draw.position(x, y);
                draw();
            } else {
                mLayout.removeView(draw);
            }
        } 
    }

    public float normalization(float x) {
        float i = x + 200;
        float normal = i / 400;
        normal = (normal * 2) - 1;
        return normal;
    }

    public float getX() {
        if (distance > min_distance && touch_state) {
            if (position_x > 200)
                return 1;
            if (position_x < -200)
                return -1;
            return normalization(position_x);
        }
        return 0;
    }

    public float getY() {
        if (distance > min_distance && touch_state) {
            if (position_y > 200)
                return -1;
            if (position_y < -200)
                return +1;
            return -(normalization(position_y));
        }
        return 0;
    }

    public void setMinimumDistance(int minDistance) {
        min_distance = minDistance;
    }

    public String getDirection() {
        if (touch_state && distance > min_distance) {
            if (angle >= 225 && angle < 315) {
                return UP;
            } else if (angle >= 315 || angle < 45) {
                return RIGHT;
            } else if (angle >= 45 && angle < 135) {
                return DOWN;
            } else if (angle >= 135 && angle < 225) {
                return LEFT;
            }
        } else if (distance <= min_distance && touch_state) {
            return STICK_NONE;
        }
        return DOWN;
    }

    public void setOffset(int offset) {
        OFFSET = offset;
    }

    public void setStickAlpha(int alpha) {
        STICK_ALPHA = alpha;
        paint.setAlpha(alpha);
    }

    public void setLayoutAlpha(int alpha) {
        LAYOUT_ALPHA = alpha;
        mLayout.getBackground().setAlpha(alpha);
    }

    public void setStickSize(int width, int height) {
        stick = Bitmap.createScaledBitmap(stick, width, height, false);
        stick_width = stick.getWidth();
        stick_height = stick.getHeight();
    }

    public void setLayoutSize(int width, int height) {
        params.width = width;
        params.height = height;
    }

    private double cal_angle(float x, float y) {
        if (x >= 0 && y >= 0)
            return Math.toDegrees(Math.atan(y / x));
        else if (x < 0 && y >= 0)
            return Math.toDegrees(Math.atan(y / x)) + 180;
        else if (x < 0 && y < 0)
            return Math.toDegrees(Math.atan(y / x)) + 180;
        else if (x >= 0 && y < 0)
            return Math.toDegrees(Math.atan(y / x)) + 360;
        return 0;
    }

    private void draw() {
        try {
            mLayout.removeView(draw);
        } catch (Exception e) {
        }
        mLayout.addView(draw);
    }

    private class DrawCanvas extends View {
        float x, y;

        private DrawCanvas(Context mContext) {
            super(mContext);
        }

        public void onDraw(Canvas canvas) {
            canvas.drawBitmap(stick, x, y, paint);
        }

        private void position(float pos_x, float pos_y) {
            x = pos_x - (stick_width / 2);
            y = pos_y - (stick_height / 2);

        }
    }
}