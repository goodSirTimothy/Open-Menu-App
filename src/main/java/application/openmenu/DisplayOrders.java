package application.openmenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayOrders extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_orders);

        TextView showHideSort = findViewById(R.id.showHideSort);
        showHideSort.setOnClickListener(this);

        Button btnSwitchView = findViewById(R.id.btnSwitchView);
        btnSwitchView.setOnClickListener(this);

        displayOrdersTaken();
    }

    private void displayOrdersTaken(){
        DayAndWeekLogic date = new DayAndWeekLogic();
        String breakfast = "0";
        String lunch = "0";
        String supper = "0";
        if(date.getHour()<11){
            CheckBox checkBox = findViewById(R.id.checkBoxBreakfast);
            checkBox.setChecked(true);
            breakfast = "1";
        } else if(date.getHour() >=11 && date.getHour() < 3){
            CheckBox checkBox = findViewById(R.id.checkBoxLunch);
            checkBox.setChecked(true);
            lunch = "1";
        } else {
            CheckBox checkBox = findViewById(R.id.checkBoxSupper);
            checkBox.setChecked(true);
            supper = "1";
        }
        saveAndLoad load = new saveAndLoad();
        QueryGetOrdersActivity orders = new QueryGetOrdersActivity(this);
        String condition = "display";
        orders.execute(condition, load.getServerURL(this), load.getDatabaseURL(this), load.getDatabaseName(this),
                load.getUsername(this), load.getPassword(this), load.getPort(this), "/getOrders.php", "0", date.getDay(), date.getYear(), breakfast, lunch, supper);
    }

    private void displayOrdersNotTaken(){
        DayAndWeekLogic date = new DayAndWeekLogic();
        String breakfast = "0";
        String lunch = "0";
        String supper = "0";
        if (date.getHour() < 11) {
            CheckBox checkBox = findViewById(R.id.checkBoxBreakfast);
            checkBox.setChecked(true);
            breakfast = "1";
        } else if (date.getHour() >= 11 && date.getHour() < 3) {
            CheckBox checkBox = findViewById(R.id.checkBoxLunch);
            checkBox.setChecked(true);
            lunch = "1";
        } else {
            CheckBox checkBox = findViewById(R.id.checkBoxSupper);
            checkBox.setChecked(true);
            supper = "1";
        }
        saveAndLoad load = new saveAndLoad();
        QueryGetOrdersNotTakenActivity notTaken = new QueryGetOrdersNotTakenActivity(this);
        String condition = "display";
        notTaken.execute(condition, load.getServerURL(this), load.getDatabaseURL(this), load.getDatabaseName(this),
                load.getUsername(this), load.getPassword(this), load.getPort(this), "/getOrders.php", "0", date.getDay(),
                date.getYear(), breakfast, lunch, supper, "/getRoom.php");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.showHideSort: {
                TextView showHideSort = findViewById(R.id.showHideSort);
                if(showHideSort.getText().toString().equals("vvv Show Order Sorting vvv")){
                    showHideSort.setText("^^^ Hide Order Sorting ^^^");
                    findViewById(R.id.layoutSortByDate).setVisibility(View.VISIBLE);

                    DayAndWeekLogic date = new DayAndWeekLogic();
                    EditText year = findViewById(R.id.year);
                    year.setText(date.getYear());
                    EditText month = findViewById(R.id.month);
                    month.setText(date.getMonth());
                    EditText day = findViewById(R.id.day);
                    day.setText(date.getDay());

                    Button submit = findViewById(R.id.submit);
                    submit.setOnClickListener(this);
                } else {
                    showHideSort.setText("vvv Show Order Sorting vvv");
                    findViewById(R.id.layoutSortByDate).setVisibility(View.GONE);
                }
                break;
            }
            case R.id.submit:{
                submitClicked();
                break;
            }
            case R.id.btnSwitchView: {
                Button btnSwitchView = findViewById(R.id.btnSwitchView);
                if (btnSwitchView.getText().toString().equals("Orders Not Taken")) {
                    displayOrdersNotTaken();
                    btnSwitchView.setText("Orders Taken");
                } else {
                    displayOrdersTaken();
                    btnSwitchView.setText("Orders Not Taken");
                }
                break;
            }
        }
    }

    private void submitClicked(){
        CheckBox checkBoxMonth = findViewById(R.id.checkBoxMonth);
        CheckBox checkBoxDay = findViewById(R.id.checkBoxDay);
        boolean checkedMonth = checkBoxMonth.isChecked();
        boolean checkedDay = checkBoxDay.isChecked();
        // EditText for displaying values
        EditText month = findViewById(R.id.month);
        EditText day = findViewById(R.id.day);
        EditText year = findViewById(R.id.year);

        DayAndWeekLogic date = new DayAndWeekLogic();
        String strBreakfast = "0";
        String strLunch = "0";
        String strSupper = "0";

        CheckBox checkBoxBfast = findViewById(R.id.checkBoxBreakfast);
        CheckBox checkBoxLunch = findViewById(R.id.checkBoxLunch);
        CheckBox checkBoxSupper = findViewById(R.id.checkBoxSupper);
        if(!checkBoxBfast.isChecked() && !checkBoxLunch.isChecked() && !checkBoxSupper.isChecked()){
            strBreakfast = "1";
            strLunch = "1";
            strSupper = "1";
        } else {
            if (checkBoxBfast.isChecked()) {
                strBreakfast = "1";
            }

            if (checkBoxLunch.isChecked()) {
                strLunch = "1";
            }

            if (checkBoxSupper.isChecked()) {
                strSupper = "1";
            }
        }

        // the values to import
        saveAndLoad load = new saveAndLoad();
        QueryGetOrdersActivity orders = new QueryGetOrdersActivity(this);
        if(checkedMonth && checkedDay){
            String condition = "display";
            orders.execute(condition, load.getServerURL(this), load.getDatabaseURL(this), load.getDatabaseName(this),
                    load.getUsername(this), load.getPassword(this), load.getPort(this), "/getOrders.php",
                    month.getText().toString(), day.getText().toString(), year.getText().toString(), strBreakfast, strLunch, strSupper);
        } else if(checkedMonth && !checkedDay){
            String condition = "display";
            orders.execute(condition, load.getServerURL(this), load.getDatabaseURL(this), load.getDatabaseName(this),
                    load.getUsername(this), load.getPassword(this), load.getPort(this), "/getOrders.php",
                    month.getText().toString(), "0", year.getText().toString(), strBreakfast, strLunch, strSupper);
        } else if(!checkedMonth && checkedDay){
            String condition = "display";
            orders.execute(condition, load.getServerURL(this), load.getDatabaseURL(this), load.getDatabaseName(this),
                    load.getUsername(this), load.getPassword(this), load.getPort(this), "/getOrders.php",
                    "0", day.getText().toString(), year.getText().toString(), strBreakfast, strLunch, strSupper);
        } else {
            String condition = "display";
            orders.execute(condition, load.getServerURL(this), load.getDatabaseURL(this), load.getDatabaseName(this),
                    load.getUsername(this), load.getPassword(this), load.getPort(this), "/getOrders.php",
                    date.getMonth(), date.getDay(), date.getYear(), strBreakfast, strLunch, strSupper);
            Toast.makeText(getApplicationContext(),"No Boxes are checked",Toast.LENGTH_LONG).show();
        }
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

    // catch if back button is pressed.
    public void onBackPressed(){
        Intent intent;
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
