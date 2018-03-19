package shuai.yxs.watermark.View.CommandDialog;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;

/**
 * 自定义的TextView 监听返回键
 * Created by Administrator on 2017/7/21.
 */

public class BackEdittext extends AppCompatEditText {
    interface PressBackCallBack {
        void callBack();
    }

    private PressBackCallBack callBack;

    public void setCallBack(PressBackCallBack callBack) {
        this.callBack = callBack;
    }

    public BackEdittext(Context context) {
        super(context);
    }

    public BackEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() != KeyEvent.ACTION_UP) {//不响应按键抬起时的动作
            if (callBack != null) {
                callBack.callBack();
            }
        }
        return true;
    }

}
