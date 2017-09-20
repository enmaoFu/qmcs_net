package com.biaoyuan.transfernet.ui.send.SendSealFgt;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.and.yzy.frame.util.AppManger;
import com.and.yzy.frame.util.DateTool;
import com.and.yzy.frame.util.RetrofitUtils;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseAty;
import com.biaoyuan.transfernet.domain.TakeTime;
import com.biaoyuan.transfernet.domain.TakeTimeSend;
import com.biaoyuan.transfernet.http.Seal;
import com.biaoyuan.transfernet.ui.send.SendSendFgt.SendSendArriveAddressActivity;
import com.biaoyuan.transfernet.ui.send.SendSendFgt.SendSendSuccessActivity;
import com.biaoyuan.transfernet.view.NumberButton;
import com.bigkoo.pickerview.OptionsPickerView;
import com.orhanobut.logger.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @title  :我要加价页面
 * @create :2017/6/1
 * @author :enmaoFu
 */
public class SendSealAddPriceActivity extends BaseAty{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    //加价选择
    @Bind(R.id.add_price)
    NumberButton mPriceWeight;
    //取件时间
    @Bind(R.id.take_data)
    TextView mTakeDate;
    //送达时间
    @Bind(R.id.send_date)
    TextView mSendDate;

    private String[] price =  {"0.00", "5.00", "10.00", "15.00", "20.00", "25.00", "30.00", "35.00", "40.00"};
    private OptionsPickerView mDialogBuilderTakeDate;
    private OptionsPickerView mDialogBuilderSendDate;
    private double basePrice;

    /**
     * 取件时间需要的集合
     */
    private ArrayList<TakeTime> opTime01 = new ArrayList<>();
    private ArrayList<List<String>> opTime02 = new ArrayList<>();
    //取件时间
    private long requiredTime;
    private String requiredTimeStr;

    /**
     * 送达时间需要的集合
     */
    private ArrayList<TakeTime> opTimeSend01 = new ArrayList<>();
    private ArrayList<List<String>> opTimeSend02 = new ArrayList<>();
    //送达时间
    private long requiredTimeSend;
    private String requiredTimeStrSend;

