package cs.com.bincalc;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.Preference;
import android.util.DisplayMetrics;

public class AppContext {
	public static final String CALC_TYPE = "CALC_TYPE";
	public static final String KEY_DEC_VALUE = "DEC_VALUE";
    public static final String KEY_NUM_FONT_SIZE = "KEY_NUM_FONT_SIZE";
	public static final String KEY_NUM_VER_PADDING = "NUM_VER_PADDING";
	public static final String KEY_NUM_HOR_PADDING = "NUM_HOR_PADDING";

	public static final int TYPE_DEC = 1;
	public static final int TYPE_BIN = 2;
	public static final int TYPE_OCT = 3;
	public static final int TYPE_HEX = 4;

	public static final int OPER_EQUALS = 0;
	public static final int OPER_PLUS = 1;
	public static final int OPER_MINUS = 2;
	public static final int OPER_DIVIDE = 3;
	public static final int OPER_MULTIP = 4;

	public static Context getContext() {
		return MyApplication.mInstance;
	}

	/* AppContext Storage: Data Transfer  */
	public static void putValue(Class<?> cls, Object value) {
		String key = cls.getName();
		 MyApplication.mInstance.saveObject(key, value);
	}

	public static<T> T getValue(Class<?> cls, boolean remove) {
		String key = cls.getName();
		return (T)MyApplication.mInstance.loadObject(key,remove);
	}
	//TO store key-values
	public static void putValue(String key, Object value) {
		 MyApplication.mInstance.saveObject(key, value);
	}

	public static<T> T getValue(String key, boolean remove) {
		return (T)MyApplication.mInstance.loadObject(key,remove);
	}
}
