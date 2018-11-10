package com.ej.calculadoraaritmetica;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.ls.LSSerializer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv, tv_last;
    private final int[] BTN_NUMBERS = {
            R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4, R.id.btn_5, R.id.btn_6,
        R.id.btn_7, R.id.btn_8, R.id.btn_9};
    private final int[] BTN_OTHERS = {
            R.id.btn_divide, R.id.btn_multiply, R.id.btn_rest, R.id.btn_plus, R.id.btn_dot,
            R.id.btn_equal, R.id.btn_back};
    private String last;
    private String[] h = new String[10];
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv_calc);
        tv_last = findViewById(R.id.tv_last);
        tv_last.setOnClickListener(this);
        sp = getSharedPreferences("Historia", Context.MODE_PRIVATE);
        for(int i =0; i < BTN_NUMBERS.length; i++){
            findViewById(BTN_NUMBERS[i]).setOnClickListener(this);
        }
        for(int i =0; i < BTN_OTHERS.length; i++){
            findViewById(BTN_OTHERS[i]).setOnClickListener(this);
        }
        // Actualizar array
        for(int i = 0; i < h.length; i++){
            h[i] = sp.getString("history_"+ i, String.valueOf(i));
        }

    }
    public  void updateHistorial(String niu){
        // añadir uno más que no cabe
        for(int i = 0; i < h.length - 1; i++){
            h[i] = h[i+1];
        }
        h[9] = niu;
        SharedPreferences.Editor e = sp.edit();
        for(int i = 0; i < h.length; i++){
            e.remove("history_"+ i);
            e.putString("history_"+ i,h[i]);
        }
        e.commit();
    }
    public void updateHistoryList(){
        for(int i = 0; i < h.length; i++){
            h[i] = sp.getString("history_"+ i, String.valueOf(i));
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == BTN_NUMBERS[0]) tv.append("0");
        else if (v.getId() == BTN_NUMBERS[1]) tv.append("1");
        else if (v.getId() == BTN_NUMBERS[2]) tv.append("2");
        else if (v.getId() == BTN_NUMBERS[3]) tv.append("3");
        else if (v.getId() == BTN_NUMBERS[4]) tv.append("4");
        else if (v.getId() == BTN_NUMBERS[5]) tv.append("5");
        else if (v.getId() == BTN_NUMBERS[6]) tv.append("6");
        else if (v.getId() == BTN_NUMBERS[7]) tv.append("7");
        else if (v.getId() == BTN_NUMBERS[8]) tv.append("8");
        else if (v.getId() == BTN_NUMBERS[9]) tv.append("9");
        else if (v.getId() == BTN_OTHERS[0]){
            if (!tv.getText().toString().isEmpty()) {
                tv.append(" / ");
                last = tv.getText().toString();
                refreshText();
            } else tv.setText("0");
        }
        else if (v.getId() == BTN_OTHERS[1]){
            if (!tv.getText().toString().isEmpty()) {
                tv.append(" * ");
                last = tv.getText().toString();
                refreshText();
            } else tv.setText("0");
        }
        else if (v.getId() == BTN_OTHERS[2]){
            if (!tv.getText().toString().isEmpty()) {
                tv.append(" - ");
                last = tv.getText().toString();
                refreshText();
            } else tv.setText("0");
        }
        else if (v.getId() == BTN_OTHERS[3]){
            if (!tv.getText().toString().isEmpty()) {
                tv.append(" + ");
                last = tv.getText().toString();
                refreshText();
            } else tv.setText("0");
        }
        else if (v.getId() == BTN_OTHERS[4]) tv.append(".");
        else if (v.getId() == BTN_OTHERS[5]){
            if (!tv.getText().toString().isEmpty() && !last.isEmpty()) {
                last += tv.getText().toString();
                String[] arit = last.split("\\s+");
                if (arit.length > 1) {
                    last.replace(arit[2], tv.getText().toString());
                    getCalculated(arit);
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < arit.length; i++) sb.append(arit[i]);
                    updateHistorial(sb.toString());
                }
            }/*else if (!tv.getText().toString().isEmpty() && last.isEmpty()){;
            }*/
        }
        else if (v.getId() == BTN_OTHERS[6]) {
            tv_last.setText("");
            last = "";
            tv.setText("");
        }else if (v.getId() == R.id.tv_last) {
            // aqui va el historial
            showHistorial();
        }
    }
    public void getCalculated(String[] t){
            Double c = 0.0;
            int d = 0;
        try{

            Double a = Double.parseDouble(t[0]);
            Double b = Double.parseDouble(t[2]);
            //Ponerle que guarde el last al intentar otra operación sin terminar
            switch (t[1]){
                case "*": c = a * b;
                 if (c % 1 ==0) {
                    d = c.intValue();
                    tv.setText(String.valueOf(d));
                     last = String.valueOf(d);
                 }else {
                     tv.setText(c.toString());
                     last = c.toString();
                 }break;

                case "/": c = a / b;
                    if (c % 1 ==0) {
                        d = c.intValue();
                        tv.setText(String.valueOf(d));
                        last = String.valueOf(d);
                    }else {
                        tv.setText(c.toString());
                        last = c.toString();
                    }break;

                case "-": c = a - b;
                    if (c % 1 ==0) {
                        d = c.intValue();
                        tv.setText(String.valueOf(d));
                        last = String.valueOf(d);
                    }else {
                        tv.setText(c.toString());
                        last = c.toString();
                    }break;

                case "+": c = a + b;
                    if (c % 1 ==0) {
                        d = c.intValue();
                        tv.setText(String.valueOf(d));
                        last = String.valueOf(d);
                    }else {
                        tv.setText(c.toString());
                        last = c.toString();
                    }break;
            }
        }catch (Exception e){
            tv.setText("Math Error");
        }
    }
    public void refreshText(){
        tv_last.setText(last);
        tv.setText("");
    }
    public void showHistorial(){
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        LayoutInflater inf = this.getLayoutInflater();
       // ListView hl = findViewById(R.id.historial_list);
        List<String> d = new ArrayList();
        ad.setView(inf.inflate(R.layout.historial_layout, null))
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        for(int i = 0; i < h.length; i++){
            d.add(h[i]);
            ad.setAdapter(new ArrayAdapter<>(MainActivity.this, R.layout.my_textview,d), null);
        }
        ad.show();
    }
}
