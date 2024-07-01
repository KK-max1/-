package com.example.food.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.example.food.R;

public class DHActivity  extends AppCompatActivity {
    private int screenWidth;
    private ImageView iv_cg,iv_hg;
    private AnimationDrawable animation;//逐帧动画对象
    private AnimatorSet flyAnimatorSet;//动画组合类，此类所有动画进行的组合并运行
    private ObjectAnimator objectAnimator;//给对象添加动画效果
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dhactivity);
        init();
    }
    private void init(){
        getWindowWidth();
        iv_cg=findViewById(R.id.iv_cg);
        iv_hg=findViewById(R.id.iv_hg);
        flyAnimation(1);
        flyAnimation(2);
    }
    private void getWindowWidth(){
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
    }
    private void flyAnimation(int flag){
        flyAnimatorSet=new AnimatorSet();
        if(flag==1){
            animation= (AnimationDrawable) iv_cg.getBackground();
            objectAnimator = objectAnimator.ofFloat(iv_cg,"rotationX",0,359 );
            objectAnimator.setDuration(3*1000);
        }else if (flag==2){
            animation= (AnimationDrawable) iv_hg.getBackground();
            objectAnimator = objectAnimator.ofFloat(iv_hg,"rotationX",0,359);
            objectAnimator.setRepeatCount(Animation.RESTART);
            objectAnimator.setRepeatCount(Animation.INFINITE);
            objectAnimator.setDuration(10*1000);
        }
        objectAnimator.setInterpolator(new LinearInterpolator());
        flyAnimatorSet.play(objectAnimator);
        animation.start();
        flyAnimatorSet.start();
    }
}