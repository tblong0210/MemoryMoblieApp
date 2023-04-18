package com.example.memorymoblieapp.more;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.fragment_album.AlbumBlockFragment;
import com.example.memorymoblieapp.local_data_storage.DataLocalManager;
import com.example.memorymoblieapp.local_data_storage.KeyData;
import com.example.memorymoblieapp.activity.MainActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SettingsFragment extends Fragment {
    TextView txtAlbumBlock;
    Spinner spinnerLanguage;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch switchDarkMode;
    private String currentLaguage;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View settingsFragment = inflater.inflate(R.layout.settings_fragment, container, false);
        Context context = settingsFragment.getContext();
        txtAlbumBlock = settingsFragment.findViewById(R.id.txtAlbumBlock);
        spinnerLanguage = settingsFragment.findViewById(R.id.spinnerLanguage);
        switchDarkMode = settingsFragment.findViewById(R.id.switchDarkMode);

        // Album block
        txtAlbumBlock.setOnClickListener(view -> {
            AlbumBlockFragment albumBlockFragment = new AlbumBlockFragment();
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout_content, albumBlockFragment).commit();
            fragmentTransaction.addToBackStack("more");
        });

        // Language
        ArrayList<String> languages = new ArrayList<>();
//        languages.add(getString(R.string.choose_language));
        languages.add(getString(R.string.language_vietnamese));
        languages.add(getString(R.string.language_united_states));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.item_text_view, languages);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(arrayAdapter);

        if(DataLocalManager.getBooleanData(KeyData.LANGUAGE_CURRENT.getKey())){
            currentLaguage = getString(R.string.language_united_states);
            spinnerLanguage.setSelection(1);
        }
        else{
            currentLaguage = getString(R.string.language_vietnamese);
            spinnerLanguage.setSelection(0);
        }
//
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // Xử lý công việc cần thực hiện sau khoảng thời gian
                spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                        Log.d("language", "onItemSelected: " + currentLaguage);
                        if (currentLaguage != null && currentLaguage.equals(languages.get(pos)))
                            return;
                        if (getString(R.string.language_vietnamese).equals(languages.get(pos))) {
                            DataLocalManager.saveBooleanData(KeyData.LANGUAGE_CURRENT.getKey(), false);
                            currentLaguage = getString(R.string.language_vietnamese);
                            Log.d("langg", "vietnam1");
                        } else {
                            Log.d("langg", "english1");
                            DataLocalManager.saveBooleanData(KeyData.LANGUAGE_CURRENT.getKey(), true);
                            currentLaguage = getString(R.string.language_united_states);
                        }

                        arrayAdapter.notifyDataSetChanged();

                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra(KeyData.CURRENT_FRAGMENT.getKey(), R.string.settings);
                        startActivity(intent);
                        getActivity().recreate();

                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        }, 1000); // Đặt thời gian tạm dừng là 2000ms (2 giây)

        // Dark mode
        Boolean isThemeDark = DataLocalManager.getBooleanData(KeyData.DARK_MODE.getKey());
        isThemeDark = isThemeDark != null && isThemeDark;
        switchDarkMode.setChecked(isThemeDark);

        switchDarkMode.setOnCheckedChangeListener((compoundButton, darkModeOn) -> {
            if (darkModeOn) {
                DataLocalManager.saveBooleanData(KeyData.DARK_MODE.getKey(), true);
            } else {
                DataLocalManager.saveBooleanData(KeyData.DARK_MODE.getKey(), false);
            }

            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra(KeyData.CURRENT_FRAGMENT.getKey(), R.string.settings);
            startActivity(intent);
            getActivity().recreate();
        });

        return settingsFragment;
    }
}
