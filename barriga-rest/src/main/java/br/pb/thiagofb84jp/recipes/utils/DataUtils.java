package br.pb.thiagofb84jp.recipes.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataUtils {

    public static String getDataDiferencaDias(Integer qtdDias) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, qtdDias);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataFutura = dateFormat.format(new Date((cal.getTimeInMillis())));

        return dataFutura;
    }

}
