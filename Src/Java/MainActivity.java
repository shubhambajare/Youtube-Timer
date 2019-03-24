package com.example.clash.youtube1;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    WebView web;
    Button set,off;
    EditText time;
    CountDownTimer c;
    TextView result;
    int a=0;
    boolean flag=true;

    PowerManager pm;
    PowerManager.WakeLock wakeLock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pm=(PowerManager)this.getSystemService(Context.POWER_SERVICE);
        wakeLock=pm.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK,"tag");

        set=(Button)findViewById(R.id.set);
        off=(Button)findViewById(R.id.screenoff);
        time=(EditText)findViewById(R.id.time);
        result=(TextView)findViewById(R.id.time1);

        off.setText("turn on proximity");

        web=(WebView)findViewById(R.id.web);
        web.setWebViewClient(new WebViewClient());
        web.loadUrl("http://m.youtube.com");
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setPluginState(WebSettings.PluginState.ON);


        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(flag==true){
                    wakeLock.acquire();
                    off.setText("turn off proximity");
                    flag=false;
                }else{
                    wakeLock.release();
                    off.setText("turn on proximity");
                    flag=true;
                }
            }
        });

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int temp=Integer.parseInt(time.getText().toString())*60*1000;
                a=temp/1000;
                c=new CountDownTimer(temp,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        result.setText(a+" sec remaining");
                        a--;
                    }

                    @Override
                    public void onFinish() {
                        Toast.makeText(MainActivity.this,"finish",Toast.LENGTH_LONG).show();
                        wakeLock.release();
                        finish();
                    }
                }.start();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (web.canGoBack()) {
            web.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
