package com.example.andumgaming.g370.views.listener;



/**
 * A simple interface to allow implementation of a Listener pattern for managing notification
 * of completed background threaded tasks managed by the RecipeSearchTask
 */
public interface IMenuCallbackListener {
    void onButtonCallback( int ms);
}
