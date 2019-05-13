package application.openmenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MysqlConnect extends AppCompatActivity implements View.OnClickListener {
    EditText server, database, port, databaseName, username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mysql_connect);
        saveAndLoad load = new saveAndLoad();
        // set EditText fields to any saved values.
        server = findViewById(R.id.server);
        server.setText(load.getServerURL(this));
        database = findViewById(R.id.database);
        database.setText(load.getDatabaseURL(this));
        port = findViewById(R.id.port);
        port.setText(load.getPort(this));
        databaseName = findViewById(R.id.databaseName);
        databaseName.setText(load.getDatabaseName(this));
        username = findViewById(R.id.username);
        username.setText(load.getUsername(this));
        password = findViewById(R.id.password);

        // buttons
        Button back = findViewById(R.id.back);
        Button home = findViewById(R.id.home);
        Button submit = findViewById(R.id.submit);

        // create on click listeners
        back.setOnClickListener(this);
        home.setOnClickListener(this);
        submit.setOnClickListener(this);
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
            case R.id.back: {
                intent = new Intent(this, Settings.class);
                startActivity(intent);
                break;
            }
            case R.id.home: {
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.submit: {
                updateMySQLFile();
                break;
            }
        }
    }

    /**
     *
     */
    private void updateMySQLFile(){
        String serverURL;
        if(server.getText().toString().equals("")){
            serverURL = "http://192.168.0.120";
        } else {
            serverURL = server.getText().toString();
        }
        String databaseURL;
        if(database.getText().toString().equals("")){
            databaseURL = "192.168.0.120";
        } else {
            databaseURL = database.getText().toString();
        }
        String serverPort = port.getText().toString();
        String db = databaseName.getText().toString();
        db = db.replace(" ", "");
        String user = username.getText().toString();
        String pass = password.getText().toString();
        SigninActivity signing = new SigninActivity(this);
        String condition = "login";
        signing.execute(condition, serverURL, databaseURL, db, user.trim(), pass.trim(), serverPort, "/testConnection.php");
    }

    // catch if back button is pressed.
    public void onBackPressed(){
        Intent intent;
        intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
}

/* DIALOG EXAMPLE CODE
// 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
AlertDialog.Builder builder = new AlertDialog.Builder(this);

// 2. Chain together various setter methods to set the dialog characteristics
builder.setMessage("test").setTitle("Am I doing this right?");

// 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
AlertDialog dialog = builder.create();
dialog.show();
 */
