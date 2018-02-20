package shreya.overallscoutingform2018;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Timer;
import java.util.TimerTask;

public class FormActivity extends AbstractForm {
    Context mContext = this;

    // Objects for visual elements
    // Basics visual elements - object declarations
    AutoCompleteTextView scoutNameEditText;
    AutoCompleteTextView matchNumEditText;
    AutoCompleteTextView redLeftEditText;
    AutoCompleteTextView redCenterEditText;
    AutoCompleteTextView redRightEditText;
    AutoCompleteTextView blueLeftEditText;
    AutoCompleteTextView blueCenterEditText;
    AutoCompleteTextView blueRightEditText;
    // Game play section - visual elements object declarations
    Button redScaleOwnershipBtn;
    Button redSwitchOwnershipBtn;
    Button redBlueSwitchOwnershipBtn;
    Button redLevitateBtn;
    Button redBoostBtn;
    Button redForceBtn;
    Button blueScaleOwnershipBtn;
    Button blueSwitchOwnershipBtn;
    Button blueRedSwitchOwnershipBtn;
    Button blueLevitateBtn;
    Button blueBoostBtn;
    Button blueForceBtn;
    // Penalties section - visual elements object declarations
    CheckBox redLeftYellowLbl;
    CheckBox redCenterYellowLbl;
    CheckBox redRightYellowLbl;
    CheckBox blueLeftYellowLbl;
    CheckBox blueCenterYellowLbl;
    CheckBox blueRightYellowLbl;
    CheckBox redLeftRedLbl;
    CheckBox redCenterRedLbl;
    CheckBox redRightRedLbl;
    CheckBox blueLeftRedLbl;
    CheckBox blueCenterRedLbl;
    CheckBox blueRightRedLbl;
    // Points section - visual elements object declarations
    EditText redFoulPoints;
    EditText blueFoulPoints;
    EditText redScoreEditText;
    EditText blueScoreEditText;
    Button saveBtn;
    Button transferBtn;
    Button viewTimelineBtn;
    Button startTimerBtn;
    Button addTimerBtn;
    Button subtractTimerBtn;
    TextView formsPendingLbl;
    final int teamNumLabelLength = 11;

    private Timer timer;
    private Handler timerHandler;
    volatile long timeInMilliseconds = 0;

    int yellowCardID = OverallForm.Items.YELLOW_CARD.getId();
    int redCardID = OverallForm.Items.RED_CARD.getId();
    Record redLeftYellow;
    Record redCenterYellow;
    Record redRightYellow;
    Record blueLeftYellow;
    Record blueCenterYellow;
    Record blueRightYellow;
    Record redLeftRed;
    Record redCenterRed;
    Record redRightRed;
    Record blueLeftRed;
    Record blueCenterRed;
    Record blueRightRed;
    Record redAllianceScore;
    Record blueAllianceScore;
    Record redFoulPts;
    Record blueFoulPts;

    File tabletNumFile;

    int[] teamsPlaying = new int[6];
    int matchNum = 0;
    int[] invalidTeamNums = new int[6];
    private final int MATCH_DURATION = 10;
    private long globalStartTime = SystemClock.uptimeMillis();
    private long globalCurrentTime = globalStartTime;
    private long globalEndTime = globalStartTime + (MATCH_DURATION)*1000;
    private ArrayList<Record> rawTimestampRecords = new ArrayList<>();

