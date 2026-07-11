package com.Denzo.firl.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Denzo.firl.Model.ChatMessage;
import com.Denzo.firl.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_THEIRS = 0;
    private static final int TYPE_MINE = 1;
    private static final int TYPE_DATE_INVITE = 2;
    private static final int TYPE_ICEBREAKER = 3;

    private List<ChatMessage> messages;

    public ChatAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage msg = messages.get(position);
        switch (msg.getType()) {
            case DATE_INVITE:
                return TYPE_DATE_INVITE;
            case ICEBREAKER:
                return TYPE_ICEBREAKER;
            default:
                return msg.isMine() ? TYPE_MINE : TYPE_THEIRS;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_DATE_INVITE:
                return new DateInviteViewHolder(inflater.inflate(R.layout.item_chat_date_invite, parent, false));
            case TYPE_ICEBREAKER:
                return new IcebreakerViewHolder(inflater.inflate(R.layout.item_chat_icebreaker, parent, false));
            case TYPE_MINE:
                return new ChatViewHolder(inflater.inflate(R.layout.item_chat_mine, parent, false));
            case TYPE_THEIRS:
            default:
                return new ChatViewHolder(inflater.inflate(R.layout.item_chat_theirs, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage msg = messages.get(position);
        
        if (holder instanceof ChatViewHolder) {
            ((ChatViewHolder) holder).bind(msg);
        } else if (holder instanceof DateInviteViewHolder) {
            ((DateInviteViewHolder) holder).bind(msg);
        } else if (holder instanceof IcebreakerViewHolder) {
            ((IcebreakerViewHolder) holder).bind(msg);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message_text);
        }

        void bind(ChatMessage msg) {
            messageText.setText(msg.getMessage());
        }
    }

    static class DateInviteViewHolder extends RecyclerView.ViewHolder {
        TextView title, message;
        ImageView icon;
        View accept, decline;

        public DateInviteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.invite_title);
            message = itemView.findViewById(R.id.invite_message);
            icon = itemView.findViewById(R.id.invite_icon);
            accept = itemView.findViewById(R.id.invite_accept);
            decline = itemView.findViewById(R.id.invite_decline);
        }

        void bind(ChatMessage msg) {
            title.setText(msg.getMessage());
            message.setText(msg.getExtraInfo());
            
            int iconRes = msg.getMessage().contains("Coffee") ? R.drawable.ic_location : R.drawable.love;
            icon.setImageResource(iconRes);

            accept.setOnClickListener(v -> Toast.makeText(v.getContext(), "Invitation Accepted!", Toast.LENGTH_SHORT).show());
            decline.setOnClickListener(v -> Toast.makeText(v.getContext(), "Maybe next time.", Toast.LENGTH_SHORT).show());
        }
    }

    static class IcebreakerViewHolder extends RecyclerView.ViewHolder {
        TextView prompt;
        View reply;

        public IcebreakerViewHolder(@NonNull View itemView) {
            super(itemView);
            prompt = itemView.findViewById(R.id.icebreaker_prompt);
            reply = itemView.findViewById(R.id.icebreaker_reply);
        }

        void bind(ChatMessage msg) {
            prompt.setText(msg.getMessage());
            reply.setOnClickListener(v -> Toast.makeText(v.getContext(), "Opening reply keyboard...", Toast.LENGTH_SHORT).show());
        }
    }
}
