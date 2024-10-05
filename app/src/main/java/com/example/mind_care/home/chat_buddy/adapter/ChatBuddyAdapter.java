package com.example.mind_care.home.chat_buddy.adapter;

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.FileObserver;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.AdapterListUpdateCallback;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mind_care.R;
import com.example.mind_care.database.chat_buddy.MessageEntity;
import com.example.mind_care.home.chat_buddy.viewmodel.ChatBuddyViewModel;

import org.apache.commons.lang3.StringUtils;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.renderer.text.TextContentRenderer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import io.noties.markwon.Markwon;

public class ChatBuddyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_AI = 2;
    private final String IMAGE_FILE_NAME = "user_avatar_image.png";
    private List<MessageEntity> messageList = Collections.unmodifiableList(new ArrayList<>());
    private final ChatBuddyViewModel chatBuddyViewModel;
    private HashMap<String, String> imageHashmap = new HashMap<>();
    private final FileChangeObserver observer;
    private final Activity mainActivity;
    private TextAnimator animator = new TextAnimator();
    private RecyclerView recyclerView;
    private MyListUpdateCallback myListUpdateCallback;
    final Markwon markwon;


    public ChatBuddyAdapter(ChatBuddyViewModel chatBuddyViewModel, LifecycleOwner owner, HashMap<String, String> imageHashmap, Activity mainActivity, RecyclerView recyclerView, Context context) {
        this.imageHashmap = imageHashmap;
        this.chatBuddyViewModel = chatBuddyViewModel;
        chatBuddyViewModel.getMessagesLiveData().observe(owner, messageEntities -> {
            updateReminders(messageEntities);
        });
        this.mainActivity = mainActivity;
        File file = new File(mainActivity.getFilesDir(), IMAGE_FILE_NAME);
        observer = new FileChangeObserver(file.getAbsolutePath());
        observer.startWatching();
        this.recyclerView = recyclerView;
        myListUpdateCallback = new MyListUpdateCallback(this, context);
        markwon = Markwon.create(context);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_USER) {
            return new UserChatBuddyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_buddy_user_message_item_layout, parent, false));
        } else {
            return new AiChatBuddyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_buddy_ai_message_item_layout, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageEntity message = messageList.get(position);

        if (message.isFromAi()) {
            //Use AI generated string which contains markdown and convert to format usable by textview.
            String text = message.getMessage();
            AiChatBuddyViewHolder aiChatBuddyViewHolder = (AiChatBuddyViewHolder) holder;
            markwon.setMarkdown(aiChatBuddyViewHolder.message, text);

            String imagePath = imageHashmap.get("ai_avatar");
            try {
                File imageFile = new File(imagePath);
                Glide.with(holder.itemView.getContext()).load(imageFile).error(R.drawable.outline_image_not_supported_24).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(((AiChatBuddyViewHolder) holder).imageView);
            } catch (NullPointerException e) {
                Log.e("Error", "Image not set yet");
                Glide.with(holder.itemView.getContext()).load(R.drawable.outline_image_not_supported_24).into(((AiChatBuddyViewHolder) holder).imageView);
            }
        } else {
            ((UserChatBuddyViewHolder) holder).message.setText(message.getMessage());
            String imagePath = imageHashmap.get("user_avatar");
            try {
                File imageFile = new File(imagePath);
                Glide.with(holder.itemView.getContext()).load(imageFile).error(R.drawable.outline_image_not_supported_24).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(((UserChatBuddyViewHolder) holder).imageView);
            } catch (NullPointerException e) {
                Log.e("Error", "Image not set yet");
                Glide.with(holder.itemView.getContext()).load(R.drawable.outline_image_not_supported_24).into(((UserChatBuddyViewHolder) holder).imageView);
            }

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            if(messageList.get(position).isFromAi()){
                Object payload = payloads.get(payloads.size() - 1);
                if(payload instanceof MessageDiff){
                    MessageDiff messageDiff = (MessageDiff) payload;
                    animator.queueTextChange(((AiChatBuddyViewHolder) holder).message, messageDiff.getMessageDiff(), messageDiff.getNewMessage());
//                    ((AiChatBuddyViewHolder) holder).message.setText(((MessageDiff) payload).newMessage);
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).isFromAi() ? VIEW_TYPE_AI : VIEW_TYPE_USER;
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        observer.stopWatching();
    }

    private void updateReminders(List<MessageEntity> newMessageList) {
        MessageDiffCallback diffCallback = new MessageDiffCallback(messageList, newMessageList);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffCallback);
        messageList = newMessageList;
        result.dispatchUpdatesTo(myListUpdateCallback);
    }

    private static class MyListUpdateCallback implements ListUpdateCallback {
        private final AdapterListUpdateCallback adapterListUpdateCallback;

        private SoundPool soundPool;
        private int soundId;
        private final int MAX_STREAMS = 5;
        private final float LEFT_VOLUME = 1.0f;
        private final float RIGHT_VOLUME = 1.0f;
        private final int PRIORITY = 0;
        private final int LOOP = 0;
        private final float RATE = 1.0f;

        public MyListUpdateCallback(RecyclerView.Adapter adapter, Context context) {
            adapterListUpdateCallback = new AdapterListUpdateCallback(adapter);
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(
                            new AudioAttributes.Builder()
                                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                    .setUsage(AudioAttributes.USAGE_MEDIA)
                                    .build()
                    ).setMaxStreams(MAX_STREAMS)
                    .build();
            soundId = soundPool.load(context, R.raw.message_pop, 1);
        }

        @Override
        public void onInserted(int position, int count) {
            if(count <= 2){
                soundPool.play(soundId, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY, LOOP, RATE);
            }
            adapterListUpdateCallback.onInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            adapterListUpdateCallback.onRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            adapterListUpdateCallback.onMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count, @Nullable Object payload) {
            adapterListUpdateCallback.onChanged(position, count, payload);
        }
    }

    public class UserChatBuddyViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        ImageView imageView;

        public UserChatBuddyViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.user_message_textview);
            imageView = itemView.findViewById(R.id.user_message_imageview);
        }
    }

    public class AiChatBuddyViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        ImageView imageView;

        public AiChatBuddyViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.ai_message_textview);
            imageView = itemView.findViewById(R.id.ai_message_imageview);
        }
    }

    private class FileChangeObserver extends FileObserver {
        private final String filePath;

        public FileChangeObserver(String path) {
            super(path);
            this.filePath = path;
        }

        @Override
        public void onEvent(int event, @Nullable String s) {
            switch (event) {
                case FileObserver.MODIFY:
                    new Thread(() -> {
                        for (int i = 0; i < messageList.size(); i++) {
                            if (!messageList.get(i).isFromAi()) {
                                int finalI = i;
                                mainActivity.runOnUiThread(() -> notifyItemChanged(finalI));
                            }
                        }
                    }).start();
            }
        }
    }

    private class MessageDiffCallback extends DiffUtil.Callback {
        private final List<MessageEntity> oldList;
        private final List<MessageEntity> newList;

        MessageDiffCallback(List<MessageEntity> oldList, List<MessageEntity> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return (oldList.get(oldItemPosition).getMessageId() == newList.get(newItemPosition).getMessageId());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            String oldMessage = oldList.get(oldItemPosition).getMessage().trim();
            String newMessage = newList.get(newItemPosition).getMessage().trim();
            return (oldMessage.equals(newMessage));
        }

        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            String oldMessage = oldList.get(oldItemPosition).getMessage().trim();
            String newMessage = newList.get(newItemPosition).getMessage().trim();

            if(!oldMessage.equals(newMessage)){
                return new MessageDiff(oldMessage, newMessage);
            }else { return null; }
        }
    }

    private class MessageDiff{
        private String oldMessage;
        private String newMessage;

        private MessageDiff(String oldMessage, String newMessage){
            this.oldMessage = oldMessage;
            this.newMessage = newMessage;
        }

        public String getMessageDiff(){
            return StringUtils.difference(oldMessage, newMessage);
        }

        public String getNewMessage() { return newMessage; }
    }

    private class TextAnimator{
        private final Queue<Runnable> runnableQueue = new LinkedList<>();
        private final Handler handler = new Handler();
        private boolean isRunning = false;
        private final long TEXT_SPEED = 10;

        public void queueTextChange(final TextView textView, final String textDiff, final String newText) {
            Runnable runnable = new Runnable() {
                final int[] index = {0};

                @Override
                public void run() {
                    if (index[0] < textDiff.length()) {
                        textView.append(String.valueOf(textDiff.charAt(index[0])));
                        index[0]++;
                        recyclerView.post(() -> recyclerView.smoothScrollToPosition(messageList.size()));
                        handler.postDelayed(this, TEXT_SPEED);
                    } else {
                        // When this Runnable finishes, start the next one
                        isRunning = false;
                        executeNextRunnable(textView, newText);
                    }
                }
            };

            // Add the runnable to the queue
            runnableQueue.add(runnable);

            // If no task is currently running, execute the next one in the queue
            if (!isRunning) {
                executeNextRunnable(textView, newText);
            }
        }
        private void executeNextRunnable(TextView textView, String newText) {
            Runnable nextRunnable = runnableQueue.poll();
            if (nextRunnable != null) {
                isRunning = true;
                handler.post(nextRunnable);
            }else{
                markwon.setMarkdown(textView, newText);
            }
        }
    }


}
