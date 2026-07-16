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

        TextView name = convertView.findViewById(R.id.name);
        ImageView image = convertView.findViewById(R.id.image);
        View infoBtn = convertView.findViewById(R.id.checkInfoBeforeMatched);
        TextView scoreText = convertView.findViewById(R.id.compatibility_score);
        ImageView verifiedBadge = convertView.findViewById(R.id.verified_badge);
        TextView jobSchool = convertView.findViewById(R.id.card_job_school);
        ChipGroup lifestyleTags = convertView.findViewById(R.id.card_lifestyle_tags);

        if (card_item != null) {
            name.setText(card_item.getName() + (card_item.getAge() > 0 ? " " + card_item.getAge() : ""));

            if (card_item.getCompatibilityScore() > 0) {
                scoreText.setVisibility(View.VISIBLE);
                double score = card_item.getCompatibilityScore();
                if (score >= 90) {
                    scoreText.setText("Amazing Match " + Math.round(score) + "%");
                    scoreText.setBackgroundTintList(android.content.res.ColorStateList.valueOf(getContext().getColor(R.color.likegreen)));
                } else if (score >= 75) {
                    scoreText.setText("Great Match " + Math.round(score) + "%");
                    scoreText.setBackgroundTintList(android.content.res.ColorStateList.valueOf(getContext().getColor(R.color.superlikeblue)));
                } else if (score >= 50) {
                    scoreText.setText("Good Match " + Math.round(score) + "%");
                    scoreText.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0x80000000));
                } else {
                    scoreText.setText(Math.round(score) + "% Match");
                    scoreText.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0x80000000));
                }
            } else {
                scoreText.setVisibility(View.GONE);
            }

            View statusIndicator = convertView.findViewById(R.id.status_chip);
            if (statusIndicator != null) {
                TextView statusText = statusIndicator.findViewById(R.id.status_text);
                View statusDot = statusIndicator.findViewById(R.id.status_dot);

                if (card_item.getActivityLevel() >= 8) {
                    if (statusText != null) statusText.setText("Popular");
                    if (statusDot != null) statusDot.setBackgroundTintList(android.content.res.ColorStateList.valueOf(getContext().getColor(R.color.brand_primary)));
                    statusIndicator.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0x66000000));
                } else if (card_item.getActivityLevel() >= 5) {
                    if (statusText != null) statusText.setText("Active");
                    if (statusDot != null) statusDot.setBackgroundTintList(android.content.res.ColorStateList.valueOf(getContext().getColor(R.color.likegreen)));
                    statusIndicator.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0x4D000000));
                } else {
                    if (statusText != null) statusText.setText("Recently Active");
                    if (statusDot != null) statusDot.setBackgroundTintList(android.content.res.ColorStateList.valueOf(getContext().getColor(R.color.grey_icon)));
                    statusIndicator.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0x33000000));
                }
            }

            verifiedBadge.setVisibility(card_item.isVerified() ? View.VISIBLE : View.GONE);

            lifestyleTags.removeAllViews();
            if (card_item.getInterests() != null) {
                for (String tag : card_item.getInterests()) {
                    Chip chip = new Chip(getContext());
                    chip.setText(tag);
                    chip.setChipMinHeight(0f);
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

            if (card_item.getProfileImageUrl() != null) {
                if (card_item.getProfileImageUrl().equals("default")) {
                    Glide.with(convertView.getContext()).load(R.mipmap.ic_launcher).into(image);
                } else {
                    Glide.with(convertView.getContext()).clear(image);
                    Glide.with(convertView.getContext()).load(card_item.getProfileImageUrl()).into(image);
                }
            }
        }

        return convertView;
    }

    private void showBottomSheet(final LikeCard card) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.layout_profile_details, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        TextView name = bottomSheetView.findViewById(R.id.detail_name);
        TextView reportBtn = bottomSheetView.findViewById(R.id.report_user);

        name.setText(card.getName() + ", " + card.getAge());
        reportBtn.setText("Report and Block " + card.getName());

        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.Denzo.firl.Model.UserRepository repository = com.Denzo.firl.Model.UserRepositoryProvider.get();
                String currentUid = com.Denzo.firl.Utils.SessionManager.getInstance().getCurrentUserId();
                
                repository.reportUser(currentUid, card.getUserId(), "Reported by user", new com.Denzo.firl.Model.UserRepository.Callback<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        Toast.makeText(getContext(), card.getName() + " has been reported. We will review this profile.", Toast.LENGTH_LONG).show();
                        bottomSheetDialog.dismiss();
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(getContext(), "Failed to report user.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        bottomSheetDialog.show();
    }
}
