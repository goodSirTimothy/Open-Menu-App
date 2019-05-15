package application.openmenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
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

        DayAndWeekLogic date = new DayAndWeekLogic();
        saveAndLoad load = new saveAndLoad();
        QueryGetOrdersActivity signing = new QueryGetOrdersActivity(this);
        String condition = "display";
        signing.execute(condition, load.getServerURL(this), load.getDatabaseURL(this), load.getDatabaseName(this),
                load.getUsername(this), load.getPassword(this), load.getPort(this), "/getOrders.php", date.getMonth(), date.getDay(), date.getYear());
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
                CheckBox checkBoxMonth = findViewById(R.id.checkBoxMonth);
                CheckBox checkBoxDay = findViewById(R.id.checkBoxDay);
                boolean checkedMonth = checkBoxMonth.isChecked();
                boolean checkedDay = checkBoxDay.isChecked();
                // EditText for displaying values
                EditText month = findViewById(R.id.month);
                EditText day = findViewById(R.id.day);
                EditText year = findViewById(R.id.year);

                // the values to import
                saveAndLoad load = new saveAndLoad();
                QueryGetOrdersActivity signing = new QueryGetOrdersActivity(this);
                if(checkedMonth && checkedDay){
                    String condition = "display";
                    signing.execute(condition, load.getServerURL(this), load.getDatabaseURL(this), load.getDatabaseName(this),
                            load.getUsername(this), load.getPassword(this), load.getPort(this), "/getOrders.php", month.getText().toString(), day.getText().toString(), year.getText().toString());
                    Toast.makeText(getApplicationContext(),"Both Boxes are checked",Toast.LENGTH_LONG).show();
                } else if(checkedMonth && !checkedDay){
                    String condition = "display";
                    signing.execute(condition, load.getServerURL(this), load.getDatabaseURL(this), load.getDatabaseName(this),
                            load.getUsername(this), load.getPassword(this), load.getPort(this), "/getOrders.php", month.getText().toString(), "0", year.getText().toString());
                    Toast.makeText(getApplicationContext(),"Month Box is checked",Toast.LENGTH_LONG).show();
                } else if(!checkedMonth && checkedDay){
                    DayAndWeekLogic date = new DayAndWeekLogic();
                    String condition = "display";
                    signing.execute(condition, load.getServerURL(this), load.getDatabaseURL(this), load.getDatabaseName(this),
                            load.getUsername(this), load.getPassword(this), load.getPort(this), "/getOrders.php", date.getMonth(), day.getText().toString(), year.getText().toString());
                    Toast.makeText(getApplicationContext(),"Day Box is checked",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),"No Boxes are checked",Toast.LENGTH_LONG).show();
                }
            }
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
