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
import com.Denzo.firl.Utils.ActivityTracker;
import com.Denzo.firl.Model.ActivityLog;
import com.Denzo.firl.R;
import com.Denzo.firl.Utils.SessionManager;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private List<ChatMessage> messageList;
    private EditText messageInput;
    private ImageButton sendBtn, backBtn, addBtn, micBtn;
    private ImageView userImageHeader;
    private TextView userNameTitle, typingIndicator;
    private boolean isSending = false;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private static final int MOCK_TYPING_DELAY = 3000;
    private static final int MOCK_REPLY_DELAY = 1500;
    private static final int MOCK_SEND_DELAY = 800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initViews();
        setupChatHeader();
        setupRecyclerView();
        setupListeners();
        
        // Mock a typing indicator
        handler.postDelayed(() -> showTypingIndicator(true), MOCK_TYPING_DELAY);
    }

    private void initViews() {
        userNameTitle = findViewById(R.id.chat_user_name);
        userImageHeader = findViewById(R.id.chat_user_image);
        typingIndicator = findViewById(R.id.typing_indicator);
        backBtn = findViewById(R.id.backBtn);
        recyclerView = findViewById(R.id.chatRecyclerView);
        messageInput = findViewById(R.id.messageInput);
        sendBtn = findViewById(R.id.sendBtn);
        addBtn = findViewById(R.id.plusBtn); 
        micBtn = findViewById(R.id.micBtn);
    }

    private void setupChatHeader() {
        String matchName = getIntent().getStringExtra("matchName");
        String matchImageUrl = getIntent().getStringExtra("matchImageUrl");
        
        userNameTitle.setText(matchName != null ? matchName : getString(R.string.title_activity_teeeeeest)); // Using existing string for now
        
        if (matchImageUrl != null && !matchImageUrl.isEmpty() && !matchImageUrl.equals("default")) {
            Glide.with(this).load(matchImageUrl).into(userImageHeader);
        } else {
            userImageHeader.setImageResource(R.drawable.monkey);
        }
    }

    private void setupRecyclerView() {
        messageList = new ArrayList<>();
        addMockMessages();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter(messageList);
        recyclerView.setAdapter(adapter);
    }

    private void addMockMessages() {
        messageList.add(new ChatMessage("Hey there!", false, System.currentTimeMillis()));
        messageList.add(new ChatMessage("Hi! How's it going?", true, System.currentTimeMillis()));
        messageList.add(new ChatMessage("Pretty good, just exploring the app.", false, System.currentTimeMillis()));
    }

    private void setupListeners() {
        backBtn.setOnClickListener(v -> finish());
        sendBtn.setOnClickListener(v -> sendMessage());
        addBtn.setOnClickListener(v -> showInteractionsMenu());
        setupMicListener();
    }

    private void setupMicListener() {
        micBtn.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case android.view.MotionEvent.ACTION_DOWN:
                    messageInput.setHint(R.string.chat_recording);
                    micBtn.setColorFilter(getColor(R.color.brand_primary));
                    return true;
                case android.view.MotionEvent.ACTION_UP:
                    messageInput.setHint(R.string.chat_hint);
                    micBtn.setColorFilter(getColor(android.R.color.white));
                    sendVoiceNote();
                    return true;
            }
            return false;
        });
    }

    private void sendVoiceNote() {
        addMessage(new ChatMessage("Voice Note (0:05)", true, System.currentTimeMillis()));
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
        addMessage(new ChatMessage(title, true, System.currentTimeMillis(), 
                       ChatMessage.MessageType.DATE_INVITE, message));
    }

    private void sendRandomIcebreaker() {
        String[] prompts = {
            "What's your dream travel destination?",
            "What's the most adventurous thing you've ever done?",
            "If you could have dinner with anyone, who would it be?",
            "What's your favorite way to spend a Sunday morning?"
        };
        String prompt = prompts[new Random().nextInt(prompts.length)];
        addMessage(new ChatMessage(prompt, true, System.currentTimeMillis(), 
                       ChatMessage.MessageType.ICEBREAKER, null));
    }

    private void addMessage(ChatMessage message) {
        messageList.add(message);
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
        
        handler.postDelayed(() -> {
            isSending = false;
            sendBtn.setEnabled(true);
            sendBtn.setAlpha(1.0f);
            
            addMessage(new ChatMessage(text, true, System.currentTimeMillis()));
            messageInput.setText("");
            ActivityTracker.getInstance().log("Send Message", ActivityLog.Status.SUCCESS, "To " + userNameTitle.getText());
            
            showTypingIndicator(true);
            handler.postDelayed(() -> {
                showTypingIndicator(false);
                addMessage(new ChatMessage("That sounds cool! Tell me more.", false, System.currentTimeMillis()));
            }, MOCK_REPLY_DELAY);
            
        }, MOCK_SEND_DELAY);
    }

    private void showTypingIndicator(boolean show) {
        if (typingIndicator != null) {
            typingIndicator.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}
