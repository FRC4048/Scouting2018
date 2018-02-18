package com.example.dan.matchscoutingmain2018;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AbstractForm {

    private Record present;
    private Record can_climb;
    private Record comments;
    private Record rate_driving;
    private Record auto_cross_baseline;
    private Record climb_success;
    private Record stays_put_when_power_out;
    private Record did_they_break_down;
    private Record auto_cube_in_vault;
    private Record auto_cube_in_ally_switch;
    private Record auto_cube_in_scale;
    private Record auto_cube_in_opponent_switch;
    private Record help_others_climb;
    private Record play_defense;
    private Record rate_defense;
    private Record cube_dropped;

    private Record[] records = new Record[16];

    private ArrayList<Record> cube_in_vault = new ArrayList<>();
    private ArrayList<Record> cube_in_ally_switch = new ArrayList<>();
    private ArrayList<Record> cube_in_scale = new ArrayList<>();
    private ArrayList<Record> cube_in_opponent_switch = new ArrayList<>();


    private CheckBox chkAutoBaseline;
    private CheckBox chkAutoCubeVault;
    private CheckBox chkAutoCubeAllySwitch;
    private CheckBox chkAutoCubeScale;
    private CheckBox chkAutoCubeOpponentSwitch;
    private CheckBox chkClimbAttempt;
    private CheckBox chkClimbSuccess;
    private CheckBox chkClimbPower;
    private CheckBox chkClimbHelp;
    private CheckBox chkMiscBreak;
    private CheckBox chkMiscDefense;

    private AutoCompleteTextView txtTeamNumber;
    private AutoCompleteTextView txtMatchNumber;
    private EditText editComments;

    private Spinner chooseName;
    private Spinner spnRateDriver;
    private Spinner spnRateDefense;

    private Button btnToggleButton;                 //check this bad boy out
    private Button btnPlus;
    private Button btnstartTimer;
    private Button btnMinus;
    private Button btnTeleopCubeVault;
    private Button btnTeleopCubeAllySwitch;
    private Button btnTeleopCubeScale;
    private Button btnTeleopCubeOpponentSwitch;
    private Button btnTeleopCubeDrop;

    private EditText txtTeleopCount1;
    private EditText txtTeleopCount2;
    private EditText txtTeleopCount3;
    private EditText txtTeleopCount4;
    private EditText txtTeleopCount5;


    private long startTime = 0;
    private Handler timerHandler = new Handler();
    volatile long timeInMilliseconds = 0;
    long timeSwapBuff = 0;
    long updatedTime = 0;
    long adjustment = 0;

    private Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime + adjustment;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            updateTimerText();
            if (timeInMilliseconds < 150000) timerHandler.postDelayed(this, 1000);
        }
    };

    private void updateTimerText()
    {
        int secs = (int) (updatedTime/1000);
        int mins = secs/60;
        secs = secs % 60;
        btnstartTimer.setText("" + mins + ":" + String.format("%02d", secs));
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initRecords();
        if (checkConfigFile()) {
            initConfigs();
        } else {
            actionRequested = Action.RECEIVE_CONFIG;
            showAlertDialog("A configuration file from the master computer is required to continue."
                    + "\nPlease transfer the file to this machine.", "I've transferred the file");
        } // End if
        initLayout();
        initSaveState();
        initArchiveSystem();
    }

    @Override
    void initConfigs() {

    }

    @Override
    void initRecords() {
        //fill in array
        records[0] = present = new Record(null, MatchForm.Items.PRESENT.getId());
        records[1] = can_climb = new Record(null, MatchForm.Items.CAN_CLIMB.getId());
        records[2] = comments = new Record(null, MatchForm.Items.COMMENTS.getId());
        records[3] = rate_driving = new Record(null, MatchForm.Items.RATE_DRIVING.getId());
        records[4] = auto_cross_baseline = new Record(null, MatchForm.Items.AUTO_CROSS_BASELINE.getId());
        records[5] = climb_success = new Record(null, MatchForm.Items.COMMENTS.getId());
        records[6] = stays_put_when_power_out  = new Record(null, MatchForm.Items.STAYS_PUT_WHEN_POWER_CUT.getId());
        records[7] = did_they_break_down = new Record(null, MatchForm.Items.DID_THEY_BREAK_DOWN.getId());
        records[8] = auto_cube_in_vault = new Record(null, MatchForm.Items.AUTO_CUBE_IN_VAULT.getId());
        records[9] = auto_cube_in_ally_switch = new Record(null, MatchForm.Items.AUTO_CUBE_IN_ALLY_SWITCH.getId());
        records[10] = auto_cube_in_scale = new Record(null, MatchForm.Items.AUTO_CUBE_IN_SCALE.getId());
        records[11] = auto_cube_in_opponent_switch = new Record(null, MatchForm.Items.AUTO_CUBE_IN_OPPONENT_SWITCH.getId());
        records[12] = help_others_climb = new Record(null, MatchForm.Items.HELP_OTHERS_CLIMB.getId());
        records[13] = play_defense = new Record(null, MatchForm.Items.PLAY_DEFENSE.getId());
        records[14] = rate_defense = new Record(null, MatchForm.Items.RATE_DEFENSE.getId());
        records[15] = cube_dropped = new Record(null, MatchForm.Items.RATE_DEFENSE.getId());

    }

    @Override
    void initLayout() {
        //create objects for all interactables
        CheckboxListener checkbox_a = new CheckboxListener();

        chkAutoBaseline = (CheckBox) findViewById(R.id.chkAutoBaseline);
        chkAutoBaseline.setOnCheckedChangeListener(checkbox_a);

        chkAutoCubeVault = (CheckBox) findViewById(R.id.chkAutoCubeVault);
        chkAutoCubeVault.setOnCheckedChangeListener(checkbox_a);

        chkAutoCubeAllySwitch = (CheckBox) findViewById(R.id.chkAutoCubeAllySwitch);
        chkAutoCubeAllySwitch.setOnCheckedChangeListener(checkbox_a);

        chkAutoCubeScale = (CheckBox) findViewById(R.id.chkAutoCubeScale);
        chkAutoCubeScale.setOnCheckedChangeListener(checkbox_a);

        chkAutoCubeOpponentSwitch = (CheckBox) findViewById(R.id.chkAutoCubeOpponentSwitch);
        chkAutoCubeOpponentSwitch.setOnCheckedChangeListener(checkbox_a);

        chkClimbAttempt = (CheckBox) findViewById(R.id.chkClimbAttempt);
        chkClimbAttempt.setOnCheckedChangeListener(checkbox_a);

        chkClimbSuccess = (CheckBox) findViewById(R.id.chkClimbSuccess);
        chkClimbSuccess.setOnCheckedChangeListener(checkbox_a);

        chkClimbPower = (CheckBox) findViewById(R.id.chkClimbPower);
        chkClimbPower.setOnCheckedChangeListener(checkbox_a);

        chkClimbHelp = (CheckBox) findViewById(R.id.chkClimbHelp);
        chkClimbHelp.setOnCheckedChangeListener(checkbox_a);

        chkMiscBreak = (CheckBox) findViewById(R.id.chkMiscBreak);
        chkMiscBreak.setOnCheckedChangeListener(checkbox_a);

        chkMiscDefense = (CheckBox) findViewById(R.id.chkMiscDefense);
        chkMiscDefense.setOnCheckedChangeListener(checkbox_a);



        ButtonListenerTeleop teleop_button_listener = new ButtonListenerTeleop();

        btnTeleopCubeVault = (Button) findViewById(R.id.btnTeleopCubeVault);
        btnTeleopCubeVault.setOnClickListener(teleop_button_listener);

        btnTeleopCubeAllySwitch = (Button) findViewById(R.id.btnTeleopCubeAllySwitch);
        btnTeleopCubeAllySwitch.setOnClickListener(teleop_button_listener);

        btnTeleopCubeScale = (Button) findViewById(R.id.btnTeleopCubeScale);
        btnTeleopCubeScale.setOnClickListener(teleop_button_listener);

        btnTeleopCubeOpponentSwitch = (Button) findViewById(R.id.btnTeleopCubeOpponentSwitch);
        btnTeleopCubeOpponentSwitch.setOnClickListener(teleop_button_listener);

        btnTeleopCubeDrop = (Button) findViewById(R.id.btnTeleopCubeDrop);
        btnTeleopCubeDrop.setOnClickListener(teleop_button_listener);

        txtTeleopCount1 = (EditText) findViewById(R.id.txtTeleopCount1);
        txtTeleopCount2 = (EditText) findViewById(R.id.txtTeleopCount2);
        txtTeleopCount3 = (EditText) findViewById(R.id.txtTeleopCount3);
        txtTeleopCount4 = (EditText) findViewById(R.id.txtTeleopCount4);
        txtTeleopCount5 = (EditText) findViewById(R.id.txtTeleopCount5);




        //ButtonListener other_button_listener = new ButtonLister();


        MasterButtonListener start_timer = new MasterButtonListener();

        btnstartTimer = (Button) findViewById(R.id.btnstartTimer);
        btnstartTimer.setOnClickListener(start_timer);

        MinusButtonListener minus_button = new MinusButtonListener();

        btnMinus = (Button) findViewById(R.id.btnMinus);
        btnMinus.setOnClickListener(minus_button);

        PlusButtonListener plus_button = new PlusButtonListener();

        btnPlus = (Button) findViewById(R.id.btnPlus);
        btnPlus.setOnClickListener(plus_button);


        ButtonListenerTeleop main_buttons = new ButtonListenerTeleop();

        btnTeleopCubeVault = (Button) findViewById(R.id.btnTeleopCubeVault);
        btnTeleopCubeVault.setOnClickListener(main_buttons);

        btnTeleopCubeAllySwitch = (Button) findViewById(R.id.btnTeleopCubeAllySwitch);
        btnTeleopCubeAllySwitch.setOnClickListener(main_buttons);

        btnTeleopCubeScale = (Button) findViewById(R.id.btnTeleopCubeScale);
        btnTeleopCubeScale.setOnClickListener(main_buttons);

        btnTeleopCubeOpponentSwitch = (Button) findViewById(R.id.btnTeleopCubeOpponentSwitch);
        btnTeleopCubeOpponentSwitch.setOnClickListener(main_buttons);

        btnTeleopCubeDrop = (Button) findViewById(R.id.btnTeleopCubeDrop);
        btnTeleopCubeDrop.setOnClickListener(main_buttons);

    }

    @Override
    void initSaveState() {

    }

    @Override
    void executeRequest() {

    }

    @Override
    void saveState() {

    }

    @Override
    void resetForm() {

    }

    @Override
    void resetRadioGroups() {

    }

    @Override
    void resetCheckboxes() {

    }

    @Override
    Form makeForm() {
        return null;
    }

    @Override
    void readyToSave() {

    }

    @Override
    void setState(String[] records, int startingIndex) {

    }

    private class CheckboxListener implements CheckBox.OnCheckedChangeListener {


        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            String value = "1";
            if (!isChecked) value = "0";
            int a = compoundButton.getId();
            switch(a){
                case R.id.chkAutoBaseline: auto_cross_baseline.setValue(value); break;
                case R.id.chkAutoCubeVault: auto_cube_in_vault.setValue(value); break;
                case R.id.chkAutoCubeAllySwitch: auto_cube_in_ally_switch.setValue(value); break;
                case R.id.chkAutoCubeScale: auto_cube_in_scale.setValue(value); break;
                case R.id.chkAutoCubeOpponentSwitch: auto_cube_in_opponent_switch.setValue(value); break;
                case R.id.chkClimbAttempt: can_climb.setValue(value); break;
                case R.id.chkClimbSuccess: climb_success.setValue(value); break;
                case R.id.chkClimbPower: stays_put_when_power_out.setValue(value); break;
                case R.id.chkClimbHelp: help_others_climb.setValue(value); break;
                case R.id.chkMiscBreak: did_they_break_down.setValue(value); break;
                case R.id.chkMiscDefense: play_defense.setValue(value); break;
                case R.id.chkShow: present.setValue(value); break;

            }

        }
        //create listener for all types of shits
    }
    private class ButtonListener implements View.OnClickListener {


        @Override
        public void onClick(View view) {



            int change = 0;
            int a = view.getId();

            switch(a) {
                case R.id.btnPlus: change++; break;
                case R.id.btnMinus: change--; break;
            }
        }

    }
    private class ButtonListenerTeleop implements View.OnClickListener {

        public void onClick(View view) {


            int a = view.getId();
            switch(a) {
                case R.id.btnTeleopCubeVault:           changeValue(txtTeleopCount1); timestamp(MatchForm.Items.CUBE_IN_VAULT.getId(), cube_in_vault); break;
                case R.id.btnTeleopCubeAllySwitch:      changeValue(txtTeleopCount2); timestamp(MatchForm.Items.CUBE_IN_ALLY_SWITCH.getId(), cube_in_ally_switch); break;
                case R.id.btnTeleopCubeScale:           changeValue(txtTeleopCount3); timestamp(MatchForm.Items.CUBE_IN_SCALE.getId(), cube_in_scale); break;
                case R.id.btnTeleopCubeOpponentSwitch:  changeValue(txtTeleopCount4); timestamp(MatchForm.Items.CUBE_IN_OPPONENT_SWITCH.getId(), cube_in_opponent_switch); break;
                case R.id.btnTeleopCubeDrop:            changeValue(txtTeleopCount5); break;
            }


        }
        private void changeValue(EditText text) {
            if(text.getText().toString().equals(""))
                text.setText("1");
            else if(text.getId() == R.id.txtTeleopCount5){ //for TeleopCubeDrop since it doesnt need timestamp and therefore doesnt need an array of them, just 1 item per form
                int a = Integer.parseInt(cube_dropped.getValue());
                cube_dropped.setValue(String.valueOf(a+1));
            }
            else {
                String str = text.getText().toString();
                int num = 1;
                if (!str.isEmpty()) num = Integer.parseInt(str) + 1;
                text.setText(String.valueOf(num));
            }
        }
        private void timestamp(int id, ArrayList<Record> name) {
            name.add(new Record(String.valueOf(timeInMilliseconds), id));
        }
    }

    private class MasterButtonListener implements View.OnClickListener {
        public void onClick(View view) {
            boolean timerstarted = false;
            if(!timerstarted){  //unfinished check so  that button isnt pressed again causing reset
                System.out.println("Start timer button pressed.");
                startTime = SystemClock.uptimeMillis();
                adjustment = 0;
                timerHandler.postDelayed(updateTimerThread, 0);
            }


        }
    }

    private class MinusButtonListener implements View.OnClickListener {

        public void onClick(View view) {
            System.out.println("Decreased timer.");
            adjustment -= 2000;
            updateTimerText();
            //checkRecords();                                       <-check this
            System.out.println("Time: " + timeInMilliseconds);
        }
    }

    private class PlusButtonListener implements View.OnClickListener {

        public void onClick(View view) {
            System.out.println("Increased timer.");
            adjustment += 1000;
            updateTimerText();
            //checkRecords();                                       <-check this
            System.out.println("Time: " + timeInMilliseconds);
        }
    }

    private class SpinnerListener implements Spinner.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> adapterView, View spinner, int position, long id)
        {
            int a = adapterView.getId();
            switch(a) {
                case R.id.spnRateDriver:    rate_defense.setValue((String)adapterView.getItemAtPosition(position));
                case R.id.spnRateDefense:   rate_driving.setValue((String)adapterView.getItemAtPosition(position));
                case R.id.chooseName:       scoutName = (String) adapterView.getItemAtPosition(position);

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }
}
