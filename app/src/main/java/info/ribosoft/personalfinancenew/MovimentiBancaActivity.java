package info.ribosoft.personalfinancenew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import info.ribosoft.personalfinancenew.HttpConn.ClassFunzioni;
import info.ribosoft.personalfinancenew.HttpConn.DBDatiBanca;
import info.ribosoft.personalfinancenew.HttpConn.DBHelper;
import info.ribosoft.personalfinancenew.HttpConn.RecyclerItemClickListener;
import info.ribosoft.personalfinancenew.HttpConn.RecyclerListaAdapterMovimenti;
import info.ribosoft.personalfinancenew.HttpConn.RecyclerListaMovimenti;

public class MovimentiBancaActivity extends AppCompatActivity {
    private DBHelper myDBMovimenti;
    private ArrayList<RecyclerListaMovimenti> recArrayMovimenti;
    private RecyclerListaAdapterMovimenti recAdapterMovimenti;
    private RecyclerView rcwListaMovimenti;
    private TextView txtMovNome;
    private String strIdBanca;
    private boolean blnFiltro=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimenti_banca);

        myDBMovimenti = new DBHelper(this);

        // read the passed parameter
        Intent intent = getIntent();
        strIdBanca = intent.getStringExtra("id");

        DBHelper myDBBanche = new DBHelper(this);
        DBDatiBanca myDBDatiBanca = myDBBanche.leggiSaldoBanca(strIdBanca);

        txtMovNome = findViewById(R.id.txtMovNome);
        txtMovNome.setText(myDBDatiBanca.getNomeBanca());

        // calculate bank balance
        double dblSaldo = Double.parseDouble(myDBDatiBanca.getSaldoEntrata()) -
            Double.parseDouble(myDBDatiBanca.getSaldoUscita());
        TextView txtMovSaldo = findViewById(R.id.txtMovSaldo);

        Context context = getApplicationContext();

        // colors the ammount green if entering, red if leaving
        if (dblSaldo<0) {
            txtMovSaldo.setTextColor(ContextCompat.getColor(context, R.color.colorUscite));
        } else {
            txtMovSaldo.setTextColor(ContextCompat.getColor(context, R.color.colorEntrate));
        }
        ClassFunzioni cFunzioni = new ClassFunzioni();
        txtMovSaldo.setText(cFunzioni.formatEuro(dblSaldo));

        ImageButton ibtnMovFiltra = findViewById(R.id.ibtnMovFiltra);
        // displays or hides the bar where you can select the start and end date
        ibtnMovFiltra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visualizzaNascondiFiltro();
            }
        });

        TextView txtMovDataInizio = findViewById(R.id.txtMovDataInizio);
        ImageButton ibtnMovDataInizio = findViewById(R.id.ibtnMovDataInizio);
        // select start date
        ibtnMovDataInizio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cFunzioni.modificaData(MovimentiBancaActivity.this, txtMovDataInizio);
            }
        });
        TextView txtMovDataFine = findViewById(R.id.txtMovDataFine);
        ImageButton ibtnMovDataFine = findViewById(R.id.ibtnMovDataFine);
        // select end date
        ibtnMovDataFine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cFunzioni.modificaData(MovimentiBancaActivity.this, txtMovDataFine);
            }
        });

        // apply the filter to view transactions that fall between the two dates
        ImageButton ibtnMovApplicaFiltro = findViewById(R.id.ibtnMovApplicaFiltro);
        ibtnMovApplicaFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flgControllo = true;
                // check that the starting date has been selected
                if (txtMovDataInizio.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Manca la data d'inizio",
                        Toast.LENGTH_LONG).show();
                    flgControllo = false;
                }
                // verify the correctness of the two dates
                if (flgControllo) {
                    String strInizio = cFunzioni.filtroData(txtMovDataInizio.getText().toString());
                    String strFine = cFunzioni.filtroData(txtMovDataFine.getText().toString());
                    int r = strInizio.compareTo(strFine);
                    // reports if the end date is less than the start date
                    if (r>0) {
                        Toast.makeText(getApplicationContext(),
                            "La data d'inizio è inferiore della data di fine",
                            Toast.LENGTH_LONG).show();
                    }
                    else {
                        // the data entered is correct so it hides the row for data input
                        visualizzaNascondiFiltro();
                        //view the movements
                        listaMovimenti(context, true, strInizio, strFine);
                    }
                }
            }
        });

        // creating new array list
        recArrayMovimenti = new ArrayList<>();
        rcwListaMovimenti = findViewById(R.id.rcwMovListaMovimenti);

        // activate listening on the list
        rcwListaMovimenti.addOnItemTouchListener(new RecyclerItemClickListener(context,
            rcwListaMovimenti, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    // starts the activity to view the movements of the selected bank
                    Intent intent = new Intent(getApplicationContext(),
                        NuovoMovimentoActivity.class);
                    String strIdMovimento = recAdapterMovimenti.leggiIdMovimento(position);
                    // read idbanche of the selected bank by clicking
                    RecyclerListaMovimenti rcIdBanca = recArrayMovimenti.get(position);
                    intent.putExtra("tipo", "modifica");
                    intent.putExtra("idbanca", strIdBanca);
                    intent.putExtra("idmov", strIdMovimento);
                    intent.putExtra("nomeBanca", txtMovNome.getText());
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onLongItemClick(View view, int position) {}
        }));

        // read the movements related to a bank
        listaMovimenti(context, false, "", "");

    }

    // defines the menu items
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movbanca_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem mItem) {
        Intent intent;

        int id = mItem.getItemId();
        if (id==R.id.movbanca_NewMovimento) { // insert new movement
            intent = new Intent(getApplicationContext(), NuovoMovimentoActivity.class);
            intent.putExtra("tipo", "nuovo");
            intent.putExtra("idbanca", strIdBanca);
            intent.putExtra("idmov", "0");
            intent.putExtra("nomeBanca", txtMovNome.getText());
            startActivity(intent);
        } else if (id==R.id.movbanca_ListaBanche) { // returns to the main activity
            intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        return false;
    }

    // read the movements related to a bank
    private void listaMovimenti(Context context, boolean blnFiltro, String strInizio,
        String strFine) {
        // fills the array with the data read from the table
        recArrayMovimenti = myDBMovimenti.leggiMovimenti(strIdBanca, blnFiltro, strInizio, strFine);
        for (int i=0; i<recArrayMovimenti.size(); i++) {
            recAdapterMovimenti = new RecyclerListaAdapterMovimenti(recArrayMovimenti, context);

            //set the layout manager for our recycler view
            LinearLayoutManager manager = new LinearLayoutManager(context);
            // setting layout manager for our recycler view
            rcwListaMovimenti.setLayoutManager(manager);

            // set the adapter to our recycler view
            rcwListaMovimenti.setAdapter(recAdapterMovimenti);
        }
    }

    // view or hide the row for entering the start and end dates
    private void visualizzaNascondiFiltro() {
        LinearLayout lnlFiltro = findViewById(R.id.lnlFiltro);
        blnFiltro = !blnFiltro;
        if (blnFiltro) {
            lnlFiltro.setVisibility(View.VISIBLE);
        } else {
            lnlFiltro.setVisibility(View.GONE);
        }
    }
}