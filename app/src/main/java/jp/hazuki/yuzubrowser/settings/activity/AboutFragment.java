package jp.hazuki.yuzubrowser.settings.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.File;

import jp.hazuki.yuzubrowser.BuildConfig;
import jp.hazuki.yuzubrowser.R;
import jp.hazuki.yuzubrowser.settings.data.AppData;
import jp.hazuki.yuzubrowser.utils.FileUtils;

/**
 * Created by hazuki on 17/01/16.
 */

public class AboutFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(AppData.PREFERENCE_NAME);
        addPreferencesFromResource(R.xml.pref_about);
        findPreference("version").setSummary(BuildConfig.VERSION_NAME);
        findPreference("build").setSummary(BuildConfig.GIT_HASH);
        findPreference("build_time").setSummary(BuildConfig.BUILD_TIME);

        findPreference("osl").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new OpenSourceLicenseDialog().show(getChildFragmentManager(), "osl");
                return true;
            }
        });

        findPreference("delete_log").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new DeleteLogDialog().show(getChildFragmentManager(), "delete");
                return true;
            }
        });
    }

    public static class OpenSourceLicenseDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            WebView webView = new WebView(getActivity());
            webView.loadUrl("file:///android_asset/licenses.html");
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.open_source_license)
                    .setView(webView);
            return builder.create();
        }
    }

    public static class DeleteLogDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.pref_delete_all_logs)
                    .setMessage(R.string.pref_delete_log_mes)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            File file = new File(getActivity().getExternalFilesDir(null), "./error_log/");
                            if (!file.exists()) {
                                Toast.makeText(getActivity(), R.string.succeed, Toast.LENGTH_SHORT).show();
                            } else if (FileUtils.deleteFile(file)) {
                                Toast.makeText(getActivity(), R.string.succeed, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), R.string.failed, Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.no, null);
            return builder.create();
        }
    }
}
