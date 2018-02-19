package com.example.dan.matchscoutingmain2018;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

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

    private EditText txtTeamNumber;
    private EditText txtMatchNumber;
    private EditText editComments;

    private Spinner chooseName;
    private Spinner spnRateDriver;
    private Spinner spnRateDefense;

    private ToggleButton btnToggleButton;
    private Button btnPlus;
    private Button btnstartTimer;
    private Button btnMinus;
    private Button btnTeleopCubeVault;
    private Button btnTeleopCubeAllySwitch;
    private Button btnTeleopCubeScale;
    private Button btnTeleopCubeOpponentSwitch;
    private Button btnTeleopCubeDrop;

    private EditText txtCubeVaultCount;
    private EditText txtCubeAllyCount;
    private EditText txtCubeScaleCount;
    private EditText txtCubeOpponentCount;
    private EditText txtCubeDropCount;

    private CheckBox chkPresent;

    private Button btnSaveForm;
    private Button btnTransferForm;
    private TextView formsPendingLbl;

    private File tabletNumFile;

    private long startTime = 0;
    private Handler timerHandler;
    Timer timer;
    volatile long timeInMilliseconds = 0;
    long timeSwapBuff = 0;
    long updatedTime = 0;
    long adjustment = 0;

    private long globalStartTime;
    private long globalEndTime;
    private long globalCurrentTime;

    private int alliance;
    final int RED_ALLIANCE_NUMBER = 0;
    final int BLUE_ALLIANCE_NUMBER = 1;

    private int matchNum;
    private int teamNum;

    ArrayList<Record> rawTimestampRecords;
    ArrayList<Record> allRecords;
    Context mContext = this;

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
        int secs = (int) (timeInMilliseconds / 1000);
        btnstartTimer.setText(Integer.toString(secs));
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        timer = new Timer();
        timerHandler = new Handler()
        {
            public void handleMessage(Message msg)
            {
                updateTimerText();
            }
        };
        tabletNumFile = new File(getFilesDir().getAbsolutePath(), TABLET_NUM_FILE);
        if (!tabletNumFile.exists())
        {
            try {
                tabletNumFile.createNewFile();
                tabletNumFile.setWritable(true);
                showTabletNumDialog();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        initConfigs();
        initLayout();
        initRecords();
        initSaveState();
        initArchiveSystem();
    }

    private void showTabletNumDialog()
    {
        if (tabletNumFile.exists()) System.out.println("tabletNumFile exists now");
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View tabletNumDialogView = inflater.inflate(R.layout.tablet_num, null);
        final AlertDialog.Builder tabletNumDialogBuilder = new AlertDialog.Builder(mContext);
        tabletNumDialogBuilder.setTitle("Set Tablet Number Dialog");
        tabletNumDialogBuilder.setView(tabletNumDialogView);
        final EditText tabletNumEditTxt = (EditText) tabletNumDialogView.findViewById(R.id.tabletNumEditTxt);
        tabletNumDialogBuilder.setCancelable(false);
        tabletNumDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String tabletNumToAdd = tabletNumEditTxt.getText().toString();
                try {
                    BufferedWriter fileWriter = new BufferedWriter(new FileWriter(tabletNumFile));
                    fileWriter.write(tabletNumToAdd);
                    fileWriter.close();
                    tabletNum = Integer.parseInt(tabletNumToAdd);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        tabletNumDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog tabletNumDialog = tabletNumDialogBuilder.create();
        tabletNumDialog.show();
    }

    @Override
    void initConfigs() {

        if (tabletNumFile.exists()) System.out.println("tabletNumFile exists.");
        names = getResources().getStringArray(R.array.scout_names);
        teams = getResources().getStringArray(R.array.team_numbers);
        pcCompanion = getResources().getString(R.string.pc_companion);
        tabletNum = readTabletNumber();
        if (tabletNum == -1) showTabletNumDialog();

    }

    @Override
    void initRecords() {

        allRecords = new ArrayList<>();
        rawTimestampRecords = new ArrayList<>();
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

        chooseName = (Spinner) findViewById(R.id.chooseName);
        ArrayAdapter scoutNameAdapter = ArrayAdapter.createFromResource(mContext, R.array.scout_names, android.R.layout.simple_spinner_item);
        scoutNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        chooseName.setAdapter(scoutNameAdapter);
        SpinnerListener spinnerListener = new SpinnerListener();
        chooseName.setOnItemSelectedListener(spinnerListener);

        spnRateDriver = (Spinner) findViewById(R.id.spnRateDriver);
        ArrayAdapter ratingsAdapter = ArrayAdapter.createFromResource(mContext, R.array.rating_values, android.R.layout.simple_spinner_item);
        ratingsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spnRateDriver.setAdapter(ratingsAdapter);
        spnRateDefense = (Spinner) findViewById(R.id.spnRateDefense);
        spnRateDefense.setAdapter(ratingsAdapter);

        EditTextListener editTextListener = new EditTextListener();
        txtTeamNumber = (EditText) findViewById(R.id.txtTeamNumber);
        txtTeamNumber.addTextChangedListener(editTextListener);
        txtMatchNumber = (EditText) findViewById(R.id.txtMatchNumber);
        txtMatchNumber.addTextChangedListener(editTextListener);
        editComments = (EditText) findViewById(R.id.editComments);

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

        chkPresent = (CheckBox) findViewById(R.id.chkShow);

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

        txtCubeVaultCount = (EditText) findViewById(R.id.txtTeleopCount1);
        txtCubeAllyCount = (EditText) findViewById(R.id.txtTeleopCount2);
        txtCubeScaleCount = (EditText) findViewById(R.id.txtTeleopCount3);
        txtCubeOpponentCount = (EditText) findViewById(R.id.txtTeleopCount4);
        txtCubeDropCount = (EditText) findViewById(R.id.txtTeleopCount5);

        btnSaveForm = (Button) findViewById(R.id.btnSaveForm);
        btnTransferForm = (Button) findViewById(R.id.btnTransferForm);

        btnToggleButton = (ToggleButton) findViewById(R.id.btnToggleButton);
        AllianceButtonListener allianceButtonListener = new AllianceButtonListener();
        btnToggleButton.setOnCheckedChangeListener(allianceButtonListener);
        if (btnToggleButton.getText().toString().equals("BLUE ALLIANCE"))
        {
            btnToggleButton.setBackgroundColor(getResources().getColor(R.color.blueAlliance));
        }
        if (btnToggleButton.getText().toString().equals("RED ALLIANCE"))
        {
            btnToggleButton.setBackgroundColor(getResources().getColor(R.color.redAlliance));
        }

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

        btnSaveForm.setOnClickListener(main_buttons);
        btnTransferForm.setOnClickListener(main_buttons);

        formsPendingLbl = (TextView) findViewById(R.id.formsPendingLbl);

    }

    @Override
    void initSaveState() {
        File saveStateFile = new File(getFilesDir().getAbsolutePath(), STATE_SAVE_FILE);
        if (saveStateFile.exists())
        {
            try {
                BufferedReader saveStateReader = new BufferedReader(new FileReader(saveStateFile));
                String formsPendingNum = saveStateReader.readLine();
                String storedTeamNum = saveStateReader.readLine();
                String storedAlliance = saveStateReader.readLine();
                String storedMatchNum = saveStateReader.readLine();
                String storedScoutName = saveStateReader.readLine();
                String records = saveStateReader.readLine();
                String storedGlobalStartTime = saveStateReader.readLine();
                String storedGlobalEndTime = saveStateReader.readLine();
                String storedGlobalCurrentTime = saveStateReader.readLine();

                formsPendingLbl.setText(formsPendingNum + " Forms Pending");
                formsPending = Integer.parseInt(formsPendingNum);
                txtTeamNumber.requestFocus();
                txtTeamNumber.setText(storedTeamNum);
                teamNum = Integer.parseInt(storedTeamNum);
                btnToggleButton.setChecked(storedAlliance.equals(RED_ALLIANCE_NUMBER));
                alliance = Integer.parseInt(storedAlliance);
                if (alliance == RED_ALLIANCE_NUMBER)
                {
                    btnToggleButton.setBackgroundColor(getResources().getColor(R.color.redAlliance));
                    btnToggleButton.setText("RED ALLIANCE");
                }
                else
                {
                    btnToggleButton.setBackgroundColor(getResources().getColor(R.color.blueAlliance));
                    btnToggleButton.setText("BLUE ALLIANCE");
                }
                txtMatchNumber.requestFocus();
                txtMatchNumber.setText(storedMatchNum);
                matchNum = Integer.parseInt(storedMatchNum);
                chooseName.requestFocus();
                chooseName.setSelection(((ArrayAdapter) chooseName.getAdapter()).getPosition(storedScoutName));
                scoutName = storedScoutName;
                globalStartTime = Long.parseLong(storedGlobalStartTime);
                globalEndTime = Long.parseLong(storedGlobalEndTime);
                globalCurrentTime = Long.parseLong(storedGlobalCurrentTime);
                updateTimerText();
                String[] splitRecords = records.split("\\|");
                setState(splitRecords, 0);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    void executeRequest() {
        switch (actionRequested) {
            case SAVE_FORM:
                if (saveForm()) {
                    Toast.makeText(getApplicationContext(), "FORM SAVED",
                            Toast.LENGTH_SHORT).show();
                    formsPending++;
                    resetForm();
                    formsPendingLbl.setText(formsPending + " Form(s) Pending");
                } else showAlertDialog("FORM NOT SAVED: " +
                        "I/O problem encountered. Try again - if the problem persists, " +
                        "TALK TO LUCAS!", "Ok", null);
                break;
            case TRANSFER_FORMS:
                if (formsPending > 0) {
                    prepareFormTransfer(TEMP_FILE);
                    prepareToTransfer(TEMP_FILE);
                    actionRequested = Action.CHECK_TRANSFER;
                    formsPending = 0;
                    formsPendingLbl.setText(formsPending + " Form(s) Pending");
                    firstForm = true;
                    archiveCurrentFile();
                } else showAlertDialog("No pending forms!", "Ok");
                break;
            case TRANSFER_LAST_FORMS:
                if (archivedFiles > 0) {
                    String fileName = ARCHIVE_FILE.split("\\.")[0] + (archivedFiles - 1)
                            + ARCHIVE_FILE.split("\\.")[1];
                    prepareFormTransfer(fileName);
                    prepareToTransfer(fileName);
                } else showAlertDialog("There has not been a transfer yet.", "Ok");
                break;
            case TRANSFER_ALL_ARCHIVES:
                break;
            case RECEIVE_CONFIG:
                retrieveComputerFile(CONFIG_FILE);
                initLayout();
                break;
            case WARNING_TEAMNUM:
                break;
        }
        actionRequested = Action.NONE;
    }

    @Override
    void saveState() {
        File saveStateFile = new File(getFilesDir().getAbsolutePath(), STATE_SAVE_FILE);
        if (!saveStateFile.exists())
        {
            System.out.println("saveStateFile does not exist.");
            try {
                saveStateFile.createNewFile();
                saveStateFile.setWritable(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (saveStateFile.exists()) {
            System.out.println("saveStateFile exists.");
            String saveStateStr = "";
            saveStateStr += Integer.toString(formsPending) + "\n";
            saveStateStr += teamNum + "\n";
            saveStateStr += alliance + "\n";
            saveStateStr += matchNum + "\n";
            saveStateStr += scoutName + "\n";
            processRawTimeStampRecords();
            if (allRecords.size() > 0)
            {
                for (int i = 0; i < allRecords.size()-1; i++)
                {
                    saveStateStr += allRecords.get(i) + "|";
                }
                saveStateStr += allRecords.get(allRecords.size()-1);
            }
            saveStateStr+="\n" + Long.toString(globalStartTime) + "\n";
            saveStateStr += Long.toString(globalEndTime) + "\n";
            saveStateStr += Long.toString(globalCurrentTime) + "\n";
            System.out.println("saveStateStr: " + "\n"+ saveStateStr.toString());
            BufferedWriter saveStateWriter = null;
            try {
                saveStateWriter = new BufferedWriter(new FileWriter(saveStateFile));
                saveStateWriter.write(saveStateStr);
                saveStateWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void processRawTimeStampRecords() {
        for (Record r : rawTimestampRecords)
        {
            long recordedTime = Long.parseLong(r.getValue());
            long elapsedTime = globalEndTime - recordedTime;
            Record processedTimeRec = new Record(Long.toString(elapsedTime), r.getItemID());
            allRecords.add(processedTimeRec);
        }
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
    boolean readyToSave() {

        return false;
    }

    @Override
    void setState(String[] records, int startingIndex) {
        for (String record : records)
        {
            String[] recordVal = record.split("\\,");
            if (recordVal.length == 2)
            {
                Record currRecord = new Record(recordVal[1], Integer.parseInt(recordVal[0]));
                allRecords.add(currRecord);

                int itemID = currRecord.getItemID();
                String val = currRecord.getValue();
                switch(itemID)
                {
                    case R.id.chkShow:
                    {
                        chkPresent.setChecked(val.equals(0));
                        break;
                    }
                    case R.id.chkAutoBaseline:
                    {
                        chkAutoBaseline.setChecked(val.equals(0));
                        break;
                    }
                    case R.id.chkAutoCubeVault:
                    {
                        chkAutoCubeVault.setChecked(val.equals(0));
                        break;
                    }
                    case R.id.chkAutoCubeAllySwitch:
                    {
                        chkAutoCubeAllySwitch.setChecked(val.equals(0));
                        break;
                    }
                    case R.id.chkAutoCubeScale:
                    {
                        chkAutoCubeScale.setChecked(val.equals(0));
                        break;
                    }
                    case R.id.chkAutoCubeOpponentSwitch:
                    {
                        chkAutoCubeOpponentSwitch.setChecked(val.equals(0));
                        break;
                    }
                    case R.id.txtTeleopCount1:
                    {
                        txtCubeVaultCount.setText(val);
                        break;
                    }
                    case R.id.txtTeleopCount2:
                    {
                        txtCubeAllyCount.setText(val);
                        break;
                    }
                    case R.id.txtTeleopCount3:
                    {
                        txtCubeScaleCount.setText(val);
                        break;
                    }
                    case R.id.txtTeleopCount4:
                    {
                        txtCubeOpponentCount.setText(val);
                        break;
                    }
                    case R.id.txtTeleopCount5:
                    {
                        txtCubeDropCount.setText(val);
                        break;
                    }
                    case R.id.chkClimbAttempt:
                    {
                        chkClimbAttempt.setChecked(val.equals(0));
                        break;
                    }
                    case R.id.chkClimbSuccess:
                    {
                        chkClimbSuccess.setChecked(val.equals(0));
                        break;
                    }
                    case R.id.chkClimbPower:
                    {
                        chkClimbPower.setChecked(val.equals(0));
                        break;
                    }
                    case R.id.chkClimbHelp:
                    {
                        chkClimbHelp.setChecked(val.equals(0));
                        break;
                    }
                    case R.id.chkMiscBreak:
                    {
                        chkMiscBreak.setChecked(val.equals(0));
                        break;
                    }
                    case R.id.chkMiscDefense:
                    {
                        chkMiscDefense.setChecked(val.equals(0));
                        break;
                    }
                    case R.id.spnRateDefense:
                    {
                        spnRateDefense.setSelection(((ArrayAdapter) spnRateDefense.getAdapter()).getPosition(val));
                        break;
                    }
                    case R.id.spnRateDriver:
                    {
                        spnRateDriver.setSelection(((ArrayAdapter) spnRateDriver.getAdapter()).getPosition(val));
                        break;
                    }
                    case R.id.editComments:
                    {
                        editComments.setText(val);
                        break;
                    }
                }
            }
        }
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

    private class ButtonListenerTeleop implements View.OnClickListener {

        public void onClick(View view) {


            int a = view.getId();
            switch(a) {
                case R.id.btnTeleopCubeVault:           changeValue(txtCubeVaultCount); timestamp(MatchForm.Items.CUBE_IN_VAULT.getId(), rawTimestampRecords); break;
                case R.id.btnTeleopCubeAllySwitch:      changeValue(txtCubeAllyCount); timestamp(MatchForm.Items.CUBE_IN_ALLY_SWITCH.getId(), rawTimestampRecords); break;
                case R.id.btnTeleopCubeScale:           changeValue(txtCubeScaleCount); timestamp(MatchForm.Items.CUBE_IN_SCALE.getId(), rawTimestampRecords); break;
                case R.id.btnTeleopCubeOpponentSwitch:  changeValue(txtCubeOpponentCount); timestamp(MatchForm.Items.CUBE_IN_OPPONENT_SWITCH.getId(), rawTimestampRecords); break;
                case R.id.btnTeleopCubeDrop:            changeValue(txtCubeDropCount); break;
                case R.id.btnSaveForm:
                {
                    break;
                }
                case R.id.btnTransferForm:
                {
                    break;
                }

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
            name.add(new Record(String.valueOf(globalCurrentTime), id));
        }
    }

    private class MasterButtonListener implements View.OnClickListener {
        public void onClick(View view) {
            globalStartTime = SystemClock.uptimeMillis();
            globalEndTime = globalStartTime + (150*1000);
            globalCurrentTime = SystemClock.uptimeMillis();
            timer.scheduleAtFixedRate(new UpdateTimerTask(), 0, 1000);
        }
    }

    private class MinusButtonListener implements View.OnClickListener {

        public void onClick(View view) {
            globalStartTime -= globalStartTime % 1000;
            globalEndTime = globalStartTime + (150*1000);
            timerHandler.obtainMessage(1).sendToTarget();
        }
    }

    private class PlusButtonListener implements View.OnClickListener {

        public void onClick(View view) {
            globalStartTime += 1000 - (globalStartTime % 1000);
            globalEndTime = globalStartTime + (150*1000);
            timerHandler.obtainMessage(1).sendToTarget();
        }
    }

    private class SpinnerListener implements Spinner.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> adapterView, View spinner, int position, long id)
        {
            int a = adapterView.getId();
            switch(a) {
                case R.id.spnRateDriver:
                {
                    rate_defense.setValue((String)adapterView.getItemAtPosition(position));
                    break;
                }
                case R.id.spnRateDefense:
                {
                    rate_driving.setValue((String)adapterView.getItemAtPosition(position));
                    break;
                }
                case R.id.chooseName:
                {
                    scoutName = (String) adapterView.getItemAtPosition(position);
                    break;
                }

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    private class UpdateTimerTask extends TimerTask {

        @Override
        public void run() {
            globalCurrentTime = SystemClock.uptimeMillis();
            System.out.println("GLOBAL CURRENT TIME: " + globalCurrentTime);
            timeInMilliseconds = globalEndTime - globalCurrentTime;
            System.out.println("CURRENT TIME IN MS: " + timeInMilliseconds);
            if (globalCurrentTime < globalEndTime) timerHandler.obtainMessage(1).sendToTarget();
        }
    }

    private class AllianceButtonListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (isChecked) // Blue switches to red
            {
                System.out.println("BLUE SWITCHING TO RED");
                btnToggleButton.setBackgroundColor(getResources().getColor(R.color.redAlliance));
                alliance = RED_ALLIANCE_NUMBER;
            }
            else // Red switches to blue
            {
                System.out.println("RED SWITCHING TO BLUE");
                btnToggleButton.setBackgroundColor(getResources().getColor(R.color.blueAlliance));
                alliance = BLUE_ALLIANCE_NUMBER;
            }
            System.out.println("ALLIANCE: " + alliance);
        }
    }

    private class EditTextListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            EditText currEditText = (EditText) getCurrentFocus();
            if (currEditText == null) return;
            int currId = currEditText.getId();
            switch (currId)
            {
                case R.id.txtMatchNumber:
                {
                    if (currEditText.getText().toString().length() > 0)
                    {
                        matchNum = Integer.parseInt(currEditText.getText().toString());
                        System.out.println("Set match number to " + matchNum);
                        break;
                    }
                }
                case R.id.txtTeamNumber:
                {
                    if (currEditText.getText().toString().length() > 0)
                    {
                        teamNum = Integer.parseInt(currEditText.getText().toString());
                        System.out.println("Set team number to " + teamNum);
                        break;
                    }
                }
            }
        }
    }

    private class PresentCheckBoxListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            CheckBox checkbox = (CheckBox) compoundButton;
            int currID = checkbox.getId();
            switch (currID)
            {
                case R.id.chkAutoBaseline:
                {
                    if (checkbox.isChecked()) allRecords.add(auto_cross_baseline);
                    else allRecords.remove(auto_cross_baseline);
                    break;
                }
                case R.id.chkAutoCubeVault:
                {
                    if (checkbox.isChecked()) allRecords.add(auto_cube_in_vault);
                    else allRecords.remove(auto_cube_in_vault);
                    break;
                }
                case R.id.chkAutoCubeAllySwitch:
                {
                    if (checkbox.isChecked()) allRecords.add(auto_cube_in_ally_switch);
                    else allRecords.remove(auto_cube_in_ally_switch);
                    break;
                }
                case R.id.chkAutoCubeOpponentSwitch:
                {
                    if (checkbox.isChecked()) allRecords.add(auto_cube_in_opponent_switch);
                    else allRecords.remove(auto_cube_in_opponent_switch);
                    break;
                }
                case R.id.chkAutoCubeScale:
                {
                    if (checkbox.isChecked()) allRecords.add(auto_cube_in_scale);
                    else allRecords.remove(auto_cube_in_scale);
                    break;
                }
            }
        }
    }
}
