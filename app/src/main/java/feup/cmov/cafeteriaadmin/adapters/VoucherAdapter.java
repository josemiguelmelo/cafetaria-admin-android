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
import feup.cmov.cafeteriaadmin.models.voucher.Voucher;
import rx.Observable;
import rx.subjects.PublishSubject;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.ViewHolder> {
    private ArrayList<Voucher> mDataset;
    private ArrayList<Voucher> appliedVouchers;

    private final PublishSubject<Voucher> onClickSubject = PublishSubject.create();

    public Observable<Voucher> getPositionClicks(){
        return onClickSubject.asObservable();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView cv;
        public TextView text;
        public ViewHolder(View v) {
            super(v);
            text = (TextView)itemView.findViewById(R.id.voucher_info_text);
            cv = (CardView)itemView.findViewById(R.id.voucher_card_view);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public VoucherAdapter(ArrayList<Voucher> myDataset, ArrayList<Voucher> appliedVouchers) {
        mDataset = myDataset;
        this.appliedVouchers = appliedVouchers;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public VoucherAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.voucher_cardview, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Voucher element = mDataset.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("voucher-click", "Clicked: " + element.toString());
                onClickSubject.onNext(element);
            }
        });
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.text.setText(element.toString());
        if(this.appliedVouchers.contains(element))
        {
            holder.text.append(" - Applied");
        }

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
