package com.example.evren.unistack.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.evren.unistack.R;
import java.util.ArrayList;

/**
 * Created by EVREN on 22.10.2017.
 */

    public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

        Context context;
        private ArrayList<Message> messageArrayList;
        private int[] mUsernameColors;

        public MessageAdapter(ArrayList<Message> messageArrayList, Context context){
            this.messageArrayList = messageArrayList;
            mUsernameColors = context.getResources().getIntArray(R.array.username_colors);
            this.context=context;
        }



        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_message,parent,false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Message message = messageArrayList.get(position);
            holder.setMessage(message.getMessage());
            holder.setUsername(message.getUsername());
        }

        @Override
        public int getItemCount() {
            return messageArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView message,username;

            public ViewHolder(View itemView){
                super(itemView);
                message=itemView.findViewById(R.id.message_txt);
                username = itemView.findViewById(R.id.username);
            }
            public void setMessage(String messages) {
                if (null == message) return;
                message.setText(messages);
            }
            public void setUsername(String usernames) {
                if (null == username) return;
                username.setText(usernames);
                username.setTextColor(getUsernameColor(usernames));
            }
            private int getUsernameColor(String usernames) {
                int hash = 7;
                for (int i = 0, len = usernames.length(); i < len; i++) {
                    hash = usernames.codePointAt(i) + (hash << 5) - hash;
                }
                int index = Math.abs(hash % mUsernameColors.length);
                return mUsernameColors[index];
            }
        }


    }

