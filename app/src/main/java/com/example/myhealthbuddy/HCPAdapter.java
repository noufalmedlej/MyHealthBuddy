package com.example.myhealthbuddy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HCPAdapter  extends RecyclerView.Adapter<HCPAdapter.HCPViewHolder> {

    private ArrayList<HCPsResult> HCPsList;
    private HCPAdapter.OnItemClickListener listener;
    Context c;

    public HCPAdapter(Context c, ArrayList<HCPsResult> list) {
        HCPsList = list;
        this.c = c;
    }

    @NonNull
    @Override
    public HCPViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.view_hcp, parent, false);
        HCPViewHolder hcpvh = new HCPViewHolder(v);
        return hcpvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final HCPViewHolder holder, final int position) {

        HCPsResult currentItem = HCPsList.get(position);
        //the same as
        holder.myname.setText(currentItem.getName());
        holder.myid.setText(currentItem.getId());
        holder.myspecialty.setText(currentItem.getSpecialty());
        holder.mygender.setText(currentItem.getGender());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!= null){
                    listener.onItemClick(HCPsList.get(position).getId(),HCPsList.get(position).getName(),HCPsList.get(position).getUid());
                    //Intent n = new Intent(c, .class);
                    // n.putExtra("Rid",mRecorslist );
                }
            }
        });

       // if(currentItem.getGender().equals("F")) set imge


    }
    @Override
    public int getItemCount() {
        return HCPsList.size() ;
    }

    public class HCPViewHolder extends RecyclerView.ViewHolder {
        public TextView myname;
        public TextView myid;
        public TextView myspecialty;
        public TextView mygender;


        //public TextView img;


        public HCPViewHolder(@NonNull View itemView) {
            super(itemView);
            myname =itemView.findViewById(R.id.all_HCP_profile_name);
            myid =itemView.findViewById(R.id.all_HCP_profile_Id);
            myspecialty=itemView.findViewById(R.id.all_HCP_profile_specialty);
            mygender= itemView.findViewById(R.id.all_HCP_profile_gender);

            Intent n = new Intent(c, MyHCPs.class);
            n.putExtra("list",HCPsList );

        }


    }
    public interface OnItemClickListener {
        void onItemClick(String HCPID,String Name,String HCPuId);

    }
    public void setOnItemClickListener (HCPAdapter.OnItemClickListener listener){
        this.listener=listener;
    }


}
