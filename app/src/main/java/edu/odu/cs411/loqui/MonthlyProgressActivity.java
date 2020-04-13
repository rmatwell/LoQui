package edu.odu.cs411.loqui;

import lecho.lib.hellocharts.view.LineChartView;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;

public class MonthlyProgressActivity extends AppCompatActivity
{
    LineChartView monthlyChart;
    String[] axisData = {"3", "6", "9", "12", "15", "18", "21", "24", "27", "30"};
    ArrayList<Double> yAxisData;
    String TAG = "MonthlyProgressActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthlyprogress);

        monthlyChart = findViewById(R.id.chart);

        List dayValues = new ArrayList();
        List scoreValues = new ArrayList();

        Line line = new Line(dayValues).setColor(Color.parseColor("#9C27B0"));

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        FirestoreWorker dbWorker = new FirestoreWorker();
        MonthlyReport newReport = new MonthlyReport();
        dbWorker.getMonthlyScores(newReport, cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));

        new CountDownTimer(2500, 1000) {
            public void onFinish() {

                int j = 0;
                double numCorrect = 0;
                double numTotal = 0;
                double dayScore = 0.0;

                yAxisData = new ArrayList<>();

                for (int i = 0; i < newReport.monthlyReport.size(); i++)
                {
                    j++;

                    Log.d(TAG, "j = " + j + " and i = " + i);

                    for (int k = 0; k < newReport.monthlyReport.get(i).dailyList.size(); k++)
                    {
                        numTotal++;

                        Log.d(TAG, "numTotal = " + numTotal);

                        if (newReport.monthlyReport.get(i).dailyList.get(k).getEmotionScore() == 1)
                        {
                            numCorrect++;

                            Log.d(TAG, "numCorrect = " + numCorrect);
                        }
                    }

                    if (j % 3 == 0 && j != 0)
                    {
                        Log.d(TAG, "Performing Calculation");
                        Log.d(TAG, "numTotal = " + numTotal);
                        Log.d(TAG, "numCorrect = " + numCorrect);
                        if (numTotal != 0)
                        {
                            Log.d(TAG, "Inside if numTotal doesnt = 0");
                            dayScore = numCorrect / numTotal;
                            Log.d(TAG, "dayScore = " + dayScore);
                        }

                        Log.d(TAG, "dayScore = " + dayScore);
                        yAxisData.add(dayScore);
                        numCorrect = 0;
                        numTotal = 0;
                        dayScore = 0;
                    }
                }

                for (int i = 0; i < axisData.length; i++)
                {
                    scoreValues.add(i, new AxisValue(i).setLabel(axisData[i]));
                }

                for (int i = 0; i < yAxisData.size(); i++)
                {
                    dayValues.add(new PointValue(i, (int)(yAxisData.get(i) * 100)));
                }

                List lines = new ArrayList();
                lines.add(line);

                LineChartData data = new LineChartData();
                data.setLines(lines);

                Axis axis = new Axis();
                axis.setValues(scoreValues);
                axis.setTextSize(16);
                axis.setTextColor(Color.parseColor("#03A9F4"));
                data.setAxisXBottom(axis);

                Axis yAxis = new Axis();
                yAxis.setName("Percentage Correct");
                yAxis.setTextColor(Color.parseColor("#03A9F4"));
                yAxis.setTextSize(16);
                data.setAxisYLeft(yAxis);

                monthlyChart.setLineChartData(data);
                Viewport viewport = new Viewport(monthlyChart.getMaximumViewport());
                viewport.top = 100;
                monthlyChart.setMaximumViewport(viewport);
                monthlyChart.setCurrentViewport(viewport);
            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();
    }
}
