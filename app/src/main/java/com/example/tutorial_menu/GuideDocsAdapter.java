package com.example.tutorial_menu;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import java.util.List;

public class GuideDocsAdapter extends RecyclerView.Adapter<GuideDocsAdapter.GuideDocsViewHolder> {
    private List<GuideDocsCard> cardList;

    public GuideDocsAdapter(List<GuideDocsCard> cardList){
        this.cardList = cardList;
    }

    @NonNull
    @Override
    public GuideDocsAdapter.GuideDocsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guide_docs_card, parent, false);
        return new GuideDocsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuideDocsAdapter.GuideDocsViewHolder holder, int position) {
        GuideDocsCard guideDocsCard = cardList.get(position);
        holder.mTitle.setText(guideDocsCard.getTitle());
        holder.mDescription.setText(guideDocsCard.getDescription());
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public class GuideDocsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mTitle;
        TextView mDescription;
        ImageButton mDropDownButton;
        LinearLayout mDescriptionLayout;
        LinearLayout mTitleLayout;
        CardView mCardView;

        int mDescriptionLayoutHeight;

        public GuideDocsViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.guide_docs_title);
            mDescription = itemView.findViewById(R.id.guide_docs_description);
            mDropDownButton = itemView.findViewById(R.id.guide_docs_button);
            mDescriptionLayout = itemView.findViewById(R.id.guide_docs_description_layout);

            //Get description layout's height then hide it
            mDescriptionLayoutHeight = mDescriptionLayout.getMeasuredHeight();
            mDescriptionLayout.setVisibility(View.GONE);

            mTitleLayout = itemView.findViewById(R.id.guide_docs_title_layout);
            mTitleLayout.setOnClickListener(this);
            mCardView = itemView.findViewById(R.id.guide_docs_card_view);
        }

        @Override
        public void onClick(View view) {
            //Handler to display text description of the tool and flip the drop arrow
            boolean descriptionIsVisible;

            if (mDescriptionLayout.getVisibility() == View.GONE){
                descriptionIsVisible = false;
            }
            else { descriptionIsVisible = true;}

            if (descriptionIsVisible) {
                collapseView();
            } else {
                expandView();
            }

            mDropDownButton.setImageResource(descriptionIsVisible?R.drawable.arrow_drop_down:R.drawable.arrow_drop_up);
        }

        private void expandView(){
            mDescriptionLayout.setVisibility(View.VISIBLE);
            mDescriptionLayout.getLayoutParams().height = 0;
            ValueAnimator animator = slideAnimator(0, mDescriptionLayoutHeight);
            animator.start();
        }

        private void collapseView(){
            ValueAnimator animator = slideAnimator(mDescriptionLayoutHeight, 0);
            animator.start();
            mDescriptionLayout.setVisibility(View.GONE);
        }

        private ValueAnimator slideAnimator(int start, int end){
            ValueAnimator animator = ValueAnimator.ofInt(start, end).setDuration(300);
//            animator.setInterpolator(new AccelerateDecelerateInterpolator());

            animator.addUpdateListener(valueAnimator ->{
                int value = (Integer) valueAnimator.getAnimatedValue();
                mDescriptionLayout.getLayoutParams().height = value;
                mDescriptionLayout.requestLayout();
            });
            return animator;
        }
    }
}
