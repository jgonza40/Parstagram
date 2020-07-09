package com.example.parstagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.parstagram.Post;
import com.example.parstagram.R;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class PostDetailsFragment extends Fragment {
    private Post post;
    private TextView tvUsername;
    private TextView tvDescription;
    private ImageView ivImage;
    private TextView tvDate;
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    public static final String TAG = "PostDetailsFragment";

    public PostDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_details, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // unwrap the movie passed in via intent, using its simple name as a key
//        post = (Post) Parcels.unwrap(getActivity().getIntent().getParcelableExtra(Post.class.getSimpleName()));
//        Log.d(TAG, String.format("Showing details for '%s'", post.getUser().getUsername()));
        tvUsername = view.findViewById(R.id.tvDetailsUsername);
        tvDescription = view.findViewById(R.id.tvDetailsDescription);
        ivImage = view.findViewById(R.id.ivDetailsImage);
        tvDate = view.findViewById(R.id.tvDetailsDate);

        Bundle bundle = getActivity().getIntent().getExtras();
        Bundle bundle2 = new Bundle();
        //Post post = bundle.getParcelable("post");
        Post post = Parcels.unwrap(bundle2.getParcelable("post"));

        tvUsername.setText("@" + post.getUser().getUsername());
        tvDescription.setText(post.getDescription());
        //tvDate.setText(getRelativeTimeAgo(post.getCreatedAt().toString()));
        tvDate.setText(getRelativeTimeAgo(post.getCreatedAt().toString()));
        ParseFile image = post.getImage();
        if(image != null) {
            Glide.with(getContext())
                    .load(image.getUrl())
                    .into(ivImage);
        }
    }


    /*public void setIgPost(Post post){
        tvUsername.setText("@" + post.getUser().getUsername());
        tvDescription.setText(post.getDescription());
        tvDate.setText(getRelativeTimeAgo(post.getCreatedAt().toString()));
        ParseFile image = post.getImage();
        if(image != null){
            Glide.with(getContext())
                    .load(image.getUrl())
                    .into(ivImage);
        }
    }*/

    // The purpose of this method is to get appropriate time stamps
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);
        try {
            long time = sf.parse(rawJsonDate).getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + "m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + "h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + "d";
            }
        } catch (ParseException e) {
            Log.i(TAG, "getRelativeTimeAgo failed");
            e.printStackTrace();
        }
        return "";
    }
}