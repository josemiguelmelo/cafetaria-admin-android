package feup.cmov.cafeteriaadmin.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import feup.cmov.cafeteriaadmin.R;
import feup.cmov.cafeteriaadmin.models.Item;
import rx.Observable;
import rx.subjects.PublishSubject;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private ArrayList<Item> mDataset;

    private final PublishSubject<Item> onClickSubject = PublishSubject.create();

    public Observable<Item> getPositionClicks(){
        return onClickSubject.asObservable();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView cv;
        public TextView text;
        public TextView price;
        public ViewHolder(View v) {
            super(v);
            text = (TextView)itemView.findViewById(R.id.info_text);
            price = (TextView)itemView.findViewById(R.id.info_price);
            cv = (CardView)itemView.findViewById(R.id.card_view);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ItemAdapter(ArrayList<Item> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Item element = mDataset.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("element-click", "Clicked: " + element.name);
                onClickSubject.onNext(element);
            }
        });
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.text.setText(element.name);
        holder.price.setText(element.showPrice());

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
