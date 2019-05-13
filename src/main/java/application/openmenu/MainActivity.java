package application.openmenu;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity  extends AppCompatActivity implements View.OnClickListener {
    TextView connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Textviews
        TextView orders = findViewById(R.id.orders);
        TextView location = findViewById(R.id.location);
        TextView menu = findViewById(R.id.menu);
        TextView settings = findViewById(R.id.settings);
        connection = findViewById(R.id.connection);

        // create on click listeners
        orders.setOnClickListener(this);
        menu.setOnClickListener(this);
        location.setOnClickListener(this);
        settings.setOnClickListener(this);
        connection.setOnClickListener(this);

        // Ping the server to make sure there is a connection (sometimes the first ping won't be received)
        pingConnection();

        // check month
        DayAndWeekLogic checkMenuDay = new DayAndWeekLogic();
        checkMenuDay.calculateChanges(this);
    }

    /**
     * Ping the server. If the Raspberry Pi/Server sleeps, it usually takes at least 2 pings for the database
     * to respond. I ping it 3 times just to be safe. I also only ping 3 times so it doesn't use up
     * too much memory.
     */
    void pingConnection(){
        int countCheckAmount = 0;
        while (!"Connected".equals(connection.getText().toString()) && countCheckAmount < 3){
            saveAndLoad load = new saveAndLoad();
            PingActivity ping = new PingActivity(this);
            ping.execute("ping", load.getServerURL(this), load.getDatabaseURL(this), load.getDatabaseName(this),
                    load.getUsername(this), load.getPassword(this), load.getPort(this), "/ping.php");
            countCheckAmount++;
        }
    }

    /**
     *
     * @param view
     */
    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            // load orders class
            case R.id.menu: {
                if(!"Connected".equals(connection.getText().toString())){
                    alertBox(Menu.class);
                } else {
                    intentExecute(Menu.class);
                    // Toast.makeText(getApplicationContext(),"Test press on Order",Toast.LENGTH_LONG).show();
                }
                break;
            }
            case R.id.orders: {
                intentExecute(OrderField.class);
                break;
            }
            case R.id.location: {
                if(!"Connected".equals(connection.getText().toString())){
                    alertBox(Rooms.class);
                } else {
                    intentExecute(Rooms.class);
                }
                break;
            }
            case R.id.settings: {
                saveAndLoad check = new saveAndLoad();
                /*
                check.checkIfFileExists("server.info", this);
                check.checkIfFileExists("user.info", this);
                check.checkIfFileExists("admin.info", this);
                */
                if(check.checkIfFileExists("admin.info", this)){
                    intentExecute(AdminLockLogin.class);
                } else {
                    intentExecute(SettingsAdminLock.class);
                }
                break;
            }
            case R.id.connection: {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Ping the server?");
                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        connection.setText("Connection Test");
                        pingConnection();
                    }
                });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            }
        }
    }

    /**
     * If there is no connection, ping the server. If a connection is found continue to the class
     * if the connection is not found, ask if they want to proceed anyway.
     * @param javaClass = the class meant to be loaded.
     */
    void alertBox(final Class javaClass){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Oh No! I couldn't find the database!\nDo you want to try connecting anyway?");
        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                pingConnection();
                if(!"Connected".equals(connection.getText().toString())){
                    finalAlertBox(javaClass);
                } else {
                    intentExecute(javaClass);
                }
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

    /**
     * The final alert box if there still is no connection. If the user wants to continue then ping
     * the server (and hopefully the server will respond like it's supposed to).
     * @param javaClass = the class meant to be loaded.
     */
    void finalAlertBox(final Class javaClass){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Still cannot connect!\nDo you want to continue anyway?\n(some information will not be displayed)");
        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                pingConnection();
                intentExecute(javaClass);
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

    /**
     *
     * @param javaClass = the class meant to be loaded.
     */
    void intentExecute(Class javaClass){
        Intent intent;
        intent = new Intent(this, javaClass);
        startActivity(intent);
    }
}

