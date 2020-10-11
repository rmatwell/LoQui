package edu.odu.cs411.loqui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class MyCustomView extends View {

    private boolean isMarkComplete = false;
    private int colors = Color.parseColor("#FF8700");
    private final float cropedge = 10.0F;
    private Path rectEmptys;
    private int barWidth = 100;
    private int barHeight = 50;
    private Paint paint;

    public MyCustomView(Context context) {
        super(context);

    }

    public MyCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);


    }

    public MyCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyCustomView(Context context, float xvalue, int i, float barWidths, float barHeights, int barColor) {
        super(context);
        this.barWidth = Math.round(barWidths);
        this.barHeight = Math.round(barHeights);
        this.colors = barColor;

        this.setLayoutParams(new ViewGroup.LayoutParams(barWidth, barHeight));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.cropToParllalegram();
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.paint.setColor(this.colors);
        canvas.drawPath(this.rectEmptys, this.paint);
    }


    private void cropToParllalegram() {
        this.rectEmptys = new Path();
        rectEmptys.moveTo(cropedge, 0);
        rectEmptys.lineTo(0, barHeight);
        rectEmptys.lineTo(barWidth - cropedge, barHeight);
        rectEmptys.lineTo(barWidth, 0);
        rectEmptys.close();
    }

    public boolean isMarkComplete() {
        return isMarkComplete;
    }

    public void setMarkComplete(boolean markComplete) {
        isMarkComplete = markComplete;
    }

    public int getColors() {
        return colors;
    }

    public void setColors(int colors) {
        this.colors = colors;
    }

    public float getCropedge() {
        return cropedge;
    }

    public Path getRectEmptys() {
        return rectEmptys;
    }

    public void setRectEmptys(Path rectEmptys) {
        this.rectEmptys = rectEmptys;
    }

    public int getBarWidth() {
        return barWidth;
    }

    public void setBarWidth(int barWidth) {
        this.barWidth = barWidth;
    }

    public int getBarHeight() {
        return barHeight;
    }

    public void setBarHeight(int barHeight) {
        this.barHeight = barHeight;
    }
}
