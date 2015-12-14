package cs.com.bincalc;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HexActivity extends Super {
    private TextView tevDec, tevBin, tevOct, tevHex;
    private Button btnHexl;
    private Button btnPlusl, btnMinusl, btnDividel, btnMultiplel, btnEqualsl;
    private Button[] typeButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hex);

        Button btnAcl, btnPnl, btnDell;
        Button btnDecl, btnBinl, btnOctl;

        Button btnZerol, btnOnel, btnTwol, btnThreel, btnFourl, btnFivel, btnSixl,
                btnSevenl, btnEightl, btnNinel, btnA, btnB, btnC, btnD, btnE, btnF;

        tevDec = (TextView)findViewById(R.id.tevDecimall);
        tevBin = (TextView)findViewById(R.id.tevBinaryl);
        tevHex = (TextView)findViewById(R.id.tevHexl);

        btnAcl = (Button)findViewById(R.id.btnAcl);
        btnDell = (Button)findViewById(R.id.btnDell);
        btnDecl = (Button)findViewById(R.id.btnDecl);
        btnBinl = (Button)findViewById(R.id.btnBinl);
        btnOctl = (Button)findViewById(R.id.btnOctl);
        btnHexl = (Button)findViewById(R.id.btnHexl);

        btnPlusl = (Button)findViewById(R.id.btnPlusl);
        btnMinusl = (Button)findViewById(R.id.btnMinusl);
        btnDividel = (Button)findViewById(R.id.btnDividel);
        btnMultiplel = (Button)findViewById(R.id.btnMultipl);
        btnEqualsl = (Button)findViewById(R.id.btnEqualsl);
        btnPnl = (Button)findViewById(R.id.btnPNl);

        typeButtons = new Button[] { btnBinl, btnOctl, btnDecl, btnHexl };

        btnZerol = (Button)findViewById(R.id.btnZerol);
        btnOnel = (Button)findViewById(R.id.btnOnel);
        btnTwol = (Button)findViewById(R.id.btnTwol);
        btnThreel = (Button)findViewById(R.id.btnThreel);
        btnFourl = (Button)findViewById(R.id.btnFourl);
        btnFivel = (Button)findViewById(R.id.btnFivel);
        btnSixl = (Button)findViewById(R.id.btnSixl);
        btnSevenl = (Button)findViewById(R.id.btnSevenl);
        btnEightl = (Button)findViewById(R.id.btnEightl);
        btnNinel = (Button)findViewById(R.id.btnNinel);
        btnA = (Button)findViewById(R.id.btnA);
        btnB = (Button)findViewById(R.id.btnB);
        btnC = (Button)findViewById(R.id.btnC);
        btnD = (Button)findViewById(R.id.btnD);
        btnE = (Button)findViewById(R.id.btnE);
        btnF = (Button)findViewById(R.id.btnF);

        super.setListener(lisSys, btnAcl, btnPnl, btnDell);
        super.setListener(lisType, btnDecl, btnBinl, btnOctl, btnHexl);
        super.setListener(lisOperation, btnPlusl, btnMinusl, btnDividel, btnMultiplel, btnEqualsl);
        super.setListener(lisNumber, btnZerol, btnOnel, btnTwol, btnThreel, btnFourl, btnFivel, btnSixl,
                btnSevenl, btnEightl, btnNinel, btnA, btnB, btnC, btnD, btnE, btnF);

        if (getIntent().getExtras() != null) {
            calcType = getIntent().getExtras().getInt(AppContext.CALC_TYPE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        float fontSize = tevDec.getTextSize();
        AppContext.putValue(AppContext.KEY_NUM_FONT_SIZE, fontSize);

        btnHexl.callOnClick();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (tevDec.getText() != null) {
            AppContext.putValue(AppContext.KEY_DEC_VALUE, tevDec.getText());
        }
    }

    private View.OnClickListener lisSys = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.btnAcl:
                    reset();
                    break;
                case R.id.btnPNl:
                    if (tevDec.getText().equals("") || tevDec.getText().equals("0")) {
                        reset();
                        break;
                    }

                    Long value = Long.parseLong(String.valueOf(tevDec.getText()));
                    inverse(value);
                    break;
                case R.id.btnDell:
                    delete();
                    break;
            }
        }
    };

    private View.OnClickListener lisType = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.btnDecl:
                    calcType = AppContext.TYPE_DEC;
                    break;
                case R.id.btnBinl:
                    calcType = AppContext.TYPE_BIN;
                    break;
                case R.id.btnOctl:
                    Intent intent = new Intent(HexActivity.this, MainActivity.class);
                    intent.putExtra(AppContext.CALC_TYPE, AppContext.TYPE_OCT);
                    startActivity(intent);
                    break;
                case R.id.btnHexl:
                    calcType = AppContext.TYPE_HEX;
                    break;
            }

            resetTypeButtonPressedStatus(typeButtons, (Button) v);
        }
    };

    private View.OnClickListener lisOperation = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String txtDec = String.valueOf(tevDec.getText());
            if (txtDec != null && !txtDec.equals("") && isNumberBefore) {
                numQueue.add(Long.parseLong(txtDec));
            }
            if (numQueue.size() >= 2) {
                calc();
            }

            resetOperButtonBg();

            switch(v.getId()) {
                case R.id.btnPlusl:
                    myOperator = AppContext.OPER_PLUS;
                    break;
                case R.id.btnMinusl:
                    myOperator = AppContext.OPER_MINUS;
                    break;
                case R.id.btnDividel:
                    myOperator = AppContext.OPER_DIVIDE;
                    break;
                case R.id.btnMultipl:
                    myOperator = AppContext.OPER_MULTIP;
                    break;
                case R.id.btnEqualsl:
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

        switch(calcType) {
            case AppContext.TYPE_DEC:
            {
                String txtDec = tevDec.getText().toString();
                dec = Long.parseLong(txtDec);
                try {
                    dec = dec == 0 ? Long.parseLong(i) : Long.parseLong(txtDec + i);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return;
                }
                break;
            }
            case AppContext.TYPE_BIN:
            {
                String txtBin = tevBin.getText().toString();
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
            case AppContext.TYPE_HEX:
            {
                String txtOct = tevHex.getText().toString();
                if (txtOct.equals("0")) {
                    dec = Long.valueOf(txtOct + i, 16);
                } else {
                    try {
                        dec = Long.valueOf(txtOct + i, 16);
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
                if (tevBin.getText().equals("") || tevBin.getText().equals("0")) {
                    reset();
                } else {
                    value = String.valueOf(tevBin.getText().subSequence(0, tevBin.getText().length() - 1));
                    if (value.equals("")) {
                        reset();
                    } else {
                        dec = Long.valueOf(value, 2);
                    }
                }

                break;
            case AppContext.TYPE_DEC:
                if (tevDec.getText().equals("") || tevDec.getText().equals("0")) {
                    reset();
                } else {
                    value = String.valueOf(tevDec.getText().subSequence(0, tevDec.getText().length() - 1));
                    if (value.equals("")) {
                        reset();
                    } else {
                        dec = Long.valueOf(value);
                    }
                }
                break;
            case AppContext.TYPE_HEX:
                if (tevHex.getText().equals("") || tevHex.getText().equals("0")) {
                    reset();
                } else {
                    value = String.valueOf(tevHex.getText().subSequence(0, tevHex.getText().length() - 1));
                    if (value.equals("")) {
                        reset();
                    } else {
                        dec = Long.valueOf(value, 16);
                    }
                }
                break;
        }

        setAllValue(dec, true);
    }

    /**
     *
     * @param dec: decimal value
     * @param isDel: 是否是删除操作的值重置
     */
    @Override
    public void setAllValue(long dec, boolean isDel) {
        if (dec == 0) {
            reset();
        } else {
            setValue(tevDec, dec, AppContext.TYPE_DEC, isDel);
            setValue(tevBin, dec, AppContext.TYPE_BIN, isDel);
            setValue(tevHex, dec, AppContext.TYPE_HEX, isDel);
        }
    }

    @Override
    public void reset() {
        super.reset();

        tevDec.setText("");
        tevBin.setText("");
        tevHex.setText("");

        float textSize = AppContext.getValue(AppContext.KEY_NUM_FONT_SIZE, false);
        tevDec.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        tevBin.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        tevHex.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    @Override
    protected void resetOperButtonBg() {
        btnPlusl.setBackgroundResource(R.drawable.hex_oper_plus);
        btnMinusl.setBackgroundResource(R.drawable.hex_oper_minus);
        btnDividel.setBackgroundResource(R.drawable.hex_oper_divide);
        btnMultiplel.setBackgroundResource(R.drawable.hex_oper_multiply);
    }
}
