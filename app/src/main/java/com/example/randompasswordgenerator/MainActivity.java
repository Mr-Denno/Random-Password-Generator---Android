package com.example.randompasswordgenerator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private CheckBox cb_minusc, cb_maiusc, cb_numbers, cb_special;
    private SeekBar seekbar;
    private TextView indexPassword;
    private Button buttonGenerate;
    private Button buttonPasswordList;
    public EditText textPassword;
    private TextView textComment;
    private MediaPlayer generateSound;
    private ImageButton btnSave, btnCopy;
    private String User = "";
    private InterfaceImplementation inter = new InterfaceImplementation();

    //TODO: criptare i file salvati

    //TODO: inserire i vari LOG message associati ad eventi

    //TODO: ottimizzazzione del codice e ordine generale

    //TODO: creare il file di log

    //TODO: inserire la pubblicità

    //TODO: definire la decisione di mercato

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        generateSound = MediaPlayer.create(MainActivity.this, R.raw.spin);  // per il suono dello "spin"

        Toolbar tb = (Toolbar) findViewById(R.id.topBar);
        setSupportActionBar(tb);

        //Ottengo il dato dal Choose
        Bundle message = getIntent().getExtras();
        if(message != null){
            User = message.getString("User");
        }


        //TODO: FIXHERE -> top bar personalizzata

        //----------------------------- Seek Bar ------------------------------
        seekbar = findViewById(R.id.seekBar);
        seekbar.setProgress(45); //settata al centro come default
        indexPassword = findViewById(R.id.tw_indexPassword);
        indexPassword.setText("" + seekbar.getProgress()/6);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                indexPassword.setText("" + seekbar.getProgress()/6);
            }
            @SuppressLint("SetTextI18n")
            public void onStartTrackingTouch(SeekBar seekBar) {
                indexPassword.setText("" + seekbar.getProgress()/6);
            }

            @SuppressLint("SetTextI18n")
            public void onStopTrackingTouch(SeekBar seekBar) {
                indexPassword.setText("" + seekbar.getProgress()/6);
            }
        });

        //----------------------------- Password List ------------------------------
        buttonPasswordList = (Button) findViewById(R.id.btnPasswordList);
        buttonPasswordList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inter.RedirectActivityPutsExtra(MainActivity.this, Activity_PasswordList.class, view, User, "User");
            }
        });

        //----------------------------- Button Generate ------------------------------
        buttonGenerate = findViewById(R.id.btnGenerate);
        buttonGenerate.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {

                cb_minusc = findViewById(R.id.cb_minusc);
                cb_maiusc = findViewById(R.id.cb_maiusc);
                cb_numbers = findViewById(R.id.cb_numbers);
                cb_special = findViewById(R.id.cb_special);

                //casi d'errore
                if ((!cb_minusc.isChecked() && !cb_maiusc.isChecked() && !cb_numbers.isChecked() && !cb_special.isChecked()) || (seekbar.getProgress()/6 == 0)){
                    Toast.makeText(getApplicationContext(), "Error! Argument Missing!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //funzione per la generazione della password
                String password = GeneratePassword();

                //----------------------------- Text Comment ------------------------------
                textPassword = findViewById(R.id.editText);
                textComment = findViewById(R.id.txtComment);
                inter.Comment(password, textComment);
                btnCopy = (ImageButton) findViewById(R.id.btnCopy);
                btnCopy.setVisibility(View.VISIBLE);
                btnSave = (ImageButton) findViewById(R.id.btnSave);
                btnSave.setVisibility(View.VISIBLE);
                textPassword.setText(password);


                textPassword.addTextChangedListener(new TextWatcher() {
                    public void afterTextChanged(Editable s) {} //inutile
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {} //inutile
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        String result = inter.Comment(textPassword.getText().toString(), textComment); //chiamata alla funzione Comment

                        if(result.equals("Password Too Short!")){
                            btnSave.setVisibility(View.INVISIBLE);
                            btnCopy.setVisibility(View.INVISIBLE);
                        } else {
                            btnCopy.setVisibility(View.VISIBLE);
                            btnSave.setVisibility(View.VISIBLE);
                        }

                    }
                });


                //----------------------------- Button Copy ------------------------------
                btnCopy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //copy
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("EditText", textPassword.getText().toString());
                        clipboard.setPrimaryClip(clip);

                        //messaggio per il feedback
                        Snackbar snackbar = Snackbar.make(v, "password copied!", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                });
                textPassword.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        //copy
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("EditText", textPassword.getText().toString());
                        clipboard.setPrimaryClip(clip);

                        //messaggio per il feedback
                        Snackbar snackbar = Snackbar.make(view, "password copied!", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        return false;
                    }
                });

                //----------------------------- Button Save ------------------------------
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
                }

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //save
                        openDialog(v, textPassword.getText().toString(), User);  //chiamata alla funzione

                        btnSave.setVisibility(View.INVISIBLE);
                        btnCopy.setVisibility(View.INVISIBLE);
                        textPassword.setText("");
                        textPassword.setVisibility(View.INVISIBLE);
                        textComment.setVisibility(View.INVISIBLE);
                        cb_numbers.setChecked(false);
                        cb_maiusc.setChecked(false);
                        cb_special.setChecked(false);
                        cb_minusc.setChecked(false);
                    }

                });
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permission, grantResults);
        switch (requestCode) {
            case 1000:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission not granted!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    //funzione per gli options menu sul ToolBar
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.logout:
                inter.RedirectActivity(MainActivity.this, Login.class);
                Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();
                break;
            case R.id.password_generator:
                //inter.RedirectActivityPutsExtra(MainActivity.this, MainActivity.class, , User, "User");
                Toast.makeText(getApplicationContext(), "Password Generator", Toast.LENGTH_SHORT).show();
                break;
            case R.id.insert_manually:
                //inter.RedirectActivityPutsExtra(MainActivity.this, SavePasswordManually.class, , User, "User");
                Toast.makeText(getApplicationContext(), "Insert Manually", Toast.LENGTH_SHORT).show();
                break;
            case R.id.password_list:
                //inter.RedirectActivityPutsExtra(MainActivity.this, Activity_PasswordList.class, , User, "User"););
                Toast.makeText(getApplicationContext(), "Password List", Toast.LENGTH_SHORT).show();

                break;
            case R.id.exit:
                //per uscire
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                Toast.makeText(getApplicationContext(), "Exit", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openDialog(View view, String text, String text2){
        SaveDialog saveDialog = new SaveDialog(text, text2); //oggetto della classe SaveDialog
        saveDialog.show(getSupportFragmentManager(), "example dialog");
    }

    public String GeneratePassword(){

        String letters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "1234567890";
        String special = ",.-_!?#";
        String password = "";


        Random random = new Random();
        int value;

        //TODO: eliminare la possibilità di inserire spazi vuoti
        //TODO: maggiore controllo sui tipi di caratteri immessi

        textPassword = findViewById(R.id.editText);
        textPassword.setVisibility(View.VISIBLE);

        //riproduzione souno
        generateSound.start();

        //calcolo della password
        for(int i=0; i<seekbar.getProgress()/6; i++) {

            value = random.nextInt(4 - 1 + 1) + 1;  //questa funzione ci da un numero randomico compreso tra 1 e 4

            if(cb_minusc.isChecked() &&  value == 1) {
                //lettera minuscola
                value = random.nextInt(25+ 1);
                password += letters.substring(value, value+1);
            }
            else if(cb_maiusc.isChecked() &&  value == 2) {
                //lettera maiuscola
                value = random.nextInt(25 + 1);
                password += letters.toUpperCase().substring(value, value + 1);
            }
            else if(cb_special.isChecked() &&  value == 3){
                //carattere speciale
                value = random.nextInt(6 + 1);
                password += special.substring(value, value+1);
            }
            else if(cb_numbers.isChecked() &&  value == 4) {
                //numero
                value = random.nextInt(9 + 1);
                password += numbers.substring(value, value + 1);
            }

            else {  //a volte capita che non entra in nessun if quindi resetto il ciclo con i--
                i--;
            }
        }
        return password;
    }

}

