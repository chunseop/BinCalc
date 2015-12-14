package cs.com.bincalc;

import android.app.Activity;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.concurrent.ConcurrentLinkedQueue;

public class Super extends Activity {
    public int myOperator;
    public boolean isNumberBefore;
    public int calcType = 1;
    public ConcurrentLinkedQueue<Long> numQueue = new ConcurrentLinkedQueue<>();

    public View.OnClickListener lisNumber = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (myOperator == AppContext.OPER_EQUALS) {
                numQueue.clear();
            }

            int i = Integer.parseInt(String.valueOf(((Button) v).getText()), 16);
            if (isNumberBefore) {
                inputNumberAppend(String.valueOf(((Button) v).getText()));
            } else {
                setAllValue(i);
                isNumberBefore = true;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
            return;
        }
        String dec = AppContext.getValue(AppContext.KEY_DEC_VALUE, true);
        if (dec != null && !dec.equals("")) {
            setAllValue(Long.parseLong(dec));
        }
    }

    protected void setListener(View.OnClickListener l, View ...views) {
        for (View v : views) {
            v.setOnClickListener(l);
        }
    }

    protected void calc() {
        if (numQueue.size() < 2) {
            return;
        }

        long resultDec = 0;
        switch (myOperator) {
            case AppContext.OPER_PLUS:
                resultDec = numQueue.poll() + numQueue.poll();
                break;
            case AppContext.OPER_MINUS:
                resultDec = numQueue.poll() - numQueue.poll();
                break;
            case AppContext.OPER_MULTIP:
                resultDec = numQueue.poll() * numQueue.poll();
                break;
            case AppContext.OPER_DIVIDE:
                resultDec = numQueue.poll() / numQueue.poll();
                break;
        }

        numQueue.add(resultDec);
        setAllValue(resultDec);
    }

    protected void setFontSize(final TextView v, String value, int lineLimit, boolean del) {
        v.setText(value);

        float orgSize = AppContext.getValue(AppContext.KEY_NUM_FONT_SIZE, false);
        float size = enlargeText(v, lineLimit);
        size = size > orgSize ? orgSize : size;

        v.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    protected void setValue(TextView v, long decValue, int type, boolean del) {
        String value;
        switch (type) {
            case AppContext.TYPE_DEC:
                value = String.valueOf(decValue);
                setFontSize(v, value, 1, del);
                break;
            case AppContext.TYPE_BIN:
                value = Long.toBinaryString(decValue);
                setFontSize(v, value, 2, del);
                break;
            case AppContext.TYPE_OCT:
                value = Long.toOctalString(decValue);
                setFontSize(v, value, 1, del);
                break;
            case AppContext.TYPE_HEX:
                value = Long.toHexString(decValue);
                setFontSize(v, value, 1, del);
                break;
        }
    }

    protected void resetOperButtonBg() {

    }

    protected void reset() {
        numQueue.clear();
        resetOperButtonBg();
    }

    protected void inverse(long value) {
        long i = 0 - value;
        setAllValue(i);
    }

    protected void setAllValue(long dec) {
        setAllValue(dec, false);
    }

    protected void setAllValue(long dec, boolean isDel) {
        // new override
    }

//    private int resizeText(TextView v, int lineLimit) {
//        CharSequence text = v.getText();
//        int width = v.getWidth() - v.getWidth() / 14;
//        int height = v.getHeight();
//        lineLimit = lineLimit > v.getLineCount() ? v.getLineCount() : lineLimit;
//        // Do not resize if the view does not have dimensions or there is no text
//        if (text == null || text.length() == 0 || height <= 0 || width <= 0) {
//            return 0;
//        }
//
//        TextPaint paint = v.getPaint();
//        int targetTextSize = (int)v.getTextSize();
//        int textHeight = this.getTextHeight(text, paint, width, targetTextSize);
//
//        while(textHeight > height) {
//            targetTextSize -= 2;
//            textHeight = getTextHeight(text, paint, width, targetTextSize);
//        }
//
//        int lineFixSize = targetTextSize;
//        int lineCount = v.getLineCount();
//        while (lineCount > lineLimit) {
//            lineFixSize -= 2;
//            lineCount = getTextLineCount(text, paint, width, lineFixSize);
//        }
//
//        targetTextSize = Math.min(lineFixSize, targetTextSize);
//        return targetTextSize;
//    }

    private int enlargeText(TextView v, int lineLimit) {
        CharSequence text = v.getText();
        int width = v.getWidth() - v.getWidth() / 14;
        int height = v.getHeight();
        lineLimit = lineLimit > v.getLineCount() ? v.getLineCount() : lineLimit;

        TextPaint paint = v.getPaint();
        float oldTextSize = v.getTextSize();
        int targetTextSize = (int)oldTextSize;
        int textHeight = this.getTextHeight(text, paint, width, targetTextSize);

        while (textHeight < height) {
            targetTextSize += 1;
            textHeight = getTextHeight(text, paint, width, targetTextSize);
        }

        textHeight = this.getTextHeight(text, paint, width, targetTextSize);
        while(textHeight > height) {
            targetTextSize -= 2;
            textHeight = getTextHeight(text, paint, width, targetTextSize);
        }

        int lineFixSize = targetTextSize;
        int lineCount = getTextLineCount(text, paint, width, lineFixSize);
        while (lineCount > lineLimit) {
            lineFixSize -= 2;
            lineCount = getTextLineCount(text, paint, width, lineFixSize);
        }

        return Math.min(lineFixSize, targetTextSize);
    }

    private int getTextHeight(CharSequence source, TextPaint paint, int width, float textSize) {
        TextPaint paintCopy = new TextPaint(paint);
        paintCopy.setTextSize(textSize);
        StaticLayout layout = new StaticLayout(source, paintCopy, width, Layout.Alignment.ALIGN_NORMAL, 1f, 0, true);
        return layout.getHeight();
    }

    private int getTextLineCount(CharSequence source, TextPaint paint, int width, float textSize) {
        TextPaint paintCopy = new TextPaint(paint);
        paintCopy.setTextSize(textSize);
        StaticLayout layout = new StaticLayout(source, paintCopy, width, Layout.Alignment.ALIGN_NORMAL, 1f, 0, true);
        return layout.getLineCount();
    }

//    protected void resetOperButtonPressedStatus(Button[] operButtons, Button b) {
//        for (Button operButton : operButtons) {
//            if (operButton == b) {
//                b.setBackgroundResource(R.drawable.oper_button_pressed_style);
//            } else {
//                operButton.setBackgroundResource(R.drawable.oper_button);
//            }
//        }
//    }

    protected void resetTypeButtonPressedStatus(Button[] typeButtons, Button b) {
        for (Button typeButton : typeButtons) {
            if (typeButton == b) {
                b.setBackgroundResource(R.drawable.type_button_pressed_style);
            } else {
                typeButton.setBackgroundResource(R.drawable.type_button);
            }
        }
    }

    public void inputNumberAppend(String i) {
        if (calcType == AppContext.TYPE_BIN && Integer.parseInt(i) > 1) {
            Toast.makeText(this, R.string.msg_binary_input_error, Toast.LENGTH_SHORT).show();
        }

        if (calcType == AppContext.TYPE_OCT && Integer.parseInt(i) > 8) {
            Toast.makeText(this, R.string.msg_octonary_input_error, Toast.LENGTH_SHORT).show();
        }
    }

    public String getTextValue(TextView t) {
        String text = String.valueOf(t.getText());
        if (text == null || text.equals("")) {
            return "0";
        } else {
            return text;
        }
    }
}
