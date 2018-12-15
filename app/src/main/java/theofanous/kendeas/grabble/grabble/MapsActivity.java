package theofanous.kendeas.grabble.grabble;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.kml.KmlLayer;
import com.google.maps.android.kml.KmlPlacemark;
import org.xmlpull.v1.XmlPullParserException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMarkerClickListener {

    private String TAG = "MapsActivity";
    private GoogleMap mMap;
    private Button options_button;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    InputStream kmlInputStream;
    KmlLayer kmlLayer;
    List<Marker> markerList = new ArrayList<>();
    File file;
    String MARKERS_FILE = "markerList_file.txt";
    MediaPlayer mPlayer;

    // Calendar to decide which KML layer to add
    Calendar cal = Calendar.getInstance();

    private static ImageButton musicOn;
    private static ImageButton musicOff;
    private static ImageButton make_word_button;

    // Variables for handling letter grabs
    Map<String, Integer> grabbed_letters = new HashMap<>();
    File letters_file;
    String LETTERS_FILE = "letters_file.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Log.i(TAG, "onCreate!!!");

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        file = new File(this.getFilesDir(), MARKERS_FILE);
        System.out.println(file.length() + "  size of file >>>>>>>>>>>>>>>>>>>>>>");

        letters_file = new File(this.getFilesDir(), LETTERS_FILE);
        System.out.println(letters_file.length() + "  size of letters_file >>>>>>>>>>>>>>>>>>>>>>");

        // Start music
        mPlayer = MediaPlayer.create(this, R.raw.intro);
        mPlayer.start();
        mPlayer.setLooping(true);

        musicOnButton();
        musicOffButton();

        readLettersFromFile();

        // Make word button
        makeWord();
    }

    public void makeWord() {
        Log.i(TAG, "makeWord function");
        make_word_button = (ImageButton)findViewById(R.id.make_word_play_button);
        make_word_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("theofanous.kendeas.grabble.grabble.MakeWordActivity");
                startActivity(intent);
            }
        });
    }

    public void readLettersFromFile(){
        Log.i(TAG, "readLettersFromFile");
        letters_file = new File(this.getFilesDir(), LETTERS_FILE);
        System.out.println(letters_file.length() + "  size of letters_file >>>>>>>>>>>>>>>>>>>>>>");
        try {
            FileInputStream input = openFileInput(LETTERS_FILE);
            DataInputStream din = new DataInputStream(input);
            int sz = din.readInt(); // Read line count
            for (int i = 0; i < sz; i++) {
                String str = din.readUTF();
                //Log.v("read", str);
                String[] stringArray = str.split(",");
                String name = stringArray[0];
                int value = Integer.parseInt(stringArray[1]);
                grabbed_letters.put(name, value);
            }
            // print letters for debugging
//            for (String name: grabbed_letters.keySet()) {
//                String value = grabbed_letters.get(name).toString();
//                Log.i(TAG, name + " -- " + value);
//            }
            din.close();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
//        System.out.println("size of grabbed_letters is ===== " + grabbed_letters.size());

    }

    public void musicOnButton() {
        Log.i(TAG, "musicOnButton function");
        musicOn = (ImageButton) findViewById(R.id.music_on);
        musicOn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                mPlayer.pause();
                musicOn.setVisibility(View.INVISIBLE);
                musicOff.setVisibility(View.VISIBLE);
            }
        });

    }

    public void musicOffButton() {
        Log.i(TAG, "musicOffButton function");
        musicOff  = (ImageButton) findViewById(R.id.music_off);
        musicOff.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                mPlayer.start();
                musicOff.setVisibility(View.INVISIBLE);
                musicOn.setVisibility(View.VISIBLE);
            }
        });

    }

    public boolean isMusicOn() {
        int x = musicOn.getVisibility();
        return x == View.VISIBLE;
    }

    public InputStream chooseKmlLayer(int day) {
        Log.i(TAG, "KML Layer of the day is " + day);
        switch (day) {
            case Calendar.SUNDAY:
                try {
                    kmlInputStream = new URL("http://www.inf.ed.ac.uk/teaching/courses/selp/coursework/sunday.kml").openStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case Calendar.MONDAY:
                try {
                    kmlInputStream = new URL("http://www.inf.ed.ac.uk/teaching/courses/selp/coursework/monday.kml").openStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case Calendar.TUESDAY:
                try {
                    kmlInputStream = new URL("http://www.inf.ed.ac.uk/teaching/courses/selp/coursework/tuesday.kml").openStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case Calendar.WEDNESDAY:
                try {
                    kmlInputStream = new URL("http://www.inf.ed.ac.uk/teaching/courses/selp/coursework/wednesday.kml").openStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case Calendar.THURSDAY:
                try {
                    kmlInputStream = new URL("http://www.inf.ed.ac.uk/teaching/courses/selp/coursework/thursday.kml").openStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case Calendar.FRIDAY:
                try {
                    kmlInputStream = new URL("http://www.inf.ed.ac.uk/teaching/courses/selp/coursework/friday.kml").openStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case Calendar.SATURDAY:
                try {
                    kmlInputStream = new URL("http://www.inf.ed.ac.uk/teaching/courses/selp/coursework/saturday.kml").openStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                try {
                    kmlInputStream = new URL("http://www.inf.ed.ac.uk/teaching/courses/selp/coursework/sunday.kml").openStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        return kmlInputStream;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG, "onMapReady");
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Choose KML layer according to current day of the week
        Calendar calendar = Calendar.getInstance();
        //calendar.setFirstDayOfWeek(Calendar.MONDAY);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        System.out.println("the day im using is === " + day + "/////////////////");
        kmlInputStream = chooseKmlLayer(day);
        try {
            kmlLayer = new KmlLayer(googleMap, kmlInputStream, getApplicationContext());
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        System.out.println("file was last modified on " + sdf.format(file.lastModified()) + " <<<<<<<<<<<<<<<<");
        int day_last_modified = 0;
        try {
            Date date = sdf.parse(sdf.format(file.lastModified()));
            System.out.println("date the file was last modified was: " + date + " >>>>>>>>>>>>>>>>>>>>");
            day_last_modified = date.getDay() + 1;
            System.out.println("day of the week it was last modified was " + day_last_modified + "!!!!!!!!!!!!!!!!!!!");
            if (day_last_modified == 8){
                day_last_modified = 1; // Set k to SUNDAY
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Know what to load as KML layer

        if (day != day_last_modified) {
            Log.i(TAG, "Day of last modified is not equal to day of kml so I take new kmlLayer");
            for (KmlPlacemark placemark : kmlLayer.getPlacemarks()) {
                // placemark.getGeometry()); ------>> Point{ coordinates=lat/lng: (55.9450968041008,-3.1852262186354863) }
                // placemark.getProperty("description"); -----> capital letters

                String[] coordinates = placemark.getGeometry().getGeometryObject().toString().split(",");

                LatLng latLng = new LatLng(Double.valueOf(coordinates[0].substring(10, coordinates[0].length())), Double.valueOf(coordinates[1].substring(0, coordinates[1].length() - 1)));

                if (null == mMap) {
                    throw new NullPointerException("Something went horribly wrong, please restart");
                }
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(placemark.getProperty("name"))
                        .snippet(placemark.getProperty("description"))

                );
                marker.setVisible(false);  // set all markers invisible until the user is in range
                markerList.add(marker);
            }
        } else {
            // Read the markers from file since probably some of them are consumed
            Log.i(TAG, "Days are equal so I read straight from the file");
            //file.delete();
            try {
                FileInputStream input = openFileInput(MARKERS_FILE);
                DataInputStream din = new DataInputStream(input);
                int sz = din.readInt(); // Read line count
                for (int i = 0; i < sz; i++) {
                    String str = din.readUTF();
                    //Log.v("read", str);
                    String[] stringArray = str.split(",");
                    double latitude = Double.parseDouble(stringArray[0]);
                    double longitude = Double.parseDouble(stringArray[1]);
                    String description = stringArray[2];
                    Marker m = mMap.addMarker(new MarkerOptions().title("Point " + String.valueOf(i+1)).position(new LatLng(latitude,longitude)).snippet(description));
                    m.setVisible(false); // set all markers invisible until the user is in range
                    markerList.add(m);
                }
                din.close();
            } catch (IOException exc) {
                exc.printStackTrace();
            }
            System.out.println("size of markerList is ===== " + markerList.size());

        }

        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);

        }

        options_button = (Button) findViewById(R.id.options_button);
        options_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("theofanous.kendeas.grabble.grabble.CollectionOfLettersActivity");
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        Log.i(TAG, "You just clicked a marker!!!!!!!!!");
        // Data from the marker
        final String letter = marker.getSnippet();
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                                    .setTitle("Result")
                                    .setMessage("Do you wish to collect the letter " + letter + "  ?")
                                    .setIcon(android.R.drawable.ic_dialog_info)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            Log.i(TAG, "YES was just clicked!!");
                                            showYesToast(marker);
                                            markerList.remove(marker);
                                            marker.remove();
                                            handleLetter(letter);
                                        }})
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            Log.i(TAG, "NO was just clicked!!");
                                            showNoToast();
                                        }}).show();

        return false;
    }

    public void handleLetter(String letter) {
        // Increment the number of letters you have
        if (grabbed_letters.containsKey(letter)){
            int i = grabbed_letters.get(letter);
            grabbed_letters.remove(letter);
            grabbed_letters.put(letter, i+1);
        } else {
            grabbed_letters.put(letter, 1);
        }
        // Print out the hash map of grabbed letters for debugging
//        for (String name: grabbed_letters.keySet()){
//            String value = grabbed_letters.get(name).toString();
//            System.out.println(name + " -- " + value);
//        }
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    protected void startLocationUpdates() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); // 5 seconds
        mLocationRequest.setFastestInterval(2000); // 2 second
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        startLocationUpdates();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);

        //**************************
        builder.setAlwaysShow(true); //this is the key ingredient
        //**************************

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    MapsActivity.this, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");

        // write markers to file
        try {
            FileOutputStream output = openFileOutput(MARKERS_FILE,
                    Context.MODE_PRIVATE);
            DataOutputStream dout = new DataOutputStream(output);
            dout.writeInt(markerList.size()); // Save line count

            for (Marker m : markerList) {
                dout.writeUTF(m.getPosition().latitude + "," + m.getPosition().longitude + "," + m.getSnippet());
                //Log.v(TAG, "I will be writing these " + m.getPosition().latitude + "," + m.getPosition().longitude + "," + m.getSnippet());
            }
            Log.i(TAG, "all markers are written onPause");
            dout.flush(); // Flush stream ...
            dout.close(); // ... and close.
        } catch (IOException exc) {
            exc.printStackTrace();
        }

        // write letters to file
        try {
            FileOutputStream output = openFileOutput(LETTERS_FILE,
                    Context.MODE_PRIVATE);
            DataOutputStream dout = new DataOutputStream(output);
            dout.writeInt(grabbed_letters.size()); // Save line count

            for (String name: grabbed_letters.keySet()) {
                String value = grabbed_letters.get(name).toString();
                dout.writeUTF(name + "," + value);
            }
            Log.i(TAG, "all LETTERS are written onPause");
            dout.flush(); // Flush stream ...
            dout.close(); // ... and close.
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        mPlayer.pause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        if (isMusicOn()) {
            mPlayer.start();
        }
        readLettersFromFile();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
        if (isMusicOn()) {
            mPlayer.start();
        }
        readLettersFromFile();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");

        // write markers to file
        try {
            FileOutputStream output = openFileOutput(MARKERS_FILE,
                    Context.MODE_PRIVATE);
            DataOutputStream dout = new DataOutputStream(output);
            dout.writeInt(markerList.size()); // Save line count
            for (Marker m : markerList) {
                dout.writeUTF(m.getPosition().latitude + "," + m.getPosition().longitude + "," + m.getSnippet());
            }
            Log.i(TAG, "all markers are written onDestroy");
            dout.flush(); // Flush stream ...
            dout.close(); // ... and close.
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        // write letters and numbers to file
        try {
            FileOutputStream output = openFileOutput(LETTERS_FILE,
                    Context.MODE_PRIVATE);
            DataOutputStream dout = new DataOutputStream(output);
            dout.writeInt(grabbed_letters.size()); // Save line count

            for (String name: grabbed_letters.keySet()) {
                String value = grabbed_letters.get(name).toString();
                dout.writeUTF(name + "," + value);
            }
            Log.i(TAG, "all LETTERS are written onDestroy");
            dout.flush(); // Flush stream ...
            dout.close(); // ... and close.
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        mPlayer.release();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    // Need to handle the range from the user's location such that the markers go visible
    private void handleNewLocation(Location location) {
        Log.i(TAG, location.toString());

        //Toast.makeText(this, location.toString(), Toast.LENGTH_SHORT).show();


        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        float zoomLevel = 18; // Max value: 21

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));

        Log.i(TAG, "handleNewLocation for loop for markers");
        for (Marker m : markerList) {
            //Log.i(TAG, "Close to a marker>>>>>>>>>>>>.");
            Location temp = new Location(LocationManager.GPS_PROVIDER);
            temp.setLatitude(m.getPosition().latitude);
            temp.setLongitude(m.getPosition().longitude);
            if (location.distanceTo(temp) < 25) {
                // I removed vibrate
                m.setVisible(true);    // consider setting all markers to visible if you are in range
            } else {
                m.setVisible(false);
            }
        }

    }

    protected void stopLocationUpdates() {
        if (mGoogleApiClient != null) {
            Log.i(TAG, "google api client is null!!!!!!!!!!!!!!!!!!!!!!!");
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged successful");

        handleNewLocation(location);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // Request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    public void showYesToast(Marker m){
        Toast.makeText(this, "You just collected the letter " + m.getSnippet() + "!", Toast.LENGTH_SHORT).show();
    }

    public void showNoToast(){
        Toast.makeText(this, "You did not collect a letter", Toast.LENGTH_SHORT).show();
    }

}


