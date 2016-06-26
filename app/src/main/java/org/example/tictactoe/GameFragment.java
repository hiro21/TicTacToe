package org.example.tictactoe;

//import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by setohiroyuki on 2016/06/21.
 */
public class GameFragment extends Fragment {

    static private int mLargeIds[] = {R.id.large1, R.id.large2, R.id.large3, R.id.large4, R.id.large5, R.id.large6, R.id.large7, R.id.large8, R.id.large9};
    static private int mSmallIds[] = {R.id.small1, R.id.small2, R.id.small3, R.id.small4, R.id.small5, R.id.small6, R.id.small7, R.id.small8, R.id.small9};
    //static private int mLargeIds[] = {};
    //static private int mSmallIds[] = {};
    private Tile mEntireBoard = new Tile(this);
    private Tile mLargeTiles[] = new Tile[9];
    private Tile mSmallTiles[][] = new Tile[9][9];
    private Tile.Owner mPlayer = Tile.Owner.X;
    private Set<Tile> mAvailable = new HashSet<>();
    private int mLastLarge;
    private int mLastSmall;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setRetainInstance(true);
        initGame();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View rootView = inflater.inflate(R.layout.large_board, container,  false);
        //View rootView = inflater.inflate(null, container,  false);
        initView(rootView);

        updateAllTiles();
        return rootView;
    }

    public void initGame() {
        mEntireBoard = new Tile(this);
        for (int i = 0; i < 9; i++) {
            mLargeTiles[i] = new Tile(this);

            for (int j = 0; j < 9; j++) {
                mSmallTiles[i][j] = new Tile(this);
            }
            mLargeTiles[i].setSubTiles(mSmallTiles[i]);
        }
        mEntireBoard.setSubTiles(mLargeTiles);

        mLastSmall = -1;
        mLastLarge = -1;
        setAvailableFromLastMove(mLastSmall);
    }

    private void initView(View rootView) {
        mEntireBoard.setView(rootView);
        for (int i = 0; i < 9; i++) {
            View outer = rootView.findViewById(mLargeIds[i]);
            mLargeTiles[i].setView(outer);

            for (int j = 0; j < 9; j++) {
                ImageButton inner = (ImageButton)outer.findViewById(mSmallIds[j]);

                final int fLarge = i;
                final int fSmall = j;
                final Tile smallTile = mSmallTiles[i][j];

                smallTile.setView(inner);
                inner.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View view) {
                        if (isAvailable(smallTile)) {
                            makeMove(fLarge, fSmall);
                            switchTurns();
                        }
                    }
                });
            }
        }
    }

    private void makeMove(int large, int small) {
        mLastLarge = large;
        mLastSmall = small;

        Tile smallTile = mSmallTiles[large][small];
        Tile largeTile = mLargeTiles[large];

        smallTile.setOwner(mPlayer);
        setAvailableFromLastMove(small);

        Tile.Owner oldWinner = largeTile.getOwner();
        Tile.Owner winner = largeTile.findWinner();

        if (winner != oldWinner) {
            largeTile.setOwner(winner);
        }

        winner = mEntireBoard.findWinner();
        mEntireBoard.setOwner(winner);

        updateAllTiles();
        if (winner != Tile.Owner.NEITHER) {
            ((GameActivity)getActivity()).reportWinner(winner);
        }
    }

    private void switchTurns() {
        mPlayer = mPlayer == Tile.Owner.X ? Tile.Owner.O : Tile.Owner.X;
    }

    public void restartGame() {
        initGame();
        initView(getView());
        updateAllTiles();
    }

    private void clearAvailable() {
        mAvailable.clear();
    }

    private void addAvailable(Tile tile) {
        mAvailable.add(tile);
    }

    public boolean isAvailable(Tile tile) {
        return mAvailable.contains(tile);
    }

    private void setAvailableFromLastMove(int small) {
        clearAvailable();

        if (small != -1) {
            for (int i = 0; i < 9; i++) {
                Tile tile = mSmallTiles[small][i];
                if (tile.getOwner() == Tile.Owner.NEITHER) {
                    addAvailable(tile);
                }
            }
        }

        if (mAvailable.isEmpty()) {
            setAllAvailable();
        }
    }

    private void setAllAvailable() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Tile tile = mSmallTiles[i][j];
                if (tile.getOwner() == Tile.Owner.NEITHER) {
                    addAvailable(tile);
                }
            }
        }
    }

    public String getState() {
        StringBuilder builder = new StringBuilder();
        builder.append(mLastLarge);
        builder.append(",");
        builder.append(mLastSmall);
        builder.append(",");

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                builder.append(mSmallTiles[i][j].getOwner().name());
                builder.append(",");
            }
        }

        return builder.toString();
    }

    public void putState(String gameData) {
        String[] fields = gameData.split(",");

        int index = 0;
        mLastLarge = Integer.parseInt(fields[index++]);
        mLastSmall = Integer.parseInt(fields[index++]);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Tile.Owner owner = Tile.Owner.valueOf(fields[index++]);
                mSmallTiles[i][j].setOwner(owner);
            }
        }
        setAvailableFromLastMove(mLastSmall);
        updateAllTiles();
    }

    private void updateAllTiles() {
        mEntireBoard.updateDrawableState();
        for (int i = 0; i < 9; i++) {
            mLargeTiles[i].updateDrawableState();

            for (int j = 0; j < 9; j++) {
                mSmallTiles[i][j].updateDrawableState();
            }
        }
    }

}
