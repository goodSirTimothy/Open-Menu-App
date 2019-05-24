package application.openmenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Calendar;

public class Menu extends AppCompatActivity implements View.OnClickListener {
    private String[] iOldData = {"", "", "", ""}, oldRoomInfo;
    private String menuType = "";
    private boolean notesTaken = false;
    private EditText orderNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        // check if the menu is correct
        DayAndWeekLogic checkMenuDay = new DayAndWeekLogic();
        checkMenuDay.calculateChanges(this);

        // set the menu value
        setupInteractiveGUI();
        setTableValues();
        setupSpecialMenus();
    }

    void setupInteractiveGUI(){
        // check the extra bundles
        checkBundles();
        // Make it so portions of the menu are hidden and shown.
        setMenuVisibility();

        orderNotes = findViewById(R.id.orderNotes);

        // Set an onclick listener to show and hide the different menu options
        TextView tvShowHidebtns = findViewById(R.id.tvShowHidebtns);
        tvShowHidebtns.setOnClickListener(this);

        // buttons for displaying parts of the menu
        Button btnBreakfast = findViewById(R.id.btnBreakfast);
        btnBreakfast.setOnClickListener(this);
        Button btnLunch = findViewById(R.id.btnLunch);
        btnLunch.setOnClickListener(this);
        Button btnSupper = findViewById(R.id.btnSupper);
        btnSupper.setOnClickListener(this);

        // the submit button
        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);

        // bring ScrollView to the top
        ScrollView scrollview = findViewById(R.id.scrollview);
        scrollview.scrollTo(0,0);
    }

    void setMenuVisibility(){
        if(menuType.equals("primary")){
            findViewById(R.id.layoutPrimary).setVisibility(View.VISIBLE);
            findViewById(R.id.layoutSandwich).setVisibility(View.VISIBLE);
            findViewById(R.id.layoutSoup).setVisibility(View.VISIBLE);
            findViewById(R.id.layoutSides).setVisibility(View.GONE);
            findViewById(R.id.layoutBreakfast).setVisibility(View.VISIBLE);
            findViewById(R.id.layoutDrinks).setVisibility(View.GONE);
            findViewById(R.id.layoutDessserts).setVisibility(View.GONE);
            findViewById(R.id.layoutWeekly).setVisibility(View.VISIBLE);
            findViewById(R.id.layoutMonthly).setVisibility(View.VISIBLE);
        } else if(menuType.equals("sides")){
            findViewById(R.id.layoutPrimary).setVisibility(View.GONE);
            findViewById(R.id.layoutSandwich).setVisibility(View.GONE);
            findViewById(R.id.layoutSoup).setVisibility(View.GONE);
            findViewById(R.id.layoutBreakfast).setVisibility(View.GONE);
            findViewById(R.id.layoutSides).setVisibility(View.VISIBLE);
            findViewById(R.id.layoutDrinks).setVisibility(View.GONE);
            findViewById(R.id.layoutDessserts).setVisibility(View.GONE);
            findViewById(R.id.layoutWeekly).setVisibility(View.GONE);
            findViewById(R.id.layoutMonthly).setVisibility(View.GONE);
        } else if(menuType.equals("drink")){
            findViewById(R.id.layoutPrimary).setVisibility(View.GONE);
            findViewById(R.id.layoutSandwich).setVisibility(View.GONE);
            findViewById(R.id.layoutSoup).setVisibility(View.GONE);
            findViewById(R.id.layoutBreakfast).setVisibility(View.GONE);
            findViewById(R.id.layoutSides).setVisibility(View.GONE);
            findViewById(R.id.layoutDrinks).setVisibility(View.VISIBLE);
            findViewById(R.id.layoutDessserts).setVisibility(View.GONE);
            findViewById(R.id.layoutWeekly).setVisibility(View.GONE);
            findViewById(R.id.layoutMonthly).setVisibility(View.GONE);
        } else if(menuType.equals("dessert")){
            findViewById(R.id.layoutPrimary).setVisibility(View.GONE);
            findViewById(R.id.layoutSandwich).setVisibility(View.GONE);
            findViewById(R.id.layoutSoup).setVisibility(View.GONE);
            findViewById(R.id.layoutBreakfast).setVisibility(View.GONE);
            findViewById(R.id.layoutSides).setVisibility(View.GONE);
            findViewById(R.id.layoutDrinks).setVisibility(View.GONE);
            findViewById(R.id.layoutDessserts).setVisibility(View.VISIBLE);
            findViewById(R.id.layoutWeekly).setVisibility(View.GONE);
            findViewById(R.id.layoutMonthly).setVisibility(View.GONE);
        } else {
            findViewById(R.id.layoutPrimary).setVisibility(View.VISIBLE);
            findViewById(R.id.layoutSandwich).setVisibility(View.VISIBLE);
            findViewById(R.id.layoutSoup).setVisibility(View.VISIBLE);
            findViewById(R.id.layoutBreakfast).setVisibility(View.VISIBLE);
            findViewById(R.id.layoutSides).setVisibility(View.VISIBLE);
            findViewById(R.id.layoutDrinks).setVisibility(View.VISIBLE);
            findViewById(R.id.layoutDessserts).setVisibility(View.VISIBLE);
            findViewById(R.id.layoutWeekly).setVisibility(View.VISIBLE);
            findViewById(R.id.layoutMonthly).setVisibility(View.VISIBLE);
        }
    }

    void setTableValues(){
        TextView tvMenuDisplay = findViewById(R.id.menuType);
        saveAndLoad load = new saveAndLoad();
        QueryMenuActivity getMenu = new QueryMenuActivity(this);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if(hour >= 6 && hour < 11){
            tvMenuDisplay.setText("Breakfast");
            setBreakfastVisibility();
            getMenu.execute("breakfast", load.getServerURL(this), load.getDatabaseURL(this), load.getDatabaseName(this),
                    load.getUsername(this), load.getPassword(this), load.getPort(this), "/getMenu.php",  "/getBreakfast"); // "/getDailySpecial.php");
        } else if (hour >= 11 && hour < 15) {
            getMenu.execute("lunch", load.getServerURL(this), load.getDatabaseURL(this), load.getDatabaseName(this),
                    load.getUsername(this), load.getPassword(this), load.getPort(this), "/getMenu.php", "/getDailySpecial.php");
            tvMenuDisplay.setText("Lunch");
        } else if (hour >= 15 && hour <= 19) {
            getMenu.execute("supper", load.getServerURL(this), load.getDatabaseURL(this), load.getDatabaseName(this),
                    load.getUsername(this), load.getPassword(this), load.getPort(this), "/getMenu.php", "/getDailySpecial.php");
            tvMenuDisplay.setText("Supper");
        } else {
            getMenu.execute("all", load.getServerURL(this), load.getDatabaseURL(this), load.getDatabaseName(this),
                    load.getUsername(this), load.getPassword(this), load.getPort(this), "/getMenu.php", "/getDailySpecial.php");
            tvMenuDisplay.setVisibility(View.GONE);
        }
    }

    private void setupSpecialMenus(){
        saveAndLoad load = new saveAndLoad();
        QueryGetSpecialActivity getSpecial = new QueryGetSpecialActivity(this);
        getSpecial.execute("weeklySpecial", load.getServerURL(this), load.getDatabaseURL(this), load.getDatabaseName(this),
                load.getUsername(this), load.getPassword(this), load.getPort(this), "/getWeeklySpecial.php");

        getSpecial = new QueryGetSpecialActivity(this);
        getSpecial.execute("monthlySpecial", load.getServerURL(this), load.getDatabaseURL(this), load.getDatabaseName(this),
                load.getUsername(this), load.getPassword(this), load.getPort(this), "/getMonthlySpecial.php");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit: {
                submitPressed();
                break;
            }
            case R.id.tvShowHidebtns: {
                TextView tvShowHidebtns = findViewById(R.id.tvShowHidebtns);
                LinearLayout layoutMenuBtns = findViewById(R.id.layoutMenuBtns);
                if (layoutMenuBtns.getVisibility() == View.GONE) {
                    tvShowHidebtns.setText("^^^ Display Menu Buttons ^^^");
                    layoutMenuBtns.setVisibility(View.VISIBLE);
                } else {
                    tvShowHidebtns.setText("vvv Display Menu Buttons vvv");
                    layoutMenuBtns.setVisibility(View.GONE);
                }
                break;
            }
            case R.id.btnBreakfast: {
                TextView tvMenuDisplay = findViewById(R.id.menuType);
                tvMenuDisplay.setText("Breakfast");
                tvMenuDisplay.setVisibility(View.VISIBLE);
                saveAndLoad load = new saveAndLoad();
                QueryMenuActivity getMenu = new QueryMenuActivity(this);
                getMenu.execute("breakfast", load.getServerURL(this), load.getDatabaseURL(this), load.getDatabaseName(this),
                        load.getUsername(this), load.getPassword(this), load.getPort(this), "/getMenu.php", "/getDailySpecial.php");
                setBreakfastVisibility();
                ScrollView scrollview = findViewById(R.id.scrollview);
                scrollview.scrollTo(0, 0);
                break;
            }
            case R.id.btnLunch: {
                TextView tvMenuDisplay = findViewById(R.id.menuType);
                tvMenuDisplay.setText("Lunch");
                tvMenuDisplay.setVisibility(View.VISIBLE);
                setMenuVisibility();
                saveAndLoad load = new saveAndLoad();
                QueryMenuActivity getMenu = new QueryMenuActivity(this);
                getMenu.execute("lunch", load.getServerURL(this), load.getDatabaseURL(this), load.getDatabaseName(this),
                        load.getUsername(this), load.getPassword(this), load.getPort(this), "/getMenu.php", "/getDailySpecial.php");
                setupSpecialMenus();
                ScrollView scrollview = findViewById(R.id.scrollview);
                scrollview.scrollTo(0,0);
                break;
            }
            case R.id.btnSupper: {
                TextView tvMenuDisplay = findViewById(R.id.menuType);
                tvMenuDisplay.setText("Supper");
                tvMenuDisplay.setVisibility(View.VISIBLE);
                setMenuVisibility();
                saveAndLoad load = new saveAndLoad();
                QueryMenuActivity getMenu = new QueryMenuActivity(this);
                getMenu.execute("supper", load.getServerURL(this), load.getDatabaseURL(this), load.getDatabaseName(this),
                        load.getUsername(this), load.getPassword(this), load.getPort(this), "/getMenu.php", "/getDailySpecial.php");
                setupSpecialMenus();
                ScrollView scrollview = findViewById(R.id.scrollview);
                scrollview.scrollTo(0,0);
                break;
            }
        }
    }

    void setBreakfastVisibility(){
        findViewById(R.id.layoutPrimary).setVisibility(View.VISIBLE);
        findViewById(R.id.layoutSandwich).setVisibility(View.GONE);
        findViewById(R.id.layoutSoup).setVisibility(View.GONE);
        findViewById(R.id.layoutSides).setVisibility(View.VISIBLE);
        findViewById(R.id.layoutDrinks).setVisibility(View.VISIBLE);
        findViewById(R.id.layoutDessserts).setVisibility(View.GONE);
        findViewById(R.id.layoutWeekly).setVisibility(View.GONE);
        findViewById(R.id.layoutMonthly).setVisibility(View.GONE);
    }

    ///////////////////////////////////////////////////////////////////////////////// SUBMIT PRESSED /////////////////////////////////////////////
    private void submitPressed(){
        String strPrimary = getPrimaryString();
        String strSides = getSideString();
        String strDrinks = getDrinksString();
        String strDesserts = getDessertsString();

        Intent intent;
        intent = new Intent(this, OrderField.class);
        intent.putExtra("roomInfo", oldRoomInfo);
        String[] sendOldData = new String[]{strPrimary, strSides, strDrinks, strDesserts};
        intent.putExtra("oldData", sendOldData);
        startActivity(intent);
    }

    private String getPrimaryString(){
        String strPrimary = "";
        if(findViewById(R.id.layoutPrimary).getVisibility()== View.VISIBLE) {
            TableLayout tablePrimary = findViewById(R.id.tablePrimary);
            strPrimary = strPrimary + checkIfStringIsEmpty(tablePrimary, strPrimary);
            TableLayout tableSandwich = findViewById(R.id.tableSandwich);
            strPrimary = strPrimary + checkIfStringIsEmpty(tableSandwich, strPrimary);
            TableLayout tableSoup = findViewById(R.id.tableSoup);
            strPrimary = strPrimary + checkIfStringIsEmpty(tableSoup, strPrimary);
            TableLayout tableWeekly = findViewById(R.id.tableWeekly);
            strPrimary = strPrimary + checkIfStringIsEmpty(tableWeekly, strPrimary);
            TableLayout tableMonthly = findViewById(R.id.tableMonthly);
            strPrimary = strPrimary + checkIfStringIsEmpty(tableMonthly, strPrimary);
            TableLayout tableBreakfast = findViewById(R.id.tableBreakfast);
            strPrimary = strPrimary + checkIfStringIsEmpty(tableBreakfast, strPrimary);
            if(!notesTaken && !"".equals(orderNotes.getText().toString())){
                strPrimary = strPrimary + "\n(" + orderNotes.getText().toString() + ")";
                notesTaken = true;
            }
        }
        if(strPrimary.equals("")){
            strPrimary = iOldData[0];
        }
        return strPrimary;
    }

    private String checkIfStringIsEmpty(TableLayout tableLayout, String str){
        String checkString = checkForCheckedBoxes(tableLayout);
        if(!"".equals(checkString)) {
            if("".equals(str)) {
                return checkString;
            } else {
                return "\n" + checkString;
            }
        }
        else {
            return "";
        }
    }

    private String getSideString(){
        String strSides = "";
        if(findViewById(R.id.layoutSides).getVisibility() == View.VISIBLE) {
            TableLayout tableSides = findViewById(R.id.tableSides);
            strSides =  checkForCheckedBoxes(tableSides);
            if(!notesTaken && !"".equals(orderNotes.getText().toString())){
                strSides = strSides + "\n(" + orderNotes.getText().toString() + ")";
                notesTaken = true;
            }
        }
        if(strSides.equals("")) {
            strSides = iOldData[1];
        }
        return strSides;
    }
    private String getDrinksString(){
        String strDrinks = "";
        if(findViewById(R.id.layoutDrinks).getVisibility() == View.VISIBLE) {
            TableLayout tableDrinks = findViewById(R.id.tableDrinks);
            strDrinks = checkForCheckedBoxes(tableDrinks);
            if(!notesTaken && !"".equals(orderNotes.getText().toString())){
                strDrinks = strDrinks + "\n(" + orderNotes.getText().toString() + ")";
                notesTaken = true;
            }
        }
        if(strDrinks.equals("")) {
            strDrinks = iOldData[2];
        }
        return strDrinks;
    }
    private String getDessertsString(){
        String strDesserts = "";
        if (findViewById(R.id.layoutDessserts).getVisibility() == View.VISIBLE) {
            TableLayout tablesDesserts = findViewById(R.id.tableDesserts);
            strDesserts = checkForCheckedBoxes(tablesDesserts);
            if(!notesTaken && !"".equals(orderNotes.getText().toString())){
                strDesserts = strDesserts + "\n(" + orderNotes.getText().toString() + ")";
                notesTaken = true;
            }
        }
        if(strDesserts.equals("")) {
            strDesserts = iOldData[3];
        }
        return strDesserts;
    }

    String checkForCheckedBoxes(TableLayout tableLayout){
        // https://www.dev2qa.com/android-display-data-in-table-layout-statically-and-programmatically-example/
        // Get table row count.
        int rowCount = tableLayout.getChildCount();

        // String of checked boxes
        String checkedBoxes = "";

        // Loope each table rows.
        for (int i = 0; i < rowCount; i++) {
            // Get table row.
            View rowView = tableLayout.getChildAt(i);
            if (rowView instanceof TableRow) {
                TableRow tableRow = (TableRow) rowView;
                // Get row column count.
                int columnCount = tableRow.getChildCount();
                // Loop all columns in row.
                for (int j = 0; j < columnCount; j++) {
                    View columnView = tableRow.getChildAt(j);
                    if (columnView instanceof CheckBox) {
                        // If columns is a checkbox and checked then save the row number in list.
                        CheckBox checkboxView = (CheckBox) columnView;
                        if (checkboxView.isChecked()) {
                            if(!"".equals(checkedBoxes)){
                                checkedBoxes = checkedBoxes + ", " + checkboxView.getText().toString();
                            } else {
                                checkedBoxes = checkedBoxes + checkboxView.getText().toString();
                            }
                        }
                    }
                }
            }
        }
        return checkedBoxes;
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
            menuType = extras.getString("menuType");
            Log.d("extras has info","oldData = " + iOldData + "\n\n oldRoomInfo = " + oldRoomInfo);
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
