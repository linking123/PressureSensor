package com.suncreate.fireiot.adapter.general;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.ViewHolder;
import com.suncreate.fireiot.adapter.base.BaseListAdapter;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.bean.SimpleBackPage;
import com.suncreate.fireiot.bean.require.Requirement;
import com.suncreate.fireiot.util.StringUtils;
import com.suncreate.fireiot.util.UIHelper;
import com.suncreate.fireiot.widget.CircleImageView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

/**
 * 服务需求抢单列表 适配器
 */
public class TechServersListAdapter extends BaseListAdapter<Requirement> {

    public TechServersListAdapter(Callback callback) {
        super(callback);
    }

    @Override
    protected void convert(final ViewHolder vh, final Requirement item, int position) {

        //头像
        String demandUserId = item.getImage();
        final CircleImageView circleImageView = vh.getView(R.id.civ_icon_avatar);
        if (demandUserId != null) {
//            vh.setImageForNet(R.id.civ_icon_avatar, MonkeyApi.getMyInformationHead(Long.parseLong(demandUserId),), R.drawable.widget_dface);
            MonkeyApi.getMyInformationHead(Long.valueOf(demandUserId), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (responseBody != null) {
                        try {
                            Bitmap imap = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
                            circleImageView.setImageBitmap(imap);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        }

        //车主姓名
        String carOwnerName = item.getUserName();
        TextView tv_carowner_name = vh.getView(R.id.tv_carowner_name);
        if (!StringUtils.isEmpty(carOwnerName)) {
            tv_carowner_name.setText(carOwnerName);
        }

        //车型
        String carModel = item.getCarbrandName();
        TextView tv_car_model = vh.getView(R.id.tv_car_model);
        if (carModel != null) {
            tv_car_model.setText(carModel);
        } else {
            tv_car_model.setText("未知车型");
        }

        //发布时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String mPublishTime = item.getDemandAddtime();
        TextView tv_publish_time = vh.getView(R.id.tv_publish_time);
        if (mPublishTime != null) {
            long addTimeL = Long.valueOf(mPublishTime);
            mPublishTime = sdf.format(new Date(addTimeL));
            tv_publish_time.setText(mPublishTime);
        }

        //需求地址
        String mAddress = item.getDemandAddress();
        TextView tv_server_address = vh.getView(R.id.tv_server_address);
        if (mAddress != null) {
            tv_server_address.setText(mAddress);
        }

        //需求描述
        String mServerDesc = item.getDemandDesc();
        TextView tv_server_project = vh.getView(R.id.tv_server_project);
        if (mServerDesc != null) {
            tv_server_project.setText(mServerDesc);
        }

        //抢单按钮
        ImageView iv_icon_qiang = vh.getView(R.id.iv_icon_qiang);
        iv_icon_qiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                String requirementId = item.getId();
                b.putString("requirementId", requirementId);
                UIHelper.showSimpleBack(mCallback.getContext(), SimpleBackPage.SERVICE_REQUIREMENT_DETAIL, b);
            }
        });


    }

    @Override
    protected int getLayoutId(int position, Requirement item) {
        return R.layout.fragment_item_tech_servers;
    }

}
