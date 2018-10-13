package com.example.daan.gamebacklog;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GameBacklogAdapter.GameBacklogClickListener{

    private RecyclerView mRecyclerView;
    private List<GameBacklog> mGameBackLogItems;
    private GameBacklogAdapter mAdapter;

    public static final int REQUESTCODE_ADD = 1234;
    public static final int REQUESTCODE_UPDATE = 12345;
    public static final String EXTRA_ADD_BACKLOG = "BacklogAdd";
    public static final String EXTRA_UPDATE_BACKLOG = "BacklogRemove";

    static AppDatabase db;

    public final static int TASK_GET_ALL_GAMEBACKLOGS = 0;
    public final static int TASK_DELETE_GAMEBACKLOG = 1;
    public final static int TASK_UPDATE_GAMEBACKLOG = 2;
    public final static int TASK_INSERT_GAMEBACKLOG = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = findViewById(R.id.recyclerView);
        mGameBackLogItems = new ArrayList<>();

        db = AppDatabase.getsInstance(this);
        new GameBacklogAsyncTask(TASK_GET_ALL_GAMEBACKLOGS).execute();

        //Set layoutmanager with span of 1
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(1,
                LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new GameBacklogAdapter(mGameBackLogItems,this);
        mRecyclerView.setAdapter(mAdapter);

        updateUI();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddGameBacklogActivity.class);
                startActivityForResult(intent, REQUESTCODE_ADD);
            }
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder
                            target) {
                        return false;
                    }

                    //Called when a user swipes left or right on a ViewHolder
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        //Get the index corresponding to the selected position
                        int position = (viewHolder.getAdapterPosition());
                        new GameBacklogAsyncTask(TASK_DELETE_GAMEBACKLOG).execute(mGameBackLogItems.get(position));
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void GameBacklogClick(int position) {
        Intent intent = new Intent(MainActivity.this, UpdateGameBacklogActivity.class);
        intent.putExtra(EXTRA_UPDATE_BACKLOG, mGameBackLogItems.get(position));
        startActivityForResult(intent, REQUESTCODE_UPDATE);
    }

    public void onGameBacklogDbUpdated(List list) {
        mGameBackLogItems = list;
        updateUI();
    }

    public void updateUI() {
        if (mAdapter == null) {
            mAdapter = new GameBacklogAdapter(mGameBackLogItems, this);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.swapList(mGameBackLogItems);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch(requestCode) {
                case REQUESTCODE_ADD:
                    GameBacklog addedGameBacklog = data.getParcelableExtra(MainActivity.EXTRA_ADD_BACKLOG);
                    new GameBacklogAsyncTask(TASK_INSERT_GAMEBACKLOG).execute(addedGameBacklog);
                    break;
                case REQUESTCODE_UPDATE:
                    GameBacklog updatedGameBacklog = data.getParcelableExtra(MainActivity.EXTRA_UPDATE_BACKLOG);
                    new GameBacklogAsyncTask(TASK_UPDATE_GAMEBACKLOG).execute(updatedGameBacklog);
                    updateUI();
                    break;
            }
        }
    }

    public class GameBacklogAsyncTask extends AsyncTask<GameBacklog, Void, List> {

        private int taskCode;

        public GameBacklogAsyncTask(int taskCode) {
            this.taskCode = taskCode;
        }

        @Override
        protected List doInBackground(GameBacklog... gameBacklogs) {

            switch (taskCode){
                case TASK_DELETE_GAMEBACKLOG:
                    db.gameBacklogDao().removeGameBacklog(gameBacklogs[0]);
                    break;
                case TASK_UPDATE_GAMEBACKLOG:
                    db.gameBacklogDao().updateGameBacklog(gameBacklogs[0]);
                    break;
                case TASK_INSERT_GAMEBACKLOG:
                    db.gameBacklogDao().insertGameBacklog(gameBacklogs[0]);
                    break;
            }

            //To return a new list with the updated data, we get all the data from the database again.
            return db.gameBacklogDao().getAllGameBacklog();
        }

        @Override
        protected void onPostExecute(List list) {
            super.onPostExecute(list);
            onGameBacklogDbUpdated(list);
        }
    }

}
