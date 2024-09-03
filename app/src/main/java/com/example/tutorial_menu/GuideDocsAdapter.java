package com.example.tutorial_menu;

import android.animation.ValueAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GuideDocsAdapter extends RecyclerView.Adapter<GuideDocsAdapter.GuideDocsViewHolder> {
    private List<GuideDocsCard> cardList;

    public GuideDocsAdapter(List<GuideDocsCard> cardList){
        this.cardList = cardList;
    }


    @Override
    public int getItemViewType(int position){
        return cardList.get(position).getGuideDocsCardType();
    }

    private View inflateView(ViewGroup parent, int layout){
        return LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
    }

    @NonNull
    @Override
    public GuideDocsAdapter.GuideDocsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case 1:
                view = inflateView(parent, R.layout.guide_docs_card_reminders);
                break;
            case 2:
                view = inflateView(parent, R.layout.guide_docs_card_contacts);
                break;
            case 3:
                view = inflateView(parent, R.layout.guide_docs_card_fences);
                break;
            case 4:
                view = inflateView(parent, R.layout.guide_docs_card_chatbuddy);
                break;
            default:
                view = inflateView(parent, R.layout.guide_docs_card_reminders);
                break;
        }
        return new GuideDocsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuideDocsAdapter.GuideDocsViewHolder holder, int position) {
        //Blank as card data is already present in individual layout files.
    }

    @Override
    public int getItemCount() {return cardList.size();}

    public class GuideDocsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageButton mDropDownButton;
        LinearLayout mDescriptionLayout;
        LinearLayout mTitleLayout;
        int mDescriptionLayoutHeight;

        public GuideDocsViewHolder(@NonNull View itemView) {
            super(itemView);
            mDropDownButton = itemView.findViewById(R.id.guide_docs_button);
            mDescriptionLayout = itemView.findViewById(R.id.guide_docs_description_layout);
            mTitleLayout = itemView.findViewById(R.id.guide_docs_title_layout);
            //Sets onclick animation when clicking on the card title.
            mTitleLayout.setOnClickListener(this);
            mDescriptionLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                //Obtains description layout's height
                @Override
                public void onGlobalLayout() {
                    mDescriptionLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    mDescriptionLayoutHeight = mDescriptionLayout.getHeight();
                    mDescriptionLayout.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onClick(View view) {
            //Handler to display text description of the tool and flip the drop arrow
            boolean descriptionIsVisible;

            descriptionIsVisible = mDescriptionLayout.getVisibility() == View.VISIBLE;

            if (descriptionIsVisible) {
                collapseView();
            } else {
                expandView();
            }

            mDropDownButton.setImageResource(descriptionIsVisible?R.drawable.arrow_drop_down:R.drawable.arrow_drop_up);
        }

        private void expandView(){
            mDescriptionLayout.setVisibility(View.VISIBLE);
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
