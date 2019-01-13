package com.haze.sameer.duisschool;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationObjectViewHolder> {

    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<NotificationObject> notiList;

    //getting the context and product list with constructor
    public NotificationAdapter(Context mCtx, List<NotificationObject> notiList) {
        this.mCtx = mCtx;
        this.notiList = notiList;
    }

    @Override
    public NotificationObjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.notification_model, null);
        return new NotificationObjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationObjectViewHolder holder, int position) {
        //getting the product of the specified position
        NotificationObject noti = notiList.get(position);

//        Random rnd = new Random();
//        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        //binding the data with the viewholder views
        String titleString = noti.getTitleNoti();
        String msgString = noti.getMsgNoti();
        holder.titleTxt.setText(Html.fromHtml(titleString));
        holder.msgTxt.setText(Html.fromHtml(msgString));
        holder.dateTxt.setText(noti.getDateNoti());
        holder.unreadRela.setVisibility((noti.isUnreadNoti()) ? View.VISIBLE : View.INVISIBLE);
//        holder.overRela.setBackgroundColor(color);
        holder.overRela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(holder.itemView.getContext(),DetailedNotificationActivity.class);
                i.putExtra("detailedTitle",noti.getTitleNoti());
                i.putExtra("detailedMsg",noti.getMsgNoti());
                i.putExtra("detailedDate",noti.getDateNoti());
                holder.itemView.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return notiList.size();
    }

    class NotificationObjectViewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt,msgTxt,dateTxt;
        RelativeLayout unreadRela,overRela;
        public NotificationObjectViewHolder(View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.notificationModel_titleTxt);
            msgTxt = itemView.findViewById(R.id.notificationModel_msgTxt);
            dateTxt = itemView.findViewById(R.id.notificationModel_dateTxt);
            unreadRela = itemView.findViewById(R.id.notificationModel_unreadRela);
            overRela = itemView.findViewById(R.id.notificationModel_overRela);
        }
    }
}

