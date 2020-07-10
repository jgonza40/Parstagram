package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Details extends AppCompatActivity {
    private Post post;
    private TextView tvUsername;
    private TextView tvDescription;
    private ImageView ivImage;
    private TextView tvDate;
    private ImageView ivProfileImage;
    private TextView tvUsername2;
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    public static final String TAG = "Details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
        Log.d(TAG, String.format("Showing details for '%s'", post.getUser().getUsername()));

        tvUsername = findViewById(R.id.tvDetUsername);
        tvDescription = findViewById(R.id.tvDetDescription);
        ivImage = findViewById(R.id.ivDetImage);
        tvDate = findViewById(R.id.tvDetDate);
        ivProfileImage = findViewById(R.id.ivDetProfileImage);
        tvUsername2 = findViewById(R.id.tvDetUsername2);

        tvUsername.setText(post.getUser().getUsername());
        tvDescription.setText(post.getDescription());
        tvDate.setText(getRelativeTimeAgo(post.getCreatedAt().toString()));
        tvUsername2.setText(post.getUser().getUsername());
        ParseFile image = post.getImage();
        if(image != null){
            Glide.with(getApplicationContext())
                    .load(image.getUrl())
                    .into(ivImage);
        }
        Glide.with(getApplicationContext())
                .load(getResources().getDrawable(R.drawable.profile_feed))
                .placeholder(R.mipmap.profile_feed)
                .circleCrop()
                .into(ivProfileImage);
    }
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
                return diff / MINUTE_MILLIS + "m" + " ago";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + "h" + " ago";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + "d" + " ago";
            }
        } catch (ParseException e) {
            Log.i(TAG, "getRelativeTimeAgo failed");
            e.printStackTrace();
        }
        return "";
    }
}