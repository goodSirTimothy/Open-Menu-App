package application.openmenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AdminLockLogin   extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_lock_login);

        Button login = findViewById(R.id.login);
        Button back = findViewById(R.id.back);
        login.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login: {
                TextView tvError = findViewById(R.id.tvError);
                saveAndLoad saveAndLoad = new saveAndLoad();
                EditText pass = findViewById(R.id.pass);
                if (saveAndLoad.getAdminPassword(this).equals(pass.getText().toString())) {
                    intentExecute(Settings.class);
                } else {
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText("password did not match.");
                }

                break;
            }
            case R.id.back: {
                intentExecute(MainActivity.class);
                break;
            }
        }
    }


    /**
     *
     */
    void intentExecute(Class javaClass){
        Intent intent;
        intent = new Intent(this, javaClass);
        startActivity(intent);
    }

    // catch if back button is pressed.
    public void onBackPressed(){
        Intent intent;
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
