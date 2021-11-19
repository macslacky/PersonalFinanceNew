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

public class RecyclerListaAdapterMovimenti extends RecyclerView.Adapter<
    RecyclerListaAdapterMovimenti.RecyclerListaHolderMovimenti>{
    // creating a variable for our array list and context
    private ArrayList<RecyclerListaMovimenti> courseDataArrayListMovimenti;
    private Context context;
    private int i=1;

    // creating a costructor class
    public RecyclerListaAdapterMovimenti(ArrayList<RecyclerListaMovimenti>
        recDataArrayListMovimenti, Context context) {
        this.courseDataArrayListMovimenti = recDataArrayListMovimenti;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerListaAdapterMovimenti.RecyclerListaHolderMovimenti onCreateViewHolder(@NonNull
        ViewGroup parent, int viewType) {

        // inflate layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_movimenti,
            parent, false);
        return new RecyclerListaAdapterMovimenti.RecyclerListaHolderMovimenti(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerListaAdapterMovimenti
        .RecyclerListaHolderMovimenti holderMovimenti, int position) {
        // set the data to textview from our modal class
        RecyclerListaMovimenti modal = courseDataArrayListMovimenti.get(position);
        int color;
        if (i==1) {
            i=0;
            color = ContextCompat.getColor(context, R.color.riga1);
        } else {
            i=1;
            color = ContextCompat.getColor(context, R.color.riga2);
        }
        holderMovimenti.courseLytListaMovimenti.setBackgroundColor(color);
        holderMovimenti.courseIdMovimento.setText(modal.getIdMovimento());
        holderMovimenti.courseMovDataValuta.setText(modal.getValuta());

        double importo = Double.parseDouble(modal.getImporto());
        ClassFunzioni cFunzioni = new ClassFunzioni();
        // colors the ammount green if entering, red if leaving
        if (importo<0) {
            holderMovimenti.courseMovImporto.setTextColor(ContextCompat.getColor(context,
                R.color.colorUscite));
            holderMovimenti.courseMovImporto.setText(cFunzioni.formatEuro(importo*-1));
        } else {
            holderMovimenti.courseMovImporto.setTextColor(ContextCompat.getColor(context,
                R.color.colorEntrate));
            holderMovimenti.courseMovImporto.setText(cFunzioni.formatEuro(importo));
        }

        holderMovimenti.courseMovNote.setText(modal.getNote());
    }

    public String leggiIdMovimento(int position) {
        RecyclerListaMovimenti modal = courseDataArrayListMovimenti.get(position);
        return modal.getIdMovimento();
    }

    @Override
    public int getItemCount() {
        // this method return the size of recyclerview
        return courseDataArrayListMovimenti.size();
    }

    // view holder class to handle recycler view
    public class RecyclerListaHolderMovimenti extends RecyclerView.ViewHolder {
        // creating variables for our views
        private LinearLayout courseLytListaMovimenti;
        private TextView courseIdMovimento, courseMovDataValuta, courseMovImporto, courseMovNote;

        public RecyclerListaHolderMovimenti(@NonNull View itemView) {
            super(itemView);
            // initializing our views with their ids
            courseLytListaMovimenti = itemView.findViewById(R.id.lytMovimentiLista);
            courseIdMovimento = itemView.findViewById(R.id.txtMovId);
            courseMovDataValuta = itemView.findViewById(R.id.txtMovDataValuta);
            courseMovImporto = itemView.findViewById(R.id.txtMovImporto);
            courseMovNote = itemView.findViewById(R.id.txtMovNote);
        }
    }
}
