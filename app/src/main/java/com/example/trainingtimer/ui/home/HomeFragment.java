package com.example.trainingtimer.ui.home;

import android.app.Application;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.trainingtimer.Managers.ApplicationManager;
import com.example.trainingtimer.R;
import com.example.trainingtimer.models.Exercise;
import com.example.trainingtimer.ui.dialogs.LoadExerciseDialog;
import com.example.trainingtimer.ui.dialogs.SaveExerciseDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment implements LoadExerciseDialog.LoadExerciseListener {

    private HomeViewModel homeViewModel;
    private SeekBar sbSteps, sbWork, sbRest;
    private long workSeconds, restSeconds, stepTime;
    private int stepsCounter = 0, steps;
    private FloatingActionButton fabStart, fabPause, fabStop;
    private TextView tvSteps, tvWork, tvRest, tvTimerStage, tvStepsToFinish, tvStageName;
    private Chronometer chronometer;
    private boolean working = false, pause = false;
    private ConstraintLayout clConfiguration, clSummary;
    private long workTime = 0, completeWorkTime;
    private Exercise exercise;
    private ImageButton ibtnSave, ibtnLoadExercise;
    private MediaPlayer soundCounter, soundGo;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);

        soundCounter = MediaPlayer.create( getContext(), R.raw.sound_counter );
        soundGo = MediaPlayer.create( getContext(), R.raw.sound_go );

        ibtnSave = view.findViewById( R.id.ibtn_save_exercise );
        ibtnLoadExercise = view.findViewById( R.id.ibtn_open_exercises );
        tvStageName = view.findViewById( R.id.tv_stage_name );
        clSummary = view.findViewById( R.id.cl_summary );
        tvStepsToFinish = view.findViewById( R.id.tv_steps_to_finish );
        tvTimerStage = view.findViewById( R.id.tv_timer_stage );
        sbSteps = view.findViewById( R.id.sb_steps );
        sbWork = view.findViewById( R.id.sb_work );
        sbRest = view.findViewById( R.id.sb_rest );
        tvSteps = view.findViewById( R.id.tv_steps );
        tvWork = view.findViewById( R.id.tv_work );
        tvRest = view.findViewById( R.id.tv_rest );
        chronometer = view.findViewById( R.id.chronometer );
        fabStart = view.findViewById( R.id.fab_start );
        fabPause = view.findViewById( R.id.fab_pause );
        fabStop = view.findViewById( R.id.fab_stop );
        clConfiguration = view.findViewById( R.id.cl_configuration );

        clSummary.setVisibility( View.GONE );

        sbSteps.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if( !working ){
                    steps = progress;
                    String stepsLabel = getResources().getString( R.string.steps_label ).replace( "10", "" + steps );
                    tvSteps.setText( stepsLabel );
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                System.out.println( "Steps: " + seekBar.getProgress() );
                if( !working )
                    calculateWorkTime();
            }
        });

        sbWork.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if( !working ){
                    workSeconds = progress;
                    String stepsLabel = getResources().getString( R.string.work_label ).replace( "45", "" + workSeconds );
                    tvWork.setText( stepsLabel );
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                System.out.println( "Work: " + seekBar.getProgress() );
                if( !working )
                    calculateWorkTime();
            }
        });

        sbRest.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                restSeconds = progress;
                String stepsLabel = getResources().getString( R.string.rest_label ).replace( "45", "" + restSeconds);
                tvRest.setText( stepsLabel );
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                System.out.println( "Work: " + seekBar.getProgress() );
                if( !working )
                    calculateWorkTime();
            }
        });

        /*homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });*/

        fabStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.start();

                exercise = new Exercise( steps, workSeconds, restSeconds, Exercise.EXERCISE_TYPE_SIMPLE );

                workSeconds = exercise.getSteps().get( 0 ).getWorkSeconds();
                restSeconds = exercise.getSteps().get( 0 ).getRestSeconds();

                clConfiguration.setVisibility( View.GONE );
                clSummary.setVisibility( View.VISIBLE );
            }
        });

        fabPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.stop();
            }
        });

        fabStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAllValues();
            }
        });

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                workTime ++;

                System.out.println( completeWorkTime + " - " + workTime );

                printTime( completeWorkTime - workTime );
                printStage( workTime );

                if( completeWorkTime == workTime ){
                    resetAllValues();
                    chronometer.stop();
                    return;
                }
            }
        });

        ibtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercise = new Exercise( steps, workSeconds, restSeconds, Exercise.EXERCISE_TYPE_SIMPLE );

                SaveExerciseDialog saveExerciseDialog = new SaveExerciseDialog( exercise );
                //ApplicationManager.showDialog( v.getContext(), saveExerciseDialog );
                saveExerciseDialog.show( getParentFragmentManager(), "" );
            }
        });

        ibtnLoadExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadExerciseDialog loadExerciseDialog = new LoadExerciseDialog( HomeFragment.this );
                loadExerciseDialog.show( getParentFragmentManager(), "" );
            }
        });

        resetAllValues();

        return view;
    }

    private String timeToString( long time ){
        long min = time / 60;
        long sec = time - ( min * 60 );

        String minString = min >= 10 ? "" + min : "0" + min;
        String secString = sec >= 10 ? "" + sec : "0" + sec;

        String workTimeString = minString + ":" + secString;
        //System.out.println( "workSeconds: " + workSeconds + " restSeconds: " + restSeconds + " Min: " + min + " Sec: " + sec + " Time: " + time + " -> " + workTime );

        return workTimeString;
    }

    private String printStage( long time ){

        if( time % ( workSeconds + restSeconds ) == 0  )
            stepsCounter ++;

        long timeInRange = time - stepTime * stepsCounter;

        if( timeInRange < workSeconds ) {
            timeInRange = workSeconds - timeInRange;
            tvStageName.setText("WORK");
            tvStageName.setTextColor( getResources().getColor( R.color.green ) );
        } else {
            timeInRange -= workSeconds;
            timeInRange = restSeconds - timeInRange;
            tvStageName.setText("REST");
            tvStageName.setTextColor( getResources().getColor( R.color.red ) );
        }



        if( timeInRange == 3 || timeInRange == 2 || timeInRange == 1 ) // timeInRange == 10 || timeInRange == 5 || timeInRange == 4 ||
            soundCounter.start();

        if( timeInRange == workSeconds || timeInRange == restSeconds )
            soundGo.start();

        String stageTime = timeToString( timeInRange );
        tvTimerStage.setText( stageTime );

        tvStepsToFinish.setText( getResources().getString(R.string.steps_to_finishlabel).replace("0", "" + (steps - stepsCounter)) );

        return stageTime;
    }

    private String printTime( long time ){
        String workTimeString = timeToString( time );
        chronometer.setText( workTimeString );

        return workTimeString;
    }

    private long calculateWorkTime(){

        stepTime = workSeconds + restSeconds;
        completeWorkTime = ( stepTime ) * steps;

        printTime( completeWorkTime );

        return completeWorkTime;
    }

    private void resetAllValues(){
        working = false;
        pause = false;
        chronometer.stop();
        steps = sbSteps.getProgress();
        workSeconds = sbWork.getProgress();
        restSeconds = sbRest.getProgress();
        calculateWorkTime();
        workTime = 0;
        clConfiguration.setVisibility( View.VISIBLE );
        clSummary.setVisibility( View.GONE );
        stepsCounter = 0;
    }

    @Override
    public void exerciseSelected(Exercise exercise) {
        this.exercise = exercise;
        if( exercise.getExerciseType() == Exercise.EXERCISE_TYPE_SIMPLE ){
            sbSteps.setProgress( exercise.getSteps().size() );
            sbWork.setProgress( (int) exercise.getSteps().get( 0 ).getWorkSeconds() );
            sbRest.setProgress( (int) exercise.getSteps().get( 0 ).getRestSeconds() );
            if( !working )
                calculateWorkTime();
        }
    }
}