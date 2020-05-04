package com.example.myhealthbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchHCP extends AppCompatActivity {


    private ImageButton searchbtn;
    private EditText searchInpuText;
    private RecyclerView SearchResultList;
    private DatabaseReference allSharesRef,DoctorRef,RecordsRef;
    private FirebaseAuth mAuth;
    String currentUserid;
    HCPAdapter mAdapter ;
    TextView NoresultHCP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_hcp);


    //allSharesRef = FirebaseDatabase.getInstance().getReference().child("Share");
   // DoctorRef = FirebaseDatabase.getInstance().getReference().child("Doctors");
    //RecordsRef = FirebaseDatabase.getInstance().getReference().child("Records");
    mAuth = FirebaseAuth.getInstance();
    currentUserid = mAuth.getCurrentUser().getUid();

    // search btn
    searchbtn = (ImageButton) findViewById(R.id.searchHCPbutton);

    // SearchInput
    searchInpuText = (EditText) findViewById(R.id.SearchHCPInput);
        searchInpuText.setOnEditorActionListener(editorListener);

    // RecyclerView
    SearchResultList = (RecyclerView) findViewById(R.id.HCPresult);
    SearchResultList.setHasFixedSize(true);
    NoresultHCP =findViewById(R.id.NoResult2);

    RecyclerView myRecycler = (RecyclerView) findViewById(R.id.HCPresult);
        myRecycler.setLayoutManager(new LinearLayoutManager(this));
        myRecycler.setAdapter(new SampleRecycler());
        BrowseMyHCP();

        SearchResultList.setLayoutManager(new LinearLayoutManager(this));
        searchbtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String SearchBoxInput = searchInpuText.getText().toString();

            SearchForHCP(SearchBoxInput);



        }
    });
}

    private TextView.OnEditorActionListener editorListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

            if(actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || event.getAction() == KeyEvent.ACTION_DOWN
                    || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                String SearchBoxInput = searchInpuText.getText().toString();
                SearchForHCP(SearchBoxInput);
                return true;

            }
            return false;
        }
    };





    public void SearchForHCP(String SearchBoxInput) {
        if (SearchBoxInput.length() == 7 ) {
            Toast.makeText(this, "جاري البحث..", Toast.LENGTH_LONG).show();
            Query searchHCPInfiQuere = DoctorRef.orderByChild("id").startAt(SearchBoxInput).endAt(SearchBoxInput + "\uf8ff");
            FirebaseRecyclerAdapter<HCPsResult, SearchViweHolder> FirebaseRecycleAdapter
                    = new FirebaseRecyclerAdapter<HCPsResult, SearchViweHolder>
                    (
                            HCPsResult.class,
                            R.layout.view_hcp,
                            SearchViweHolder.class,
                            searchHCPInfiQuere
                    ) {
                @Override
                protected void populateViewHolder(SearchViweHolder searchViweHolder, final HCPsResult module, final int i) {
                    searchViweHolder.setName(module.getName());
                    searchViweHolder.setID(module.getId());
                    searchViweHolder.setSpecialty(module.getSpecialty());
                    searchViweHolder.setGender(module.getGender());
                    //searchViweHolder.setImage(getApplicationContext(),module.getImage());
                    final String HCPID=module.getId();

                    searchViweHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String HCPuId= getRef(i).getKey();
                            Intent intent = new Intent(SearchHCP.this, ShareRecord.class);
                            intent.putExtra("HCPuID", HCPuId);
                            intent.putExtra("HCPID",HCPID);
                            intent.putExtra("HCPName",module.getName());
                            ArrayList<item_record>  CkList=(ArrayList<item_record> ) getIntent().getExtras().get("list");
                            intent.putExtra("list",CkList);
                            startActivity(intent);
                        }
                    });
                    if (getItemCount()==0)
                        NoresultHCP.setVisibility(View.VISIBLE);
                    else NoresultHCP.setVisibility(View.INVISIBLE);
                }
            };
            SearchResultList.setAdapter(FirebaseRecycleAdapter);
            NoresultHCP.setVisibility(View.VISIBLE);
        } else
            Toast.makeText(this, "الرجاء ادخال رقم صحيح ", Toast.LENGTH_LONG).show();
    }



    public void BrowseMyHCP() {

        final ArrayList<HCPsResult> HCPArrayList= new ArrayList<>();
        final ArrayList<String> Ids= new ArrayList<>();

        DoctorRef=FirebaseDatabase.getInstance().getReference().child("Doctors");

        DoctorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot2) {
                if (dataSnapshot2.exists()) {
                    RecordFromRecords(dataSnapshot2,Ids,HCPArrayList);
                    /*allSharesRef = FirebaseDatabase.getInstance().getReference().child("Share");
                    Query paitentShare=allSharesRef.orderByChild("patient_uid").equalTo(currentUserid + "\uf8ff");
                    paitentShare.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                            String id;
                            HCPsResult HCP;
                            for (DataSnapshot docs : dataSnapshot2.getChildren()) {
                                for (DataSnapshot share : dataSnapshot1.getChildren()) {
                                        String did=docs.getKey();
                                        if (share.child("share_id").equals(currentUserid+did)){
                                            HCP = docs.getValue(HCPsResult.class);
                                            id = docs.getKey();
                                            //if (!Ids.contains(id)) {
                                                Ids.add(id);
                                                HCP.uid=docs.getKey();
                                                HCPArrayList.add(HCP);
                                            //}
                                        }
                                    }

                                }
                            mAdapter = new HCPAdapter(SearchHCP.this, HCPArrayList);
                            mAdapter.setOnItemClickListener(new HCPAdapter.OnItemClickListener(){

                                @Override
                                public void onItemClick(String HCPID,String Name,String HCPuId) {
                                    Intent intent = new Intent(SearchHCP.this, ShareRecord.class);
                                    intent.putExtra("HCPuID", HCPuId);
                                    intent.putExtra("HCPID",HCPID);
                                    intent.putExtra("HCPName",Name);
                                    ArrayList<item_record>  CkList=(ArrayList<item_record> ) getIntent().getExtras().get("list");
                                    intent.putExtra("list",CkList);
                                    startActivity(intent);
                                }
                            });
                           SearchResultList.setAdapter(mAdapter);
                            if (HCPArrayList.size() == 0) {
                                NoresultHCP.setVisibility(View.VISIBLE);

                            } else NoresultHCP.setVisibility(View.INVISIBLE);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    private void RecordFromRecords(final DataSnapshot dataSnapshot2,final  ArrayList<String> Ids, final ArrayList<HCPsResult> HCPArrayList) {
        RecordsRef = FirebaseDatabase.getInstance().getReference().child("Records");
        Query DocRecord= RecordsRef.orderByChild("pid").startAt(currentUserid).endAt(currentUserid + "\uf8ff");
        DocRecord.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                if (dataSnapshot1.exists()) {
                    String id;
                    for (DataSnapshot records : dataSnapshot1.getChildren()) {

                        for (DataSnapshot doc : dataSnapshot2.getChildren()) {
                            HCPsResult HCP;
                            if (doc.getKey().equals(records.child("did").getValue().toString())) {
                                HCP = doc.getValue(HCPsResult.class);
                                id = doc.getKey();
                                if (!Ids.contains(id)) {
                                    Ids.add(id);
                                    HCP.uid=doc.getKey();
                                    HCPArrayList.add(HCP);
                                }
                            }
                        }

                    }


                }

                mAdapter = new HCPAdapter(SearchHCP.this, HCPArrayList);
                mAdapter.setOnItemClickListener(new HCPAdapter.OnItemClickListener(){

                    @Override
                    public void onItemClick(String HCPID,String Name,String HCPuId) {
                        Intent intent = new Intent(SearchHCP.this, ShareRecord.class);
                        intent.putExtra("HCPuID", HCPuId);
                        intent.putExtra("HCPID",HCPID);
                        intent.putExtra("HCPName",Name);
                        ArrayList<item_record>  CkList=(ArrayList<item_record> ) getIntent().getExtras().get("list");
                        intent.putExtra("list",CkList);
                        startActivity(intent);
                    }
                });
                SearchResultList.setAdapter(mAdapter);
                if (HCPArrayList.size() == 0) {
                    NoresultHCP.setVisibility(View.VISIBLE);

                } else NoresultHCP.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    public static class SearchViweHolder extends RecyclerView.ViewHolder {
    View mViwe;


    //defolt constroctor
    public SearchViweHolder(@NonNull View itemView) {
        super(itemView);
        mViwe = itemView;
    }

    public void setName(String Name) {
        TextView MyName= (TextView)mViwe.findViewById(R.id.all_HCP_profile_name);
        MyName.setText(Name);
    }
    public void setID(String ID) {
        TextView myID=(TextView)mViwe.findViewById(R.id.all_HCP_profile_Id);
        // result was all user names (retreved from database)
        myID.setText(ID);
    }

    public void setSpecialty(String Specialty) {
        TextView mySpecialty=(TextView) mViwe.findViewById(R.id.all_HCP_profile_specialty);
        mySpecialty.setText(Specialty);
    }
    public void setGender(String gender) {
        TextView mygender=(TextView) mViwe.findViewById(R.id.all_HCP_profile_gender);
        mygender.setText(gender);
    }
    public void setImage(Context ctx, String img) {

  /*  CircleImageView MyImage= (CircleImageView) mViwe.findViewById(R.id.all_HCP_profileImg);
    Picasso.with(ctx).load(img).placeholder(R.drawable.doctoricon.Into(MyImage);
    // Into or into
    */

        ImageView MyImage = (ImageView) mViwe.findViewById(R.id.all_HCP_profileImg);
        //  Picasso.get().load(img).into(post_image);
        Picasso.get().load(img).into(MyImage);
        Glide.with(ctx).load(img).into(MyImage);

    }

}

public class SampleHolder extends RecyclerView.ViewHolder {
    public SampleHolder(View itemView) {
        super(itemView);
    }
}

public class SampleRecycler extends RecyclerView.Adapter<SampleHolder> {
    @Override
    public SampleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(SampleHolder holder, int position) {



    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

}
