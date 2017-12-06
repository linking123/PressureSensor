package com.suncreate.fireiot.fragment.master;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.suncreate.fireiot.R;
import com.suncreate.fireiot.fragment.base.BaseFragment;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;

/**
 * 店铺设置
 * <p>
 * desc
 */
public class TechSettingsFragment extends BaseFragment implements View.OnClickListener{
    private Dialog dialog;
    private View inflate;
    private Uri imageUri;
    private final int MUTI_CHOICE_DIALOG = 1;
    boolean[] selected = new boolean[]{false,false,false,false,false,false,false,false,false,false};
    @Bind(R.id.icon_master_technician)
    ImageView icon_master_technician;
    @Bind(R.id.btn__store_save)
    Button btn__store_save;
    @Bind(R.id.tv_master_technician_brand)
    TextView tv_master_technician_brand;

    @Bind(R.id.tv_master_technician_time_select_first)
    TextView  tv_master_technician_time_select_first;

    @Bind(R.id.car_brand_name)
    TextView  car_brand_name;

   // tech_telling  tech_note
   @Bind(R.id.tech_telling)
   TextView  tech_telling;

    @Bind(R.id.tech_note)
    TextView  tech_note;

    @Bind(R.id.tv_master_technician_time_select_final)
    TextView  tv_master_technician_time_select_final;
    @Override
    protected void initWidget(View root) {
        icon_master_technician.setOnClickListener(this);
        tv_master_technician_brand.setOnClickListener(this);
        tv_master_technician_time_select_first.setOnClickListener(this);
        tv_master_technician_time_select_final.setOnClickListener(this);
        btn__store_save.setOnClickListener(this);
        car_brand_name.setOnClickListener(this);
        tech_telling.setOnClickListener(this);
        tech_note.setOnClickListener(this);

    }

    @Override
    protected int getLayoutId() {
       // return R.layout.fragment_garage_detail;
        return R.layout.fragment_tech_settings;
    }

    @Override
    protected void initData() {


    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tech_note:
            case R.id.tech_telling:
            case R.id.icon_master_technician:
                File outputImage = new File(Environment.getExternalStorageDirectory(),
                        "output_image.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageUri = Uri.fromFile(outputImage);
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                intent.putExtra("crop", true);
                intent.putExtra("scale", true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, 2);

                break;
            case R.id.tv_master_technician_brand:
                showDialog(MUTI_CHOICE_DIALOG);
              //  UIHelper.showSimpleBack(getContext(), SimpleBackPage.CAR_BRAND_MANY_FRAGMENT);
                break;
            case R.id.tv_master_technician_time_select_first:
                new TimePickerDialog(getActivity(),new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view ,int hourOfDay,int minute){
                        tv_master_technician_time_select_first.setText(String.format("%d:%02d",hourOfDay,minute));
                    }
                },0,0,true).show();
                break;
            case R.id.btn__store_save:
                Toast.makeText(getActivity(),"保存成功",Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_master_technician_time_select_final:
                new TimePickerDialog(getActivity(),new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view ,int hourOfDay,int minute){
                        tv_master_technician_time_select_final.setText(String.format("%d:%02d",hourOfDay,minute));
                    }
                },0,0,true).show();
                break;
            default:
                break;
        }
    }
    public Dialog showDialog(int id ){
        Dialog dialog = null;
        switch (id){
            case (MUTI_CHOICE_DIALOG):
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("请选择车牌：");
                builder.setIcon(R.drawable.arrow);
                DialogInterface.OnMultiChoiceClickListener mutiListener
                        = new DialogInterface.OnMultiChoiceClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface,
                                        int which,boolean isChecked){
                        selected[which] = isChecked;
                    }
                };
                builder.setMultiChoiceItems(R.array.car_brands,selected,
                        mutiListener);
                DialogInterface.OnClickListener btnListener=
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface,
                                                int which){

                                for (int i =0;i<selected.length;i++){
                                    if(selected[i] == true){
                                      car_brand_name.setText(car_brand_name.getText()+getResources()
                                    .getStringArray(R.array.car_brands)[i]);
                                    }
                                }
                            }
                        };
                builder.setPositiveButton("确定",btnListener);
                builder.show();
                dialog = builder.create();
                 break;

        }
        return dialog;
    }
    public void showContactGarage(){
        dialog = new Dialog(getActivity(), R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        inflate = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_contact_server, null);
        //初始化控件
        Button btn_confirm_contact = (Button) inflate.findViewById(R.id.btn_confirm_contact);
        btn_confirm_contact.setOnClickListener(this);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        //设置textview初始值

        dialog.show();//显示对话框

    }
}
