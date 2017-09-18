package com.Jaliee.IrcBot;

/**
 * Created by Jocke on 2017-09-15.
 */
public class Main {

    public static void main(String[] args) {

        // Start bot.
        IrcBot bot = new IrcBot("server.cfg", "channels.cfg");

        // Enable debugging.
        bot.setVerbose(true);

    }
}
