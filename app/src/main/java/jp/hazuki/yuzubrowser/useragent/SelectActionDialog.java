package jp.hazuki.yuzubrowser.useragent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import jp.hazuki.yuzubrowser.R;

/**
 * Created by hazuki on 17/01/19.
 */

public class SelectActionDialog extends DialogFragment {

    private static final String POS = "pos";
    private static final String UA = "ua";

    public static final int EDIT = 0;
    public static final int DELETE = 1;

    @IntDef({EDIT, DELETE})
    public @interface ActionMode {
    }

    public static SelectActionDialog newInstance(int position, UserAgent ua) {
        SelectActionDialog dialog = new SelectActionDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(POS, position);
        bundle.putSerializable(UA, ua);
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final UserAgent ua = (UserAgent) getArguments().getSerializable(UA);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(ua.name)
                .setItems(R.array.edit_user_agent, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (getParentFragment() instanceof OnActionSelect) {
                            ((OnActionSelect) getParentFragment())
                                    .onActionSelected(which, getArguments().getInt(POS), ua);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null);
        return builder.create();
    }

    public interface OnActionSelect {
        void onActionSelected(@ActionMode int mode, int position, UserAgent userAgent);
    }
}
