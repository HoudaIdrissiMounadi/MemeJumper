package com.memejumper.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;

public class LeaderboardClient {
    private static final String API_URL = "https://api.memejumper.com/scores";

    public void submitScore(String playerName, long score) {
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        Net.HttpRequest httpRequest = requestBuilder.newRequest()
            .method(Net.HttpMethods.POST)
            .url(API_URL)
            .content("name=" + playerName + "&score=" + score)
            .build();

        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                Gdx.app.log("Leaderboard", "Score submitted successfully: " + httpResponse.getResultAsString());
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("Leaderboard", "Failed to submit score", t);
            }

            @Override
            public void cancelled() {
                Gdx.app.log("Leaderboard", "Score submission cancelled");
            }
        });
    }
}
