package com.winify.happy_hours.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.winify.happy_hours.R;
import com.winify.happy_hours.controller.ServiceGateway;
import com.winify.happy_hours.listeners.ServiceListener;
import com.winify.happy_hours.models.Time;
import com.winify.happy_hours.models.Token;
import com.winify.happy_hours.models.User;
import com.winify.happy_hours.service.TrackerService;
import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class StatisticsActivity extends Activity implements View.OnClickListener {
    private TextView monthly;
    private TextView weekly;
    private TextView daily;
    private int monthlyMiliSec;
    private int dailyMiliSec;
    private int weeklyMiliSec;
    private TrackerService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.statistics);
        super.onCreate(savedInstanceState);
        ServiceGateway serviceGateway = new ServiceGateway(StatisticsActivity.this);
        service = serviceGateway.getService();

        monthly = (TextView) findViewById(R.id.monthly_work);
        Button monthlyBtn = (Button) findViewById(R.id.monthlyBtn);
        monthlyBtn.setOnClickListener(this);
        weekly = (TextView) findViewById(R.id.weekly_work);
        Button weeklyBtn = (Button) findViewById(R.id.weeklyBtn);
        weeklyBtn.setOnClickListener(this);
        daily = (TextView) findViewById(R.id.daily_work);
        Button dailyBtn = (Button) findViewById(R.id.dailyBtn);
        dailyBtn.setOnClickListener(this);
        getStatistics();
    }

     private void getStatistics() {
        ApplicationPreferences preferences = new ApplicationPreferences(StatisticsActivity.this);
        Token token = new Token( preferences.getKeyToken());
        service.getWorkedTime(token, new ServiceListener<Time>() {

            @Override
            public void success(Time time, Response response) {
                dailyMiliSec = Integer.parseInt(time.getDaily());
                daily.setText(convertTime(time.getDaily()));

                weekly.setText(convertTime(time.getWeekly()));
                weeklyMiliSec = Integer.parseInt(time.getWeekly());

                monthly.setText(convertTime(time.getMonthly()));
                monthlyMiliSec = Integer.parseInt(time.getMonthly());
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
    }

    private String convertTime(String time) {

        int milliseconds = Integer.parseInt(time);
        int hour = (milliseconds / (1000 * 60 * 60));
        int min = ((milliseconds - (milliseconds / (1000 * 60 * 60))) / (1000 * 60)) % 60;
        return hour + ":" + min;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.dailyBtn: {
                CreatePieChart(dailyMiliSec, 28800000);
            }
            break;
            case R.id.weeklyBtn: {
                CreatePieChart(weeklyMiliSec, 144000000);
            }
            break;
            case R.id.monthlyBtn: {

                CreatePieChart(monthlyMiliSec, 576000000);
            }
            break;
        }
    }

    private void CreatePieChart(int workedTime, int totalTime) {

        if (workedTime > totalTime) {
            String[] code = new String[]{"OverTime Hours", "Hours Left To Work"};
            double[] distribution = {workedTime - totalTime, totalTime};

            int[] colors = {Color.YELLOW, Color.GREEN};

            CategorySeries distributionSeries = new CategorySeries("Work Time Chart");
            for (int i = 0; i < distribution.length; i++) {
                distributionSeries.add(code[i], distribution[i]);
            }
            DefaultRenderer defaultRenderer = new DefaultRenderer();
            for (int i = 0; i < distribution.length; i++) {
                SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
                seriesRenderer.setColor(colors[i]);
                seriesRenderer.setDisplayChartValues(true);
                defaultRenderer.addSeriesRenderer(seriesRenderer);
            }
            defaultRenderer.setLegendTextSize(30);
            defaultRenderer.setChartTitle("OverTime Work Chart");
            defaultRenderer.setChartTitleTextSize(20);
            defaultRenderer.setZoomButtonsVisible(true);
            defaultRenderer.setBackgroundColor(45454545);
            Intent intent = ChartFactory.getPieChartIntent(getBaseContext(),
                    distributionSeries, defaultRenderer, "PieChart");
            startActivity(intent);
        } else {
            String[] code = new String[]{"Worked Hours", "Hours Left To Work"};
            double[] distribution = {workedTime, totalTime};
            int[] colors = {Color.GREEN, Color.RED};
            CategorySeries distributionSeries = new CategorySeries("Work Time Chart");
            for (int i = 0; i < distribution.length; i++) {
                distributionSeries.add(code[i], distribution[i]);
            }
            DefaultRenderer defaultRenderer = new DefaultRenderer();
            for (int i = 0; i < distribution.length; i++) {
                SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
                seriesRenderer.setColor(colors[i]);
                seriesRenderer.setDisplayChartValues(true);
                defaultRenderer.addSeriesRenderer(seriesRenderer);
            }
            defaultRenderer.setLegendTextSize(30);
            defaultRenderer.setChartTitle("Work Time Chart");
            defaultRenderer.setChartTitleTextSize(20);
            defaultRenderer.setZoomButtonsVisible(true);
            defaultRenderer.setBackgroundColor(45454545);

            Intent intent = ChartFactory.getPieChartIntent(getBaseContext(),
                    distributionSeries, defaultRenderer, "PieChart");
            startActivity(intent);
        }
    }
}
