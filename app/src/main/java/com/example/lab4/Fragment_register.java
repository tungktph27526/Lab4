package com.example.lab4;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lab4.Api.RequestInterface;
import com.example.lab4.Model.ServerRequest;
import com.example.lab4.Model.ServerResponse;
import com.example.lab4.Model.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_register#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_register extends Fragment implements View.OnClickListener{
    private AppCompatButton btn_register;
    private EditText et_email,et_password,et_name;
    private TextView tv_login;
    private ProgressBar progress;
    public Fragment_register() {
        // Required empty public constructor
    }

    public static Fragment_register newInstance() {
        Fragment_register fragment = new Fragment_register();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register,container,false);
        initViews(view);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_login) {
            goToLogin();
        } else if (view.getId() == R.id.btn_register) {
            String name = et_name.getText().toString();
            String email = et_email.getText().toString();
            String password = et_password.getText().toString();
            if (!name.isEmpty() && !email.isEmpty()
                    && !password.isEmpty()) {
                progress.setVisibility(View.VISIBLE);
                registerProcess(name, email, password);
            } else {
                Snackbar.make(getView(), "Fields are empty !",
                        Snackbar.LENGTH_LONG).show();
            }
        }
    }
    private void  initViews(View view){
        btn_register = (AppCompatButton)view.findViewById(R.id.btn_register);
        tv_login = (TextView)view.findViewById(R.id.tv_login);
        et_name = (EditText)view.findViewById(R.id.et_name);
        et_email = (EditText)view.findViewById(R.id.et_email);
        et_password = (EditText)view.findViewById(R.id.et_password);
        progress = (ProgressBar)view.findViewById(R.id.progress);
        btn_register.setOnClickListener(this);
        tv_login.setOnClickListener(this);
    }
    private void registerProcess(String name, String email, String password){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.REGISTER_OPERATION);
        request.setUser(user);
        Call<ServerResponse> response = requestInterface.operation(request);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse resp = response.body();
                Snackbar.make(getView(), resp.getMessage(),
                        Snackbar.LENGTH_LONG).show();
                Log.d("TAG", "onResponse: "+resp.getMessage());
                progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progress.setVisibility(View.INVISIBLE);
                Log.d(Constants.TAG,"failed");
                Snackbar.make(getView(), t.getLocalizedMessage(),
                        Snackbar.LENGTH_LONG).show();
                t.printStackTrace(); // Add this line to print the stack trace
                Log.d("TAG_Res", "onFailure: " + t.getMessage());

            }
        });
    }
    private void goToLogin(){
        Fragment login = new Fragment_login();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame,login);
        ft.commit();
    }

}