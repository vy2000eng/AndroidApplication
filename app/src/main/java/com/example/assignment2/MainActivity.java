package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    RadioButton averageCase, WorstCase;
    int whichArray = 0;
    int whichDataStructure = -1;
    private static final int CLICKED_ICON = R.drawable.email_icon;
    private static final int ORIGINAL_ICON = R.drawable.edit_pencil_icon;
     boolean checkToSendEmail = false;

    String[][] worstCaseArray = {
            {"Get Min: O(log(n))", "Insert: O(log(n))", "Search: O(log(n))"},//2-3 tree
            {"Get Min: O(n)", "Insert: O(n)", "Search: O(n)"},        //bst
            {"Get Min: O(n)", "Insert: O(n)", "Search: O(n)"},        //hash-table
            {"Get Min: O(n)", "Insert: O(1)", "Search: O(n)"},         //linked-list
            {"Get Min: O(1)", "Insert: O(log(n))", "Search: O(n)"}                  //min-heap
    };
    String[][] averageCaseArray = {
            {"Get Min: O(log(n))", "Insert: O(log(n))", "Search: O(log(n))"},//2-3 tree
            {"Get Min: O(log(n))", "Insert: O(log(n))", "Search: O(log(n))"},        //bst
            {"Get Min: O(1)", "Insert: O(1)", "Search: O(1)"},        //hash-table
            {"Get Min: O(n)", "Insert: O(1)", "Search: O(n)"},         //linked-list
            {"Get Min: O(1)", "Insert: O(1)", "Search: O(n)"}                  //min-heap
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Log.d("life_cycle", "onCreate invoked");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Spinner spinner = findViewById(R.id.dataStructureSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.data_structures_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Toolbar myToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolBar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_save);
        menuItem.setOnMenuItemClickListener(item -> {
            Log.d("SEND EMAIL", String.valueOf(checkToSendEmail));
            //checkToSendEmail = true;
            if(checkToSendEmail){
                EditText EmailAddress = findViewById(R.id.editTextTextEmailAddress2);
                EditText SubjectOfEmailAddress = findViewById(R.id.editTextTextEmailAddress3);
                String subject = String.valueOf(SubjectOfEmailAddress.getText());
                String email = EmailAddress.getText().toString();
                Log.d("EMAIL", email);
                String content = buttonClick(item.getActionView());

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT,subject );
                emailIntent.putExtra(Intent.EXTRA_TEXT, content);
                startActivity(emailIntent);
                checkToSendEmail = false;
                menuItem.setIcon(ORIGINAL_ICON);
            }
            else{
                checkToSendEmail = true;
                Log.d("SEND EMAIL", String.valueOf(checkToSendEmail));

                menuItem.setIcon(CLICKED_ICON);
                buttonClick(item.getActionView());
            }
            return true;
        });

        return true;
    }



    public String buttonClick(View Button) {
        EditText EmailAddress = findViewById(R.id.editTextTextEmailAddress2);
        EditText SubjectOfEmailAddress = findViewById(R.id.editTextTextEmailAddress3);
        Spinner dataStructure = findViewById(R.id.dataStructureSpinner);
        averageCase = findViewById(R.id.averageCaseRB);
        WorstCase = findViewById(R.id.WorstCaseRB);

        boolean getMinCheckBox = ((CheckBox) findViewById(R.id.getMinCheckBox)).isChecked();
        boolean insertCheckBox = ((CheckBox) findViewById(R.id.insertCheckBox)).isChecked();
        boolean searchCheckBox = ((CheckBox) findViewById(R.id.searchCheckBox)).isChecked();
        boolean[] whichCheckBoxes = {getMinCheckBox, insertCheckBox, searchCheckBox};


        String contentOfEmailAddress = "To: " + EmailAddress.getText().toString();
        String contentOfSubjectOfEmailAddress = "Subject: " + SubjectOfEmailAddress.getText().toString();
        String prefixForMessage = "Time Complexity for ";
        String contentOfDataStructure = dataStructure.getSelectedItem().toString();
        String runningTime = (averageCase.isChecked()) ? "Average Case " : (WorstCase.isChecked()) ? "Worst Case " : "";
        StringBuilder outputOfSelectedOperations = new StringBuilder();
        whichArray = (averageCase.isChecked()) ? 0 : 1;
        String[][] currentArray = whichArray == 0 ? averageCaseArray : worstCaseArray;
        whichDataStructure = contentOfDataStructure.equals("2–3 Tree") ? 0 :
                contentOfDataStructure.equals("Binary Search Tree") ? 1 :
                        contentOfDataStructure.equals("Hash Table") ? 2 :
                                contentOfDataStructure.equals("Linked List") ? 3 : 4;

        for (int i = 0; i < whichCheckBoxes.length; i++) {
            if (whichCheckBoxes[i]) {
                outputOfSelectedOperations.append("\t").append(currentArray[whichDataStructure][i]).append('\n');
            }
        }
        String contentOfMessage =
                runningTime +
                        prefixForMessage +
                        contentOfDataStructure +
                        ":" + '\n'
                        + outputOfSelectedOperations;


        TextView setContentOfEmailAddress = findViewById(R.id.recieverOfText);
        TextView setContentOfSubjectOfEmailAddress = findViewById(R.id.whatsItAbout);
        TextView setContentOfDataStructure = findViewById(R.id.content);

        setContentOfEmailAddress.setText(contentOfEmailAddress);
        setContentOfSubjectOfEmailAddress.setText(contentOfSubjectOfEmailAddress);
        setContentOfDataStructure.setText(contentOfMessage);
        return contentOfMessage;

    }

}