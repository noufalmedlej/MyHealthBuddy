package com.example.myhealthbuddy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {
 private ArrayList<item_record> mRecorslist;
    private ArrayList<String>  Rids;
    private OnItemClickListener listener;
    Context c;


    public RecordAdapter(Context c , ArrayList<item_record> records) {
        mRecorslist=records;
        this.c=c;
        Rids=new ArrayList<String>();
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View v= LayoutInflater.from(c).inflate(R.layout.record_item,parent, false);
         RecordViewHolder rvh = new RecordViewHolder(v);
         return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecordViewHolder holder, final int position) {

        item_record currentItem = mRecorslist.get(position);
        //the same as
        holder.mdate.setText(mRecorslist.get(position).getDate());
        holder.mdoctorName.setText(currentItem.getDoctorName());
        holder.mpatientName.setText(currentItem.getPatientName());
        holder.mrid.setText(currentItem.getRid());
        holder.mhname.setText(currentItem.getHname());
       /* holder.box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.box.isChecked()) {
                    //mRecorslist.get(position).setCk(true);
                    //Rids.add(mRecorslist.get(position).getRid());
                    Toast.makeText(c, "Checked  ", Toast.LENGTH_SHORT).show();
                } else {
                    //mRecorslist.get(position).setCk(false);
                    //Rids.remove(mRecorslist.get(position).getRid());
                    Toast.makeText(c, "NOT Checked  ", Toast.LENGTH_SHORT).show();

                }
            }
        });

        Intent n = new Intent(c, ShareRecord.class);
        n.putExtra("list",mRecorslist );*/
    }

    @Override
    public int getItemCount() {
        return mRecorslist.size() ;
    }



    public class RecordViewHolder extends RecyclerView.ViewHolder {
        public TextView mdate;
        public TextView mrid;
        public TextView mdoctorName;
        public TextView mpatientName;
        public TextView mhname;
        public CheckBox box;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            mdoctorName =itemView.findViewById(R.id.d_name);
            mhname =itemView.findViewById(R.id.hname);
            mpatientName =itemView.findViewById(R.id.patient_name);
            mdate=itemView.findViewById(R.id.record_date);
            mrid= itemView.findViewById(R.id.Rid);
            box=itemView.findViewById(R.id.chk);

            box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int position =getAdapterPosition();
                    if (box.isChecked()) {
                        mRecorslist.get(position).setCk(true);
                        Rids.add(mRecorslist.get(position).getRid());
                        // Toast.makeText(c, "Checked  ", Toast.LENGTH_SHORT).show();
                    } else {
                        mRecorslist.get(position).setCk(false);
                        Rids.remove(mRecorslist.get(position).getRid());
                        //Toast.makeText(c, "NOT Checked  ", Toast.LENGTH_SHORT).show();

                    }


                    if(position!= RecyclerView.NO_POSITION && listener!= null){
                        listener.onItemClick(Rids);
                    }


                }
            });

            Intent n = new Intent(c, ShareRecord.class);
            n.putExtra("list",mRecorslist );

        }
    }
    public interface OnItemClickListener {
        void onItemClick(ArrayList<String> mRecorslist);

    }
    public void setOnItemClickListener (OnItemClickListener listener){
        this.listener=listener;
    }


}