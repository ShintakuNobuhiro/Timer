package com.example.shintaku.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Button startButton, lapButton;
    private TextView timerText;

    private Timer timer;
    private CountUpTimerTask timerTask = null;
    private Handler handler = new Handler();
    private long count = 0;
    private long mm = 0;
    private long ss = 0;
    private long ms = 0;
    private int lapCount = 0;
    private String lap = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (Button) findViewById(R.id.start_button);
        lapButton = (Button) findViewById(R.id.lap_button);
        timerText = (TextView) findViewById(R.id.timer);

        final List<String> timeList = new ArrayList<String>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,timeList);
        final ListView listView = (ListView) findViewById(R.id.time_list);
        listView.setAdapter(adapter);

        timerText.setText("00:00.0");

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 0) {
                    if (null != timer) {
                        timer.cancel();
                        timer = null;
                    }

                    // Timer インスタンスを生成
                    timer = new Timer();
                    // TimerTask インスタンスを生成
                    timerTask = new CountUpTimerTask();

                    // スケジュールを設定 100msec
                    // public void schedule (TimerTask task, long delay, long period)
                    timer.schedule(timerTask, 0, 100);
                    startButton.setBackgroundColor(getResources().getColor(R.color.stop));
                    startButton.setText("タイマー　停止");
                } else if (null != timer) {
                    // Cancel
                    timer.cancel();
                    timer = null;
                    timerText.setText(String.format("%1$02d:%2$02d.%3$01d", mm, ss, ms));
                    startButton.setBackgroundColor(getResources().getColor(R.color.reset));
                    startButton.setTextColor(getResources().getColor(R.color.black));
                    startButton.setText("タイマー・ラップ　リセット");
                } else {
                    count = 0;
                    lapCount = 0;
                    startButton.setBackgroundColor(getResources().getColor(R.color.start));
                    startButton.setTextColor(getResources().getColor(R.color.white));
                    startButton.setText("タイマー　開始");
                    timerText.setText("00:00.0");
                }
            }
        });

        lapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count != 0) {
                    if(timer != null) {
                        lapCount++;
                        if (lapCount < 10)
                            timeList.add("0"+lapCount+": "+String.format("%1$02d:%2$02d.%3$01d", mm, ss, ms));
                        else
                            timeList.add(lapCount+": "+String.format("%1$02d:%2$02d.%3$01d", mm, ss, ms));
                    }
                    listView.setAdapter(adapter);
                }
            }
        });
    }

    class CountUpTimerTask extends TimerTask {
        @Override
        public void run() {
            // handlerを使って処理をキューイングする
            handler.post(new Runnable() {
                public void run() {
                    count++;
                    mm = count * 100 / 1000 / 60;
                    ss = count * 100 / 1000 % 60;
                    ms = (count * 100 - ss * 1000 - mm * 1000 * 60) / 100;
                    // 桁数を合わせるために02d(2桁)を設定
                    timerText.setText(String.format("%1$02d:%2$02d.%3$01d", mm, ss, ms));
                }
            });
        }
    }
}