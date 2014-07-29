package com.winify.happy_hours.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.winify.happy_hours.R;
import com.winify.happy_hours.activities.ApplicationPreferences;
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

public class WeeklyStatisticFragment extends Fragment {
    private GraphicalView mChartView;
    private int weeklyMilliSeconds;
    private TrackerService service;
    private LinearLayout chartContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ServiceGateway serviceGateway = new ServiceGateway(this.getActivity());
        service = serviceGateway.getService();
        View rootView = inflater.inflate(R.layout.fragment_weekly, container, false);
        chartContainer = (LinearLayout) rootView.findViewById(
                R.id.chart_container_weekly);
        if (container == null) {
            return null;
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getStatistics();
    }

    private void displayChart() {

        final String[] status = new String[]{"Worked", "Left To work"};

        double[] distribution = {weeklyMilliSeconds, 201600000 - weeklyMilliSeconds};

        int[] colors = {Color.GREEN, Color.RED};

        CategorySeries distributionSeries = new CategorySeries(" week ");
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
        defaultRenderer.setChartTitle(" This week worked: " + convertTime(weeklyMilliSeconds));
        defaultRenderer.setChartTitleTextSize(20);
        defaultRenderer.setZoomButtonsVisible(true);
        defaultRenderer.setLabelsTextSize(20);
        mChartView = ChartFactory.getPieChartView(getActivity(),
                distributionSeries, defaultRenderer);
        chartContainer.addView(mChartView);
    }

    private void getStatistics() {
        ApplicationPreferences preferences = new ApplicationPreferences(this.getActivity());
        Token token = new Token(preferences.getKeyToken());
        service.getWorkedTime(token, new ServiceListener<Time>() {

            @Override
            public void success(Time time, Response response) {
                weeklyMilliSeconds = Integer.parseInt(time.getWeekly());
                displayChart();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
            }
        });
    }

    private String convertTime(int time) {
        int hour = (time / (1000 * 60 * 60));
        int min = ((time - (time / (1000 * 60 * 60))) / (1000 * 60)) % 60;
        return hour + ":" + min;
    }
}
