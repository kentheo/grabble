package theofanous.kendeas.grabble.grabble;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SignUpActivity extends AppCompatActivity {

    private static EditText username;
    private static EditText password;
    private static Button sign_up_button;
    private String user, pass;

    File user_info_file;
    String USERINFO = "user_info.txt";

    private String TAG = "SignUpActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_main);
        Log.i(TAG, "onCreate!!");

        user_info_file = new File(this.getFilesDir(), USERINFO);
        System.out.println(user_info_file.length() + "  size of user_info_file <<<<<<<<<<<<<<<<<<<<<<");

        buttons();
    }

    private void signUpDialog() {
        Log.i(TAG, "sign up button was clicked");
        username =  (EditText)findViewById(R.id.editText_username_signUp);
        password = (EditText)findViewById(R.id.editText_password_signUp);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Are you sure these are your details?")
                        .setMessage("If you are sure click yes and enjoy the game!")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // write user info to file
                        try {
                            FileOutputStream output = openFileOutput(USERINFO,
                                    Context.MODE_PRIVATE);
                            DataOutputStream dout = new DataOutputStream(output);
                            user = username.getText().toString();
                            pass = password.getText().toString();
                            dout.writeInt(1);
                            dout.writeUTF(user + "," + pass);
                            System.out.println(user + " -- " + pass); // debugging

                            Log.i(TAG, "info was stored to file");
                            dout.flush(); // Flush stream ...
                            dout.close(); // ... and close.
                        } catch (IOException exc) {
                            exc.printStackTrace();
                        }
                        // go to splash screen
                        Intent intent = new Intent("theofanous.kendeas.grabble.grabble.SplashActivity");
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void buttons() {
        sign_up_button = (Button)findViewById(R.id.sign_up_button_once);

        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpDialog();
            }
        });

    }
}
