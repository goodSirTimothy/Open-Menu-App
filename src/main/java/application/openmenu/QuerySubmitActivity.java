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

public class QuerySubmitActivity extends AsyncTask<String, Void, String> {
    private WeakReference<OrderField> activityReference;

    /**
     *
     * @param context
     */
    QuerySubmitActivity(OrderField context){
        activityReference = new WeakReference<>(context);
    }

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
        if(type.equals("submit")){
            try{
                String mainURL = params[1];
                String databaseURL = params[2];
                String db = params[3];
                String username = params[4];
                String password = params[5];
                String port = params[6];
                String phpName = params[7];
                String roomID = params[8];
                String primaryDish = params[9];
                String sides = params[10];
                String drinks = params[11];
                String desserts = params[12];
                String month = params[13];
                String day = params[14];
                String year = params[15];
                String ordered = params[16];
                String served = params[17];
                URL url = new URL(mainURL + ":" + port + phpName);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username", "UTF-8")+"="+ URLEncoder.encode(username, "UTF-8")+
                        "&"+ URLEncoder.encode("password", "UTF-8")+"="+ URLEncoder.encode(password, "UTF-8")+
                        "&"+ URLEncoder.encode("databaseURL", "UTF-8")+"="+ URLEncoder.encode(databaseURL, "UTF-8")+
                        "&"+ URLEncoder.encode("db", "UTF-8")+"="+ URLEncoder.encode(db, "UTF-8")+
                        "&"+ URLEncoder.encode("roomID", "UTF-8")+"="+ URLEncoder.encode(roomID, "UTF-8")+
                        "&"+ URLEncoder.encode("primaryDish", "UTF-8")+"="+ URLEncoder.encode(primaryDish, "UTF-8")+
                        "&"+ URLEncoder.encode("sides", "UTF-8")+"="+ URLEncoder.encode(sides, "UTF-8")+
                        "&"+ URLEncoder.encode("drinks", "UTF-8")+"="+ URLEncoder.encode(drinks, "UTF-8")+
                        "&"+ URLEncoder.encode("desserts", "UTF-8")+"="+ URLEncoder.encode(desserts, "UTF-8")+
                        "&"+ URLEncoder.encode("month", "UTF-8")+"="+ URLEncoder.encode(month, "UTF-8")+
                        "&"+ URLEncoder.encode("day", "UTF-8")+"="+ URLEncoder.encode(day, "UTF-8")+
                        "&"+ URLEncoder.encode("year", "UTF-8")+"="+ URLEncoder.encode(year, "UTF-8")+
                        "&"+ URLEncoder.encode("ordered", "UTF-8")+"="+ URLEncoder.encode(ordered, "UTF-8")+
                        "&"+ URLEncoder.encode("served", "UTF-8")+"="+ URLEncoder.encode(served, "UTF-8");
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
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Connection Failed";// + e;
            }catch (IOException e){
                e.printStackTrace();
                return "Connection Failed";// + e;
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
        final OrderField activity = activityReference.get();
        if (activity == null || activity.isFinishing()){
            Log.d("Not Submitted", "dang... MySQL orders wasn't submitted. ");
            return;
        } else {
            if(result.equals("Insert Complete")){
                Toast.makeText(activity,"Order Sent!",Toast.LENGTH_SHORT).show();
                Intent intent;
                intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
            } else {
                Toast.makeText(activity,"Failed to send order...",Toast.LENGTH_LONG).show();
            }
            Log.d("Submitted", "MySQL orders should be updated. " +
                    "\nResults = " + result);
            //TextView connection = activity.findViewById(R.id.connection);
            //connection.setText(result);
        }
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