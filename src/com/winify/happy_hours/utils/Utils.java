package com.winify.happy_hours.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import retrofit.RetrofitError;
import retrofit.mime.MimeUtil;
import retrofit.mime.TypedInput;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class Utils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static byte[] streamToBytes(InputStream stream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (stream != null) {
            byte[] buf = new byte[1024];
            int r;
            while ((r = stream.read(buf)) != -1) {
                baos.write(buf, 0, r);
            }
        }
        return baos.toByteArray();
    }

    public static String getErrorMessage(RetrofitError retrofitError, String errorMsg) {
        if (retrofitError.getResponse() != null) {
            TypedInput body = retrofitError.getResponse().getBody();
            byte[] bytes = new byte[0];
            try {
                bytes = streamToBytes(body.in());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String charset = MimeUtil.parseCharset(body.mimeType());
            try {
                errorMsg = new String(bytes, charset);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return errorMsg;
    }
}
