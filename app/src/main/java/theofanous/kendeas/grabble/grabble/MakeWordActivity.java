package theofanous.kendeas.grabble.grabble;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MakeWordActivity extends AppCompatActivity {

    private String TAG = "MakeWordActivity";
    private TextView word_value;
    // Letters from MapsActivity
    Map<String, Integer> grabbed_letters = new HashMap<>();
    File letters_file;
    String LETTERS_FILE = "letters_file.txt";
    // Dictionary
    ArrayList<String> dictionary = new ArrayList<>();

    // Letters to ImageViews Map
    Map<ImageView, String> letters_to_imageViews = new HashMap<>();
    List<TextView> text_boxes = new ArrayList<>();

    // Map for letters and their values
    Map<String, Integer> letter_values = new HashMap<>();

    // Word and values file and map
    Map<String, Integer> words_created = new HashMap<>();
    File words_file;
    String WORDS_FILE = "words_file.txt";

    // Score
    int score;
    File score_file;
    String SCORE_FILE = "score_file.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate!!!");
        setContentView(R.layout.make_word_layout);

        setUp();
        readLettersFromFile(); // and fill the hashmap
        readWordsFromFile();   // and fill the hashmap
        readScoreFromFile();   // and fill the int
        readDictionary();      // and fill the arraylist
        buttons();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
        // write letters to file
        try {
            FileOutputStream output = openFileOutput(LETTERS_FILE,
                    Context.MODE_PRIVATE);
            DataOutputStream dout = new DataOutputStream(output);
            dout.writeInt(grabbed_letters.size()); // Save line count

            for (String name: grabbed_letters.keySet()) {
                String value = grabbed_letters.get(name).toString();
                dout.writeUTF(name + "," + value);
                System.out.println(name + " -- " + value); // debugging
            }
            Log.i(TAG, "all LETTERS are written onPause");
            dout.flush(); // Flush stream ...
            dout.close(); // ... and close.
        } catch (IOException exc) {
            exc.printStackTrace();
        }

        // write words to file
        try {
            FileOutputStream output = openFileOutput(WORDS_FILE,
                    Context.MODE_PRIVATE);
            DataOutputStream dout = new DataOutputStream(output);
            dout.writeInt(words_created.size()); // Save line count

            for (String name: words_created.keySet()) {
                String value = words_created.get(name).toString();
                dout.writeUTF(name + "," + value);
            }
            Log.i(TAG, "all WORDS are written onPause");
            dout.flush(); // Flush stream ...
            dout.close(); // ... and close.
        } catch (IOException exc) {
            exc.printStackTrace();
        }

        // write score to file
        try {
            FileOutputStream output = openFileOutput(SCORE_FILE,
                    Context.MODE_PRIVATE);
            DataOutputStream dout = new DataOutputStream(output);
            //dout.writeInt(score.size()); // Save line count
            dout.writeInt(1);
            dout.writeUTF(String.valueOf(score));

            Log.i(TAG, "SCORE is written onPause");
            dout.flush(); // Flush stream ...
            dout.close(); // ... and close.
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
        // write letters to file
        try {
            FileOutputStream output = openFileOutput(LETTERS_FILE,
                    Context.MODE_PRIVATE);
            DataOutputStream dout = new DataOutputStream(output);
            dout.writeInt(grabbed_letters.size()); // Save line count

            for (String name: grabbed_letters.keySet()) {
                String value = grabbed_letters.get(name).toString();
                dout.writeUTF(name + "," + value);
                System.out.println(name + " -- " + value); // debugging
            }
            Log.i(TAG, "all LETTERS are written onPause");
            dout.flush(); // Flush stream ...
            dout.close(); // ... and close.
        } catch (IOException exc) {
            exc.printStackTrace();
        }

        // write words to file
        try {
            FileOutputStream output = openFileOutput(WORDS_FILE,
                    Context.MODE_PRIVATE);
            DataOutputStream dout = new DataOutputStream(output);
            dout.writeInt(words_created.size()); // Save line count

            for (String name: words_created.keySet()) {
                String value = words_created.get(name).toString();
                dout.writeUTF(name + "," + value);
            }
            Log.i(TAG, "all WORDS are written onPause");
            dout.flush(); // Flush stream ...
            dout.close(); // ... and close.
        } catch (IOException exc) {
            exc.printStackTrace();
        }

        // write score to file
        try {
            FileOutputStream output = openFileOutput(SCORE_FILE,
                    Context.MODE_PRIVATE);
            DataOutputStream dout = new DataOutputStream(output);
            //dout.writeInt(score.size()); // Save line count
            dout.writeInt(1);
            dout.writeUTF(String.valueOf(score));

            Log.i(TAG, "SCORE is written onPause");
            dout.flush(); // Flush stream ...
            dout.close(); // ... and close.
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
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
            Log.i(TAG, "all LETTERS are written onDestroy");
            dout.flush(); // Flush stream ...
            dout.close(); // ... and close.
        } catch (IOException exc) {
            exc.printStackTrace();
        }

        // write words to file
        try {
            FileOutputStream output = openFileOutput(WORDS_FILE,
                    Context.MODE_PRIVATE);
            DataOutputStream dout = new DataOutputStream(output);
            dout.writeInt(words_created.size()); // Save line count

            for (String name: words_created.keySet()) {
                String value = words_created.get(name).toString();
                dout.writeUTF(name + "," + value);
            }
            Log.i(TAG, "all WORDS are written onDestroy");
            dout.flush(); // Flush stream ...
            dout.close(); // ... and close.
        } catch (IOException exc) {
            exc.printStackTrace();
        }

        // write score to file
        try {
            FileOutputStream output = openFileOutput(SCORE_FILE,
                    Context.MODE_PRIVATE);
            DataOutputStream dout = new DataOutputStream(output);
            //dout.writeInt(score.size()); // Save line count
            dout.writeInt(1);
            dout.writeUTF(String.valueOf(score));

            Log.i(TAG, "SCORE is written onDestroy");
            dout.flush(); // Flush stream ...
            dout.close(); // ... and close.
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    public void setUp() {
        // Create an array for the alphabet
        Log.i(TAG, "Time to set up everything");

        letters_file = new File(this.getFilesDir(), LETTERS_FILE);
        System.out.println(letters_file.length() + "  size of letters_file >>>>>>>>>>>>>>>>>>>>>>");

        words_file = new File(this.getFilesDir(), WORDS_FILE);
        System.out.println(words_file.length() + "  size of words_file <<<<<<<<<<<<<<<<<<<<<<");

        word_value = (TextView) findViewById(R.id.score_value);

        score_file = new File(this.getFilesDir(), SCORE_FILE);
        System.out.println(score_file.length() + "  size of score_file <<<<<<<<<<<<<<<<<<<<<<");

        TextView textbox1 = (TextView) findViewById(R.id.textbox1);
        TextView textbox2 = (TextView) findViewById(R.id.textbox2);
        TextView textbox3 = (TextView) findViewById(R.id.textbox3);
        TextView textbox4 = (TextView) findViewById(R.id.textbox4);
        TextView textbox5 = (TextView) findViewById(R.id.textbox5);
        TextView textbox6 = (TextView) findViewById(R.id.textbox6);
        TextView textbox7 = (TextView) findViewById(R.id.textbox7);

        text_boxes.add(0, textbox1);
        text_boxes.add(1, textbox2);
        text_boxes.add(2, textbox3);
        text_boxes.add(3, textbox4);
        text_boxes.add(4, textbox5);
        text_boxes.add(5, textbox6);
        text_boxes.add(6, textbox7);

        final View.OnClickListener letterClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Check if boxes are full or not
                if (!areBoxesFull()) {
                    if (isLetterAvailable(letters_to_imageViews.get(view))) {
                        populateBox(letters_to_imageViews.get(view));
                    }
                } else {
                    Toast.makeText(MakeWordActivity.this, "You seem to have filled all the boxes!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        View.OnClickListener textBoxClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Need to put the letter back in the grabbed letters map
                if (String.valueOf(((TextView) view).getText()).equals("")) {
                    // do nothing
                } else {
                    returnLetter(view);
                }

            }
        };

        ImageView letter_a = (ImageView) findViewById(R.id.table_a);
        ImageView letter_b = (ImageView) findViewById(R.id.table_b);
        ImageView letter_c = (ImageView) findViewById(R.id.table_c);
        ImageView letter_d = (ImageView) findViewById(R.id.table_d);
        ImageView letter_e = (ImageView) findViewById(R.id.table_e);
        ImageView letter_f = (ImageView) findViewById(R.id.table_f);
        ImageView letter_g = (ImageView) findViewById(R.id.table_g);
        ImageView letter_h = (ImageView) findViewById(R.id.table_h);
        ImageView letter_i = (ImageView) findViewById(R.id.table_i);
        ImageView letter_j = (ImageView) findViewById(R.id.table_j);
        ImageView letter_k = (ImageView) findViewById(R.id.table_k);
        ImageView letter_l = (ImageView) findViewById(R.id.table_l);
        ImageView letter_m = (ImageView) findViewById(R.id.table_m);
        ImageView letter_n = (ImageView) findViewById(R.id.table_n);
        ImageView letter_o = (ImageView) findViewById(R.id.table_o);
        ImageView letter_p = (ImageView) findViewById(R.id.table_p);
        ImageView letter_q = (ImageView) findViewById(R.id.table_q);
        ImageView letter_r = (ImageView) findViewById(R.id.table_r);
        ImageView letter_s = (ImageView) findViewById(R.id.table_s);
        ImageView letter_t = (ImageView) findViewById(R.id.table_t);
        ImageView letter_u = (ImageView) findViewById(R.id.table_u);
        ImageView letter_v = (ImageView) findViewById(R.id.table_v);
        ImageView letter_w = (ImageView) findViewById(R.id.table_w);
        ImageView letter_x = (ImageView) findViewById(R.id.table_x);
        ImageView letter_y = (ImageView) findViewById(R.id.table_y);
        ImageView letter_z = (ImageView) findViewById(R.id.table_z);

        letters_to_imageViews.put(letter_a, "A");
        letters_to_imageViews.put(letter_b, "B");
        letters_to_imageViews.put(letter_c, "C");
        letters_to_imageViews.put(letter_d, "D");
        letters_to_imageViews.put(letter_e, "E");
        letters_to_imageViews.put(letter_f, "F");
        letters_to_imageViews.put(letter_g, "G");
        letters_to_imageViews.put(letter_h, "H");
        letters_to_imageViews.put(letter_i, "I");
        letters_to_imageViews.put(letter_j, "J");
        letters_to_imageViews.put(letter_k, "K");
        letters_to_imageViews.put(letter_l, "L");
        letters_to_imageViews.put(letter_m, "M");
        letters_to_imageViews.put(letter_n, "N");
        letters_to_imageViews.put(letter_o, "O");
        letters_to_imageViews.put(letter_p, "P");
        letters_to_imageViews.put(letter_q, "Q");
        letters_to_imageViews.put(letter_r, "R");
        letters_to_imageViews.put(letter_s, "S");
        letters_to_imageViews.put(letter_t, "T");
        letters_to_imageViews.put(letter_u, "U");
        letters_to_imageViews.put(letter_v, "V");
        letters_to_imageViews.put(letter_w, "W");
        letters_to_imageViews.put(letter_x, "X");
        letters_to_imageViews.put(letter_y, "Y");
        letters_to_imageViews.put(letter_z, "Z");

        letter_a.setOnClickListener(letterClickListener);
        letter_b.setOnClickListener(letterClickListener);
        letter_c.setOnClickListener(letterClickListener);
        letter_d.setOnClickListener(letterClickListener);
        letter_e.setOnClickListener(letterClickListener);
        letter_f.setOnClickListener(letterClickListener);
        letter_g.setOnClickListener(letterClickListener);
        letter_h.setOnClickListener(letterClickListener);
        letter_i.setOnClickListener(letterClickListener);
        letter_j.setOnClickListener(letterClickListener);
        letter_k.setOnClickListener(letterClickListener);
        letter_l.setOnClickListener(letterClickListener);
        letter_m.setOnClickListener(letterClickListener);
        letter_n.setOnClickListener(letterClickListener);
        letter_o.setOnClickListener(letterClickListener);
        letter_p.setOnClickListener(letterClickListener);
        letter_q.setOnClickListener(letterClickListener);
        letter_r.setOnClickListener(letterClickListener);
        letter_s.setOnClickListener(letterClickListener);
        letter_t.setOnClickListener(letterClickListener);
        letter_u.setOnClickListener(letterClickListener);
        letter_v.setOnClickListener(letterClickListener);
        letter_w.setOnClickListener(letterClickListener);
        letter_x.setOnClickListener(letterClickListener);
        letter_y.setOnClickListener(letterClickListener);
        letter_z.setOnClickListener(letterClickListener);

        textbox1.setOnClickListener(textBoxClickListener);
        textbox2.setOnClickListener(textBoxClickListener);
        textbox3.setOnClickListener(textBoxClickListener);
        textbox4.setOnClickListener(textBoxClickListener);
        textbox5.setOnClickListener(textBoxClickListener);
        textbox6.setOnClickListener(textBoxClickListener);
        textbox7.setOnClickListener(textBoxClickListener);

        // Set up hashMap for letters and values
        letter_values.put("A", 3);
        letter_values.put("B", 20);
        letter_values.put("C", 13);
        letter_values.put("D", 10);
        letter_values.put("E", 1);
        letter_values.put("F", 15);
        letter_values.put("G", 18);
        letter_values.put("H", 9);
        letter_values.put("I", 5);
        letter_values.put("J", 25);
        letter_values.put("K", 22);
        letter_values.put("L", 11);
        letter_values.put("M", 14);
        letter_values.put("N", 6);
        letter_values.put("O", 4);
        letter_values.put("P", 19);
        letter_values.put("Q", 24);
        letter_values.put("R", 8);
        letter_values.put("S", 7);
        letter_values.put("T", 2);
        letter_values.put("U", 12);
        letter_values.put("V", 21);
        letter_values.put("W", 17);
        letter_values.put("X", 23);
        letter_values.put("Y", 16);
        letter_values.put("Z", 26);

    }

    // Method used to put back the letter to the hashmap, when the user clicked the box to remove it
    public void returnLetter(View view) {
        String letter = String.valueOf(((TextView) view).getText());
        Log.i(TAG, "returnLetter  ---- " + letter + " -----------------");
        int number = grabbed_letters.get(letter);
        number += 1;
        grabbed_letters.remove(letter);
        grabbed_letters.put(letter, number);
        ((TextView) view).setText("");
    }

    public boolean isLetterAvailable(String letter) {
        // Check grabbed letters
        //Log.i(TAG, "isLetterAvailable()");
        if (grabbed_letters.containsKey(letter)) {
            int x = grabbed_letters.get(letter);
            // System.out.println("Value x = " + x + " >>>>>>>>>>>>>>>>>>>>>>");
            if (x > 0) {
                return true;
            } else {
                Toast.makeText(MakeWordActivity.this, "Oops you don't have enough " + letter + "'s", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(MakeWordActivity.this, "Oops you don't have enough " + letter + "'s", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean areBoxesFull() {
        for (int i = 0; i < text_boxes.size(); i++) {
            if (text_boxes.get(i).getText() == "") {
                return false;
            }
        }
        return true;
    }

    public void populateBox(String letter_to_write) {
        Log.i(TAG, "populateBox()");
        for (int i = 0; i < text_boxes.size(); i++) {
            if (text_boxes.get(i).getText() == "") {
                text_boxes.get(i).setText(letter_to_write);
                text_boxes.get(i).setTextSize(40);
                text_boxes.get(i).setTextColor(Color.BLUE);
                int x = grabbed_letters.get(letter_to_write);
                x -= 1;
                grabbed_letters.remove(letter_to_write);
                grabbed_letters.put(letter_to_write, x);
                // Only show the toast if x == 0
                if (x == 0) {
                    Toast.makeText(MakeWordActivity.this, "You have " + String.valueOf(x) + " " + letter_to_write + "'s left", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
        // Print out the hash map of grabbed letters for debugging
//        for (String name: grabbed_letters.keySet()){
//            String value = grabbed_letters.get(name).toString();
//            System.out.println(name + " -- " + value);
//        }
    }

    public void buttons() {
        Log.i(TAG, "buttons function");
        Button calculate_btn = (Button)findViewById(R.id.calculate_value_button);
        Button clear_button = (Button)findViewById(R.id.clear_letters_button);
        Button save_word_button = (Button)findViewById(R.id.save_word_button);


        calculate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int word_value_number = calculateWordValue();
                // display the value of the word
                word_value.setText(String.valueOf(word_value_number));
            }
        });

        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear all letters from the textBoxes
                for (int i = 0; i < text_boxes.size(); i++) {
                    if (text_boxes.get(i).getText() != "") {
                        returnLetter(text_boxes.get(i));
                    }
                }
                word_value.setText("");
            }
        });

        save_word_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String word = createWordfromLetters();
                if (isItAvalidWord(word)) {
                    int value = calculateWordValue();
                    word_value.setText(String.valueOf(value));
                    Log.i(TAG, "word you created  -->  " + word);
                    Log.i(TAG, "value of the word  -->  " + value);
                    if (words_created.containsKey(word)) {
                        // If word was already created then tell user
                        Toast.makeText(MakeWordActivity.this, "No cheating! You have already created that word!", Toast.LENGTH_SHORT).show();
                    } else {
                        showWordDialog(word, value);
                    }
                } else {
                    Toast.makeText(MakeWordActivity.this, "Oops something is wrong with your word!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showWordDialog(final String word, final int value) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to create and save the word to your History?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Put the word and value to the map
                        words_created.put(word, value);
                        // Need to add the value of the word to score
                        score += value;
                        // Clear the boxes and the value of the word
                        for (int i = 0; i < text_boxes.size(); i++) {

                            if (text_boxes.get(i).getText() != "") {
                                text_boxes.get(i).setText("");
                            }
                        }
                        word_value.setText("");
                        // Print out the hash map of grabbed letters for debugging
                        for (String name: grabbed_letters.keySet()){
                            String value = grabbed_letters.get(name).toString();
                            System.out.println(name + " -- " + value);
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isItAvalidWord(String word) {
        // Check by looking at the Dictionary
        return dictionary.contains(word);
    }

    public int calculateWordValue() {
        Log.i(TAG, "calculateWordValue()");
        int value = 0;
        for (int i = 0; i < text_boxes.size(); i++) {
            String letter = String.valueOf(text_boxes.get(i).getText());
            if (text_boxes.get(i).getText() != "") {
                int temp = letter_values.get(letter);
                value += temp;
            }
        }
        Log.i(TAG, "calculateWordValue() === " + value);
        return value;
    }

    public String createWordfromLetters() {
        Log.i(TAG, "createWordsfromLetters()");
        String word = "";
        for (int i = 0; i < text_boxes.size(); i++) {
            word = word + String.valueOf(text_boxes.get(i).getText());
        }
        Log.i(TAG, "this is the word created ---> " + word);
        return word;
    }

    public void readLettersFromFile() {
        Log.i(TAG, "readLettersFromFile");
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
            din.close();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        System.out.println("size of grabbed_letters is ===== " + grabbed_letters.size());

        // Print out the hash map of grabbed letters for debugging
        for (String name: grabbed_letters.keySet()){
            String value = grabbed_letters.get(name).toString();
            System.out.println(name + " -- " + value);
        }
    }

    public void readWordsFromFile() {
        Log.i(TAG, "readWordsFromFile");
        try {
            FileInputStream input = openFileInput(WORDS_FILE);
            DataInputStream din = new DataInputStream(input);
            int sz = din.readInt(); // Read line count
            for (int i = 0; i < sz; i++) {
                String str = din.readUTF();
                //Log.v("read", str);
                String[] stringArray = str.split(",");
                String name = stringArray[0];
                int value = Integer.parseInt(stringArray[1]);
                words_created.put(name, value);
            }
            din.close();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        Log.i(TAG, " readWordsFrom File -- size of words_created is ===== " + words_created.size());

        // Print out the hash map of words created for debugging
        for (String name: words_created.keySet()){
            String value = words_created.get(name).toString();
            System.out.println(name + " -- " + value);
        }
    }

    public void readDictionary() {
        //Log.i(TAG, "readDictionary");
        InputStream is = getResources().openRawResource(R.raw.dictionary);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        try {
            while ((line = br.readLine()) != null) {
                dictionary.add(line.toUpperCase());
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void readScoreFromFile() {
        Log.i(TAG, "readScoreFromFile");
        try {
            FileInputStream input = openFileInput(SCORE_FILE);
            DataInputStream din = new DataInputStream(input);
            int sz = din.readInt(); // Read line count
            for (int i = 0; i < sz; i++) {
                String str = din.readUTF();
                Log.v("read", str);
                score = Integer.parseInt(str);
            }
            din.close();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        Log.i(TAG,"score of the user  ====  " + score);
    }
}
