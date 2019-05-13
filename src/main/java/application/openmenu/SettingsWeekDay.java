package application.openmenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class SettingsWeekDay extends AppCompatActivity implements View.OnClickListener {
    EditText etWeeks, etDays;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_week_day);
        etWeeks = findViewById(R.id.etWeeks);
        etDays = findViewById(R.id.etDays);
        saveAndLoad load = new saveAndLoad();
        int day = load.getMenuDay(this);
        if(day == 0) {
            Calendar calendar = Calendar.getInstance();
            day = calendar.get(Calendar.DAY_OF_WEEK);
            etDays.setText("" + day);
        } else {
            int week = load.getMenuWeek(this);
            etWeeks.setText("" + week);
            etDays.setText("" + day);
        }

        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.submit) {
            Calendar calendar = Calendar.getInstance();
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH), month = calendar.get(Calendar.MONTH) + 1, year = calendar.get(Calendar.YEAR);
            String weekNum = etWeeks.getText().toString();
            String dayNum = etDays.getText().toString();
            if (Integer.parseInt(weekNum) > 0 && Integer.parseInt(weekNum) < 4){
                if (Integer.parseInt(dayNum) > 0 && Integer.parseInt(dayNum) < 8) {
                    if (!"".equals(weekNum) && !"".equals(dayNum)) {
                        String[] menuNumbers = {weekNum, dayNum, "" + dayOfMonth, "" + month, "" + year};
                        saveAndLoad save = new saveAndLoad();
                        save.saveInformation("Menu Day And Week.info", menuNumbers, this);
                        Toast.makeText(getApplicationContext(), "Test press on Submit", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter a day between 1 and 7", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Please enter a week between 1 and 4", Toast.LENGTH_LONG).show();
            }
        }
    }

    // catch if back button is pressed.
    public void onBackPressed(){
        Intent intent;
        intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
}
