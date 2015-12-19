package cs.com.bincalc;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Super {
    private TextView tevDecimal, tevBinary, tevOctonary;
    private Button btnDec, btnBin, btnOct, btnHex;
    private Button[] typeButtons;
    private Button btnPlus, btnMinus, btnDivide, btnMultiple, btnEquals;
    private BackPressClose backClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Button btnAc, btnPn, btnDel;
        Button btnZero, btnOne, btnTwo, btnThree, btnFour,
                btnFive, btnSix, btnSeven, btnEight, btnNine;

        setContentView(R.layout.activity_main);

        tevDecimal = (TextView)findViewById(R.id.tevDecimal);
        tevBinary = (TextView)findViewById(R.id.tevBinary);
        tevOctonary = (TextView)findViewById(R.id.tevOctonary);

        btnAc = (Button)findViewById(R.id.btnAc);
        btnDel = (Button)findViewById(R.id.btnDel);
        btnDec = (Button)findViewById(R.id.btnDec);
        btnBin = (Button)findViewById(R.id.btnBin);
        btnOct = (Button)findViewById(R.id.btnOct);
        btnHex = (Button)findViewById(R.id.btnHex);
        btnPlus = (Button)findViewById(R.id.btnPlus);
        btnMinus = (Button)findViewById(R.id.btnMinus);
        btnDivide = (Button)findViewById(R.id.btnDivide);
        btnMultiple = (Button)findViewById(R.id.btnMultip);
        btnEquals = (Button)findViewById(R.id.btnEquals);
        btnPn = (Button)findViewById(R.id.btnPN);

        //operButtons = new Button[] { btnPlus, btnMinus, btnMultiple, btnDivide};
        typeButtons = new Button[] { btnBin, btnOct, btnDec, btnHex};

        btnZero = (Button)findViewById(R.id.btnZero);
        btnOne = (Button)findViewById(R.id.btnOne);
        btnTwo = (Button)findViewById(R.id.btnTwo);
        btnThree = (Button)findViewById(R.id.btnThree);
        btnFour = (Button)findViewById(R.id.btnFour);
        btnFive = (Button)findViewById(R.id.btnFive);
        btnSix = (Button)findViewById(R.id.btnSix);
        btnSeven = (Button)findViewById(R.id.btnSeven);
        btnEight = (Button)findViewById(R.id.btnEight);
        btnNine = (Button)findViewById(R.id.btnNine);

        super.setListener(lisSys, btnAc, btnPn, btnDel);
        super.setListener(lisType, btnDec, btnBin, btnOct, btnHex, btnPlus);
        super.setListener(lisOperation, btnPlus, btnMinus, btnDivide, btnMultiple, btnEquals);
        super.setListener(lisNumber, btnZero, btnOne, btnTwo, btnThree, btnFour,
                btnFive, btnSix, btnSeven, btnEight, btnNine);

        backClose = new BackPressClose(this);
    }

    protected void onResume() {
        super.onResume();

        if (getIntent().getExtras() != null) {
            calcType = getIntent().getIntExtra(AppContext.CALC_TYPE, AppContext.TYPE_DEC);
        }

        switch(calcType) {
            case AppContext.TYPE_DEC:
                btnDec.callOnClick();
                break;
            case AppContext.TYPE_BIN:
                btnBin.callOnClick();
                break;
            case AppContext.TYPE_OCT:
                btnOct.callOnClick();
                break;
            case AppContext.TYPE_HEX:
                btnHex.callOnClick();
                break;
        }

        float fontSize = tevDecimal.getTextSize();
        AppContext.putValue(AppContext.KEY_NUM_FONT_SIZE, fontSize);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (tevDecimal.getText() != null) {
            AppContext.putValue(AppContext.KEY_DEC_VALUE, tevDecimal.getText());
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            super.onWindowFocusChanged(hasFocus);
//            AppContext.putValue(AppContext.KEY_NUM_VER_PADDING, tevDecimal.getTotalPaddingTop() + tevDecimal.getTotalPaddingBottom());
//            AppContext.putValue(AppContext.KEY_NUM_HOR_PADDING, tevDecimal.getTotalPaddingStart() + tevDecimal.getTotalPaddingEnd());
//            Log.d("TAG", "textColor: " + typeButtons[0].getCurrentTextColor());
//            Log.d("TAG", "textColor: " + typeButtons[0].getTextColors());
        }
    }

    @Override
    public void onBackPressed() {
        backClose.onBackPressed();
    }

    private OnClickListener lisSys = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.btnAc:
                    reset();
                    break;
                case R.id.btnPN:
                    if (tevDecimal.getText().equals("") || tevDecimal.getText().equals("0")) {
                        reset();
                        break;
                    }

                    Long value = Long.parseLong(String.valueOf(tevDecimal.getText()));
                    inverse(value);
                    break;
                case R.id.btnDel:
                    delete();
                    break;
            }
        }
    };

    private OnClickListener lisType = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.btnDec:
                    calcType = AppContext.TYPE_DEC;
                    break;
                case R.id.btnBin:
                    calcType = AppContext.TYPE_BIN;
                    break;
                case R.id.btnOct:
                    calcType = AppContext.TYPE_OCT;
                    break;
                case R.id.btnHex:
                    Intent intent = new Intent(MainActivity.this, HexActivity.class);
                    intent.putExtra(AppContext.CALC_TYPE, AppContext.TYPE_HEX);
                    startActivity(intent);
                    //finish();
                    break;
            }

            resetTypeButtonPressedStatus(typeButtons, (Button) v);
        }
    };

    private OnClickListener lisOperation = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String txtDec = String.valueOf(tevDecimal.getText());
            if (txtDec != null && !txtDec.equals("") && isNumberBefore) {
                numQueue.add(Long.parseLong(txtDec));
            }
            if (numQueue.size() >= 2) {
                calc();
            }

            resetOperButtonBg();

            switch(v.getId()) {
                case R.id.btnPlus:
                    myOperator = AppContext.OPER_PLUS;
                    v.setBackgroundResource(R.mipmap.plus_active);
                    break;
                case R.id.btnMinus:
                    myOperator = AppContext.OPER_MINUS;
                    v.setBackgroundResource(R.mipmap.minus_active);
                    break;
                case R.id.btnDivide:
                    myOperator = AppContext.OPER_DIVIDE;
                    v.setBackgroundResource(R.mipmap.divide_active);
                    break;
                case R.id.btnMultip:
                    myOperator = AppContext.OPER_MULTIP;
                    v.setBackgroundResource(R.mipmap.multiply_active);
                    break;
                case R.id.btnEquals:
                    myOperator = AppContext.OPER_EQUALS;
                    break;
            }

            //resetOperButtonPressedStatus(operButtons, (Button)v);
            isNumberBefore = false;
        }
    };

    @Override
    public void inputNumberAppend(String i) {
        super.inputNumberAppend(i);
        long dec = 0l;

        switch (calcType) {
            case AppContext.TYPE_DEC:
            {
                String txtDec = getTextValue(tevDecimal);
                try {
                    dec = Long.parseLong(txtDec + i);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return;
                }

                break;
            }
            case AppContext.TYPE_BIN:
            {
                String txtBin = getTextValue(tevBinary);
                try {
                    dec = Long.valueOf(txtBin + i, 2);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return;
                }

                break;
            }
            case AppContext.TYPE_OCT:
            {
                String txtOct = getTextValue(tevOctonary);
                try {
                    dec = Long.valueOf(txtOct + i, 8);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return;
                }

                break;
            }
        }

        setAllValue(dec);
    }

    private void delete() {
        String value;
        long dec = 0;

        switch(calcType) {
            case AppContext.TYPE_BIN:
                if (tevBinary.getText().equals("") || tevBinary.getText().equals("0")) {
                    reset();
                } else {
                    value = String.valueOf(tevBinary.getText().subSequence(0, tevBinary.getText().length() - 1));

                    if (value.equals("")) {
                        reset();
                    } else {
                        dec = Long.valueOf(value, 2);
                    }
                }

                break;
            case AppContext.TYPE_OCT:
                if (tevOctonary.getText().equals("") || tevOctonary.getText().equals("0")) {
                    reset();
                } else {
                    value = String.valueOf(tevOctonary.getText().subSequence(0, tevOctonary.getText().length() - 1));
                    if (value.equals("")) {
                        reset();
                    } else {
                        dec = Long.valueOf(value, 8);
                    }
                }
                break;
            case AppContext.TYPE_DEC:
                if (tevDecimal.getText().equals("") || tevDecimal.getText().equals("0")) {
                    reset();
                } else {
                    value = String.valueOf(tevDecimal.getText().subSequence(0, tevDecimal.getText().length() - 1));
                    if (value.equals("")) {
                        reset();
                    } else {
                        dec = Long.valueOf(value);
                    }
                }
                break;
        }

        setAllValue(dec, true);
    }

    @Override
    protected void setAllValue(long dec, boolean isDel) {
        if (dec == 0) {
            reset();
        } else {
            super.setValue(tevDecimal, dec, AppContext.TYPE_DEC, isDel);
            super.setValue(tevBinary, dec, AppContext.TYPE_BIN, isDel);
            super.setValue(tevOctonary, dec, AppContext.TYPE_OCT, isDel);
        }
    }

    @Override
    protected void reset() {
        super.reset();

        tevDecimal.setText("");
        tevBinary.setText("");
        tevOctonary.setText("");

        float size = AppContext.getValue(AppContext.KEY_NUM_FONT_SIZE, false);
        tevDecimal.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        tevBinary.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        tevOctonary.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    @Override
    protected void resetOperButtonBg() {
        btnPlus.setBackgroundResource(R.drawable.oper_plus);
        btnMinus.setBackgroundResource(R.drawable.oper_minus);
        btnDivide.setBackgroundResource(R.drawable.oper_divide);
        btnMultiple.setBackgroundResource(R.drawable.oper_multiply);
    }
}
