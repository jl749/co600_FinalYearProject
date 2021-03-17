package com.example;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Profile extends Activity {

    private String m_Text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yourprofile);
        Button setPostcode = (Button) findViewById(R.id.setPostcode);
        setPostcode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                inputDialog();
            }
        });
    }

    private void inputDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Input postcode");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                updatePostCode(m_Text);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void updatePostCode(String postCode){
        ((MyApplication)this.getApplication()).setPostCode(postCode);
        String houseID = ((MyApplication)this.getApplication()).getCurrentHouse();
        UpdatePostcode upc = new UpdatePostcode();
        upc.run(houseID,postCode);
    }

}