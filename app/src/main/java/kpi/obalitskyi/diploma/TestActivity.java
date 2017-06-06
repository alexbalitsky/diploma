package kpi.obalitskyi.diploma;

/**
 * Created by obalitskyi on 5/30/17.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;


public class TestActivity extends Activity {

    private static final String TEST_FILE_NAME = "test.jpg";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        final EyeTestView etv = (EyeTestView) findViewById(R.id.at_etv);
        // load bitmap from resources
        etv.setBitmapResource(R.drawable.test);

        Button send = (Button) findViewById(R.id.at_b_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences(PrefsActivity.default_preferences_name, Context.MODE_PRIVATE);
                String to = prefs.getString("pref_doctor_email", "");

                // make image file for to send
                makeAttachement(etv.getBitmap());
                // send picture to doctor
                // make email intent
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("application/image");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{to});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, prefs.getString("email_subject", ""));
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, prefs.getString("email_text", ""));

                // attach our test image
                emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + getExternalCacheDir() + "/" + TEST_FILE_NAME));
                emailIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                // time to send the mail
                startActivity(Intent.createChooser(emailIntent, "Виберіть програму:"));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void makeAttachement(Bitmap bitmap) {
        try {
            //create a file to write bitmap data
            File f = new File(getExternalCacheDir() + "/", TEST_FILE_NAME);
            f.createNewFile();

            //Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);

            // next 2 functions do nothing in current implementation
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // should not be errors here, it's just because some file related functions required it
            e.printStackTrace();
        }
    }
}