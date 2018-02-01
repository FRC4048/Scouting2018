package shreya.overallscoutingform2018;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Dan on 1/14/18.
 */

abstract public class AbstractForm extends Activity{

    // File names
    static final String STATE_SAVE_FILE = "stateSave.txt";
    static final String TEMP_FILE = "tempFile.txt";
    static final String ARCHIVE_FILE = "archiveFile.txt";
    static final String CONFIG_FILE = "configFile.txt";
    static int archivedFiles = 0;

    boolean firstForm = true;
    int formsPending = 0;

    // Necessary items for data transfer
    private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private static BluetoothDevice device = null;

    private static String pcCompanion ="LUCASPC";

    static String MESSAGE = "";
    static String POSITIVE_BUTTON = "";
    static String NEGATIVE_BUTTON = "";
    static String NEUTRAL_BUTTON = "";

    final private static String BLUETOOTH_FOLDER_PATH = "/storage/emulated/0/Download/";

    // The name of the current scout.
    String scoutName;
    // The identifier number given to this machine.
    int tabletNum;

    // The names of the available scouts. Meant to be overridden by the config file.
    String[] names = {"Scout"};
    // The teams competing in this competition. Meant to be overridden by the config file.
    String[] teams = {"4048"};

    abstract void initConfigs();
    abstract void initRecords();
    abstract void initLayout();
    abstract void initSaveState();
    abstract void executeRequest();
    abstract void saveState();
    abstract void resetForm();
    abstract void resetRadioGroups();
    abstract void resetCheckboxes();
    abstract Form makeForm();
    abstract void readyToSave();
    abstract void setState(String[] records, int startingIndex);



    // These are the potential intentions for having opened an alert dialog.
    enum Action {
        NONE, SAVE_FORM, CHOOSE_TRANSFER_ACTION, TRANSFER_FORMS, TRANSFER_LAST_FORMS,
        RECEIVE_CONFIG, CHECK_TRANSFER, WARNING_TEAMNUM, WARNING_KEYWORD, TRANSFER_ALL_ARCHIVES
    } // End Action
    Action actionRequested = AbstractForm.Action.NONE;

    public void initArchiveSystem() {
        boolean done = false;
        while (!done) {
            String fileName = ARCHIVE_FILE.split("\\.")[0] + archivedFiles
                    + ARCHIVE_FILE.split("\\.")[1];
            File file = new File(getFilesDir().getAbsolutePath(), fileName);
            if (!file.exists()) done = true;
            else archivedFiles++;
        } // End while
    } // End initArchiveSystem



    public void btTransfer(String fileName){
        File file = getFileStreamPath(fileName);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

        PackageManager pm = getPackageManager();
        List appsList = pm.queryIntentActivities(intent, 0);
        if(appsList.size() > 0) {
            String packageName = null;
            String className = null;
            boolean found = false;
            for(int i = 0; i < appsList.size(); i++) {
                ResolveInfo info = (ResolveInfo) appsList.get(i);
                packageName = info.activityInfo.packageName;
                if (packageName.equals("com.android.bluetooth")) {
                    className = info.activityInfo.name;
                    found = true;
                    break;// found
                } // End if
            } // End for
            if (!found) Toast.makeText(this, "Not found!", Toast.LENGTH_SHORT).show();
            else {
                intent.setClassName(packageName, className);
                startActivityForResult(intent, 1);
                firstForm = true;
            } // End if
        } // End if
    } // End btTransfer

    public String prepareFormTransfer(String filename) {
        String content = "";
        String message = "There has been an I/O issue!\nTRANSFER FAILED";
        try {
            File file = new File(getFilesDir().getAbsolutePath(), filename);
            if (!file.exists()) {
                message = "There has been an I/O issue!\n" +
                        "TRANSFER FAILED: " + filename + " DOES NOT EXIST.";
                throw new IOException();
            } // End if
            String str;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (!((str = reader.readLine())== null)) content += str;
            reader.close();
            if (!file.delete()) throw new IOException();
            FileOutputStream fos = openFileOutput(filename, Context.MODE_WORLD_READABLE);
            fos.write(content.getBytes());
            fos.close();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        } // End try
        return content;
    } // End prepareFormTransfer

