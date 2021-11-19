package info.ribosoft.personalfinancenew.HttpConn;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerItemClickListener   implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener myListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
        public void onLongItemClick(View view, int position);
    }

    // raccogliere dati sugli eventi di tocco
    GestureDetector myGestureDetector;

    public RecyclerItemClickListener(Context context, final RecyclerView recyclerView,
        OnItemClickListener listener) {
        myListener = listener;
        myGestureDetector = new GestureDetector(context, new GestureDetector
            .SimpleOnGestureListener() {

            @Override
            // singolo click
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return true;
            }

            @Override
            // click prolungato
            public void onLongPress(MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(),
                    motionEvent.getY());
                if (child!=null && myListener!=null) {
                    myListener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child));
                }
            }
        });
    }

    @Override
    // intercetta un evento
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        View childView = view.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (childView!=null && myListener!=null && myGestureDetector.onTouchEvent(motionEvent)) {
            myListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            return true;
        }
        return false;
    }

    @Override
    // intercetta gli eventi
    public  void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {}

    @Override
    // disabilita l'intercettazione degli eventi
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
}
