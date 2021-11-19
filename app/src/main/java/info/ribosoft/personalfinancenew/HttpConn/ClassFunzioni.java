package info.ribosoft.personalfinancenew.HttpConn;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;
import static java.lang.String.format;

public class ClassFunzioni {
    // creates a dialog that allows the user to select the date
    private void gestioneData(Context context, TextView txtData, int giorno, int mese, int anno) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
            new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String data = format(Locale.US,"%02d/%02d/%04d", day, month+1, year);
                txtData.setText(data);
            }
        }, anno, mese, giorno);
        datePickerDialog.show();
    }
    // reads and changes the date
    public void
    modificaData(Context context, TextView txtData) {
        int giorno, mese, anno;

        if (txtData.length() > 0) { // reads the previously selected date
            anno = Integer.parseInt(txtData.getText().toString().substring(6));
            mese = Integer.parseInt(txtData.getText().toString().substring(3, 5)) - 1;
            giorno = Integer.parseInt(txtData.getText().toString().substring(0, 2));
        } else {
            // read the current date
            Calendar calendar = Calendar.getInstance();
            anno = calendar.get(Calendar.YEAR);
            mese = calendar.get(Calendar.MONTH);
            giorno = calendar.get(Calendar.DAY_OF_MONTH);
        }
        // creates a dialog that allows the user to select the date
        gestioneData(context, txtData, giorno, mese, anno);
    }

    // format the ammount
    public void formatImporto(EditText txtImporto) {
        String strDepo = txtImporto.getText().toString();
        if (strDepo.length() == 0) {
            strDepo = "0.00";
        } else {
            strDepo = strImporto(Double.parseDouble(strDepo));
        }
        txtImporto.setText(strDepo);
    }

    // formats the number of digits after the comma
    public String strImporto(double importo) {
        return String.format(Locale.US, "%.2f", importo);
    }

    // format the number in euro format
    public String formatEuro(double importo) {
        NumberFormat numForm = new DecimalFormat("#,##0.00");
        return numForm.format(importo);
    }

    // format the date to be displayed
    public String formatData(String datain) {
        // day/month/year
        return datain.substring(6) + "/" + datain.substring(4, 6) + "/" + datain.substring(0, 4);
    }

    // format the date to be recorded in the database
    public String filtroData(String dataIn) {
        // yearmonthday
        return dataIn.substring(6) + dataIn.substring(3, 5) + dataIn.substring(0, 2);
    }
}
