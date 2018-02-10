package shreya.overallscoutingform2018;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class FormActivity extends AppCompatActivity {

    // Objects for visual elements
    // Basics visual elements - object declarations
    EditText scoutName;
    EditText matchNum;
    EditText redLeft;
    EditText redCenter;
    EditText redRight;
    EditText blueLeft;
    EditText blueCenter;
    EditText blueRight;
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
    EditText redScore;
    EditText blueScore;
    Button saveBtn;
    Button transferBtn;
    Button viewTimelineBtn;
    Button startTimerBtn;
    Button addTimerBtn;
    Button subtractTimerBtn;
    final int teamNumLabelLength = 11;

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
        startTimerBtn.setText("" + mins + ":" +
                String.format("%02d", secs));
    }

    ArrayList<Record> allRecords = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        // Linking objects to visual elements
        // Basics section
        scoutName = (EditText) findViewById(R.id.scoutNameEditTxt);
        matchNum = (EditText) findViewById(R.id.matchNumEditTxt);
        redLeft = (EditText) findViewById(R.id.redLeftEditTxt);
        redCenter = (EditText) findViewById(R.id.redCenterEditTxt);
        redRight = (EditText) findViewById(R.id.redRightEditTxt);
        blueLeft = (EditText) findViewById(R.id.blueLeftEditTxt);
        blueCenter = (EditText) findViewById(R.id.blueCenterEditTxt);
        blueRight = (EditText) findViewById(R.id.blueRightEditTxt);
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
        redScore = (EditText) findViewById(R.id.redScoreEditTxt);
        blueScore = (EditText) findViewById(R.id.blueScoreEditTxt);
        saveBtn = (Button) findViewById(R.id.saveFormBtn);
        transferBtn = (Button) findViewById(R.id.transferFormBtn);
        viewTimelineBtn = (Button) findViewById(R.id.viewTimelineBtn);
        // Timer
        startTimerBtn = (Button) findViewById(R.id.startTimerBtn);
        addTimerBtn = (Button) findViewById(R.id.addTimerBtn);
        subtractTimerBtn = (Button) findViewById(R.id.subtractTimerBtn); 

        EditTextWatcher textWatcher = new EditTextWatcher();

        redLeft.addTextChangedListener(textWatcher);
        redCenter.addTextChangedListener(textWatcher);
        redRight.addTextChangedListener(textWatcher);
        blueLeft.addTextChangedListener(textWatcher);
        blueCenter.addTextChangedListener(textWatcher);
        blueRight.addTextChangedListener(textWatcher);

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

    }

    private class ButtonListener implements View.OnClickListener
    {

        @Override
        public void onClick(View view) {
            Button currButton = (Button) view;
            int currId = currButton.getId();
            switch (currId)
            {
                case R.id.startTimerBtn:
                {
                    System.out.println("Start timer button pressed.");
                    startTime = SystemClock.uptimeMillis();
                    adjustment = 0;
                    timerHandler.postDelayed(updateTimerThread, 0);
                    break;
                }
                case R.id.addTimerBtn:
                {
                    System.out.println("Increased timer.");
                    adjustment += 1000;
                    updateTimerText();
                    checkRecords();
                    System.out.println("Time: " + timeInMilliseconds);
                    break;
                }
                case R.id.subtractTimerBtn:
                {
                    System.out.println("Decreased timer.");
                    adjustment -= 2000;
                    updateTimerText();
                    checkRecords();
                    System.out.println("Time: " + timeInMilliseconds);
                    break;
                }
                case R.id.redOwnershipScaleBtn:
                {
                    System.out.println("Red alliance gained ownership of the scale.");
                    Record redScale = new Record(Double.toString((timeInMilliseconds+adjustment)/1000), OverallForm.Items.RED_OWNS_SCALE.getId());
                    allRecords.add(redScale);
                    System.out.println(redScale);
                    break;
                }
                case R.id.redSwitchOwnershipBtn:
                {
                    System.out.println("Red alliance gained ownership of red switch.");
                    Record redSwitch = new Record(Double.toString((timeInMilliseconds + adjustment)/1000), OverallForm.Items.RED_OWNS_RED_SWITCH.getId());
                    allRecords.add(redSwitch);
                    System.out.println(redSwitch);
                    break;
                }
                case R.id.redBlueSwitchOwnershipBtn:
                {
                    System.out.println("Red alliance gained ownership of the blue switch.");
                    Record redBlueSwitch = new Record(Double.toString((timeInMilliseconds + adjustment)/1000), OverallForm.Items.RED_OWNS_BLUE_SWITCH.getId());
                    allRecords.add(redBlueSwitch);
                    System.out.println(redBlueSwitch);
                    break;
                }
                case R.id.redBoostBtn:
                {
                    System.out.println("Red alliance used boost.");
                    Record redBoost = new Record(Double.toString((timeInMilliseconds + adjustment)/1000), OverallForm.Items.RED_USED_BOOST.getId());
                    allRecords.add(redBoost);
                    System.out.println(redBoost);
                    break;
                }
                case R.id.redForceBtn:
                {
                    System.out.println("Red alliance used force.");
                    Record redForce = new Record(Double.toString((timeInMilliseconds + adjustment)/1000), OverallForm.Items.RED_USED_FORCE.getId());
                    allRecords.add(redForce);
                    System.out.println(redForce);
                    break;
                }
                case R.id.redLevitateBtn:
                {
                    System.out.println("Red alliance used levitate.");
                    Record redLevitate = new Record(Double.toString((timeInMilliseconds + adjustment)/1000), OverallForm.Items.RED_USED_LEVITATE.getId());
                    allRecords.add(redLevitate);
                    System.out.println(redLevitate);
                    break;
                }
                case R.id.blueScaleOwnershipBtn:
                {
                    System.out.println("Blue alliance gained ownership of the scale.");
                    Record blueScale = new Record(Double.toString((timeInMilliseconds + adjustment)/1000), OverallForm.Items.BLUE_OWNS_SCALE.getId());
                    allRecords.add(blueScale);
                    System.out.println(blueScale);
                    break;
                }
                case R.id.blueSwitchOwnershipBtn:
                {
                    System.out.println("Blue alliance gained ownership of blue switch.");
                    Record blueSwitch = new Record(Double.toString((timeInMilliseconds + adjustment)/1000), OverallForm.Items.BLUE_OWNS_BLUE_SWITCH.getId());
                    allRecords.add(blueSwitch);
                    System.out.println(blueSwitch);
                    break;
                }
                case R.id.blueRedSwitchOwnershipBtn:
                {
                    System.out.println("Blue alliance gained ownership of red switch.");
                    Record blueRedSwitch = new Record(Double.toString((timeInMilliseconds + adjustment)/1000), OverallForm.Items.BLUE_OWNS_RED_SWITCH.getId());
                    allRecords.add(blueRedSwitch);
                    System.out.println(blueRedSwitch);
                    break;
                }
                case R.id.blueBoostBtn:
                {
                    System.out.println("Blue used boost");
                    Record blueBoost = new Record(Double.toString((timeInMilliseconds + adjustment)/1000), OverallForm.Items.BLUE_USED_BOOST.getId());
                    allRecords.add(blueBoost);
                    System.out.println(blueBoost);
                    break;
                }
                case R.id.blueForceBtn:
                {
                    System.out.println("Blue used force");
                    Record blueForce 
                    break;
                }
                case R.id.blueLevitateBtn:
                {
                    System.out.println("Blue used levitate");
                    break;
                }
                case R.id.saveFormBtn:
                {
                    System.out.println("Attempting to save form");
                    startTimerBtn.setText("START TIMER");
                    startTime = 0;
                    timeInMilliseconds = 0;
                    break;
                }
                case R.id.transferFormBtn:
                {
                    System.out.println("Attempting to transfer form");
                    break;
                }
                case R.id.viewTimelineBtn:
                {
                    System.out.println("Attempting to view timeline");
                    break;
                }
            }
        }
    }

    private class CheckBoxChangeListener implements CompoundButton.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(CompoundButton c, boolean b) {

            CheckBox checkbox = (CheckBox) c;
            int currId = checkbox.getId();
            int yellowCardID = OverallForm.Items.YELLOW_CARD.getId();
            int redCardID = OverallForm.Items.RED_CARD.getId();
            if (!checkbox.isChecked())
            {
                switch (currId)
                {
                    case R.id.redLeftYellowChk:
                    {
                        Record redLeftYellow = new Record(redLeft.getText().toString(), yellowCardID);
                        System.out.println(redLeftYellow);
                        allRecords.add(redLeftYellow);
                        break;
                    }
                    case R.id.redCenterYellowChk:
                    {
                        Record redCenterYellow = new Record(redCenter.getText().toString(), yellowCardID);
                        System.out.println(redCenterYellow);
                        allRecords.add(redCenterYellow);
                        break;
                    }
                    case R.id.redRightYellowChk:
                    {
                        Record redRightYellow = new Record(redRight.getText().toString(), yellowCardID);
                        System.out.println(redRightYellow);
                        allRecords.add(redRightYellow);
                        break;
                    }
                    case R.id.blueLeftYellowChk:
                    {
                        Record blueLeftYellow = new Record(blueLeft.getText().toString(), yellowCardID);
                        System.out.println(blueLeftYellow);
                        allRecords.add(blueLeftYellow);
                        break;
                    }
                    case R.id.blueCenterYellowChk:
                    {
                        Record blueCenterYellow = new Record(blueCenter.getText().toString(), yellowCardID);
                        System.out.println(blueCenterYellow);
                        allRecords.add(blueCenterYellow);
                        break;
                    }
                    case R.id.blueRightYellowChk:
                    {
                        Record blueRightYellow = new Record(blueRight.getText().toString(), yellowCardID);
                        System.out.println(blueRightYellow);
                        allRecords.add(blueRightYellow);
                        break;
                    }
                    case R.id.redLeftRedChk:
                    {
                        Record redLeftRed = new Record(redLeft.getText().toString(), redCardID);
                        System.out.println(redLeftRed);
                        allRecords.add(redLeftRed);
                        break;
                    }
                    case R.id.redCenterRedChk:
                    {
                        Record redCenterRed = new Record(redCenter.getText().toString(), redCardID);
                        System.out.println(redCenterRed);
                        allRecords.add(redCenterRed);
                        break;
                    }
                    case R.id.redRightRedChk:
                    {
                        Record redRightRed = new Record(redRight.getText().toString(), redCardID);
                        System.out.println(redRightRed);
                        allRecords.add(redRightRed);
                        break;
                    }
                    case R.id.blueLeftRedChk:
                    {
                        Record blueLeftRed = new Record(blueLeft.getText().toString(), redCardID);
                        System.out.println(blueLeftRed);
                        allRecords.add(blueLeftRed);
                        break;
                    }
                    case R.id.blueCenterRedChk:
                    {
                        Record blueCenterRed = new Record(blueCenter.getText().toString(), redCardID);
                        System.out.println(blueCenterRed);
                        allRecords.add(blueCenterRed);
                        break;
                    }
                    case R.id.blueRightRedChk:
                    {
                        Record blueRightRed = new Record(blueRight.getText().toString(), redCardID);
                        System.out.println(blueRightRed);
                        allRecords.add(blueRightRed);
                        break;
                    }
                    default: System.out.println("Can't tell which checkbox was clicked.");
                }
            }
        }
    }

    private void checkRecords()
    {
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
        for (Record r : allRecords)
        {
            int rID = r.getItemID();
            int index = timeStampRecords.indexOf(rID);
            if (index > 0)
            {
                double rValue = Double.parseDouble(r.getValue());
                if (rValue < 0 || rValue > 2.50) allRecords.remove(r);
            }

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
            int currId = currEditText.getId();
            switch (currId)
            {
                case R.id.redLeftEditTxt:
                {
                    if (currEditText.getText().toString().length() > 0)
                    {
                        String teamNum = currEditText.getText().toString();
                        while (teamNum.length() < teamNumLabelLength)
                        {
                            teamNum += " ";
                        }
                        redLeftYellowLbl.setText(teamNum);
                        redLeftRedLbl.setText(teamNum);
                        break;
                    }
                }
                case R.id.redCenterEditTxt:
                {
                    if (currEditText.getText().toString().length() > 0)
                    {
                        String teamNum = currEditText.getText().toString();
                        while (teamNum.length() < teamNumLabelLength)
                        {
                            teamNum += " ";
                        }
                        redCenterYellowLbl.setText(teamNum);
                        redCenterRedLbl.setText(teamNum);
                        break;
                    }
                }
                case R.id.redRightEditTxt:
                {
                    if (currEditText.getText().toString().length() > 0)
                    {
                        String teamNum = currEditText.getText().toString();
                        while (teamNum.length() < teamNumLabelLength)
                        {
                            teamNum += " ";
                        }
                        redRightYellowLbl.setText(teamNum);
                        redRightRedLbl.setText(teamNum);
                        break;
                    }
                }
                case R.id.blueLeftEditTxt:
                {
                    if (currEditText.getText().toString().length() > 0)
                    {
                        String teamNum = currEditText.getText().toString();
                        while (teamNum.length() < teamNumLabelLength)
                        {
                            teamNum += " ";
                        }
                        blueLeftYellowLbl.setText(teamNum);
                        blueLeftRedLbl.setText(teamNum);
                        break;
                    }
                }
                case R.id.blueCenterEditTxt:
                {
                    if (currEditText.getText().toString().length() > 0)
                    {
                        String teamNum = currEditText.getText().toString();
                        while (teamNum.length() < teamNumLabelLength)
                        {
                            teamNum += " ";
                        }
                        blueCenterYellowLbl.setText(teamNum);
                        blueCenterRedLbl.setText(teamNum);
                        break;
                    }
                }
                case R.id.blueRightEditTxt:
                {
                    if (currEditText.getText().toString().length() > 0)
                    {
                        String teamNum = currEditText.getText().toString();
                        while (teamNum.length() < teamNumLabelLength)
                        {
                            teamNum += " ";
                        }
                        blueRightYellowLbl.setText(teamNum);
                        blueRightRedLbl.setText(teamNum);
                        break;
                    }
                }
                default: System.out.println("ERROR.");
            }
        }
    }

}

