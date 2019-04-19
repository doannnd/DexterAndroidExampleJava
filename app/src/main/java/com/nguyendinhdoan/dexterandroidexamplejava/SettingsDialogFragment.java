package com.nguyendinhdoan.dexterandroidexamplejava;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import java.util.Objects;

public class SettingsDialogFragment extends DialogFragment {

    private static final String SETTING_TITLE_KEY = "SETTING_TITLE_KEY";
    private static final int SETTING_REQUEST_CODE = 103;

    public static SettingsDialogFragment newInstance(String dialogTitle) {
        SettingsDialogFragment settingsDialogFragment = new SettingsDialogFragment();
        Bundle args = new Bundle();
        args.putString(SETTING_TITLE_KEY, dialogTitle);
        settingsDialogFragment.setArguments(args);
        return settingsDialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder settingsDialog = new AlertDialog.Builder(context);

        // get dialog title from argument bundle and set for settings dialog
        if (getArguments() != null) {
            String dialogTitle = getArguments().getString(SETTING_TITLE_KEY);
            settingsDialog.setTitle(dialogTitle);
        }

        // set remain information for settings dialog
        settingsDialog.setCancelable(false);
        settingsDialog.setMessage(Objects.requireNonNull(context).getString(R.string.settings_dialog_message));
        settingsDialog.setPositiveButton(context.getString(R.string.goto_settings_button_text), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // close the dialog and open settings of phone
                dialog.dismiss();
                openSettings();
            }
        });
        settingsDialog.setNegativeButton(context.getString(R.string.cancel_settings_button_text), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // close the dialog and back to the main activity
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        return settingsDialog.create();
    }

    private void openSettings() {
        Intent intentSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        startActivityForResult(intentSettings, SETTING_REQUEST_CODE);
    }

}
