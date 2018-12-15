package theofanous.kendeas.grabble.grabble;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.*;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private String TAG = "MainActivity";
    private static EditText username;
    private static EditText password;
    private static TextView attempts;
    private static Button login_button;
    private static Button sign_up_button;
    private static Button dc_button;
    private static final int RC_SIGN_IN = 9001;
    int attempt_counter = 5;

    private String userName, passWord;
    File user_info_file;
    String USERINFO = "user_info.txt";

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "Application is working!!!!!!");

        user_info_file = new File(this.getFilesDir(), USERINFO);
        System.out.println(user_info_file.length() + "  size of user_info_file <<<<<<<<<<<<<<<<<<<<<<");

        if (isConnected(getApplicationContext())) {
//            if (isFirstTime()){
//                signUpDialog();
//            }
            readUserInfoFromFile();
            LoginButton();
        } else {
            showDialog();
            if (isConnected(getApplicationContext())){
                showDialog2();
            }

            readUserInfoFromFile();
            LoginButton();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        findViewById(R.id.sign_in_button).setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    public boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiInfo != null && wifiInfo.isConnected()) || (mobileInfo != null && mobileInfo.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Grabble requires network connection! Turn on your Wi-Fi!")
                .setCancelable(false)
                .setPositiveButton("Connect to WIFI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
//                .setPositiveButton("Connect to net",new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
//                    }
//                })
                .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void showDialog2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Grabble requires network connection! Turn on your Wi-Fi!")
                .setCancelable(false)
                .setPositiveButton("Connect to WIFI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
                    }
                })
//                .setPositiveButton("Connect to net",new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
//                    }
//                })
                .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void LoginButton(){
        username = (EditText)findViewById(R.id.editText_username);
        password = (EditText)findViewById(R.id.editText_password);
        attempts = (TextView) findViewById(R.id.textView_attempts_counter);
        login_button = (Button)findViewById(R.id.login_button);
        sign_up_button = (Button) findViewById(R.id.sign_up_button);
        dc_button = (Button) findViewById(R.id.disconnect_button);

        attempts.setText(Integer.toString(attempt_counter));

        login_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.i(TAG, "Login Button was clicked!");
                Log.i(TAG, "username = " + userName + ", password = " + passWord);
                // get username and password from file
                if ((username.getText().toString().equals(userName)) && (password.getText().toString().equals(passWord))){
                    Toast.makeText(MainActivity.this, "Username and Password is correct", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent("theofanous.kendeas.grabble.grabble.SplashActivity");
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Username and Password is incorrect", Toast.LENGTH_SHORT).show();
                    attempt_counter--;
                    attempts.setText(Integer.toString(attempt_counter));
                    if (attempt_counter == 0){
                        login_button.setEnabled(false);
                    }
                }
            }
        });

        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

        dc_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revokeAccess();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.disconnect_button:
                revokeAccess();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            //updateUI(true);
            Intent intent = new Intent("theofanous.kendeas.grabble.grabble.SplashActivity");
            startActivity(intent);
        } else {
            //
        }
    }

    private void revokeAccess() {
        Log.i(TAG, "revokeAccess");
        if (mGoogleApiClient.isConnected()) {
            Log.i(TAG, "revokeAccess and google api client is connected");
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            // [START_EXCLUDE]
                            // updateUI(false);
                            // [END_EXCLUDE]
                        }
                    });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void signUp() {
        Log.i(TAG, "signUp button was clicked");
        if (user_info_file.exists()) {
            LayoutInflater li = LayoutInflater.from(MainActivity.this);
            final View promptsView = li.inflate(R.layout.pass_check, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // set prompts.xml to alertdialog builder
            builder.setView(promptsView);

            final EditText pass = (EditText) promptsView
                    .findViewById(R.id.pass_check_edit);
            builder.setMessage("Enter your previous password")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.i(TAG, "passWord == " + passWord);
                            Log.i(TAG, "pass entered == " + pass.getText().toString());
                            if (pass.getText().toString().equals(passWord)) {
                                Intent intent = new Intent("theofanous.kendeas.grabble.grabble.SignUpActivity");
                                startActivity(intent);
                            } else {
                                Toast.makeText(MainActivity.this, "The password is incorrect", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            Intent intent = new Intent("theofanous.kendeas.grabble.grabble.SignUpActivity");
            startActivity(intent);
        }

    }

    public void readUserInfoFromFile() {
        Log.i(TAG, "readUserInfoFromFile");
        sign_up_button = (Button) findViewById(R.id.sign_up_button);
        //user_info_file.delete();
        if (user_info_file.exists()) {
            Log.i(TAG, "user info file exists");
            try {
                FileInputStream input = openFileInput(USERINFO);
                DataInputStream din = new DataInputStream(input);
                int sz = din.readInt(); // Read line count
                Log.i(TAG, "size of din === " + sz);
                for (int i = 0; i < sz; i++) {
                    String str = din.readUTF();
                    Log.v("read", str);
                    String[] stringArray = str.split(",");
                    userName = stringArray[0];
                    passWord = stringArray[1];
                    System.out.println("I just put username and password as " + userName + ", " + passWord);
                }
                din.close();
            } catch (IOException exc) {
                exc.printStackTrace();
            }
            System.out.println("Username:  " + userName + "  , Password:  " + passWord);

            sign_up_button.setText("Change pass");
        }
    }
}