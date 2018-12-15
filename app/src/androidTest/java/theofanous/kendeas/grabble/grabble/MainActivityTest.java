package theofanous.kendeas.grabble.grabble;

import android.support.test.filters.SmallTest;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }


    @SmallTest
    public void testEditTexts() {
        EditText username = (EditText) getActivity().findViewById(R.id.editText_username);
        EditText password = (EditText) getActivity().findViewById(R.id.editText_password);
        assertNotNull(username);
        assertNotNull(password);
    }


    @SmallTest
    public void testButtons() {
        Button login_button = (Button) getActivity().findViewById(R.id.login_button);
        Button sign_up_button = (Button) getActivity().findViewById(R.id.sign_up_button);
        assertNotNull(login_button);
        assertNotNull(sign_up_button);

    }

    @SmallTest
    public void testTextViews() {
        TextView attempts = (TextView) getActivity().findViewById(R.id.textView_attempts_counter);
        assertNotNull(attempts);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
