package info.ribosoft.personalfinancenew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import info.ribosoft.personalfinancenew.HttpConn.DBHelper;

public class NuovaBancaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuova_banca);

        EditText txtNomeBanca = findViewById(R.id.txtNewBancaNome);

        ImageButton ibtnSalva = findViewById(R.id.ibtnNuovaBancaSalva);
        // activate listening on the save button
        ibtnSalva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper myDBBanche = new DBHelper(getApplicationContext());
                // writes a new bank in the table
                myDBBanche.scriveNuovaBanca(txtNomeBanca.getText().toString());
                vaiMainActivity();
            }
        });

        ImageButton ibtnAnnulla = findViewById(R.id.ibtnNuovaBancaAnnulla);
        // activate listening on the cancel button
        ibtnAnnulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vaiMainActivity();
            }
        });
    }

    private void vaiMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}