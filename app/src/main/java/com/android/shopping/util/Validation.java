package com.android.shopping.util;

import android.content.Context;
import android.widget.EditText;

/**
 * Created by rejina on 11/16/2015.
 */
public class Validation {

    public static boolean validateView(EditText editText){
        if(editText.getText().toString().length() == 0) {
            editText.setError("Required");
            return  false;
        }
        return true;
    }
}
