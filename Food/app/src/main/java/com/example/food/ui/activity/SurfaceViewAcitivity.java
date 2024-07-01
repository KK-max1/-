package com.example.food.ui.activity;

import android.content.ContentResolver;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food.R;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class SurfaceViewAcitivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener,SurfaceHolder.Callback {
    private SurfaceView sv;//SurfaceView控件播放视频
    private SurfaceHolder holder;//SurfaceView控件的管理器
    private MediaPlayer mediaplayer;//多媒体播放器
    private RelativeLayout rl;//相对布局管理器
    private Timer timer;//计时器
    private TimerTask task;//时间任务器
    private SeekBar sbar;//进度条控件
    private ImageView play;//播放视图
    private Surface mMainViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_surface_view_acitivity);
        sv=findViewById(R.id.sv);
        holder=sv.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(this);
        rl=findViewById(R.id.rl);
        play=findViewById(R.id.play);
        sbar=findViewById(R.id.sbar);
        sbar.setOnSeekBarChangeListener(this);
        timer=new Timer();
        task=new TimerTask() {
            @Override
            public void run() {
                if (mediaplayer!=null && mediaplayer.isPlaying()){
                    int total=mediaplayer.getDuration();
                    sbar.setMax(total);
                    int progress=mediaplayer.getCurrentPosition();
                    sbar.setProgress(progress);
                }else {
                    play.setImageResource(android.R.drawable.ic_media_play);
                }
            }
        };
        timer.schedule(task,500,500);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        try {
            mediaplayer=new MediaPlayer();
            mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            Uri uri= Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://"+getPackageName()+"/"+ R.raw.szrp);
            try {
                mediaplayer.setDataSource(SurfaceViewAcitivity.this,uri);
            }catch (IOException ioException){
                Toast.makeText(SurfaceViewAcitivity.this,"播放失败",Toast.LENGTH_SHORT).show();
                ioException.printStackTrace();
            }
            mediaplayer.setDisplay(holder);
            mediaplayer.prepareAsync();
            mediaplayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        if (mediaplayer.isPlaying()){
            mediaplayer.stop();
        }
    }
    public void click(View view){
        if(mediaplayer!=null && mediaplayer.isPlaying()){
            mediaplayer.pause();
            play.setImageResource(android.R.drawable.ic_media_play);
        }else{
            mediaplayer.start();
            play.setImageResource(android.R.drawable.ic_media_pause);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {


    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int position=seekBar.getProgress();
        if (mediaplayer!=null){
            mediaplayer.seekTo(position);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (rl.getVisibility() == View.INVISIBLE) {
                rl.setVisibility(View.VISIBLE);
                CountDownTimer countDownTimer = new CountDownTimer(3000, 1000) {
                    @Override
                    public void onTick(long l) {
                        System.out.println(l);
                    }

                    @Override
                    public void onFinish() {
                        rl.setVisibility(View.INVISIBLE);
                    }
                };
                countDownTimer.start();
            } else if (rl.getVisibility() == View.VISIBLE) {
                rl.setVisibility(View.INVISIBLE);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        task.cancel();
        timer.cancel();
        timer=null;
        task=null;
        mediaplayer.release();
        mediaplayer=null;
        super.onDestroy();
    }
}