    private Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            globalCurrentTime = SystemClock.uptimeMillis();
            System.out.println("GLOBAL CURRENT TIME: " + globalCurrentTime);
            timeInMilliseconds = globalEndTime - globalCurrentTime;
            System.out.println("CURRENT TIME IN MS: " + timeInMilliseconds);
            updateTimerText();
            new Thread(runTimerThread).run();
        }
    };

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

    private Runnable runTimerThread = new Runnable() {
        @Override
        public void run() {
            if (globalCurrentTime < globalEndTime) timerHandler.postDelayed(new Thread(updateTimerThread), 1000);
        }
    };

    private void updateTimerText() {
        int secs = (int) (timeInMilliseconds / 1000);
        startTimerBtn.setText(Integer.toString(secs));
    }


    ArrayList<Record> allRecords = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_form);
        timerHandler = new Handler()
        {
            public void handleMessage(Message msg)
            {
                updateTimerText();
            }
        };
        timer = new Timer();
        tabletNumFile = new File(getFilesDir().getAbsolutePath(), TABLET_NUM_FILE);
        if (!tabletNumFile.exists()) {
            try {
                tabletNumFile.createNewFile();
                tabletNumFile.setWritable(true);
                showTabletNumDialog();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else System.out.println("tabletNumFile does exist.");
        initConfigs();
        initLayout();
        initSaveState();
        initArchiveSystem();

//        if (checkConfigFile()) {
//            initConfigs();
//            initLayout();
//            initSaveState();
//            initArchiveSystem();
//        } else {
//            initLayout();
//            initSaveState();
//            initArchiveSystem();
//            actionRequested = Action.RECEIVE_CONFIG;
//            showAlertDialog("A configuration file from the master computer is required to continue."
//                    + "\nPlease transfer the file to this machine.", "I've transferred the file");
//        }

    }

    private class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Button currButton = (Button) view;
            int currId = currButton.getId();
            switch (currId) {
                case R.id.startTimerBtn: {
                    System.out.println("Start timer button pressed.");
                    globalStartTime = SystemClock.uptimeMillis();
                    System.out.println("GLOBAL START TIME: " + globalStartTime);
                    globalCurrentTime = SystemClock.uptimeMillis();
                    globalEndTime = globalStartTime + MATCH_DURATION*1000;
                    System.out.println("GLOBAL END TIME: " + globalEndTime);
                    timer.scheduleAtFixedRate(new UpdateTimerTask(), 0, 1000);
                    break;
                }
                case R.id.addTimerBtn: {
                    System.out.println("Increased timer.");
                    globalStartTime += 1000 - (globalStartTime % 1000);
                    globalEndTime = globalStartTime + (MATCH_DURATION*1000);
                    System.out.println("NEW GLOBAL START TIME: " + globalStartTime);
                    System.out.println("NEW GLOBAL END TIME: " + globalEndTime);
                    System.out.println("NEW GLOBAL CURRENT TIME: " + globalCurrentTime);
                    timerHandler.obtainMessage(1).sendToTarget();
                    break;
                }
                case R.id.subtractTimerBtn: {
                    System.out.println("Decreased timer.");
                    globalStartTime -= (globalStartTime % 1000 + 1000);
                    globalEndTime = globalStartTime + (MATCH_DURATION*1000);
                    System.out.println("NEW GLOBAL START TIME: " + globalStartTime);
                    System.out.println("NEW GLOBAL END TIME: " + globalEndTime);
                    System.out.println("NEW GLOBAL CURRENT TIME: " + globalCurrentTime);
                    timerHandler.obtainMessage(1).sendToTarget();
                    break;
                }
                case R.id.redOwnershipScaleBtn: {
                    System.out.println("Red alliance gained ownership of the scale at " + globalCurrentTime + ".");
                    Record redScale = new Record(Long.toString(globalCurrentTime), OverallForm.Items.RED_OWNS_SCALE.getId());
                    rawTimestampRecords.add(redScale);
                    break;
                }
                case R.id.redSwitchOwnershipBtn: {
                    System.out.println("Red alliance gained ownership of red switch.");
                    Record redSwitch = new Record(Long.toString(globalCurrentTime), OverallForm.Items.RED_OWNS_RED_SWITCH.getId());
                    rawTimestampRecords.add(redSwitch);
                    break;
                }
                case R.id.redBlueSwitchOwnershipBtn: {
                    System.out.println("Red alliance gained ownership of the blue switch.");
                    Record redBlueSwitch = new Record(Long.toString(globalCurrentTime), OverallForm.Items.RED_OWNS_BLUE_SWITCH.getId());
                    rawTimestampRecords.add(redBlueSwitch);
                    break;
                }
                case R.id.redBoostBtn: {
                    System.out.println("Red alliance used boost.");
                    Record redBoost = new Record(Long.toString(globalCurrentTime), OverallForm.Items.RED_USED_BOOST.getId());
                    rawTimestampRecords.add(redBoost);
                    break;
                }
                case R.id.redForceBtn: {
                    System.out.println("Red alliance used force.");
                    Record redForce = new Record(Long.toString(globalCurrentTime), OverallForm.Items.RED_USED_FORCE.getId());
                    rawTimestampRecords.add(redForce);
                    break;
                }
                case R.id.redLevitateBtn: {
                    System.out.println("Red alliance used levitate.");
                    Record redLevitate = new Record(Long.toString(globalCurrentTime), OverallForm.Items.RED_USED_LEVITATE.getId());
                    rawTimestampRecords.add(redLevitate);
                    break;
                }
                case R.id.blueScaleOwnershipBtn: {
                    System.out.println("Blue alliance gained ownership of the scale.");
                    Record blueScale = new Record(Long.toString(globalCurrentTime), OverallForm.Items.BLUE_OWNS_SCALE.getId());
                    rawTimestampRecords.add(blueScale);
                    break;
                }
                case R.id.blueSwitchOwnershipBtn: {
                    System.out.println("Blue alliance gained ownership of blue switch.");
                    Record blueSwitch = new Record(Long.toString(globalCurrentTime), OverallForm.Items.BLUE_OWNS_BLUE_SWITCH.getId());
                    rawTimestampRecords.add(blueSwitch);
                    break;
                }
                case R.id.blueRedSwitchOwnershipBtn: {
                    System.out.println("Blue alliance gained ownership of red switch.");
                    Record blueRedSwitch = new Record(Long.toString(globalCurrentTime), OverallForm.Items.BLUE_OWNS_RED_SWITCH.getId());
                    rawTimestampRecords.add(blueRedSwitch);
                    break;
                }
                case R.id.blueBoostBtn: {
                    System.out.println("Blue used boost");
                    Record blueBoost = new Record(Long.toString(globalCurrentTime), OverallForm.Items.BLUE_USED_BOOST.getId());
                    rawTimestampRecords.add(blueBoost);
                    break;
                }
                case R.id.blueForceBtn: {
                    System.out.println("Blue used force");
                    Record blueForce = new Record(Long.toString(globalCurrentTime), OverallForm.Items.BLUE_USED_FORCE.getId());
                    rawTimestampRecords.add(blueForce);
                    break;
                }
                case R.id.blueLevitateBtn: {
                    System.out.println("Blue used levitate");
                    Record blueLevitate = new Record(Long.toString(globalCurrentTime), OverallForm.Items.BLUE_USED_LEVITATE.getId());
                    rawTimestampRecords.add(blueLevitate);
                    break;
                }
                case R.id.saveFormBtn: {
                    processRawTimeStampRecords();
                    printRecords();
                    boolean readyToSave = true;
                    System.out.println("Attempting to save form");
                    String message = "Cannot save the form because";
                    if (checkInvalidTeams())
                    {
                        message += "\n" + "- There are invalid team numbers." + "\n";
                        readyToSave = false;
                    }
                    if (checkInvalidScoutName()) {
                        message += "\n" + "- The scout name is invalid." + "\n";
                        resetInvalidEditTexts(6);
                        readyToSave = false;
                    }
                    if (matchNum <= 0) {
                        message += "\n" + "- The match number is invalid." + "\n";
                        resetInvalidEditTexts(7);
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
                case R.id.transferFormBtn: {
                    System.out.println("Attempting to transfer form");
                    actionRequested = Action.CHOOSE_TRANSFER_ACTION;
                    showAlertDialog("Choose your transfer action.", "Transfer all forms", "Transfer last form", "Transfer all archives");
                    break;
                }
                case R.id.viewTimelineBtn: {
                    System.out.println("Attempting to view timeline");
                    break;
                }
            }
        }
    }

    private void printRecords() {
        for (Record r : allRecords) {
            System.out.print(r + "|");
        }
    }

    private void processRawTimeStampRecords()
    {
        for (Record r : rawTimestampRecords)
        {
            long recordedTime = Long.parseLong(r.getValue());
            long elapsedTime = globalEndTime - recordedTime;
            Record processedTimeRec = new Record(Long.toString(elapsedTime), r.getItemID());
            allRecords.add(processedTimeRec);
        }
    }

    private class CheckBoxChangeListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton c, boolean b) {

            CheckBox checkbox = (CheckBox) c;
            int currId = checkbox.getId();

            switch (currId) {
                case R.id.redLeftYellowChk: {
                    if (checkbox.isChecked()) allRecords.add(redLeftYellow);
                    else allRecords.remove(redLeftYellow);
                    printRecords();
                    System.out.println();
                    break;
                }
                case R.id.redCenterYellowChk: {
                    if (checkbox.isChecked()) allRecords.add(redCenterYellow);
                    else allRecords.remove(redCenterYellow);
                    printRecords();
                    System.out.println();
                    break;
                }
                case R.id.redRightYellowChk: {
                    if (checkbox.isChecked()) allRecords.add(redRightYellow);
                    else allRecords.remove(redRightYellow);
                    printRecords();
                    System.out.println();
                    break;
                }
                case R.id.blueLeftYellowChk: {
                    if (checkbox.isChecked()) allRecords.add(blueLeftYellow);
                    else allRecords.remove(blueLeftYellow);
                    printRecords();
                    System.out.println();
                    break;
                }
                case R.id.blueCenterYellowChk: {
                    if (checkbox.isChecked()) allRecords.add(blueCenterYellow);
                    else allRecords.remove(blueCenterYellow);
                    printRecords();
                    System.out.println();
                    break;
                }
                case R.id.blueRightYellowChk: {
                    if (checkbox.isChecked()) allRecords.add(blueRightYellow);
                    else allRecords.remove(blueRightYellow);
                    printRecords();
                    System.out.println();
                    break;
                }
                case R.id.redLeftRedChk: {
                    if (checkbox.isChecked()) allRecords.add(redLeftRed);
                    else allRecords.remove(redLeftRed);
                    break;
                }
                case R.id.redCenterRedChk: {
                    if (checkbox.isChecked()) allRecords.add(redCenterRed);
                    else allRecords.remove(redCenterRed);
                    printRecords();
                    System.out.println();
                    break;
                }
                case R.id.redRightRedChk: {
                    if (checkbox.isChecked()) allRecords.add(redRightRed);
                    else allRecords.remove(redRightRed);
                    printRecords();
                    System.out.println();
                    break;
                }
                case R.id.blueLeftRedChk: {
                    if (checkbox.isChecked()) allRecords.add(blueLeftRed);
                    else allRecords.remove(blueLeftRed);
                    printRecords();
                    System.out.println();
                    break;
                }
                case R.id.blueCenterRedChk: {
                    if (checkbox.isChecked()) allRecords.add(blueCenterRed);
                    else allRecords.remove(blueCenterRed);
                    printRecords();
                    System.out.println();
                    break;
                }
                case R.id.blueRightRedChk: {
                    if (checkbox.isChecked()) allRecords.add(blueRightRed);
                    else allRecords.remove(blueCenterRed);
                    printRecords();
                    System.out.println();
                    break;
                }
                default:
                    System.out.println("Can't tell which checkbox was clicked.");
            }
        }
    }

    private void checkRecords() {
        System.out.println("Original Records: ");
        printRecords();
        ArrayList<Integer> timeStampRecords = new ArrayList<>();
        timeStampRecords.add(R.id.blueBoostBtn);
        timeStampRecords.add(R.id.blueForceBtn);
        timeStampRecords.add(R.id.blueLevitateBtn);
        timeStampRecords.add(R.id.blueRedSwitchOwnershipBtn);
        timeStampRecords.add(R.id.blueScaleOwnershipBtn);
        timeStampRecords.add(R.id.blueSwitchOwnershipBtn);
        timeStampRecords.add(R.id.redBoostBtn);
        timeStampRecords.add(R.id.redOwnershipScaleBtn);
        timeStampRecords.add(R.id.redLevitateBtn);
        timeStampRecords.add(R.id.redForceBtn);
        timeStampRecords.add(R.id.redSwitchOwnershipBtn);
        timeStampRecords.add(R.id.redBlueSwitchOwnershipBtn);

        ArrayList<Record> recordsToRemove = new ArrayList<>();
        for (Record r : allRecords) {
            int rID = r.getItemID();
            int index = timeStampRecords.indexOf(rID);
            if (index > 0) {
                double rValue = Double.parseDouble(r.getValue());
                if (rValue < 0.0 || rValue > MATCH_DURATION) recordsToRemove.add(r);
            }

        }
        allRecords.removeAll(recordsToRemove);
        System.out.println();
        System.out.println("Checked Records: ");
        printRecords();
    }

    private void updateRecords(double adjustment) {
        for (Record r : allRecords) {
            double rValue = Double.parseDouble(r.getValue());
            rValue += adjustment;
            r.setValue(Double.toString(rValue));
        }
        checkRecords();

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
            switch (currId) {
                case R.id.redLeftEditTxt: {
                    if (currEditText.getText().toString().length() > 0) {
                        String teamNum = currEditText.getText().toString();
                        while (teamNum.length() < teamNumLabelLength) {
                            teamNum += " ";
                        }
                        redLeftYellowLbl.setText(teamNum);
                        redLeftRedLbl.setText(teamNum);
                        teamsPlaying[0] = Integer.parseInt(teamNum.trim());
                        redLeftYellow = new Record(redLeftEditText.getText().toString(), yellowCardID);
                        redLeftRed = new Record(redLeftEditText.getText().toString(), redCardID);
                        break;
                    }
                }
                case R.id.redCenterEditTxt: {
                    if (currEditText.getText().toString().length() > 0) {
                        String teamNum = currEditText.getText().toString();
                        while (teamNum.length() < teamNumLabelLength) {
                            teamNum += " ";
                        }
                        redCenterYellowLbl.setText(teamNum);
                        redCenterRedLbl.setText(teamNum);
                        teamsPlaying[1] = Integer.parseInt(teamNum.trim());
                        redCenterYellow = new Record(redCenterEditText.getText().toString(), yellowCardID);
                        redCenterRed = new Record(redCenterEditText.getText().toString(), redCardID);
                        break;
                    }
                }
                case R.id.redRightEditTxt: {
                    if (currEditText.getText().toString().length() > 0) {
                        String teamNum = currEditText.getText().toString();
                        while (teamNum.length() < teamNumLabelLength) {
                            teamNum += " ";
                        }
                        redRightYellowLbl.setText(teamNum);
                        redRightRedLbl.setText(teamNum);
                        teamsPlaying[2] = Integer.parseInt(teamNum.trim());
                        redRightYellow = new Record(redRightEditText.getText().toString(), yellowCardID);
                        redRightRed = new Record(redRightEditText.getText().toString(), redCardID);
                        break;
                    }
                }
                case R.id.blueLeftEditTxt: {
                    if (currEditText.getText().toString().length() > 0) {
                        String teamNum = currEditText.getText().toString();
                        while (teamNum.length() < teamNumLabelLength) {
                            teamNum += " ";
                        }
                        blueLeftYellowLbl.setText(teamNum);
                        blueLeftRedLbl.setText(teamNum);
                        teamsPlaying[3] = Integer.parseInt(teamNum.trim());
                        blueLeftYellow = new Record(blueLeftEditText.getText().toString(), yellowCardID);
                        blueLeftRed = new Record(blueLeftEditText.getText().toString(), redCardID);
                        break;
                    }
                }
                case R.id.blueCenterEditTxt: {
                    if (currEditText.getText().toString().length() > 0) {
                        String teamNum = currEditText.getText().toString();
                        while (teamNum.length() < teamNumLabelLength) {
                            teamNum += " ";
                        }
                        blueCenterYellowLbl.setText(teamNum);
                        blueCenterRedLbl.setText(teamNum);
                        teamsPlaying[4] = Integer.parseInt(teamNum.trim());
                        blueCenterYellow = new Record(blueCenterEditText.getText().toString(), yellowCardID);
                        blueCenterRed = new Record(blueCenterEditText.getText().toString(), redCardID);
                        break;
                    }
                }
                case R.id.blueRightEditTxt: {
                    if (currEditText.getText().toString().length() > 0) {
                        String teamNum = currEditText.getText().toString();
                        while (teamNum.length() < teamNumLabelLength) {
                            teamNum += " ";
                        }
                        blueRightYellowLbl.setText(teamNum);
                        blueRightRedLbl.setText(teamNum);
                        teamsPlaying[5] = Integer.parseInt(teamNum.trim());
                        blueRightYellow = new Record(blueRightEditText.getText().toString(), yellowCardID);
                        blueRightRed = new Record(blueRightEditText.getText().toString(), redCardID);
                        break;
                    }
                }
                case R.id.redScoreEditTxt: {
                    if (currEditText.getText().toString().length() > 0) {
                        String redScore = currEditText.getText().toString();
                        redAllianceScore = new Record(redScore, OverallForm.Items.RED_SCORE.getId());
                        allRecords.add(redAllianceScore);
                    }
                    break;
                }
                case R.id.blueScoreEditTxt: {
                    if (currEditText.getText().toString().length() > 0) {
                        String blueScore = currEditText.getText().toString();
                        blueAllianceScore = new Record(blueScore, OverallForm.Items.BLUE_SCORE.getId());
                        allRecords.add(blueAllianceScore);
                    }
                    break;
                }
                case R.id.redFoulEditTxt: {
                    if (currEditText.getText().toString().length() > 0) {
                        String redFoul = currEditText.getText().toString();
                        redFoulPts = new Record(redFoul, OverallForm.Items.RED_FOUL_POINTS.getId());
                        allRecords.add(redFoulPts);
                    }
                    break;
                }
                case R.id.blueFoulEditTxt: {
                    if (currEditText.getText().toString().length() > 0) {
                        String blueFoul = currEditText.getText().toString();
                        blueFoulPts = new Record(blueFoul, OverallForm.Items.BLUE_FOUL_POINTS.getId());
                        allRecords.add(blueFoulPts);
                    }
                    break;
                }
                case R.id.scoutNameEditTxt: {
                    if (currEditText.getText().toString().length() > 0) {
                        scoutName = currEditText.getText().toString();
                        System.out.println("Set Scout Name: " + scoutName);
                    }
                    break;
                }
                case R.id.matchNumEditTxt: {
                    if (currEditText.getText().toString().length() > 0)
                        matchNum = Integer.parseInt(currEditText.getText().toString());
                    break;
                }
                default:
                    System.out.println("ERROR.");
            }
        }
    }

    @Override
    /**
     * Initialize the configurations for the app from the configuration file.
     * Configurations include:
     *  List of scout names
     *  List of team numbers
     *  Tablet number
     *  PC companion
     */
    void initConfigs() {
        if (tabletNumFile.exists()) System.out.println("tabletNumFile exists now");
        else System.out.println("tabletNumFile still does not exist"); 
        names = getResources().getStringArray(R.array.scout_names);
        teams = getResources().getStringArray(R.array.team_numbers);
        pcCompanion = getResources().getString(R.string.pc_companion);
        tabletNum = readTabletNumber();
        if (tabletNum == -1) showTabletNumDialog();
    }

    private void showTabletNumDialog() {
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
    /**
     * Initializes the record objects.
     */
    void initRecords() {

    }

    /**
     * Initializes the layout.
     * Initializes objects for the visual components.
     */
    @Override
    void initLayout() {

        // Linking objects to visual elements
        // Basics section
        scoutNameEditText = (AutoCompleteTextView) findViewById(R.id.scoutNameEditTxt);
        ArrayAdapter<String> scoutNameAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, names);
        scoutNameEditText.setAdapter(scoutNameAdapter);
        matchNumEditText = (AutoCompleteTextView) findViewById(R.id.matchNumEditTxt);
        ArrayAdapter<String> teamNumAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, teams);
        redLeftEditText = (AutoCompleteTextView) findViewById(R.id.redLeftEditTxt);
        redLeftEditText.setAdapter(teamNumAdapter);
        redCenterEditText = (AutoCompleteTextView) findViewById(R.id.redCenterEditTxt);
        redCenterEditText.setAdapter(teamNumAdapter);
        redRightEditText = (AutoCompleteTextView) findViewById(R.id.redRightEditTxt);
        redRightEditText.setAdapter(teamNumAdapter);
        blueLeftEditText = (AutoCompleteTextView) findViewById(R.id.blueLeftEditTxt);
        blueLeftEditText.setAdapter(teamNumAdapter);
        blueCenterEditText = (AutoCompleteTextView) findViewById(R.id.blueCenterEditTxt);
        blueCenterEditText.setAdapter(teamNumAdapter);
        blueRightEditText = (AutoCompleteTextView) findViewById(R.id.blueRightEditTxt);
        blueRightEditText.setAdapter(teamNumAdapter);
        formsPendingLbl = (TextView) findViewById(R.id.formsPendingLbl);
        // Game play section
        redScaleOwnershipBtn = (Button) findViewById(R.id.redOwnershipScaleBtn);
        redSwitchOwnershipBtn = (Button) findViewById(R.id.redSwitchOwnershipBtn);
        redBlueSwitchOwnershipBtn = (Button) findViewById(R.id.redBlueSwitchOwnershipBtn);
        redLevitateBtn = (Button) findViewById(R.id.redLevitateBtn);
        redForceBtn = (Button) findViewById(R.id.redForceBtn);
        redBoostBtn = (Button) findViewById(R.id.redBoostBtn);
        blueScaleOwnershipBtn = (Button) findViewById(R.id.blueScaleOwnershipBtn);
        blueSwitchOwnershipBtn = (Button) findViewById(R.id.blueSwitchOwnershipBtn);
        blueRedSwitchOwnershipBtn = (Button) findViewById(R.id.blueRedSwitchOwnershipBtn);
        blueLevitateBtn = (Button) findViewById(R.id.blueLevitateBtn);
        blueForceBtn = (Button) findViewById(R.id.blueForceBtn);
        blueBoostBtn = (Button) findViewById(R.id.blueBoostBtn);
        // Penalties
        redLeftYellowLbl = (CheckBox) findViewById(R.id.redLeftYellowChk);
        redCenterYellowLbl = (CheckBox) findViewById(R.id.redCenterYellowChk);
        redRightYellowLbl = (CheckBox) findViewById(R.id.redRightYellowChk);
        blueLeftYellowLbl = (CheckBox) findViewById(R.id.blueLeftYellowChk);
        blueCenterYellowLbl = (CheckBox) findViewById(R.id.blueCenterYellowChk);
        blueRightYellowLbl = (CheckBox) findViewById(R.id.blueRightYellowChk);
        redLeftRedLbl = (CheckBox) findViewById(R.id.redLeftRedChk);
        redCenterRedLbl = (CheckBox) findViewById(R.id.redCenterRedChk);
        redRightRedLbl = (CheckBox) findViewById(R.id.redRightRedChk);
        blueLeftRedLbl = (CheckBox) findViewById(R.id.blueLeftRedChk);
        blueCenterRedLbl = (CheckBox) findViewById(R.id.blueCenterRedChk);
        blueRightRedLbl = (CheckBox) findViewById(R.id.blueRightRedChk);
        // Points
        redFoulPoints = (EditText) findViewById(R.id.redFoulEditTxt);
        blueFoulPoints = (EditText) findViewById(R.id.blueFoulEditTxt);
        redScoreEditText = (EditText) findViewById(R.id.redScoreEditTxt);
        blueScoreEditText = (EditText) findViewById(R.id.blueScoreEditTxt);
        saveBtn = (Button) findViewById(R.id.saveFormBtn);
        transferBtn = (Button) findViewById(R.id.transferFormBtn);
        viewTimelineBtn = (Button) findViewById(R.id.viewTimelineBtn);
        // Timer
        startTimerBtn = (Button) findViewById(R.id.startTimerBtn);
        addTimerBtn = (Button) findViewById(R.id.addTimerBtn);
        subtractTimerBtn = (Button) findViewById(R.id.subtractTimerBtn);

        EditTextWatcher textWatcher = new EditTextWatcher();

        redLeftEditText.addTextChangedListener(textWatcher);
        redCenterEditText.addTextChangedListener(textWatcher);
        redRightEditText.addTextChangedListener(textWatcher);
        blueLeftEditText.addTextChangedListener(textWatcher);
        blueCenterEditText.addTextChangedListener(textWatcher);
        blueRightEditText.addTextChangedListener(textWatcher);
        matchNumEditText.addTextChangedListener(textWatcher);
        scoutNameEditText.addTextChangedListener(textWatcher);
        redScoreEditText.addTextChangedListener(textWatcher);
        blueScoreEditText.addTextChangedListener(textWatcher);
        redFoulPoints.addTextChangedListener(textWatcher);
        blueFoulPoints.addTextChangedListener(textWatcher);

        CheckBoxChangeListener checkBoxHandler = new CheckBoxChangeListener();
        redLeftYellowLbl.setOnCheckedChangeListener(checkBoxHandler);
        redCenterYellowLbl.setOnCheckedChangeListener(checkBoxHandler);
        redRightYellowLbl.setOnCheckedChangeListener(checkBoxHandler);
        blueLeftYellowLbl.setOnCheckedChangeListener(checkBoxHandler);
        blueCenterYellowLbl.setOnCheckedChangeListener(checkBoxHandler);
        blueRightYellowLbl.setOnCheckedChangeListener(checkBoxHandler);
        redLeftRedLbl.setOnCheckedChangeListener(checkBoxHandler);
        redCenterRedLbl.setOnCheckedChangeListener(checkBoxHandler);
        redRightRedLbl.setOnCheckedChangeListener(checkBoxHandler);
        blueLeftRedLbl.setOnCheckedChangeListener(checkBoxHandler);
        blueCenterRedLbl.setOnCheckedChangeListener(checkBoxHandler);
        blueRightRedLbl.setOnCheckedChangeListener(checkBoxHandler);

        ButtonListener buttonListener = new ButtonListener();
        startTimerBtn.setOnClickListener(buttonListener);
        addTimerBtn.setOnClickListener(buttonListener);
        subtractTimerBtn.setOnClickListener(buttonListener);
        redScaleOwnershipBtn.setOnClickListener(buttonListener);
        redSwitchOwnershipBtn.setOnClickListener(buttonListener);
        redBlueSwitchOwnershipBtn.setOnClickListener(buttonListener);
        redBoostBtn.setOnClickListener(buttonListener);
        redForceBtn.setOnClickListener(buttonListener);
        redLevitateBtn.setOnClickListener(buttonListener);
        blueScaleOwnershipBtn.setOnClickListener(buttonListener);
        blueSwitchOwnershipBtn.setOnClickListener(buttonListener);
        blueRedSwitchOwnershipBtn.setOnClickListener(buttonListener);
        blueForceBtn.setOnClickListener(buttonListener);
        blueLevitateBtn.setOnClickListener(buttonListener);
        blueBoostBtn.setOnClickListener(buttonListener);
        saveBtn.setOnClickListener(buttonListener);
        transferBtn.setOnClickListener(buttonListener);
        viewTimelineBtn.setOnClickListener(buttonListener);

        for (int i = 0; i < invalidTeamNums.length; i++)
        {
            invalidTeamNums[i] = -1;
        }

        startTimerBtn.setText("Start Timer");
    }

    /**
     * Fail-safe in case the user accidentally closes the form. Initializes the save state. Restores fields for the Report.
     * 1. Make sure STATE_SAVE_FILE exists.
     * 2. Read from STATE_SAVE_FILE.
     * 3. Restore the team numbers.
     * 4. Restore the match number.
     * 5. Restore the scout name.
     */
    @Override
    void initSaveState() {
        File saveStateFile = new File(getFilesDir().getAbsolutePath(), STATE_SAVE_FILE);
        if (saveStateFile.exists())
        {
            System.out.println("READING FROM STATE SAVE FILE");
            try {
                BufferedReader saveStateReader = new BufferedReader(new FileReader(saveStateFile));
                String formsPendingNum = saveStateReader.readLine();
                System.out.println("formsPendingNum: " + formsPendingNum);
                String teamNumbers = saveStateReader.readLine();
                System.out.println("teamNumbers: " + teamNumbers);
                String storedMatchNum = saveStateReader.readLine();
                System.out.println("storedMatchNum: " + storedMatchNum);
                String storedScoutName = saveStateReader.readLine();
                System.out.println("storedScoutName: " + storedScoutName);
                String storedRecords = saveStateReader.readLine();
                System.out.println("storedRecords: " + storedRecords);
                String savedStartTime = saveStateReader.readLine();
                String savedEndTime = saveStateReader.readLine();
                String savedCurrTime = saveStateReader.readLine();
                formsPending = Integer.parseInt(formsPendingNum);
                formsPendingLbl.setText(formsPendingNum + " Forms Pending");
                String[] teamsArr = teamNumbers.split("\\|");
                for (int i = 0; i < teamsArr.length; i++)
                {
                    teamsPlaying[i] = Integer.parseInt(teamsArr[i]);
                }
                System.out.print("TEAMS: ");
                for (int team : teamsPlaying)
                {
                    System.out.print(team + "|");
                }
                redLeftEditText.requestFocus();
                redLeftEditText.setText(Integer.toString(teamsPlaying[0]));
                redCenterEditText.requestFocus();
                redCenterEditText.setText(Integer.toString(teamsPlaying[1]));
                redRightEditText.requestFocus();
                redRightEditText.setText(Integer.toString(teamsPlaying[2]));
                blueLeftEditText.requestFocus();
                blueLeftEditText.setText(Integer.toString(teamsPlaying[3]));
                blueCenterEditText.requestFocus();
                blueCenterEditText.setText(Integer.toString(teamsPlaying[4]));
                blueRightEditText.requestFocus();
                blueRightEditText.setText(Integer.toString(teamsPlaying[5]));
                matchNum = Integer.parseInt(storedMatchNum);
                matchNumEditText.requestFocus();
                matchNumEditText.setText(Integer.toString(matchNum));
                scoutName = storedScoutName;
                scoutNameEditText.setText(scoutName);
                String[] records = storedRecords.split("\\|");
                System.out.println("STORED RECORDS: ");
                for (String record : records)
                {
                    System.out.println(record);
                }
                setState(records, 0);
                globalStartTime = Long.parseLong(savedStartTime);
                globalEndTime = Long.parseLong(savedEndTime);
                globalCurrentTime = Long.parseLong(savedCurrTime);
                updateTimerText();
              //  timerHandler.postDelayed(updateTimerThread, 0);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles consequences of a "YES" selection in a dialog box.
     */
    @Override
    void executeRequest() {
        /**
         * Consider switch(actionRequested):
         * 1) SAVE_FORM --> call saveForm(), create toast, increment forms pending... (copied from 2017)
         * if (readyToSave())
         */

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
                    System.out.println("Transferring all forms.");
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
                checkInvalidTeams();
                String message = "The following team numbers are invalid: ";
                for (int i = 0; i < invalidTeamNums.length; i++)
                {
                    if (invalidTeamNums[i] != -1)
                    {
                        message += invalidTeamNums+"\n";
                        resetInvalidEditTexts(i);
                    }
                }

                break;
        }
        actionRequested = Action.NONE;

    }

    private boolean checkInvalidTeams()
    {
        boolean foundInvalidTeam = false;
        for (int i = 0; i < teamsPlaying.length; i++)
        {
            int index = getTeamIndex(teamsPlaying[i]);
            System.out.println("Found team #" + teamsPlaying[i] + " at index = " + index+".");
            if (index == -1)
            {
                invalidTeamNums[i] = teamsPlaying[i];
                foundInvalidTeam = true;
            }

            System.out.print("invalidTeamNums at this point: ");
            for (int j = 0; j < invalidTeamNums.length - 1; j++)
            {
                System.out.print(invalidTeamNums[j] + ",");
            }
            System.out.println(invalidTeamNums[invalidTeamNums.length -1]);
        }
        return foundInvalidTeam;
    }
    private void resetInvalidEditTexts(int index)
    {
        final String INVALID_MESSAGE = "INVALID";
        switch (index)
        {
            case 0:
                redLeftEditText.setText(INVALID_MESSAGE);
                break;
            case 1:
                redCenterEditText.setText(INVALID_MESSAGE);
                break;
            case 2:
                redRightEditText.setText(INVALID_MESSAGE);
                break;
            case 3:
                blueLeftEditText.setText(INVALID_MESSAGE);
                break;
            case 4:
                blueCenterEditText.setText(INVALID_MESSAGE);
                break;
            case 5:
                blueRightEditText.setText(INVALID_MESSAGE);
                break;
            case 6:
                scoutNameEditText.setText(INVALID_MESSAGE);
                break;
            case 7:
                matchNumEditText.setText(INVALID_MESSAGE);
        }
    }

    private int getTeamIndex(int teamNumber)
    {
        String teamNum = Integer.toString(teamNumber);
        for (int i = 0; i < teams.length; i++) {
            if (teams[i].equals(teamNum)) return i;
        }
        return -1;
    }

    /**
     * Creates the saved state.
     */
    @Override
    void saveState() {

        /**
         * 1. Create STATE_SAVE_FILE (make sure it exists).
         * 2. Write to the STATE_SAVE_FILE w/ the following:
         * - Store the # of pending forms.
         * - Store all team numbers.
         * - Store the match number.
         * - Store the scout name.
         * - Store records for this form.
         */

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
        if (saveStateFile.exists())
        {
            System.out.println("saveStateFile exists.");
            String saveStateStr = "";
            saveStateStr += Integer.toString(formsPending) + "\n";
            for (int i = 0; i < teamsPlaying.length - 1; i++)
            {
                saveStateStr += teamsPlaying[i] + "|";
            }
            saveStateStr += teamsPlaying[teamsPlaying.length-1] + "\n";
            saveStateStr += Integer.toString(matchNum) + "\n";
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
            printRecords();
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

    /**
     * Reset the form after the save button is pressed.
     * 1. Clear the edit texts (all of them).
     * 2. Reset the checkboxes.
     * 3. Clear the allRecords arrayList.
     * 4. Reset the timer to 0.
     * 5. Set the text of the timer to Start Timer.
     * 6. Increment the matchNumEditText.
     */
    @Override
    void resetForm() {
        resetCheckboxes();
        resetEditTexts();
        allRecords = new ArrayList<Record>();
        rawTimestampRecords = new ArrayList<>();
        resetTimer();
        matchNum++;
        matchNumEditText.setText(Integer.toString(matchNum));
    }

    private void resetTimer() {
        globalStartTime = SystemClock.uptimeMillis();
        globalEndTime = globalStartTime + MATCH_DURATION*1000;
        globalCurrentTime = SystemClock.uptimeMillis();
        startTimerBtn.setText("Start Timer");
    }

    @Override
    void resetRadioGroups() {

    }

    @Override
    void resetCheckboxes() {
        redLeftYellowLbl.setChecked(false);
        redLeftYellowLbl.setText("Red Left   ");
        redLeftRedLbl.setChecked(false);
        redLeftRedLbl.setText("Red Left   ");
        redCenterYellowLbl.setChecked(false);
        redCenterYellowLbl.setText("Red Center ");
        redCenterRedLbl.setChecked(false);
        redCenterRedLbl.setText("Red Center ");
        redRightYellowLbl.setChecked(false);
        redRightYellowLbl.setText("Red Right  ");
        redRightRedLbl.setText("Red Right  ");
        redRightRedLbl.setChecked(false);
        blueLeftYellowLbl.setChecked(false);
        blueLeftYellowLbl.setText("Blue Left  ");
        blueLeftRedLbl.setChecked(false);
        blueLeftRedLbl.setText("Blue Left  ");
        blueCenterYellowLbl.setChecked(false);
        blueCenterYellowLbl.setText("Blue Center");
        blueCenterRedLbl.setText("Blue Center");
        blueCenterRedLbl.setChecked(false);
        blueRightYellowLbl.setChecked(false);
        blueRightYellowLbl.setText("Blue Right ");
        blueRightRedLbl.setChecked(false);
        blueRightRedLbl.setText("Blue Right ");
    }

    void resetEditTexts()
    {
        redLeftEditText.setText("");
        redCenterEditText.setText("");
        redRightEditText.setText("");
        blueLeftEditText.setText("");
        blueCenterEditText.setText("");
        blueRightEditText.setText("");
        redScoreEditText.setText("");
        blueScoreEditText.setText("");
        redFoulPoints.setText("");
        blueFoulPoints.setText("");

    }

    public Form makeForm()
    {
        OverallForm form = new OverallForm(tabletNum, teamsPlaying, Integer.parseInt(matchNumEditText.getText().toString()), scoutNameEditText.getText().toString());
        form.addRecords(allRecords);
        return form;
    }

    @Override
    /**
     * Confirm if a form is ready to be saved.
     * 1. Check that there are 6 team numbers, and that the teams are in the list of teams. --> set global variable for the invalid team number (or team number(s)) if it is invalid?
     * 2. Check that there is a scout name, and that it is on the list of scout names.
     * 3. Check that there is a match number (MN > 0).
     * 4. Check if the timer is done (2:30). [maybe]
     */
    boolean readyToSave() {
        if (checkInvalidTeams() || checkInvalidScoutName() || matchNum < 0 || globalCurrentTime > globalEndTime) return false;
        return true;
    }

    private int findScoutNameIndex()
    {
        System.out.print("Scout Names: ");
        for (int i = 0; i < names.length - 1; i++)
        {
            System.out.print(names[i] + ", ");
        }
        System.out.println(names[names.length-1]);

        int index = 0;
        boolean found = false;
        while (!found && index < names.length)
        {
            found = names[index].equals(scoutName);
            if (found)
            {
                System.out.println("Found " + scoutName + "at index = " + index);
                return index;
            }
            else index++;
        }
        return -1;
    }

    private boolean checkInvalidScoutName()
    {
        boolean foundInvalidScout = false;
        foundInvalidScout = (findScoutNameIndex() == -1);
        System.out.println("foundInvalidScout: " + foundInvalidScout);
        return foundInvalidScout;
    }

    @Override
    void setState(String[] records, int startingIndex) {

        for (String record : records)
        {
            String[] recordAndVal = record.split("\\,");
            if (recordAndVal.length == 2)
            {
                Record currRec = new Record(recordAndVal[1], Integer.parseInt(recordAndVal[0]));
                allRecords.add(currRec);

                int itemID = currRec.getItemID();
                String val = currRec.getValue();
                final int YELLOW_CARD_ID = OverallForm.Items.YELLOW_CARD.getId();
                final int RED_CARD_ID = OverallForm.Items.RED_CARD.getId();
                if (itemID == OverallForm.Items.YELLOW_CARD.getId())
                {
                    if (Integer.parseInt(val) == teamsPlaying[0]) redLeftYellowLbl.setChecked(true);
                    else if (Integer.parseInt(val) == teamsPlaying[1]) redCenterYellowLbl.setChecked(true);
                    else if (Integer.parseInt(val) == teamsPlaying[2]) redRightYellowLbl.setChecked(true);
                    else if (Integer.parseInt(val) == teamsPlaying[3]) blueLeftYellowLbl.setChecked(true);
                    else if (Integer.parseInt(val) == teamsPlaying[4]) blueCenterYellowLbl.setChecked(true);
                    else if (Integer.parseInt(val) == teamsPlaying[5]) blueRightYellowLbl.setChecked(true);
                    else System.out.println("ERROR: CANNOT DETERMINE TEAM.");
                }
                else if (itemID == OverallForm.Items.RED_CARD.getId())
                {
                    if (Integer.parseInt(val) == teamsPlaying[0]) redLeftRedLbl.setChecked(true);
                    else if (Integer.parseInt(val) == teamsPlaying[1]) redCenterRedLbl.setChecked(true);
                    else if (Integer.parseInt(val) == teamsPlaying[2]) redRightRedLbl.setChecked(true);
                    else if (Integer.parseInt(val) == teamsPlaying[3]) blueLeftRedLbl.setChecked(true);
                    else if (Integer.parseInt(val) == teamsPlaying[4]) blueCenterRedLbl.setChecked(true);
                    else if (Integer.parseInt(val) == teamsPlaying[5]) blueRightRedLbl.setChecked(true);
                    else System.out.println("ERROR: CANNOT DETERMINE TEAM.");
                }
                else if (itemID == OverallForm.Items.RED_SCORE.getId())
                {
                    redScoreEditText.setText(val);
                }
                else if (itemID == OverallForm.Items.BLUE_SCORE.getId())
                {
                    blueScoreEditText.setText(val);
                }
                else if (itemID == OverallForm.Items.RED_FOUL_POINTS.getId())
                {
                    redFoulPoints.setText(val);
                }
                else if (itemID == OverallForm.Items.BLUE_FOUL_POINTS.getId())
                {
                    blueFoulPoints.setText(val);
                }
                else System.out.println("ERROR: CANNOT DETERMINE THE ID OF THE RECORD.");
            }
        }


//            switch (itemID)
//            {
//                case YELLOW_CARD_ID: {
//                    switch (val) {
//                        case teamsPlaying[0]:
//                            redLeftYellowLbl.setChecked(true);
//                            break;
//                        case teamsPlaying[1]:
//                            redCenterYellowLbl.setChecked(true);
//                            break;
//                        case teamsPlaying[2]:
//                            redRightYellowLbl.setChecked(true);
//                            break;
//                        case teamsPlaying[3]:
//                            blueLeftYellowLbl.setChecked(true);
//                            break;
//                        case teamsPlaying[4]:
//                            blueCenterYellowLbl.setChecked(true);
//                            break;
//                        case teamsPlaying[5]:
//                            blueCenterRedLbl.setChecked(true);
//                    }
//                }
//                case RED_CARD_ID:
//                {
//                    switch(val)
//                    {
//                        case teamsPlaying[0]:
//                            redLeftRedLbl.setChecked(true);
//                            break;
//                        case teamsPlaying[1]:
//                            redCenterRedLbl.setChecked(true);
//                            break;
//                        case teamsPlaying[2]:
//                            redRightRedLbl.setChecked(true);
//                            break;
//                        case teamsPlaying[3]:
//                            blueLeftRedLbl.setChecked(true);
//                            break;
//                        case teamsPlaying[4]:
//                            blueCenterRedLbl.setChecked(true);
//                            break;
//                        case teamsPlaying[5]:
//                            blueRightRedLbl.setChecked(true);
//                    }
//                }
//                case OverallForm.Items.RED_SCORE.getId():
//                {
//                    redScoreEditText.setText(Integer.toString(val));
//                    break;
//                }
//                case OverallForm.Items.BLUE_SCORE.getId():
//                {
//                    blueScoreEditText.setText(Integer.toString(val));
//                    break;
//                }
//                case OverallForm.Items.RED_FOUL_POINTS.getId():
//                {
//                    redFoulPoints.setText(Integer.toString(val));
//                    break;
//                }
//                case OverallForm.Items.BLUE_FOUL_POINTS.getId():
//                {
//                    blueFoulPoints.setText(Integer.toString(val));
//                    break;
//                }
//                default: System.out.println("ERROR: CANNOT DETERMINE RECORD ID.");
//            }
    }
}

