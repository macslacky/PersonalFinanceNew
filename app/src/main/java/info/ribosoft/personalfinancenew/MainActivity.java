package info.ribosoft.personalfinancenew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import info.ribosoft.personalfinancenew.HttpConn.DBHelper;
import info.ribosoft.personalfinancenew.HttpConn.RecyclerItemClickListener;
import info.ribosoft.personalfinancenew.HttpConn.RecyclerListaAdapterBanche;
import info.ribosoft.personalfinancenew.HttpConn.RecyclerListaBanche;

public class MainActivity extends AppCompatActivity {
    private ArrayList<RecyclerListaBanche> recArrayBanche;
    private RecyclerListaAdapterBanche recAdapterBanche;
    private RecyclerView rcwListaBanche;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();
        // creating new array list
        recArrayBanche = new ArrayList<>();

        rcwListaBanche = findViewById(R.id.rcwListaBanche);
        // activate listening on the list
        rcwListaBanche.addOnItemTouchListener(new RecyclerItemClickListener(context, rcwListaBanche,
            new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // starts the activity to view the movements of the selected bank
                Intent intent = new Intent(getApplicationContext(), MovimentiBancaActivity.class);
                // read idbanche of the selected bank by clicking
                RecyclerListaBanche rcIdBanca = recArrayBanche.get(position);
                intent.putExtra("id", rcIdBanca.getIdBanca());
                startActivity(intent);
                finish();
            }

            @Override
            public void onLongItemClick(View view, int position) {}
        }));

        // read the banks table and associate the date to a recyclerview
        listaBanche(context);
    }

    // defines the menu item
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // reads the menu item selected by the user
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id==R.id.main_NuovaBanca) { // enter new bank
            Intent intent = new Intent(getApplicationContext(), NuovaBancaActivity.class);
            startActivity(intent);
            finish();
        } else if (id==R.id.main_about) { // view app information
            InfoApp infoApp = new InfoApp();
            infoApp.OnCreate(this);
        }
        return false;
    }

    // read the banks table and associate the data to a listview
    private void listaBanche(Context context) {
        DBHelper myDBBanche = new DBHelper(this);

        // fills the array with the data read from the table
        recArrayBanche = myDBBanche.leggiBanche();
        for (int i=0; i<recArrayBanche.size(); i++) {
            recAdapterBanche = new RecyclerListaAdapterBanche(recArrayBanche, context);

            // set the layout manager for our recycler view
            LinearLayoutManager manager = new LinearLayoutManager(context);
            // setting layout manager for our recycler view
            rcwListaBanche.setLayoutManager(manager);

            // set the adapter to our recycler view
            rcwListaBanche.setAdapter(recAdapterBanche);
        }
    }
}