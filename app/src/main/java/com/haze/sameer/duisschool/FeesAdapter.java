package com.haze.sameer.duisschool;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class FeesAdapter extends RecyclerView.Adapter<FeesAdapter.FeesObjectViewHolder> {

    private Context mCtx;
    private List<FeesObject> feesList;

    public FeesAdapter(Context mCtx, List<FeesObject> feesList) {
        this.mCtx = mCtx;
        this.feesList = feesList;
    }

    @Override
    public FeesObjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.fees_model, null);
        return new FeesObjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeesObjectViewHolder holder, int position) {
        FeesObject feesValue = feesList.get(position);
        holder.typeTxt.setText(feesValue.getTypeFees());
        holder.groupTxt.setText(feesValue.getGroupFees());
        holder.amountTxt.setText(feesValue.getAmountFees());
        holder.dueDateTxt.setText(feesValue.getDueDateFees());
        holder.isactiveTxt.setText(feesValue.getIsactiveFees());
    }

    @Override
    public int getItemCount() {
        return feesList.size();
    }

    class FeesObjectViewHolder extends RecyclerView.ViewHolder {
        TextView typeTxt,groupTxt,amountTxt,dueDateTxt,isactiveTxt;
        public FeesObjectViewHolder(View itemView) {
            super(itemView);
            typeTxt = itemView.findViewById(R.id.feesmodel_feeTypeTxt);
            groupTxt = itemView.findViewById(R.id.feesmodel_feeGroupTxt);
            amountTxt = itemView.findViewById(R.id.feesmodel_amountTxt);
            dueDateTxt = itemView.findViewById(R.id.feesmodel_dueTxt);
            isactiveTxt = itemView.findViewById(R.id.feesmodel_activeTxt);
        }
    }
}