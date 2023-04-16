package com.example.memorymoblieapp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.local_data_storage.DataLocalManager;
import com.example.memorymoblieapp.local_data_storage.KeyData;
import com.example.memorymoblieapp.main.MainActivity;

import java.security.Key;
import java.util.ArrayList;

public class SettingsFragment extends Fragment {
    TextView txtAlbumBlock;
    Spinner spinnerLanguage;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch switchDarkMode;
    boolean isSpinnerInitialized = true;

    public SettingsFragment() {}

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
        languages.add(getString(R.string.choose_language));
        languages.add(getString(R.string.language_united_states));
        languages.add(getString(R.string.language_vietnamese));


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.item_text_view, languages);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(arrayAdapter);
//
        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                Toast.makeText(context, languages.get(pos), Toast.LENGTH_SHORT).show();
                if (isSpinnerInitialized) {
                    isSpinnerInitialized = false;
                    return;
                }
                if (getString(R.string.language_vietnamese) == languages.get(pos)) {
                    DataLocalManager.saveBooleanData(KeyData.LANGUAGE_CURRENT.getKey(), false);
                    Log.d("langg", "vietnam1");
                } else {
                    Log.d("langg", "english1");
                    DataLocalManager.saveBooleanData(KeyData.LANGUAGE_CURRENT.getKey(), true);
                }

                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra(KeyData.CURRENT_FRAGMENT.getKey(), R.string.settings);
                startActivity(intent);
                getActivity().recreate();
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        // Dark mode
        Boolean isThemeDark = DataLocalManager.getBooleanData(KeyData.DARK_MODE.getKey());
        isThemeDark = isThemeDark != null && isThemeDark;
        switchDarkMode.setChecked(isThemeDark);

        switchDarkMode.setOnCheckedChangeListener((compoundButton, darkModeOn) -> {
            if (darkModeOn) {
                DataLocalManager.saveBooleanData(KeyData.DARK_MODE.getKey(), true);

                Toast.makeText(context, "Bật chế độ tối", Toast.LENGTH_SHORT).show();
            } else {
                DataLocalManager.saveBooleanData(KeyData.DARK_MODE.getKey(), false);

                Toast.makeText(context, "Tắt chế độ tối", Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra(KeyData.CURRENT_FRAGMENT.getKey(), R.string.settings);
            startActivity(intent);
            getActivity().recreate();
        });

        return settingsFragment;
    }
}
