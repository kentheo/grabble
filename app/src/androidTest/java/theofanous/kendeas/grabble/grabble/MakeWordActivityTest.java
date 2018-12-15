package theofanous.kendeas.grabble.grabble;

import android.support.test.filters.SmallTest;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by kendeas93 on 17/01/17.
 */

public class MakeWordActivityTest extends ActivityInstrumentationTestCase2<MakeWordActivity> {
    public MakeWordActivityTest() {
        super(MakeWordActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @SmallTest
    public void testTextViews() {
        TextView word_value = (TextView) getActivity().findViewById(R.id.score_value);
        TextView textbox1 = (TextView) getActivity().findViewById(R.id.textbox1);
        TextView textbox2 = (TextView) getActivity().findViewById(R.id.textbox2);
        TextView textbox3 = (TextView) getActivity().findViewById(R.id.textbox3);
        TextView textbox4 = (TextView) getActivity().findViewById(R.id.textbox4);
        TextView textbox5 = (TextView) getActivity().findViewById(R.id.textbox5);
        TextView textbox6 = (TextView) getActivity().findViewById(R.id.textbox6);
        TextView textbox7 = (TextView) getActivity().findViewById(R.id.textbox7);
        assertNotNull(word_value);
        assertNotNull(textbox1);
        assertNotNull(textbox2);
        assertNotNull(textbox3);
        assertNotNull(textbox4);
        assertNotNull(textbox5);
        assertNotNull(textbox6);
        assertNotNull(textbox7);
    }

    @SmallTest
    public void testButtons() {
        Button calculate_btn = (Button) getActivity().findViewById(R.id.calculate_value_button);
        Button clear_button = (Button) getActivity().findViewById(R.id.clear_letters_button);
        Button save_word_button = (Button) getActivity().findViewById(R.id.save_word_button);
        assertNotNull(calculate_btn);
        assertNotNull(clear_button);
        assertNotNull(save_word_button);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
