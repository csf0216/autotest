package autotest.example.test.autotest;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class MyAccessService extends AccessibilityService {
    private static final String TAG = "MyAccessService";
    private int totaltimes=0, sucesstimes=0;
    private Handler mHandler = new Handler();
    
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                if (event.getClassName().equals("android.widget.TextView")) {
                    AccessibilityNodeInfo node1=getNodeByText("立即更新");
                    if (node1 != null) {
                        clickByText("立即更新");
                    }
                    if (getNodeByText("升级完成") != null) {
                        totaltimes++;
                        sucesstimes++;
                        Log.d(TAG, "totaltimes: "+totaltimes+" sucesstimes: "+sucesstimes);
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                clickById("com.dhuav.android:id/setting_select_close");
                            }
                        }, 100000);
                    } else if (getNodeByText("升级失败") != null) {
                        totaltimes++;
                        Log.d(TAG, "totaltimes: "+totaltimes+" sucesstimes: "+sucesstimes);
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                clickById("com.dhuav.android:id/setting_select_close");
                            }
                        }, 100000);
                    }
                }
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                if (event.getClassName().equals("com.dhuav.android.activities.MainActivity")) {
                    clickById("com.dhuav.android:id/main_top_setting");
                } else if (event.getClassName().equals("com.dhuav.android.activities.SettingActivity")) {
                    clickByIndex("com.dhuav.android:id/setting_left_listview", 4);
                }
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                Log.d(TAG, "onAccessibilityEvent: TYPE_VIEW_TEXT_CHANGED "+event.getText());
                break;
        }
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "onInterrupt: ");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void clickById(String id) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            //为了演示,直接查看了红包控件的id
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(id);
            nodeInfo.recycle();
            for (AccessibilityNodeInfo item : list) {
                item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void clickByText(String txt) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            //为了演示,直接查看了红包控件的id
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(txt);
            nodeInfo.recycle();
            for (AccessibilityNodeInfo item : list) {
                item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void clickByIndex(String id, int index) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            //为了演示,直接查看了红包控件的id
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(id);
            nodeInfo.recycle();
            for (AccessibilityNodeInfo item : list) {
                AccessibilityNodeInfo leftside = item.getChild(index);
                leftside.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private AccessibilityNodeInfo getNodeByText(String txt) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            //为了演示,直接查看了红包控件的id
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(txt);
            nodeInfo.recycle();
            if (list.size() == 1) {
                return list.get(0);
            } else if (list.size() > 1){
                Log.d(TAG, "getNodeByText: "+list.size());
            }
        }
        return null;
    }

}
