package fia.ues.saluddigital.RutinaEjercicios;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import fia.ues.saluddigital.R;
import android.widget.MediaController;
import android.widget.VideoView;
public class EjercicioVideo extends AppCompatActivity{

    VideoView video;
    MediaController mediaController;
    int position =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicios);
        video=(VideoView) findViewById(R.id.video);

        String path = "android.resource://"+getResources()+ "/" + R.raw.rutina;
        video.setVideoURI(Uri.parse(path));
        video.start();

        if(this.mediaController==null){
            this.mediaController = new MediaController(EjercicioVideo.this);
            this.mediaController.setAnchorView(video);
            this.video.setMediaController(mediaController);

        }
        this.video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                video.seekTo(position);
                if(position==0){
                    video.start();
                }
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        mediaController.setAnchorView(video);
                    }
                });
            }
        });
    }
}
