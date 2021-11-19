package info.ribosoft.personalfinancenew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import info.ribosoft.personalfinancenew.HttpConn.ClassFunzioni;
import info.ribosoft.personalfinancenew.HttpConn.DBDatiBanca;
import info.ribosoft.personalfinancenew.HttpConn.DBHelper;
import info.ribosoft.personalfinancenew.HttpConn.RecyclerListaMovimenti;

public class NuovoMovimentoActivity extends AppCompatActivity {
    private LinearLayout lytNewMovCancella;
    private String strIdBanca;
    double oldImporto;
    String saldoIn, saldoOut;
    private Intent intent;
    private int tipoMov;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuovo_movimento);

        ClassFunzioni cFunzioni = new ClassFunzioni();

        tipoMov = 1;
        intent = getIntent();
        // read data passed as parameters
        String strTipo = intent.getStringExtra("tipo");
        String strIdMov = intent.getStringExtra("idmov");
        strIdBanca = intent.getStringExtra("idbanca");
        String strNomeBanca = intent.getStringExtra("nomeBanca");

        TextView txtNewMovData = findViewById(R.id.txtNewMovData);
        TextView txtNewMovValuta = findViewById(R.id.txtNewMovValuta);
        EditText txtNewMovImporto = findViewById(R.id.txtNewMovImporto);
        RadioButton btnEntrata = findViewById(R.id.btnEntrata);
        RadioButton btnUscita = findViewById(R.id.btnUscita);
        EditText txtNewMovNote = findViewById(R.id.txtNewMovNote);

        lytNewMovCancella = findViewById(R.id.lytNewMovCancella);
        if (strTipo.equals("modifica")) {
            lytNewMovCancella.setVisibility(View.VISIBLE);

            DBHelper myDBBanche = new DBHelper(this);
            // read the bank's incoming and outcoming balance
            DBDatiBanca myDBDatiBanca = myDBBanche.leggiSaldoBanca(strIdBanca);
            saldoIn = myDBDatiBanca.getSaldoEntrata();
            saldoOut = myDBDatiBanca.getSaldoUscita();
            // read signle movement
            RecyclerListaMovimenti myDBMovimenti = myDBBanche.leggiSingoloMov(strIdMov);

            txtNewMovData.setText(myDBMovimenti.getData());
            txtNewMovValuta.setText(myDBMovimenti.getValuta());
            // read the amount before any modifcation
            oldImporto = Double.parseDouble(myDBMovimenti.getImporto());
            // set the various parameters if it is an outgoing or entering movement
            if (oldImporto<0) {
                tipoMov = -1;
                txtNewMovImporto.setTextColor(ContextCompat.getColor(getApplicationContext(),
                    R.color.colorUscite));
                btnUscita.setChecked(true);
            } else {
                tipoMov = 1;
                txtNewMovImporto.setTextColor(ContextCompat.getColor(getApplicationContext(),
                    R.color.colorEntrate));
                btnEntrata.setChecked(true);
            }
            // format the amount to be displayed
            txtNewMovImporto.setText(cFunzioni.strImporto(oldImporto*tipoMov));
            // read note
            txtNewMovNote.setText(myDBMovimenti.getNote());
        }
        if (strTipo.equals("nuovo")) {
            lytNewMovCancella.setVisibility(View.GONE);
        }

        TextView txtNewMovBanca = findViewById(R.id.txtNewMovBanca);
        // display the name of the bank
        txtNewMovBanca.setText(strNomeBanca);

        ImageButton ibtnNewMovData = findViewById(R.id.ibtnNewMovData);
        // activate listening on the button to enter the date of the movement
        ibtnNewMovData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cFunzioni.modificaData(NuovoMovimentoActivity.this, txtNewMovData);
            }
        });

        ImageButton ibtnNewMovValuta = findViewById(R.id.ibtnNewMovValuta);
        // activate listening on the button to enter the value date
        ibtnNewMovValuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cFunzioni.modificaData(NuovoMovimentoActivity.this, txtNewMovValuta);
            }
        });

        // if the user passes to another element, the function to format the amount is called
        txtNewMovImporto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) cFunzioni.formatImporto(txtNewMovImporto);
            }
        });

        RadioGroup rdgEntrataUscita = findViewById(R.id.rdgEntrataUscita);
        // activate listening on checked radio buttons
        rdgEntrataUscita.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.btnEntrata) { // displays the amount in green
                    txtNewMovImporto.setTextColor(ContextCompat.getColor(getApplicationContext(),
                        R.color.colorEntrate));
                    tipoMov = 1;
                } else if (checkedId==R.id.btnUscita) { // displays the amount in red
                    txtNewMovImporto.setTextColor(ContextCompat.getColor(getApplicationContext(),
                        R.color.colorUscite));
                    tipoMov = -1;
                }
            }
        });

        ImageButton ibtnNewMovSalva = findViewById(R.id.ibtnNewMovSalva);
        // activate listening on the save button
        ibtnNewMovSalva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // format the ammount
                cFunzioni.formatImporto(txtNewMovImporto);
                boolean flgSalva = true;
                if (txtNewMovData.length()==0) { // check if the movement date has benn entered
                    flgSalva = false;
                    Toast.makeText(getApplicationContext(), "Inserire la data del movimento",
                        Toast.LENGTH_LONG).show();
                }
                if (txtNewMovValuta.length()==0) { // check if the movement date has benn entered
                    flgSalva = false;
                    Toast.makeText(getApplicationContext(), "Inserire la valuta del movimento",
                        Toast.LENGTH_LONG).show();
                }

                if (flgSalva) {
                    DBHelper myDBConti = new DBHelper(getApplicationContext());

                    if (strTipo.equals("modifica")) {
                        // updates the bank balance
                        aggiornaSaldo(oldImporto, Double.parseDouble(saldoIn),
                            Double.parseDouble(saldoOut),
                            Double.parseDouble(txtNewMovImporto.getText().toString()), tipoMov);
                        double depo = Double.parseDouble(txtNewMovImporto.getText().toString())
                            * tipoMov;
                        myDBConti.aggiornaMovimento(strIdMov, cFunzioni.strImporto(depo),
                            cFunzioni.filtroData(txtNewMovData.getText().toString()),
                            cFunzioni.filtroData(txtNewMovValuta.getText().toString()),
                            txtNewMovNote.getText().toString());
                    } else {
                        double depo = Double.parseDouble(txtNewMovImporto.getText().toString());
                        // read the banks incoming and outgoing balance
                        DBDatiBanca myDBDatiBanca = myDBConti.leggiSaldoBanca(strIdBanca);
                        double depoIn = Double.parseDouble(myDBDatiBanca.getSaldoEntrata());
                        double depoOut = Double.parseDouble(myDBDatiBanca.getSaldoUscita());
                        // updates the bank balance with the new movement
                        if (tipoMov > 0) depoIn += depo;
                        else depoOut += depo;

                        //updates the bank balance
                        myDBConti.ScriviSaldoBanca(strIdBanca, depoIn, depoOut);

                        depo = tipoMov * Double.parseDouble(txtNewMovImporto.getText().toString());
                        // writes a new movement in the table
                        myDBConti.scriviNuovoMovimento(strIdBanca, cFunzioni.strImporto(depo),
                            txtNewMovData.getText().toString(), txtNewMovValuta.getText()
                            .toString(), txtNewMovNote.getText().toString());
                    }
                    // return to the activity listing the movements
                    intent = new Intent(getApplicationContext(), MovimentiBancaActivity.class);
                    intent.putExtra("id", strIdBanca);
                    startActivity(intent);
                }
            }
        });

        ImageButton ibtnCancella = findViewById(R.id.ibtnNewMovCancella);
        // activate listening on the delete button
        ibtnCancella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // updates the bank balance
                aggiornaSaldo(oldImporto, Double.parseDouble(saldoIn), Double.parseDouble(saldoOut),
                    0.0,1);
                DBHelper myDBConti = new DBHelper(getApplicationContext());
                // cancels the indicated Movement
                myDBConti.CancellaMovimento(strIdMov);
                intent = new Intent(getApplicationContext(), MovimentiBancaActivity.class);
                intent.putExtra("id", strIdBanca);
                startActivity(intent);
            }
        });

        // activate listening on the cancel button
        ImageButton ibtnAnnulla = findViewById(R.id.ibtnNewMovAnnulla);
        ibtnAnnulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), MovimentiBancaActivity.class);
                intent.putExtra("id", strIdBanca);
                startActivity(intent);
            }
        });
    }

    // updates the movement
    private void aggiornaSaldo(double oldImporto, double saldoIn, double saldoOut, double importo,
            int tipoMov) {
        if (oldImporto<0) {saldoOut += oldImporto;} // Uscita
        else              {saldoIn -= oldImporto;} // Entrata

        if (tipoMov==1) {saldoIn += importo;}
        else            {saldoOut += importo;}
        DBHelper myDBConti = new DBHelper(this);
        myDBConti.ScriviSaldoBanca(strIdBanca, saldoIn, saldoOut);
    }
}
