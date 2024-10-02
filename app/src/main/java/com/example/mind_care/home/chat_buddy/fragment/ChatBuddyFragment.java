package com.example.mind_care.home.chat_buddy.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mind_care.BuildConfig;
import com.example.mind_care.R;
import com.example.mind_care.database.chat_buddy.MessageEntity;
import com.example.mind_care.home.BaseTools;
import com.example.mind_care.home.chat_buddy.adapter.ChatBuddyAdapter;
import com.example.mind_care.home.chat_buddy.viewmodel.ChatBuddyViewModel;
import com.example.mind_care.home.chat_buddy.viewmodel.ChatBuddyViewModelFactory;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.ChatFutures;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.ai.client.generativeai.type.InvalidStateException;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChatBuddyFragment extends BaseTools implements View.OnClickListener {
    private TextInputLayout textInputLayout;
    private TextInputEditText editText;
    private ChatFutures chat;
    private View.OnClickListener onSendClickListener;
    private ChatBuddyViewModel chatBuddyViewModel;
    private boolean hasCheckedDatabase = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chat_buddy_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.onSendClickListener = this;
        ChatBuddyViewModelFactory factory = new ChatBuddyViewModelFactory(requireActivity().getApplication());
        chatBuddyViewModel = new ViewModelProvider(this, factory).get(ChatBuddyViewModel.class);


        //Create Gemini API model
        GenerativeModel gm = new GenerativeModel("gemini-1.5-flash-002", BuildConfig.GEMINI_API_KEY);
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);


        List<Content> history = new ArrayList<>();

        chatBuddyViewModel.getMessagesLiveData().observe(getViewLifecycleOwner(), messages -> {
            if(hasCheckedDatabase == false){
                if(messages.isEmpty()){
                    //No messages in database yet.
                    Content.Builder modelContentBuilder = new Content.Builder();
                    String introString = "Hello there how can I help you?";
                    modelContentBuilder.addText(introString);
                    modelContentBuilder.setRole("model");
                    Content modelContent = modelContentBuilder.build();

                    history.add(modelContent);

                    chatBuddyViewModel.insertMessage(new MessageEntity(true, introString));
                }else{
                    //Messages in database.
                    Content.Builder tempContentBuilder = new Content.Builder();

                    for(MessageEntity message : messages){
                        if(message.isFromAi()){
                            tempContentBuilder.setRole("model");
                        }else{
                            tempContentBuilder.setRole("user");
                        }
                        tempContentBuilder.addText(message.getMessage());
                        history.add(tempContentBuilder.build());
                    }
                }
                hasCheckedDatabase = true;
                chat = model.startChat(history);
            }
            for(MessageEntity message : messages){
                Log.i("INFO", "Message is" + message.getMessage());
            }
        });

        //get layouts and set up recyclerview
        textInputLayout = view.findViewById(R.id.chat_buddy_text_input_layout);
        editText = view.findViewById(R.id.chat_buddy_edit_text);

        RecyclerView recyclerView = view.findViewById(R.id.chat_buddy_recyclerview);
        recyclerView.setAdapter(new ChatBuddyAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //TODO set up recycler view animations for items as well

        textInputLayout.setEndIconOnClickListener(this);


    }


    @Override
    public void setOnFabClickedDestination(FloatingActionButton fab, NavController navController) {
    }

    @Override
    public void setFabImage(FloatingActionButton fab) {
//        // Set the image for the FAB
//        fab.setImageResource(R.drawable.outline_send_10);
    }

    @Override
    public void onClick(View view) {
        String message = editText.getText().toString();
//        if(message.isEmpty()){
//            textInputLayout.setError("Please enter a message");
//            return;
//        }
        chatBuddyViewModel.insertMessage(new MessageEntity(false, message));

        //resets text input
        editText.setText("");
        editText.clearFocus();
        textInputLayout.postDelayed(()-> textInputLayout.setEndIconOnClickListener(null), 200);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null){
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }

        Content.Builder messageBuilder = new Content.Builder();
        messageBuilder.setRole("user");
        messageBuilder.addText(message);
        Content messageContent = messageBuilder.build();
        try{
            Publisher<GenerateContentResponse> streamingResponse = chat.sendMessageStream(messageContent);
            StringBuilder stringOutput = new StringBuilder();
            streamingResponse.subscribe(new Subscriber<GenerateContentResponse>() {
                @Override
                public void onSubscribe(Subscription s) {
                    s.request(Long.MAX_VALUE);
                }

                @Override
                public void onNext(GenerateContentResponse generateContentResponse) {
                    String chunk = generateContentResponse.getText();
                    stringOutput.append(chunk);
                }

                @Override
                public void onError(Throwable t) {

                }

                @Override
                public void onComplete() {
                    Log.i("INFO", "AI STRING IS:" + stringOutput);
                    chatBuddyViewModel.insertMessage(new MessageEntity(true, stringOutput.toString()));
                    requireActivity().runOnUiThread(()-> textInputLayout.setEndIconOnClickListener(onSendClickListener));
                }
            });
        } catch (InvalidStateException e){
            Toast.makeText(getContext(), "Please wait for the previous message to be sent", Toast.LENGTH_SHORT).show();
        }
    }
}
