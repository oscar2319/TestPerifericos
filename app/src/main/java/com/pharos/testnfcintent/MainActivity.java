package com.pharos.testnfcintent;

import static com.pharos.testnfcintent.Constants.ALIGN_CENTER;
import static com.pharos.testnfcintent.Constants.ALIGN_RIGHT;
import static com.pharos.testnfcintent.Constants.CREDIBANCO_PACKAGE;
import static com.pharos.testnfcintent.Constants.FONT_BIG;
import static com.pharos.testnfcintent.Constants.FONT_NORMAL;
import static com.pharos.testnfcintent.Constants.GRAY_LEVEL;
import static com.pharos.testnfcintent.Constants.GRAY_LEVEL_2;
import static com.pharos.testnfcintent.Constants.IMAGE;
import static com.pharos.testnfcintent.Constants.LETTER_SPACING;
import static com.pharos.testnfcintent.Constants.PACKAGE_NAME;
import static com.pharos.testnfcintent.Constants.QR;
import static com.pharos.testnfcintent.Constants.TEXT;
import static com.pharos.testnfcintent.Constants.TYPEFACE;
import static com.pharos.testnfcintent.Constants.TYPEFACE_DEFAULT;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        boolean[] isUIDtrue = {true};
        boolean[] isBluetooth = {false};

        FloatingActionButton fab = findViewById(R.id.fab);
        FloatingActionButton fab2 = findViewById(R.id.fab2);
        FloatingActionButton fab3 = findViewById(R.id.fab3);
        Switch uidSwitch = findViewById(R.id.uidSwitch);
        Switch BlSwitch = findViewById(R.id.bluetoothSwitch);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mView = view;
                Intent intent = new Intent(Intent.ACTION_MAIN);

                intent.putExtra(UID, isUIDtrue[0]);

                intent.setComponent(new ComponentName("com.credibanco.smartposperipherals", "com.credibanco.smartposperipherals.presentation.activity.ExternalNfcReadActivity"));
                startActivityForResult(intent, 60000);

            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mView = view;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                String imageName = "logo";
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

                writeToFile(profileDir, "qr", encodedImage);

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
                valuesToSend.add(QR + "," + FONT_NORMAL + "," + ALIGN_CENTER);

                intent.setPackage(CREDIBANCO_PACKAGE);
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.setType("*/*");
                intent.putExtra(TYPEFACE, TYPEFACE_DEFAULT);
                intent.putExtra(LETTER_SPACING, 6);
                intent.putExtra(GRAY_LEVEL, GRAY_LEVEL_2);
                intent.putExtra(PACKAGE_NAME, myAppPackageName);
                intent.putStringArrayListExtra(Intent.EXTRA_STREAM, valuesToSend);

                intent.setComponent(new ComponentName("com.credibanco.smartposperipherals", "com.credibanco.smartposperipherals.presentation.activity.ExternalPrintingActivity"));
                startActivityForResult(intent, 60000);

            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                intent.setComponent(new ComponentName("com.credibanco.smartposperipherals", "com.credibanco.smartposperipherals.presentation.activity.ExternalScannerActivity"));
                startActivityForResult(intent, 60001);

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
                intent.setComponent(new ComponentName("com.credibanco.smartposperipherals", "com.credibanco.smartposperipherals.presentation.activity.ExternalBluetoothActivity"));
                startActivityForResult(intent, 6000);

            }
        });
    }

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
            if (resultCode == 6000){
                String bluetoothState = data.getStringExtra("BLUETOOTH_ADAPTER_STATUS");
                Toast.makeText(this, bluetoothState, Toast.LENGTH_SHORT).show();
            }
        }
    }
}