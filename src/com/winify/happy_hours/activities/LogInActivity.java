package com.winify.happy_hours.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.winify.happy_hours.ApplicationPreferences;
import com.winify.happy_hours.R;
import com.winify.happy_hours.constants.Extra;
import com.winify.happy_hours.controller.ServiceGateway;
import com.winify.happy_hours.listeners.ServiceListener;
import com.winify.happy_hours.models.Token;
import com.winify.happy_hours.models.User;
import com.winify.happy_hours.service.TrackerService;
import com.winify.happy_hours.utils.Utils;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.MimeUtil;
import retrofit.mime.TypedInput;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class LogInActivity extends Activity {
    private EditText login;
    private EditText password;
    private ApplicationPreferences preferences;
    private ProgressBar progressBar;
    private TrackerService service;
    private String errorMsg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ServiceGateway serviceGateway = new ServiceGateway(LogInActivity.this);
        service = serviceGateway.getService();
        preferences = new ApplicationPreferences(this);

        if (!Utils.isNetworkAvailable(this)) {

            showErrorMessage("Check you're Internet connection,it might be closed");
        }

        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);

        Button b = (Button) findViewById(R.id.logingBtn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (preferences.getIp().equals("") ||
                        preferences.getPort().equals("")) {
                    showErrorMessage("Check your Ip Address and Port in  settings");
                } else {
                    if (login.getText().toString().length() > 0 && password.getText().toString().length() > 0) {
                        getKeyToken(login.getText().toString(), password.getText().toString());
                        progressBar = (ProgressBar) findViewById(R.id.progressBar);
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void redirectHomePage() {
        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
        startActivity(intent);
        LogInActivity.this.finish();
    }

    public void getKeyToken(String login, String password) {
        User user = new User(login, password);
        preferences.removeToken();
        service.getToken(user, new ServiceListener<Token>() {

            @Override
            public void success(Token token, Response response) {
                preferences.saveToken(token.getToken());
                Toast.makeText(LogInActivity.this, preferences.getKeyToken(), Toast.LENGTH_LONG).show();
                redirectHomePage();
                finish();
            }

            @Override
            public void failure(RetrofitError retrofitError) {

                if (getErrorMessage(retrofitError).equals(Extra.BAD_CREDENTIALS_MESSAGE)) {
                    showErrorMessage("Please check your Username and Password");
                } else {
                    showErrorMessage("Please check your connection with Server");
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showErrorMessage(String error) {
        AlertDialog ad = new AlertDialog.Builder(this).create();
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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater Inflater = getMenuInflater();
        Inflater.inflate(R.menu.loginmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.startSettings: {
                Intent intent = new Intent(LogInActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
            break;
        }
        return super.onOptionsItemSelected(item);
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
}

