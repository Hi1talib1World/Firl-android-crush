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

    private List<ChatMessage> messages;

    public ChatAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage msg = messages.get(position);
        if (msg.getType() == ChatMessage.MessageType.DATE_INVITE) return 2;
        if (msg.getType() == ChatMessage.MessageType.ICEBREAKER) return 3;
        return msg.isMine() ? 1 : 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case 2:
                return new DateInviteViewHolder(inflater.inflate(R.layout.item_chat_date_invite, parent, false));
            case 3:
                return new IcebreakerViewHolder(inflater.inflate(R.layout.item_chat_icebreaker, parent, false));
            case 1:
                return new ChatViewHolder(inflater.inflate(R.layout.item_chat_mine, parent, false));
            default:
                return new ChatViewHolder(inflater.inflate(R.layout.item_chat_theirs, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage msg = messages.get(position);
        
        if (holder instanceof ChatViewHolder) {
            ((ChatViewHolder) holder).messageText.setText(msg.getMessage());
        } else if (holder instanceof DateInviteViewHolder) {
            DateInviteViewHolder vh = (DateInviteViewHolder) holder;
            vh.title.setText(msg.getMessage());
            vh.message.setText(msg.getExtraInfo());
            
            if (msg.getMessage().contains("Coffee")) {
                vh.icon.setImageResource(R.drawable.ic_location);
            } else {
                vh.icon.setImageResource(R.drawable.love);
            }

            vh.accept.setOnClickListener(v -> Toast.makeText(v.getContext(), "Invitation Accepted!", Toast.LENGTH_SHORT).show());
            vh.decline.setOnClickListener(v -> Toast.makeText(v.getContext(), "Maybe next time.", Toast.LENGTH_SHORT).show());

        } else if (holder instanceof IcebreakerViewHolder) {
            IcebreakerViewHolder vh = (IcebreakerViewHolder) holder;
            vh.prompt.setText(msg.getMessage());
            vh.reply.setOnClickListener(v -> Toast.makeText(v.getContext(), "Opening reply keyboard...", Toast.LENGTH_SHORT).show());
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
    }

    static class IcebreakerViewHolder extends RecyclerView.ViewHolder {
        TextView prompt;
        View reply;

        public IcebreakerViewHolder(@NonNull View itemView) {
            super(itemView);
            prompt = itemView.findViewById(R.id.icebreaker_prompt);
            reply = itemView.findViewById(R.id.icebreaker_reply);
        }
    }
}
