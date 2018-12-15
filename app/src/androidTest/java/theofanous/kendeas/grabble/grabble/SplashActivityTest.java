package theofanous.kendeas.grabble.grabble;

import android.support.test.filters.SmallTest;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;


public class SplashActivityTest extends ActivityInstrumentationTestCase2<SplashActivity> {
    public SplashActivityTest() {
        super(SplashActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @SmallTest
    public void testButtons() {
        Button instructions_button = (Button) getActivity().findViewById(R.id.instructions_button);
        Button continue_button = (Button) getActivity().findViewById(R.id.continue_button);
        assertNotNull(instructions_button);
        assertNotNull(continue_button);
    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
