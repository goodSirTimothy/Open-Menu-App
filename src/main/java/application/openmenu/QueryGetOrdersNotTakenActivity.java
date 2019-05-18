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
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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

public class QueryGetOrdersNotTakenActivity extends AsyncTask<String, Void, String> {
    private WeakReference<DisplayOrders> activityReference;
    boolean connectionError = false;
    /**
     *
     * @param context
     */
    QueryGetOrdersNotTakenActivity(DisplayOrders context){
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
        if(type.equals("display")){
            try{
                String mainURL = params[1];
                String databaseURL = "localhost";
                String db = params[3];
                String username = params[4];
                String password = params[5];
                String port = params[6];
                port = ":" + port;
                String phpName = params[7];
                String month = params[8];
                String day = params[9];
                String year = params[10];
                String breakfast = params[11];
                String lunch = params[12];
                String supper = params[13];
                Log.d("URL", mainURL + port + phpName);
                URL url = new URL(mainURL + port + phpName);
                String result = getOrders(url, username, password, databaseURL, db, month, day, year, breakfast, lunch, supper);
                phpName = params[14];
                Log.d("URL", mainURL + port + phpName);
                url = new URL(mainURL + port + phpName);
                result = result + "~" + getRoom(url, username, password, databaseURL, db);
                connectionError = false;
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                connectionError = true;
                Log.d("Closed", "MalformedURLException: " + e);
                return "error" + e;
            }

        }
        return null;
    }

    private String getOrders(URL url, String username, String password, String databaseURL, String db, String month, String day, String year, String breakfast, String lunch, String supper){
        String result = "";
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
                    "&" + URLEncoder.encode("db", "UTF-8") + "=" + URLEncoder.encode(db, "UTF-8") +
                    "&" + URLEncoder.encode("month", "UTF-8") + "=" + URLEncoder.encode(month, "UTF-8") +
                    "&" + URLEncoder.encode("day", "UTF-8") + "=" + URLEncoder.encode(day, "UTF-8") +
                    "&" + URLEncoder.encode("year", "UTF-8") + "=" + URLEncoder.encode(year, "UTF-8") +
                    "&" + URLEncoder.encode("breakfast", "UTF-8") + "=" + URLEncoder.encode(breakfast, "UTF-8") +
                    "&" + URLEncoder.encode("lunch", "UTF-8") + "=" + URLEncoder.encode(lunch, "UTF-8") +
                    "&" + URLEncoder.encode("supper", "UTF-8") + "=" + URLEncoder.encode(supper, "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            connectionError = true;
            Log.d("Closed", "MalformedURLException: " + e);
            return "error" + e;
        }catch (IOException e){
            e.printStackTrace();
            connectionError = true;
            Log.d("Closed", "IOException" + e);
            return "error" + e;
        }
        return result;
    }

    private String getRoom(URL url, String username, String password, String databaseURL, String db){
        String result = "";
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
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            connectionError = true;
            Log.d("Closed", "MalformedURLException: " + e);
            return "error" + e;
        }catch (IOException e){
            e.printStackTrace();
            connectionError = true;
            Log.d("Closed", "IOException" + e);
            return "error" + e;
        }
        return  result;
    }

    /**
     *
     * @param result
     */
    @Override
    protected void onPostExecute(final String result) {
        final DisplayOrders activity = activityReference.get();
        if (activity == null || activity.isFinishing()){
            Log.d("Closed", "Wat? Why did I close?");
            Log.d("result", result);
            return;
        } else {
            TableLayout table = activity.findViewById(R.id.ordersTable);
            table.removeAllViews();
            Log.d("result", result);
            String[] resultArray = result.split("~");
            populateTable(resultArray[1].split("/"), resultArray[0].split("/"), table, activity);
        }
    }

    private void populateTable(String[] results, String[] roomResults, TableLayout table, final Context context) {
        TableRow row = new TableRow(context);
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        tableRowParams.setMargins(5, 5, 5, 5);
        // row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        row.setLayoutParams(tableRowParams);
        String[] tableHead = {"Hallway", "Room", "First Name", "Last Name", "Food Diet", "Drink Diet", "Other Notes"};
        for (String aTableHead : tableHead) {
            row.addView(setupRowView(aTableHead, context));
        }
        table.addView(row, 0);

        if(!connectionError) {
            // fixIndex is important because if a row is skipped when creating a table, the program will break. And instead of adding
            // blank rows, I've decided to just leave out the rows not needed. thus, fixIndex. (same concept for displaying the menu)
            int fixIndex = 0;
            for (int i = 1; i < results.length + 1; i++) {
                final String[] resultsArray = results[i - 1].split(",");
                boolean roomIDFound = false;
                for(int j = 0; j < roomResults.length; j++){
                    final String[] room = roomResults[j].split("\\|");
                    if(room.length>1) {
                        if (room[1].equals(resultsArray[1])) {
                            roomIDFound = true;
                            fixIndex++;
                        }
                    }
                }
                if(!roomIDFound) {
                    if (resultsArray[2].equals("1")) {
                        row = new TableRow(context);
                        row.setLayoutParams(tableRowParams);
                        for (int j = 0; j < resultsArray.length; j++) {
                            if (j != 2) {
                                row.addView(setupRowView(resultsArray[j], context));
                            }
                        }
                        row.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent;
                                intent = new Intent(context, OrderField.class);
                                intent.putExtra("roomInfo", resultsArray);
                                context.startActivity(intent);
                            }
                        });
                        String strArray = "";
                        for(int j = 0; j<resultsArray.length; j++){
                            strArray = strArray + resultsArray[j];
                        }
                        Log.d("Row Index = ", "i = " + i + "\nResult Array = " + strArray);
                        table.addView(row, i-fixIndex);
                    } else {
                        fixIndex++;
                    }
                }
            }
        }
    }

    private TextView setupRowView(String str, Context context){
        TextView tv = new TextView(context);
        // This replaces all spaces with a newline for every 3 blank spaces
        str = str.replaceAll("((?:\\w+\\s){2}\\w+)(\\s)", "$1\n");
        tv.setText(str);
        tv.setTextSize(18);
        tv.setPadding(5,1,5,1);
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