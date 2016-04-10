package com.example.hyejung.easysubway;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


/**
 * Created by Seon on 2016-03-26.
 */

public class SplashActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler hd = new Handler();
        hd.postDelayed(new splashhandler() , 3000); // 3초 후에 hd Handler 실행
    }

        private class splashhandler implements Runnable{
            public void run() {
                /*if(visitNum > 0) {
                    info.setUserOption(userOption);
                    Intent it = new Intent(this, MainActivity.class);
                    startActivity(it);
                }
                else {
                    Intent it = new Intent(this, SettingPage.class);
                    startActivity(it);
                }*/
                startActivity(new Intent(getApplication(), StartActivity.class)); // 로딩이 끝난후 이동할 Activity
//                finish();
                SplashActivity.this.finish(); // 로딩페이지 Activity Stack에서 제거
            }
        }
}

