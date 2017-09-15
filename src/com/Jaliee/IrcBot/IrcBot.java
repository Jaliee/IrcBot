package com.Jaliee.IrcBot;

import org.jibble.pircbot.*;

/**
 * Created by Jocke on 2017-09-15.
 */
public class IrcBot extends PircBot {

    public IrcBot() {
        this.setName("JalieeBot");
    }

    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        if (message.equalsIgnoreCase("time")) {
            String time = new java.util.Date().toString();
            sendMessage(channel, sender + ": The time is now " + time);
        }

        String lcMessage = message.toLowerCase();
        switch (lcMessage) {

        }

    }

}
