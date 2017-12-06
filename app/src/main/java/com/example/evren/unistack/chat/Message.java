package com.example.evren.unistack.chat;

/**
 * Created by EVREN on 22.10.2017.
 */

public class Message {
    private String message;
    private String username;

    public Message(String username, String message){
        this.message=message;
        this.username = username;
    }

    public String getMessage(){ return message;}

    public String getUsername(){return  username;}
}
