package theofanous.kendeas.grabble.grabble;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static android.graphics.Color.rgb;

public class CollectionOfLettersActivity extends AppCompatActivity {
    private String TAG = "CollectionOfLetters";
    //private static Button dictionary;

    // Letters from MapsActivity
    Map<String, Integer> grabbed_letters = new HashMap<>();
    File letters_file;
    String LETTERS_FILE = "letters_file.txt";

    // Score
    int score;
    File score_file;
    String SCORE_FILE = "score_file.txt";
    TextView score_textView;

    // Word and values file and map
    Map<String, Integer> words_created = new HashMap<>();
    File words_file;
    String WORDS_FILE = "words_file.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_of_letters);
        buttons();

        letters_file = new File(this.getFilesDir(), LETTERS_FILE);
        System.out.println(letters_file.length() + "  size of letters_file >>>>>>>>>>>>>>>>>>>>>>");

        score_file = new File(this.getFilesDir(), SCORE_FILE);
        System.out.println(score_file.length() + "  size of score_file <<<<<<<<<<<<<<<<<<<<<<");

        score_textView = (TextView) findViewById(R.id.score);

        words_file = new File(this.getFilesDir(), WORDS_FILE);
        System.out.println(words_file.length() + "  size of words_file <<<<<<<<<<<<<<<<<<<<<<");


