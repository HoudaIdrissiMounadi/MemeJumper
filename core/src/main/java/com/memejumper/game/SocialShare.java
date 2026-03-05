package com.memejumper.game;

import com.badlogic.gdx.Gdx;

public class SocialShare {
    public void shareScore(long score) {
        String message = "I just scored " + score + " points in MemeJumper! #MemeJumper #GenZGame";
        Gdx.app.log("SocialShare", "Sharing to social media: " + message);

        // In a real Android/iOS implementation, this would call a native share intent.
        // For desktop, it might open a browser with a pre-filled tweet URL.
    }
}
