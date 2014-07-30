package com.winify.happy_hours.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.winify.happy_hours.R;
import com.winify.happy_hours.ApplicationPreferences;
import com.winify.happy_hours.activities.LogInActivity;
import com.winify.happy_hours.activities.MainActivity;
import com.winify.happy_hours.controller.ServiceGateway;
import com.winify.happy_hours.listeners.ServiceListener;
import com.winify.happy_hours.models.Time;
import com.winify.happy_hours.models.Token;
import com.winify.happy_hours.service.TrackerService;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.MimeUtil;
import retrofit.mime.TypedInput;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class StatisticFragment extends Fragment {
    private String range;
    private int rangeNumber;
    private int totalTime;
    private int timeWorked;
    private TrackerService service;
    private LinearLayout chartContainer;
    private int HoursHadToWork;
    private String errorMsg;

    public StatisticFragment(int totalTime, String range) {
        this.totalTime = totalTime;
        this.range = range;
    }

    public StatisticFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ServiceGateway serviceGateway = new ServiceGateway(this.getActivity());
        service = serviceGateway.getService();
        View rootView = inflater.inflate(R.layout.fragment_daily, container, false);
        chartContainer = (LinearLayout) rootView.findViewById(R.id.chart_container_daily);
        if (container == null) {
            return null;
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (range.equals("today")) {
            rangeNumber = 1;
        } else if (range.equals("this week")) {
            rangeNumber = 2;
        } else if (range.equals("this month")) {
            rangeNumber = 3;
        }
        getStatistics();
    }

    private void drawTimeChart() {
        final String[] status = new String[]{"Worked", "Left To work"};
        double[] distribution = {timeWorked, totalTime - timeWorked};
        int[] colors = {Color.GREEN, Color.RED};
        CategorySeries distributionSeries = new CategorySeries(" day ");
        for (int i = 0; i < distribution.length; i++) {
            distributionSeries.add(status[i], distribution[i]);
        }
        DefaultRenderer defaultRenderer = new DefaultRenderer();
        for (int i = 0; i < distribution.length; i++) {
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            seriesRenderer.setColor(colors[i]);
            seriesRenderer.setDisplayChartValues(true);
            defaultRenderer.addSeriesRenderer(seriesRenderer);
        }

        defaultRenderer.setChartTitle("I worked " + range + " : " + convertTime(timeWorked));
        defaultRenderer.setChartTitleTextSize(20);
        defaultRenderer.setZoomButtonsVisible(true);
        defaultRenderer.setLabelsTextSize(20);
        GraphicalView mChartView = ChartFactory.getPieChartView(getActivity(),
                distributionSeries, defaultRenderer);
        chartContainer.addView(mChartView);
    }

    private void drawOverTimeChart() {
        final String[] status = new String[]{"Worked OverTime", "Left To work"};
        double[] distribution = {timeWorked - totalTime, totalTime - (timeWorked - totalTime)};
        int[] colors = {Color.YELLOW, Color.GREEN};
        CategorySeries distributionSeries = new CategorySeries(range);
        for (int i = 0; i < distribution.length; i++) {
            distributionSeries.add(status[i], distribution[i]);
        }
        DefaultRenderer defaultRenderer = new DefaultRenderer();
        for (int i = 0; i < distribution.length; i++) {
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            seriesRenderer.setColor(colors[i]);
            seriesRenderer.setDisplayChartValues(true);
            defaultRenderer.addSeriesRenderer(seriesRenderer);
        }

        defaultRenderer.setChartTitle("I worked " + range + " : " + convertTime(timeWorked));
        defaultRenderer.setChartTitleTextSize(20);
        defaultRenderer.setZoomButtonsVisible(true);
        defaultRenderer.setLabelsTextSize(20);
        GraphicalView mChartView = ChartFactory.getPieChartView(getActivity(),
                distributionSeries, defaultRenderer);
        chartContainer.addView(mChartView);
    }

    private void getStatistics() {
        ApplicationPreferences preferences = new ApplicationPreferences(this.getActivity());
        Token token = new Token(preferences.getKeyToken());
        service.getWorkedTime(token, new ServiceListener<Time>() {

            @Override
            public void success(Time time, Response response) {
                HoursHadToWork = Integer.parseInt(time.getTimeToWork());
                switch (rangeNumber) {
                    case 1:
                        StatisticFragment.this.timeWorked = Integer.parseInt(time.getDaily());
                        createPieChart(timeWorked, totalTime);
                        break;
                    case 2:
                        StatisticFragment.this.timeWorked = Integer.parseInt(time.getWeekly());
                        createPieChart(timeWorked, totalTime);
                        break;
                    case 3:
                        StatisticFragment.this.timeWorked = Integer.parseInt(time.getMonthly());
                        totalTime = HoursHadToWork;
                        createPieChart(timeWorked, totalTime);
                        break;
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                if (getErrorMessage(retrofitError).equals("Error: cant find user with such token")) {

                    Intent intent = new Intent(getActivity(),LogInActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }


            }
        });
    }

    private void createPieChart(int time, int totalTime) {
        if (time > totalTime) {
            drawOverTimeChart();
        } else {
            drawTimeChart();
        }
    }

    private String convertTime(int time) {
        int hour = (time / (1000 * 60 * 60));
        int min = ((time - (time / (1000 * 60 * 60))) / (1000 * 60)) % 60;
        return hour + " h :" + min + " m";
    }
    private String getErrorMessage(RetrofitError retrofitError) {
        if (retrofitError.getResponse() != null) {
            TypedInput body = retrofitError.getResponse().getBody();
            byte[] bytes = new byte[0];
            try {
                bytes = streamToBytes(body.in());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String charset = MimeUtil.parseCharset(body.mimeType());
            try {
                errorMsg = new String(bytes, charset);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return errorMsg;
    }

    static byte[] streamToBytes(InputStream stream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (stream != null) {
            byte[] buf = new byte[1024];
            int r;
            while ((r = stream.read(buf)) != -1) {
                baos.write(buf, 0, r);
            }
        }
        return baos.toByteArray();
    }
}
