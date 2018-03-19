package shuai.yxs.watermark.View.CommandDialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import shuai.yxs.watermark.R;
import shuai.yxs.watermark.yxsUtils.Utils;

/**
 * 点击评论弹出输入框
 * 自定义弹窗需要在OnCreate中setContentView
 * 以前写的，直接拉过来了，懒得改Kotlin了
 * Created by Administrator on 2017/7/21.
 */
public class SendCommandDialog extends AlertDialog {
    private BackEdittext mEdit_commane;
    private ImageView mTop, mBottom;
    private boolean isShow = false;

    private final InputMethodManager mInputKey;

    public SendCommandDialog(Context context) {
        super(context, R.style.CustomDialog);
        mInputKey = Utils.getInputKeyState(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setCancelable(true);//是否可以取消 (也可以在调用处设置)
        setCanceledOnTouchOutside(true);//是否点击外部消失

        View view = View.inflate(getContext(), R.layout.item_command_dialog, null);
        mEdit_commane = (BackEdittext) view.findViewById(R.id.Item_Dialog_Edit);
        mTop = (ImageView) view.findViewById(R.id.Item_Dialog_Top);
        mBottom = (ImageView) view.findViewById(R.id.Item_Dialog_Bottom);


        initListener();

        setContentView(view);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Window dialog_window = this.getWindow();

        dialog_window.setGravity(Gravity.BOTTOM);//设置显示的位置
        dialog_window.setAttributes(params);//设置显示的大小
    }

    public void setSendListener(final OnSendCommandListener onSend) {
        mEdit_commane.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onSend.send(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void setChangeSizeListener(final OnChangeTextSize onChangeTextSize) {
        mTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChangeTextSize.Top();
            }
        });
        mBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChangeTextSize.Bottom();
            }
        });
    }

    public void setText(String str) {
        if (mEdit_commane != null) {
            mEdit_commane.setText(str);
        }
    }

    private void initListener() {
        mEdit_commane.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if (focused) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);//使得点击 Dialog 中的EditText 可以弹出键盘
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);//总是显示键盘
                }
            }
        });

        //自定义的 EditText 监听 Back 键的点击( 在软键盘显示的情况下 )
        mEdit_commane.setCallBack(new BackEdittext.PressBackCallBack() {
            @Override
            public void callBack() {
                if (isShow) {
                    Utils.PackUpInputKey(mInputKey, mEdit_commane);
                    isShow = false;
                } else
                    dismiss();

            }
        });
    }

    @Override
    public void show() {
        super.show();
        isShow = true;
    }

    //动态修改水印文字
    public interface OnSendCommandListener {
        void send(String msg);
    }

    //改变字体大小
    public interface OnChangeTextSize {
        void Top(); //变大

        void Bottom(); //变小
    }

    /**
     * 设置Edit点击是隐藏软键盘还是消失
     *
     * @param b
     */
    public void setIsShow(boolean b) {
        this.isShow = b;
    }

}
