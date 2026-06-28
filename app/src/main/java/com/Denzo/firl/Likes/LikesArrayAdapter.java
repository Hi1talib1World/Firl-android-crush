package com.Denzo.firl.Likes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Denzo.firl.R;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class LikesArrayAdapter extends ArrayAdapter<LikeCard> {

    public LikesArrayAdapter(Context context, int resourceId, List<LikeCard> items) {
        super(context, resourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final LikeCard card_item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        View infoBtn = convertView.findViewById(R.id.checkInfoBeforeMatched);
        TextView scoreText = convertView.findViewById(R.id.compatibility_score);
        ImageView verifiedBadge = convertView.findViewById(R.id.verified_badge);
        TextView jobSchool = (TextView) convertView.findViewById(R.id.card_job_school);
        ChipGroup lifestyleTags = (ChipGroup) convertView.findViewById(R.id.card_lifestyle_tags);

        name.setText(card_item.getName() + (card_item.getAge() > 0 ? ", " + card_item.getAge() : ""));

        if (card_item.getCompatibilityScore() > 0) {
            scoreText.setVisibility(View.VISIBLE);
            scoreText.setText(Math.round(card_item.getCompatibilityScore()) + "% Match");
        } else {
            scoreText.setVisibility(View.GONE);
        }

        verifiedBadge.setVisibility(card_item.isVerified() ? View.VISIBLE : View.GONE);

        // Populate Lifestyle Tags with premium styling
        lifestyleTags.removeAllViews();
        if (card_item.getInterests() != null) {
            for (String tag : card_item.getInterests()) {
                Chip chip = new Chip(getContext());
                chip.setText(tag);
                chip.setChipMinHeight(0f);
                // Use a semi-transparent dark background for better contrast against profile images
                chip.setChipBackgroundColor(android.content.res.ColorStateList.valueOf(0x60000000));
                chip.setTextColor(getContext().getColor(android.R.color.white));
                chip.setChipStrokeColor(android.content.res.ColorStateList.valueOf(0x80FFFFFF));
                chip.setChipStrokeWidth(2f);
                chip.setTextSize(12f);
                lifestyleTags.addView(chip);
            }
        }

        String job = card_item.getJob() != null ? card_item.getJob() : "Professional";
        String school = card_item.getSchool() != null ? card_item.getSchool() : "University";
        jobSchool.setText(String.format("%s at %s", job, school));

        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(card_item);
            }
        });

        switch (card_item.getProfileImageUrl()) {
            case "default":
                Glide.with(convertView.getContext()).load(R.mipmap.ic_launcher).into(image);
                break;
            default:
                Glide.with(convertView.getContext()).clear(image);
                Glide.with(convertView.getContext()).load(card_item.getProfileImageUrl()).into(image);
                break;
        }

        return convertView;
    }

    private void showBottomSheet(final LikeCard card) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.layout_profile_details, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        TextView name = bottomSheetView.findViewById(R.id.detail_name);
        TextView reportBtn = bottomSheetView.findViewById(R.id.report_user);

        name.setText(card.getName() + ", 22"); // Sample age
        reportBtn.setText("Report and Block " + card.getName());

        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), card.getName() + " has been reported. We will review this profile.", Toast.LENGTH_LONG).show();
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }
}
