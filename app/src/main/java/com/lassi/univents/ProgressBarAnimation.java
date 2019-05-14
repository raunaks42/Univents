package com.lassi.univents;

import android.content.Context;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;

public class ProgressBarAnimation extends Animation {

    private Context context;
    private ProgressBar progressBar;
    private float start;
    private float end;

    public ProgressBarAnimation(Context context, ProgressBar progressBar, float start, float end){
        this.context = context;
        this.progressBar = progressBar;
        this.start = start;
        this.end = end;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

        float value = start + (end - start) * interpolatedTime;
        progressBar.setProgress((int)value);

        if(value == end){
            context.startActivity(new Intent(context , HomeScreen.class));
        }
    }
}