    private String packageId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_send_seal_add_price;
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        packageId = bundle.getString("packageId");
        basePrice = bundle.getDouble("basePrice");
        initToolbar(mToolbar,"我要加价");
        mPriceWeight.setData(price);
        mPriceWeight.setTextSize(18);
        mPriceWeight.setTextColor(R.color.font_orange_red);
    }

    @Override
    public void requestData() {

    }

    @OnClick({R.id.take_data,R.id.send_date,R.id.textView})
    @Override
    public void btnClick(View view) {
        switch (view.getId()){
            case R.id.take_data:
                initTime();
                initTakeDateDialog();
                mDialogBuilderTakeDate.show();
                break;
            case R.id.send_date:
                String takeDateTextView = mTakeDate.getText().toString().trim();
                if(takeDateTextView.equals("请选择时间")){
                    showErrorToast("请先选择取件时间");
                }else{
                    String getTakeDate = mTakeDate.getText().toString().trim();
                    try {
                        CalculationSendDate(getTakeDate,requiredTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    initSendDateDialog();
                }
                break;
            case R.id.textView:
                String takeDateTextStr = mTakeDate.getText().toString().trim();
                String sendDateTextStr = mSendDate.getText().toString().trim();
                String getPrice = price[mPriceWeight.getPosition()];
                double getPriceDouble = Double.valueOf(getPrice);
                double getPriceDoubleNew = basePrice + getPriceDouble;
                if(takeDateTextStr.equals("请选择时间")){
                    showErrorToast("请选择取件时间");
                }else if(sendDateTextStr.equals("请选择时间")){
                    showErrorToast("请选择送达时间");
                }else{

                    AppManger.getInstance().killActivity(SendSealReceivingOrdersActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("packageId",packageId);
                    bundle.putString("value","result");
                    bundle.putString("requiredTime",String.valueOf(requiredTime));
                    bundle.putString("requiredTimeSend",String.valueOf(requiredTimeSend));
                    bundle.putString("getPriceDouble",String.valueOf(getPriceDoubleNew));
                    startActivity(SendSealReceivingOrdersActivity.class,bundle);
                    finish();

                    /*showLoadingDialog(null);
                    doHttp(RetrofitUtils.createApi(Seal.class).parcelFareIncrease(packageId,requiredTime,requiredTimeSend,getPriceDouble),1);*/
                }
                break;
        }
    }

   /* @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:
                AppManger.getInstance().killActivity(SendSealReceivingOrdersActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("packageId",packageId);
                bundle.putString("value","result");
                startActivity(SendSealReceivingOrdersActivity.class,bundle);
                finish();
                break;
        }
    }*/

    /**
     * 计算时间
     */
    private void initTime() {

        opTime01.clear();
        opTime02.clear();

       /* 取件时间选择：

        今天	1个小时内 （这里需要判断如果是当前时间是17点30分，那么就只能选择明天了，如果是17点，那么可以选择1个小时。
        如果现在时间是8点前那么显示就是9点前，10点前...）
        2个小时内
        现在时间加3个小时，如现在时间是10点那么这里显示14点之前
        现在时间加4个小时，直到18点
        明天	9点前
        10点前
        ......
        18 点*/

        //得到现在的时间
        long nowTime = System.currentTimeMillis();


        //得到8:00的时间
        long time8_00 = DateTool.strTimeToTimestamp(DateTool.timesToStrTime(System.currentTimeMillis() + "", "yyyy-MM-dd ") + "8:00:00");


        //得到16:00 的时间

        long time16_00 = DateTool.strTimeToTimestamp(DateTool.timesToStrTime(System.currentTimeMillis() + "", "yyyy-MM-dd ") + "16:00:00");


        //得到17:00 的时间

        long time17_00 = DateTool.strTimeToTimestamp(DateTool.timesToStrTime(System.currentTimeMillis() + "", "yyyy-MM-dd ") + "17:00:00");

        //今晚12点的时间
        long time0_00 = DateTool.strTimeToTimestamp(DateTool.timesToStrTime(System.currentTimeMillis() + 24 * 60 * 60 * 1000 + "", "yyyy-MM-dd ") + "0:00:00");


        //今天8点之前显示
        if (nowTime < time8_00) {

            TakeTime today = new TakeTime();
            today.setType("今日");
            TakeTime moday = new TakeTime();
            moday.setType("明日");


            opTime01.add(today);
            opTime01.add(moday);


            //得到9:00的时间
            long time9_00 = DateTool.strTimeToTimestamp(DateTool.timesToStrTime(System.currentTimeMillis() + "", "yyyy-MM-dd ") + "9:00:00");
            //显示今日9点前 10前 。。。。。18点前
            String lastTimestr = DateTool.timesToStrTime(System.currentTimeMillis() + "", "yyyy-MM-dd ") + "18:00:00";
            long lastTime = DateTool.strTimeToTimestamp(lastTimestr);

            ArrayList<Long> longList = new ArrayList<>();

            while (time9_00 < lastTime) {
                longList.add(time9_00);
                //加1小时
                time9_00 += 60 * 60 * 1000L;
            }

            longList.add(lastTime);

            today.setLongTime(longList);

            ArrayList<String> toTimes = new ArrayList<>();
            for (int i = 0; i < longList.size(); i++) {
                toTimes.add(DateTool.timesToStrTime(longList.get(i) + "", "HH:mm") + "之前");
            }
            today.setTimes(toTimes);
            opTime02.add(toTimes);

            addModayTime(moday,opTime02,24,24);


            // 今天8点到16点前
        } else if (nowTime >= time8_00 && nowTime <= time16_00) {

            TakeTime today = new TakeTime();
            today.setType("今日");
            TakeTime moday = new TakeTime();
            moday.setType("明日");


            opTime01.add(today);
            opTime01.add(moday);


            ArrayList<String> toTime = new ArrayList<>();
            ArrayList<Long> toLongTime = new ArrayList<>();

            toTime.add("1小时之内");
            toTime.add("2小时之内");


            toLongTime.add(System.currentTimeMillis() + 60 * 60 * 1 * 1000);
            toLongTime.add(System.currentTimeMillis() + 60 * 60 * 2 * 1000);

            //得到今天晚上18:00的时间

            String lastTimestr = DateTool.timesToStrTime(System.currentTimeMillis() + "", "yyyy-MM-dd ") + "18:00:00";
            long lastTime = DateTool.strTimeToTimestamp(lastTimestr);
            //得到现在的分钟
            int newMin = Integer.parseInt(DateTool.getNowMin());

            //如果小于30 -30   大于30 让他==00 小时加1小时
            long time = System.currentTimeMillis() + 60 * 60 * 2 * 1000;

            if (newMin < 30) {

            } else if (newMin >= 30) {
                //加1小时
                time += 60 * 60 * 1 * 1000;
            }


            String newTiemStr = DateTool.timesToStrTime(time + "", "yyyy-MM-dd HH:") + "00:00";

            //  Logger.v("newTime=="+newTiemStr);

            long newTime = DateTool.strTimeToTimestamp(newTiemStr);


            while (newTime < lastTime) {
                toLongTime.add(newTime);
                //加1小时
                newTime += 60 * 60 * 1000L;
            }
            toLongTime.add(lastTime);
            today.setLongTime(toLongTime);

            for (int i = 2; i < toLongTime.size(); i++) {
                toTime.add(DateTool.timesToStrTime(toLongTime.get(i) + "", "HH:mm") + "之前");
            }

            today.setTimes(toTime);
            opTime02.add(toTime);

            addModayTime(moday,opTime02,24,24);


            //今天16点到17点
        } else if (nowTime > time16_00 && nowTime <= time17_00) {
            //显示一个小时之内


            TakeTime today = new TakeTime();
            today.setType("今日");
            TakeTime moday = new TakeTime();
            moday.setType("明日");


            opTime01.add(today);
            opTime01.add(moday);


            ArrayList<String> toTime = new ArrayList<>();
            ArrayList<Long> toLongTime = new ArrayList<>();
            toTime.add("1小时之内");
            toLongTime.add(System.currentTimeMillis() + 60 * 60 * 1 * 1000);

            today.setTimes(toTime);
            today.setLongTime(toLongTime);
            opTime02.add(toTime);

            addModayTime(moday,opTime02,24,24);


            //今天17到0：00点
        } else if (nowTime > time17_00 && nowTime <= time0_00) {
            //0点之前显示明天9点前10点前
            TakeTime moday = new TakeTime();
            moday.setType("明日");
            opTime01.add(moday);
            addModayTime(moday,opTime02,24,24);

        }


    }

    /**
     * 加入明天的时间，以及某日的时间
     *
     * @param takeTime
     */
    private void addModayTime(TakeTime takeTime,ArrayList<List<String>> time,int date09,int date18) {

        //某一天的18 9点
        long time18_00 = DateTool.strTimeToTimestamp(DateTool.timesToStrTime(System.currentTimeMillis() + date18 * 60 * 60 * 1000 + "", "yyyy-MM-dd ") + "18:00:00");
        long time9_00 = DateTool.strTimeToTimestamp(DateTool.timesToStrTime(System.currentTimeMillis() + date09 * 60 * 60 * 1000 + "", "yyyy-MM-dd ") + "9:00:00");

        ArrayList<Long> longList = new ArrayList<>();

        while (time9_00 < time18_00) {
            longList.add(time9_00);
            //加1小时
            time9_00 += 60 * 60 * 1000L;
        }

        longList.add(time18_00);

        takeTime.setLongTime(longList);
        ArrayList<String> moTimes = new ArrayList<>();
        for (int i = 0; i < longList.size(); i++) {
            moTimes.add(DateTool.timesToStrTime(longList.get(i) + "", "HH:mm") + "之前");
        }
        takeTime.setTimes(moTimes);
        time.add(moTimes);
    }

    /**
     * 选择取件时间
     */
    public void initTakeDateDialog() {

        if (mDialogBuilderTakeDate == null) {
            mDialogBuilderTakeDate = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {

                    requiredTime = opTime01.get(options1).getLongTime().get(options2);
                    requiredTimeStr = opTime01.get(options1).getType() + opTime01.get(options1).getTimes().get(options2);
                    Logger.v("取件时间-----"+DateTool.timesToStrTime(requiredTime + "", "yyyy.MM.dd-HH:mm:ss"));

                    mTakeDate.setText(requiredTimeStr);
                }
            })
                    .setTitleText("选择上门取件时间")
                    .setContentTextSize(14)
                    .setOutSideCancelable(true)// default is true
                    .build();

        }

        mDialogBuilderTakeDate.setPicker(opTime01, opTime02);
        mDialogBuilderTakeDate.show();

    }

    /**
     * 计算送达时间
     * @param getTakeDate 取件时间String
     * @param getRequiredTime 取件时间Long
     */
    public void CalculationSendDate(String getTakeDate,long getRequiredTime) throws ParseException {

        opTimeSend01.clear();
        opTimeSend02.clear();

        //截取取件时间String，判断是今日还是明日
        String getTakeDateSb = getTakeDate.substring(0,2).trim();
        Log.v("print","截取的取件时间判断今日还是明日----"+getTakeDateSb);

        //转换取件时间为yyyy-MM-dd HH:mm:ss格式
        String getRequiredTimeStr = DateTool.timesToStrTime(getRequiredTime + "", "yyyy-MM-dd HH:mm:ss");
        Log.v("print","没有转换的取件时间Long----"+getRequiredTime);
        Log.v("print","已经转换的取件时间----"+getRequiredTimeStr);

        //得到今日16:00 的时间
        long time16_00 = DateTool.strTimeToTimestamp(DateTool.timesToStrTime(System.currentTimeMillis() + "", "yyyy-MM-dd ") + "16:00:00");

        //得到明日16:00时间
        long MTime16Long = DateTool.strTimeToTimestamp(DateTool.timesToStrTime(System.currentTimeMillis() + 24 * 60 * 60 * 1000 + "", "yyyy-MM-dd ") + "16:00:00");

        //获取今日18:00时间
        String JTime18 = DateTool.timesToStrTime(System.currentTimeMillis() + "", "yyyy-MM-dd ") + "18:00:00";
        long JTime18Long = DateTool.strTimeToTimestamp(JTime18);

        //获取明日12:00时间
        long MTime12Long = DateTool.strTimeToTimestamp(DateTool.timesToStrTime(System.currentTimeMillis() + 24 * 60 * 60 * 1000 + "", "yyyy-MM-dd ") + "12:00:00");
        String MTime12 = DateTool.timesToStrTime(MTime12Long + "", "yyyy-MM-dd HH:mm:ss");

        //获取明日18:00时间
        long MTime18Long = DateTool.strTimeToTimestamp(DateTool.timesToStrTime(System.currentTimeMillis() + 24 * 60 * 60 * 1000 + "", "yyyy-MM-dd ") + "18:00:00");
        String MTime18 = DateTool.timesToStrTime(MTime18Long + "", "yyyy-MM-dd HH:mm:ss");

        //格式化时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if(getTakeDateSb.equals("今日")){

            Log.v("print","今日----");

            //取件时间小于今日的16点
            if(getRequiredTime < time16_00){

                //某日 2017/06/19  = 2017/06/19+2
                long moDayTwoLong = DateTool.strTimeToTimestamp(DateTool.timesToStrTime(System.currentTimeMillis() + 42 * 60 * 60 * 1000 + "", "yyyy-MM-dd ") + "09:00:00");
                String HTimeTwo = DateTool.timesToStrTime(moDayTwoLong + "", "yyyy-MM-dd HH:mm:ss");
                //某日 2017/06/19  = 2017/06/19+3
                long MHTimeLongAddThree = DateTool.strTimeToTimestamp(DateTool.timesToStrTime(System.currentTimeMillis() + 72 * 60 * 60 * 1000 + "", "yyyy-MM-dd ") + "09:00:00");
                String HTimeThree = DateTool.timesToStrTime(MHTimeLongAddThree + "", "yyyy-MM-dd HH:mm:ss");
                //某日 2017/06/19  = 2017/06/19+4
                long MHTimeLongAddFour = DateTool.strTimeToTimestamp(DateTool.timesToStrTime(System.currentTimeMillis() + 96 * 60 * 60 * 1000 + "", "yyyy-MM-dd ") + "09:00:00");
                String HTimeFour = DateTool.timesToStrTime(MHTimeLongAddFour + "", "yyyy-MM-dd HH:mm:ss");
                //某日 2017/06/19  = 2017/06/19+5
                long MHTimeLongAddFive = DateTool.strTimeToTimestamp(DateTool.timesToStrTime(System.currentTimeMillis() + 120 * 60 * 60 * 1000 + "", "yyyy-MM-dd ") + "09:00:00");
                String HTimeFive = DateTool.timesToStrTime(MHTimeLongAddFive + "", "yyyy-MM-dd HH:mm:ss");

                TakeTime day = new TakeTime();
                day.setType("今日");
                opTimeSend01.add(day);

                ArrayList<String> timeStr = new ArrayList<>();
                ArrayList<Long> timeLong = new ArrayList<>();

                timeStr.add("1小时之内");
                timeStr.add("2小时之内");

                timeLong.add(getRequiredTime + 60 * 60 * 1 * 1000);
                timeLong.add(getRequiredTime + 60 * 60 * 2 * 1000);

                //得到取件时间的后3个小时
                long three_3 = getRequiredTime + 60 * 60 * 3 * 1000;

                while (three_3 < JTime18Long) {
                    timeLong.add(three_3);
                    //加1小时
                    three_3 += 60 * 60 * 1000L;
                }

                timeLong.add(JTime18Long);
                day.setLongTime(timeLong);

                for (int i = 2; i < timeLong.size(); i++) {
                    timeStr.add(DateTool.timesToStrTime(timeLong.get(i) + "", "HH:00") + "之前");
                }

                day.setTimes(timeStr);
                opTimeSend02.add(timeStr);

                TakeTime today = new TakeTime();
                today.setType("明日");
                opTimeSend01.add(today);

                TakeTime moDayTwo= new TakeTime();
                moDayTwo.setType(HTimeTwo.substring(0,10));
                opTimeSend01.add(moDayTwo);

                TakeTime moDayThree = new TakeTime();
                moDayThree.setType(HTimeThree.substring(0,10));
                opTimeSend01.add(moDayThree);

                TakeTime moDayFour = new TakeTime();
                moDayFour.setType(HTimeFour.substring(0,10));
                opTimeSend01.add(moDayFour);

                TakeTime moDayFive = new TakeTime();
                moDayFive.setType(HTimeFive.substring(0,10));
                opTimeSend01.add(moDayFive);

                addModayTime(today,opTimeSend02,24,24);
                addModayTime(moDayTwo,opTimeSend02,42,42);
                addModayTime(moDayThree,opTimeSend02,72,72);
                addModayTime(moDayFour,opTimeSend02,96,96);
                addModayTime(moDayFive,opTimeSend02,120,120);

            }else{

                //某日 2017/06/19  = 2017/06/19+2
                long moDayTwoLong = DateTool.strTimeToTimestamp(DateTool.timesToStrTime(System.currentTimeMillis() + 42 * 60 * 60 * 1000 + "", "yyyy-MM-dd ") + "09:00:00");
                String HTimeTwo = DateTool.timesToStrTime(moDayTwoLong + "", "yyyy-MM-dd HH:mm:ss");
                //某日 2017/06/19  = 2017/06/19+3
                long MHTimeLongAddThree = DateTool.strTimeToTimestamp(DateTool.timesToStrTime(System.currentTimeMillis() + 72 * 60 * 60 * 1000 + "", "yyyy-MM-dd ") + "09:00:00");
                String HTimeThree = DateTool.timesToStrTime(MHTimeLongAddThree + "", "yyyy-MM-dd HH:mm:ss");
                //某日 2017/06/19  = 2017/06/19+4
                long MHTimeLongAddFour = DateTool.strTimeToTimestamp(DateTool.timesToStrTime(System.currentTimeMillis() + 96 * 60 * 60 * 1000 + "", "yyyy-MM-dd ") + "09:00:00");
                String HTimeFour = DateTool.timesToStrTime(MHTimeLongAddFour + "", "yyyy-MM-dd HH:mm:ss");
                //某日 2017/06/19  = 2017/06/19+5
                long MHTimeLongAddFive = DateTool.strTimeToTimestamp(DateTool.timesToStrTime(System.currentTimeMillis() + 120 * 60 * 60 * 1000 + "", "yyyy-MM-dd ") + "09:00:00");
                String HTimeFive = DateTool.timesToStrTime(MHTimeLongAddFive + "", "yyyy-MM-dd HH:mm:ss");

                TakeTime moday = new TakeTime();
                moday.setType("明日");
                opTimeSend01.add(moday);

                TakeTime moDayTwo= new TakeTime();
                moDayTwo.setType(HTimeTwo.substring(0,10));
                opTimeSend01.add(moDayTwo);

                TakeTime moDayThree = new TakeTime();
                moDayThree.setType(HTimeThree.substring(0,10));
                opTimeSend01.add(moDayThree);

                TakeTime moDayFour = new TakeTime();
                moDayFour.setType(HTimeFour.substring(0,10));
                opTimeSend01.add(moDayFour);

                TakeTime moDayFive = new TakeTime();
                moDayFive.setType(HTimeFive.substring(0,10));
                opTimeSend01.add(moDayFive);

                addModayTime(moday,opTimeSend02,24,24);
                addModayTime(moDayTwo,opTimeSend02,42,42);
                addModayTime(moDayThree,opTimeSend02,72,72);
                addModayTime(moDayFour,opTimeSend02,96,96);
                addModayTime(moDayFive,opTimeSend02,120,120);

            }

        }else{
            Log.v("print","明日----");

            //取件时间小于明日的16点
            if(getRequiredTime < MTime16Long){

                //某日 2017/06/19  = 2017/06/19+2
                long moDayTwoLong = DateTool.strTimeToTimestamp(DateTool.timesToStrTime(System.currentTimeMillis() + 42 * 60 * 60 * 1000 + "", "yyyy-MM-dd ") + "09:00:00");
                String HTimeTwo = DateTool.timesToStrTime(moDayTwoLong + "", "yyyy-MM-dd HH:mm:ss");
                //某日 2017/06/19  = 2017/06/19+3
                long MHTimeLongAddThree = DateTool.strTimeToTimestamp(DateTool.timesToStrTime(System.currentTimeMillis() + 72 * 60 * 60 * 1000 + "", "yyyy-MM-dd ") + "09:00:00");
                String HTimeThree = DateTool.timesToStrTime(MHTimeLongAddThree + "", "yyyy-MM-dd HH:mm:ss");
                //某日 2017/06/19  = 2017/06/19+4
                long MHTimeLongAddFour = DateTool.strTimeToTimestamp(DateTool.timesToStrTime(System.currentTimeMillis() + 96 * 60 * 60 * 1000 + "", "yyyy-MM-dd ") + "09:00:00");
                String HTimeFour = DateTool.timesToStrTime(MHTimeLongAddFour + "", "yyyy-MM-dd HH:mm:ss");
                //某日 2017/06/19  = 2017/06/19+5
                long MHTimeLongAddFive = DateTool.strTimeToTimestamp(DateTool.timesToStrTime(System.currentTimeMillis() + 120 * 60 * 60 * 1000 + "", "yyyy-MM-dd ") + "09:00:00");
                String HTimeFive = DateTool.timesToStrTime(MHTimeLongAddFive + "", "yyyy-MM-dd HH:mm:ss");
                //某日 2017/06/19  = 2017/06/19+6
                long MHTimeLongAddSix = DateTool.strTimeToTimestamp(DateTool.timesToStrTime(System.currentTimeMillis() + 144 * 60 * 60 * 1000 + "", "yyyy-MM-dd ") + "09:00:00");
                String HTimeSix = DateTool.timesToStrTime(MHTimeLongAddSix + "", "yyyy-MM-dd HH:mm:ss");

                TakeTime day = new TakeTime();
                day.setType("明日");
                opTimeSend01.add(day);

                ArrayList<String> timeStr = new ArrayList<>();
                ArrayList<Long> timeLong = new ArrayList<>();

                timeStr.add("1小时之内");
                timeStr.add("2小时之内");

                timeLong.add(getRequiredTime + 60 * 60 * 1 * 1000);
                timeLong.add(getRequiredTime + 60 * 60 * 2 * 1000);

                //得到取件时间的后3个小时
                long three_3 = getRequiredTime + 60 * 60 * 3 * 1000;

                while (three_3 < MTime18Long) {
                    timeLong.add(three_3);
                    //加1小时
                    three_3 += 60 * 60 * 1000L;
                }

                timeLong.add(MTime18Long);
                day.setLongTime(timeLong);

                for (int i = 2; i < timeLong.size(); i++) {
                    timeStr.add(DateTool.timesToStrTime(timeLong.get(i) + "", "HH:00") + "之前");
                }

                day.setTimes(timeStr);
                opTimeSend02.add(timeStr);

                TakeTime moDayTwo= new TakeTime();
                moDayTwo.setType(HTimeTwo.substring(0,10));
                opTimeSend01.add(moDayTwo);

                TakeTime moDayThree = new TakeTime();
                moDayThree.setType(HTimeThree.substring(0,10));
                opTimeSend01.add(moDayThree);

                TakeTime moDayFour = new TakeTime();
                moDayFour.setType(HTimeFour.substring(0,10));
                opTimeSend01.add(moDayFour);

                TakeTime moDayFive = new TakeTime();
                moDayFive.setType(HTimeFive.substring(0,10));
                opTimeSend01.add(moDayFive);

                TakeTime moDaySix = new TakeTime();
                moDaySix.setType(HTimeSix.substring(0,10));
                opTimeSend01.add(moDaySix);

                addModayTime(moDayTwo,opTimeSend02,42,42);
                addModayTime(moDayThree,opTimeSend02,72,72);
                addModayTime(moDayFour,opTimeSend02,96,96);
                addModayTime(moDayFive,opTimeSend02,120,120);
                addModayTime(moDaySix,opTimeSend02,144,144);

            }else{

                //某日 2017/06/19  = 2017/06/19+2
                long MHTime09Long = DateTool.strTimeToTimestamp(DateTool.timesToStrTime(System.currentTimeMillis() + 42 * 60 * 60 * 1000 + "", "yyyy-MM-dd ") + "09:00:00");
                String HTime18 = DateTool.timesToStrTime(MHTime09Long + "", "yyyy-MM-dd HH:mm:ss");
                //某日 2017/06/19  = 2017/06/19+3
                long MHTimeLongAddThree = DateTool.strTimeToTimestamp(DateTool.timesToStrTime(System.currentTimeMillis() + 72 * 60 * 60 * 1000 + "", "yyyy-MM-dd ") + "09:00:00");
                String HTimeThree = DateTool.timesToStrTime(MHTimeLongAddThree + "", "yyyy-MM-dd HH:mm:ss");
                //某日 2017/06/19  = 2017/06/19+4
                long MHTimeLongAddFour = DateTool.strTimeToTimestamp(DateTool.timesToStrTime(System.currentTimeMillis() + 96 * 60 * 60 * 1000 + "", "yyyy-MM-dd ") + "09:00:00");
                String HTimeFour = DateTool.timesToStrTime(MHTimeLongAddFour + "", "yyyy-MM-dd HH:mm:ss");
                //某日 2017/06/19  = 2017/06/19+5
                long MHTimeLongAddFive = DateTool.strTimeToTimestamp(DateTool.timesToStrTime(System.currentTimeMillis() + 120 * 60 * 60 * 1000 + "", "yyyy-MM-dd ") + "09:00:00");
                String HTimeFive = DateTool.timesToStrTime(MHTimeLongAddFive + "", "yyyy-MM-dd HH:mm:ss");
                //某日 2017/06/19  = 2017/06/19+6
                long MHTimeLongAddSix = DateTool.strTimeToTimestamp(DateTool.timesToStrTime(System.currentTimeMillis() + 144 * 60 * 60 * 1000 + "", "yyyy-MM-dd ") + "09:00:00");
                String HTimeSix = DateTool.timesToStrTime(MHTimeLongAddSix + "", "yyyy-MM-dd HH:mm:ss");


                TakeTime moDayTwo = new TakeTime();
                moDayTwo.setType(HTime18.substring(0,10));
                opTimeSend01.add(moDayTwo);

                TakeTime moDayThree = new TakeTime();
                moDayThree.setType(HTimeThree.substring(0,10));
                opTimeSend01.add(moDayThree);

                TakeTime moDayFour = new TakeTime();
                moDayFour.setType(HTimeFour.substring(0,10));
                opTimeSend01.add(moDayFour);

                TakeTime moDayFive = new TakeTime();
                moDayFive.setType(HTimeFive.substring(0,10));
                opTimeSend01.add(moDayFive);

                TakeTime moDaySix = new TakeTime();
                moDaySix.setType(HTimeSix.substring(0,10));
                opTimeSend01.add(moDaySix);

                addModayTime(moDayTwo,opTimeSend02,42,42);
                addModayTime(moDayThree,opTimeSend02,72,72);
                addModayTime(moDayFour,opTimeSend02,96,96);
                addModayTime(moDayFive,opTimeSend02,120,120);
                addModayTime(moDaySix,opTimeSend02,144,144);

            }

        }

    }

    /**
     * 选择送到时间
     */
    public void initSendDateDialog(){

        if (mDialogBuilderSendDate == null) {
            mDialogBuilderSendDate = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {

                    requiredTimeSend = opTimeSend01.get(options1).getLongTime().get(options2);
                    requiredTimeStrSend = opTimeSend01.get(options1).getType() + opTimeSend01.get(options1).getTimes().get(options2);
                    Logger.v("送达时间-----"+"未转码----"+requiredTimeSend+"-----"+DateTool.timesToStrTime(requiredTimeSend + "", "yyyy.MM.dd-HH:mm:ss"));

                    String is = requiredTimeStrSend.substring(0,2);
                    if(is.equals("今日")){
                        mSendDate.setText(requiredTimeStrSend);
                    }else if(is.equals("明日")){
                        mSendDate.setText(requiredTimeStrSend);
                    }else{
                        mSendDate.setText(requiredTimeStrSend.substring(5,requiredTimeStrSend.length() - 7) + " " + requiredTimeStrSend.substring(10,requiredTimeStrSend.length() - 1));
                    }
                }
            })
                    .setTitleText("选择送达时间")
                    .setContentTextSize(14)
                    .setOutSideCancelable(true)// default is true
                    .build();

        }

        mDialogBuilderSendDate.setPicker(opTimeSend01, opTimeSend02);
        mDialogBuilderSendDate.show();

    }



}
