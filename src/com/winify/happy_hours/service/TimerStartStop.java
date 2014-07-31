package com.winify.happy_hours.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import com.winify.happy_hours.ApplicationPreferences;
import com.winify.happy_hours.R;
import com.winify.happy_hours.constants.Constants;
import com.winify.happy_hours.controller.ServiceGateway;
import com.winify.happy_hours.listeners.ServiceListener;
import com.winify.happy_hours.models.Time;
import com.winify.happy_hours.models.Token;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.MimeUtil;
import retrofit.mime.TypedInput;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class TimerStartStop extends Thread {

    private EditText timerView;
    private Activity activity;
    private Boolean runThread;
    private String serverTime;
    private TrackerService service;
    private ApplicationPreferences preferences;
    private String errorMsg;

    public TimerStartStop(EditText timerView, Activity activity, Boolean runThread) {
        this.timerView = timerView;
        this.runThread = runThread;
        this.activity = activity;
    }

    public void setRunThread(Boolean runThread) {
        this.runThread = runThread;
    }

    @Override
    public void run() {
        super.run();

        try {
            synchronized (this) {
                ServiceGateway serviceGateway = new ServiceGateway(activity);
                service = serviceGateway.getService();
                while (runThread) {
                    getServerTime();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timerView.setText(serverTime);
                        }
                    });

                    Thread.sleep(30000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getServerTime() {
        preferences = new ApplicationPreferences(activity);
        Token token = new Token(preferences.getKeyToken());
        service.getWorkedTime(token, new ServiceListener<Time>() {
            @Override
            public void success(Time time, Response response) {
                serverTime = convertTime(time.getDaily());
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                if (retrofitError.getResponse() != null) {
                    if (getErrorMessage(retrofitError).equals(Constants.TOKEN_EXPIRE)) {
                        preferences.removeToken();
                        showErrorMessage("Your session has expired, please logout in login again");
                    }
                } else {
                    showErrorMessage(activity.getResources().getString(R.string.server_bad_connection));
                }
            }
        });
    }

    private String getErrorMessage(RetrofitError retrofitError) {
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

    private String convertTime(String time) {

        int milliseconds = Integer.parseInt(time);
        int hour = (milliseconds / (1000 * 60 * 60)) % 24;
        int min = ((milliseconds - (milliseconds / (1000 * 60 * 60))) / (1000 * 60)) % 60;
        return hour + "h : " + min + "m";
    }

    static byte[] streamToBytes(InputStream stream) throws IOException {
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
    private void showErrorMessage(String error) {
        AlertDialog ad = new AlertDialog.Builder(activity).create();
        ad.setCancelable(false); // This blocks the 'BACK' button
        ad.setMessage(error);
        ad.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ad.show();
    }
}
