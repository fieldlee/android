package cn.com.yqhome.instrumentapp.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by depengli on 2017/9/28.
 */

public class NoTouchLinearLayout extends LinearLayout {
    public NoTouchLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NoTouchLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoTouchLinearLayout(Context context) {
        super(context);
    }


    private OnResizeListener mListener;

    public interface OnResizeListener {
        void OnResize();
    }

    public void setOnResizeListener(OnResizeListener l) {
        mListener = l;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (mListener != null){
                mListener.OnResize();
            }
        }
        return super.dispatchKeyEventPreIme(event);
    }
}
