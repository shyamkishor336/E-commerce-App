package com.skinfotech.onlinedeliveryliquor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import static com.skinfotech.onlinedeliveryliquor.RegisterActivity.onResetpasswordFragment;

/**
 * A simple {@link Fragment} subclass.

 */
public class SigninFragment extends Fragment {


    public SigninFragment() {
        // Required empty public constructor
    }

   private TextView dontHaveanAccount;
    private FrameLayout parentFrameLayout;
    private EditText email,password;
    private ImageButton close;
    private TextView forgotPassword;
    private Button signin;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private String emailPattern ="[a-zA-z0-9._-]+@[a-z]+.[a-z]+";
    public static boolean disableCloseBtn = false;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signin, container, false);
        dontHaveanAccount = view.findViewById(R.id.signup);
        parentFrameLayout = getActivity().findViewById(R.id.register_framelayout);
        email = view.findViewById(R.id.signin_email);
        password = view.findViewById(R.id.sign_password);
        forgotPassword = view.findViewById(R.id.signin_forget_pw);
        progressBar = view.findViewById(R.id.signin_progressbar);
        close = view.findViewById(R.id.sign_in_close_btn);

        signin = view.findViewById(R.id.signin_btn);
        firebaseAuth = FirebaseAuth.getInstance();
        if (disableCloseBtn){
            close.setVisibility(View.GONE);
        }else {
            close.setVisibility(View.VISIBLE);
        }


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dontHaveanAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SignupFragment());
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onResetpasswordFragment=true;
                setFragment(new ResetPasswordFragment());
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minIntent();
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        checkInputs();
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmailandPassword();
            }
        });
    }
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_rignt,R.anim.slide_out_left);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
    private void checkInputs(){
        if (!TextUtils.isEmpty(email.getText())){
            if (!TextUtils.isEmpty(password.getText())){
                signin.setEnabled(true);
                signin.setTextColor(Color.rgb(255,255,255));
            }else {
                signin.setEnabled(false);
                signin.setTextColor(Color.argb(50,255,255,255));
            }
        }else {
            signin.setEnabled(false);
            signin.setTextColor(Color.argb(50,255,255,255));
        }
    }
    private void checkEmailandPassword(){
        if (email.getText().toString().matches(emailPattern)){
            if (password.length()>=8){

                progressBar.setVisibility(View.VISIBLE);
                signin.setEnabled(false);
                signin.setTextColor(Color.argb(50,255,255,255));

                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    minIntent();
                                }else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    signin.setEnabled(true);
                                    signin.setTextColor(Color.rgb(255,255,255));
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(),error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }else {
                Toast.makeText(getActivity(), "Incorrect email or password!", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getActivity(), "Incorrect email or password!", Toast.LENGTH_SHORT).show();

        }
    }
    private void minIntent(){
        if (disableCloseBtn){
            disableCloseBtn=false;

        }else {
            Intent mainIntent = new Intent(getActivity(), MainActivity.class);
            startActivity(mainIntent);
            getActivity().finish();
        }
    }
}