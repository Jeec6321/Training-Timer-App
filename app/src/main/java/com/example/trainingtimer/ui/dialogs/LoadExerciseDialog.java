package com.example.trainingtimer.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.trainingtimer.Managers.ApplicationManager;
import com.example.trainingtimer.R;
import com.example.trainingtimer.models.Exercise;

import java.util.List;

public class LoadExerciseDialog extends AppCompatDialogFragment {

    private LinearLayout llExercises;
    private TextView tvExercisesNotFound;
    private LoadExerciseListener listener;

    public LoadExerciseDialog( LoadExerciseListener listener ){
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate( R.layout.dialog_load_exercise, null );
        builder.setView( view );

        tvExercisesNotFound = view.findViewById( R.id.tv_exercise_not_found );
        llExercises = view.findViewById( R.id.ll_exercises );

        List<Exercise> exercises = ApplicationManager.getSavedExercises( getContext() );

        if( exercises.size() == 0 ){
            tvExercisesNotFound.setVisibility( View.VISIBLE );
            return builder.create() ;
        }

        for( Exercise exercise : exercises ){

            View viewExercise = inflater.inflate( R.layout.list_exercise_saved, null );

            if( exercise.getExerciseType() == Exercise.EXERCISE_TYPE_SIMPLE ){
                ( ( TextView ) viewExercise.findViewById( R.id.tv_exercise_name ) ).setText( exercise.getExerciseName() );
                ( ( TextView ) viewExercise.findViewById( R.id.tv_steps ) ).setText( "" + exercise.getSteps().size() );
                ( ( TextView ) viewExercise.findViewById( R.id.tv_work_seconds ) ).setText( "" + exercise.getSteps().get( 0 ).getWorkSeconds() );
                ( ( TextView ) viewExercise.findViewById( R.id.tv_rest_seconds ) ).setText( "" + exercise.getSteps().get( 0 ).getRestSeconds() );
            }

            ( ( ImageButton ) viewExercise.findViewById( R.id.ibtn_delete ) ).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ApplicationManager.deleteExercise( exercise, getContext() );
                    dismiss();

                }
            });

            ( ( ConstraintLayout ) viewExercise.findViewById( R.id.cl_back ) ).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.exerciseSelected( exercise );
                    dismiss();
                }
            });

            llExercises.addView( viewExercise );
        }

        return builder.create();
    }

    public interface LoadExerciseListener{
        void exerciseSelected( Exercise exercise );
    }
}
