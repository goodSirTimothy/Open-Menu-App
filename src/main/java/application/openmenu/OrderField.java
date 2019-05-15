package application.openmenu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Omega on 5/10/2018.
 */

public class OrderField extends AppCompatActivity implements View.OnClickListener{

    Log log;
    String[] iRoom, iOldData, sendOldData;
    String lay1Boxes, lay2Boxes, lay3Boxes, lay4Boxes, iGuest;
    boolean iExtra;
    TextView tvDiet, tvPrimaryFood, tvSide, tvDrink, tvDesserts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_field);

        setValues();
        // Get extras from the intent
        Bundle extras = getIntent().getExtras();
        // make sure there is an extra in the intent
        if(extras == null) {
            log.d("extras is null","There are no extras"  + "\n\n");
        } else {
            checkBundles(extras);
        }
    }

    private void checkBundles(Bundle extras) {
        iGuest = extras.getString("guest");
        iRoom = extras.getStringArray("roomInfo");
        if(iRoom != null) {
            TextView tvRoomNum = findViewById(R.id.tvRoomNum);
            TextView tvName = findViewById(R.id.tvName);
            tvRoomNum.setText("Room # " + iRoom[1]);
            tvName.setText(iRoom[3] + ", " + iRoom[4]);
            if (iRoom.length > 7) {
                tvDiet.setText("Food Restriction:\n\t" + iRoom[5]
                        + "\nFluid Restriction:\n\t" + iRoom[6]
                        + "\nOther Notes:\n\t" + iRoom[7]);
            } else {
                tvDiet.setText("Food Restriction:\n\t" + iRoom[5]
                        + "\nFluid Restriction:\n\t" + iRoom[6]);
            }
        } else if (iGuest!=null){
            TextView tvRoomNum = findViewById(R.id.tvRoomNum);
            TextView tvName = findViewById(R.id.tvName);
            tvRoomNum.setText("");
            tvName.setText(iGuest);
            tvDiet.setText("");
        }
        updateValues(extras);
        log.d("extras has info","extras: " + iRoom + "\n\n");
    }

    private void updateValues(Bundle extras){
        iOldData = extras.getStringArray("oldData");
        if(iOldData != null){
            tvPrimaryFood.setText(iOldData[0]);
            tvSide.setText(iOldData[1]);
            tvDrink.setText(iOldData[2]);
            tvDesserts.setText(iOldData[3]);
        }
        lay1Boxes = extras.getString("lay1Boxes");
        if(lay1Boxes != null){
            tvPrimaryFood.setText(lay1Boxes);
        }
        lay2Boxes = extras.getString("lay2Boxes");
        if(lay2Boxes != null){
            tvSide.setText(lay2Boxes);
        }
        lay3Boxes = extras.getString("lay3Boxes");
        if(lay3Boxes != null){
            tvDrink.setText(lay3Boxes);
        }
        lay4Boxes = extras.getString("lay4Boxes");
        if(lay4Boxes != null){
            tvDesserts.setText(lay4Boxes);
        }
    }

    private void setValues(){
        TextView tvRoomNum = findViewById(R.id.tvRoomNum);
        TextView tvName = findViewById(R.id.tvName);
        LinearLayout layNameNum = findViewById(R.id.layNameNum);
        tvDiet = findViewById(R.id.tvDiet);
        tvPrimaryFood = findViewById(R.id.tvPrimary);
        LinearLayout layPrimaryFood = findViewById(R.id.layPrimary);
        tvSide = findViewById(R.id.tvSides);
        LinearLayout laySide = findViewById(R.id.laySides);
        tvDrink = findViewById(R.id.tvDrink);
        LinearLayout layDrink = findViewById(R.id.layDrink);
        tvDesserts = findViewById(R.id.tvDesserts);
        LinearLayout layDessert = findViewById(R.id.layDesserts);

        Button send = findViewById(R.id.sendBtn);

        tvRoomNum.setOnClickListener(this);
        tvName.setOnClickListener(this);
        layNameNum.setOnClickListener(this);
        layPrimaryFood.setOnClickListener(this);
        laySide.setOnClickListener(this);
        layDrink.setOnClickListener(this);
        layDessert.setOnClickListener(this);
        send.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent iMenuControl;
        switch (view.getId()){
            case R.id.tvRoomNum:{
                iMenuControl = new Intent(this, Rooms.class);
                iMenuControl.putExtra("location", "location");
                sendOldData = new String[]{tvPrimaryFood.getText().toString(), tvSide.getText().toString(),
                        tvDrink.getText().toString(), tvDesserts.getText().toString()};
                iMenuControl.putExtra("oldData", sendOldData);
                startActivity(iMenuControl);
                break;
            }
            case R.id.tvName:{
                iMenuControl = new Intent(this, Rooms.class);
                iMenuControl.putExtra("location", "location");
                sendOldData = new String[]{tvPrimaryFood.getText().toString(), tvSide.getText().toString(),
                        tvDrink.getText().toString(), tvDesserts.getText().toString()};
                iMenuControl.putExtra("oldData", sendOldData);
                startActivity(iMenuControl);
                break;
            }
            case R.id.layNameNum:{
                iMenuControl = new Intent(this, Rooms.class);
                iMenuControl.putExtra("location", "location");
                sendOldData = new String[]{tvPrimaryFood.getText().toString(), tvSide.getText().toString(),
                        tvDrink.getText().toString(), tvDesserts.getText().toString()};
                iMenuControl.putExtra("oldData", sendOldData);
                startActivity(iMenuControl);
                break;
            }
            case R.id.layScroll:{
                break;
            }
            case R.id.layPrimary: {
                iMenuControl = new Intent(this, Menu.class);
                iMenuControl.putExtra("menuType", "primary");
                iMenuControl.putExtra("roomInfo", iRoom);
                sendOldData = new String[]{tvPrimaryFood.getText().toString(), tvSide.getText().toString(),
                        tvDrink.getText().toString(), tvDesserts.getText().toString()};
                iMenuControl.putExtra("oldData", sendOldData);
                startActivity(iMenuControl);
                break;
            }
            case R.id.laySides: {
                iMenuControl = new Intent(this, Menu.class);
                iMenuControl.putExtra("menuType", "sides");
                iMenuControl.putExtra("roomInfo", iRoom);
                sendOldData = new String[]{tvPrimaryFood.getText().toString(), tvSide.getText().toString(),
                        tvDrink.getText().toString(), tvDesserts.getText().toString()};
                iMenuControl.putExtra("oldData", sendOldData);
                startActivity(iMenuControl);
                break;
            }
            case R.id.layDrink: {
                iMenuControl = new Intent(this, Menu.class);
                iMenuControl.putExtra("menuType", "drink");
                iMenuControl.putExtra("roomInfo", iRoom);
                sendOldData = new String[]{tvPrimaryFood.getText().toString(), tvSide.getText().toString(),
                        tvDrink.getText().toString(), tvDesserts.getText().toString()};
                iMenuControl.putExtra("oldData", sendOldData);
                startActivity(iMenuControl);
                break;
            }
            case R.id.layDesserts: {
                iMenuControl = new Intent(this, Menu.class);
                iMenuControl.putExtra("menuType", "dessert");
                iMenuControl.putExtra("roomInfo", iRoom);
                sendOldData = new String[]{tvPrimaryFood.getText().toString(), tvSide.getText().toString(),
                        tvDrink.getText().toString(), tvDesserts.getText().toString()};
                iMenuControl.putExtra("oldData", sendOldData);
                startActivity(iMenuControl);
                break;
            }
            case R.id.sendBtn: {
                if(iRoom != null) {
                    final QuerySubmitActivity submit = new QuerySubmitActivity(this);
                    final Context context = this;
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("Are you sure you want send the information?");
                    alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int id) {
                            DayAndWeekLogic getInfo = new DayAndWeekLogic();
                            saveAndLoad load = new saveAndLoad();
                            submit.execute("submit", load.getServerURL(context), load.getDatabaseURL(context), load.getDatabaseName(context),
                                    load.getUsername(context), load.getPassword(context), load.getPort(context), "/insertOrders.php", iRoom[1],
                                    tvPrimaryFood.getText().toString(), tvSide.getText().toString(), tvDrink.getText().toString(), tvDesserts.getText().toString(),
                                    getInfo.getMonth(), getInfo.getDay(), getInfo.getYear(), "1", "0");
                        }
                    });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int id) {

                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else if (iGuest!=null){
                    TextView tvRoomNum = findViewById(R.id.tvRoomNum);
                    TextView tvName = findViewById(R.id.tvName);
                    tvRoomNum.setText("");
                    tvName.setText(iGuest);
                } else {

                    Toast.makeText(this,"Please select a room or guest.",Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    public void onBackPressed(){
        final Context context = this;
        // do something here and don't write super.onBackPressed()
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to go back?\nInformation my be lost.");
        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                Intent intent;
                intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
