package application.openmenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class Settings extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        // Textviews
        TextView connect = findViewById(R.id.connect);
        TextView mealDays = findViewById(R.id.mealDays);
        TextView adminLock = findViewById(R.id.adminLock);
        TextView about = findViewById(R.id.about);

        // create on click listeners
        connect.setOnClickListener(this);
        mealDays.setOnClickListener(this);
        adminLock.setOnClickListener(this);
        about.setOnClickListener(this);
    }

    /**
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            // load orders class
            case R.id.connect: {
                intent = new Intent(this, MysqlConnect.class);
                startActivity(intent);
                break;
            }
            case R.id.mealDays: {
                intent = new Intent(this, SettingsWeekDay.class);
                startActivity(intent);
                break;
            }
            case R.id.adminLock: {
                intent = new Intent(this, SettingsAdminLock.class);
                startActivity(intent);
                break;
            }
            case R.id.about: {
                intent = new Intent(this, About.class);
                startActivity(intent);
                break;
            }
        }
    }

    // catch if back button is pressed.
    public void onBackPressed(){
        Intent intent;
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
