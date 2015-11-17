package cs.com.bincalc;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Super {
    private TextView tevDecimal, tevBinary, tevOctonary;
    private Button[] operButtons;
    private Button[] typeButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Button btnAc, btnPn, btnDel;
        Button btnDec, btnBin, btnOct, btnHex;
        Button btnPlus, btnMinus, btnDivide, btnMultiple, btnEquals;
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

        operButtons = new Button[] { btnPlus, btnMinus, btnMultiple, btnDivide};
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

        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        float fontSize = AppContext.px2sp(tevDecimal.getTextSize(), display.scaledDensity);
        AppContext.putValue(AppContext.KEY_NUM_FONT_SIZE, fontSize);
    }

    protected void onResume() {
        super.onResume();
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
        }
    }

    private OnClickListener lisSys = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.btnAc:
                    reset();
                    break;
                case R.id.btnPN:
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
                    // calcType = TYPE_HEX;
                    Intent intent = new Intent(MainActivity.this, HexActivity.class);
                    intent.putExtra(AppContext.CALC_TYPE, AppContext.TYPE_HEX);
                    startActivity(intent);
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

            switch(v.getId()) {
                case R.id.btnPlus:
                    myOperator = AppContext.OPER_PLUS;
                    break;
                case R.id.btnMinus:
                    myOperator = AppContext.OPER_MINUS;
                    break;
                case R.id.btnDivide:
                    myOperator = AppContext.OPER_DIVIDE;
                    break;
                case R.id.btnMultip:
                    myOperator = AppContext.OPER_MULTIP;
                    break;
                case R.id.btnEquals:
                    myOperator = AppContext.OPER_EQUALS;
                    break;
            }

            resetOperButtonPressedStatus(operButtons, (Button)v);
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
                if (txtDec.equals("0")) {
                    dec = Long.parseLong(i);
                } else {
                    try {
                        dec = Long.parseLong(tevDecimal.getText() + i);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        return;
                    }
                }

                break;
            }
            case AppContext.TYPE_BIN:
            {
                String txtBin = getTextValue(tevBinary);
                if (txtBin.equals("0")) {
                    dec = Long.valueOf(txtBin + i, 2);
                } else {
                    try {
                        dec = Long.valueOf(txtBin + i, 2);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        return;
                    }
                }

                break;
            }
            case AppContext.TYPE_OCT:
            {
                String txtOct = getTextValue(tevOctonary);
                if (txtOct.equals("0")) {
                    dec = Long.valueOf(txtOct + i, 8);
                } else {
                    try {
                        dec = Long.valueOf(txtOct + i, 8);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        return;
                    }
                }

                break;
            }
        }

        setAllValue(dec);
    }

    private void delete() {
        String value;
        Long dec = 0l;

        switch(calcType) {
            case AppContext.TYPE_BIN:
                if (tevBinary.getText().length() == 1) {
                    // do nothing;
                } else {
                    value = String.valueOf(tevBinary.getText().subSequence(0, tevBinary.getText().length() - 1));
                    dec = Long.valueOf(value, 2);
                }

                break;
            case AppContext.TYPE_OCT:
                if (tevOctonary.getText().length() == 1) {
                    // do nothing;
                } else {
                    value = String.valueOf(tevOctonary.getText().subSequence(0, tevOctonary.getText().length() - 1));
                    dec = Long.valueOf(value, 8);
                }
                break;
            case AppContext.TYPE_DEC:
                if (tevDecimal.getText().length() == 1) {
                    // do nothing;
                } else {
                    value = String.valueOf(tevDecimal.getText().subSequence(0, tevDecimal.getText().length() - 1));
                    dec = Long.valueOf(value);
                }
                break;
        }

        setAllValue(dec, true);
    }

//    protected void setFontSize(final TextView v, String value, int lineLimit) {
//        if (v == tevBinary && v.getLineCount() == 1) {
//            v.setTextSize((int)AppContext.getValue(AppContext.KEY_NUM_FONT_SIZE, false));
//        }
//
//        super.setFontSize(v, value, lineLimit);
//    }

    @Override
    protected void setAllValue(long dec, boolean isDel) {
        super.setValue(tevDecimal, dec, AppContext.TYPE_DEC, isDel);
        super.setValue(tevBinary, dec, AppContext.TYPE_BIN, isDel);
        super.setValue(tevOctonary, dec, AppContext.TYPE_OCT, isDel);
    }

    @Override
    public void reset() {
        super.reset();

        tevDecimal.setText("");
        tevBinary.setText("");
        tevOctonary.setText("");

        float size = AppContext.getValue(AppContext.KEY_NUM_FONT_SIZE, false);
        tevDecimal.setTextSize(size);
        tevBinary.setTextSize(size);
        tevOctonary.setTextSize(size);
    }
}
