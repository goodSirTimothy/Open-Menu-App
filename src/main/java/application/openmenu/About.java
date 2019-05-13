package application.openmenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class About extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        /*
        // Textviews
        TextView connect = findViewById(R.id.connect);
        TextView adminLock = findViewById(R.id.adminLock);
        TextView about = findViewById(R.id.about);

        // create on click listeners
        connect.setOnClickListener(this);
        adminLock.setOnClickListener(this);
        about.setOnClickListener(this);
        */
    }

    @Override
    public void onClick(View view) {

    }

    // catch if back button is pressed.
    public void onBackPressed(){
        Intent intent;
        intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
}
