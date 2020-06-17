package cn.com.ths.patternlocker;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import com.github.ihsg.demo.ui.whole.WholePatternCheckingActivity;
import com.github.ihsg.demo.ui.whole.WholePatternSettingActivity;


import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 思路手势密码插件
 */
public class ThsPatternLocker extends CordovaPlugin {
    private Context context;
    public  String MY_BDR_ACTION = "cn.com.ths.mybroadcastreceiver.action";
    public  String MY_BDR_PERMISSION = "cn.com.ths.mybroadcastreceiver.permission";
    private MyBroadcastReceiver myBroadcastReceiver;
    private  ThsPatternLocker instance;
    public ThsPatternLocker() {
        instance = this;
    }
    /**
     * 初始化插件
     *
     * @param cordova
     * @param webView
     */
    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        this.context = cordova.getActivity();
       
        IntentFilter intentFilter =new IntentFilter(MY_BDR_ACTION);
        myBroadcastReceiver = new MyBroadcastReceiver();
        //注册receiver时，指定发送者的权限，不然外部应用可以收到receiver
        this.context.registerReceiver(myBroadcastReceiver, intentFilter,MY_BDR_PERMISSION,null);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("setPwd")) { // 设置手势密码
            context.startActivity(new Intent(context, WholePatternSettingActivity.class));
            return true;
        } else if (action.equals("veryPwd")) { // 验证手势密码
            context.startActivity(new Intent(context, WholePatternCheckingActivity.class));
            return true;
        }else if (action.equals("veryPwd")) { // 验证手势密码
            context.startActivity(new Intent(context, WholePatternCheckingActivity.class));
            return true;
        }else if (action.equals("closeActivity")) { // 关闭验证activity
            WholePatternCheckingActivity.Companion.finishActivity();
            return true;
        }
        return false;
    }


    /**
     * 自定义广播接收者
     */
    class MyBroadcastReceiver extends BroadcastReceiver {
        private final String  TAG = "MyBroadcastReceiver";
        private int checkTimes = 0; // 检测测试
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(MY_BDR_ACTION)){
                boolean  onComplete = intent.getBooleanExtra("onComplete",false);
                String type = intent.getStringExtra("type");
                if(type.equals("check")){
                    if(onComplete){
                        sendMsg("success","onVeryPwd");
                    }
                }else if(type.equals("set")){
                    if(onComplete){
                        checkTimes++;
                        if(checkTimes==2){
                            sendMsg("success","onSetPwd");
                            checkTimes = 0;
                        }
                    }else{
                        checkTimes = 0;
                    }
                }
            }
        }
    }
    /**
     * 发送消息到
     * @param data
     * @param methodStr
     */
    private  void sendMsg(String data,String methodStr){
        String format = "cordova.plugins.ThsPatternLocker."+methodStr+"InAndroidCallback(%s);";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("res",data);
            final String js = String.format(format, jsonObject.toString());
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    instance.webView.loadUrl("javascript:" + js);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        if(myBroadcastReceiver!=null){
            this.context.unregisterReceiver(myBroadcastReceiver);
        }
        super.onDestroy();
    }
}
