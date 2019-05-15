package application.openmenu;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Tim C. on 3/3/2018.
 */

class saveAndLoad {

    /**
     *
     * @param fileName = the file name
     * @param fileContents = the file contents to be saved
     * @param context = the application context
     */
    void saveInformation(String fileName, String[] fileContents, Context context){
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            for (int i = 0; i < fileContents.length; i++){
                if(i == fileContents.length -1){
                    outputStream.write(fileContents[i].getBytes());
                } else {
                    outputStream.write((fileContents[i]+"\n").getBytes());
                }
                String TAGsave = "saveAndLoad, FILE SAVE";
                Log.d(TAGsave, "\n" + "Line " + i + ":" + fileContents[i]);
            }
            outputStream.close();
        } catch (Exception e) {
            Toast.makeText(context,"Error: " + e, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    String getAdminPassword(Context context){
        return loadinformation("admin.info", 0, context);
    }

    String getServerURL(Context context){
        return loadinformation("server.info", 0, context);
    }
    String getDatabaseURL(Context context){
        return loadinformation("server.info", 1, context);
    }
    String getDatabaseName(Context context){
        return loadinformation("server.info", 2, context);
    }
    String getPort(Context context){
        if(loadinformation("server.info", 3, context) == null){
            // If there was no input for server info, then return the default port 80 (because my code forces a port other whys it pings for port null (which doesn't work))
            return "80";
        }
        return loadinformation("server.info", 3, context);
    }

    String getUsername(Context context){
        return loadinformation("user.info", 0, context);
    }
    String getPassword(Context context){
        return loadinformation("user.info", 1, context);
    }

    int getMenuWeek(Context context){
        return Integer.parseInt(loadNumber(0, context));
    }
    int getMenuDay(Context context){
        return Integer.parseInt(loadNumber(1, context));
    }
    int getDayOfMonth(Context context){
        return Integer.parseInt(loadNumber(2, context));
    }
    int getMonth(Context context){
        return Integer.parseInt(loadNumber(3, context));
    }
    int getYear(Context context){
        return Integer.parseInt(loadNumber(4, context));
    }

    String loadNumber(int dataNum, Context context){
        FileInputStream fis;
        String line = "0";

        try {
            fis = context.openFileInput("Menu Day And Week.info");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            int count = 0;
            String TAGsave = "saveAndLoad, FILE LOAD";
            while((line=br.readLine())!=null) {
                if (count == dataNum){ // (count == 0) && (count == dataNum)
                    Log.d(TAGsave, "\nLine: " +line+ "\ncount: " + count);
                    return line;
                }
                count++;
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }

    String loadinformation(String fileName, int dataNum, Context context){
        FileInputStream fis;

        String line = "";

        try {
            fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            int count = 0;
            String TAGsave = "saveAndLoad, FILE LOAD";
            while((line=br.readLine())!=null) {
                if (count == dataNum){ // (count == 0) && (count == dataNum)
                        Log.d(TAGsave, "\nLine: " +line+ "\ncount: " + count);
                        return line;
                }
                count++;
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }

    Boolean checkIfFileExists(String fileName, Context context){
        try {
            context.openFileInput(fileName);
            Log.d("File Exists", "Sweet! The file exists! ");
            return true;
        } catch (FileNotFoundException e) {
            Log.d("File Does Not Exists", "Awwwwww dang... The file doesn't exist. \n");
            // e.printStackTrace();
            return false;
        }


/*
        File file = new File(fileName);
        if(file.exists()){
            Log.d("File Exists", "Sweet! The file exists! ");
        } else {
            Log.d("File Does Not Exists", "Awwwwww dang... The file doesn't exist. \n" + file);
        }*/

    }
}