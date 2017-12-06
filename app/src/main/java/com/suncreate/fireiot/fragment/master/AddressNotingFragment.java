package com.suncreate.fireiot.fragment.master;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;

import com.suncreate.fireiot.R;
import com.suncreate.fireiot.fragment.base.BaseFragment;
import com.suncreate.fireiot.util.AreaHelper;

import butterknife.Bind;

/**
 * 技师详情
 * <p>
 * desc
 */
public class AddressNotingFragment extends BaseFragment implements View.OnClickListener {
    @Bind(R.id.layout1)
    RelativeLayout layout1;

    @Bind(R.id.user_area)
    TextView user_area;

    @Bind(R.id.btn_del)
    Button btn_del;

    @Bind(R.id.btn_save)
    Button btn_save;

    OptionsPickerView addrOptions;
//
//    @Bind(R.id.tv_arrow_right)
//    Button tv_arrow_right;

    //    private View inflate;
//    private Dialog dialog;
//    private EditText normal;
//    private Button selector_del;
//    private Button selector_add;
//    private Button btn_goodscar_sure;
//    private Button btn_buy_sure;
//    private int amount = 1; //购买数量
//    private int goods_storage = 100; //库存
    @Override
    protected void initWidget(View root) {
        //选择地址
        addrOptions = AreaHelper.chooseAddress(addrOptions, this.getContext());

        layout1.setOnClickListener(this);
        btn_del.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        addrOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                AreaHelper.onOptionsSelect(options1, option2, options3, user_area);
            }
        });
//       tv_arrow_right.setOnClickListener(this);

//        addressTextView.setText(address);
        //  点击弹出选项选择器
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_address_noting;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.layout1:
                addrOptions.show();
                break;
            case R.id.btn_del:
                this.getActivity().finish();
                break;
            case R.id.btn_save:
                this.getActivity().finish();
                break;
            default:
                break;
        }
    }


//    public void show(int i){
//        dialog = new Dialog(getActivity(),R.style.ActionSheetDialogStyle);
//        //填充对话框的布局
//        inflate = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_layout, null);
//        //初始化控件
//        normal = (EditText) inflate.findViewById(R.id.normal);
//        selector_del = (Button) inflate.findViewById(R.id.selector_del);
//        selector_add = (Button) inflate.findViewById(R.id.selector_add);
//        btn_goodscar_sure = (Button) inflate.findViewById(R.id.btn_goodscar_sure);
//        btn_buy_sure = (Button) inflate.findViewById(R.id.btn_buy_sure);
//        selector_del.setOnClickListener(this);
//        selector_add.setOnClickListener(this);
//        btn_goodscar_sure.setOnClickListener(this);
//        btn_buy_sure.setOnClickListener(this);
//        //将布局设置给Dialog
//        dialog.setContentView(inflate);
//        //获取当前Activity所在的窗体
//        Window dialogWindow = dialog.getWindow();
//        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
//        //设置Dialog从窗体底部弹出
//        dialogWindow.setGravity( Gravity.BOTTOM);
//        //获得窗体的属性
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
////       将属性设置给窗体
//        dialogWindow.setAttributes(lp);
//        //设置edittext初始值
//        normal.setText(amount+"");
//        if(i==1){
//            btn_buy_sure.setVisibility(View.GONE);
//        }
//        if(i==2){
//            btn_goodscar_sure.setVisibility(View.GONE);
//        }
//        dialog.show();//显示对话框
//    }
}
