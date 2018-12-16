package com.agobal.KnyguKeitykla.BookDetails;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.agobal.KnyguKeitykla.Entities.MyBook;
import com.agobal.KnyguKeitykla.Fragments.LibraryFragment;
import com.agobal.KnyguKeitykla.R;
import com.agobal.KnyguKeitykla.activity.AddNewBook;
import com.agobal.KnyguKeitykla.activity.MainActivity;
import com.agobal.KnyguKeitykla.activity.SearchBookAPI;
import com.agobal.KnyguKeitykla.helper.BookClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;

public class MyBookDetail extends AppCompatActivity {

    DatabaseReference mUserDatabase;
    DatabaseReference mUserBookDelete;

    private ImageView ivBookCover;
    private TextView tvTitle;
    private TextView tvAuthor;
    private TextView tvPublisher;
    private TextView tvBookYear;
    private TextView tvBookCondition;
    private TextView tvBookCategory;
    String BookDeleteKey;

    TextView title;

    FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    String current_uid = Objects.requireNonNull(mCurrentUser).getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_book_detail);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));

        // Fetch views
        ivBookCover = findViewById(R.id.ivBookCover);
        tvTitle = findViewById(R.id.tvTitle);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvPublisher = findViewById(R.id.tvPublisher);
        tvBookYear= findViewById(R.id.tvBookYear);
        tvBookCondition= findViewById(R.id.tvBookCondition);
        tvBookCategory= findViewById(R.id.tvBookCategory);

        FloatingActionButton fabEdit = findViewById(R.id.fb_edit);
        FloatingActionButton fabDelete = findViewById(R.id.fb_delete);

        fabEdit.setOnClickListener(view -> {
            //Intent intent = new Intent(MyBookDetail.this, test.class);
            //startActivity(intent);
        });

        fabDelete.setOnClickListener(view -> {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Dėmėsio!")
                    .setContentText("Ar tikrai norite ištrinti šią knygą?")
                    .setConfirmText("Taip, noriu ištrinti")
                    .setCancelText("Ne")
                    .setConfirmClickListener(sDialog -> {
                        deleteMyBook();
                        sDialog.dismissWithAnimation();
                    })
                    .show();
        });

        // Use the book to populate the data into our views
        MyBook myBook = (MyBook) getIntent().getSerializableExtra(LibraryFragment.MY_BOOK_DETAIL_KEY);
        BookDeleteKey = getIntent().getStringExtra("BOOK_KEY");
        loadBook(myBook);

    }

    private void loadBook(MyBook myBook) {
        //change activity title
        title.setText(myBook.getBookName());
        // Populate data
        Picasso.get().load(Uri.parse(myBook.getBookImage())).error(R.drawable.ic_nocover).rotate(90).resize(400,600).centerCrop().into(ivBookCover);
        tvTitle.setText(myBook.getBookName());
        tvAuthor.setText(myBook.getBookAuthor());
        tvPublisher.setText("Leidykla: "+ myBook.getBookPublisher());
        tvBookYear.setText("Išleidimo metai: " + myBook.getBookYear());
        tvBookCondition.setText("Būklė: " + myBook.getBookCondition());
        tvBookCategory.setText("Kategorija: " + myBook.getBookCategory());

    }

    private void deleteMyBook() {

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mUserBookDelete = FirebaseDatabase.getInstance().getReference().child("UserBooks").child(current_uid).child(BookDeleteKey);
        Log.d("TAG"," "+ BookDeleteKey);
        mUserBookDelete.setValue(null);
        new SweetAlertDialog(this)
                .setTitleText("Knyga panaikinta!")
                .setConfirmClickListener(sweetAlertDialog -> {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                })
                .show();
    }

    public void onBackPressed() {
        int backstack = getSupportFragmentManager().getBackStackEntryCount();

        if (backstack > 0) {
            getSupportFragmentManager().popBackStack();
        }
        else{
            super.onBackPressed();
            //System.exit(0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
