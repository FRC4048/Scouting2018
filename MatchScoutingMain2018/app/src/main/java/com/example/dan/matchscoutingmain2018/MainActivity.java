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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AbstractForm {

    private File tabletNumFile;

    private Spinner scoutNameSpinner;
    // aleady have scoutName variable from AbstractForm

    private EditText matchNumEditText;
    private int matchNum;

    private Spinner teamNumberSpinner;
    private int teamNum;

    private Button startTimerBtn;
    private long globalStartTime;
    private long globalEndTime;
    private long globalCurrentTime;
    private Handler timerHandler;
    Timer timer;
    volatile long timeInMilliseconds = 0;
    private final int MATCH_DURATION = 20;
    private Button plusTimerBtn;
    private Button minusTimerBtn;

    private CheckBox absentCheckbox;

    private CheckBox autoBaselineCheckbox;
    private CheckBox autoVaultCheckbox;
    private CheckBox autoAllyCheckbox;
    private CheckBox autoScaleCheckbox;
    private CheckBox autoOpponentCheckbox;

    private ToggleButton allianceToggleBtn;
    private int alliance;
    final int RED_ALLIANCE_NUMBER = 0;
    final int BLUE_ALLIANCE_NUMBER = 1;

    private Button teleopVaultBtn;
    private int teleopVaultCount = 0;
    private EditText vaultCountEditText;

    private Button teleopeAllyBtn;
    private int teleopAllyCount = 0;
    private EditText allyCountEditText;

    private Button teleopScaleBtn;
    private int teleopScaleCount = 0;
    private EditText scaleCountEditText;

    private Button teleopOpponentBtn;
    private int teleopOpponentCount = 0;
    private EditText opponentCountEditText;

    private Button teleopDropBtn;
    private int teleopDropCount = 0;
    private EditText dropCountEditText;

    private CheckBox climbAttemptCheckbox;
    private CheckBox climbSuccessCheckbox;
    private CheckBox climbPowerCheckbox;
    private CheckBox climbHelpCheckbox;
    private CheckBox breakDownCheckbox;
    private CheckBox defenseCheckBox;

    private EditText commentsEditText;

    private Spinner rateDriverSpinner;
    private Spinner rateDefenseSpinner;

    private Button btnSaveForm;
    private Button btnTransferForm;

    private TextView formsPendingLbl;
    // Already have a variable for # of formsPending

    final String FALSE = "0";
    final String TRUE = "1";
    private Record teleopVault;
    private Record teleopAlly;
    private Record teleopScale;
    private Record teleopOpponent;
    private Record teleopCubeDrop;
    private Record absent;
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

    ArrayList<Record> rawTimestampRecords;
    ArrayList<Record> allRecords;
    Record[] staticRecords;

    Context mContext = this;

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

    private class CheckBoxListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            CheckBox checkbox = (CheckBox) compoundButton;
            int currID = checkbox.getId();
            switch (currID)
            {
                case R.id.chkShow:
                {
                    if (checkbox.isChecked()) absent.setValue(TRUE);
                    else absent.setValue(FALSE);
                    break;
                }
                case R.id.btnToggleButton:
                {
                    if (checkbox.isChecked()) // Blue switches to red
                    {
                        System.out.println("BLUE SWITCHING TO RED");
                        allianceToggleBtn.setBackgroundColor(getResources().getColor(R.color.redAlliance));
                        alliance = RED_ALLIANCE_NUMBER;
                    }
                    else // Red switches to blue
                    {
                        System.out.println("RED SWITCHING TO BLUE");
                        allianceToggleBtn.setBackgroundColor(getResources().getColor(R.color.blueAlliance));
                        alliance = BLUE_ALLIANCE_NUMBER;
                    }
                    System.out.println("ALLIANCE: " + alliance);
                    break;
                }
                case R.id.chkAutoBaseline:
                {
                    if (checkbox.isChecked()) auto_cross_baseline.setValue(TRUE);
                    else auto_cross_baseline.setValue(FALSE);
                    break;
                }
                case R.id.chkAutoCubeVault:
                {
                    if (checkbox.isChecked()) auto_cube_in_vault.setValue(TRUE);
                    else auto_cube_in_vault.setValue(FALSE);
                    break;
                }
                case R.id.chkAutoCubeAllySwitch:
                {
                    if (checkbox.isChecked()) auto_cube_in_ally_switch.setValue(TRUE);
                    else auto_cube_in_ally_switch.setValue(FALSE);
                    break;
                }
                case R.id.chkAutoCubeOpponentSwitch:
                {
                    if (checkbox.isChecked()) auto_cube_in_opponent_switch.setValue(TRUE);
                    else auto_cube_in_opponent_switch.setValue(FALSE);
                    break;
                }
                case R.id.chkAutoCubeScale:
                {
                    if (checkbox.isChecked()) auto_cube_in_scale.setValue(TRUE);
                    else auto_cube_in_scale.setValue(FALSE);
                    break;
                }
                case R.id.chkClimbAttempt:
                {
                    if (checkbox.isChecked()) can_climb.setValue(TRUE);
                    else can_climb.setValue(FALSE);
                    break;
                }
                case R.id.chkClimbSuccess:
                {
                    if (checkbox.isChecked()) climb_success.setValue(TRUE);
                    else climb_success.setValue(FALSE);
                    break;
                }
                case R.id.chkClimbPower:
                {
                    if (checkbox.isChecked()) stays_put_when_power_out.setValue(TRUE);
                    else stays_put_when_power_out.setValue(FALSE);
                    break;
                }
                case R.id.chkClimbHelp:
                {
                    if (checkbox.isChecked()) help_others_climb.setValue(TRUE);
                    else help_others_climb.setValue(FALSE);
                    break;
                }
                case R.id.chkMiscBreak:
                {
                    if (checkbox.isChecked()) did_they_break_down.setValue(TRUE);
                    else did_they_break_down.setValue(FALSE);
                    break;
                }
                case R.id.chkMiscDefense:
                {
                    if (checkbox.isChecked()) play_defense.setValue(TRUE);
                    else play_defense.setValue(FALSE);
                    break;
                }
            }
        }
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

    private void updateTimerText()
    {
        int secs = (int) (timeInMilliseconds / 1000);
        startTimerBtn.setText(Integer.toString(secs));
    }

    @Override
    void initRecords() {

        allRecords = new ArrayList<>();
        rawTimestampRecords = new ArrayList<>();
        teleopCubeDrop = new Record("0", MatchForm.Items.CUBE_DROPPED.getId());
        absent = new Record(FALSE, MatchForm.Items.PRESENT.getId());
        can_climb = new Record(FALSE, MatchForm.Items.CAN_CLIMB.getId());
        comments = new Record(null, MatchForm.Items.COMMENTS.getId());
        rate_driving = new Record(null, MatchForm.Items.RATE_DRIVING.getId());
        auto_cross_baseline = new Record(FALSE, MatchForm.Items.AUTO_CROSS_BASELINE.getId());
        climb_success = new Record(FALSE, MatchForm.Items.CLIMB_SUCCESS.getId());
        stays_put_when_power_out = new Record(FALSE, MatchForm.Items.STAYS_PUT_WHEN_POWER_CUT.getId());
        did_they_break_down = new Record(FALSE, MatchForm.Items.DID_THEY_BREAK_DOWN.getId());
        auto_cube_in_vault = new Record(FALSE, MatchForm.Items.AUTO_CUBE_IN_VAULT.getId());
        auto_cube_in_ally_switch = new Record(FALSE, MatchForm.Items.AUTO_CUBE_IN_ALLY_SWITCH.getId());
        auto_cube_in_scale = new Record(FALSE, MatchForm.Items.AUTO_CUBE_IN_SCALE.getId());
        auto_cube_in_opponent_switch = new Record(FALSE, MatchForm.Items.AUTO_CUBE_IN_OPPONENT_SWITCH.getId());
        help_others_climb = new Record(FALSE, MatchForm.Items.HELP_OTHERS_CLIMB.getId());
        play_defense = new Record(FALSE, MatchForm.Items.PLAY_DEFENSE.getId());
        rate_defense = new Record(null, MatchForm.Items.RATE_DEFENSE.getId());

        staticRecords = new Record[16];
        staticRecords[0] = teleopCubeDrop;
        staticRecords[1] = absent;
        staticRecords[2] = can_climb;
        staticRecords[3] = auto_cross_baseline;
        staticRecords[4] = climb_success;
        staticRecords[5] = stays_put_when_power_out;
        staticRecords[6] = did_they_break_down;
        staticRecords[7] = auto_cube_in_vault;
        staticRecords[8] = auto_cube_in_ally_switch;
        staticRecords[9] = auto_cube_in_scale;
        staticRecords[10] = auto_cube_in_opponent_switch;
        staticRecords[11] = help_others_climb;
        staticRecords[12] = play_defense;
        staticRecords[13] = comments;
        staticRecords[14] = rate_defense;
        staticRecords[15] = rate_driving;
    }

    @Override
    void initLayout() {

        SpinnerListener spinnerListener = new SpinnerListener();
        ButtonListener buttonListener = new ButtonListener();
        EditTextWatcher editTextWatcher = new EditTextWatcher();
        CheckBoxListener checkboxListener = new CheckBoxListener();

        scoutNameSpinner = (Spinner) findViewById(R.id.chooseName);
        ArrayAdapter scoutNameAdapter = ArrayAdapter.createFromResource(mContext, R.array.scout_names, android.R.layout.simple_spinner_item);
        scoutNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        scoutNameSpinner.setAdapter(scoutNameAdapter);
        scoutNameSpinner.setOnItemSelectedListener(spinnerListener);

        teamNumberSpinner = (Spinner) findViewById(R.id.txtTeamNumber);
        ArrayAdapter teamNumsAdapter = ArrayAdapter.createFromResource(mContext, R.array.team_numbers, android.R.layout.simple_spinner_item);
        teamNumsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        teamNumberSpinner.setAdapter(teamNumsAdapter);
        teamNumberSpinner.setOnItemSelectedListener(spinnerListener);

        matchNumEditText = (EditText) findViewById(R.id.txtMatchNumber);
        matchNumEditText.addTextChangedListener(editTextWatcher);
        matchNumEditText.setText("");

        startTimerBtn = (Button) findViewById(R.id.btnstartTimer);
        startTimerBtn.setOnClickListener(buttonListener);
        startTimerBtn.setText("START TIMER");
        minusTimerBtn = (Button) findViewById(R.id.btnMinus);
        minusTimerBtn.setOnClickListener(buttonListener);
        plusTimerBtn = (Button) findViewById(R.id.btnPlus);
        plusTimerBtn.setOnClickListener(buttonListener);

        absentCheckbox = (CheckBox) findViewById(R.id.chkShow);
        absentCheckbox.setOnCheckedChangeListener(checkboxListener);

        allianceToggleBtn = (ToggleButton) findViewById(R.id.btnToggleButton);
        allianceToggleBtn.setOnCheckedChangeListener(checkboxListener);
        if (allianceToggleBtn.getText().toString().equals("BLUE ALLIANCE"))
        {
            allianceToggleBtn.setBackgroundColor(getResources().getColor(R.color.blueAlliance));
        }
        if (allianceToggleBtn.getText().toString().equals("RED ALLIANCE"))
        {
            allianceToggleBtn.setBackgroundColor(getResources().getColor(R.color.redAlliance));
        }

        autoBaselineCheckbox = (CheckBox) findViewById(R.id.chkAutoBaseline);
        autoBaselineCheckbox.setOnCheckedChangeListener(checkboxListener);
        autoVaultCheckbox = (CheckBox) findViewById(R.id.chkAutoCubeVault);
        autoVaultCheckbox.setOnCheckedChangeListener(checkboxListener);
        autoAllyCheckbox = (CheckBox) findViewById(R.id.chkAutoCubeAllySwitch);
        autoAllyCheckbox.setOnCheckedChangeListener(checkboxListener);
        autoScaleCheckbox = (CheckBox) findViewById(R.id.chkAutoCubeScale);
        autoScaleCheckbox.setOnCheckedChangeListener(checkboxListener);
        autoOpponentCheckbox = (CheckBox) findViewById(R.id.chkAutoCubeOpponentSwitch);
        autoOpponentCheckbox.setOnCheckedChangeListener(checkboxListener);

        teleopVaultBtn = (Button) findViewById(R.id.btnTeleopCubeVault);
        teleopVaultBtn.setOnClickListener(buttonListener);
        teleopVaultCount = 0;
        vaultCountEditText = (EditText) findViewById(R.id.txtTeleopCount1);
        vaultCountEditText.addTextChangedListener(editTextWatcher);

        teleopeAllyBtn = (Button) findViewById(R.id.btnTeleopCubeAllySwitch);
        teleopeAllyBtn.setOnClickListener(buttonListener);
        teleopAllyCount = 0;
        allyCountEditText = (EditText) findViewById(R.id.txtTeleopCount2);
        allyCountEditText.addTextChangedListener(editTextWatcher);

        teleopScaleBtn = (Button) findViewById(R.id.btnTeleopCubeScale);
        teleopScaleBtn.setOnClickListener(buttonListener);
        scaleCountEditText = (EditText) findViewById(R.id.txtTeleopCount3);
        scaleCountEditText.addTextChangedListener(editTextWatcher);
        teleopScaleCount = 0;

        teleopOpponentBtn = (Button) findViewById(R.id.btnTeleopCubeOpponentSwitch);
        teleopOpponentBtn.setOnClickListener(buttonListener);
        opponentCountEditText = (EditText) findViewById(R.id.txtTeleopCount4);
        teleopOpponentCount = 0;
        opponentCountEditText.addTextChangedListener(editTextWatcher);

        teleopDropBtn = (Button) findViewById(R.id.btnTeleopCubeDrop);
        teleopDropBtn.setOnClickListener(buttonListener);
        dropCountEditText = (EditText) findViewById(R.id.txtTeleopCount5);
        teleopDropCount = 0;
        dropCountEditText.addTextChangedListener(editTextWatcher);

        climbAttemptCheckbox = (CheckBox) findViewById(R.id.chkClimbAttempt);
        climbAttemptCheckbox.setOnCheckedChangeListener(checkboxListener);
        climbSuccessCheckbox = (CheckBox) findViewById(R.id.chkClimbSuccess);
        climbSuccessCheckbox.setOnCheckedChangeListener(checkboxListener);
        climbPowerCheckbox = (CheckBox) findViewById(R.id.chkClimbPower);
        climbPowerCheckbox.setOnCheckedChangeListener(checkboxListener);
        climbHelpCheckbox = (CheckBox) findViewById(R.id.chkClimbHelp);
        climbHelpCheckbox.setOnCheckedChangeListener(checkboxListener);
        breakDownCheckbox = (CheckBox) findViewById(R.id.chkMiscBreak);
        breakDownCheckbox.setOnCheckedChangeListener(checkboxListener);
        defenseCheckBox = (CheckBox) findViewById(R.id.chkMiscDefense);
        defenseCheckBox.setOnCheckedChangeListener(checkboxListener);

        commentsEditText = (EditText) findViewById(R.id.editComments);

        rateDriverSpinner = (Spinner) findViewById(R.id.spnRateDriver);
        ArrayAdapter ratingsAdapter = ArrayAdapter.createFromResource(mContext, R.array.rating_values, android.R.layout.simple_spinner_item);
        ratingsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        rateDriverSpinner.setAdapter(ratingsAdapter);
        rateDefenseSpinner = (Spinner) findViewById(R.id.spnRateDefense);
        rateDefenseSpinner.setAdapter(ratingsAdapter);

        btnSaveForm = (Button) findViewById(R.id.btnSaveForm);
        btnTransferForm = (Button) findViewById(R.id.btnTransferForm);

        btnSaveForm.setOnClickListener(buttonListener);
        btnTransferForm.setOnClickListener(buttonListener);

        formsPendingLbl = (TextView) findViewById(R.id.formsPendingLbl);

    }

    private class ButtonListener implements  View.OnClickListener {

        @Override
        public void onClick(View view) {
            Button button = (Button) view;
            int id = button.getId();

            switch(id)
            {
                case R.id.btnstartTimer:
                {
                    globalStartTime = SystemClock.uptimeMillis();
                    globalEndTime = globalStartTime + (MATCH_DURATION*1000);
                    globalCurrentTime = SystemClock.uptimeMillis();
                    timer.scheduleAtFixedRate(new UpdateTimerTask(), 0, 1000);
                    break;
                }
                case R.id.btnPlus:
                {
                    globalStartTime += 1000 - (globalStartTime % 1000);
                    globalEndTime = globalStartTime + (MATCH_DURATION*1000);
                    timerHandler.obtainMessage(1).sendToTarget();
                    break;
                }
                case R.id.btnMinus:
                {
                    globalStartTime -= (globalStartTime % 1000 + 1000);
                    globalEndTime = globalStartTime + (MATCH_DURATION*1000);
                    timerHandler.obtainMessage(1).sendToTarget();
                    break;
                }
                case R.id.btnTeleopCubeVault:
                {
                    teleopVaultCount++;
                    vaultCountEditText.setText(Integer.toString(teleopVaultCount));
                    teleopVault = new Record(Long.toString(globalCurrentTime), MatchForm.Items.CUBE_IN_VAULT.getId());
                    rawTimestampRecords.add(teleopVault);
                    printRawTimestampRecords();
                    break;
                }
                case R.id.btnTeleopCubeAllySwitch:
                {
                    teleopAllyCount++;
                    allyCountEditText.setText(Integer.toString(teleopAllyCount));
                    teleopAlly = new Record(Long.toString(globalCurrentTime), MatchForm.Items.CUBE_IN_ALLY_SWITCH.getId());
                    rawTimestampRecords.add(teleopAlly);
                    printRawTimestampRecords();
                    break;
                }
                case R.id.btnTeleopCubeScale:
                {
                    teleopScaleCount++;
                    scaleCountEditText.setText(Integer.toString(teleopScaleCount));
                    teleopScale = new Record(Long.toString(globalCurrentTime), MatchForm.Items.CUBE_IN_SCALE.getId());
                    rawTimestampRecords.add(teleopScale);
                    printRawTimestampRecords();
                    break;
                }
                case R.id.btnTeleopCubeOpponentSwitch:
                {
                    teleopOpponentCount++;
                    opponentCountEditText.setText(Integer.toString(teleopOpponentCount));
                    teleopOpponent = new Record(Long.toString(globalCurrentTime), MatchForm.Items.CUBE_IN_OPPONENT_SWITCH.getId());
                    rawTimestampRecords.add(teleopOpponent);
                    printRawTimestampRecords();
                    break;
                }
                case R.id.btnTeleopCubeDrop:
                {
                    teleopDropCount++;
                    dropCountEditText.setText(Integer.toString(teleopDropCount));
                    teleopCubeDrop.setValue(Integer.toString(teleopDropCount));
                    printStaticRecords();
                    break;
                }
                case R.id.btnSaveForm:
                {
                    processRawTimeStampRecords();
                    addStaticRecords();
                    printAllRecords();
                    boolean readyToSave = true;
                    System.out.println("Attempting to save form");
                    String message = "Cannot save the form because";
                    if (checkInvalidTeam())
                    {
                        message += "\n" + "- There are invalid team numbers." + "\n";
                        readyToSave = false;
                    }
                    if (checkInvalidScoutName()) {
                        message += "\n" + "- The scout name is invalid." + "\n";
                        readyToSave = false;
                    }
                    if (matchNum <= 0) {
                        message += "\n" + "- The match number is invalid." + "\n";
                        matchNumEditText.setText("INVALID");
                        readyToSave = false;
                    }
                    System.out.println("GCT: " + globalCurrentTime);
                    System.out.println("GET: " + globalEndTime);
                    if (timeInMilliseconds > 0)
                    {
                        message += "\n" + "- The timer is not yet done." + "\n";
                        readyToSave = false;
                    }
                    if (readyToSave)
                    {
                        actionRequested = Action.SAVE_FORM;
                        showAlertDialog("Are you sure you want to save this form?", "Yes");
                    }
                    else
                    {
                        actionRequested = Action.NONE;
                        showAlertDialog(message, "OK");
                    }
                    break;
                }
                case R.id.btnTransferForm:
                {
                    System.out.println("Attempting to transfer form");
                    actionRequested = Action.CHOOSE_TRANSFER_ACTION;
                    showAlertDialog("Choose your transfer action.", "Transfer all forms", "Transfer last form", "Transfer all archives");
                    break;
                }
            }
        }
    }

    private void printStaticRecords() {
        System.out.print("STATIC RECORDS: ");
        for (int i = 0; i < staticRecords.length-1; i++) {
            System.out.print(staticRecords[i] + "|");
        }
        System.out.println(staticRecords[staticRecords.length -1]);
    }

    private void printAllRecords()
    {
        System.out.print("RECORDS: ");
        for (int i = 0; i < allRecords.size()-1; i++)
        {
            System.out.print(allRecords.get(i) + "|");
        }
        System.out.println(allRecords.get(allRecords.size()-1));
    }

    private boolean checkInvalidScoutName() {
        int index = -1;
        for (int i = 0; i < names.length; i++)
        {
            if (names[i].equals(scoutName))
            {
                index = i;
            }
        }
        return (index == -1);
    }

    private boolean checkInvalidTeam() {
        int index = -1;
        for (int i = 0; i < teams.length; i++)
        {
            if (Integer.parseInt(teams[i]) == teamNum)
            {
                index = i;
            }
        }
        return (index == -1);
    }

    private void addStaticRecords() {

        for (Record staticRecord : staticRecords)
        {
            allRecords.add(staticRecord);
        }
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
                teamNumberSpinner.requestFocus();
                teamNumberSpinner.setSelection(((ArrayAdapter) teamNumberSpinner.getAdapter()).getPosition(storedTeamNum));
                teamNum = Integer.parseInt(storedTeamNum);
                allianceToggleBtn.setChecked(storedAlliance.equals(RED_ALLIANCE_NUMBER));
                alliance = Integer.parseInt(storedAlliance);
                if (alliance == RED_ALLIANCE_NUMBER)
                {
                    allianceToggleBtn.setBackgroundColor(getResources().getColor(R.color.redAlliance));
                    allianceToggleBtn.setText("RED ALLIANCE");
                }
                else
                {
                    allianceToggleBtn.setBackgroundColor(getResources().getColor(R.color.blueAlliance));
                    allianceToggleBtn.setText("BLUE ALLIANCE");
                }
                matchNumEditText.requestFocus();
                matchNumEditText.setText(storedMatchNum);
                matchNum = Integer.parseInt(storedMatchNum);
                scoutNameSpinner.requestFocus();
                scoutNameSpinner.setSelection(((ArrayAdapter) scoutNameSpinner.getAdapter()).getPosition(storedScoutName));
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

        startTimerBtn.setText("START TIMER");
        resetSpinners();
        resetCheckboxes();
        resetEditTexts();

    }

    void resetSpinners()
    {
        scoutNameSpinner.setSelection(((ArrayAdapter) scoutNameSpinner.getAdapter()).getPosition(scoutName));
        teamNumberSpinner.setSelection(((ArrayAdapter) teamNumberSpinner.getAdapter()).getPosition(teamNum));
        rateDriverSpinner.setSelection(((ArrayAdapter) rateDriverSpinner.getAdapter()).getPosition("1"));
        rateDefenseSpinner.setSelection(((ArrayAdapter) rateDefenseSpinner.getAdapter()).getPosition("1"));
    }

    @Override
    void resetRadioGroups() {

    }

    @Override
    void resetCheckboxes() {
        absentCheckbox.setChecked(false);
        autoBaselineCheckbox.setChecked(false);
        autoVaultCheckbox.setChecked(false);
        autoAllyCheckbox.setChecked(false);
        autoScaleCheckbox.setChecked(false);
        autoOpponentCheckbox.setChecked(false);
        climbAttemptCheckbox.setChecked(false);
        climbSuccessCheckbox.setChecked(false);
        climbPowerCheckbox.setChecked(false);
        climbHelpCheckbox.setChecked(false);
        allianceToggleBtn.setChecked(false);
    }

    void resetEditTexts()
    {
        matchNum++;
        matchNumEditText.setText(Integer.toString(matchNum));

        teleopVaultCount = 0;
        vaultCountEditText.setText(Integer.toString(teleopVaultCount));
        teleopAllyCount = 0;
        allyCountEditText.setText(Integer.toString(teleopAllyCount));
        teleopScaleCount = 0;
        scaleCountEditText.setText(Integer.toString(teleopScaleCount));
        teleopOpponentCount = 0;
        opponentCountEditText.setText(Integer.toString(teleopOpponentCount));
        teleopDropCount = 0;
        dropCountEditText.setText(Integer.toString(teleopDropCount));

        commentsEditText.setText("");
    }

    @Override
    Form makeForm() { return new MatchForm(tabletNum, teamNum, matchNum, scoutName, alliance); }

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
                        absentCheckbox.setChecked(val.equals(TRUE));
                        break;
                    }
                    case R.id.chkAutoBaseline:
                    {
                        autoBaselineCheckbox.setChecked(val.equals(TRUE));
                        break;
                    }
                    case R.id.chkAutoCubeVault:
                    {
                        autoVaultCheckbox.setChecked(val.equals(TRUE));
                        break;
                    }
                    case R.id.chkAutoCubeAllySwitch:
                    {
                        autoAllyCheckbox.setChecked(val.equals(TRUE));
                        break;
                    }
                    case R.id.chkAutoCubeScale:
                    {
                        autoScaleCheckbox.setChecked(val.equals(TRUE));
                        break;
                    }
                    case R.id.chkAutoCubeOpponentSwitch:
                    {
                        autoOpponentCheckbox.setChecked(val.equals(TRUE));
                        break;
                    }
                    case R.id.txtTeleopCount1:
                    {
                        vaultCountEditText.setText(val);
                        break;
                    }
                    case R.id.txtTeleopCount2:
                    {
                        allyCountEditText.setText(val);
                        break;
                    }
                    case R.id.txtTeleopCount3:
                    {
                        scaleCountEditText.setText(val);
                        break;
                    }
                    case R.id.txtTeleopCount4:
                    {
                        opponentCountEditText.setText(val);
                        break;
                    }
                    case R.id.txtTeleopCount5:
                    {
                        dropCountEditText.setText(val);
                        break;
                    }
                    case R.id.chkClimbAttempt:
                    {
                        climbAttemptCheckbox.setChecked(val.equals(0));
                        break;
                    }
                    case R.id.chkClimbSuccess:
                    {
                        climbSuccessCheckbox.setChecked(val.equals(0));
                        break;
                    }
                    case R.id.chkClimbPower:
                    {
                        climbPowerCheckbox.setChecked(val.equals(0));
                        break;
                    }
                    case R.id.chkClimbHelp:
                    {
                        climbHelpCheckbox.setChecked(val.equals(0));
                        break;
                    }
                    case R.id.chkMiscBreak:
                    {
                        breakDownCheckbox.setChecked(val.equals(0));
                        break;
                    }
                    case R.id.chkMiscDefense:
                    {
                        defenseCheckBox.setChecked(val.equals(0));
                        break;
                    }
                    case R.id.spnRateDefense:
                    {
                        rateDefenseSpinner.setSelection(((ArrayAdapter) rateDefenseSpinner.getAdapter()).getPosition(val));
                        break;
                    }
                    case R.id.spnRateDriver:
                    {
                        rateDriverSpinner.setSelection(((ArrayAdapter) rateDriverSpinner.getAdapter()).getPosition(val));
                        break;
                    }
                    case R.id.editComments:
                    {
                        commentsEditText.setText(val);
                        break;
                    }
                }
            }
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

    private class EditTextWatcher implements TextWatcher {

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
            if (currEditText.getText().toString().length() > 0)
            {
                switch (currId)
                {
                    case R.id.txtMatchNumber:
                    {
                        matchNum = Integer.parseInt(currEditText.getText().toString());
                        break;
                    }
                    case R.id.txtTeleopCount1:
                    {
                        teleopVaultCount = Integer.parseInt(currEditText.getText().toString());
                        System.out.println("New TeleopVaultCount: " + teleopVaultCount);
                        adjustNumberOccurrences(teleopVaultCount, teleopVault);
                        break;
                    }
                    case R.id.txtTeleopCount2:
                    {
                        teleopAllyCount = Integer.parseInt(currEditText.getText().toString());
                        System.out.println("New TeleopAllyCount: " + teleopAllyCount);
                        adjustNumberOccurrences(teleopAllyCount, teleopAlly);
                        break;
                    }
                    case R.id.txtTeleopCount3:
                    {
                        teleopScaleCount = Integer.parseInt(currEditText.getText().toString());
                        System.out.println("New TeleopScaleCount: " + teleopScaleCount);
                        adjustNumberOccurrences(teleopScaleCount, teleopScale);
                        break;
                    }
                    case R.id.txtTeleopCount4:
                    {
                        teleopOpponentCount = Integer.parseInt(currEditText.getText().toString());
                        System.out.println("New TeleopOpponentCount: " + teleopOpponentCount);
                        adjustNumberOccurrences(teleopOpponentCount, teleopOpponent);
                        break;
                    }
                    case R.id.txtTeleopCount5:
                    {
                        teleopDropCount = Integer.parseInt(currEditText.getText().toString());
                        System.out.println("New TeleopDropCount: " + teleopDropCount);
                        teleopCubeDrop.setValue(Integer.toString(teleopDropCount));
                        break;
                    }
                    case R.id.editComments:
                    {
                        comments.setValue(currEditText.getText().toString());
                        break;
                    }
                }
            }
        }
    }

    private void printRawTimestampRecords()
    {
        if (rawTimestampRecords.size() > 0)
        {
            System.out.print("RAW TIMESTAMPS: ");
            for (int i = 0; i < rawTimestampRecords.size() - 1; i++)
            {
                System.out.print(rawTimestampRecords.get(i) + "|");
            }
            System.out.println(rawTimestampRecords.get(rawTimestampRecords.size()-1));
        }
        else System.out.println("NO RAWTIMESTAMPS YET.");

    }
    private void adjustNumberOccurrences(int goalOccurences, Record wantedRecord)
    {
        printRawTimestampRecords();
        int currOccurrences = findNumberOccurences(wantedRecord);
        System.out.println("Initial current number of occurrences: " + currOccurrences);
        while (currOccurrences != goalOccurences)
        {
            if (goalOccurences > currOccurrences)
            {
                rawTimestampRecords.add(new Record("DIDN'T RECORD TIMESTAMP", wantedRecord.getItemID()));
                System.out.println("Added a raw timestamp record.");
            }
            if (goalOccurences < currOccurrences && findLastTimestamp(wantedRecord) != -1) {
                rawTimestampRecords.remove(findLastTimestamp(wantedRecord));
                System.out.println("Removed a raw timestamp record.");
            }
            currOccurrences = findNumberOccurences(wantedRecord);
            System.out.println("Current number of occurences (updated): " + currOccurrences);
        }
        printRawTimestampRecords();
    }

    private int findLastTimestamp(Record wantedRecord) {
        int index = -1;
        for (int i = rawTimestampRecords.size()-1; i > 0; i--)
        {
            if (rawTimestampRecords.get(i).getItemID() == wantedRecord.getItemID())
            {
                index = i;
                return index;
            }
        }
        return index;
    }

    private int findNumberOccurences(Record wantedRecord) {

        int counter = 0;
        for (Record record : rawTimestampRecords)
        {
            if (record.getItemID() == wantedRecord.getItemID()) counter++;
        }
        return counter;
    }
}
