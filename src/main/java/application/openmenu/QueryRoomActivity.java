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

public class QueryRoomActivity extends AsyncTask<String, Void, String> {
    private WeakReference<Rooms> activityReference;
    boolean connectionError = false;
    /**
     *
     * @param context
     */
    QueryRoomActivity(Rooms context){
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
        if(type.equals("login")){
            try{
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
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username", "UTF-8")+"="+ URLEncoder.encode(username, "UTF-8")+
                        "&"+ URLEncoder.encode("password", "UTF-8")+"="+ URLEncoder.encode(password, "UTF-8")+
                        "&"+ URLEncoder.encode("databaseURL", "UTF-8")+"="+ URLEncoder.encode(databaseURL, "UTF-8")+
                "&"+ URLEncoder.encode("db", "UTF-8")+"="+ URLEncoder.encode(db, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line;
                while((line = bufferedReader.readLine()) != null){
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
            }catch (IOException e){
                e.printStackTrace();
                connectionError = true;
                return "error" + e;
            }

        }
        return null;
    }

    /**
     *
     * @param result
     */
    @Override
    protected void onPostExecute(final String result) {
        final Rooms activity = activityReference.get();
        if (activity == null || activity.isFinishing()){
            return;
        } else {
            TableLayout table = activity.findViewById(R.id.roomTable);
            TextView connectionProblem = activity.findViewById(R.id.connectionProblem);
            populateTable(result.split("/"), table, connectionProblem,activity);
        }
    }

    private void populateTable(String[] results, TableLayout table, TextView connectionProblem, final Context context) {
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
            int fixIndex = 0;
            for (int i = 1; i < results.length + 1; i++) {
                final String[] resultsArray = results[i - 1].split(",");
                row = new TableRow(context);
                row.setLayoutParams(tableRowParams);
                if (resultsArray[2].equals("1")) {
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
                            String[] oldData = activityReference.get().iOldData;
                            intent.putExtra("oldData", oldData);
                            context.startActivity(intent);
                        }
                    });
                    table.addView(row, i - fixIndex);
                } else {
                    fixIndex++;
                }

            }
        } else {
            String str = "";
            for (String result : results) {
                str = str + result;
            }
            connectionProblem.setText("There was a problem connecting to the database: \n" + str);
            connectionProblem.setVisibility(View.VISIBLE);
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