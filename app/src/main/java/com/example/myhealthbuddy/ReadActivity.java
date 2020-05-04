package com.example.myhealthbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import android.widget.TextView;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import org.apache.commons.io.IOUtils;
import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class ReadActivity extends AppCompatActivity {
    PDFView pdf;
    String url,recordIDٍ;
    Button export;
    DatabaseReference mData;
    StorageReference storageReference;
    StorageReference ref;
    BottomNavigationView bottomnav;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        bottomnav =  findViewById(R.id.bottom_navigation);
        bottomnav.setSelectedItemId(R.id.nav_home);
        bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                UserMenuSelector(menuItem);
                return false;
            }
        });


        pdf=findViewById(R.id.pdfView);
        url=getIntent().getExtras().get("url").toString();
        recordIDٍ=getIntent().getExtras().get("recordID").toString();


        tv=findViewById(R.id.textView);
        tv.setText("Record "+recordIDٍ+" PDF");
        export=findViewById(R.id.export);
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                export();
            }
        });

        new RetrievePDFbyte()
                .execute(url);
    }

    private void export() {
        storageReference= FirebaseStorage.getInstance().getReference();
        ref = storageReference.child("RecordsFiles").child(recordIDٍ+".pdf");
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String urlfromfb=uri.toString();

                download(ReadActivity.this,recordIDٍ,".pdf",DIRECTORY_DOWNLOADS,urlfromfb);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void download(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {


        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,destinationDirectory,fileName+fileExtension);

        downloadManager.enqueue(request);

    }




    class RetrievePDFbyte extends AsyncTask<String,Void,byte[]>{
        ProgressDialog progressDialog;
        protected void onPreExecute()
        {
            progressDialog = new ProgressDialog(ReadActivity.this);
            progressDialog.setTitle("Fetching PDF...");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

        }

        @Override
        protected byte [] doInBackground(String... strings) {
            InputStream inputStream = null;
            try{
                URL url=new URL(strings[0]);
                HttpsURLConnection httpsURLConnection=(HttpsURLConnection)url.openConnection();
                if (httpsURLConnection.getResponseCode()==200){
                    inputStream=new BufferedInputStream(httpsURLConnection.getInputStream());
                }
            }catch (IOException e){
                return null;
            }
            try {
                return IOUtils.toByteArray(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  null;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            pdf.fromBytes(bytes).load();
            progressDialog.dismiss();
        }

    }

    private void UserMenuSelector(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_share:
                Intent intent = new Intent(ReadActivity.this, ViewRecordtoShare.class);
                startActivity(intent);
                break;

            case R.id.nav_request:
                Intent intentrequest=new Intent(ReadActivity.this, ViewRequests.class);
                startActivity(intentrequest);
                break;

            case R.id.nav_person:
                Intent intentsearch=new Intent(ReadActivity.this, Profile.class);
                startActivity(intentsearch);
                break;

            case R.id.nav_not:
                Intent intentnot=new Intent(ReadActivity.this, ViewRecordsNotificatins.class);
                startActivity(intentnot);
                break;
            case R.id.nav_home:
                Intent intenthome=new Intent(ReadActivity.this, HomePage.class);
                startActivity(intenthome);
                break;

        }
    }


}