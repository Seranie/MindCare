package com.example.tutorial_menu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GuideShowcaseAdapter extends RecyclerView.Adapter<GuideShowcaseAdapter.GuideShowcaseViewHolder> {
    //Adapter for guide showcase's recycler view of cards.
    private final List<GuideShowcaseCard> cardList;

    public GuideShowcaseAdapter(List<GuideShowcaseCard> cardList){
        this.cardList = cardList;
    }

    @NonNull
    @Override
    public GuideShowcaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guide_showcase_card, parent, false);
        return new GuideShowcaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuideShowcaseViewHolder holder, int position) {
        //gets the card at x position and updates the view with the card details
        GuideShowcaseCard card = cardList.get(position);
        holder.mImageView.setImageResource(card.getCardImage());
        holder.mCardTitle.setText(card.getCardTitle());
        holder.mCardSubtitle.setText(card.getCardSubtitle());
        holder.mCardDescription.setText(card.getCardDescription());
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public class GuideShowcaseViewHolder extends RecyclerView.ViewHolder {
        //Holder retrieves and holds references to the card layout's UI elements.
        ImageView mImageView;
        TextView mCardTitle;
        TextView mCardSubtitle;
        TextView mCardDescription;
        //TODO link up with navigation
        Button mShowMe;
        Button mLearnMore;

        public GuideShowcaseViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.showcase_card_image);
            mCardTitle = itemView.findViewById(R.id.showcase_card_title);
            mCardSubtitle = itemView.findViewById(R.id.showcase_card_subtitle);
            mCardDescription = itemView.findViewById(R.id.showcase_card_description);
            mShowMe = itemView.findViewById(R.id.showcase_card_show_me_button);
            mLearnMore = itemView.findViewById(R.id.showcase_card_learn_more);
        }
    }
}
