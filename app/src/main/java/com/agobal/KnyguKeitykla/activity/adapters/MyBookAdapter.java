package com.agobal.KnyguKeitykla.activity.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.agobal.KnyguKeitykla.Entities.MyBook;
import com.agobal.KnyguKeitykla.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyBookAdapter extends ArrayAdapter<MyBook> {

    private Context mContext;
    private List<MyBook> myBookList = new ArrayList<>();




    public MyBookAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<MyBook> list) {

        super(context, 0 , list);
        mContext = context;
        myBookList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_book,parent,false);

        MyBook currentBook = myBookList.get(position);

        ImageView image = listItem.findViewById(R.id.ivBookCover);



        //Glide.with(getContext()).load(urlImage).into(image);

        Picasso.get().load(currentBook.getBookImage())
                .rotate(90)
                .resize(200,200)
                .centerCrop()
                .into(image);


        //Picasso.get().load(image).into(image);


        TextView name = listItem.findViewById(R.id.tvTitle);
        name.setText(currentBook.getBookName());

        TextView release = listItem.findViewById(R.id.tvAuthor);
        release.setText(currentBook.getBookAuthor());


    return listItem;
    }



}
