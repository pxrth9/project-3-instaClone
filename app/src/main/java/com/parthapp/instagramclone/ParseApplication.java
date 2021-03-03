package com.parthapp.instagramclone;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models
        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("zn4bLjYv9y2maTbu2ZmaRouqVj4Xv1Rww6tFn8Rb")
                .clientKey("iEkTBUURlMJJXetdnKgCRAphETLTN1QdLcCITOkJ")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
