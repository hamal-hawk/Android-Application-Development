package com.example.tipsplit;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {

    EditText totalBill;
    RadioGroup radioGroup;
    TextView tipAmount;
    TextView totalAmount;
    EditText numPeople;
    TextView totalPerPerson;
    static String tipAmount_store = "TipAmount";
    static String totalAmount_store = "TotalAmount";
    static String totalPerPerson_store = "TotalPerPerson";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tipAmount = findViewById(R.id.tipAmount);
        totalAmount = findViewById(R.id.totalAmount);
        totalPerPerson = findViewById(R.id.totalPerPerson);

        if (savedInstanceState != null) {
            CharSequence tip_text = savedInstanceState.getCharSequence(tipAmount_store);
            CharSequence total_text = savedInstanceState.getCharSequence(totalAmount_store);
            CharSequence totalPerPerson_text = savedInstanceState.getCharSequence(totalPerPerson_store);
            tipAmount.setText(tip_text);
            totalAmount.setText(total_text);
            totalPerPerson.setText(totalPerPerson_text);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){

        // Saving state for restoring during orientation changes.

        tipAmount = findViewById(R.id.tipAmount);
        totalAmount = findViewById(R.id.totalAmount);
        totalPerPerson = findViewById(R.id.totalPerPerson);
        super.onSaveInstanceState(outState);
        outState.putCharSequence(tipAmount_store, tipAmount.getText());
        outState.putCharSequence(totalAmount_store, totalAmount.getText());
        outState.putCharSequence(totalPerPerson_store, totalPerPerson.getText());
    }


    public void processTip(View v) {

        /*
        This method runs when the radio button is checked to
        calculate the tip amount and the total amount including tip.
         */

        radioGroup = findViewById(R.id.radioG);
        totalBill = findViewById(R.id.billTotal);

        if(totalBill.getText().toString().equals("")){
            radioGroup.clearCheck();
        }
        else {
            tipAmount = findViewById(R.id.tipAmount);
            totalAmount = findViewById(R.id.totalAmount);
            int rButtonId = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = findViewById(rButtonId);
            double tipPercentValue = Double.parseDouble((radioButton.getText().toString()).substring(0,2));
            double totalBillAmountValue = Double.parseDouble(totalBill.getText().toString());
            double tipAmountValue = (tipPercentValue / 100) * totalBillAmountValue;
            double totalAmountValue = tipAmountValue + totalBillAmountValue;
            tipAmount.setText("$ " + String.format("%.2f", tipAmountValue));
            totalAmount.setText("$ " + String.format("%.2f", totalAmountValue));
        }
    }

    public void processTotalPerPerson(View v) {

        /*
        This method runs when the GO button is clicked to calculate the total amount
        to be paid per person.
         */

        numPeople = findViewById(R.id.numPeople);
        totalBill = findViewById(R.id.billTotal);
        radioGroup = findViewById(R.id.radioG);
        totalAmount = findViewById(R.id.totalAmount);
        if(totalBill.getText().toString().equals("") || numPeople.getText().toString().equals("")
        || radioGroup.getCheckedRadioButtonId() == -1){
            Toast toast = Toast.makeText(getApplicationContext(), "Enter values!", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if(numPeople.getText().toString().equals("0")){
            Toast toast = Toast.makeText(getApplicationContext(), "Invalid value!", Toast.LENGTH_SHORT);
            toast.show();
        }
        else{
            totalPerPerson = findViewById(R.id.totalPerPerson);
            DecimalFormat decimalFormat = new DecimalFormat(("0.00"));
            decimalFormat.setRoundingMode(RoundingMode.UP);
            long n = Long.parseLong(numPeople.getText().toString());
            totalPerPerson.setText("$ "+decimalFormat.format(Double.parseDouble(totalAmount.getText().toString().substring(2))/n));
        }

    }

    public void clearAll(View v){

        /*
        Implementation for the CLEAR button. It clears all fields including radio buttons
        when clicked.
         */

        totalBill = findViewById(R.id.billTotal);
        numPeople = findViewById(R.id.numPeople);
        radioGroup = findViewById(R.id.radioG);
        tipAmount = findViewById(R.id.tipAmount);
        totalAmount = findViewById(R.id.totalAmount);
        totalPerPerson = findViewById(R.id.totalPerPerson);
        if(totalBill.getText().toString().equals("") && numPeople.getText().toString().equals("")
                && tipAmount.getText().toString().equals("") && totalAmount.getText().toString().equals("")
                && totalPerPerson.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "No fields to clear!", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            radioGroup.clearCheck();
            totalBill.setText("");
            tipAmount.setText("");
            totalAmount.setText("");
            numPeople.setText("");
            totalPerPerson.setText("");
        }

    }


}