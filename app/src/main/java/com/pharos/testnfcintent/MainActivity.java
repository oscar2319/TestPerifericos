package com.pharos.testnfcintent;

import static com.pharos.testnfcintent.Constants.ALIGN_CENTER;
import static com.pharos.testnfcintent.Constants.ALIGN_RIGHT;
import static com.pharos.testnfcintent.Constants.BLOCK_NFC;
import static com.pharos.testnfcintent.Constants.CREDIBANCO_PACKAGE;
import static com.pharos.testnfcintent.Constants.FONT_BIG;
import static com.pharos.testnfcintent.Constants.FONT_NORMAL;
import static com.pharos.testnfcintent.Constants.GRAY_LEVEL;
import static com.pharos.testnfcintent.Constants.GRAY_LEVEL_2;
import static com.pharos.testnfcintent.Constants.IMAGE;
import static com.pharos.testnfcintent.Constants.KEY_HEX_NFC;
import static com.pharos.testnfcintent.Constants.KEY_TYPE_NFC;
import static com.pharos.testnfcintent.Constants.LETTER_SPACING;
import static com.pharos.testnfcintent.Constants.PACKAGE_NAME;
import static com.pharos.testnfcintent.Constants.QR;
import static com.pharos.testnfcintent.Constants.TEXT;
import static com.pharos.testnfcintent.Constants.TYPEFACE;
import static com.pharos.testnfcintent.Constants.TYPEFACE_DEFAULT;
import static com.pharos.testnfcintent.Constants.TYPE_PROCESS_NFC;
import static com.pharos.testnfcintent.Constants.UID;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    View mView;
    EditText hashCode;
    private boolean updating = false;
    @Nullable
    private TypeProcessNFC typeProcess = TypeProcessNFC.DEFAULT;

    private Switch authenticateSwitch, readSwitch, writeSwitch, encrypthSwitch;

    private TextView blockLabel, keyTypeLabel, keyHexLabel;
    private TextInputLayout blockTil, keyTypeTil, keyHexTil;

    private AutoCompleteTextView keyTypeDropdown;
    @Nullable private M1KeyTypeEnum selectedKeyType = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        hashCode = findViewById(R.id.hash_code);
        setSupportActionBar(toolbar);

        boolean[] isUIDtrue = {true};
        boolean[] isBluetooth = {false};

        FloatingActionButton fab = findViewById(R.id.fab);
        FloatingActionButton fab2 = findViewById(R.id.fab2);
        FloatingActionButton fab3 = findViewById(R.id.fab3);
        Switch uidSwitch = findViewById(R.id.uidSwitch);
        Switch BlSwitch = findViewById(R.id.bluetoothSwitch);

        authenticateSwitch = findViewById(R.id.authenticateSwitch);
        readSwitch         = findViewById(R.id.readSwitch);
        writeSwitch        = findViewById(R.id.writeSwitch);
        encrypthSwitch     = findViewById(R.id.encrypthSwitch);

        blockLabel = findViewById(R.id.block_label);
        blockTil   = findViewById(R.id.txt_input_event_block);

        keyTypeLabel = findViewById(R.id.key_type_label);
        keyTypeTil   = findViewById(R.id.key_type_dropdown_layout);

        keyHexLabel = findViewById(R.id.key_hex_label);
        keyHexTil   = findViewById(R.id.txt_input_event_key_hex);

        keyTypeDropdown =findViewById(R.id.key_type_dropdown);
        M1KeyTypeEnum[] keyEnums = M1KeyTypeEnum.values();
        String[] display = new String[keyEnums.length];
        for (int i = 0; i < keyEnums.length; i++) display[i] = keyEnums[i].getDisplayName();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, display);
        keyTypeDropdown.setAdapter(adapter);
        keyTypeDropdown.setOnItemClickListener((parent, v1, pos, id) -> selectedKeyType = keyEnums[pos]);

        authenticateSwitch.setChecked(false);
        readSwitch.setChecked(false);
        writeSwitch.setChecked(false);
        encrypthSwitch.setChecked(false);

        typeProcess = TypeProcessNFC.DEFAULT;
        updateInputsFor(TypeProcessNFC.DEFAULT);
        List<Switch> nfcSwitches = Arrays.asList(authenticateSwitch, readSwitch, writeSwitch, encrypthSwitch);
        Map<Integer, TypeProcessNFC> id2Type = new HashMap<>();
        id2Type.put(R.id.authenticateSwitch, TypeProcessNFC.AUTHENTICATE);
        id2Type.put(R.id.readSwitch,         TypeProcessNFC.READ);
        id2Type.put(R.id.writeSwitch,        TypeProcessNFC.WRITE);
        id2Type.put(R.id.encrypthSwitch,     TypeProcessNFC.ENCRYPT);

        CompoundButton.OnCheckedChangeListener exclusive = (btn, checked) -> {
            if (updating) return;

            if (checked) {
                updating = true;
                if (btn != authenticateSwitch) authenticateSwitch.setChecked(false);
                if (btn != readSwitch)         readSwitch.setChecked(false);
                if (btn != writeSwitch)        writeSwitch.setChecked(false);
                if (btn != encrypthSwitch)     encrypthSwitch.setChecked(false);
                updating = false;

                if (btn.getId() == R.id.authenticateSwitch)      typeProcess = TypeProcessNFC.AUTHENTICATE;
                else if (btn.getId() == R.id.readSwitch)         typeProcess = TypeProcessNFC.READ;
                else if (btn.getId() == R.id.writeSwitch)        typeProcess = TypeProcessNFC.WRITE;
                else if (btn.getId() == R.id.encrypthSwitch)     typeProcess = TypeProcessNFC.ENCRYPT;

            } else {
                boolean anyOn = authenticateSwitch.isChecked() || readSwitch.isChecked()
                        || writeSwitch.isChecked() || encrypthSwitch.isChecked();
                if (!anyOn) typeProcess = TypeProcessNFC.DEFAULT;
            }

            updateInputsFor(typeProcess);
        };
        for (Switch s : nfcSwitches) s.setOnCheckedChangeListener(exclusive);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!hashCode.getText().toString().isEmpty()){
                    String blockStr = "";
                    if (blockTil.getEditText() != null) {
                        blockStr = blockTil.getEditText().getText().toString().trim();
                    }

                    String keyHexStr = "";
                    if (keyHexTil.getEditText() != null) {
                        keyHexStr = keyHexTil.getEditText().getText().toString().trim();
                    }

                    String keyTypeStr = (selectedKeyType != null)
                            ? selectedKeyType.name()
                            : keyTypeDropdown.getText().toString().trim();

                    mView = view;
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    switch (typeProcess == null ? TypeProcessNFC.DEFAULT : typeProcess) {
                        case AUTHENTICATE:
                            intent.putExtra(BLOCK_NFC, blockStr);
                            intent.putExtra(KEY_TYPE_NFC, keyTypeStr);
                            intent.putExtra(KEY_HEX_NFC, keyHexStr);
                            break;

                        case READ:
                            intent.putExtra(BLOCK_NFC, blockStr);
                            break;

                        case WRITE:
                        case ENCRYPT:
                            intent.putExtra(BLOCK_NFC, blockStr);
                            intent.putExtra(KEY_HEX_NFC, keyHexStr);
                            break;

                        case DEFAULT:
                        default:
                            intent.putExtra(UID, isUIDtrue[0]);
                            break;
                    }

                    intent.putExtra(TYPE_PROCESS_NFC, typeProcess.toString());
                    intent.putExtra(ScannerConstants.HASH_CODE, hashCode.getText().toString());
                    intent.setComponent(new ComponentName("com.credibanco.smartposperipherals", "com.credibanco.smartposperipherals.presentation.activity.ExternalNfcReadActivity"));
                    startActivityForResult(intent, 60000);
                } else {
                    Toast.makeText(getApplicationContext(), "Ingrese su Hash Code", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!hashCode.getText().toString().isEmpty()){

                    mView = view;
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    String imageName = "logo";
                    String qrName1 = "qrName1";
                    String qrName2 = "qrName2";
                    String text = "Prueba para imprimir";
                    String myAppPackageName = "com.pharos.testnfcintent";

                    Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            R.drawable.qr1675805453449);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] b = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                    String dirName = "images";
                    File profileDir = makeAndGetProfileDirectory(dirName);

                    writeToFile(profileDir, qrName1, encodedImage);
                    writeToFile(profileDir, qrName2, encodedImage);

                    //CON FONT_BIG = 24 CARACTERES POR LINEA
                    //CON FONT_NORMAL = 32 CARACTERES POR LINEA
                    //CON FONT_IOU = 48 CARACTERES POR LINEA


                    // Listado de imágenes y líneas a enviar
                    ArrayList<String> valuesToSend = new ArrayList<>();
                    valuesToSend.add(IMAGE + "," + imageName + "," + ALIGN_RIGHT);
                    valuesToSend.add(TEXT + "," + text + "," + FONT_BIG + "," + ALIGN_RIGHT);
                    valuesToSend.add(TEXT + "," + text + "," + FONT_BIG + "," + ALIGN_RIGHT);
                    valuesToSend.add(TEXT + "," + text + "," + FONT_BIG + "," + ALIGN_RIGHT);
                    valuesToSend.add(TEXT + "," + text + "," + FONT_BIG + "," + ALIGN_RIGHT);
                    valuesToSend.add(TEXT + "," + text + "," + FONT_BIG + "," + ALIGN_RIGHT);
                    valuesToSend.add(TEXT + "," + text + "," + FONT_BIG + "," + ALIGN_RIGHT);
                    valuesToSend.add(TEXT + "," + text + "," + FONT_BIG + "," + ALIGN_RIGHT);
                    valuesToSend.add(QR + ", " + qrName1 + ","  + FONT_NORMAL + "," + ALIGN_CENTER);
                    valuesToSend.add(QR + ", " + qrName2 + ","  + FONT_NORMAL + "," + ALIGN_CENTER);

                    intent.setPackage(CREDIBANCO_PACKAGE);
                    intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                    intent.setType("*/*");
                    intent.putExtra(TYPEFACE, TYPEFACE_DEFAULT);
                    intent.putExtra(LETTER_SPACING, 6);
                    intent.putExtra(GRAY_LEVEL, GRAY_LEVEL_2);
                    intent.putExtra(ScannerConstants.HASH_CODE, hashCode.getText().toString());
                    intent.putExtra(PACKAGE_NAME, myAppPackageName);
                    intent.putStringArrayListExtra(Intent.EXTRA_STREAM, valuesToSend);

                    intent.setComponent(new ComponentName("com.credibanco.smartposperipherals", "com.credibanco.smartposperipherals.presentation.activity.ExternalPrintingActivity"));
                    startActivityForResult(intent, 60000);

                } else {
                    Toast.makeText(getApplicationContext(), "Ingrese su Hash Code", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!hashCode.getText().toString().isEmpty()){
                    mView = view;
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.putExtra(ScannerConstants.SHOWBAR, true);
                    intent.putExtra(ScannerConstants.SHOWBACK, true);
                    intent.putExtra(ScannerConstants.SHOWTITLE, true);
                    intent.putExtra(ScannerConstants.SHOWSWITCH, true);
                    intent.putExtra(ScannerConstants.SHOWMENU, true);
                    intent.putExtra(ScannerConstants.TITLE, "TITULO");
                    intent.putExtra(ScannerConstants.TITLESIZE, 10);
                    intent.putExtra(ScannerConstants.TIPSIZE, 10);
                    intent.putExtra(ScannerConstants.SCANTIP, "Scan tip");
                    intent.putExtra(ScannerConstants.HASH_CODE, hashCode.getText().toString());
                    intent.setComponent(new ComponentName("com.credibanco.smartposperipherals", "com.credibanco.smartposperipherals.presentation.activity.ExternalScannerActivity"));
                    startActivityForResult(intent, 60001);

                } else {
                    Toast.makeText(getApplicationContext(), "Ingrese su Hash Code", Toast.LENGTH_SHORT).show();
                }
            }
        });

        uidSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isUIDtrue[0] = !isUIDtrue[0];

                String message = "Entrada de uid :  " + isUIDtrue[0];
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        BlSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBluetooth[0] = !isBluetooth[0];

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.putExtra(Constants.STATE_BLUETOOTH, isBluetooth[0]);
                intent.putExtra(ScannerConstants.HASH_CODE, hashCode.getText().toString());
                intent.setComponent(new ComponentName("com.credibanco.smartposperipherals", "com.credibanco.smartposperipherals.presentation.activity.ExternalBluetoothActivity"));
                startActivityForResult(intent, 6000);

            }
        });
    }

    private void updateInputsFor(@Nullable TypeProcessNFC type) {
        if (type == TypeProcessNFC.AUTHENTICATE) {
            show(blockLabel, blockTil, keyTypeLabel, keyTypeTil, keyHexLabel, keyHexTil);
        } else if (type == TypeProcessNFC.READ) {
            show(blockLabel, blockTil);
            hide(keyTypeLabel, keyTypeTil, keyHexLabel, keyHexTil);
        } else if (type == TypeProcessNFC.WRITE || type == TypeProcessNFC.ENCRYPT) {
            show(blockLabel, blockTil, keyHexLabel, keyHexTil);
            hide(keyTypeLabel, keyTypeTil);
        } else {
            hide(blockLabel, blockTil, keyTypeLabel, keyTypeTil, keyHexLabel, keyHexTil);
        }
    }

    private void show(View... vs) { for (View v : vs) v.setVisibility(View.VISIBLE); }
    private void hide(View... vs) { for (View v : vs) v.setVisibility(View.GONE); }

    private void writeToFile(File directory, String file, String data) {

        File qrFile = new File(directory, file);
        FileOutputStream fileOutput = null;
        OutputStreamWriter outputStreamWriter = null;

        try {
            fileOutput = new FileOutputStream(qrFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            outputStreamWriter = new OutputStreamWriter(fileOutput);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    File makeAndGetProfileDirectory(String dirName) {
        // determine the profile directory
        File profileDirectory;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            profileDirectory = new File(Environment.getStorageDirectory(), dirName);
        } else {
            profileDirectory = new File(Environment.getExternalStorageDirectory(), dirName);
        }

        // creates the directory if not present yet
        profileDirectory.mkdir();

        return profileDirectory;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
        if (requestCode == 60000) {
            if (resultCode == 60000) { // Activity.RESULT_OK

                // get String data from Intent
                String returnString = data.getStringExtra("NFC_READ_TAG");


                TextView text = findViewById(R.id.textView);
                text.setText(returnString);
                Snackbar.make(mView, returnString, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        } else if (requestCode == 60001) {
            if (resultCode == 80000) { // Activity.RESULT_OK

                // get String data from Intent
                String returnString = data.getStringExtra("SCANNER");


                TextView text = findViewById(R.id.textView);
                text.setText(returnString);
                Snackbar.make(mView, returnString, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        } else if (requestCode == 6000) {
            if (resultCode == 6010){
                String bluetoothState = data.getStringExtra("BLUETOOTH_ADAPTER_STATUS");
                Toast.makeText(this, bluetoothState, Toast.LENGTH_SHORT).show();
            }
        }
    }
}