        readLettersFromFile(); // and fill the hashmap
        readScoreFromFile();   // and fill the int
        readWordsFromFile();   // and fill the hashmap
        putScore();            // show the score to the user
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
        // write score to file
        try {
            FileOutputStream output = openFileOutput(SCORE_FILE,
                    Context.MODE_PRIVATE);
            DataOutputStream dout = new DataOutputStream(output);
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

    public void putScore() {
        if (score_file.length() == 0) {
            score_textView.setText("0");
        } else {
            score_textView.setText(String.valueOf(score));
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

    public void readLettersFromFile(){
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

    public void readDictionary() {
        Log.i(TAG, "readDictionary");
        Dialog dialog = new Dialog(CollectionOfLettersActivity.this, android.R.style.Theme_Light);
        dialog.setTitle("Official Grabble Dictionary 2016");
        dialog.setContentView(R.layout.dictionary_layout);
        // Add the ListView
        ListView listView = (ListView) dialog.findViewById(R.id.words_list);
        InputStream is = getResources().openRawResource(R.raw.dictionary);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        try {
            ArrayList<String> lines = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(CollectionOfLettersActivity.this, android.R.layout.simple_list_item_1,lines);
            listView.setAdapter(adapter);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dialog.show();
    }

    public void createLettersTable() {
        Log.i(TAG, "createLettersTable");
        // Create an array for the alphabet. Will be used to fill the table with the letters
        char[] alphabet = new char[26];
        //int k = 0;
        for(int i = 0; i < 26; i++){
            // CAPS
            alphabet[i] = (char)(65 + i);
        }

        Dialog dialog = new Dialog(CollectionOfLettersActivity.this, android.R.style.Theme_Light);
        dialog.setTitle("Number of Letters you collected:");
        dialog.setContentView(R.layout.letters_layout);

        // Add left table of letters collected
        final TableLayout letters_table_left = (TableLayout)dialog.findViewById(R.id.letters_table_left);
        letters_table_left.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
        letters_table_left.setGravity(View.TEXT_ALIGNMENT_CENTER);
        //letters_table_left.setBackground(); // make a drawable???

        for (int i = 0; i < 26; i++){
            // Creation row
            final TableRow tableRow = new TableRow(this);
            //tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
            tableRow.setBackgroundColor(Color.DKGRAY);
            tableRow.setPadding(0,0,0,2); // border ?
            TableRow.LayoutParams llp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            llp.setMargins(0,0,2,0);

            // New letter_cell
            LinearLayout letter_cell = new LinearLayout(this);
            if (i % 2 == 0) {
                letter_cell.setBackgroundColor(Color.WHITE);
            } else {
                letter_cell.setBackgroundColor(Color.GRAY);
            }
            letter_cell.setLayoutParams(llp);//2px border on the right for theletter_cell
            
            // Creation textView of Letter
            final TextView letter = new TextView(this);
            letter.setText(String.valueOf(alphabet[i]));
            letter.setTypeface(Typeface.SERIF, Typeface.BOLD);
            letter.setTextSize(15);
            letter.setPadding(20,0,4,3);
            
            letter_cell.addView(letter);

            // New number cell
            LinearLayout number_cell = new LinearLayout(this);
            if (i % 2 == 0) {
                number_cell.setBackgroundColor(Color.WHITE);
            } else {
                number_cell.setBackgroundColor(Color.GRAY);
            }
            number_cell.setLayoutParams(llp);//2px border on the right for theletter_cell

            // Find number of letters collected
            final TextView number = new TextView(this);
            if (grabbed_letters.containsKey(Character.toString(alphabet[i]))){
                number.setText(String.valueOf(grabbed_letters.get(Character.toString(alphabet[i]))));
            } else {
                number.setText("0");
            }
            number.setTypeface(Typeface.SERIF, Typeface.BOLD);
            number.setTextSize(15);
            number.setPadding(20,0,4,3);
            
            number_cell.addView(number);
            
            tableRow.addView(letter_cell);
            tableRow.addView(number_cell);

            letters_table_left.addView(tableRow);
        }
        letters_table_left.setStretchAllColumns(true);
        //letters_table_left.setShrinkAllColumns(true);
        dialog.show();
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

    // Sort a hashmap in descending order. Used to sort the hashmap of words created such that they
    // are shown in the history, starting from the highest value word.
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map ) {
        List<Map.Entry<K, V>> list =
                new LinkedList<Map.Entry<K, V>>( map.entrySet() );
        Collections.sort( list, new Comparator<Map.Entry<K, V>>() {
            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
            {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        } );

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }

    public void showHistory() {
        Log.i(TAG, "showHistory");
        Dialog dialog = new Dialog(CollectionOfLettersActivity.this, android.R.style.Theme_Light);
        dialog.setTitle("History and Scores");
        LayoutInflater factory = LayoutInflater.from(CollectionOfLettersActivity.this);
        final View view = factory.inflate(R.layout.history_layout, null);
        dialog.setContentView(view);

        // Add left table of letters collected
        final TableLayout letters_table_left = (TableLayout)dialog.findViewById(R.id.history_words_table);
        letters_table_left.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
        letters_table_left.setGravity(View.TEXT_ALIGNMENT_CENTER);

        Map<String, Integer> sorted_words = sortByValue(words_created);

        int i = 0;
        for (String word : sorted_words.keySet()){
            // Creation row
            final TableRow tableRow = new TableRow(this);
            //tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
            tableRow.setBackgroundColor(Color.DKGRAY);
            tableRow.setPadding(0, 0, 0, 2); // border ?
            TableRow.LayoutParams llp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            llp.setMargins(0, 0, 2, 0);

            // New letter_cell
            LinearLayout letter_cell = new LinearLayout(this);
            if (i % 2 == 0) {
                letter_cell.setBackgroundColor(Color.WHITE);
            } else {
                letter_cell.setBackgroundColor(Color.WHITE);
            }
            letter_cell.setLayoutParams(llp);//2px border on the right for the letter_cell

            // Creation textView of Letter
            final TextView letter = new TextView(this);
            letter.setTypeface(Typeface.SERIF, Typeface.BOLD);
            letter.setTextSize(15);
            letter.setPadding(20, 0, 4, 3);

            // set 1,2,3
            switch (i) {
                case 0:
                    letter.setText(String.format("1.  %s", word));
                    letter.setTextColor(Color.rgb(255,215,0));
                    break;
                case 1:
                    letter.setText(String.format("2.  %s", word));
                    letter.setTextColor(Color.rgb(192,192,192));
                    break;
                case 2:
                    letter.setText(String.format("3.  %s", word));
                    letter.setTextColor(Color.rgb(218,165,32));
                    break;
                default:
                    letter.setText(String.format("     %s", word));
                    break;
            }

            letter_cell.addView(letter);

            // New number cell
            LinearLayout number_cell = new LinearLayout(this);
            if (i % 2 == 0) {
                number_cell.setBackgroundColor(Color.WHITE);
            } else {
                number_cell.setBackgroundColor(Color.WHITE);
            }
            number_cell.setLayoutParams(llp);//2px border on the right for theletter_cell

            // Find number of letters collected
            final TextView number = new TextView(this);
            number.setText(String.valueOf(sorted_words.get(word)));

            number.setTypeface(Typeface.SERIF, Typeface.BOLD);
            number.setTextSize(15);
            number.setPadding(20, 0, 4, 3);

            number_cell.addView(number);

            tableRow.addView(letter_cell);
            tableRow.addView(number_cell);

            letters_table_left.addView(tableRow);
            i++;

        }
        letters_table_left.setStretchAllColumns(true);
        //letters_table_left.setShrinkAllColumns(true);
        dialog.show();
    }

    public void buttons() {
        Button dictionary = (Button)findViewById(R.id.dictionary_button);
        Button letters = (Button)findViewById(R.id.letters_button);
        Button history = (Button)findViewById(R.id.history_button);

        dictionary.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.i(TAG, "Dictionary Button was clicked!");
                if (score > 500) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CollectionOfLettersActivity.this);
                    builder.setMessage("You will be deducted 500pts! Do you want to proceed?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    score -= 500;
                                    score_textView.setText(String.valueOf(score));
                                    readDictionary();
                                }
                            })
                            .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    Toast.makeText(CollectionOfLettersActivity.this, "Dictionary is locked! Have you reached the next 500 checkpoint?", Toast.LENGTH_SHORT).show();

                }
            }
        });

        letters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Letters Button was clicked!");
                createLettersTable();
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "History Button was clicked!");
                showHistory();
            }
        });

    }

}
