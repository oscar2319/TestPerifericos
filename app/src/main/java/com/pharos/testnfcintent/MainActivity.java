package com.pharos.testnfcintent;

import static com.pharos.testnfcintent.Constants.*;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    View mView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        FloatingActionButton fab2 = findViewById(R.id.fab2);
        FloatingActionButton fab3 = findViewById(R.id.fab3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mView = view;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setComponent(new ComponentName("com.credibanco.smartposperipherals","com.credibanco.smartposperipherals.presentation.activity.ExternalNfcReadActivity"));
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

                intent.setPackage(CREDIBANCO_PACKAGE);
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.setType("*/*");
                intent.putExtra(TYPEFACE, TYPEFACE_DEFAULT);
                intent.putExtra(LETTER_SPACING, 6);
                intent.putExtra(GRAY_LEVEL, GRAY_LEVEL_2);
                intent.putExtra(PACKAGE_NAME, myAppPackageName);
                intent.putStringArrayListExtra(Intent.EXTRA_STREAM, valuesToSend);

                intent.setComponent(new ComponentName("com.credibanco.smartposperipherals","com.credibanco.smartposperipherals.presentation.activity.ExternalPrintingActivity"));
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
                intent.setComponent(new ComponentName("com.credibanco.smartposperipherals","com.credibanco.smartposperipherals.presentation.activity.ExternalScannerActivity"));
                startActivityForResult(intent, 60001);

            }
        });
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
                Snackbar.make( mView,returnString, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        } else if(requestCode == 60001) {
            if (resultCode == 80000) { // Activity.RESULT_OK

                // get String data from Intent
                String returnString = data.getStringExtra("SCANNER");


                TextView text = findViewById(R.id.textView);
                text.setText(returnString);
                Snackbar.make( mView,returnString, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }
}