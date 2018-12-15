package theofanous.kendeas.grabble.grabble;

import android.app.Instrumentation;
import android.support.test.filters.SmallTest;
import android.support.v7.app.AlertDialog;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


public class SignUpActivityTest extends ActivityInstrumentationTestCase2<SignUpActivity> {
    public SignUpActivityTest() {
        super(SignUpActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @SmallTest
    public void testEditTexts() {
        EditText username =  (EditText) getActivity().findViewById(R.id.editText_username_signUp);
        EditText password = (EditText) getActivity().findViewById(R.id.editText_password_signUp);
        assertNotNull(username);
        assertNotNull(password);
    }

    @SmallTest
    public void testButtons() {
        Button sign_up_button = (Button) getActivity().findViewById(R.id.sign_up_button_once);
        assertNotNull(sign_up_button);
    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
