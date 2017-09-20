package com.biaoyuan.transfernet.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.DeleteObjectRequest;
import com.alibaba.sdk.android.oss.model.DeleteObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.and.yzy.frame.base.BaseFrameAty;
import com.and.yzy.frame.util.AppManger;
import com.and.yzy.frame.util.FileUtils;
import com.and.yzy.frame.util.RetrofitUtils;
import com.biaoyuan.transfernet.app.MyApplication;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.domain.AliyunSTS;
import com.biaoyuan.transfernet.domain.PicInfo;
import com.biaoyuan.transfernet.http.Image;
import com.biaoyuan.transfernet.ui.login.LoginAty;
import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Title  :
 * Create : 2017/6/21
 * Author ：chen
 */

public class UpImageUtils {


    private Handler mHandler;

    /**
     * 正在上传的那个图片position
     */
    private int number = 0;


    /**
     * 需要请求的token的文件类型
     */
    private String tag;
    public static final String TAG_PACKAGE = "package";
    public static final String TAG_ORDER = "order";
    public static final String TAG_QMCS_NW_HEADER= "qmcs-nw/head";


    /**
     * 上传的状态
     */
    public static int STATE = 0;


    /**
     * 上传停止
     */
    public static int STATE_STOP = 0;

    /**
     * 上传开始
     */
    public static int STATE_START = 1;

    /**
     * 上传结束
     */
    public static int STATE_END = 2;


    /**
     * 获取token的实体
     */
    private static AliyunSTS mAliyunSTS;


    private BaseFrameAty mBaseFrameAty;


    /**
     * 文件的总大小
     */
    private double totSize;
    /**
     * 上传进度值
     */
    private double curSize;
    private double tagSize;

    /**
     * 用户提交数据是否成功，必须在aty的onsuccess中指定为true;
     */
    public static boolean isCommitSuccess = false;


    /**
     * 文件集合
     */
    private static List<PicInfo> filesPath;

    /**
     * 文件路径
     */
    private StringBuffer imagePath = null;

    /**
     * 保存的文件夹一般是一个唯一的id(如订单id)
     */
    private String fileName;
    private OSS mOss;
    private OSSAsyncTask mTask;


    /**
     * 得到保存到服务器的图片路径（package/159/[14695258.jpg,149684166.jpg,1489688466.jpg,218614364.jpg]）
     *
     * @return
     */
    public String getImagePath() {

        if (imagePath == null) {
            return null;
        }
        return imagePath.toString();
    }


