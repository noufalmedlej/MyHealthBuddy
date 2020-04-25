package com.example.myhealthbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CanceledRequests extends Fragment {

    private RecyclerView declinedlist;
    private DatabaseReference requestsRef,PatientRef,docRef;
    FirebaseAuth mAuth;
    String uid,patientid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_canceled_requests,container,false);

        requestsRef= FirebaseDatabase.getInstance().getReference().child("Requests");
        PatientRef=FirebaseDatabase.getInstance().getReference().child("Patients");
        mAuth = FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid().toString();

        PatientRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                patientid=dataSnapshot.child("national_id").getValue().toString();
                displayDeclinedRequests(patientid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        declinedlist = (RecyclerView)view.findViewById(R.id.DeclinedRequests);
        declinedlist.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(view.getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        declinedlist.setLayoutManager(linearLayoutManager);


        return view;
    }



    private void displayDeclinedRequests(String patientid){
        Query query =requestsRef.child("DeclinedRequests").orderByChild("patient_id").startAt(patientid).endAt(patientid+"\uf8ff");


        FirebaseRecyclerAdapter<MyRequests, declinedViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<MyRequests, declinedViewHolder>(MyRequests.class,R.layout.display_requests, declinedViewHolder.class,query) {
                    @Override
                    protected void populateViewHolder(final declinedViewHolder requestsDeclinedViewHolder, MyRequests myRequests, int i) {
                        requestsDeclinedViewHolder.setDoctor_id(myRequests.getDoctor_id());
                        requestsDeclinedViewHolder.setType(myRequests.getType());
                        requestsDeclinedViewHolder.setDate(myRequests.getDate());
                        ///////////////////////////////////////////////////
                        requestsDeclinedViewHolder.setDeclined_date(myRequests.getDeclined_date());

                        PatientRef = FirebaseDatabase.getInstance().getReference().child("Doctors");
                        PatientRef.child(myRequests.getDoctor_uid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){

                                    String doctor_name =dataSnapshot.child("name").getValue().toString();

                                    requestsDeclinedViewHolder.setDoctorName(doctor_name);}

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                        final String RequestKey = getRef(i).getKey();
                        requestsDeclinedViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent ClickRequest = new Intent(CanceledRequests.this.getActivity(), ViewRequestDetails.class);
                                ClickRequest.putExtra("Type","DeclinedRequests");
                                ClickRequest.putExtra("RequestKey",RequestKey);
                                startActivity(ClickRequest);
                            }
                        });
                    }

                };

        declinedlist.setAdapter(firebaseRecyclerAdapter);
    }








    public static class declinedViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public declinedViewHolder(View itemview){
            super(itemview);
            mView=itemview;

        }
        public void setType(String type){
            TextView t=(TextView)mView.findViewById(R.id.display_request_type);
            if(type.equals("Medical Report")){
                t.setText("تقرير");
            }
            if(type.equals("Radiology Report")){
                t.setText("أشعة");
            }
            if(type.equals("Prescription")){
                t.setText("وصفة طبية");
            }
            if(type.equals("Vital Signs")){
                t.setText("مؤشرات حيوية");
            }
            if(type.equals("Lab Report")){
                t.setText("تحليل مختبر");
            }

        }
        public void setDoctor_id(String doctorId){
            TextView doc=(TextView)mView.findViewById(R.id.display_request_id);
            doc.setText(doctorId);
        }
        public void setDate(String date){
            TextView myDate=(TextView)mView.findViewById(R.id.display_request_Date);
            myDate.setText(date);
        }
/////////////////////////////////////Change the date base on type

        public void setDeclined_date(String declined_date) {
            TextView declined=(TextView)mView.findViewById(R.id.display_reqordec);
            declined.setText("Declined at:");
            TextView rdate=(TextView)mView.findViewById(R.id.display_request_daterequest);
            rdate.setText(declined_date);
        }
        public void setDoctorName(String Dname) {
            TextView myID=(TextView)mView.findViewById(R.id.display_request_name);
            myID.setText(Dname);
        }

    }
}
