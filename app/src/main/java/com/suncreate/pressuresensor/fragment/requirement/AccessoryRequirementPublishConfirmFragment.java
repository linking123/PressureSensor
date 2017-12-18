package com.suncreate.pressuresensor.fragment.requirement;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.fragment.base.BaseFragment;

import butterknife.Bind;

/**
 * 选择配件
 * <p>
 * desc
 */
public class AccessoryRequirementPublishConfirmFragment extends BaseFragment implements View.OnClickListener{

    //下一步，选择配件商按钮
    @Bind(R.id.btn_choose_supplier)
    Button btn_choose_supplier;

    @Override
    protected void initWidget(View root) {
        btn_choose_supplier.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_requirement_accessory_publish_choose_accessory;
    }

    @Override
    protected void initData() {


    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_publish:
                Toast.makeText(getContext(),"需求发布成功，请等待商家报价。", Toast.LENGTH_SHORT).show();
                //需求发布成功，提醒后，自动跳转回到配件需求列表，并刷新列表，本次发布状态为新发布；但目前requirementViewpageFragment中怎么定tab不知道；
//                UIHelper.showSimpleBack(getContext(), SimpleBackPage.ACCESSORY_REQUIREMENT);

                break;
            default:
                break;
        }

    }
}
