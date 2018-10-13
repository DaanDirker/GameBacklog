package com.example.daan.gamebacklog;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateGameBacklogActivity extends AppCompatActivity {

    private EditText mGameTitle;
    private EditText mGamePlatform;
    private EditText mGameNote;
    private Spinner mGameStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_game_backlog);

        setTitle("Edit");

        //Initiate variables
        mGameTitle = findViewById(R.id.titleEditText);
        mGamePlatform = findViewById(R.id.platformEditText);
        mGameNote = findViewById(R.id.notesEditText);
        mGameStatus = findViewById(R.id.spinner);

        final GameBacklog gameBacklogUpdate = getIntent().getParcelableExtra(MainActivity.EXTRA_UPDATE_BACKLOG);

        mGameTitle.setText(gameBacklogUpdate.getGameTitle());
        mGamePlatform.setText(gameBacklogUpdate.getGamePlatform());
        mGameNote.setText(gameBacklogUpdate.getGameNote());
        mGameStatus.setSelection(getIndexSpinner(mGameStatus,gameBacklogUpdate.getPlayStatus()));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gameTitle = mGameTitle.getText().toString();
                String gamePlatform = mGamePlatform.getText().toString();
                String gameNote = mGameNote.getText().toString();
                String gameStatus = mGameStatus.getSelectedItem().toString();

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String date = formatter.format(new Date());

                if (!TextUtils.isEmpty(gameTitle)) {
                    gameBacklogUpdate.setGameTitle(gameTitle);
                    gameBacklogUpdate.setGamePlatform(gamePlatform);
                    gameBacklogUpdate.setGameNote(gamePlatform);
                    gameBacklogUpdate.setGameNote(gameNote);
                    gameBacklogUpdate.setPlayStatus(gameStatus);
                    gameBacklogUpdate.setDateAdded(date);

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(MainActivity.EXTRA_UPDATE_BACKLOG, gameBacklogUpdate);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } else {
                    Snackbar.make(view, "A title is required", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Get the index of a specified string in a spinner
     * @param spinner
     * @param myString
     * @return
     */
    private int getIndexSpinner(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }
}
