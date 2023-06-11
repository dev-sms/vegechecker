package com.dongseo.vegechecker;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;

public class LoadingDialog extends Dialog {

    public LoadingDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loading);

        // 취소 불가능
        setCancelable(false);

        // 배경 투명하게 바꿔줌
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}

