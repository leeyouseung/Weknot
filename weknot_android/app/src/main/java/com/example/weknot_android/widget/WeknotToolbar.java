package com.example.weknot_android.widget;

import android.content.Context;
import android.content.res.Resources;

import android.graphics.Typeface;

import android.util.AttributeSet;

import android.view.Gravity;

import android.widget.TextView;

import androidx.annotation.Nullable;

import androidx.appcompat.widget.Toolbar;

import androidx.core.content.res.ResourcesCompat;

import com.example.weknot_android.R;

import java.lang.reflect.Field;

import java.util.Optional;

public class WeknotToolbar extends Toolbar {

    public WeknotToolbar(Context context) {
        super(context);
    }

    public WeknotToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WeknotToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        try {
            Class superClass = getClass().getSuperclass();
            if (superClass != null) {
                Field field = superClass.getDeclaredField("mTitleTextView");
                field.setAccessible(true);
                TextView textView = (TextView) field.get(this);
                if (textView == null) return;
                try {
                    textView.setTypeface(ResourcesCompat.getFont(getContext(), R.font.nanum_squareeb));
                    textView.setTextColor(getResources().getColor(R.color.colorMainDark));
                    textView.setTextSize(15);
                } catch (Resources.NotFoundException ignore) {

                }

                LayoutParams layoutParams = generateDefaultLayoutParams();
                Field paramField = layoutParams.getClass().getField("mViewType");
                paramField.setInt(layoutParams, 1);
                textView.setLayoutParams(layoutParams);
            }
        } catch (NoSuchFieldException | IllegalAccessException ignore) {

        }
    }

    @Override
    public void setSubtitle(CharSequence subtitle) {
        super.setSubtitle(subtitle);
        try {
            Class superClass = getClass().getSuperclass();
            if (superClass != null) {
                Field field = superClass.getDeclaredField("mSubtitleTextView");
                field.setAccessible(true);
                TextView textView = (TextView) field.get(this);
                if (textView == null) return;
                Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.nanum_squareeb);
                textView.setTypeface(typeface);
                LayoutParams layoutParams = generateDefaultLayoutParams();
                Field paramField = layoutParams.getClass().getField("mViewType");
                paramField.setInt(layoutParams, 1);
                textView.setLayoutParams(layoutParams);
            }
        } catch (NoSuchFieldException | IllegalAccessException ignore) {

        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        try {
            Class superClass = getClass().getSuperclass();
            if (superClass != null) {
                Field fieldTitle = superClass.getDeclaredField("mTitleTextView");
                fieldTitle.setAccessible(true);
                TextView titleView = (TextView) fieldTitle.get(this);

                Field fieldSubTitle = superClass.getDeclaredField("mSubtitleTextView");
                fieldSubTitle.setAccessible(true);
                TextView subTitleView = (TextView) fieldSubTitle.get(this);

                if (titleView == null) return;
                titleView.layout((getWidth() / 2) - (titleView.getWidth() / 2), titleView.getTop(), (getWidth() / 2) + (titleView.getWidth() / 2), titleView.getBottom());
                Optional.ofNullable(subTitleView).map(view -> {
                    view.layout((getWidth() / 2) - (view.getWidth() / 2), view.getTop(), (getWidth() / 2) + (view.getWidth() / 2), view.getBottom());
                    return view;
                });
            }
        } catch (NoSuchFieldException | IllegalAccessException ignore) {

        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        return layoutParams;
    }
}
