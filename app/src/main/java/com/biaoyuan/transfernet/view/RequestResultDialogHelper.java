package com.biaoyuan.transfernet.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.and.yzy.frame.view.dialog.Effectstype;
import com.and.yzy.frame.view.dialog.NiftyDialogNullDataBuilder;
import com.biaoyuan.transfernet.R;

/**
 * Title  :
 * Create : 2017/6/12
 * Author ：chen
 */

public class RequestResultDialogHelper extends NiftyDialogNullDataBuilder {

    //标题
    private TextView tv_title;
    //提示内容
    private TextView tv_msg;
    //确定按钮
    private TextView tv_commit;
    //关闭按钮
    private ImageView iv_close;
    //中间的图片
    private ImageView iv_type;


    /**
     * 确认
     */
    public static final int CONFIRM = 0;
    /**
     * 取件
     */
    public static final int TAKE = 1;
    /**
     * 发布
     */
    public static final int PUBLISH = 2;

    /**
     * 封包
     */
    public static final int CLOSE_PACKAGE = 3;


    /**
     * 验收
     */
    public static final int TEST = 4;

    /**
     * 派送
     */
    public static final int SEND = 5;


    /**
     * 提交
     */
    public static final int COMMIT = 6;


    /**
     * 代发
     */
    public static final int OTHER_SEND = 7;

    /**
     * 签收
     */
    public static final int SIGN = 8;

    public RequestResultDialogHelper(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.all_commit_dialog, null, false);

        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_msg = (TextView) view.findViewById(R.id.tv_msg);
        tv_commit = (TextView) view.findViewById(R.id.tv_commit);
        iv_close = (ImageView) view.findViewById(R.id.img_close);

        iv_type = (ImageView) view.findViewById(R.id.img_type);

        setND_AddCustomView(view);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    public RequestResultDialogHelper(Context context, int theme) {
        super(context, theme);
    }


    /**
     * 设置类型
     * @param type
     * @param isSuccess 是否成功
     * @return
     */
    public RequestResultDialogHelper setType(int type, boolean isSuccess) {

        String title = null;
        String msg = null;
        int img_type = 0;
        String commit_txt = null;


        switch (type) {
            case CONFIRM:
                //确认

                if (isSuccess) {

                    title = "确认成功";
                    msg = "请按要求时间到用户所在地址上门收件";
                    img_type = R.drawable.t_img_confirm;
                    commit_txt = "回到首页";

                } else {

                    title = "确认失败";
                    msg = "确认失败，请重新确认";
                    img_type = R.drawable.t_img_defeat;
                    commit_txt = "重新确认";

                    setND_Effect(Effectstype.Shake);
                }


                break;
            case TAKE:

                if (isSuccess) {

                    title = "取件成功";
                    msg = "您已成功取件，请尽快发布!";
                    img_type = R.drawable.t_img_pickup;
                    commit_txt = "回到首页";

                } else {

                    title = "取件失败";
                    msg = "取件失败，请重新取件";
                    img_type = R.drawable.t_img_defeat;
                    commit_txt = "重新取件";

                    setND_Effect(Effectstype.Shake);
                }


                break;
            case PUBLISH:


                if (isSuccess) {

                    title = "发布成功";
                    msg = "请等待传送员接单\n如长时间无人接单，您可适当加价";
                    img_type = R.drawable.t_img_relaseparcel;
                    commit_txt = "回到首页";

                } else {

                    title = "发布失败";
                    msg = "发布失败，请重新发布";
                    img_type = R.drawable.t_img_defeat;
                    commit_txt = "重新发布";

                    setND_Effect(Effectstype.Shake);
                }

                break;
            case CLOSE_PACKAGE:


                if (isSuccess) {

                    title = "封包完成";
                    msg = "请等待传送天使前来取件";
                    img_type = R.drawable.t_img_packet;
                    commit_txt = "回到首页";

                } else {

                    title = "封包失败";
                    msg = "封包失败，请重新封包";
                    img_type = R.drawable.t_img_defeat;
                    commit_txt = "重新封包";

                    setND_Effect(Effectstype.Shake);
                }


                break;
            case TEST:


                if (isSuccess) {

                    title = "验收成功";
                    msg = "请按照收货地址，尽快派件!";
                    img_type = R.drawable.t_img_check;
                    commit_txt = "回到首页";

                } else {

                    title = "验收失败";
                    msg = "验收失败，请重新验收";
                    img_type = R.drawable.t_img_defeat;
                    commit_txt = "重新验收";

                    setND_Effect(Effectstype.Shake);
                }


                break;
            case SEND:


                if (isSuccess) {

                    title = "派送完成";
                    msg = "快件派送成功，棒棒哒!";
                    img_type = R.drawable.t_img_sign;
                    commit_txt = "回到首页";

                } else {

                    title = "派送失败";
                    msg = "派送失败，请重新派送";
                    img_type = R.drawable.t_img_defeat;
                    commit_txt = "重新派送";

                    setND_Effect(Effectstype.Shake);
                }


                break;
            case COMMIT:

                if (isSuccess) {

                    title = "提交成功";
                    msg = "信息提交成功，请等待处理结果!";
                    img_type = R.drawable.t_img_submit;
                    commit_txt = "回到首页";

                } else {

                    title = "提交失败";
                    msg = "提交失败，请重新提交";
                    img_type = R.drawable.t_img_defeat;
                    commit_txt = "重新提交";

                    setND_Effect(Effectstype.Shake);
                }

                break;
            case OTHER_SEND:

                if (isSuccess) {
                    title = "代发成功";
                    msg = "快件代发成功，请留意快件物流信息!";
                    img_type = R.drawable.t_img_intransit;
                    commit_txt = "回到首页";

                } else {
                    title = "代发失败";
                    msg = "代发失败，请重新派发";
                    img_type = R.drawable.t_img_defeat;
                    commit_txt = "重新派发";
                    setND_Effect(Effectstype.Shake);
                }


                break;
            case SIGN:


                if (isSuccess) {

                    title = "签收成功";
                    msg = "快件签收成功，棒棒哒!";
                    img_type = R.drawable.t_img_sign;
                    commit_txt = "回到首页";

                } else {

                    title = "签收失败";
                    msg = "签收失败，请重新签收";
                    img_type = R.drawable.t_img_defeat;
                    commit_txt = "重新签收";

                    setND_Effect(Effectstype.Shake);
                }


                break;
        }


        setData(title, msg, img_type, commit_txt);
        return this;
    }

    private void setData(String title, String msg, int img_type, String commit_txt) {
        tv_title.setText(title);
        tv_msg.setText(msg);
        tv_commit.setText(commit_txt);
        iv_type.setImageResource(img_type);
    }

    /**
     * 确定按钮
     *
     * @param click
     * @return
     */
    public RequestResultDialogHelper setCommitClick(View.OnClickListener click) {
        tv_commit.setOnClickListener(click);
        return this;
    }


    /**
     * 关闭按钮
     *
     * @param click
     * @return
     */
    public RequestResultDialogHelper setDismissClick(View.OnClickListener click) {
        iv_close.setOnClickListener(click);
        return this;
    }

}
