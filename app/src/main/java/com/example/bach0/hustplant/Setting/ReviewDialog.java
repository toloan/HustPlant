package com.example.bach0.hustplant.Setting;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;

import com.example.bach0.hustplant.R;

public class ReviewDialog extends DialogPreference {

  public ReviewDialog(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public ReviewDialog(Context context, AttributeSet attrs) {
    super(context, attrs);
    setDialogLayoutResource(R.layout.reviewdialog);
    setPositiveButtonText(android.R.string.ok);
    setNegativeButtonText(android.R.string.cancel);

    setDialogIcon(null);
  }
}
