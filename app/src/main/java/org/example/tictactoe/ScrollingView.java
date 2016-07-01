package org.example.tictactoe;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by setohiroyuki on 2016/06/30.
 */
public class ScrollingView extends View {
    private Drawable mBackground;
    private int mScrollpos;

    public ScrollingView(Context context) {
        this(context, null);
    }

    public ScrollingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {

        // load to custumView attribute
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ScrollingView, defStyleAttr, 0);

        // get background
        if (a.hasValue(R.styleable.ScrollingView_scrollingDrawable)) {
            mBackground = a.getDrawable(R.styleable.ScrollingView_scrollingDrawable);
            mBackground.setCallback(this);
        }

        // releace for load Attribute
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // get view size(ignore padding)
        int contentWidth = getWidth();
        int contendHeight = getHeight();

        // draw background
        if (mBackground != null) {
            int max = Math.max(mBackground.getIntrinsicHeight(), mBackground.getIntrinsicWidth());
            mBackground.setBounds(0, 0, contentWidth * 4, contendHeight * 4);

            // swich point display img
            mScrollpos += 2;
            if(mScrollpos >= max) {
                mScrollpos -= max;
            }

            setTranslationX(-mScrollpos);
            setTranslationY(-mScrollpos);

            // draw and next draw
            mBackground.draw(canvas);
            this.invalidate();
        }

    }
}
