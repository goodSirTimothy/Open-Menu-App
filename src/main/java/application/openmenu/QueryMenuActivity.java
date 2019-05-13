/**
 * Reference for how to make the AsyncTask:
 *      https://www.sourcecodester.com/android/12282/connect-php-server-using-android.html
 *      https://stackoverflow.com/questions/44309241/warning-this-asynctask-class-should-be-static-or-leaks-might-occur
 *
 * About Dialogs:
 *      https://www.tutorialspoint.com/android/android_alert_dialoges.htm
 *      https://developer.android.com/guide/topics/ui/dialogs#java
 */


package application.openmenu;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Calendar;

public class QueryMenuActivity extends AsyncTask<String, Void, String> {
    private WeakReference<Menu> activityReference;
    private boolean connectionError = false;
    private int tableNumber, properEntreeIndex = 0, properSandwichIndex = 0, properSideIndex = 0, properSoupIndex = 0, properDrinkIndex = 0, properDessertIndex = 0, moveIndexBack = 0;
    private String error;

    /**
     *
     * @param context
     */
    QueryMenuActivity(Menu context){
        activityReference = new WeakReference<>(context);
    }

    /**
     *
     */
    @Override
    protected void onPreExecute() {
    }

    /**
     *
     * @param params
     * @return
     */
    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String table;
        if(type.equals("breakfast")){
            table = "bfastMenu";
            tableNumber = 0;
        } else if (type.equals("lunch")){
            table = "lunchMenu";
            tableNumber = 1;
        } else if (type.equals("supper")){
            table = "supperMenu";
            tableNumber = 2;
        } else if (type.equals("all")){
            table = "supperMenu";
            tableNumber = 2;
        } else {
            // error out the query if the types aren't matched
            table = "nothing";
        }
        try {
            String mainURL = params[1];
            String databaseURL = "localhost";
            String db = params[3];
            String username = params[4];
            String password = params[5];
            String port = params[6];
            port = ":" + port;
            String phpGetFeatureMenu = params[7];
            Log.d("URL", mainURL + port + phpGetFeatureMenu);
            URL url = new URL(mainURL + port + phpGetFeatureMenu);
            String result = "";
            // query to database
            result = getFeatureMenu(username, password, databaseURL, table, db, url);
                String phpGetDailySpecial = params[8];
                Log.d("URL", mainURL + port + phpGetDailySpecial);
                url = new URL(mainURL + port + phpGetDailySpecial);
                result = result + "~" + getDailySpecial(username, password, databaseURL, db, url);
            Log.d("URL", mainURL + port + "/getBreakfast.php");
            url = new URL(mainURL + port + "/getBreakfast.php");
            result = result + "~" + getDailySpecial(username, password, databaseURL, db, url);
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            connectionError = true;
            return "error" + e;
        }
    }

    /**
     *
     * @param username
     * @param password
     * @param databaseURL
     * @param table
     * @param db
     * @param url
     * @return
     */
    private String getFeatureMenu(String username, String password, String databaseURL, String table, String db, URL url) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") +
                    "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") +
                    "&" + URLEncoder.encode("databaseURL", "UTF-8") + "=" + URLEncoder.encode(databaseURL, "UTF-8") +
                    "&" + URLEncoder.encode("table", "UTF-8") + "=" + URLEncoder.encode(table, "UTF-8") +
                    "&" + URLEncoder.encode("db", "UTF-8") + "=" + URLEncoder.encode(db, "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String result = "";
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Log.d("Line = ", line);
                result += line;
            }
            connectionError = false;
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            connectionError = true;
            return "error" + e;
        } catch (IOException e) {
            e.printStackTrace();
            connectionError = true;
            return "error" + e;
        }
    }

    private String getDailySpecial(String username, String password, String databaseURL, String db, URL url) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") +
                    "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") +
                    "&" + URLEncoder.encode("databaseURL", "UTF-8") + "=" + URLEncoder.encode(databaseURL, "UTF-8") +
                    "&" + URLEncoder.encode("db", "UTF-8") + "=" + URLEncoder.encode(db, "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String result = "";
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Log.d("Line = ", line);
                result += line;
            }
            connectionError = false;
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            connectionError = true;
            error = "error: " + e;
            return "error" + e;
        } catch (IOException e) {
            e.printStackTrace();
            connectionError = true;
            error = "error: " + e;
            return "error" + e;
        }
    }

    /**
     *
     * @param firstResult = the result from the doInBackground method.
     */
    @Override
    protected void onPostExecute(String firstResult) {
        final Menu activity = activityReference.get();
        if (activity == null || activity.isFinishing()){
            return;
        } else {
            if(!connectionError) {
                String[] result = firstResult.split("~");
                TextView connectionProblem = activity.findViewById(R.id.connectionProblem);
                TableLayout tablePrimary = activity.findViewById(R.id.tablePrimary);
                TableLayout tableSandwich = activity.findViewById(R.id.tableSandwich);
                TableLayout tableSoup = activity.findViewById(R.id.tableSoup);
                TableLayout tableBreakfast = activity.findViewById(R.id.tableBreakfast);
                TableLayout tableSides = activity.findViewById(R.id.tableSides);
                TableLayout tableDrinks = activity.findViewById(R.id.tableDrinks);
                TableLayout tableDesserts = activity.findViewById(R.id.tableDesserts);
                TableLayout tableWeekly = activity.findViewById(R.id.tableWeekly);
                TableLayout tableMonthly = activity.findViewById(R.id.tableMonthly);
                Log.d("Check Arrays: ", "result = " + Arrays.toString(result));
                if (tableNumber == 0) {
                    // reset the tables values.
                    tableReset(tablePrimary, tableSandwich, tableSoup, tableSides, tableDrinks, tableDesserts, tableWeekly, tableMonthly, tableBreakfast);
                    getMenuDay(result[0].split("/"), tablePrimary, tableSides, tableDrinks, connectionProblem, activity);
                    breakfastTable(result[2].split("/"), connectionProblem, activity);
                } else if (tableNumber == 1) {
                    // reset the tables values.
                    tableReset(tablePrimary, tableSandwich, tableSoup, tableSides, tableDrinks, tableDesserts, tableWeekly, tableMonthly, tableBreakfast);
                    getMenuDay(result[0].split("/"), tablePrimary, tableSides, tableDesserts, connectionProblem, activity);
                    checkArrayAndSetupTables(result[1].split("/"), connectionProblem, activity);
                    breakfastTable(result[2].split("/"), connectionProblem, activity);
                } else if (tableNumber == 2) {
                    // reset the tables values.
                    tableReset(tablePrimary, tableSandwich, tableSoup, tableSides, tableDrinks, tableDesserts, tableWeekly, tableMonthly, tableBreakfast);
                    getMenuDay(result[0].split("/"), tablePrimary, tableSides, tableDesserts, connectionProblem, activity);
                    checkArrayAndSetupTables(result[1].split("/"), connectionProblem, activity);
                    breakfastTable(result[2].split("/"), connectionProblem, activity);
                }
            }
        }
    }

    /**
     * This is for reseting the tables if
     * @param tablePrimary
     * @param tableSides
     * @param tableDrinks
     * @param tableDesserts
     */
    private void tableReset(TableLayout tablePrimary, TableLayout tableSandwich, TableLayout tableSoup,
                            TableLayout tableSides, TableLayout tableDrinks, TableLayout tableDesserts,
                            TableLayout tableWeekly, TableLayout tableMonthly, TableLayout tableBreakfast){
        tablePrimary.removeAllViews();
        tableSandwich.removeAllViews();
        tableSoup.removeAllViews();
        tableSides.removeAllViews();
        tableDrinks.removeAllViews();
        tableDesserts.removeAllViews();
        tableWeekly.removeAllViews();
        tableMonthly.removeAllViews();
        tableBreakfast.removeAllViews();
    }

    ///////////////////////////////////////////////////////////////////////////////// Feature Tables ///////////////////////////////////////////
    private void getMenuDay(String[] results, TableLayout table0, TableLayout table1, TableLayout table2, TextView connectionProblem, Context context) {
        if (!connectionError) {
            //  load the menu week and menu day that it is supposed to be on.
            saveAndLoad load = new saveAndLoad();
            int menuWeek = load.getMenuWeek(context), menuDay = load.getMenuDay(context);
            // check if breakfast menu should display the next day (from the night before
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            // if tableNumber == 0 (breakfast table), check if hour is greater the 1PM. If it is, add one day to display the next of the menu
            if (hour > 13 && tableNumber == 0) {
                menuDay++;
                if (menuDay > 7) {
                    menuDay = 1;
                    menuWeek++;
                    if (menuWeek > 4) {
                        menuWeek = 1;
                    }
                }
            }
            for (int i = 0; i < results.length; i++) {
                String[] resultsArray = results[i].split(",");
                // if the week and day IDs don't match. Do not display the information.
                try {
                    if (menuWeek == Integer.parseInt(resultsArray[0])) {
                        if (menuDay == Integer.parseInt(resultsArray[1])) {
                            String[] resultArray = {resultsArray[2], resultsArray[3], resultsArray[4], resultsArray[5], resultsArray[6]};
                            populateFeatureTables(resultArray, table0, table1, table2, context);
                        }
                    }
                } catch (NumberFormatException e){
                    e.printStackTrace();
                }

            }
        } else {
            Log.d("ERROR::::", "Feature " + error);
            for (int i = 0; i < results.length; i++){
                Log.d("Check Arrays: ", "resultArray " + i + " = " + results[i]);
            }
            connectionProblem.setText("There was a problem connecting to the database...");
            connectionProblem.setVisibility(View.VISIBLE);
        }
    }


    private void populateFeatureTables(String[] resultArray, TableLayout table0, TableLayout table1, TableLayout table2, Context context) {
        TableRow row = new TableRow(context);
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        tableRowParams.setMargins(5, 5, 5, 5);
        row.setLayoutParams(tableRowParams);
        if (resultArray.length > 1) {
            // set the table for the primary feature
            row.addView(setupRowViewCheckbox(resultArray[0], context));
            table0.addView(row, 0);
            // set the table for sides
            if (!"".equals(resultArray[1])) {
                row = new TableRow(context);
                row.addView(setupRowViewCheckbox(resultArray[1], context));
                table1.addView(row, 0);
            } else {
                moveIndexBack++;
            }
            if (!"".equals(resultArray[2])) {
                row = new TableRow(context);
                row.addView(setupRowViewCheckbox(resultArray[2], context));
                table1.addView(row, 1 - moveIndexBack);
            } else {
                moveIndexBack++;
            }
            if (!"".equals(resultArray[3])) {
                row = new TableRow(context);
                row.addView(setupRowViewCheckbox(resultArray[3], context));
                table1.addView(row, 2 - moveIndexBack);
            }
            // set the table for dessert (or drink for breakfast)
            row = new TableRow(context);
            row.addView(setupRowViewCheckbox(resultArray[4], context));
            table2.addView(row, 0);
        }
    }

    /////////////////////////////////////////////////////////////////////// DAILY SPECIAL TABLES ////////////////////////////////
    /**
     *
     * @param result
     * @param connectionProblem
     * @param context
     */
    private void checkArrayAndSetupTables(String[] result, TextView connectionProblem, Context context) {
        if(!connectionError) {
            if (result.length > 0) {
                // for each value
                for (int i = 0; i < result.length; i++) {
                    TableRow row = new TableRow(context);
                    TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                    tableRowParams.setMargins(5, 5, 5, 5);
                    row.setLayoutParams(tableRowParams);
                    String[] resultArray = result[i].split(",");
                    for (int j = 0; j<resultArray.length; j++){
                        // Log.d("Check Arrays: ", "resultArray " + j + " = " + resultArray[j]);
                    }
                    populateTables(resultArray, i, row, context);
                }
            }
        } else {
            Log.d("ERROR::::", "Special " + error);
            for (int i = 0; i < result.length; i++){
                Log.d("Check Arrays: ", "resultArray " + i + " = " + result[i]);
            }
            connectionProblem.setText("There was a problem connecting to the database...");
            connectionProblem.setVisibility(View.VISIBLE);
        }
    }

    /**
     *
     * @param resultArray
     * @param i
     * @param row
     * @param context
     */
    private void populateTables(String[] resultArray, int i, TableRow row, Context context){
        // set up table views
        final Menu activity = activityReference.get();
        TableLayout tableEntree = activity.findViewById(R.id.tablePrimary);
        TableLayout tableSandwich = activity.findViewById(R.id.tableSandwich);
        TableLayout tableSoup = activity.findViewById(R.id.tableSoup);
        TableLayout tableSides = activity.findViewById(R.id.tableSides);
        TableLayout tableDrinks = activity.findViewById(R.id.tableDrinks);
        TableLayout tableDesserts = activity.findViewById(R.id.tableDesserts);
        updateProperIndexValues(resultArray[1]);
        // populate the tables with information from the database.
        switch (resultArray[1]) {
            case "Entrees":
                // set the table for the primary feature
                row.addView(setupRowViewCheckbox(resultArray[2], context));
                row.addView(setupRowViewTextview(resultArray[3], context));
                tableEntree.addView(row, i+1-properEntreeIndex);
                break;
            case "Sandwich":
                // set the table for the primary feature
                row.addView(setupRowViewCheckbox(resultArray[2], context));
                row.addView(setupRowViewTextview(resultArray[3], context));
                tableSandwich.addView(row, i-properSandwichIndex);
                break;
            case "Soups":
                // set the table for the primary feature
                row.addView(setupRowViewCheckbox(resultArray[2], context));
                row.addView(setupRowViewTextview(resultArray[3], context));
                tableSoup.addView(row, i-properSoupIndex);
                break;
            // set the table for the Sides
            case "Sides":
                row.addView(setupRowViewCheckbox(resultArray[2], context));
                row.addView(setupRowViewTextview(resultArray[3], context));
                tableSides.addView(row, i+(3-moveIndexBack)-properSideIndex);
                break;
            case "Drinks":
                // set the table for the Drinks
                row.addView(setupRowViewCheckbox(resultArray[2], context));
                row.addView(setupRowViewTextview(resultArray[3], context));
                tableDrinks.addView(row, i-properDrinkIndex);
                properSoupIndex++;
                break;
            case "Desserts":
                // set the table for the Desserts
                row.addView(setupRowViewCheckbox(resultArray[2], context));
                row.addView(setupRowViewTextview(resultArray[3], context));
                tableDesserts.addView(row, i+1-properDessertIndex);
                break;
        }
    }

    /**
     *
     * @param resultString
     */
    private void updateProperIndexValues(String resultString){
        if(!"Entrees".equals(resultString))
            properEntreeIndex++;
        if(!"Sandwich".equals(resultString))
            properSandwichIndex++;
        if(!"Soups".equals(resultString))
            properSoupIndex++;
        if(!"Sides".equals(resultString))
            properSideIndex++;
        if(!"Drinks".equals(resultString))
            properDrinkIndex++;
        if(!"Desserts".equals(resultString))
            properDessertIndex++;
    }

    /**
     *
     * @param result
     * @param connectionProblem
     * @param context
     */
    private void breakfastTable(String[] result, TextView connectionProblem, Context context) {
        if (!connectionError) {
            if (result.length > 0) {
                int properIndexSide = 0, properIndexBreakfast =0;
                // for each value
                for (int i = 0; i < result.length; i++) {
                    TableRow row = new TableRow(context);
                    TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                    tableRowParams.setMargins(5, 5, 5, 5);
                    row.setLayoutParams(tableRowParams);
                    String[] resultArray = result[i].split(",");
                    for (int j = 0; j < resultArray.length; j++) {
                        // Log.d("Check Arrays: ", "resultArray " + j + " = " + resultArray[j]);
                    }
                    Menu activity = activityReference.get();
                    TableLayout tableBreakfast = activity.findViewById(R.id.tableBreakfast);
                    TableLayout tableSides = activity.findViewById(R.id.tableSides);
                    if(resultArray[1].equals("Sides")){
                        row.addView(setupRowViewCheckbox(resultArray[2], context));
                        row.addView(setupRowViewTextview(resultArray[3], context));
                        tableSides.addView(row, i + properSideIndex - properIndexSide);
                        properIndexBreakfast++;
                    }else {
                        row.addView(setupRowViewCheckbox(resultArray[2], context));
                        row.addView(setupRowViewTextview(resultArray[3], context));
                        tableBreakfast.addView(row, i - properIndexBreakfast);
                        properIndexSide++;
                    }
                }
            }
        } else {
            Log.d("ERROR::::", "Breakfast " + error);
            for (int i = 0; i < result.length; i++) {
                Log.d("Check Arrays: ", "resultArray " + i + " = " + result[i]);
            }
            connectionProblem.setText("There was a problem connecting to the database...");
            connectionProblem.setVisibility(View.VISIBLE);
        }
    }

    /**
     *
     * @param str
     * @param context
     * @return
     */
    private CheckBox setupRowViewCheckbox(String str, Context context){
        CheckBox tv = new CheckBox(context);
        tv.setText(str);
        tv.setTextSize(18);
        tv.setPadding(5,1,5,1);
        tv.setGravity(Gravity.CENTER);
        return tv;
    }

    /**
     *
     * @param str
     * @param context
     * @return
     */
    private TextView setupRowViewTextview(String str, Context context){
        TextView tv = new TextView(context);
        // This replaces all spaces with a newline for every 3 blank spaces
        str = str.replaceAll("((?:\\w+\\s){2}\\w+)(\\s)", "$1\n");
        tv.setText(str);
        tv.setTextSize(18);
        tv.setPadding(10,1,5,5);
        tv.setGravity(Gravity.CENTER);
        return tv;
    }

    /**
     *
     * @param values
     */
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}