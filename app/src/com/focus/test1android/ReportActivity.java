package com.focus.test1android;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ReportActivity extends Activity {

  ArrayList<BarEntry> entries = new ArrayList<>();
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    JSONArray myChartSortedArray = AppTrackActivity.mySortedArray;
    super.onCreate(savedInstanceState);

    int len = myChartSortedArray.length();
    ArrayList<String> labels = new ArrayList<String>();
    for(int i = 0; i < len; i++) {
      try {
        entries.add(new BarEntry( (float) myChartSortedArray.getJSONObject(i).getLong("sumTime"), i ) );
        labels.add( myChartSortedArray.getJSONObject(i).getString("packageName"));
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    BarDataSet dataset = new BarDataSet(entries, "# of App");

    final BarChart chart = new BarChart(this);
    chart.setDescription("#1-minute app usage");
    setContentView(chart);

    dataset.setColors(ColorTemplate.LIBERTY_COLORS);

    BarData data = new BarData(labels, dataset);
    chart.setData(data);
//        LimitLine line = new LimitLine(10f);
//        data.addLimitLine(line);

    chart.animateY(5000);

  }
}
