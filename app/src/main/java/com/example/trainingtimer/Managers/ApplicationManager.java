package com.example.trainingtimer.Managers;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.ConsumerIrManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.trainingtimer.models.Exercise;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class ApplicationManager extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        /*if( BuildConfig.DEBUG )
        {
            FirebaseCrashlytics.getInstance( ).setCrashlyticsCollectionEnabled( false );
        }*/

        context = getApplicationContext();

        //String token = FirebaseInstanceId.getInstance().getToken();
    }

    public static List<Exercise> getSavedExercises( Context context ){
        SharedPreferences exercisesPreferences = context.getSharedPreferences("Exercises", Context.MODE_PRIVATE);

        List<Exercise> exercisesList = new ArrayList<>();

        try{
            exercisesList = new Gson().fromJson(
                    exercisesPreferences.getString("exercises","[]"),
                    new TypeToken< List <Exercise> >() {}.getType()
            );
        } catch ( Exception e ){ }

        //System.out.println( "Memory: " + new Gson().toJson( exercises ) );
        System.out.println( "Size: " + exercisesList.size() );

        return exercisesList;
    }

    public static boolean saveExercise( Exercise exercise, Context context ){

        SharedPreferences exercisesPreferences = context.getSharedPreferences("Exercises", Context.MODE_PRIVATE);


        List<Exercise> exercisesList = getSavedExercises( context );
        exercisesList.add( exercise );

        SharedPreferences.Editor editor = exercisesPreferences.edit();
        editor.putString( "exercises", new Gson().toJson( exercisesList ) );

        editor.commit();

        return true;
    }

    public static boolean deleteExercise( Exercise exercise, Context context ){
        SharedPreferences exercisesPreferences = context.getSharedPreferences("Exercises", Context.MODE_PRIVATE);

        List<Exercise> exercisesList = getSavedExercises( context );
        List<Exercise> newExercises = new ArrayList<>();

        Gson gson = new Gson();

        for( Exercise e : exercisesList )
            if( !gson.toJson( e ).equals( gson.toJson( exercise ) ) )
                newExercises.add( e );
                //System.out.println(" Se encontro ");

        SharedPreferences.Editor editor = exercisesPreferences.edit();
        editor.putString( "exercises", new Gson().toJson( newExercises ) );

        editor.commit();

        return true;
    }

    public static boolean showDialog(Context context, DialogFragment dialogFragment ) {
        FragmentTransaction fragmentTransaction = ( (FragmentActivity)context ).getSupportFragmentManager( ).beginTransaction( );
        fragmentTransaction.add( dialogFragment, "dialog" + dialogFragment.getTag() );

        if( !( (Activity)context ).isFinishing( ) )
        {
            try
            {
                fragmentTransaction.commitAllowingStateLoss( );
            }
            catch( Exception e )
            {
                //Do nothing
            }
        }

        return true;
    }

    public static boolean closeDialogOld ( Context context, DialogFragment dialogFragment ){
        FragmentTransaction fragmentTransaction = ( (FragmentActivity)context ).getSupportFragmentManager().beginTransaction( );
        fragmentTransaction.remove( dialogFragment );

        if( !( ( Activity )context ).isFinishing( ) ){
            try
            {
                fragmentTransaction.commitAllowingStateLoss( );
            }catch ( Exception e ){
                //Do nothing
            }
        }

        return true;
    }

    public static Context getContext(){
        return context;
    }

}
