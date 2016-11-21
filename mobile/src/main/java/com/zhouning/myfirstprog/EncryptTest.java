package com.zhouning.myfirstprog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EncryptTest extends AppCompatActivity {

    EditText UnencryptE;
    TextView EncryptT;
    Button ok;
    String unEncryptStr,encryptStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt_test);
        UnencryptE = (EditText)findViewById(R.id.unencryptedittext);
        EncryptT = (TextView)findViewById(R.id.encrypttextview);
        ok = (Button)findViewById(R.id.encryptbutton);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doEncrypt();
            }
        });
    }

    private void doEncrypt() {
        unEncryptStr = UnencryptE.getText().toString();
        Encrypt enc = new Encrypt("E0FEE0FEF1FEF1FE");
        encryptStr = enc.encrypt(unEncryptStr);
        EncryptT.setText("Encrypted Str: \n"+encryptStr+"\nUnEncrypt Str: \n"+enc.decrypt(encryptStr));
    }
}
