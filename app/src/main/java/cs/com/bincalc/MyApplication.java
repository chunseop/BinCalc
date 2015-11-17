package cs.com.bincalc;

import android.app.Application;
import java.util.HashMap;

public class MyApplication extends Application {
    public HashMap<String, Object> mStorage = new HashMap<>();
    public static MyApplication mInstance= null;

    @Override
    public void onCreate() {
        mInstance = this;
        super.onCreate();
    }

    @Override
    public Object getSystemService(String name) {
        return super.getSystemService(name);
    }

    public void saveObject(String key,Object value) {
        mStorage.put(key, value);
    }

    public Object loadObject(String key,boolean remove) {
        Object value;
        if (remove) {
            value = mStorage.remove(key);
        } else {
            value = mStorage.get(key);
        }
        return value;
    }
}
