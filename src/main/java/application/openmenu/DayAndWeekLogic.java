package application.openmenu;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

class DayAndWeekLogic {
    private int menuWeek, menuDay;
    void calculateChanges(Context context) {
        // load new dates to check if there is any need for an update
        Calendar calendar = Calendar.getInstance();
        int newMonth = calendar.get(Calendar.MONTH) + 1;
        int newDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int newYear = calendar.get(Calendar.YEAR);

        // load old menu information
        saveAndLoad load = new saveAndLoad();
        menuWeek = load.getMenuWeek(context);
        if(menuWeek!=0) {
            menuDay = load.getMenuDay(context);
            int oldDayOfMonth = load.getDayOfMonth(context);
            int oldMonth = load.getMonth(context);
            int oldYear = load.getYear(context);
            if (newYear == oldYear) {
                int totalNewDays = calculateMonthsToDays(newMonth, newYear) + newDayOfMonth;
                int totalOldDays = calculateMonthsToDays(oldMonth, oldYear) + oldDayOfMonth;
                if (totalOldDays < totalNewDays) {
                    countUpDays(totalOldDays, totalNewDays);
                    Toast.makeText(context, "Week: " + menuWeek + "Day: " + menuDay, Toast.LENGTH_LONG).show();
                    String[] saveArray = {""+menuWeek, ""+menuDay, ""+newDayOfMonth, ""+newMonth, ""+newYear};
                    load.saveInformation("Menu Day And Week.info", saveArray, context);
                }
            } else {
                if (oldMonth == 12 && oldDayOfMonth == 31) {
                    newYear(newMonth, newDayOfMonth, newYear, load, context);
                } else {
                    int totalNewDays = calculateMonthsToDays(12, oldYear) + 31;
                    int totalOldDays = calculateMonthsToDays(oldMonth, oldYear) + oldDayOfMonth;
                    if (totalOldDays < totalNewDays) {
                        countUpDays(totalOldDays, totalNewDays);
                        newYear(newMonth, newDayOfMonth, newYear, load, context);
                    }
                }
            }
        }
    }

    private void newYear( int newMonth, int newDayOfMonth, int newYear, saveAndLoad load, Context context){
        int oldMonth = 1;
        int oldDayOfMonth = 1;
        menuDay++;
        if (menuDay > 7){
            menuWeek++;
            menuDay = 1;
        }
        if (menuWeek > 4){
            menuWeek = 1;
        }
        int totalNewDays = calculateMonthsToDays(newMonth, newYear) + newDayOfMonth;
        int totalOldDays = calculateMonthsToDays(oldMonth, newYear) + oldDayOfMonth;
        if (totalOldDays < totalNewDays) {
            countUpDays(totalOldDays, totalNewDays);
            Toast.makeText(context, "Week: " + menuWeek + "Day: " + menuDay, Toast.LENGTH_LONG).show();
            String[] saveArray = {""+menuWeek, ""+menuDay, ""+newDayOfMonth, ""+newMonth, ""+newYear};
            load.saveInformation("Menu Day And Week.info", saveArray, context);
        }
    }

    private void countUpDays(int totalOldDays, int totalNewDays){
        while(totalOldDays < totalNewDays){
            menuDay++;
            if(menuDay > 7){
                menuDay = 1;
                menuWeek++;
            }
            if(menuWeek > 4){
                menuWeek = 1;
            }
            totalOldDays++;
            Log.d("", "menuDay: " + menuDay + "\ntotalOldDays: " + totalOldDays + "\ntotalNewDays: " + totalNewDays);
        }
    }

    private int calculateMonthsToDays(int month, int year){
        boolean leapYear = checkLeapYear(year);
        int numOfDays = 0;{
            for(int i = 1; i<month; i++) {
                if (i == 1) {
                    numOfDays = numOfDays + 31;
                } else if (i == 2 && leapYear) {
                    numOfDays = numOfDays + 29;
                } else if (i == 2 && !leapYear) {
                    numOfDays = numOfDays + 28;
                } else if (i == 3) {
                    numOfDays = numOfDays + 31;
                } else if (i == 4) {
                    numOfDays = numOfDays + 30;
                } else if (i == 5) {
                    numOfDays = numOfDays + 31;
                } else if (i == 6) {
                    numOfDays = numOfDays + 30;
                } else if (i == 7) {
                    numOfDays = numOfDays + 31;
                } else if (i == 8) {
                    numOfDays = numOfDays + 31;
                } else if (i == 9) {
                    numOfDays = numOfDays + 30;
                } else if (i == 10) {
                    numOfDays = numOfDays + 31;
                } else if (i == 11) {
                    numOfDays = numOfDays + 30;
                } else if (i == 12) {
                    numOfDays = numOfDays + 31;
                }
            }
        }
        return numOfDays;
    }

    private boolean checkLeapYear(int year){
        boolean leapYear;
        year -= 2000;
        if(year % 400 == 0) {
            leapYear = true;
        } else if (year % 100 == 0) {
            leapYear = false;
        } else {
            leapYear = year % 4 == 0;
        }
        return leapYear;
    }

    String getMonth(){
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        return "" +  month;
    }

    String getDay(){
        Calendar calendar = Calendar.getInstance();
        return "" +  calendar.get(Calendar.DAY_OF_MONTH);
    }

    String getYear(){
        Calendar calendar = Calendar.getInstance();
        return "" + calendar.get(Calendar.YEAR);
    }
}