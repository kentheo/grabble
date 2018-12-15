package theofanous.kendeas.grabble.grabble;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.os.Bundle;
import android.os.Handler;
import android.animation.ObjectAnimator;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import static java.lang.Thread.sleep;
import static theofanous.kendeas.grabble.grabble.R.id.textView;

public class SplashActivity extends Activity {

    private static final int SPLASH_DISPLAY_LENGTH = 2000;
    private ProgressBar progressBar;
//    private int progressStatus = 0;
    private String TAG = "SplashActivity";
    private Button instructions_button;
    private Button continue_button;
    private ListView instructions_list;
    private ArrayAdapter<String> listAdapter ;
    boolean is_clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.i(TAG,"You are in the SplashActivity!!!!!!!!!!");


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Map-Activity. */
                Intent mainIntent = null;
                startAnimation();
                try {
                    mainIntent = new Intent(SplashActivity.this, Class.forName("theofanous.kendeas.grabble.grabble.MapsActivity"));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (!is_clicked) {
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                } else {
                    //
                }

            }
        }, SPLASH_DISPLAY_LENGTH);

        button();
    }

    private void startAnimation(){
        progressBar = (ProgressBar) findViewById(R.id.progressBar3);
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", 1000, 0);
        progressAnimator.setDuration(SPLASH_DISPLAY_LENGTH);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.start();
        if (is_clicked) {
            progressAnimator.pause();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }

    public void showInstructions() {
        Dialog dialog = new Dialog(SplashActivity.this, android.R.style.Theme_Light);
        dialog.setTitle("Grabble"); // Consider this instead of title
        dialog.setContentView(R.layout.instructions_layout);

        instructions_list = (ListView) dialog.findViewById(R.id.instructions_list);

        ArrayList<String> instructions = new ArrayList<String>();

        // Create ArrayAdapter using the planet list.
        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, instructions);
        listAdapter.add("• Walk around the Central Area of UoE");
        listAdapter.add("• Markers will show up if you get close enough!");
        listAdapter.add("• Click and collect the letters you want");
        listAdapter.add("• Your GOAL? Create 7-letter words!");
        listAdapter.add("• You think you have enough letters?");
        listAdapter.add("• Click the play button to write a word");
        listAdapter.add("• Save your word and check your score!");
        listAdapter.add("• The dictionary is locked.. but there may be surprises!");
        listAdapter.add("• Can you get 500pts? Aim for 500pt \"checkpoints\"");

        // Set listAdapter as the ListView
        instructions_list.setAdapter( listAdapter );
        dialog.show();
        Log.i(TAG, "Is the list showing?");
    }

    public void button() {
        instructions_button = (Button) findViewById(R.id.instructions_button);
        continue_button = (Button) findViewById(R.id.continue_button);

        instructions_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Instructions Button was clicked!");
                is_clicked = true;
                showInstructions();
                continue_button.setVisibility(View.VISIBLE);
            }
        });

        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = null;
                startAnimation();
                try {
                    mainIntent = new Intent(SplashActivity.this, Class.forName("theofanous.kendeas.grabble.grabble.MapsActivity"));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        });
    }
}
