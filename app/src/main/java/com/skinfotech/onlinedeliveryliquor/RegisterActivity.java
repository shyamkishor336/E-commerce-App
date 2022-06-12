package com.skinfotech.onlinedeliveryliquor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;

public class RegisterActivity extends AppCompatActivity {
private FrameLayout frameLayout;
public static boolean onResetpasswordFragment=false;
public static Boolean setSignUpFragment = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        frameLayout = findViewById(R.id.register_framelayout);
        if (setSignUpFragment){
            setSignUpFragment = false;
            setDefaultFragment(new SignupFragment());
        }else {
            setDefaultFragment(new SigninFragment());
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode==KeyEvent.KEYCODE_BACK){
        SigninFragment.disableCloseBtn = false;
        SignupFragment.disableCloseBtn = false;
        if (onResetpasswordFragment){
            onResetpasswordFragment= false;
            setFragment(new SigninFragment());
            return false;
        }
    }

        return super.onKeyDown(keyCode, event);

    }

    private void setDefaultFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slide_out_right);
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
}