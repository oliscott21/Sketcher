package com.example.sketcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawingFragment df = new DrawingFragment();
        df.setContainerActivity(this);
        Bundle t = new Bundle();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frag_view, df);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void onContactClick(View view) {
        TextView tv = (TextView) view;
        String id = tv.getText().toString().split(": ")[1];
        String contact = "";

        Cursor email = getContentResolver().query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + id, null, null);
        while (email.moveToNext()) {
            contact = email.getString(email.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.ADDRESS));
        }
        email.close();

        String temp = "image.png";
        File file = null;
        try {
            file = new File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES), temp);
            //file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(
                "vnd.android.cursor.dir/email"
        );
        intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {
                contact
        });
        assert file != null;
        Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID +
                        ".elester32_PA8", file);
        intent.putExtra(android.content.Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }


}