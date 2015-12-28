package calculator.simplemobiletools.com.simple_calculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.result) TextView result;

    private double baseValue;
    private double secondValue;
    private boolean resetValue;
    private int lastKey;
    private int lastOperation;

    public static final int DIGIT = 0;
    public static final int EQUALS = 1;
    public static final int PLUS = 2;
    public static final int MINUS = 3;
    public static final int MULTIPLY = 4;
    public static final int DIVIDE = 5;
    public static final int MODULO = 6;
    public static final int POWER = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        resetValues();
    }

    private void resetValues() {
        baseValue = 0;
        secondValue = 0;
        resetValue = false;
        lastKey = 0;
        lastOperation = 0;
        setResult("0");
    }

    public void addDigit(int number) {
        final String currentValue = getDisplayedNumber();
        final String newValue = removeLeadingZero(currentValue + number);
        setResult(newValue);
    }

    public void setResult(String value) {
        result.setText(value);
    }

    private String removeLeadingZero(String str) {
        final double doubleValue = Double.parseDouble(str);
        return Formatter.doubleToString(doubleValue);
    }

    private String getDisplayedNumber() {
        return result.getText().toString();
    }

    public double getDisplayedNumberAsDouble() {
        return Double.parseDouble(getDisplayedNumber());
    }

    private void resetValueIfNeeded() {
        if (resetValue)
            setResult("0");

        resetValue = false;
    }

    private void divideNumbers() {
        double resultValue = 0;
        if (secondValue != 0)
            resultValue = baseValue / secondValue;

        updateResult(resultValue);
    }

    private void moduloNumbers() {
        double resultValue = 0;
        if (secondValue != 0)
            resultValue = baseValue % secondValue;

        updateResult(resultValue);
    }

    private void powerNumbers() {
        double resultValue = Math.pow(baseValue, secondValue);
        if (Double.isInfinite(resultValue) || Double.isNaN(resultValue))
            resultValue = 0;
        updateResult(resultValue);
    }

    public void handleOperation(int operation) {
        if (lastKey == operation)
            return;

        if (lastKey == DIGIT)
            getResult();

        resetValue = true;
        lastKey = operation;
        lastOperation = operation;
    }

    @OnClick(R.id.btn_plus)
    public void plusClicked() {
        handleOperation(PLUS);
    }

    @OnClick(R.id.btn_minus)
    public void minusClicked() {
        handleOperation(MINUS);
    }

    @OnClick(R.id.btn_multiply)
    public void multiplyClicked() {
        handleOperation(MULTIPLY);
    }

    @OnClick(R.id.btn_divide)
    public void divideClicked() {
        handleOperation(DIVIDE);
    }

    @OnClick(R.id.btn_modulo)
    public void moduloClicked() {
        handleOperation(MODULO);
    }

    @OnClick(R.id.btn_power)
    public void powerClicked() {
        handleOperation(POWER);
    }

    @OnClick(R.id.btn_root)
    public void rootClicked() {
        getResult();
        lastOperation = EQUALS;
        updateResult(Math.sqrt(baseValue));
    }

    @OnClick(R.id.btn_clear)
    public void clearClicked() {
        final String oldValue = getDisplayedNumber();
        String newValue;
        final int len = oldValue.length();
        int minLen = 1;
        if (oldValue.contains("-"))
            minLen++;

        if (len > minLen)
            newValue = oldValue.substring(0, len - 1);
        else
            newValue = "0";

        if (newValue.equals("-0"))
            newValue = "0";

        setResult(newValue);
        baseValue = Double.parseDouble(newValue);
    }

    @OnLongClick(R.id.btn_clear)
    public boolean clearLongClicked() {
        resetValues();
        return true;
    }

    @OnClick(R.id.btn_equals)
    public void equalsClicked() {
        if (lastKey == EQUALS)
            calculateResult();

        if (lastKey != DIGIT)
            return;

        secondValue = getDisplayedNumberAsDouble();
        calculateResult();
        lastKey = EQUALS;
    }

    public void decimalClicked() {
        String value = getDisplayedNumber();
        if (!value.contains("."))
            value += ".";
        setResult(value);
    }

    public void zeroClicked() {
        String value = getDisplayedNumber();
        if (!value.equals("0"))
            value += "0";
        setResult(value);
    }

    private void updateResult(double value) {
        setResult(Formatter.doubleToString(value));
        baseValue = value;
    }

    public void getResult() {
        secondValue = getDisplayedNumberAsDouble();
        calculateResult();
        baseValue = getDisplayedNumberAsDouble();
    }

    public void calculateResult() {
        switch (lastOperation) {
            case PLUS:
                updateResult(baseValue + secondValue);
                break;
            case MINUS:
                updateResult(baseValue - secondValue);
                break;
            case MULTIPLY:
                updateResult(baseValue * secondValue);
                break;
            case DIVIDE:
                divideNumbers();
                break;
            case MODULO:
                moduloNumbers();
                break;
            case POWER:
                powerNumbers();
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.btn_decimal, R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8,
            R.id.btn_9})
    public void numpadClicked(View view) {
        if (lastKey == EQUALS)
            lastOperation = EQUALS;
        lastKey = DIGIT;
        resetValueIfNeeded();

        switch (view.getId()) {
            case R.id.btn_decimal:
                decimalClicked();
                break;
            case R.id.btn_0:
                zeroClicked();
                break;
            case R.id.btn_1:
                addDigit(1);
                break;
            case R.id.btn_2:
                addDigit(2);
                break;
            case R.id.btn_3:
                addDigit(3);
                break;
            case R.id.btn_4:
                addDigit(4);
                break;
            case R.id.btn_5:
                addDigit(5);
                break;
            case R.id.btn_6:
                addDigit(6);
                break;
            case R.id.btn_7:
                addDigit(7);
                break;
            case R.id.btn_8:
                addDigit(8);
                break;
            case R.id.btn_9:
                addDigit(9);
                break;
            default:
                break;
        }
    }
}