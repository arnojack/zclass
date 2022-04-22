package com.example.zclass.online.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.zclass.MainActivity;
import com.example.zclass.online.Dao.Cou_Stu;
import com.example.zclass.online.Dao.Course;
import com.example.zclass.online.Dao.User;
import com.example.zclass.online.tool.BaseActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpClientUtils {

    private static final String TAG = "HttpClientUtils";

    public static void get(final String requestUrl, final HttpClientUtils.OnRequestCallBack callBack) {
        new Thread() {
            public void run() {
                getRequest(requestUrl, callBack);
            }
        }.start();
    }

    public static void post(final String requestUrl, final String params, final HttpClientUtils.OnRequestCallBack callBack) {
        new Thread() {
            public void run() {
                postRequest(requestUrl, params, callBack);
            }
        }.start();
    }
    public static String maptostr(Map<String,String> paramsMap){
        StringBuilder tempParams = new StringBuilder();
        int pos = 0;
        for (String key : paramsMap.keySet()) {
            if (pos >0) {
                tempParams.append("&");
            }
            try {
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            pos++;
        }
        String params = tempParams.toString();
        return params;
    }

    private static void getRequest(String requestUrl, HttpClientUtils.OnRequestCallBack callBack) {
        boolean isSuccess = false;
        String message;

        InputStream inputStream = null;
        ByteArrayOutputStream baos = null;
        try {
            URL url = null;
            try {
                url = new URL(requestUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection connection = null;
            try {
                assert url != null;
                connection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 设定请求的方法为"POST"，默认是GET
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            // User-Agent  IE9的标识
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0;");
            connection.setRequestProperty("Accept-Language", "zh-CN");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");
            /*
             * 当我们要获取我们请求的http地址访问的数据时就是使用connection.getInputStream().read()方式时我们就需要setDoInput(true)，
             * 根据api文档我们可知doInput默认就是为true。我们可以不用手动设置了，如果不需要读取输入流的话那就setDoInput(false)。
             * 当我们要采用非get请求给一个http网络地址传参 就是使用connection.getOutputStream().write() 方法时我们就需要setDoOutput(true), 默认是false
             */
            // 设置是否从httpUrlConnection读入，默认情况下是true;
            connection.setDoInput(true);
            // 设置是否向httpUrlConnection输出，如果是post请求，参数要放在http正文内，因此需要设为true, 默认是false;
            //connection.setDoOutput(true);//Android  4.0 GET时候 用这句会变成POST  报错java.io.FileNotFoundException
            connection.setUseCaches(false);
            connection.connect();//
            inputStream = connection.getInputStream();//会隐式调用connect()
            int contentLength = connection.getContentLength();
            if (connection.getResponseCode() == 200) {
                baos = new ByteArrayOutputStream();
                int readLen;
                byte[] bytes = new byte[1024];
                while ((readLen = inputStream.read(bytes)) != -1) {
                    baos.write(bytes, 0, readLen);
                }
                String result = baos.toString();
                Log.i(TAG, " result:" + result);

                message = result;
                isSuccess = true;
            } else {
                message = "请求失败 code:" + connection.getResponseCode();
            }

        } catch (IOException e) {
            message = e.getMessage();
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                message = e.getMessage();
                e.printStackTrace();
            }
        }
        if (isSuccess) {
            callBack.onSuccess(message);
        } else {
            callBack.onError(message);
        }
    }

    private static void postRequest(String requestUrl, String params, HttpClientUtils.OnRequestCallBack callBack) {
        boolean isSuccess = false;
        String message;
        InputStream inputStream = null;
        ByteArrayOutputStream baos = null;
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设定请求的方法为"POST"，默认是GET
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            // User-Agent  IE9的标识
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0;");
            connection.setRequestProperty("Accept-Language", "zh-CN");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");
            /*
             * 当我们要获取我们请求的http地址访问的数据时就是使用connection.getInputStream().read()方式时我们就需要setDoInput(true)，
             * 根据api文档我们可知doInput默认就是为true。我们可以不用手动设置了，如果不需要读取输入流的话那就setDoInput(false)。
             * 当我们要采用非get请求给一个http网络地址传参 就是使用connection.getOutputStream().write() 方法时我们就需要setDoOutput(true), 默认是false
             */
            // 设置是否从httpUrlConnection读入，默认情况下是true;
            connection.setDoInput(true);
            // 设置是否向httpUrlConnection输出，如果是post请求，参数要放在http正文内，因此需要设为true, 默认是false;
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            // set  params three way  OutputStreamWriter
            OutputStreamWriter out = new OutputStreamWriter(
                    connection.getOutputStream(), StandardCharsets.UTF_8);
            // 发送请求params参数
            out.write(params);
            out.flush();
            connection.connect();

            int contentLength = connection.getContentLength();
            if (connection.getResponseCode() == 200) {
                // 会隐式调用connect()
                inputStream = connection.getInputStream();

                baos = new ByteArrayOutputStream();
                int readLen;
                byte[] bytes = new byte[1024];
                while ((readLen = inputStream.read(bytes)) != -1) {
                    baos.write(bytes, 0, readLen);
                }
                String backStr = baos.toString();
                Log.i(TAG, "backStr:" + backStr);

                message = backStr;
                isSuccess = true;
            } else {
                message = "请求失败 code:" + connection.getResponseCode();
            }

        } catch (IOException e) {
            message = e.getMessage();
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                message = e.getMessage();
                e.printStackTrace();
            }
        }
        if (isSuccess) {
            callBack.onSuccess(message);
        } else {
            callBack.onError(message);
        }
    }

    public static void uploadic(String method,String filename, String filePath,Callback callback){
        OkHttpClient client = new OkHttpClient();
        String url= BaseActivity.BaseUrl+"uploadHandleServlet2";
        String filetype=getMimeType(filename);

        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder()
                .addFormDataPart("file", filename,
                        RequestBody.create(MediaType.parse(filetype), new File(filePath)))
                .setType(MultipartBody.FORM);

        multipartBodyBuilder.addFormDataPart(User.USERID, MainActivity.user_info.getUserid());
        multipartBodyBuilder.addFormDataPart("method",method);

        RequestBody requestBody =multipartBodyBuilder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    private static String getMimeType(String fileName) {
        FileNameMap filenameMap = URLConnection.getFileNameMap();
        String contentType = filenameMap.getContentTypeFor(fileName);
        if (contentType == null) {
            contentType = "application/octet-stream"; //* exe,所有的可执行程序
        }
        return contentType;
    }
    /*
     * @param saveDir 储存下载文件的SDCard目录
     * @param listener 下载监听
     */
    public static void download( String method, String cou_on_id, OnDownloadListener listener) {
        String suffx=".jpg";
        String url=BaseActivity.BaseUrl+"downLoadServlet1";
        //String saveDir=getExternalCacheDir().getAbsolutePath();
        String saveDir="/data/local/tmp/com.example.zclass/";
        String filename=MainActivity.user_info.getUserid()+suffx;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .build();

        FormBody formBody = null;
        switch (method){
            case "icon":
                formBody= new FormBody.Builder()
                        .add("method",method)
                        .add("filename","1.jpg")
                        .add(User.USERID, MainActivity.user_info.getUserid())
                        .build();

                break;
            case "work":
                formBody= new FormBody.Builder()
                        .add("method",method)
                        .add("filename","1.jpg")
                        .add(Course.COUONID, cou_on_id)
                        .add(User.USERID, MainActivity.user_info.getUserid())
                        .build();

                break;
        }
        assert formBody != null;
        Request request= new Request.Builder()
                .post(formBody)
                .url(url)
                .build();

        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                listener.onDownloadFailed(e.getLocalizedMessage());
            }
            @Override
            public void onResponse(Call call, Response response) {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;

                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(saveDir,filename);
                    if(!file.exists())
                        file.createNewFile();
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        listener.onDownloading(progress);
                    }
                    fos.flush();
                    // 下载完成
                    listener.onDownloadSuccess();
                } catch (Exception e) {
                    listener.onDownloadFailed(e.toString());
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException ignored) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        });
    }

    public interface OnRequestCallBack {
        void onSuccess(String json);
        void onError(String errorMsg);
    }
    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess();

        /**
         * @param progress
         * 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed(String msg);
    }
}
