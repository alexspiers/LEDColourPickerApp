package com.alexspiers.ledcolourpicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private GradientView mTop;
    private GradientView mBottom;
    private TextView mTextView;
    private Drawable mIcon;

    private Thread socketThread;
    private SocketClient socketClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.activity_main, null);
        setContentView(view);

        mIcon = getResources().getDrawable(R.mipmap.ic_launcher);
        mTextView = (TextView)findViewById(R.id.color);
        mTextView.setCompoundDrawablesWithIntrinsicBounds(mIcon, null, null, null);
        mTop = (GradientView)findViewById(R.id.top);
        mBottom = (GradientView) findViewById(R.id.bottom);
        mTop.setBrightnessGradientView(mBottom);

        mBottom.setOnColorChangedListener(new GradientView.OnColorChangedListener() {
            @Override
            public void onColorChanged(GradientView view, int color) {
                mTextView.setTextColor(color);
                mTextView.setText("#" + Integer.toHexString(color));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mIcon.setTint(color);
                }
                if (socketClient == null)
                    return;

                socketClient.writeString(String.valueOf(color));
            }
        });

        int color = 0xFF394572;
        mTop.setColor(color);

        socketClient = new SocketClient(mBottom);
        socketThread = new Thread(socketClient);
        socketThread.start();

        setTitle("Pick a Color");

    }

    @Override
    public void onPause(){
        super.onPause();
        socketClient = null;
    }

    @Override
    public void onStop(){
        super.onStop();
        socketClient = null;
    }

    @Override
    public void onResume(){
        super.onResume();
        socketClient = new SocketClient(mBottom);
        socketThread = new Thread(socketClient);
        socketThread.start();
    }

    @Override
    public void onRestart(){
        super.onRestart();
        socketClient = new SocketClient(mBottom);
        socketThread = new Thread(socketClient);
        socketThread.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            socketClient.writeString("smooth");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
