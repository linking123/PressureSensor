package com.suncreate.fireiot.adapter.general;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.suncreate.fireiot.R;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.bean.user.BankCardManger;
import com.suncreate.fireiot.util.DialogHelp;
import com.suncreate.fireiot.util.StringUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class BankCardManagerListAdapter extends BaseAdapter {

    private List<BankCardManger> mList;
    private Fragment mFragment;
    private Context context;
    private LayoutInflater inflater = null;

    public BankCardManagerListAdapter(List<BankCardManger> list, Context context, Fragment fragment) {
        this.context = context;
        this.mList = list;
        inflater = LayoutInflater.from(context);
        mFragment = fragment;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    public List<BankCardManger> getmList() {
        return mList;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final BankCardManger bankCardManger = mList.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            // 导入布局并赋值给convertview
            convertView = inflater.inflate(R.layout.fragment_item_bank_card_manager, null);
            viewHolder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
            viewHolder.tv_phone_num = (TextView) convertView.findViewById(R.id.tv_phone_num);
            viewHolder.tv_bank_name = (TextView) convertView.findViewById(R.id.tv_bank_name);
            viewHolder.tv_bank_card_num = (TextView) convertView.findViewById(R.id.tv_bank_card_num);
            viewHolder.btn_del = (TextView) convertView.findViewById(R.id.btn_del);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //用户名
        final String mUserName = bankCardManger.getRealname();
        if (!StringUtils.isEmpty(mUserName)) {
            viewHolder.tv_user_name.setText(mUserName);
        }
        //手机号
        final String mUserPhoneNum = bankCardManger.getPhoneNumber();
        if (!StringUtils.isEmpty(mUserPhoneNum)) {
            viewHolder.tv_phone_num.setText(mUserPhoneNum);
        }
        //银行名称
        final String mBankName = bankCardManger.getBankName();
        if (!StringUtils.isEmpty(mBankName)) {
            viewHolder.tv_bank_name.setText(mBankName);
        }
        //银行账号
        final String mBankAccount = bankCardManger.getBankCardNumber();
        if (!StringUtils.isEmpty(mBankAccount)) {
            viewHolder.tv_bank_card_num.setText(mBankAccount);
        }

        //点击item中的删除按钮
        final BankCardManger BankCardManger = mList.get(position);
        viewHolder.btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionDel(context, BankCardManger, position);
            }
        });

        return convertView;
    }

    final static class ViewHolder {
        TextView tv_user_name;
        TextView tv_phone_num;
        TextView tv_bank_name;
        TextView tv_bank_card_num;
        TextView btn_del;
    }

    private void optionDel(Context context, final BankCardManger BankCardManger, final int position) {
        DialogHelp.getConfirmDialog(context, "确定删除吗?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MonkeyApi.deleteUserBank(BankCardManger.getId(), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        mList.remove(position);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

                    }
                });
            }
        }).show();
    }
}