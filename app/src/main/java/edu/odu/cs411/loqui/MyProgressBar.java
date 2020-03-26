package edu.odu.cs411.loqui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyProgressBar extends LinearLayout {
    float barWidth = 0.0f;
    float bufferWidth = 0.0f;
    float barHeight = 50.0f;
    List<MyCustomView> barArray = new ArrayList<MyCustomView>();
    boolean isBarDrwan = false;

    private android.os.Handler handler = new android.os.Handler();
    int completeColor;
    int waitingColor;
    int noOfSteps;
    public LinearLayout parent;
    Context context;
    LayoutInflater inflater;

    public MyProgressBar(Context context) {
        super(context);
    }

    public MyProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyProgressBar, 0, 0);
        try {
            this.completeColor = typedArray.getInt(R.styleable.MyProgressBar_completeColor, 0);
            this.waitingColor = typedArray.getInt(R.styleable.MyProgressBar_waitingColor, 0);
            this.noOfSteps = typedArray.getInteger(R.styleable.MyProgressBar_noOfSteps, 0);
        } finally {
            typedArray.recycle();
        }


        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.step_progress, this, true);
        parent = (LinearLayout) getChildAt(0);///*findViewById(R.id.ll_step_progress);*/
        addedBarToView();
    }

    public MyProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addedBarToView() {
        calculateBarWidth(noOfSteps);


    }

    private void intializeBarArray() {
        cleanAllBars();

        int i = 0;
        while (i < noOfSteps) {
            int xvalue = Math.round((barWidth * (i) + bufferWidth * (i + 1)));
            MyCustomView parralogramView = new MyCustomView(context, xvalue, 0, barWidth, barHeight, waitingColor);
            parent.addView(parralogramView);
            barArray.add(parralogramView);
            Collections.reverse(barArray);
            i = i + 1;
        }
    }

    private void cleanAllBars() {
        barArray.clear();
    }

    private void calculateBarWidth(final int noOfSteps) {
        parent.post(new Runnable() {

            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        float barSizeWithBuffer = parent.getWidth() / noOfSteps;
                        float bufferSize = (1 * barSizeWithBuffer / 32);
                        bufferWidth = 2 * bufferSize;
                        barWidth = barSizeWithBuffer - bufferWidth;
                        intializeBarArray();
                    }
                });
            }
        });

    }


    public void updateProgress(int step) {
        if (step >= -1 && step < noOfSteps) {
            int i = 0;
            while (i < noOfSteps) {
                MyCustomView k = barArray.get(i);
                if (i <= step) {
                    if (!k.isMarkComplete()) {
                        MyCustomView toggeledBar = new MyCustomView(context, 0, 0, k.getBarWidth(), k.getBarHeight(), completeColor);
                        toggeledBar.setMarkComplete(true);
                        barArray.set(i, toggeledBar);
                        parent.removeViewAt(i);
                        parent.addView(toggeledBar, i);
                    }
                } else {
                    if (k.isMarkComplete()) {
                        MyCustomView toggeledBar = new MyCustomView(context, 0, 0, k.getBarWidth(), k.getHeight(), waitingColor);
                        toggeledBar.setMarkComplete(false);
                        barArray.set(i, toggeledBar);
                        parent.removeViewAt(i);
                        parent.addView(toggeledBar, i);
                    }
                }
                i = i + 1;
            }
        }
    }

    public int getNoOfParts() {
        return noOfSteps;
    }
}
