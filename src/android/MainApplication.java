package cn.com.ths.patternlocker;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import com.github.ihsg.demo.IComponentApplication;


public class MainApplication extends Application {
    private static final String TAG = "Init";
    private static final String[] MODULESLIST =
            {"com.github.ihsg.demo.PatternLockApplication",
                    "com.moduleA.B"};
    private static MainApplication instante;
    public synchronized static MainApplication getInstance() {
        return instante;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instante = this;
        // 加载依赖模块的App
        modulesApplicationInit();
    }
    

    private void modulesApplicationInit(){
        for (String moduleImpl : MODULESLIST){
            try {
                Class<?> clazz = Class.forName(moduleImpl);
                Object obj = clazz.newInstance();
                if (obj instanceof IComponentApplication){
                    ((IComponentApplication) obj).init(MainApplication.getInstance());
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }
}
