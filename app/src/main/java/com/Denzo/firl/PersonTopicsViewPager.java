package com.Denzo.firl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.Denzo.firl.Model.MatchPerson;
import com.Denzo.firl.databinding.ItemPagerCardBinding;
import com.Denzo.firl.listeners.MatchPersonClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.util.List;

public class PersonTopicsViewPager extends RecyclerView.Adapter<PersonTopicsViewPager.ViewHolder> {
    private LayoutInflater mInflater;
    private List<MatchPerson> mCoursesList;
    private Context mContext;
    private MatchPersonClickListener matchPersonClickListener;

    public PersonTopicsViewPager(List<MatchPerson> mCoursesList, Context context, MatchPersonClickListener listener) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.mCoursesList = mCoursesList;
        this.matchPersonClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = mInflater.inflate(R.layout.item_pager_card, parent, false);
//        return new ViewHolder(view);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        View v = inflater.inflate(R.layout.item_shop_card, parent, false);
//        return new ViewHolder(v);

        ItemPagerCardBinding itemPagerCardBinding = ItemPagerCardBinding.inflate(inflater, parent, false);
        return new ViewHolder(itemPagerCardBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setBind(mCoursesList.get(position));

        holder.binding.cardViewPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchPersonClickListener.onScrollPagerItemClick(mCoursesList.get(position), holder.binding.image);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCoursesList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {


        ItemPagerCardBinding binding;

        ViewHolder(@NonNull ItemPagerCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setBind(MatchPerson matchCourse) {

            binding.tvTitulo.setText(matchCourse.getName());
            binding.tvCantidadCursos.setText(matchCourse.getNumberOfPersons());

            Glide.with(itemView.getContext())
                    .load(matchCourse.getImageResource())
//                .transform(new CenterCrop(), new RoundedCorners(24))
//                .transform(new RoundedCorners(40))
                    .transform(new CenterCrop())
                    .into(binding.image);
        }


    }
}