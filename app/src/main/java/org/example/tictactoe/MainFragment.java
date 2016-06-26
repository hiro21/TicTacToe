package org.example.tictactoe;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by setohiroyuki on 2016/06/14.
 */
public class MainFragment extends Fragment {

    private AlertDialog mDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //View rootView = inflater.inflate(null, container, false);

        // button execute
        View abountButton = rootView.findViewById(R.id.about_button);
        //View abountButton = rootView.findViewById('1');
        abountButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                                                builder.setTitle(R.string.about_title);
                                                builder.setMessage(R.string.about_text);
                                                builder.setCancelable(false);
                                                builder.setPositiveButton(R.string.ok_label, null);
                                                mDialog = builder.show();
                                            }
                                        }
        );

        View newButton = rootView.findViewById(R.id.new_button);
        View continueButton = rootView.findViewById(R.id.continue_button);
        //View newButton = rootView.findViewById('1');
        //View continueButton = rootView.findViewById('1');
        newButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GameActivity.class);
                getActivity().startActivity(intent);
            }
        });
        continueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GameActivity.class);
                intent.putExtra(GameActivity.KEY_RESTORE, true);
                getActivity().startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

}
