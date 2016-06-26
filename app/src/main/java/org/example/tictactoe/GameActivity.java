package org.example.tictactoe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by setohiroyuki on 2016/06/21.
 */
public class GameActivity extends AppCompatActivity {

    public static final String KEY_RESTORE = "key_restore";
    public static final String PREF_RESTORE = "pref_restore";
    private GameFragment mGameFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setContentView(null);

        mGameFragment = (GameFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_game);
        boolean restore = getIntent().getBooleanExtra(KEY_RESTORE, false);
        if (restore) {
            String gameData = getPreferences(MODE_PRIVATE).getString(PREF_RESTORE, null);
            if (gameData != null) {
                mGameFragment.putState(gameData);

            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        String gameData = mGameFragment.getState();
        getPreferences(MODE_PRIVATE).edit().putString(PREF_RESTORE, gameData).commit();
    }

    public void restartGame() {
        mGameFragment.restartGame();
    }

    public void reportWinner(final Tile.Owner winner) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.declare_winner, winner));
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok_label,
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        final Dialog diaog = builder.create();
        diaog.show();

        mGameFragment.initGame();

    }
}
