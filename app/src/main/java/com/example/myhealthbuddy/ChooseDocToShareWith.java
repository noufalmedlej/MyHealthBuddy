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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ChooseDocToShareWith extends AppCompatActivity {

    private BottomNavigationView bottomnav;
    private ImageButton searchbtn;
    private EditText searchInpuText;
    private RecyclerView SearchResultList;
    private DatabaseReference allUsersdatabaseRef;
    private FirebaseAuth mAuth;
    String currentUserid;
    //public static final String HCPkey="com.example.myhealthbuddy.HCPkey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_doc_to_share_with);


    allUsersdatabaseRef = FirebaseDatabase.getInstance().getReference().child("Doctors");
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

    RecyclerView myRecycler = (RecyclerView) findViewById(R.id.HCPresult);
        myRecycler.setLayoutManager(new LinearLayoutManager(this));
        myRecycler.setAdapter(new SampleRecycler());

        SearchResultList.setLayoutManager(new LinearLayoutManager(this));
        searchbtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String SearchBoxInput = searchInpuText.getText().toString();

                SearchMethod(SearchBoxInput);



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
                SearchMethod(SearchBoxInput);
                return true;

            }
            return false;
        }
    };





    public void SearchMethod(String SearchBoxInput) {
        if (SearchBoxInput.length() == 7) {
            Toast.makeText(this, "جاري البحث..", Toast.LENGTH_LONG).show();
            Query searchHCPInfiQuere = allUsersdatabaseRef.orderByChild("id").startAt(SearchBoxInput).endAt(SearchBoxInput + "\uf8ff");
            FirebaseRecyclerAdapter<HCPsResult, SearchViweHolder> FirebaseRecycleAdapter
                    = new FirebaseRecyclerAdapter<HCPsResult, SearchViweHolder>
                    (
                            HCPsResult.class,
                            R.layout.view_hcp,
                            SearchViweHolder.class,
                            searchHCPInfiQuere
                    ) {
                @Override
                protected void populateViewHolder(SearchViweHolder searchViweHolder, HCPsResult module, final int i) {
                    searchViweHolder.setName(module.getName());
                    searchViweHolder.setID(module.getID());
                    searchViweHolder.setSpecialty(module.getSpecialty());
                    searchViweHolder.setGender(module.getGender());
                    //searchViweHolder.setImage(getApplicationContext(),module.getImage());
                    final String HCPID=module.getID();

                    searchViweHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String HCPuId= getRef(i).getKey();
                            Intent intent = new Intent(ChooseDocToShareWith.this, ShareRecord.class);
                            intent.putExtra("HCPuID", HCPuId);
                            intent.putExtra("HCPID",HCPID);
                            startActivity(intent);
                        }
                    });
                }
            };
            SearchResultList.setAdapter(FirebaseRecycleAdapter);
        } else
            Toast.makeText(this, "الرجاء ادخال رقم صحيح ", Toast.LENGTH_LONG).show();
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
