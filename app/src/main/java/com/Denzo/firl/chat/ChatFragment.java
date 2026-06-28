package com.Denzo.firl.chat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Denzo.firl.Model.ChatMessage;
import com.Denzo.firl.R;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChatFragment extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private List<ChatMessage> messageList;
    private EditText messageInput;
    private ImageButton sendBtn, backBtn, addBtn;
    private ImageView userImageHeader;
    private TextView userNameTitle, typingIndicator;
    private boolean isSending = false;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chat);

        String matchName = getIntent().getStringExtra("matchName");
        String matchImageUrl = getIntent().getStringExtra("matchImageUrl");
        if (matchName == null) matchName = "Chat";

        userNameTitle = findViewById(R.id.chat_user_name);
        userNameTitle.setText(matchName);
        
        userImageHeader = findViewById(R.id.chat_user_image);
        if (matchImageUrl != null && !matchImageUrl.isEmpty() && !matchImageUrl.equals("default")) {
            Glide.with(this).load(matchImageUrl).into(userImageHeader);
        } else {
            userImageHeader.setImageResource(R.drawable.monkey);
        }

        typingIndicator = findViewById(R.id.typing_indicator);

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        messageList = new ArrayList<>();
        messageList.add(new ChatMessage("Hey there!", false, System.currentTimeMillis()));
        messageList.add(new ChatMessage("Hi! How's it going?", true, System.currentTimeMillis()));
        messageList.add(new ChatMessage("Pretty good, just exploring the app.", false, System.currentTimeMillis()));

        recyclerView = findViewById(R.id.chatRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter(messageList);
        recyclerView.setAdapter(adapter);

        messageInput = findViewById(R.id.messageInput);
        sendBtn = findViewById(R.id.sendBtn);
        addBtn = findViewById(R.id.plusBtn); // Need to add ID to XML

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        addBtn.setOnClickListener(v -> showInteractionsMenu());
        
        // Mock a typing indicator after 3 seconds
        handler.postDelayed(() -> showTypingIndicator(true), 3000);
    }

    private void showInteractionsMenu() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.layout_interactions_menu, null);
        dialog.setContentView(view);

        view.findViewById(R.id.action_coffee).setOnClickListener(v -> {
            sendDateInvite("Coffee Date", "Let's grab a cup of coffee!");
            dialog.dismiss();
        });

        view.findViewById(R.id.action_dinner).setOnClickListener(v -> {
            sendDateInvite("Dinner Date", "I'd love to take you out for dinner.");
            dialog.dismiss();
        });

        view.findViewById(R.id.action_icebreaker).setOnClickListener(v -> {
            sendRandomIcebreaker();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void sendDateInvite(String title, String message) {
        messageList.add(new ChatMessage(title, true, System.currentTimeMillis(), 
                       ChatMessage.MessageType.DATE_INVITE, message));
        adapter.notifyItemInserted(messageList.size() - 1);
        recyclerView.scrollToPosition(messageList.size() - 1);
    }

    private void sendRandomIcebreaker() {
        String[] prompts = {
            "What's your dream travel destination?",
            "What's the most adventurous thing you've ever done?",
            "If you could have dinner with anyone, who would it be?",
            "What's your favorite way to spend a Sunday morning?"
        };
        String prompt = prompts[new Random().nextInt(prompts.length)];
        messageList.add(new ChatMessage(prompt, true, System.currentTimeMillis(), 
                       ChatMessage.MessageType.ICEBREAKER, null));
        adapter.notifyItemInserted(messageList.size() - 1);
        recyclerView.scrollToPosition(messageList.size() - 1);
    }

    private void sendMessage() {
        if (isSending) return;
        
        String text = messageInput.getText().toString().trim();
        if (text.isEmpty()) return;

        isSending = true;
        sendBtn.setEnabled(false);
        sendBtn.setAlpha(0.5f);
        
        // Mock async send delay
        handler.postDelayed(() -> {
            isSending = false;
            sendBtn.setEnabled(true);
            sendBtn.setAlpha(1.0f);
            
            messageList.add(new ChatMessage(text, true, System.currentTimeMillis()));
            adapter.notifyItemInserted(messageList.size() - 1);
            recyclerView.scrollToPosition(messageList.size() - 1);
            messageInput.setText("");
            
            // Mock an auto-reply after 1.5s
            handler.postDelayed(() -> {
                showTypingIndicator(false);
                messageList.add(new ChatMessage("That sounds cool! Tell me more.", false, System.currentTimeMillis()));
                adapter.notifyItemInserted(messageList.size() - 1);
                recyclerView.scrollToPosition(messageList.size() - 1);
            }, 1500);
            
            showTypingIndicator(true);
        }, 800);
    }

    private void showTypingIndicator(boolean show) {
        if (typingIndicator != null) {
            typingIndicator.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}
