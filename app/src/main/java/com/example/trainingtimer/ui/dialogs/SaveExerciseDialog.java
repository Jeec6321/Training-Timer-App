package com.example.trainingtimer.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.trainingtimer.Managers.ApplicationManager;
import com.example.trainingtimer.R;
import com.example.trainingtimer.models.Exercise;
import com.google.gson.Gson;

public class SaveExerciseDialog extends AppCompatDialogFragment {

    private Exercise exercise;
    private Button btnAccept, btnCancel;
    private LinearLayout llExerciseSummary;
    private EditText etExerciseName;

    public SaveExerciseDialog( Exercise exercise ){
        this.exercise = exercise;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate( R.layout.dialog_save_exercise , null );
        builder.setView( view );

        etExerciseName = view.findViewById( R.id.et_exercise_name );
        llExerciseSummary = view.findViewById( R.id.ll_exercise_summary );
        btnAccept = view.findViewById( R.id.btn_accept );
        btnCancel = view.findViewById( R.id.btn_cancel );

        View viewExercise = inflater.inflate( R.layout.view_exercise_summary, null );

        if( exercise.getExerciseType() == Exercise.EXERCISE_TYPE_SIMPLE ){
            ( ( TextView ) viewExercise.findViewById( R.id.tv_steps ) ).setText( "" + exercise.getSteps().size() );
            ( ( TextView ) viewExercise.findViewById( R.id.tv_work_out ) ).setText( "" + exercise.getSteps().get( 0 ).getWorkSeconds() );
            ( ( TextView ) viewExercise.findViewById( R.id.tv_rest ) ).setText( "" + exercise.getSteps().get( 0 ).getRestSeconds() );

            llExerciseSummary.addView( viewExercise );
        }

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( etExerciseName.getText().toString().isEmpty() ){
                    Toast.makeText( getContext(), getContext().getResources().getString( R.string.toast_exercise_name_empty ), Toast.LENGTH_SHORT).show();
                    return;
                }

                exercise.setExerciseName( etExerciseName.getText().toString() );
                ApplicationManager.saveExercise( exercise, getContext());

                dismiss();
                //ApplicationManager.closeDialogOld( getContext(), SaveExerciseDialog.this );

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println( new Gson().toJson( ApplicationManager.getSavedExercises( getContext() ) ) );
                dismiss();
                //ApplicationManager.closeDialogOld( getContext(), SaveExerciseDialog.this );
            }
        });

        return builder.create();
    }
}
