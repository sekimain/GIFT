package com.example.mypar.gift.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.mypar.gift.R;

public class Splash_Activity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(),3000);

    }
    private class splashhandler implements Runnable{
        public void run(){
            startActivity(new Intent(getApplication(),Login_Activity.class));
            Splash_Activity.this.finish();
        }
    }

    @Override
    public void onBackPressed(){

    }
}
