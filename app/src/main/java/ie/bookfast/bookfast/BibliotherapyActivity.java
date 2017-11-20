//BIBLIOTHERAPYACTIVITY.JAVA
package ie.bookfast.bookfast;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.app.Activity;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;

import android.app.AlertDialog;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    public static final String Book6 = "Michael Matthews - 'Bigger Leaner Stronger: The Simple Science of Building the Ultimate Male Body'";
    public static final String Book7 = "Timothy Ferriss - 'The 4-Hour Body: An Uncommon Guide to Rapid Fat-loss, Incredible Sex and Becoming Superhuman'";
    public static final String Book8 = "Russell Brand - 'Recovery: Freedom From Our Addictions'";
    public static final String Book9 = "Ned Vizzini - 'It's Kind of a Funny Story'";
    public static final String Book10 = "Linda Naomi Katz - 'Surviving Mental Illness: My Story'";
    public static final String Book11 = "Ella Berthoud - 'The Novel Cure: From Abandonment to Zestlessness: 751 Books to Cure What Ails You'";
    public static final String Book12 = "Charlotte Moundlic - 'The Scar'";
    public static final String Book13 = "Viktor E. Frankl - 'Man's Search for Meaning'";
    public static final String Book14 = "Dan Harris - '10% Happier: How I Tamed the Voice in My Head, Reduced Stress Without Losing My Edge, and Found Self-Help That Actually Works'";
    public static final String Book15 = "Nigel Benson - 'The Psychology Book'";
    public static final String Book16 = "Dr. Tina Payne Bryson - 'The Whole-Brain Child: 12 Proven Strategies to Nurture Your Child’s Developing Mind'";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bibliotherapy);
        myDb = new DatabaseHelper(this);

        /*Button testBtn=(Button) findViewById(R.id.testBtn);;

        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(BibliotherapyActivity.this, BookDetail.class);
                mIntent.putExtra("ISBN",healthBooks().getString(Book1, "error"));
                startActivity(mIntent);
            }
        });*/
/*
        editName = (EditText)findViewById(R.id.editText_name);
        editSurname = (EditText)findViewById(R.id.editText_surname);
        editMarks = (EditText)findViewById(R.id.editText_Marks);
        editTextId = (EditText)findViewById(R.id.editText_id);
        btnAddData = (Button)findViewById(R.id.button_add);
        btnviewAll = (Button)findViewById(R.id.button_viewAll);
        btnviewUpdate= (Button)findViewById(R.id.button_update);
        btnDelete= (Button)findViewById(R.id.button_delete);
        AddData();
        viewAll();
        UpdateData();
        DeleteData();*/

//expandablelist

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
    }


    public SharedPreferences healthBookISBNs(ExpandableListView listView){

        SharedPreferences isbnSharedPreference;

        isbnSharedPreference = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = isbnSharedPreference.edit();
        //Healthy Diet
        editor.putString("0", "0718187725");                //5 Ingredients
        editor.putString("1", "1780722400");                //The 8-Week Blood Sugar Diet
        editor.putString("2", "1509800662");                //Lean in 15
        editor.putString("3", "978-1780722375");            //The Fast Diet
        //Physical Health
        editor.putString("100", "1450400248");              //Yoga Anatomy
        editor.putString("101", "978-1938895272");          //Bigger Leaner Stronger
        editor.putString("102", "0091939526");              //The 4-Hour Body
        //Mental Illness
        editor.putString("200", "078685197X");              //It's Kind of a Funny Story
        editor.putString("201", "1432783998");              //Surviving Metal Illness
        //Self Help
        editor.putString("300", "1509844945");              //Recovery
        editor.putString("301", "0062265423");              //10% Happier
        editor.putString("302", "1594205167");              //The Novel Cure
        //Psychology
        editor.putString("400", "080701429X");              //Man's Search for Meaning
        editor.putString("401", "1405391243");              //The Psychology Book
        //Child Psychology
        editor.putString("500", "0763653411");              //The Scar
        editor.putString("501", "1780338376");              //The Whole-Brain Child

        editor.commit();

        return isbnSharedPreference;
    }

/*
    public void DeleteData() {
        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer deletedRows = myDb.deleteData(editTextId.getText().toString());
                        if(deletedRows > 0)
                            Toast.makeText(BibliotherapyActivity.this,"Data Deleted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(BibliotherapyActivity.this,"Data not Deleted",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
    public void UpdateData() {
        btnviewUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isUpdate = myDb.updateData(editTextId.getText().toString(),
                                editName.getText().toString(),
                                editSurname.getText().toString(),editMarks.getText().toString());
                        if(isUpdate == true)
                            Toast.makeText(BibliotherapyActivity.this,"Data Update",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(BibliotherapyActivity.this,"Data not Updated",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
    public  void AddData() {
        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = myDb.insertData(editName.getText().toString(),
                                editSurname.getText().toString(),
                                editMarks.getText().toString() );
                        if(isInserted == true)
                            Toast.makeText(BibliotherapyActivity.this,"Data Inserted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(BibliotherapyActivity.this,"Data not Inserted",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void viewAll() {
        btnviewAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor res = myDb.getAllData();
                        if(res.getCount() == 0) {
                            // show message
                            showMessage("Error","Nothing found");
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("Id :"+ res.getString(0)+"\n");
                            buffer.append("Name :"+ res.getString(1)+"\n");
                            buffer.append("Surname :"+ res.getString(2)+"\n");
                            buffer.append("Marks :"+ res.getString(3)+"\n\n");
                        }

                        // Show all data
                        showMessage("Data",buffer.toString());
                    }
                }
        );
    }

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

*/
// expandablelist

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
    //end of expandablelist
}
