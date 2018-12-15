package theofanous.kendeas.grabble.grabble;


import android.app.Instrumentation;
import android.support.test.filters.SmallTest;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.ImageButton;

public class MapsActivityTest extends ActivityInstrumentationTestCase2<MapsActivity>{
    public MapsActivityTest() {
        super(MapsActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @SmallTest
    public void testButtons() {
        Button options_button = (Button) getActivity().findViewById(R.id.options_button);
        ImageButton musicOn = (ImageButton) getActivity().findViewById(R.id.music_on);
        ImageButton musicOff = (ImageButton) getActivity().findViewById(R.id.music_off);
        ImageButton make_word_button = (ImageButton) getActivity().findViewById(R.id.make_word_play_button);
        assertNotNull(options_button);
        assertNotNull(musicOn);
        assertNotNull(musicOff);
        assertNotNull(make_word_button);
    }

    @SmallTest
    public void testIntentToMakeWordActivity() {
        // register next activity that need to be monitored.
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(MakeWordActivity.class.getName(), null, false);

        // open current activity.
        final MapsActivity mapsActivity = getActivity();
        final ImageButton button = (ImageButton) getActivity().findViewById(R.id.make_word_play_button);
        mapsActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // click button and open next activity.
                button.performClick();
            }
        });

        //Watch for the timeout
        //example values 5000 if in ms, or 5 if it's in seconds.
        MakeWordActivity nextActivity = (MakeWordActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        assertNotNull(nextActivity);
        nextActivity .finish();
    }

    @SmallTest
    public void testIntentToCollectionOfLettersActivity() {
        // register next activity that need to be monitored.
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(CollectionOfLettersActivity.class.getName(), null, false);

        // open current activity.
        final MapsActivity mapsActivity = getActivity();
        final Button button = (Button) getActivity().findViewById(R.id.options_button);
        mapsActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // click button and open next activity.
                button.performClick();
            }
        });

        //Watch for the timeout
        //example values 5000 if in ms, or 5 if it's in seconds.
        CollectionOfLettersActivity nextActivity = (CollectionOfLettersActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        assertNotNull(nextActivity);
        nextActivity .finish();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
