package com.example.mind_care.home.chat_buddy.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatBuddyFragment extends BaseTools implements View.OnClickListener {
    private TextInputLayout textInputLayout;
    private TextInputEditText editText;
    private ChatFutures chat;
    private View.OnClickListener onSendClickListener;
    private ChatBuddyViewModel chatBuddyViewModel;
    private boolean hasCheckedDatabase = false;
    private final String image_file_name = "ai_avatar_image.png";
    private RecyclerView.Adapter chatBuddyAdapter;
    private final HashMap<String, String> imageHashmap = new HashMap<>();

    private final ActivityResultLauncher<PickVisualMediaRequest> pickMediaLauncher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
        if (uri != null) {
            saveImageToInternalStorage(uri);
        }
    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chat_buddy_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
        this.onSendClickListener = this;
        ChatBuddyViewModelFactory factory = new ChatBuddyViewModelFactory(requireActivity().getApplication());
        chatBuddyViewModel = new ViewModelProvider(this, factory).get(ChatBuddyViewModel.class);


        //Create Gemini API model
        GenerativeModel gm = new GenerativeModel("gemini-1.5-flash-002", BuildConfig.GEMINI_API_KEY);
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);


        List<Content> history = new ArrayList<>();

        chatBuddyViewModel.getMessagesLiveData().observe(getViewLifecycleOwner(), messages -> {
            if (!hasCheckedDatabase) {
                if (messages.isEmpty()) {
                    //No messages in database yet.
                    Content.Builder modelContentBuilder = new Content.Builder();
                    String introString = "Hello there how can I help you?";
                    modelContentBuilder.addText(introString);
                    modelContentBuilder.setRole("model");
                    Content modelContent = modelContentBuilder.build();

                    history.add(modelContent);

                    chatBuddyViewModel.insertMessage(new MessageEntity(true, introString));
                } else {
                    //Messages in database.
                    Content.Builder tempContentBuilder = new Content.Builder();

                    for (MessageEntity message : messages) {
                        if (message.isFromAi()) {
                            tempContentBuilder.setRole("model");
                        } else {
                            tempContentBuilder.setRole("user");
                        }
                        tempContentBuilder.addText(message.getMessage());
                        history.add(tempContentBuilder.build());
                    }
                }
                hasCheckedDatabase = true;
                chat = model.startChat(history);
            }
            for (MessageEntity message : messages) {
                Log.i("INFO", "Message is" + message.getMessage());//TODO
            }
        });

        //get layouts and set up recyclerview
        textInputLayout = view.findViewById(R.id.chat_buddy_text_input_layout);
        editText = view.findViewById(R.id.chat_buddy_edit_text);

        RecyclerView recyclerView = view.findViewById(R.id.chat_buddy_recyclerview);
        chatBuddyAdapter = new ChatBuddyAdapter(chatBuddyViewModel, getViewLifecycleOwner(), imageHashmap);
        recyclerView.setAdapter(chatBuddyAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadImageFromInternalStorage();

        //TODO set up recycler view animations for items as well

        textInputLayout.setEndIconOnClickListener(this);


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.chat_buddy_options_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.chat_buddy_change_avatar) {
            //Get image from image picker and add to internal storage for retrieval later
            pickMediaLauncher.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
        } else if (itemId == R.id.chat_buddy_reset_chat) {
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.chat_buddy_reset_chat_dialog_title)
                    .setMessage(R.string.chat_buddy_reset_chat_dialog_message)
                    .setPositiveButton(R.string.chat_buddy_reset_chat_dialog_yes, (dialog, which) -> {
                        chatBuddyViewModel.resetChat();
                        dialog.dismiss();
                    })
                    .setNegativeButton(R.string.chat_buddy_reset_chat_dialog_no, (dialog, which) -> {
                        dialog.dismiss();
                    }).show();
        }
        return super.onOptionsItemSelected(item);
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
        textInputLayout.postDelayed(() -> textInputLayout.setEndIconOnClickListener(null), 200);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }

        Content.Builder messageBuilder = new Content.Builder();
        messageBuilder.setRole("user");
        messageBuilder.addText(message);
        Content messageContent = messageBuilder.build();
        try {
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
                    Log.i("INFO", "AI STRING IS:" + stringOutput);// TODO
                    chatBuddyViewModel.insertMessage(new MessageEntity(true, stringOutput.toString()));
                    requireActivity().runOnUiThread(() -> textInputLayout.setEndIconOnClickListener(onSendClickListener));
                }
            });
        } catch (InvalidStateException e) {
            Toast.makeText(getContext(), "Please wait for the previous message to be sent", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImageToInternalStorage(Uri uri) {
        try { //Write Uri image to app's internal storage
            InputStream inputStream = requireActivity().getContentResolver().openInputStream(uri);
            FileOutputStream fileOutputStream = requireActivity().openFileOutput(image_file_name, Context.MODE_PRIVATE);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, length);
            }
            fileOutputStream.close();
            inputStream.close();

            String imagePath = new File(requireActivity().getFilesDir(), image_file_name).getAbsolutePath();
            imageHashmap.put("ai_avatar", imagePath);
            Log.i("INFO", String.valueOf(imageHashmap));
            chatBuddyAdapter.notifyDataSetChanged();

        } catch (FileNotFoundException e) {
            Toast.makeText(getContext(), "Image not found", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("Error", "IOException");
        }

    }

    private void loadImageFromInternalStorage() {
        File directory = requireActivity().getFilesDir();
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".png")) {
                    imageHashmap.put("ai_avatar", file.getAbsolutePath());
                }
            }
        }
        chatBuddyAdapter.notifyDataSetChanged();
    }
}
