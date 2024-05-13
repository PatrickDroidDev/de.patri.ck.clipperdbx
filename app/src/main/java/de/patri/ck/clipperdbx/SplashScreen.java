package de.patri.ck.clipperdbx;

import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.content_splash);

    ImageView splashImage = findViewById(R.id.splashImageView);
    Animation fadeIn  = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.long_fade_in);

    fadeIn.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) { }

      @Override
      public void onAnimationEnd(Animation animation) {
        Intent app = new Intent(SplashScreen.this, Main.class);
        startActivity(app);
        finishAffinity();
       }
       @Override
       public void onAnimationRepeat(Animation animation) { }
    });
    splashImage.startAnimation(fadeIn);
  }

}