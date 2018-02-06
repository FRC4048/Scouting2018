package com.example.dan.matchscoutingmain2018;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

    private Record[] records = new Record[15];

    private ArrayList<Record> cube_in_vault;
    private ArrayList<Record> cube_in_ally_switch;
    private ArrayList<Record> cube_in_scale;
    private ArrayList<Record> cube_in_opponent_switch;
    private ArrayList<Record> cube_in_dropper;

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






    // other variables?


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
                case R.id.chkAutoCubeVault:
                case R.id.chkAutoCubeAllySwitch:
                case R.id.chkAutoCubeScale:
                case R.id.chkAutoCubeOpponentSwitch:
                case R.id.chkClimbAttempt:
                case R.id.chkClimbSuccess:
                case R.id.:
                case R.id.:
                case R.id.:
                case R.id.:
                case R.id.:


            }

        }
        //create listener for all types of shits
    }
    private class ButtonListener implements View.OnClickListener {


        @Override
        public void onClick(View view) {
          //create new record with timestamp
          //2 methods , for lower buttons, for timer butttons
        }
    }

    private class SpinnerListener implements
}
