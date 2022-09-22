package com.freeislamicapps.athantime.ui.settings;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.Fragment;

import com.freeislamicapps.athantime.BuildConfig;
import com.freeislamicapps.athantime.R;
import com.freeislamicapps.athantime.alarm.AlarmStart;
import com.freeislamicapps.athantime.helper.SharedPreferencesHelper;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SettingsFragment extends Fragment implements DialogInterface.OnDismissListener {
    TextView currentLocation;
    TextView methodText, asrCalculationText, highLatsAdjustmentText, manualTunesText;

    Drawable soundOnIcon;
    Drawable soundOffIcon;
    int colorBackground;
    int colorPrimary;
    int colorOnSurface;

    boolean fajrSound;
    boolean sunriseSound;
    boolean dhuhrSound;
    boolean asrSound;
    boolean maghribSound;
    boolean ishaaSound;

    private Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);


        MaterialCardView cardViewLocation = view.findViewById(R.id.cardViewLocation);
        currentLocation = view.findViewById(R.id.currentlocation);
        methodText = view.findViewById(R.id.methodText);
        asrCalculationText = view.findViewById(R.id.asrcalculationText);
        highLatsAdjustmentText = view.findViewById(R.id.highlatadjustmentText);
        manualTunesText = view.findViewById(R.id.manualtunesText);


        MaterialCardView cardViewMethod = view.findViewById(R.id.cardViewMethod);
        cardViewMethod.setOnClickListener(view1 -> showDialogMethod());

        MaterialCardView cardViewAsrcalculations = view.findViewById(R.id.cardViewAsrcalculation);
        cardViewAsrcalculations.setOnClickListener(view12 -> showDialogAsrcalculations());

        MaterialCardView cardViewHighLatsAdjustment = view.findViewById(R.id.cardViewHighlatsadjustment);
        cardViewHighLatsAdjustment.setOnClickListener(view13 -> showDialogHighlatsadjustment());

        MaterialCardView cardViewManualtunes = view.findViewById(R.id.cardviewManualtunes);
        cardViewManualtunes.setOnClickListener(view14 -> showDialogManualtunes());

        MaterialCardView cardViewDiyanetMethod = view.findViewById(R.id.cardViewDiyanetMethod);
        cardViewDiyanetMethod.setOnClickListener(view16 -> showDialogDiyanetMethod());

        MaterialCardView cardViewNotification = view.findViewById(R.id.cardViewNotification);
        cardViewNotification.setOnClickListener(view15 -> showDialogNotification());

        currentLocation.setText(SharedPreferencesHelper.getValue(requireContext(), "location", ""));
        methodText.setText(SharedPreferencesHelper.getValue(requireContext(), "MethodText", getResources().getString(R.string.method_isna)));
        asrCalculationText.setText(SharedPreferencesHelper.getValue(requireContext(), "AsrCalculationText", getResources().getString(R.string.asrcalculation_shafi)));
        highLatsAdjustmentText.setText(SharedPreferencesHelper.getValue(requireContext(), "HighLatsAdjustmentText", getResources().getString(R.string.highlatadjustment_angle)));

        manualTunesText.setText(SharedPreferencesHelper.getValue(requireContext(), "ManualTunesText", "0,0,0,0,0,0"));

        cardViewLocation.setOnClickListener(v -> {
            if (SharedPreferencesHelper.getValue(requireContext(),"MethodText",getResources().getString(R.string.method_isna)).equals("Diyanet")) {
                showDialogDiyanetLocation();
            } else {
                LocationFragment locationFragment = new LocationFragment();
                locationFragment.show(getChildFragmentManager(), "MyFragment");
            }
        });


        return view;
    }


    private void showDialogHighlatsadjustment() {
        final Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_highlatadjustment);

        LinearLayout cardViewHighLatsNone = dialog.findViewById(R.id.cardViewHighlatsnone);
        LinearLayout cardViewHighLatsAngle = dialog.findViewById(R.id.cardViewHighlatsangle);
        LinearLayout cardViewHighLatsMiddle = dialog.findViewById(R.id.cardViewHighlatsmiddle);
        LinearLayout cardViewHighLatsSeventh = dialog.findViewById(R.id.cardViewHighlatsseventh);

        ImageButton closeButton = dialog.findViewById(R.id.closeBottomsheetButton);

        TextView titleHighLats = dialog.findViewById(R.id.title_highlatadjustment);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(titleHighLats, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);

        cardViewHighLatsNone.setOnClickListener(v -> {
            dialog.dismiss();
            highLatsAdjustmentText.setText(getResources().getString(R.string.highlatadjustment_none));
            SharedPreferencesHelper.storeValue(requireContext(), "HighLatsAdjustment", "None");
            SharedPreferencesHelper.storeValue(requireContext(), "HighLatsAdjustmentText", getResources().getString(R.string.highlatadjustment_none));
        });

        cardViewHighLatsAngle.setOnClickListener(v -> {
            dialog.dismiss();
            highLatsAdjustmentText.setText(getResources().getString(R.string.highlatadjustment_angle));
            SharedPreferencesHelper.storeValue(requireContext(), "HighLatsAdjustment", "Angle-Based");
            SharedPreferencesHelper.storeValue(requireContext(), "HighLatsAdjustmentText", getResources().getString(R.string.highlatadjustment_angle));
        });

        cardViewHighLatsMiddle.setOnClickListener(v -> {
            dialog.dismiss();
            highLatsAdjustmentText.setText(getResources().getString(R.string.highlatadjustment_middle));
            SharedPreferencesHelper.storeValue(requireContext(), "HighLatsAdjustment", "Middle of the night");
            SharedPreferencesHelper.storeValue(requireContext(), "HighLatsAdjustmentText", getResources().getString(R.string.highlatadjustment_middle));
        });

        cardViewHighLatsSeventh.setOnClickListener(v -> {
            dialog.dismiss();
            highLatsAdjustmentText.setText(getResources().getString(R.string.highlatadjustment_seventh));
            SharedPreferencesHelper.storeValue(requireContext(), "HighLatsAdjustment", "One-Seventh of the Night");
            SharedPreferencesHelper.storeValue(requireContext(), "HighLatsAdjustmentText", getResources().getString(R.string.highlatadjustment_seventh));
        });

        dialog.setOnDismissListener(dialogInterface -> setAlarm());

        closeButton.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void showDialogAsrcalculations() {
        final Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_asrcalculation);

        LinearLayout cardViewShafi = dialog.findViewById(R.id.cardViewShafi);
        LinearLayout cardViewHanafi = dialog.findViewById(R.id.cardViewHanafi);

        ImageButton closeButton = dialog.findViewById(R.id.closeBottomsheetButton);

        TextView titleAsrCalculation = dialog.findViewById(R.id.title_asrcalculation);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(titleAsrCalculation, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);

        cardViewShafi.setOnClickListener(v -> {
            dialog.dismiss();
            asrCalculationText.setText(getResources().getString(R.string.asrcalculation_shafi));
            SharedPreferencesHelper.storeValue(requireContext(), "AsrCalculation", "Shafi, Hanbali, Maliki");
            SharedPreferencesHelper.storeValue(requireContext(), "AsrCalculationText", getResources().getString(R.string.asrcalculation_shafi));
        });

        cardViewHanafi.setOnClickListener(v -> {
            dialog.dismiss();
            asrCalculationText.setText(getResources().getString(R.string.asrcalculation_hanafi));
            SharedPreferencesHelper.storeValue(requireContext(), "AsrCalculation", "Hanafi");
            SharedPreferencesHelper.storeValue(requireContext(), "AsrCalculationText", getResources().getString(R.string.asrcalculation_hanafi));
        });

        dialog.setOnDismissListener(dialogInterface -> setAlarm());

        closeButton.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void showDialogMethod() {
        final Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_method);

        LinearLayout cardviewEgypt = dialog.findViewById(R.id.cardViewEgypt);
        LinearLayout cardviewTehran = dialog.findViewById(R.id.cardViewTehran);
        LinearLayout cardViewIsna = dialog.findViewById(R.id.cardViewIsna);
        LinearLayout cardViewMwl = dialog.findViewById(R.id.cardViewMwl);
        LinearLayout cardViewMakkah = dialog.findViewById(R.id.cardViewMakkah);
        LinearLayout cardviewUoif = dialog.findViewById(R.id.cardViewUOIF);
        LinearLayout cardViewKarachi = dialog.findViewById(R.id.cardViewKarachi);
        LinearLayout cardViewDiyanet = dialog.findViewById(R.id.cardViewDiyanet);

        ImageButton closeButton = dialog.findViewById(R.id.closeBottomsheetButton);

        TextView titleMethod = dialog.findViewById(R.id.title_method);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(titleMethod, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);


        cardviewEgypt.setOnClickListener(v -> {
            dialog.dismiss();
            methodText.setText(getResources().getString(R.string.method_egypt));
            SharedPreferencesHelper.storeValue(requireContext(), "Method", "Egyptian General Authority of Survey");
            SharedPreferencesHelper.storeValue(requireContext(), "MethodText", getResources().getString(R.string.method_egypt));
        });

        cardviewTehran.setOnClickListener(v -> {
            dialog.dismiss();
            methodText.setText(getResources().getString(R.string.method_tehran));
            SharedPreferencesHelper.storeValue(requireContext(), "Method", "Institute of Geophysics, University of Tehran");
            SharedPreferencesHelper.storeValue(requireContext(), "MethodText", getResources().getString(R.string.method_tehran));
        });

        cardViewIsna.setOnClickListener(v -> {
            dialog.dismiss();
            methodText.setText(getResources().getString(R.string.method_isna));
            SharedPreferencesHelper.storeValue(requireContext(), "Method", "Islamic Society of North America (ISNA)");
            SharedPreferencesHelper.storeValue(requireContext(), "MethodText", getResources().getString(R.string.method_isna));
        });

        cardViewMwl.setOnClickListener(v -> {
            dialog.dismiss();
            methodText.setText(getResources().getString(R.string.method_mwl));
            SharedPreferencesHelper.storeValue(requireContext(), "Method", "Muslim World League (MWL)");
            SharedPreferencesHelper.storeValue(requireContext(), "MethodText", getResources().getString(R.string.method_mwl));
        });

        cardViewMakkah.setOnClickListener(v -> {
            dialog.dismiss();
            methodText.setText(getResources().getString(R.string.method_makkah));
            SharedPreferencesHelper.storeValue(requireContext(), "Method", "Umm Al-Qura University, Makkah");
            SharedPreferencesHelper.storeValue(requireContext(), "MethodText", getResources().getString(R.string.method_makkah));
        });

        cardviewUoif.setOnClickListener(v -> {
            dialog.dismiss();
            methodText.setText(getResources().getString(R.string.method_uoif));
            SharedPreferencesHelper.storeValue(requireContext(), "Method", "Union des organisations islamiques de France");
            SharedPreferencesHelper.storeValue(requireContext(), "MethodText", getResources().getString(R.string.method_uoif));
        });

        cardViewKarachi.setOnClickListener(v -> {
            dialog.dismiss();
            methodText.setText(getResources().getString(R.string.method_karachi));
            SharedPreferencesHelper.storeValue(requireContext(), "Method", "University of Islamic Sciences, Karachi");
            SharedPreferencesHelper.storeValue(requireContext(), "MethodText", getResources().getString(R.string.method_karachi));
        });

        cardViewDiyanet.setOnClickListener(v -> {
            dialog.dismiss();
            methodText.setText("Diyanet");
            SharedPreferencesHelper.storeValue(requireContext(), "Method", "Diyanet");
            SharedPreferencesHelper.storeValue(requireContext(), "MethodText", "Diyanet");
        });

        dialog.setOnDismissListener(dialogInterface -> setAlarm());

        closeButton.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    private void showDialogManualtunes() {
        final Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_manualtunes);

        TextView fajrMinute = dialog.findViewById(R.id.fajrMinute);
        TextView fajrMinuteText = dialog.findViewById(R.id.fajrMinuteText);
        ImageButton fajrAdd = dialog.findViewById(R.id.fajrAdd);
        ImageButton fajrRemove = dialog.findViewById(R.id.fajrRemove);
        TextView sunriseMinute = dialog.findViewById(R.id.sunriseMinute);
        TextView sunriseMinuteText = dialog.findViewById(R.id.sunriseMinuteText);
        ImageButton sunriseAdd = dialog.findViewById(R.id.sunriseAdd);
        ImageButton sunriseRemove = dialog.findViewById(R.id.sunriseRemove);
        TextView dhuhrMinute = dialog.findViewById(R.id.dhuhrMinute);
        TextView dhuhrMinuteText = dialog.findViewById(R.id.dhuhrMinuteText);
        ImageButton dhuhrAdd = dialog.findViewById(R.id.dhuhrAdd);
        ImageButton dhuhrRemove = dialog.findViewById(R.id.dhuhrRemove);
        TextView asrMinute = dialog.findViewById(R.id.asrMinute);
        TextView asrMinuteText = dialog.findViewById(R.id.asrMinuteText);
        ImageButton asrAdd = dialog.findViewById(R.id.asrAdd);
        ImageButton asrRemove = dialog.findViewById(R.id.asrRemove);
        TextView maghribMinute = dialog.findViewById(R.id.maghribMinute);
        TextView maghribMinuteText = dialog.findViewById(R.id.maghribMinuteText);
        ImageButton maghribAdd = dialog.findViewById(R.id.maghribAdd);
        ImageButton maghribRemove = dialog.findViewById(R.id.maghribRemove);
        TextView ishaaMinute = dialog.findViewById(R.id.ishaaMinute);
        TextView ishaaMinuteText = dialog.findViewById(R.id.ishaaMinuteText);
        ImageButton ishaaAdd = dialog.findViewById(R.id.ishaaAdd);
        ImageButton ishaaRemove = dialog.findViewById(R.id.ishaaRemove);

        TextView titleManualTunes = dialog.findViewById(R.id.title_manualtunes);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(titleManualTunes, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);


        displayManualTunes("FajrManualTunes", fajrMinuteText, fajrMinute);
        displayManualTunes("SunriseManualTunes", sunriseMinuteText, sunriseMinute);
        displayManualTunes("DhuhrManualTunes", dhuhrMinuteText, dhuhrMinute);
        displayManualTunes("AsrManualTunes", asrMinuteText, asrMinute);
        displayManualTunes("MaghribManualTunes", maghribMinuteText, maghribMinute);
        displayManualTunes("IshaaManualTunes", ishaaMinuteText, ishaaMinute);

        fajrAdd.setOnClickListener(view -> changeManualTunes("FajrManualTunes", fajrMinuteText, fajrMinute, +1));
        fajrRemove.setOnClickListener(view -> changeManualTunes("FajrManualTunes", fajrMinuteText, fajrMinute, -1));
        sunriseAdd.setOnClickListener(view -> changeManualTunes("SunriseManualTunes", sunriseMinuteText, sunriseMinute, +1));
        sunriseRemove.setOnClickListener(view -> changeManualTunes("SunriseManualTunes", sunriseMinuteText, sunriseMinute, -1));
        dhuhrAdd.setOnClickListener(view -> changeManualTunes("DhuhrManualTunes", dhuhrMinuteText, dhuhrMinute, +1));
        dhuhrRemove.setOnClickListener(view -> changeManualTunes("DhuhrManualTunes", dhuhrMinuteText, dhuhrMinute, -1));
        asrAdd.setOnClickListener(view -> changeManualTunes("AsrManualTunes", asrMinuteText, asrMinute, +1));
        asrRemove.setOnClickListener(view -> changeManualTunes("AsrManualTunes", asrMinuteText, asrMinute, -1));
        maghribAdd.setOnClickListener(view -> changeManualTunes("MaghribManualTunes", maghribMinuteText, maghribMinute, +1));
        maghribRemove.setOnClickListener(view -> changeManualTunes("MaghribManualTunes", maghribMinuteText, maghribMinute, -1));
        ishaaAdd.setOnClickListener(view -> changeManualTunes("IshaaManualTunes", ishaaMinuteText, ishaaMinute, +1));
        ishaaRemove.setOnClickListener(view -> changeManualTunes("IshaaManualTunes", ishaaMinuteText, ishaaMinute, -1));


        dialog.setOnDismissListener(dialogInterface -> {
            manualTunesText.setText(SharedPreferencesHelper.getValue(requireContext(), "ManualTunesText", "0,0,0,0,0,0"));
            setAlarm();
        });

        ImageButton closeButton = dialog.findViewById(R.id.closeBottomsheetButton);

        closeButton.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void displayManualTunes(String savedKey, TextView prayerMinuteText, TextView prayerMinute) {
        int prayerManualTunes = SharedPreferencesHelper.getValue(requireContext(), savedKey, 0);
        prayerMinute.setText(String.valueOf(prayerManualTunes));
        if (prayerManualTunes == 1 || prayerManualTunes == -1) {
            prayerMinuteText.setText(R.string.minute);
        }
    }

    private void changeManualTunes(String savedKey, TextView prayerMinuteText, TextView prayerMinute, int count) {
        int oldPrayerManualTunes = SharedPreferencesHelper.getValue(requireContext(), savedKey, 0);

        SharedPreferencesHelper.storeValue(requireContext(), savedKey, oldPrayerManualTunes + count);
        int newPrayerManualTunes = oldPrayerManualTunes + count;

        prayerMinuteText.setText(R.string.minutes);
        if (newPrayerManualTunes == 1 || newPrayerManualTunes == -1) {
            prayerMinuteText.setText(R.string.minute);
        }
        prayerMinute.setText(String.valueOf(newPrayerManualTunes));

        String fajrManualTunes = SharedPreferencesHelper.getValue(requireContext(), "FajrManualTunes", "0");
        String sunriseManualTunes = SharedPreferencesHelper.getValue(requireContext(), "SunriseManualTunes", "0");
        String dhuhrManualTunes = SharedPreferencesHelper.getValue(requireContext(), "DhuhrManualTunes", "0");
        String asrManualTunes = SharedPreferencesHelper.getValue(requireContext(), "AsrManualTunes", "0");
        String maghribManualTunes = SharedPreferencesHelper.getValue(requireContext(), "MaghribManualTunes", "0");
        String ishaaManualTunes = SharedPreferencesHelper.getValue(requireContext(), "IshaaManualTunes", "0");
        SharedPreferencesHelper.storeValue(requireContext(), "ManualTunesText",
                fajrManualTunes + "," + sunriseManualTunes + "," + dhuhrManualTunes + ","
                        + asrManualTunes + "," + maghribManualTunes + "," + ishaaManualTunes);
    }

    private void showDialogDiyanetLocation() {
        final Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_method_diyanet);

        Spinner spinnerCountry = dialog.findViewById(R.id.spinnerCountry);
        ArrayList<LocationCountry> countryListNew = new ArrayList<>();

        Spinner spinnerState = dialog.findViewById(R.id.spinnerState);

        Spinner spinnerCity = dialog.findViewById(R.id.spinnerCity);

        try {
            String str = "";
            InputStream inputStream = requireActivity().getAssets().open("diyanetcountrycity.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            str = new String(buffer);

            JSONArray jsonArray = new JSONArray(str);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject countryObject = jsonArray.getJSONObject(i);
                countryListNew.add(new LocationCountry(countryObject.getInt("UlkeID"), countryObject.getString("UlkeAdi")));
            }
            ArrayAdapter<LocationCountry> locationCountryArrayAdapter = new ArrayAdapter<LocationCountry>(requireContext(),
                    android.R.layout.simple_spinner_dropdown_item, countryListNew);
            spinnerCountry.setAdapter(locationCountryArrayAdapter);

            spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    LocationCountry locationCountry = (LocationCountry) parent.getItemAtPosition(position);
                    Toast.makeText(requireContext(), String.valueOf(locationCountry.getId()), Toast.LENGTH_SHORT).show();
                    ArrayList<LocationCountry> stateListNew = new ArrayList<>();
                    try {
                        JSONObject specificCountry = jsonArray.getJSONObject(position);
                        JSONArray specificState = specificCountry.getJSONArray("state");
                        for (int i = 0; i < specificState.length(); i++) {
                            JSONObject countryObject = specificState.getJSONObject(i);
                            System.out.println(countryObject);
                            stateListNew.add(new LocationCountry(countryObject.getInt("SehirID"), countryObject.getString("SehirAdiEn")));
                        }
                        ArrayAdapter<LocationCountry> locationCountryArrayAdapter = new ArrayAdapter<LocationCountry>(requireContext(),
                                android.R.layout.simple_spinner_dropdown_item, stateListNew);
                        spinnerState.setAdapter(locationCountryArrayAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    LocationCountry locationCountry = (LocationCountry) parent.getItemAtPosition(position);
                    Toast.makeText(requireContext(), String.valueOf(locationCountry.getId()), Toast.LENGTH_SHORT).show();

                    ArrayList<LocationCountry> cityListNew = new ArrayList<>();
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://namaz-vakti-api.herokuapp.com/regions?country=2&city=503")
                            .get()
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {

                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            try {
                                JSONArray cities = new JSONArray(response.body().string());
                                for (int i=0;i<cities.length();i++) {
                                    JSONObject specificCity = cities.getJSONObject(i);
                                    System.out.println(specificCity);
                                    cityListNew.add(new LocationCountry(specificCity.getInt("IlceID"),specificCity.getString("IlceAdiEn")));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    ArrayAdapter<LocationCountry> locationCountryArrayAdapter = new ArrayAdapter<LocationCountry>(requireContext(),
                            android.R.layout.simple_spinner_dropdown_item, cityListNew);
                    spinnerCity.setAdapter(locationCountryArrayAdapter);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    LocationCountry locationCountry = (LocationCountry) parent.getItemAtPosition(position);
                    Toast.makeText(requireContext(), String.valueOf(locationCountry.getId()), Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


        dialog.setOnDismissListener(dialogInterface -> setAlarm());

        ImageButton closeButton = dialog.findViewById(R.id.closeBottomsheetButton);
        closeButton.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void showDialogDiyanetMethod() {
        final Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_method_diyanet);

        Spinner spinnerCountry = dialog.findViewById(R.id.spinnerCountry);
        ArrayList<LocationCountry> countryListNew = new ArrayList<>();

        Spinner spinnerCity = dialog.findViewById(R.id.spinnerState);

        try {
            String str = "";
            InputStream inputStream = requireActivity().getAssets().open("diyanetcountrycity.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            str = new String(buffer);

            JSONArray jsonArray = new JSONArray(str);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject countryObject = jsonArray.getJSONObject(i);
                countryListNew.add(new LocationCountry(countryObject.getInt("UlkeID"), countryObject.getString("UlkeAdi")));
            }
            ArrayAdapter<LocationCountry> locationCountryArrayAdapter = new ArrayAdapter<LocationCountry>(requireContext(),
                    android.R.layout.simple_spinner_dropdown_item, countryListNew);
            spinnerCountry.setAdapter(locationCountryArrayAdapter);

            spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    LocationCountry locationCountry = (LocationCountry) parent.getItemAtPosition(position);
                    Toast.makeText(requireContext(), String.valueOf(locationCountry.getId()), Toast.LENGTH_SHORT).show();
                    ArrayList<LocationCountry> cityListNew = new ArrayList<>();
                    try {
                        JSONObject specificCountry = jsonArray.getJSONObject(position);
                        JSONArray specificState = specificCountry.getJSONArray("state");
                        for (int i = 0; i < specificState.length(); i++) {
                            JSONObject countryObject = specificState.getJSONObject(i);
                            System.out.println(countryObject);
                            cityListNew.add(new LocationCountry(countryObject.getInt("SehirID"), countryObject.getString("SehirAdiEn")));
                        }
                        ArrayAdapter<LocationCountry> locationCountryArrayAdapter = new ArrayAdapter<LocationCountry>(requireContext(),
                                android.R.layout.simple_spinner_dropdown_item, cityListNew);
                        spinnerCity.setAdapter(locationCountryArrayAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    LocationCountry locationCountry = (LocationCountry) parent.getItemAtPosition(position);
                    Toast.makeText(requireContext(), String.valueOf(locationCountry.getId()), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


        dialog.setOnDismissListener(dialogInterface -> setAlarm());

        ImageButton closeButton = dialog.findViewById(R.id.closeBottomsheetButton);
        closeButton.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void initDiyanetCountries(ArrayList<String> countryList) {
        countryList.add("ABD");
        countryList.add("AFGANISTAN");
        countryList.add("ALMANYA");
        countryList.add("ANDORRA");
        countryList.add("ANGOLA");
        countryList.add("ANGUILLA");
        countryList.add("ANTIGUA VE BARBUDA");
        countryList.add("ARJANTIN");
        countryList.add("ARNAVUTLUK");
        countryList.add("ARUBA");
        countryList.add("AVUSTRALYA");
        countryList.add("AVUSTURYA");
        countryList.add("AZERBAYCAN");
        countryList.add("BAHAMALAR");
        countryList.add("BAHREYN");
        countryList.add("BANGLADES");
        countryList.add("BARBADOS");
        countryList.add("BELARUS");
        countryList.add("BELCIKA");
        countryList.add("BELIZE");
        countryList.add("BENIN");
        countryList.add("BERMUDA");
        countryList.add("BIRLESIK ARAP EMIRLIGI");
        countryList.add("BOLIVYA");
        countryList.add("BOSNA HERSEK");
        countryList.add("BOTSVANA");
        countryList.add("BREZILYA");
        countryList.add("BRUNEI");
        countryList.add("BULGARISTAN");
        countryList.add("BURKINA FASO");
        countryList.add("BURMA (MYANMAR)");
        countryList.add("BURUNDI");
        countryList.add("BUTAN");
        countryList.add("CAD");
        countryList.add("CECENISTAN");
        countryList.add("CEK CUMHURIYETI");
        countryList.add("CEZAYIR");
        countryList.add("CIBUTI");
        countryList.add("CIN");
        countryList.add("DANIMARKA");
        countryList.add("DEMOKRATIK KONGO CUMHURIYETI");
        countryList.add("DOGU TIMOR");
        countryList.add("DOMINIK");
        countryList.add("DOMINIK CUMHURIYETI");
        countryList.add("EKVATOR");
        countryList.add("EKVATOR GINESI");
        countryList.add("EL SALVADOR");
        countryList.add("ENDONEZYA");
        countryList.add("ERITRE");
        countryList.add("ERMENISTAN");
        countryList.add("ESTONYA");
        countryList.add("ETYOPYA");
        countryList.add("FAS");
        countryList.add("FIJI");
        countryList.add("FILDISI SAHILI");
        countryList.add("FILIPINLER");
        countryList.add("FILISTIN");
        countryList.add("FINLANDIYA");
        countryList.add("FRANSA");
        countryList.add("GABON");
        countryList.add("GAMBIYA");
        countryList.add("GANA");
        countryList.add("GINE");
        countryList.add("GRENADA");
        countryList.add("GRONLAND");
        countryList.add("GUADELOPE");
        countryList.add("GUAM ADASI");
        countryList.add("GUATEMALA");
        countryList.add("GUNEY AFRIKA");
        countryList.add("GUNEY KORE");
        countryList.add("GURCISTAN");
        countryList.add("GUYANA");
        countryList.add("HAITI");
        countryList.add("HINDISTAN");
        countryList.add("HIRVATISTAN");
        countryList.add("HOLLANDA");
        countryList.add("HOLLANDA ANTILLERI");
        countryList.add("HONDURAS");
        countryList.add("HONG KONG");
        countryList.add("INGILTERE");
        countryList.add("IRAK");
        countryList.add("IRAN");
        countryList.add("IRLANDA");
        countryList.add("ISPANYA");
        countryList.add("ISRAIL");
        countryList.add("ISVEC");
        countryList.add("ISVICRE");
        countryList.add("ITALYA");
        countryList.add("IZLANDA");
        countryList.add("JAMAIKA");
        countryList.add("JAPONYA");
        countryList.add("KAMBOCYA");
        countryList.add("KAMERUN");
        countryList.add("KANADA");
        countryList.add("KARADAG");
        countryList.add("KATAR");
        countryList.add("KAZAKISTAN");
        countryList.add("KENYA");
        countryList.add("KIRGIZISTAN");
        countryList.add("KOLOMBIYA");
        countryList.add("KOMORLAR");
        countryList.add("KOSOVA");
        countryList.add("KOSTARIKA");
        countryList.add("KUBA");
        countryList.add("KUDUS");
        countryList.add("KUVEYT");
        countryList.add("KUZEY KIBRIS");
        countryList.add("KUZEY KORE");
        countryList.add("LAOS");
        countryList.add("LESOTO");
        countryList.add("LETONYA");
        countryList.add("LIBERYA");
        countryList.add("LIBYA");
        countryList.add("LIECHTENSTEIN");
        countryList.add("LITVANYA");
        countryList.add("LUBNAN");
        countryList.add("LUKSEMBURG");
        countryList.add("MACARISTAN");
        countryList.add("MADAGASKAR");
        countryList.add("MAKAO");
        countryList.add("MAKEDONYA");
        countryList.add("MALAVI");
        countryList.add("MALDIVLER");
        countryList.add("MALEZYA");
        countryList.add("MALI");
        countryList.add("MALTA");
        countryList.add("MARTINIK");
        countryList.add("MAURITIUS ADASI");
        countryList.add("MAYOTTE");
        countryList.add("MEKSIKA");
        countryList.add("MIKRONEZYA");
        countryList.add("MISIR");
        countryList.add("MOGOLISTAN");
        countryList.add("MOLDAVYA");
        countryList.add("MONAKO");
        countryList.add("MONTSERRAT (U.K.)");
        countryList.add("MORITANYA");
        countryList.add("MOZAMBIK");
        countryList.add("NAMIBYA");
        countryList.add("NEPAL");
        countryList.add("NIJER");
        countryList.add("NIJERYA");
        countryList.add("NIKARAGUA");
        countryList.add("NIUE");
        countryList.add("NORVEC");
        countryList.add("ORTA AFRIKA CUMHURIYETI");
        countryList.add("OZBEKISTAN");
        countryList.add("PAKISTAN");
        countryList.add("PALAU");
        countryList.add("PANAMA");
        countryList.add("PAPUA YENI GINE");
        countryList.add("PARAGUAY");
        countryList.add("PERU");
        countryList.add("PITCAIRN ADASI");
        countryList.add("POLONYA");
        countryList.add("PORTEKIZ");
        countryList.add("PORTO RIKO");
        countryList.add("REUNION");
        countryList.add("ROMANYA");
        countryList.add("RUANDA");
        countryList.add("RUSYA");
        countryList.add("S. ARABISTAN");
        countryList.add("SAMOA");
        countryList.add("SENEGAL");
        countryList.add("SEYSEL ADALARI");
        countryList.add("SILI");
        countryList.add("SINGAPUR");
        countryList.add("SIRBISTAN");
        countryList.add("SLOVAKYA");
        countryList.add("SLOVENYA");
        countryList.add("SOMALI");
        countryList.add("SRI LANKA");
        countryList.add("SUDAN");
        countryList.add("SURINAM");
        countryList.add("SURIYE");
        countryList.add("SVALBARD");
        countryList.add("SVAZILAND");
        countryList.add("TACIKISTAN");
        countryList.add("TANZANYA");
        countryList.add("TAYLAND");
        countryList.add("TAYVAN");
        countryList.add("TOGO");
        countryList.add("TONGA");
        countryList.add("TRINIDAT VE TOBAGO");
        countryList.add("TUNUS");
        countryList.add("TURKMENISTAN");
        countryList.add("TÜRKİYE");
        countryList.add("UGANDA");
        countryList.add("UKRAYNA");
        countryList.add("UKRAYNA-KIRIM");
        countryList.add("UMMAN");
        countryList.add("URDUN");
        countryList.add("URUGUAY");
        countryList.add("VANUATU");
        countryList.add("VATIKAN");
        countryList.add("VENEZUELA");
        countryList.add("VIETNAM");
        countryList.add("YEMEN");
        countryList.add("YENI KALEDONYA");
        countryList.add("YENI ZELLANDA");
        countryList.add("YESIL BURUN");
        countryList.add("YUNANISTAN");
        countryList.add("ZAMBIYA");
        countryList.add("ZIMBABVE");
    }

    private void showDialogNotification() {
        final Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_notification);

        RelativeLayout relativeLayoutFajr = dialog.findViewById(R.id.relativeLayoutFajr);
        RelativeLayout relativeLayoutSunrise = dialog.findViewById(R.id.relativeLayoutSunrise);
        RelativeLayout relativeLayoutDhuhr = dialog.findViewById(R.id.relativeLayoutDhuhr);
        RelativeLayout relativeLayoutAsr = dialog.findViewById(R.id.relativeLayoutAsr);
        RelativeLayout relativeLayoutMaghrib = dialog.findViewById(R.id.relativeLayoutMaghrib);
        RelativeLayout relativeLayoutIshaa = dialog.findViewById(R.id.relativeLayoutIshaa);

        ImageView fajrSoundIcon = dialog.findViewById(R.id.fajrSoundIcon);
        ImageView sunriseSoundIcon = dialog.findViewById(R.id.sunriseSoundIcon);
        ImageView dhuhrSoundIcon = dialog.findViewById(R.id.dhuhrSoundIcon);
        ImageView asrSoundIcon = dialog.findViewById(R.id.asrSoundIcon);
        ImageView maghribSoundIcon = dialog.findViewById(R.id.maghribSoundIcon);
        ImageView ishaaSoundIcon = dialog.findViewById(R.id.ishaaSoundIcon);

        TextView fajrSoundText = dialog.findViewById(R.id.fajrSoundText);
        TextView sunriseSoundText = dialog.findViewById(R.id.sunriseSoundText);
        TextView dhuhrSoundText = dialog.findViewById(R.id.dhuhrSoundText);
        TextView asrSoundText = dialog.findViewById(R.id.asrSoundText);
        TextView maghribSoundText = dialog.findViewById(R.id.maghribSoundText);
        TextView ishaaSoundText = dialog.findViewById(R.id.ishaaSoundText);

        TextView titleNotification = dialog.findViewById(R.id.title_notification);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(titleNotification, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);

        fajrSound = SharedPreferencesHelper.getValue(requireContext(), "Fajr_Sound", true);
        sunriseSound = SharedPreferencesHelper.getValue(requireContext(), "Sunrise_Sound", false);
        dhuhrSound = SharedPreferencesHelper.getValue(requireContext(), "Dhuhr_Sound", true);
        asrSound = SharedPreferencesHelper.getValue(requireContext(), "Asr_Sound", true);
        maghribSound = SharedPreferencesHelper.getValue(requireContext(), "Maghrib_Sound", true);
        ishaaSound = SharedPreferencesHelper.getValue(requireContext(), "Ishaa_Sound", true);

        soundOnIcon = AppCompatResources.getDrawable(requireContext(), R.drawable.symbol_notification_selected);
        soundOffIcon = AppCompatResources.getDrawable(requireContext(), R.drawable.symbol_volumeoff);
        TypedValue typedValueBackground = new TypedValue();
        requireActivity().getTheme().resolveAttribute(com.google.android.material.R.attr.backgroundColor, typedValueBackground, true);
        colorBackground = typedValueBackground.data;

        TypedValue typedValuePrimary = new TypedValue();
        requireActivity().getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValuePrimary, true);
        colorPrimary = typedValuePrimary.data;

        TypedValue typedValueColorOnSurface = new TypedValue();
        requireActivity().getTheme().resolveAttribute(com.google.android.material.R.attr.colorOnSurface, typedValueColorOnSurface, true);
        colorOnSurface = typedValueColorOnSurface.data;

        initNotification(fajrSound, fajrSoundIcon, relativeLayoutFajr, fajrSoundText);
        initNotification(sunriseSound, sunriseSoundIcon, relativeLayoutSunrise, sunriseSoundText);
        initNotification(dhuhrSound, dhuhrSoundIcon, relativeLayoutDhuhr, dhuhrSoundText);
        initNotification(asrSound, asrSoundIcon, relativeLayoutAsr, asrSoundText);
        initNotification(maghribSound, maghribSoundIcon, relativeLayoutMaghrib, maghribSoundText);
        initNotification(ishaaSound, ishaaSoundIcon, relativeLayoutIshaa, ishaaSoundText);


        relativeLayoutFajr.setOnClickListener(view -> {
            notificationClick("Fajr_Sound", fajrSound, fajrSoundIcon, relativeLayoutFajr, fajrSoundText);
            fajrSound = !fajrSound;
        });
        relativeLayoutSunrise.setOnClickListener(view -> {
            notificationClick("Sunrise_Sound", sunriseSound, sunriseSoundIcon, relativeLayoutSunrise, sunriseSoundText);
            sunriseSound = !sunriseSound;
        });
        relativeLayoutDhuhr.setOnClickListener(view -> {
            notificationClick("Dhuhr_Sound", dhuhrSound, dhuhrSoundIcon, relativeLayoutDhuhr, dhuhrSoundText);
            dhuhrSound = !dhuhrSound;
        });
        relativeLayoutAsr.setOnClickListener(view -> {
            notificationClick("Asr_Sound", asrSound, asrSoundIcon, relativeLayoutAsr, asrSoundText);
            asrSound = !asrSound;
        });
        relativeLayoutMaghrib.setOnClickListener(view -> {
            notificationClick("Maghrib_Sound", maghribSound, maghribSoundIcon, relativeLayoutMaghrib, maghribSoundText);
            maghribSound = !maghribSound;
        });
        relativeLayoutIshaa.setOnClickListener(view -> {
            notificationClick("Ishaa_Sound", ishaaSound, ishaaSoundIcon, relativeLayoutIshaa, ishaaSoundText);
            ishaaSound = !ishaaSound;
        });

        dialog.setOnDismissListener(dialogInterface -> setAlarm());

        ImageButton closeButton = dialog.findViewById(R.id.closeBottomsheetButton);
        closeButton.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void initNotification(Boolean prayerSound, ImageView icon, RelativeLayout relativeLayout, TextView textView) {
        if (prayerSound) {
            icon.setImageDrawable(soundOnIcon);
            relativeLayout.setBackgroundColor(colorPrimary);
            textView.setTextColor(getResources().getColor(R.color.white));
        } else {
            icon.setImageDrawable(soundOffIcon);
            relativeLayout.setBackgroundColor(colorBackground);
            textView.setTextColor(colorOnSurface);
        }
    }

    private void notificationClick(String savedKey, Boolean prayerSound, ImageView icon, RelativeLayout relativeLayout, TextView textView) {
        SharedPreferencesHelper.storeValue(requireContext(), savedKey, !prayerSound);
        if (prayerSound) {
            icon.setImageDrawable(soundOffIcon);
            relativeLayout.setBackgroundColor(colorBackground);
            textView.setTextColor(colorOnSurface);
        } else {
            icon.setImageDrawable(soundOnIcon);
            relativeLayout.setBackgroundColor(colorPrimary);
            textView.setTextColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        currentLocation.setText(SharedPreferencesHelper.getValue(requireContext(), "location", ""));
        setAlarm();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void setAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 1);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(mContext, AlarmStart.class);

        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getBroadcast(mContext, 20, intent, PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getBroadcast(mContext, 20, intent, 0);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

    }


}