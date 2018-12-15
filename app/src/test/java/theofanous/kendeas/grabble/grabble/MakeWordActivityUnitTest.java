package theofanous.kendeas.grabble.grabble;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MakeWordActivityUnitTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void isLetterAvailableTest() {
        MakeWordActivity makeWordActivity = new MakeWordActivity();
        Map<String, Integer> grabbed_letters_test = new HashMap<>();
        grabbed_letters_test.put("A", 2);
        grabbed_letters_test.put("B", 3);
        makeWordActivity.grabbed_letters = grabbed_letters_test;
        boolean availability = makeWordActivity.isLetterAvailable("A");
        Assert.assertEquals(true, availability);
    }

    @After
    public void tearDown() throws Exception {
    }
}
