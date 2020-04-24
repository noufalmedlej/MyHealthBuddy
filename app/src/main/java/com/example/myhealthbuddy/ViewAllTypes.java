package com.example.myhealthbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewAllTypes extends AppCompatActivity {

    DatabaseReference RecordRef,HospitalRef;
    FirebaseAuth mAuth;
    String currentPatienid, HospitalName;
    RecyclerView BloodTestList;
    BottomNavigationView bottomnav;
    RecordAdapter2 mAdapter ;
    TextView NoBloodTests,PageTitel;
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_types);


        RecordRef = FirebaseDatabase.getInstance().getReference().child("Records");
        mAuth = FirebaseAuth.getInstance();
        currentPatienid = mAuth.getCurrentUser().getUid();

        bottomnav =  findViewById(R.id.bottom_navigation);
        bottomnav.setSelectedItemId(R.id.nav_home);
        bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                UserMenuSelector(menuItem);
                return false;
            }
        });



        PageTitel=findViewById(R.id.Titele);
        BloodTestList= findViewById(R.id.BloodTestList);
        BloodTestList.setHasFixedSize(true);
        RecyclerView myRecycler =  findViewById(R.id.BloodTestList);
        myRecycler.setLayoutManager(new LinearLayoutManager(this));
        BloodTestList.setLayoutManager(new LinearLayoutManager(this));
        NoBloodTests =findViewById(R.id.NoBloodTests);
        type=(int)getIntent().getExtras().get("type");
        switch(type){

            case 1: NoBloodTests.setText("لايوجد وصفات طبية ");
                PageTitel.setText("الوصفات الطبية");
                break;
            case 2: NoBloodTests.setText("لايوجد تحاليل طبية ");
                PageTitel.setText("التحاليل الطبية");
                break;
            case 3: NoBloodTests.setText("لايوجد أشعة ");
                PageTitel.setText("الأشعة");
                break;
            case 4: NoBloodTests.setText("لايوجد علامات حيوية ");
                PageTitel.setText("العلامات الحيوية");

                break;
            case 5: NoBloodTests.setText("لايوجد تقارير طبية ");
                PageTitel.setText("التقارير الطبية");
                break;
        }

        Browse();
    }

    private void Browse() {
        final ArrayList<item_record> MyBloodTest=new ArrayList<>();
        RecordRef.orderByChild("pid").equalTo(currentPatienid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot Myrecords:dataSnapshot.getChildren()){

                    item_record record=Myrecords.getValue(item_record.class);
                    if(record.getType()==type) {
                        record.rid = Myrecords.getKey();
                        MyBloodTest.add(record);
                    }
                }
                mAdapter= new RecordAdapter2(ViewAllTypes.this,MyBloodTest);
                BloodTestList.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(new RecordAdapter2.OnItemClickListener() {
                    @Override
                    public void onItemClick(String Rid) {

                       // Toast.makeText(ViewAllBloodTest.this,"heeere "+Rid,Toast.LENGTH_LONG).show();
                        Intent intentPills=new Intent(ViewAllTypes.this, ViewPrescription.class);
                        intentPills.putExtra("Rid",Rid);
                        startActivity(intentPills);

                       /* switch(type){
                            case 1:  Intent intentPills=new Intent(ViewAllBloodTest.this, ViewAllRecord.class);
                                intentPills.putExtra("Rid",Rid);
                                startActivity(intentPills);
                                break;
                            case 2: Intent intentMyBloodTests = new Intent(ViewAllBloodTest.this, ViewAllBloodTest.class);
                                intentMyBloodTests.putExtra("Rid",Rid);
                                startActivity(intentMyBloodTests);
                                break;
                            case 3:  Intent intentMyx_Rays = new Intent(ViewAllBloodTest.this, ViewAllBloodTest.class);
                                intentMyx_Rays.putExtra("Rid",Rid);
                                startActivity(intentMyx_Rays);
                                break;
                            case 4: Intent intentMyVital = new Intent(ViewAllBloodTest.this, ViewAllBloodTest.class);
                                intentMyVital.putExtra("Rid",Rid);
                                startActivity(intentMyVital);
                                break;
                            case 5:Intent intentRecord = new Intent(ViewAllBloodTest.this, ViewAllRecord.class);
                                intentRecord.putExtra("Rid",Rid);
                                startActivity(intentRecord);
                                break;
                        }*/

                    }
                });
                if(MyBloodTest.size()==0){
                    NoBloodTests.setVisibility(View.VISIBLE);

                }else NoBloodTests.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

  /*  public void Browse() {

        Query DisplayInfiQuere =RecordRef.orderByChild("pid").startAt(currentPatienid).endAt(currentPatienid+"\uf8ff");

        FirebaseRecyclerAdapter<item_record, ViewAllRecord.RecordViweHolder> FirebaseRecycleAdapter
                = new FirebaseRecyclerAdapter<item_record, ViewAllRecord.RecordViweHolder>
                (
                        item_record.class,
                        R.layout.record_item,
                        ViewAllRecord.RecordViweHolder.class,
                        DisplayInfiQuere
                ){
            @Override
            protected void populateViewHolder(final ViewAllRecord.RecordViweHolder recordViweHolder, final item_record module, final int i) {
                if (module.getType() == 2){
                    recordViweHolder.setDate(module.getDate());
                    recordViweHolder.setPatientName(module.getPatientName());
                    recordViweHolder.setDoctorName(module.getDoctorName());
                    recordViweHolder.setRid(getRef(i).getKey());
                    recordViweHolder.setType(module.getType());
                    recordViweHolder.setCk(true);
                    //recordViweHolder.setHospitalName(GetHospitalName("122"));

                    recordViweHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           // String recordId= getRef(i).getKey();
                            //Intent intent = new Intent(ViewAllBloodTest.this, viweRecodrs.class);
                            //intent.putExtra("recordId", recordId);
                            //startActivity(intent);
                        }
                    });

            }
                // end if
            }
        };
        BloodTestList.setAdapter(FirebaseRecycleAdapter);
    }*/


    public String GetHospitalName(String hospitalID){

        HospitalRef= FirebaseDatabase.getInstance().getReference().child("Hospitals").child(hospitalID);
        HospitalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HospitalName =dataSnapshot.child("Name").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return HospitalName;
    }
    private void UserMenuSelector(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_share:
                Intent intent = new Intent(ViewAllTypes.this, ViewRecordtoShare.class);
                startActivity(intent);
                break;

            case R.id.nav_request:
                Intent intentrequest=new Intent(ViewAllTypes.this, ViewRequests.class);
                startActivity(intentrequest);
                break;

            case R.id.nav_person:
                Intent intentsearch=new Intent(ViewAllTypes.this, Profile.class);
                startActivity(intentsearch);
                break;
        }
    }



}
