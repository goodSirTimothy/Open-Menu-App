package application.openmenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class Rooms extends AppCompatActivity implements View.OnClickListener {
    String[] iOldData, oldRoomInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rooms);
        checkBundles();
        Button guestOrder = findViewById(R.id.guestClicked);
        Button submit = findViewById(R.id.submit);

        saveAndLoad load = new saveAndLoad();
        QueryRoomActivity signing = new QueryRoomActivity(this);
        String condition = "login";
        signing.execute(condition, load.getServerURL(this), load.getDatabaseURL(this), load.getDatabaseName(this),
                load.getUsername(this), load.getPassword(this), load.getPort(this), "/getRoom.php");

        guestOrder.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    void checkBundles(){
        // Get extras from the intent
        Bundle extras = getIntent().getExtras();
        // make sure there is an extra in the intent
        if(extras == null) {
            Log.d("extras is null","There are no extras"  + "\n\n");
        } else {
            oldRoomInfo = extras.getStringArray("roomInfo");
            iOldData = extras.getStringArray("oldData");
            Log.d("extras has info","oldData = " + iOldData);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            // load orders class
            case R.id.guestClicked: {
                LinearLayout guestLayout = findViewById(R.id.guestLayout);
                if(guestLayout.getVisibility() == View.VISIBLE){
                    guestLayout.setVisibility(View.GONE);
                    findViewById(R.id.bfastTable).setVisibility(View.VISIBLE);
                } else if(guestLayout.getVisibility() == View.GONE){
                    guestLayout.setVisibility(View.VISIBLE);
                    findViewById(R.id.bfastTable).setVisibility(View.GONE);
                }
                // Toast.makeText(getApplicationContext(),"Test press on Order",Toast.LENGTH_LONG).show();
                break;
            }

            case R.id.submit: {
                EditText guestName = findViewById(R.id.guestName);
                intent = new Intent(this, OrderField.class);
                intent.putExtra("guest", guestName.getText().toString());
                intent.putExtra("oldData", iOldData);
                startActivity(intent);
                // Toast.makeText(getApplicationContext(),"Test press on Order",Toast.LENGTH_LONG).show();
                break;
            }

        }
    }

    // catch if back button is pressed.
    public void onBackPressed(){
        Intent intent;
        intent = new Intent(this, OrderField.class);
        intent.putExtra("roomInfo", oldRoomInfo);
        intent.putExtra("oldData", iOldData);
        startActivity(intent);
    }
}
