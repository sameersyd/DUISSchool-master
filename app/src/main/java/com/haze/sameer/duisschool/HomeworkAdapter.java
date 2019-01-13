package com.haze.sameer.duisschool;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class HomeworkAdapter extends RecyclerView.Adapter<HomeworkAdapter.HomeworkObjectViewHolder> {

    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<HomeworkObject> workList;

    //getting the context and product list with constructor
    public HomeworkAdapter(Context mCtx, List<HomeworkObject> workList) {
        this.mCtx = mCtx;
        this.workList = workList;
    }

    @Override
    public HomeworkObjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.homework_model, null);
        return new HomeworkObjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeworkObjectViewHolder holder, int position) {
        //getting the product of the specified position
        HomeworkObject work = workList.get(position);

        //binding the data with the viewholder views
        holder.teacherNameTxt.setText(work.getTeacherNameHomework());
        holder.dateTxt.setText(work.getDateHomework());
        holder.subEvaTxt.setText(work.getSubmissionHomework()+" / "+work.getEvaHomework());
        holder.classesTxt.setText(work.getClassesHomework());
        holder.sectionTxt.setText("Section: "+work.getSectionHomework());
        holder.subjectTxt.setText("Subject: "+work.getSubjectHomework());

    }

    @Override
    public int getItemCount() {
        return workList.size();
    }

    class HomeworkObjectViewHolder extends RecyclerView.ViewHolder {
        TextView teacherNameTxt,dateTxt,subEvaTxt,classesTxt,sectionTxt,subjectTxt;
        public HomeworkObjectViewHolder(View itemView) {
            super(itemView);
            teacherNameTxt = itemView.findViewById(R.id.homeworkModel_teacherNameTxt);
            dateTxt = itemView.findViewById(R.id.homeworkModel_dateTxt);
            subEvaTxt = itemView.findViewById(R.id.homeworkModel_subEvaDateTxt);
            classesTxt = itemView.findViewById(R.id.homeworkModel_classesTxt);
            sectionTxt = itemView.findViewById(R.id.homeworkModel_sectionTxt);
            subjectTxt = itemView.findViewById(R.id.homeworkModel_subjectTxt);
        }
    }

}