    public void prepareToTransfer(String fileName) {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        } // End if
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equals(pcCompanion)) {
                    AbstractForm.device = device;
                    btTransfer(fileName);
                } // End if
            } // End for
        } else Toast.makeText(this, "Please pair master computer to this device.",
                Toast.LENGTH_SHORT).show();  // End if
    } // End prepareToTransfer

    boolean checkConfigFile() {
        File file = new File(getFilesDir().getAbsolutePath(), CONFIG_FILE);
        return file.exists();
    } // End checkConfigFile

    public void archiveCurrentFile() {
        String message = "There has been an I/O issue!";
        try {
            File file = new File(getFilesDir().getAbsolutePath(), TEMP_FILE);
            if (!file.exists()) {
                message = "There has been an I/O issue! \n" +
                        "TEMP FILE NOT FOUND (LAST TRANSFER NOT ARCHIVED)";
                throw new IOException();
            } // End if
            String content = "";
            String str;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (!((str = reader.readLine())== null)) content += str;
            reader.close();

            String fileName = ARCHIVE_FILE.split("\\.")[0] + archivedFiles
                    + ARCHIVE_FILE.split("\\.")[1];
            file = new File(getFilesDir().getAbsolutePath(), fileName);
            if (!file.exists()) if (!file.createNewFile()) {
                message = "There has been an I/O issue! \n" +
                        "FAILED TO CREATE ARCHIVE FILE";
                throw new IOException();
            } // End if
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
            writer.close();
            archivedFiles++;
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } // End try
    } // End archiveCurrentFile

    boolean retrieveComputerFile(String fileName) {
        String message = "There has been an I/O issue!\nCONFIG FILE RETRIEVE FAILED";
        try {
            File file = new File(BLUETOOTH_FOLDER_PATH, fileName);
            if (!file.exists()) {
                showAlertDialog("There is no received file.", "OK");
                return false;
            } else {
                ArrayList<String> contents = new ArrayList<>();
                String str;
                BufferedReader reader = new BufferedReader(new FileReader(file));
                while (!((str = reader.readLine()) == null)) contents.add(str);
                reader.close();
                if (!file.delete()) {
                    message = "There has been an I/O issue!\n" +
                            "BLUETOOTH DIR CONFIG FILE DELETE FAILED";
                    throw new IOException();
                } // End if

                file = new File(getFilesDir().getAbsolutePath(), fileName);
                if (file.exists()) {
                    if (!file.delete()) {
                        message = "There has been an I/O issue!\nOLD CONFIG FILE DELETE FAILED";
                        throw new IOException();
                    } // End if
                } // End if
                if (!file.createNewFile()) {
                    message = "There has been an I/O issue!\n" +
                            "CONFIG FILE CREATION FAILED";
                    throw new IOException();
                } // End if
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                for (int i = 0; i < contents.size(); i++) {
                    if (i > 0) writer.newLine();
                    writer.write(contents.get(i));
                } // End for
                writer.close();
                initConfigs();
                showAlertDialog("SUCCESS!", "OK");
            } // End if
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),
                    message + "\n" + e.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        } // End try
        return true;
    } // End retrieveComputerFile

    public void showAlertDialog(String message, String positive) {
        MESSAGE = message;
        POSITIVE_BUTTON = positive;
        NEGATIVE_BUTTON = null;
        NEUTRAL_BUTTON = null;
        DialogFragment dialog = new AbstractForm.AlertDialogFragment();
        dialog.show(getFragmentManager(), "alertDialog");
    } // End showAlertDialog

    public void showAlertDialog(String message, String positive, String negative) {
        MESSAGE = message;
        POSITIVE_BUTTON = positive;
        NEGATIVE_BUTTON = negative;
        NEUTRAL_BUTTON = null;
        DialogFragment dialog = new AbstractForm.AlertDialogFragment();
        dialog.show(getFragmentManager(), "alertDialog");
    } // End showAlertDialog

    public void showAlertDialog(String message, String positive, String negative, String neutral) {
        MESSAGE = message;
        POSITIVE_BUTTON = positive;
        NEGATIVE_BUTTON = negative;
        NEUTRAL_BUTTON = neutral;
        DialogFragment dialog = new AbstractForm.AlertDialogFragment();
        dialog.show(getFragmentManager(), "alertDialog");
    } // End showAlertDialog

    class AlertDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(MESSAGE);
            builder.setPositiveButton(POSITIVE_BUTTON, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    switch (actionRequested) {
                        case CHOOSE_TRANSFER_ACTION:
                            actionRequested = AbstractForm.Action.RECEIVE_CONFIG;
                            executeRequest();
                            break;
                        default:
                            executeRequest();
                    } // End switch
                    actionRequested = AbstractForm.Action.NONE;
                }
            }); // End setPositiveButton
            try {
                if (null != NEGATIVE_BUTTON)
                    builder.setNegativeButton(NEGATIVE_BUTTON,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    switch (actionRequested) {
                                        case CHOOSE_TRANSFER_ACTION:
                                            actionRequested = AbstractForm.Action.TRANSFER_LAST_FORMS;
                                            executeRequest();
                                    } // End switch
                                    actionRequested = AbstractForm.Action.NONE;
                                }
                            }); // End setNegativeButton
                if (null != NEUTRAL_BUTTON)
                    builder.setNeutralButton(NEUTRAL_BUTTON,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (actionRequested) {
                                        case CHOOSE_TRANSFER_ACTION:
                                            actionRequested = AbstractForm.Action.TRANSFER_ALL_ARCHIVES;
                                            executeRequest();
                                    } // End switch
                                    actionRequested = AbstractForm.Action.NONE;
                                }
                            }); // End setNeutralButton
            } catch (NullPointerException e) {
                e.printStackTrace();
            } // End try
            return builder.create();
        } // End onCreateDialog
        @Override
        public void dismiss() {
            if (actionRequested.equals(AbstractForm.Action.RECEIVE_CONFIG)) if (!checkConfigFile())
                showAlertDialog("A configuration file from the master computer is required to" +
                                "continue.\nPlease transfer the file to this machine.",
                        "I've transferred the file");
            super.dismiss();
        } // End dismiss
        @Override
        public void dismissAllowingStateLoss() {
            if (actionRequested.equals(AbstractForm.Action.RECEIVE_CONFIG)) if (!checkConfigFile())
                showAlertDialog("A configuration file from the master computer is required to" +
                                "continue.\nPlease transfer the file to this machine.",
                        "I've transferred the file");
            super.dismissAllowingStateLoss();
        } // End dismissAllowingStateLoss
    } // End AlertDialogFragment

    @Override
    public void onPause() {
        saveState();
        super.onPause();
    } // End onPause

    @Override
    public void onBackPressed() {
        saveState();
        super.onBackPressed();
    } // End onBackPressed

    boolean saveForm() {
        String message = "There has been an I/O issue! FORM NOT SAVED.";
        try {
            File file = new File(getFilesDir().getAbsolutePath(), TEMP_FILE);
            if (!file.exists()) {
                if (!file.createNewFile()) throw new IOException();
            } else {
                if (firstForm) {
                    if (!file.delete()) {
                        message = "There has been an I/O issue! \n" +
                                "FAILED TO DELETE OLD TEMP FILE";
                        throw new IOException();
                    } // End if
                    file.createNewFile();
                    firstForm = false;
                } // End if
            } // End if
            String content = "";
            String str;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (!((str = reader.readLine())== null)) content += str;
            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            if (content.isEmpty()) writer.append(makeForm().toString());
            else writer.append(content + Form.FORM_DELIMITER + makeForm().toString());
            writer.close();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return false;
        } // End try
        return true;
    } // End saveForm
}
