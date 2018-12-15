package theofanous.kendeas.grabble.grabble;

import android.support.test.filters.SmallTest;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ApplicationTestCase;
import android.widget.Button;
import android.widget.TextView;


public class CollectionOfLettersActivityTest extends ActivityInstrumentationTestCase2<CollectionOfLettersActivity> {
    public CollectionOfLettersActivityTest() {
        super(CollectionOfLettersActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @SmallTest
    public void testTextViews() {
        TextView score_textView = (TextView) getActivity().findViewById(R.id.score_text);
        assertNotNull(score_textView);
    }

    @SmallTest
    public void testButtons() {
        Button dictionary = (Button) getActivity().findViewById(R.id.dictionary_button);
        Button letters = (Button) getActivity().findViewById(R.id.letters_button);
        Button history = (Button) getActivity().findViewById(R.id.history_button);
        assertNotNull(dictionary);
        assertNotNull(letters);
        assertNotNull(history);
    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
