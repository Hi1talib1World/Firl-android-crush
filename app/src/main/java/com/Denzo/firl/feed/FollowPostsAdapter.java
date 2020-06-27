package com.Denzo.firl.feed;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Denzo.firl.R;
import com.Denzo.firl.feed.model.FollowingPost;

import java.util.ArrayList;
import java.util.List;

public class FollowPostsAdapter extends RecyclerView.Adapter<FollowPostViewHolder> {
    public static final String TAG = FollowPostsAdapter.class.getSimpleName();

    private List<FollowingPost> itemsList = new ArrayList<>();

    private CallBack callBack;

    private BaseActivity activity;

    private int selectedPostPosition = RecyclerView.NO_POSITION;

    public FollowPostsAdapter(BaseActivity activity) {
        this.activity = activity;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    @NonNull
    @Override
    public FollowPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.post_item_list_view, parent, false);
        return new FollowPostViewHolder(view, createOnClickListener(), activity, true);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowPostViewHolder holder, int position) {
        holder.bindData(itemsList.get(position));
    }

    public void setList(List<FollowingPost> list) {
        itemsList.clear();
        itemsList.addAll(list);
        notifyDataSetChanged();
    }

    private PostViewHolder.OnClickListener createOnClickListener() {
        return new PostViewHolder.OnClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                if (callBack != null) {
                    callBack.onItemClick(getItemByPosition(position), view);
                    selectedPostPosition = position;
                }
            }

            @Override
            public void onLikeClick(LikeController likeController, int position) {
                FollowingPost followingPost = getItemByPosition(position);
                likeController.handleLikeClickAction(activity, followingPost.getPostId());
            }

            @Override
            public void onAuthorClick(int position, View view) {
                if (callBack != null) {
                    callBack.onAuthorClick(position, view);
                }
            }
        };
    }

    public FollowingPost getItemByPosition(int position) {
        return itemsList.get(position);
    }

    public void updateSelectedItem() {
        if (selectedPostPosition != RecyclerView.NO_POSITION) {
            notifyItemChanged(selectedPostPosition);
        }
    }

    public interface CallBack {
        void onItemClick(FollowingPost followingPost, View view);

        void onAuthorClick(int position, View view);
    }
}