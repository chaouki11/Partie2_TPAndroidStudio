package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button contactidbtn;
    Button btndetail;
    Button btncall;
    TextView resulttxt;
    private static final int PICK_CONTACT_REQUEST = 1;  // The request code

    private int Perm_CTC;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS},
                Perm_CTC);
        resulttxt = (TextView) findViewById(R.id.resulttxt);
        contactidbtn = (Button) findViewById(R.id.contactidbtn);
        btncall = (Button) findViewById(R.id.btncall);
        btndetail = (Button) findViewById(R.id.btndetail);
        btndetail.setEnabled(false);
        btncall.setEnabled(false);
        contactidbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent,1);

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check which request we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {

            if (resultCode == RESULT_OK) {
                //Uri contactUri = data.getData();
                Toast.makeText(this, "contact sélectionné", Toast.LENGTH_SHORT).show();
                 String contactUriString = data.getData().toString();
                Uri uriContact = Uri.parse(contactUriString);

                resulttxt.setText(contactUriString);
                btndetail.setEnabled(true);
                btndetail.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("Range")
                    @Override
                    public void onClick(View view) {
                        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String Id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        String contactID = "null";
                        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " + ContactsContract.CommonDataKinds.Phone.TYPE + " = " + ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE, new String[]{contactID}, null);
                        cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                });


            }
            else {
                Toast.makeText(this, "contact non sélectionné", Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    public void onRequestPermissionsResult
            (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //check the permission type using the requestCode if (requestCode == Perm_CTC)
        // { //the array is empty if not granted
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "GRANTED CALL", Toast.LENGTH_SHORT).show();
        } }

}
