package com.example.chaochin.bentleybooks;

/**
 * Created by chaochin on 2018/4/28.
 */
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.content.Intent;

public class Animation extends Activity {

    String email, phone, name, password;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.animation_layout);
        ImageView img = (ImageView) findViewById(R.id.simple_anim);
        img.setBackgroundResource(R.drawable.animation);

        AnimationRoutine1 task1 = new AnimationRoutine1();
        AnimationRoutine2 task2 = new AnimationRoutine2();

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        phone = intent.getStringExtra("phone");
        name = intent.getStringExtra("name");
        password = intent.getStringExtra("password");

        Timer t = new Timer();
        t.schedule(task1, 0);
        Timer t2 = new Timer();
        t2.schedule(task2, 4000);

    }

    class AnimationRoutine1 extends TimerTask {

        @Override
        public void run() {
            ImageView img = (ImageView) findViewById(R.id.simple_anim);
            AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
            frameAnimation.start();
        }
    }

    class AnimationRoutine2 extends TimerTask {

        @Override
        public void run() {
            ImageView img = (ImageView) findViewById(R.id.simple_anim);
            AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
            frameAnimation.stop();
            Intent intent1 = new Intent(Animation.this,Search_ISBN.class);
            intent1.putExtra("email",email);
            intent1.putExtra("phone",phone);
            intent1.putExtra("name", name);
            intent1.putExtra("password", password);
            startActivity(intent1);
            finish();

        }
    }
}
