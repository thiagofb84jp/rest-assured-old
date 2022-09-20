package br.pb.thiagofb84jp.rest.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataUtils {

    public static String getDataDiferencaDias(Integer qtdDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, qtdDays);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String futureDate = dateFormat.format(new Date((calendar.getTimeInMillis())));

        return futureDate;
    }

}
