package kpi.obalitskyi.diploma;

/**
 * Created by obalitskyi on 5/30/17.
 */

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button start_test = (Button)findViewById(R.id.ma_b_start);
        start_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_start_test = new Intent(getApplicationContext(), TestActivity.class);
                startActivity(intent_start_test);
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


    /*ImageButton settings = (ImageButton) findViewById(R.id.at_ib_settings);
        settings.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // show preference activity

        }
    });*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_favorite:
                Intent intent_show_prefs = new Intent(getApplicationContext(), PrefsActivity.class);
                startActivity(intent_show_prefs);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
