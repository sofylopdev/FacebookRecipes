package edu.galileo.android.tinderrecipes.libs.di;


import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import edu.galileo.android.tinderrecipes.libs.GlideImageLoader;
import edu.galileo.android.tinderrecipes.libs.GreenRobotEventBus;
import edu.galileo.android.tinderrecipes.libs.base.EventBus;
import edu.galileo.android.tinderrecipes.libs.base.ImageLoader;

@Module
public class LibsModule {
    private AppCompatActivity activity;

    public LibsModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Provides
    @Singleton
    EventBus providesEventBus(org.greenrobot.eventbus.EventBus eventBus){
        return new GreenRobotEventBus(eventBus);
    }

    @Provides
    @Singleton
    org.greenrobot.eventbus.EventBus providesLibraryEventBus(){
        return org.greenrobot.eventbus.EventBus.getDefault();
    }


    @Provides
    @Singleton
    ImageLoader providesImageLoader(RequestManager requestManager){
        return new GlideImageLoader(requestManager);
    }

    @Provides
    @Singleton
    RequestManager providesRequestManager(AppCompatActivity activity){
        return Glide.with(activity);
    }

    @Provides
    @Singleton
    AppCompatActivity providesActivity(){
        return this.activity;
    }
}
