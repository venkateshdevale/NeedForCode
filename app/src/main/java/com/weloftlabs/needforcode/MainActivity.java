package com.weloftlabs.needforcode;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Author Venkatesh Devale developed on 15th August 2015; Contact: mr.venkatesh.d@gmail.com
 */

public class MainActivity extends ActionBarActivity {

    // Variables
    private static final String[] mTensPower = {"", " thousand", " lakh"};
    private static final String[] mTensMultiple = {"", " ten", " twenty", " thirty", " forty",
            " fifty", " sixty", " seventy", " eighty", " ninety"};
    private static final String[] mTillTwenty = {"", " one", " two", " three", " four", " five",
            " six", " seven", " eight", " nine", " ten", " eleven", " twelve", " thirteen",
            " fourteen", " fifteen", " sixteen", " seventeen", " eighteen", " nineteen"};

    // Views
    private TextView mOutputTv;
    private EditText mInputEt;
    private Switch mModeOfOutputSwh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        /**
         * To read the input data dynamically, we are using a TextWatcher which listens at the EditText.
         */
        mInputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mInputEt.length() != 0) {
                    String output = convertToWords(Integer.valueOf(charSequence.toString()));
                    mOutputTv.setText(output.substring(0, 1).toUpperCase() + output.substring(1));
                } else {
                    mOutputTv.setText("Output");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        /**
         *  Listener to dynamically change the input data according to the modes selected by user.
         */
        mModeOfOutputSwh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mInputEt.length() != 0) {
                    String output = convertToWords(Integer.valueOf(mInputEt.getText().toString()));
                    mOutputTv.setText(output.substring(0, 1).toUpperCase() + output.substring(1));
                } else {
                    mOutputTv.setText("Output");
                }
            }
        });

    }

    /**
     * Initializes UI elements such as Views and Viewgroup here
     */
    private void init() {
        mOutputTv = (TextView) findViewById(R.id.output_textview);
        mInputEt = (EditText) findViewById(R.id.input_textview);
        mModeOfOutputSwh = (Switch) findViewById(R.id.mode_of_output_switch);
    }

    /**
     * Function to convert any numerical less than thousand to words.
     *
     * @param number to be converted.
     * @param place  to detect hundredth unit; to add 'and' word.
     * @return String which was generated corresponding to the input number.
     */
    private String numebersToWordsWithinThousand(int number, int place) {
        String currentNum;

        // If the input is less than 20 then return the corresponding word from the list mTillTwenty.
        if (number % 100 < 20) {
            currentNum = mTillTwenty[number % 100];
            number /= 100;
        } else {
            currentNum = mTillTwenty[number % 10];
            number /= 10;

            // If its a first iteration then add " and" after hundreds place.
            // For example, three hundred and sixty nine
            if (place == 0 && number > 10)
                currentNum = " and" + mTensMultiple[number % 10] + currentNum;
            else
                currentNum = mTensMultiple[number % 10] + currentNum;

            number /= 10;
        }

        if (number == 0) return currentNum;

        // If its the second iteration, that is three numbers starting from the highest power,
        // add " hundred" is mode is set to American version else add " lakh"
        if (place == 1 && !mModeOfOutputSwh.isChecked())
            return mTillTwenty[number] + " lakh" + currentNum;
        else
            return mTillTwenty[number] + " hundred" + currentNum;
    }

    /**
     * @param inputNum which has to be converted in to words
     * @return String of words which was the result of conversion of numerical user input
     */
    public String convertToWords(int inputNum) {

        /**
         * If input is zero then return zero else proceed further.
         */
        if (inputNum == 0) {
            return "zero";
        }

        /*
              Commenting these lines because I have already configured the InputType to TYPE_CLASS_NUMBER
        for the EditText which will prevent the user from entering NEGATIVE numbers.

              Even the input length to the EditText is configured using maxLength function directly in
        layout XML

        if (number < 0) {
            mInputEt.setError("Invalid input : Found negative number");
        }
        */

        String current = "";

        // To save the iteration count
        int place = 0;

        do {
            int number = inputNum % 1000; /* Because naming convention for the numerical would become
                                        symmetric for every 1000 multiple*/

            if (number != 0) {
                String s = numebersToWordsWithinThousand(number, place);
                current = s + mTensPower[place] + current;
            }
            place++;
            inputNum /= 1000;
        } while (inputNum > 0);

        return (current).trim();
    }


}
