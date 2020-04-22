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
        holder.mdate.setText(mRecorslist.get(position).getDateCreated());
        holder.mdoctorName.setText(currentItem.getDoctorName());
        holder.mrid.setText(currentItem.getRid());
        holder.mhname.setText(currentItem.getHospital());
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
        public TextView type;


        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            mdoctorName =itemView.findViewById(R.id.d_name);
            mhname =itemView.findViewById(R.id.hname);
            mpatientName =itemView.findViewById(R.id.patient_name);
            mdate=itemView.findViewById(R.id.record_date);
            mrid= itemView.findViewById(R.id.Rid);
            type= itemView.findViewById(R.id.Type);


            box=itemView.findViewById(R.id.chk);

            box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int position =getAdapterPosition();
                    if (box.isChecked()) {
                        mRecorslist.get(position).setCk(true);
                        Rids.add(mRecorslist.get(position).getRid());
                    } else {
                        mRecorslist.get(position).setCk(false);
                        Rids.remove(mRecorslist.get(position).getRid());
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
