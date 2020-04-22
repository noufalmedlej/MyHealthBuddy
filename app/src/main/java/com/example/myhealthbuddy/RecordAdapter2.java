package com.example.myhealthbuddy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecordAdapter2 extends RecyclerView.Adapter<RecordAdapter2.RecordViewHolder> {
    private ArrayList<item_record> mRecorslist;
    private RecordAdapter2.OnItemClickListener listener;
    Context c;


    public RecordAdapter2(Context c , ArrayList<item_record> records) {
        mRecorslist=records;
        this.c=c;
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
        holder.mdate.setText(currentItem.getDateCreated());
        holder.mdoctorName.setText(currentItem.getDoctorName());
        holder.mrid.setText(currentItem.getRid());
        DatabaseReference Hospital= FirebaseDatabase.getInstance().getReference().child("Hospitals");
        Hospital.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.mhname.setText(dataSnapshot.child(mRecorslist.get(position).getHospital()).child("Name").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference name= FirebaseDatabase.getInstance().getReference().child("Patients");
        name.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.mpatientName.setText(dataSnapshot.child(mRecorslist.get(position).getPid()).child("name").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        int myType =currentItem.getType();
        if(myType==1)
            holder.type.setText("وصفة طبية");
        if(myType==2)
            holder.type.setText(" تحليل طبي ");
        if(myType==3)
            holder.type.setText(" أشعة");
        if(myType==4)
            holder.type.setText("علامات حيوية");
        if(myType==5)
            holder.type.setText("تقرير طبي ");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClick(mRecorslist.get(position).getRid());
                //Intent n = new Intent(c, .class);
               // n.putExtra("Rid",mRecorslist );

            }
        });

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
        public TextView type;


        public RecordViewHolder(@NonNull final View itemView) {
            super(itemView);
            mdoctorName =itemView.findViewById(R.id.d_name);
            mhname =itemView.findViewById(R.id.hname);
            mpatientName =itemView.findViewById(R.id.patient_name);
            mdate=itemView.findViewById(R.id.record_date);
            mrid= itemView.findViewById(R.id.Rid);
            type= itemView.findViewById(R.id.Type);

            box=itemView.findViewById(R.id.chk);
            box.setVisibility(View.INVISIBLE);
            box.setEnabled(false);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(String rid);

    }
    public void setOnItemClickListener (OnItemClickListener listener){
        this.listener=listener;
    }

}
