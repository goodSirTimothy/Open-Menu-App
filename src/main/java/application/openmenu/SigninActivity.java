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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

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

public class SigninActivity extends AsyncTask<String, Void, String> {
    private WeakReference<MysqlConnect> activityReference;
    private AlertDialog.Builder builder;
    private String mainURL, port, databaseURL, db, username, password;

    /**
     *
     * @param context
     */
    SigninActivity(MysqlConnect context){
        activityReference = new WeakReference<>(context);
        builder = new AlertDialog.Builder(context);
    }

    /**
     *
     */
    @Override
    protected void onPreExecute() {
        builder.setTitle("System Information");
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
                mainURL = params[1];
                databaseURL = params[2];
                db = params[3];
                username = params[4];
                password = params[5];
                port = params[6];
                String phpName = params[7];
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
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "error" + e;
            }catch (IOException e){
                e.printStackTrace();
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
        final MysqlConnect activity = activityReference.get();
        if (activity == null || activity.isFinishing()){
            return;
        } else {
            builder.setMessage(result);
            builder.setCancelable(false);
            builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if(result.equals("Connected")) {
                        saveAndLoad save = new saveAndLoad();
                        String[] serverInfoArray = {mainURL, databaseURL, db, port};
                        save.saveInformation("server.info", serverInfoArray, activity);
                        String[] userInfo = {username, password};
                        save.saveInformation("user.info", userInfo, activity);

                        Intent intent;
                        intent = new Intent(activityReference.get(), Settings.class);
                        activity.startActivity(intent);
                    } else {
                        return;
                    }
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
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