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

public class QueryGetSpecialActivity extends AsyncTask<String, Void, String> {
    private WeakReference<Menu> activityReference;
    private boolean connectionError = false, ifWeeklySpecial = false, ifMonthlySpecial = false;
    /**
     *
     * @param context
     */
    QueryGetSpecialActivity(Menu context){
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
        ifWeeklySpecial = type.equals("weeklySpecial");
        ifMonthlySpecial = type.equals("monthlySpecial");
        try {
            String mainURL = params[1];
            String databaseURL = "localhost";
            String db = params[3];
            String username = params[4];
            String password = params[5];
            String port = params[6];
            port = ":" + port;
            String phpName = params[7];
            Log.d("URL", mainURL + port + phpName);
            URL url = new URL(mainURL + port + phpName);
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
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            connectionError = false;
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

    /**
     *
     * @param result
     */
    @Override
    protected void onPostExecute(String result) {
        final Menu activity = activityReference.get();
        if (activity == null || activity.isFinishing()) {
            return;
        } else {
            TextView connectionProblem = activity.findViewById(R.id.connectionProblem);
            TableLayout tableWeekly = activity.findViewById(R.id.tableWeekly);
            TableLayout tableMonthly = activity.findViewById(R.id.tableMonthly);
            // reset the tables values.
            checkArrayAndSetupTables(result.split("/"), tableWeekly, tableMonthly, connectionProblem, activity);
        }
    }

    /**
     *
     * @param result
     * @param tableWeekly
     * @param tableMonthly
     * @param connectionProblem
     * @param context
     */
    private void checkArrayAndSetupTables(String[] result, TableLayout tableWeekly, TableLayout tableMonthly, TextView connectionProblem, Context context) {
        if (!connectionError) {
            if (result.length > 1) {
                // for each value
                for (int i = 0; i < result.length; i++) {
                    TableRow row = new TableRow(context);
                    TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                    tableRowParams.setMargins(5, 5, 5, 5);
                    row.setLayoutParams(tableRowParams);
                    String[] resultArray = result[i].split(",");
                    for (int j = 0; j < resultArray.length; j++) {
                        Log.d("Check Arrays: ", "resultArray " + j + " = " + resultArray[j]);
                    }
                    if (ifWeeklySpecial) {
                        // check if the weekly special is supposed to be displayed.
                        saveAndLoad load = new saveAndLoad();
                        if (load.getMenuWeek(context) == dayToNum(resultArray[1])) {
                            // if the array is greater then 2, then there is information to be displayed.
                            if (resultArray.length > 2) {
                                // set the table for the weekly special
                                row.addView(setupRowViewCheckbox(resultArray[2], context));
                                row.addView(setupRowViewTextview(resultArray[3], context));
                                tableWeekly.addView(row, i);
                            }
                        }
                    } else if (ifMonthlySpecial) {
                        if (resultArray.length > 2) {
                            // set the table for the monthly special
                            row.addView(setupRowViewCheckbox(resultArray[2], context));
                            row.addView(setupRowViewTextview(resultArray[3], context));
                            tableMonthly.addView(row, i);
                        }
                    }
                }
            }
        } else {
            connectionProblem.setText("There was a problem connecting to the database...");
            connectionProblem.setVisibility(View.VISIBLE);
        }
    }

    /**
     *
     * @param dayOfWeek
     * @return
     */
    private int dayToNum(String dayOfWeek){
        int number = 0;
        if(dayOfWeek.equals("Sunday"))
            number = 1;
        if(dayOfWeek.equals("Monday"))
            number = 2;
        if(dayOfWeek.equals("Tuesday"))
            number = 3;
        if(dayOfWeek.equals("Wednesday"))
            number = 4;
        if(dayOfWeek.equals("Thursday"))
            number = 5;
        if(dayOfWeek.equals("Friday"))
            number = 6;
        if(dayOfWeek.equals("Saturday"))
            number = 7;
        return number;
    }

    /**
     *
     * @param str
     * @param context
     * @return
     */
    private CheckBox setupRowViewCheckbox(String str, Context context){
        CheckBox tv = new CheckBox(context);
        // This replaces all spaces with a newline for every 3 blank spaces
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