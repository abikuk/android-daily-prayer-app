package com.abidemikusimo.dailyprayerpoints;

/**
 * Created by SK on 28/07/2015.
 */
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
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

public class DayPrayer extends ActionBarActivity {

    String[] mTestArray;
    ImageButton share_show;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    public static final String ddate = "dateKey";
    SharedPreferences sharedpreferences;

    ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_prayer);

        share_show = (ImageButton) findViewById(R.id.share_btn);

        mTestArray =   getResources().getStringArray(R.array.prayers_array);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        saveThePrayer();

        final Typeface font2 = Typeface.createFromAsset(getAssets(), "JosefinSansRegular.ttf");
        final Typeface font1 = Typeface.createFromAsset(getAssets(), "JosefinSansBold.ttf");
        TextView descr = (TextView) findViewById(R.id.day_prayer);
        TextView special = (TextView)findViewById(R.id.special_day);
        TextView url_link = (TextView)findViewById(R.id.url_link);
        String message_url = "<a href=\"http://bit.ly/1JMkOP9\">See More</a>";
        url_link.setMovementMethod(LinkMovementMethod.getInstance());
        special.setText(getString(R.string.special) + Html.fromHtml(message_url));
        TextView tbar = (TextView) findViewById(R.id.customtitlebar);
        descr.setTypeface(font2);
        url_link.setTypeface(font2);
        tbar.setTypeface(font1);

        share_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                String prayer = (shared.getString(Name, ""));

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String message = "<br/><br/>Get this Free Daily Prayer Points App:<br/> ";
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Sharing Prayer from Daily Prayer Points:\n \n \"" + prayer + "\"" + Html.fromHtml(message) + "http://bit.ly/1JMkOP9");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.hello_world)));


            }
        });


    }

    private void updateTextView() {

        Calendar c = Calendar.getInstance();
        int today_date = c.get(Calendar.DATE);
        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        int stored_date =(shared.getInt(ddate, 0));

        SharedPreferences.Editor editor = sharedpreferences.edit();
        if(today_date != stored_date) {
            String stored_prayer = (shared.getString(Name, ""));
            if((stored_prayer).equals(stored_prayer)) {
                TextView textp = (TextView)findViewById(R.id.day_prayer);
                Random random = new Random();

                int maxIndex = mTestArray.length;
                int generatedIndex = random.nextInt(maxIndex);
                textp.setText(mTestArray[generatedIndex]);
                //String new_rand = mTestArray[generatedIndex];

            }

        }

    }
 /*  //when back button is pressed exit to homepage
   @Override
   public void onDestroy()
   {
       android.os.Process.killProcess(android.os.Process.myPid());
       super.onDestroy();
   }
*/

    private void saveThePrayer(){

        // Toast.makeText(DayPrayer.this,"Everyday is special, tomorrow you will receive another prayer point.", Toast.LENGTH_LONG).show();

        Calendar c = Calendar.getInstance();
        int now_date = c.get(Calendar.DATE);

        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String ptoday = (shared.getString(Name, ""));
        int today_date =(shared.getInt(ddate, 0));


        int new_day = c.get(Calendar.DATE);

        TextView day_prayer = (TextView)findViewById(R.id.day_prayer);

        int stored_date =(shared.getInt(ddate, 0));

        SharedPreferences.Editor editor = sharedpreferences.edit();
        if(stored_date != new_day) {
            String stored_prayer = (shared.getString(Name, ""));
            if( (stored_prayer).equals(stored_prayer)) {

                updateprayer();

                //reload();

            }

        } else{day_prayer.setText(ptoday);}

    }

    public void updateprayer() {
        TextView textp = (TextView)findViewById(R.id.day_prayer);
        Random random = new Random();

        int maxIndex = mTestArray.length;
        int generatedIndex = random.nextInt(maxIndex);
        textp.setText(mTestArray[generatedIndex]);
        //String new_rand = mTestArray[generatedIndex];


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
}
