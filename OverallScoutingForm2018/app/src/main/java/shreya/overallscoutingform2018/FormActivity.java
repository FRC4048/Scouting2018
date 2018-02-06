package shreya.overallscoutingform2018;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

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

