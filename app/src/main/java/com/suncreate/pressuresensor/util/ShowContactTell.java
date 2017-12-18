package com.suncreate.pressuresensor.util;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;

/**
 * Created by Administrator on 2016/11/18.
 */

public class ShowContactTell {

    public static void showContact(final Context context, final String phoneNum) {

        if (TextUtils.isEmpty(phoneNum)) {
            AppContext.showToast("无法联系!");
            return;
        }
        View inflate;
        Dialog dialog;
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        inflate = LayoutInflater.from(context).inflate(R.layout.dialog_contact_server, null);
        TextView phoneNumText = (TextView) inflate.findViewById(R.id.contact_text);
        phoneNumText.setText(String.format(context.getString(R.string.call_someone), phoneNum));
        Button btn_confirm_contact = (Button) inflate.findViewById(R.id.btn_confirm_contact);
        btn_confirm_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
                context.startActivity(intent);
            }
        });
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }
}