    /**
     * @param aty       BaseFrameAty
     * @param tag       需要请求的token的文件类型
     * @param fileName  保存的文件夹一般是一个唯一的id(如订单id)
     * @param mListener 上传进度监听
     */
    public UpImageUtils(BaseFrameAty aty, String tag, String fileName, final UpImageListener mListener) {

        this.mBaseFrameAty = aty;
        this.tag = tag;
        this.fileName = fileName;
        STATE = 0;
        this.mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        //失败

                        STATE = STATE_STOP;
                        mListener.onUpFailure();

                        break;
                    case 1:
                        //进度回调
                        int progress = (new Double((curSize / totSize) * 100)).intValue();
                        Logger.v("progress==" + progress);
                        mListener.onUpLoading(progress);

                        break;
                    case 2:
                        //成功
                        STATE = STATE_END;
                        mListener.onUpSuccess();
                        break;
                    case 3:
                        //上传开始
                        STATE = STATE_START;
                        startUpImage();

                        break;
                }
            }
        };

    }

    /**
     * 开始上传图片
     */
    private void startUpImage() {


        if (number == 0) {
            tagSize = 0;
        } else {
            tagSize = 0;
            for (int i = 0; i < number; i++) {
                tagSize += FileUtils.getFileOrFilesSize(filesPath.get(i).getPath(), 1);
            }
        }

        String path = filesPath.get(number).getPath();


        File file = new File(path);

        //拼接上传地址
        imagePath.append(file.getName());
        if (number != filesPath.size() - 1) {
            imagePath.append(",");
        } else {
            imagePath.append("]");
        }


        String objectKey = mAliyunSTS.getSaveDirectory() + "/" + fileName + "/" + file.getName();


        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(mAliyunSTS.getBucketName(), objectKey, path);

        put.setCallbackParam(new HashMap<String, String>() {
            {
                put("callbackUrl", mAliyunSTS.getCallbackUrl());
                put("callbackBodyType", "application/json");
                put("callbackBody", mAliyunSTS.getCallbackBody());
            }
        });


        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);

                Long aLong = currentSize;
                curSize = tagSize + aLong.doubleValue();


                mHandler.sendEmptyMessage(1);


            }
        });


        // 只有设置了servercallback，这个值才有数据
        //上传完成
        // 请求异常
        // 本地异常如网络异常等
        // 服务异常
        //这里代表上传阿里云成功，但是自己服务器回调失败了
        //上传完成
        mTask = mOss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d("PutObject", "UploadSuccess");


                // 只有设置了servercallback，这个值才有数据
                String serverCallbackReturnJson = result.getServerCallbackReturnBody();
                Log.d("servercallback", "serverCallbackReturnJson====" + serverCallbackReturnJson);


                if (number == filesPath.size() - 1) {
                    //上传完成
                    mHandler.sendEmptyMessage(2);
                } else {
                    number++;
                    mHandler.sendEmptyMessage(3);
                }

            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                    deleteFile();
                    mHandler.sendEmptyMessage(0);

                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("RawMessage", serviceException.getRawMessage());

                    //这里代表上传阿里云成功，但是自己服务器回调失败了
                    if (number == filesPath.size() - 1) {
                        //上传完成
                        mHandler.sendEmptyMessage(2);
                    } else {
                        number++;
                        mHandler.sendEmptyMessage(3);
                    }
                }
            }
        });


    }


    /**
     * 上传图片
     *
     * @param filesPath
     */
    public void upPhoto(List<PicInfo> filesPath) {


        if (STATE == STATE_START ) {
            mBaseFrameAty.showErrorToast("请等待");
            return;
        }
        STATE = STATE_START;


        if (filesPath == null || filesPath.size() == 0) {
            mBaseFrameAty.showErrorToast("请选择图片");
            return;
        }

        isCommitSuccess = false;

        this.filesPath = filesPath;

        totSize = 0;
        for (PicInfo s : filesPath) {
            totSize += FileUtils.getFileOrFilesSize(s.getPath(), 1);
            Logger.v("size==" + FileUtils.getFileOrFilesSize(s.getPath(), 1));
        }
        Logger.v("totalSize==" + totSize);

        this.imagePath = null;
        imagePath = new StringBuffer();

        //获取token
        doHttp(RetrofitUtils.createApi(Image.class).orderAssumeRole(tag));

    }


    private void doHttp(Call<ResponseBody> bodyCall) {

        bodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    String result = response.body().string();
                    Logger.json(result);
                    JSONObject object = JSONObject.parseObject(result);
                    if (object.containsKey("code")) {
                        int code = object.getInteger("code");
                        if (code == 200) {


                            mAliyunSTS = null;
                            mAliyunSTS = AppJsonUtil.getObject(result, "aliyunSTS", AliyunSTS.class);

                            OSSStsTokenCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(mAliyunSTS.getAccessKeyId(), mAliyunSTS.getAccessKeySecret(), mAliyunSTS.getSecurityToken());
                            String endpoint = mAliyunSTS.getEndpoint();
                            mOss = new OSSClient(MyApplication.getApplicationCotext(), endpoint, credentialProvider);

                            imagePath.append(mAliyunSTS.getSaveDirectory() + "/" + fileName + "/[");

                            number = 0;
                            mHandler.sendEmptyMessage(3);

                        } else {

                            mHandler.sendEmptyMessage(0);

                            if (code == 401) {
                                mBaseFrameAty.showErrorToast("登录已失效");
                                UserManger.setIsLogin(false);
                                AppManger.getInstance().killAllActivity();
                                mBaseFrameAty.dismissLoadingDialog();
                                mBaseFrameAty.setHasAnimiation(false);
                                mBaseFrameAty.startActivity(LoginAty.class, null, false);
                                mBaseFrameAty.finish();
                            }
                        }
                    }

                } catch (Exception e) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    e.printStackTrace(new PrintStream(baos));
                    String exception = baos.toString();
                    Logger.w(exception);
                    mBaseFrameAty.dismissLoadingDialog();
                    mBaseFrameAty.showErrorToast("网络连接错误");
                    mHandler.sendEmptyMessage(0);
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Logger.w(t.getMessage() + call.request().url().toString());
                mBaseFrameAty.dismissLoadingDialog();
                mBaseFrameAty.showErrorToast("网络连接错误");
                mHandler.sendEmptyMessage(0);

            }
        });

    }


    public interface UpImageListener {

        void onUpSuccess();

        void onUpFailure();

        void onUpLoading(int progress);

    }


    /**
     * 删除oos上的图片
     */
    public void deleteFile() {
        if (filesPath == null || mAliyunSTS == null || mOss == null) {
            return;
        }
        for (PicInfo picInfo : filesPath) {


            String path = picInfo.getPath();

            File file = new File(path);
            String objectKey = mAliyunSTS.getSaveDirectory() + "/" + fileName + "/" + file.getName();

            // 创建删除请求
            DeleteObjectRequest delete = new DeleteObjectRequest(mAliyunSTS.getBucketName(), objectKey);
            // 异步删除
            OSSAsyncTask deleteTask = mOss.asyncDeleteObject(delete, new OSSCompletedCallback<DeleteObjectRequest, DeleteObjectResult>() {
                @Override
                public void onSuccess(DeleteObjectRequest request, DeleteObjectResult result) {
                    Log.d("result", "delete_success!");
                }

                @Override
                public void onFailure(DeleteObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                    // 请求异常
                    if (clientExcepion != null) {
                        // 本地异常如网络异常等
                        clientExcepion.printStackTrace();
                    }
                    if (serviceException != null) {
                        // 服务异常
                        Log.e("ErrorCode", serviceException.getErrorCode());
                        Log.e("RequestId", serviceException.getRequestId());
                        Log.e("HostId", serviceException.getHostId());
                        Log.e("RawMessage", serviceException.getRawMessage());
                    }
                }

            });


        }


    }

    /**
     * 删除oos上的图片
     */
    public void deleteFile(String objKey) {
        if (filesPath == null || mAliyunSTS == null || mOss == null) {
            return;
        }

        // 创建删除请求
        DeleteObjectRequest delete = new DeleteObjectRequest(mAliyunSTS.getBucketName(), objKey);
        // 异步删除
        OSSAsyncTask deleteTask = mOss.asyncDeleteObject(delete, new OSSCompletedCallback<DeleteObjectRequest, DeleteObjectResult>() {
            @Override
            public void onSuccess(DeleteObjectRequest request, DeleteObjectResult result) {
                Log.d("result", "delete_success!");
            }

            @Override
            public void onFailure(DeleteObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }

        });


    }


    private void doDeleteHttp(Call<ResponseBody> bodyCall) {

        bodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    String result = response.body().string();
                    Logger.json(result);
                    JSONObject object = JSONObject.parseObject(result);
                    if (object.containsKey("code")) {
                        int code = object.getInteger("code");
                        if (code == 200) {

                            AliyunSTS aliyunSTS = AppJsonUtil.getObject(result, "aliyunSTS", AliyunSTS.class);

                            OSSStsTokenCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(aliyunSTS.getAccessKeyId(), aliyunSTS.getAccessKeySecret(), aliyunSTS.getSecurityToken());
                            String endpoint = aliyunSTS.getEndpoint();
                            OSSClient oss = new OSSClient(MyApplication.getApplicationCotext(), endpoint, credentialProvider);
                            for (String objectKey : objList) {

                                // 创建删除请求
                                DeleteObjectRequest delete = new DeleteObjectRequest(aliyunSTS.getBucketName(), objectKey);
                                // 异步删除
                                OSSAsyncTask deleteTask = oss.asyncDeleteObject(delete, new OSSCompletedCallback<DeleteObjectRequest, DeleteObjectResult>() {
                                    @Override
                                    public void onSuccess(DeleteObjectRequest request, DeleteObjectResult result) {
                                        Log.d("result", "delete_success!");
                                    }

                                    @Override
                                    public void onFailure(DeleteObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                                        // 请求异常
                                        if (clientExcepion != null) {
                                            // 本地异常如网络异常等
                                            clientExcepion.printStackTrace();
                                        }
                                        if (serviceException != null) {
                                            // 服务异常
                                            Log.e("ErrorCode", serviceException.getErrorCode());
                                            Log.e("RequestId", serviceException.getRequestId());
                                            Log.e("HostId", serviceException.getHostId());
                                            Log.e("RawMessage", serviceException.getRawMessage());
                                        }
                                    }

                                });


                            }


                        } else {


                            if (code == 401) {
                                mBaseFrameAty.showErrorToast("登录已失效");
                                UserManger.setIsLogin(false);
                                AppManger.getInstance().killAllActivity();
                                mBaseFrameAty.setHasAnimiation(false);
                                mBaseFrameAty.startActivity(LoginAty.class, null, false);
                                mBaseFrameAty.finish();
                            }
                        }
                    }

                } catch (Exception e) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    e.printStackTrace(new PrintStream(baos));
                    String exception = baos.toString();
                    Logger.w(exception);
                    mBaseFrameAty.showErrorToast("网络连接错误");

                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Logger.w(t.getMessage() + call.request().url().toString());
                mBaseFrameAty.showErrorToast("网络连接错误");
            }
        });

    }

    public UpImageUtils() {
    }

    List<String> objList;

    /**
     * 删除oos上的图片
     */
    public void deleteOtherFile(List<String> objList, String tag) {
        if (objList == null || objList.size() == 0) {
            return;
        }
        this.objList = objList;

        //获取token
        doDeleteHttp(RetrofitUtils.createApi(Image.class).orderAssumeRole(tag));


    }

    /**
     * 退出界面，清除数据
     */
    public void clear() {
        mAliyunSTS = null;
        if (filesPath != null) {
            filesPath.clear();
        }
        filesPath = null;
        STATE = 0;
    }

    /**
     * 停止上传
     */
    public void stop() {
        if (mTask != null) {
            mTask.cancel();
            STATE = STATE_STOP;
        }
    }


    /**
     * 在acitivity 的 onDestory中执行，如果提交数据没有执行，直接删除已上传的数据
     */
    public void isOnDestoryDoing() {

        //本地提交数据不成功需要删除数据
        if (!isCommitSuccess) {
            stop();
            deleteFile();
        }
        clear();
    }

    /**
     * 在acitivity 的 sOnBackPressed中执行
     */
    public void isOnBackPressedDoing() {

        //如果上传中点了取消，直接停止，删除已上传的
        if (STATE == STATE_START) {
            stop();
            deleteFile();
        }
    }


}
