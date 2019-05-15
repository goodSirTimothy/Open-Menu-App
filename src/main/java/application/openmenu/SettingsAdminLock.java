package application.openmenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsAdminLock  extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_admin_lock);

        saveAndLoad check = new saveAndLoad();
        if(!check.checkIfFileExists("admin.info", this)){
            findViewById(R.id.layoutOldAdmin).setVisibility(View.GONE);
        }

        Button submit = findViewById(R.id.submit);
        Button back = findViewById(R.id.back);
        submit.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit: {
                TextView tvError = findViewById(R.id.tvError);
                if (findViewById(R.id.layoutOldAdmin).getVisibility() == View.GONE) {
                    saveAndLoad save = new saveAndLoad();
                    // get the admin username and password
                    EditText newPassword = findViewById(R.id.newPass);
                    if( !"".equals(newPassword.getText().toString())){
                        // pass the username and password into an array to save them.
                        String[] saveArray = {newPassword.getText().toString()};
                        // save them.
                        save.saveInformation("admin.info", saveArray, this);
                        intentExecute();
                    } else {
                        tvError.setVisibility(View.VISIBLE);
                        tvError.setText("Values cannot be blank.");
                    }
                } else {
                    saveAndLoad saveAndLoad = new saveAndLoad();
                    EditText oldPassword = findViewById(R.id.oldPass);
                    if (saveAndLoad.getAdminPassword(this).equals(oldPassword.getText().toString())) {
                        // get the admin username and password
                        EditText newPassword = findViewById(R.id.newPass);
                        if (!"".equals(newPassword.getText().toString())) {
                            // pass the username and password into an array to save them.
                            String[] saveArray = {newPassword.getText().toString()};
                            // save them.
                            saveAndLoad.saveInformation("admin.info", saveArray, this);
                            intentExecute();
                        } else {
                            tvError.setVisibility(View.VISIBLE);
                            tvError.setText("Values cannot be blank.");
                        }
                    } else {
                        tvError.setVisibility(View.VISIBLE);
                        tvError.setText("password did not match.");
                    }
                }
                break;
            }
            case R.id.back: {
                intentExecute();
                break;
            }
        }
    }


    /**
     *
     */
    void intentExecute(){
        Intent intent;
        intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    // catch if back button is pressed.
    public void onBackPressed(){
        if (findViewById(R.id.layoutOldAdmin).getVisibility() == View.GONE) {
            Intent intent;
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            intentExecute();
        }
    }
}
