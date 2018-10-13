package com.example.daan.gamebacklog;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class GameBacklogAdapter extends RecyclerView.Adapter<GameBacklogAdapter.ViewHolder> {

    public List<GameBacklog> mGameBackLogList;
    private GameBacklogClickListener mGameBacklogClickListener;

    public interface GameBacklogClickListener {
        void GameBacklogClick(int position);
    }

    public GameBacklogAdapter(List<GameBacklog> gameBacklogObjects, GameBacklogClickListener mGameBacklogClickListener) {
        mGameBackLogList = gameBacklogObjects;
        this.mGameBacklogClickListener = mGameBacklogClickListener;
    }

    @Override
    public GameBacklogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new GameBacklogAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GameBacklogAdapter.ViewHolder holder, int position) {
        // Gets a single item in the list from its position
        final GameBacklog gameBacklog = mGameBackLogList.get(position);
        // The holder argument is used to reference the views inside the viewHolder
        // Populate the views with the data from the list
        holder.gameTitle.setText(gameBacklog.getGameTitle());
        holder.platformText.setText(gameBacklog.getGamePlatform());
        holder.statusText.setText(gameBacklog.getPlayStatus());
        holder.noteText.setText(gameBacklog.getGameNote());
        holder.dateText.setText(gameBacklog.getDateAdded());
    }

    @Override
    public int getItemCount() {
        return mGameBackLogList.size();
    }

    public void swapList (List<GameBacklog> newList) {
        mGameBackLogList = newList;
        if (newList != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView gameTitle;
        public TextView platformText;
        public TextView statusText;
        public TextView dateText;
        public TextView noteText;


        public ViewHolder(View itemView) {
            super(itemView);
            this.gameTitle = itemView.findViewById(R.id.gameTitle);
            this.platformText = itemView.findViewById(R.id.platformText);
            this.statusText = itemView.findViewById(R.id.statusText);
            this.dateText = itemView.findViewById(R.id.dateText);
            this.noteText = itemView.findViewById(R.id.noteText);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mGameBacklogClickListener.GameBacklogClick(clickedPosition);
        }
    }
}
