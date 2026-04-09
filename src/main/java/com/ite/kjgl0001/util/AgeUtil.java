package com.ite.kjgl0001.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AgeUtil {

    public static String calculateAge(String birthday) {
        if (birthday == null || birthday.trim().isEmpty()) {
            return "未知";
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date birthDate = sdf.parse(birthday);

            Calendar birthCalendar = Calendar.getInstance();
            birthCalendar.setTime(birthDate);

            Calendar now = Calendar.getInstance();

            int age = now.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);

            if (now.get(Calendar.MONTH) < birthCalendar.get(Calendar.MONTH) ||
                    (now.get(Calendar.MONTH) == birthCalendar.get(Calendar.MONTH) &&
                            now.get(Calendar.DAY_OF_MONTH) < birthCalendar.get(Calendar.DAY_OF_MONTH))) {
                age--;
            }

            return age > 0 ? String.valueOf(age) : "0";
        } catch (ParseException e) {
            return "未知";
        }
    }
}
