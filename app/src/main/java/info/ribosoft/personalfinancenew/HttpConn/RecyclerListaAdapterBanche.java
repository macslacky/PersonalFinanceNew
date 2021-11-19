package info.ribosoft.personalfinancenew.HttpConn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import info.ribosoft.personalfinancenew.R;

public class RecyclerListaAdapterBanche extends RecyclerView.Adapter<RecyclerListaAdapterBanche.
        RecyclerListaHolderBanche> {
    //creating a variable for our array list and context
    private ArrayList<RecyclerListaBanche> courseDataArrayListBanche;
    private Context context;
    private int i=1;

    // creating a constructor class
    public RecyclerListaAdapterBanche(ArrayList<RecyclerListaBanche> recyclerDataArrayListBanche,
        Context context) {
        this.courseDataArrayListBanche = recyclerDataArrayListBanche;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerListaAdapterBanche.RecyclerListaHolderBanche onCreateViewHolder(@NonNull
        ViewGroup parent, int viewType) {
        // inflate layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_banche,
                parent, false);
        return new RecyclerListaAdapterBanche.RecyclerListaHolderBanche(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerListaAdapterBanche.RecyclerListaHolderBanche
                                         holderBanche, int position) {
        // set the data to textview fromour modal class
        RecyclerListaBanche modal = courseDataArrayListBanche.get(position);
        if (i==1) {
            i=0;
            holderBanche.courseLytListaBanche.setBackgroundColor(ContextCompat.getColor(context,
                    R.color.riga1));
        } else {
            i=1;
            holderBanche.courseLytListaBanche.setBackgroundColor(ContextCompat.getColor(context,
                    R.color.riga2));
        }
        holderBanche.courseIdBanca.setText(modal.getIdBanca());
        holderBanche.courseNomeBanca.setText(modal.strNomeBanca);

        double saldoEntrata = Double.parseDouble(modal.strSaldoEntrata);
        double saldoUscita = Double.parseDouble(modal.strSaldoUscita);
        double saldoBanca = saldoEntrata - saldoUscita;
        ClassFunzioni cFunzioni = new ClassFunzioni();
        // colors the ammount green if entering, red if leaving
        if (saldoBanca<0) {
            holderBanche.courseSaldoBanca.setTextColor(ContextCompat.getColor(context,
                R.color.colorUscite));
            holderBanche.courseSaldoBanca.setText(cFunzioni.formatEuro(saldoBanca*-1));
        } else {
            holderBanche.courseSaldoBanca.setTextColor(ContextCompat.getColor(context,
                R.color.colorEntrate));
            holderBanche.courseSaldoBanca.setText(cFunzioni.formatEuro(saldoBanca));
        }

        holderBanche.courseSaldoEntrata.setText(cFunzioni.formatEuro(saldoEntrata));
        holderBanche.courseSaldoUscita.setText(cFunzioni.formatEuro(saldoUscita));
    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return courseDataArrayListBanche.size();
    }

    // view holder class to handle recycler view
    public class RecyclerListaHolderBanche extends RecyclerView.ViewHolder {
        // creating variables for our views
        private  LinearLayout courseLytListaBanche;
        private TextView courseIdBanca, courseNomeBanca, courseSaldoBanca, courseSaldoEntrata,
                courseSaldoUscita;

        public RecyclerListaHolderBanche(@NonNull View itemView) {
            super(itemView);
            // initializing our views with their ids
            courseLytListaBanche = itemView.findViewById(R.id.lytBancheLista);
            courseIdBanca = itemView.findViewById(R.id.txtBancheidBanca);
            courseNomeBanca = itemView.findViewById(R.id.txtBancheNomeBanca);
            courseSaldoBanca = itemView.findViewById(R.id.txtBancheSaldo);
            courseSaldoEntrata = itemView.findViewById(R.id.txtBancheSaldoEntrata);
            courseSaldoUscita = itemView.findViewById(R.id.txtBancheSaldoUscita);
        }
    }
}
