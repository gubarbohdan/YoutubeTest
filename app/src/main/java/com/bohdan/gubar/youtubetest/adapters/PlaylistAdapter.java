package com.bohdan.gubar.youtubetest.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bohdan.gubar.youtubetest.R;
import com.bohdan.gubar.youtubetest.model.realm.ListItem;
import com.squareup.picasso.Picasso;

import org.threeten.bp.Duration;

import java.util.List;

/**
 * Created by gubar on 10.03.2017.
 */

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.MyViewHolder> {

    private Context mContext;
    private List<ListItem> mList;

    private ListItem currentItem;
    private OnItemClickListener mListener;

    public PlaylistAdapter(Context context, List<ListItem> videoInfos, OnItemClickListener listener) {
        mContext = context;
        mList = videoInfos;
        mListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        currentItem = getItem(position);

        holder.videoTitleTextView.setText(currentItem.getTitle());
        holder.videoDescriptionTextView.setText(currentItem.getDescription());
        if(currentItem.getDuration() != null){
            Duration duration = Duration.parse(currentItem.getDuration());
            String minutes = Long.toString(duration.toMinutes());
            Long seconds = duration.minusMinutes(duration.toMinutes()).getSeconds();
            String time;
            if (seconds < 10){
                time = minutes+":0"+seconds;
            }
            else{
                time = minutes+":"+seconds;
            }

            holder.videoDurationTextView.setText(time);
        }
                Picasso.with(mContext).load(currentItem.getPhotoUrl()).into(holder.videoImageView);
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(getItem(position).getVideoId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private ListItem getItem(int position){
        return mList.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(String videoId);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public CardView cardView;
        public ImageView videoImageView;
        public TextView videoTitleTextView;
        public TextView videoDescriptionTextView;
        public TextView videoDurationTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            videoImageView = (ImageView) itemView.findViewById(R.id.imageView_video);
            videoTitleTextView = (TextView) itemView.findViewById(R.id.textView_video_title);
            videoDescriptionTextView = (TextView) itemView.findViewById(R.id.textView_video_description);
            videoDurationTextView = (TextView) itemView.findViewById(R.id.textView_video_duration);
        }

        public void setOnClickListener(View.OnClickListener listener) {
            cardView.setOnClickListener(listener);
        }
    }
}
