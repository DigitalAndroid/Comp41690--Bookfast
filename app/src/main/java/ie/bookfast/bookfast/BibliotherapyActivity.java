//BIBLIOTHERAPYACTIVITY.JAVA
package ie.bookfast.bookfast;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;


/**
 * Created by keala_000 on 23/10/17.
 */

public class BibliotherapyActivity extends Activity {
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;
    DatabaseHelper myDb;
    EditText editName,editSurname,editMarks ,editTextId;
    Button btnAddData;
    Button btnviewAll;
    Button btnDelete;
    Button btnviewUpdate;


   // SharedPreferences sharedpreferences;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Book1 = "Jamie Oliver - '5 Ingredients - Quick & Easy Food'";
    public static final String Book2 = "Michael Mosely - 'The 8-Week Blood Sugar Diet: Lose weight fast and reprogramme your body'";
    public static final String Book3 = "Joe Wicks - 'Lean in 15 - The Shift Plan: 15 Minute Meals and Workouts to Keep You Lean and Healthy'";
    public static final String Book4 = "Michael Mosely - 'The Fast Diet: Lose Weight, Stay Healthy, Live Longer'";
    public static final String Book5 = "Leslie Kaimoff - 'Yoga Anatomy'";
    public static final String Book6 = "Frederic Delavier - 'Strength Training Anatomy'";
    public static final String Book7 = "Timothy Ferriss - 'The 4-Hour Body: An Uncommon Guide to Rapid Fat-loss, Incredible Sex and Becoming Superhuman'";
    public static final String Book8 = "Russell Brand - 'Recovery: Freedom From Our Addictions'";
    public static final String Book9 = "Ned Vizzini - 'It's Kind of a Funny Story'";
    public static final String Book10 = "Linda Naomi Katz - 'Surviving Mental Illness: My Story'";
    public static final String Book11 = "Ella Berthoud - 'The Novel Cure: From Abandonment to Zestlessness: 751 Books to Cure What Ails You'";
    public static final String Book12 = "Charlotte Moundlic - 'The Scar'";
    public static final String Book13 = "Viktor E. Frankl - 'Man's Search for Meaning'";
    public static final String Book14 = "Emma Seppälä - 'The Happiness Track: How to Apply the Science of Happiness to Accelerate Your Success'";
    public static final String Book15 = "Nigel Benson - 'The Psychology Book'";
    public static final String Book16 = "Dr. Tina Payne Bryson - 'The Whole-Brain Child: 12 Proven Strategies to Nurture Your Child’s Developing Mind'";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bibliotherapy);
        myDb = new DatabaseHelper(this);

        TextView txtView = (TextView) findViewById(R.id.text_id);

        listView = (ExpandableListView)findViewById(R.id.lvExp);
        initData();
        listAdapter = new ExpandableListAdapter(this,listDataHeader,listHash);
        listView.setAdapter(listAdapter); //end of expandable list

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent mIntent = new Intent(BibliotherapyActivity.this, BookDetail.class);
                mIntent.putExtra("ISBN",healthBookISBNs(listView).getString(Integer.toString(childPosition+(100*groupPosition)), "error"));
                startActivity(mIntent);
                return false;
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Bookfast");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

    }


    public SharedPreferences healthBookISBNs(ExpandableListView listView){

        SharedPreferences isbnSharedPreference;

        isbnSharedPreference = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = isbnSharedPreference.edit();
        //Healthy Diet
        editor.putString("0", "0718187725");                //5 Ingredients
        editor.putString("1", "1780722400");                //The 8-Week Blood Sugar Diet
        editor.putString("2", "1509800662");                //Lean in 15
        editor.putString("3", "9781501102011");            //The Fast Diet
        //Physical Health
        editor.putString("100", "1450400248");              //Yoga Anatomy
        editor.putString("101", "9780736092265");          //Strength Training Anatomy
        editor.putString("102", "0091939526");              //The 4-Hour Body
        //Mental Illness
        editor.putString("200", "9780786851973");          //It's Kind of a Funny Story
        editor.putString("201", "1432783998");              //Surviving Metal Illness
        //Self Help
        editor.putString("300", "1509844945");              //Recovery
        editor.putString("301", "9780062344014");          //The Happiness Track
        editor.putString("302", "1594205167");              //The Novel Cure
        //Psychology
        editor.putString("400", "1844132390");              //Man's Search for Meaning
        editor.putString("401", "1405391243");              //The Psychology Book
        //Child Psychology
        editor.putString("500", "0763653411");              //The Scar
        editor.putString("501", "1780338376");              //The Whole-Brain Child

        editor.commit();

        return isbnSharedPreference;
    }



    private void initData() {
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        listDataHeader.add("Healthy Diet");
        listDataHeader.add("Fitness & Exercise");
        listDataHeader.add("Mental Illness");
        listDataHeader.add("Self Help");
        listDataHeader.add("Psychology");
        listDataHeader.add("Child Psychology");

        List<String> healthyDiet = new ArrayList<>();
        healthyDiet.add(Book1);
        healthyDiet.add(Book2);
        healthyDiet.add(Book3);
        healthyDiet.add(Book4);

        List<String> fitnessAndExercise = new ArrayList<>();
        fitnessAndExercise.add(Book5);
        fitnessAndExercise.add(Book6);
        fitnessAndExercise.add(Book7);


        List<String> mentalIllness = new ArrayList<>();
        mentalIllness.add(Book9);
        mentalIllness.add(Book10);


        List<String> selfHelp = new ArrayList<>();
        selfHelp.add(Book8);
        selfHelp.add(Book14);
        selfHelp.add(Book11);

        List<String> psychology = new ArrayList<>();
        psychology.add(Book13);
        psychology.add(Book15);

        List<String> childPsychology = new ArrayList<>();
        childPsychology.add(Book12);
        childPsychology.add(Book16);





        listHash.put(listDataHeader.get(0),healthyDiet);
        listHash.put(listDataHeader.get(1),fitnessAndExercise);
        listHash.put(listDataHeader.get(2),mentalIllness);
        listHash.put(listDataHeader.get(3),selfHelp);
        listHash.put(listDataHeader.get(4),psychology);
        listHash.put(listDataHeader.get(5),childPsychology);
    }

}
