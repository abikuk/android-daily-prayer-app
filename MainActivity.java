package com.abidemikusimo.dailyprayerpoints;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.os.CountDownTimer;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.w3c.dom.Document;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class MainActivity extends ActionBarActivity {

    String[] mTestArray;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    public static final String ddate = "dateKey";
    public static final String countnumber = "numberoflaunches";
    SharedPreferences sharedpreferences;

    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    String currentDateandTime = formatter.format(new Date());

    MyCount timerCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //count down activity exit
        timerCount = new MyCount(8 * 1000, 1000);
        timerCount.start();

        mTestArray =   getResources().getStringArray(R.array.prayers_array);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //TO DO
        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm"); //change format as needed
        String formattedDate = df.format(c.getTime());


        Button btn = (Button)findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
            }
        });
//count down activity exit
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MainActivity.this, DayPrayer.class);
                startActivity(intent);
                // finishscreen();
            }
        };
        Timer t = new Timer();
        t.schedule(task, 7000);

        //AdView mAdView = (AdView) findViewById(R.id.adView);
       // AdRequest adRequest = new AdRequest.Builder().build();
        //mAdView.loadAd(adRequest);


    }

    //count down number
    public class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            //some script here
            Button btn = (Button)findViewById(R.id.btn);
            btn.setText("Done" );
        }

        @Override
        public void onTick(long millisUntilFinished) {
            //some script here
            Button btn = (Button)findViewById(R.id.btn);
            btn.setText("Loading Prayer Point of The Day... " + millisUntilFinished / 1000);
            TextView tfield = (TextView)findViewById(R.id.timeField);
            // tfield.setText("seconds remaining: " + millisUntilFinished / 1000);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateTextView();

        saveThePrayer();

        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String channel = (shared.getString(Name, ""));
        int new_d = (shared.getInt("ddate", 0));
            alarmMethod();
        TextView share = (TextView)findViewById(R.id.shared);
        share.setText(channel);
    }

    private void alarmMethod() {

        Calendar calendar = Calendar.getInstance();
        // we can set time by open date and time picker dialog
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Intent intent1 = new Intent(MainActivity.this, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                MainActivity.this, 0, intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) MainActivity.this
                .getSystemService(MainActivity.this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    private void updateTextView() {
        TextView textView = (TextView)findViewById(R.id.randomTextView);
        Random random = new Random();

        int maxIndex = mTestArray.length;
        int generatedIndex = random.nextInt(maxIndex);
        textView.setText(mTestArray[generatedIndex]);
    }

    private void saveThePrayer(){
        TextView textp = (TextView)findViewById(R.id.randomTextView);
        String n  = textp.getText().toString();

        //long days_1 = 1000 * 60 * 60 * 24 * 1;
        //Toast.makeText(MainActivity.this,"D = " + days_1, Toast.LENGTH_LONG).show();

        Calendar c = Calendar.getInstance();
        int today_date = c.get(Calendar.DATE);

        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        int stored_date =(shared.getInt(ddate, 0));

        SharedPreferences.Editor editor = sharedpreferences.edit();
        if(today_date != stored_date) {
            editor.putString(Name, n);
        }

        editor.putInt(ddate, today_date);
        editor.putInt(countnumber, 1);
        editor.commit();

        //Toast.makeText(MainActivity.this,"Thanks" + today_date, Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getAction();
        if (action == null) {
            return;
        }

    }

    /**
     * Stops started services and exits the application.
     */
    private void exit() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       // intent.setAction(Stn1110Service.CLOSE_ACTION);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void finishscreen() {
        this.finish();
    }


}
