package de.patri.ck.clipperdbx.fragmente;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import android.os.AsyncTask;

import org.json.JSONObject;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.graphics.Color;

import androidx.fragment.app.Fragment;

import org.json.JSONException;

import de.patri.ck.clipperdbx.Main;
import de.patri.ck.clipperdbx.R;
import de.patri.ck.clipperdbx.app.Config;
import de.patri.ck.clipperdbx.app.pApp;
import de.patri.ck.clipperdbx.common.utils.LoginParser;

public class Login extends Fragment {

  public static final String TAG = Config.TITLE_LOGiN;

  TextView tvMsg;
  EditText etName, etPass;

  Button btn_login, btn_cancel;

  LoginParser jsonParser = new LoginParser();
  int i = 0;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    ((Main) getContext()).setSubTitle(Config.TITLE_LOGiN);
    View content = inflater.inflate(R.layout.fragment_login, container, false);

    tvMsg  = content.findViewById(R.id.tvMsg);
    etName = content.findViewById(R.id.etName);
    etPass = content.findViewById(R.id.etPass);

        tvMsg.setVisibility(View.GONE);

        btn_login = content.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttemptLogin attemptLogin = new AttemptLogin();
                attemptLogin.execute(etName.getText().toString(), etPass.getText().toString(),"");
            }
        });

        btn_cancel = content.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etName.setText("");
                etPass.setText("");
                tvMsg.setVisibility(View.GONE);
            }
        });
        return content;
    }

    private class AttemptLogin extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            String name = args[0];
            String pass = args[1];

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_name", name));
            params.add(new BasicNameValuePair("user_pass", pass));

            JSONObject json = jsonParser.makeHttpRequest(Config.API_URL+"/login/all", "POST", params);

            return json;
        }

        protected void onPostExecute(JSONObject result) {
            try {
                if(result != null) {
                    tvMsg.setTextColor(Color.GREEN);
                    tvMsg.setText(result.getString("message"));
                } else {
                    tvMsg.setTextColor(Color.RED);
                    tvMsg.setText("Unable to retrieve any data from server");
                }
                tvMsg.setVisibility(View.VISIBLE);
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }

}