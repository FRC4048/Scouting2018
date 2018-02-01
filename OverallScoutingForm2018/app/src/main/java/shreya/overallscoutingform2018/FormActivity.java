package shreya.overallscoutingform2018;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

        // Update team labels when team numbers are entered

//        /*EditText[] redAllianceNumbers = {redLeft, redCenter, redRight};
//        final CheckBox[][] redPenaltyOptions = {{redLeftYellowLbl, redLeftRedLbl}, {redCenterYellowLbl, redCenterRedLbl}, {redRightYellowLbl, redRightRedLbl}};
//
//        for (int i = 0; i < redAllianceNumbers.length; i++)
//        {
//            final int currIndex = i;
//            final EditText currTeam = redAllianceNumbers[i];
//            currTeam.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//                    if (currTeam.getText().toString().length() > 0)
//                    {
//                        int penaltyIndex = 0;
//                        if (currIndex == 0) penaltyIndex = currIndex;
//                        else penaltyIndex = currIndex + 1;
//                        while (penaltyIndex < currIndex + 2) {
//                            CheckBox[] currTeamPenaltyOptions = redPenaltyOptions[penaltyIndex];
//                            for (int k = 0; k < currTeamPenaltyOptions.length; k++)
//                            {
//                                currTeamPenaltyOptions[k].setText(currTeam.getText().toString());
//                            }
//                        }
//                    }
//
//                }
//            });
//        }*/

        redLeft.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (redLeft.getText().toString().length() > 0) {
                    String teamNum = redLeft.getText().toString();
                    while (teamNum.length() < 7)
                    {
                        teamNum += " ";
                    }
                    redLeftYellowLbl.setText(teamNum);
                    redLeftRedLbl.setText(teamNum);
                }



            }
        });

        redCenter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (redCenter.getText().toString().length() > 0) {
                    String teamNum = redCenter.getText().toString();
                    while (teamNum.length() < 10)
                    {
                        teamNum += " ";
                    }
                    redCenterYellowLbl.setText(teamNum);
                    redCenterRedLbl.setText(teamNum);
                }


            }
        });

        redRight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (redRight.getText().toString().length() > 0) {
                    String teamNum = redRight.getText().toString();
                    while (teamNum.length() < 9)
                    {
                        teamNum += " ";
                    }
                    redRightYellowLbl.setText(teamNum);
                    redRightRedLbl.setText(teamNum);
                }


            }
        });

        blueLeft.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (blueLeft.getText().toString().length() > 0) {
                    String teamNum = blueLeft.getText().toString();
                    while (teamNum.length() < 9)
                    {
                        teamNum += " ";
                    }
                    blueLeftYellowLbl.setText(teamNum);
                    blueLeftRedLbl.setText(teamNum);
                }


            }
        });

        blueCenter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (blueCenter.getText().toString().length() > 0) {
                    String teamNum = blueCenter.getText().toString();
                    while (teamNum.length() < 11)
                    {
                        teamNum += " ";
                    }
                    blueCenterYellowLbl.setText(teamNum);
                    blueCenterRedLbl.setText(teamNum);
                }


            }
        });

        blueRight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (blueRight.getText().toString().length() > 0) {
                    String teamNum = blueRight.getText().toString();
                    while (teamNum.length() < 10)
                    {
                        teamNum += " ";
                    }
                    blueRightYellowLbl.setText(teamNum);
                    blueRightRedLbl.setText(teamNum);
                }


            }
        });


    }

//    private class EditTextWatcher extends TextWatcher
//    {
//
//        @Override
//        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//        }
//
//        @Override
//        public void afterTextChanged(Editable editable) {
//
//        }
//    }

}

