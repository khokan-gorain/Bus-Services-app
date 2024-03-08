package com.khokan_gorain_nsubusservices_app.Activityes;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.khokan_gorain_nsubusservices_app.ApiController.ApiController;
import com.khokan_gorain_nsubusservices_app.ConstantData.Constant;
import com.khokan_gorain_nsubusservices_app.Fragments.BusListFragment;
import com.khokan_gorain_nsubusservices_app.Fragments.ContactsFragment;
import com.khokan_gorain_nsubusservices_app.Fragments.HomeFragment;
import com.khokan_gorain_nsubusservices_app.Fragments.MyBusFragment;
import com.khokan_gorain_nsubusservices_app.Fragments.ProfileFragment;
import com.khokan_gorain_nsubusservices_app.ModelClass.DatabaseResponseMsg;
import com.khokan_gorain_nsubusservices_app.ModelClass.UserLoginInfo;
import com.khokan_gorain_nsubusservices_app.R;
import com.khokan_gorain_nsubusservices_app.SharedPreference.UserLoginData;
import com.khokan_gorain_nsubusservices_app.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    String current_location;
    FusedLocationProviderClient fusedLocationProviderClient;
    AlertDialog dialog,checkInternet;
    public  static Toolbar mToolbar;
    EditText userName,password;
    Button loginBtn;
    String uName,uPassword;
    Button internetRetryBtn;
    NavigationView navigationView;
    TextInputEditText inputDvPhoneNumber;
    String userPhoneNumber,otpVerificationCode;
    View loginDialog;
    private FirebaseAuth kAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    EditText et1,et2,et3,et4,et5,et6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setSupportActionBar(binding.appBarMain.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        binding.appBarMain.toolbar.setNavigationIcon(R.drawable.ic_about);

//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_about);
//        getSupportActionBar().setTitle("hello");
//        getSupportActionBar().setDisplayShowTitleEnabled(true);
//        getSupportActionBar().setTitle("Hello");
       // getSupportActionBar().setTitle("Bus Services");
//        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_mybus, R.id.nav_user_profile,R.id.nav_contacts)
                .setOpenableLayout(drawer)
                .build();


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        NavigationUI.setupWithNavController(binding.appBarMain.toolbar, navController, mAppBarConfiguration);
        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("Bus Services");
        });

      //  NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


//        navController.addOnDestinationChangedListener { controller, destination, arguments ->
//            if (destination.id == R.id.nav_home){
//                toolbar.setNavigationIcon(R.drawable.xxxx)
//            }
//        }


        kAuth = FirebaseAuth.getInstance();
        // Internet Checking Dialog Box
        AlertDialog.Builder internetCheck = new AlertDialog.Builder(MainActivity.this);
        View noInternet = LayoutInflater.from(MainActivity.this).inflate(R.layout.no_internet_connection_dialog,null);
        internetRetryBtn = noInternet.findViewById(R.id.retryBtn);
        internetCheck.setView(noInternet);
        checkInternet = internetCheck.create();
        checkInternet.setCancelable(false);
        checkInternet.getWindow().setGravity(Gravity.CENTER);


        // Login Dialog Box
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        loginDialog = LayoutInflater.from(MainActivity.this).inflate(R.layout.login_dialog,null);
        builder.setView(loginDialog);
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.getWindow().setGravity(Gravity.CENTER);
        userName = loginDialog.findViewById(R.id.userName);
        password = loginDialog.findViewById(R.id.userPassword);
        loginBtn = loginDialog.findViewById(R.id.loginBtn);


        loginDialog.findViewById(R.id.forgotPassword).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View view) {
                loginDialog.findViewById(R.id.login_lyt).setVisibility(View.GONE);
                loginDialog.findViewById(R.id.phoneNoLyt).setVisibility(View.VISIBLE);
            }
        });
        loginDialog.findViewById(R.id.loginNow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginDialog.findViewById(R.id.login_lyt).setVisibility(View.VISIBLE);
                loginDialog.findViewById(R.id.phoneNoLyt).setVisibility(View.GONE);
            }
        });
        loginDialog.findViewById(R.id.getOtpBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // checkPhoneNumberIsExist();
                phoneNumberIsExist();
            }
        });
        loginDialog.findViewById(R.id.verifyOtpBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyOtp(otpVerificationCode);
            }
        });
        loginDialog.findViewById(R.id.changePassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });

     navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
         @Override
         public boolean onNavigationItemSelected(@NonNull MenuItem item) {

             drawer.closeDrawer(GravityCompat.START);
             item.setChecked(true);
             switch (item.getItemId()){
                 case R.id.nav_home:
                     replaceFragment(new HomeFragment());
                     break;
                 case R.id.nav_mybus:
                     replaceFragment(new MyBusFragment());
                     break;
                 case R.id.nav_bus_list:
                     replaceFragment(new BusListFragment());
                     break;
                 case R.id.nav_user_profile:
                     replaceFragment(new ProfileFragment());
                     break;
                 case R.id.nav_contacts:
                     replaceFragment(new ContactsFragment());
                     break;
                 case R.id.nav_about_us:
                     Intent about_us = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/khokan-gorain-1a431321b/"));
                     startActivity(about_us);
                     break;
                 case R.id.nav_rating:
                     Intent rating = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                     startActivity(rating);
                     break;
                 case R.id.nav_share:

                     Intent share = new Intent(Intent.ACTION_SEND);
                     share.setType("text/plain");
                     String shareBody = "https://play.google.com/store/apps/details?id=" +getPackageName();
                     share.putExtra(Intent.EXTRA_TEXT,shareBody);
                     startActivity(Intent.createChooser(share,"Share Using"));
                     break;
                 case R.id.nav_policy:
                    // Toast.makeText(MainActivity.this, "This is a project. So we have no privacy policy...", Toast.LENGTH_SHORT).show();
                     Intent policy = new Intent(Intent.ACTION_VIEW, Uri.parse("http://stdroom.in/nsu/bus_services.html"));
                     startActivity(policy);
                     break;
                 case R.id.nav_log_out:
//
                     logOutDialog();
                     break;
             }
             return true;
         }
     });


        checkConnection();  // Call should be first

        internetRetryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
                replaceFragment(new HomeFragment());
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }


    public void checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile_network = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnected()) {
            checkInternet.dismiss();
            loginProcess();
        } else if(mobile_network.isConnected()) {
            checkInternet.dismiss();
            loginProcess();
        } else {
            checkInternet.show();
        }

    }

    public void loginProcess(){

        UserLoginData userLoginData = new UserLoginData(MainActivity.this);
        if(userLoginData.getUserName().isEmpty() && userLoginData.getPassword().isEmpty()){

            dialog.show();
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 uName = userName.getText().toString().trim();
                 uPassword = password.getText().toString().trim();
                 if(uName.isEmpty()){
                     userName.setError("User name is empty...");
                 } else if(uPassword.isEmpty()){
                     password.setError("Password is empty...");
                 } else {
                     userLoginCheck(uName,uPassword);
                 }
                }
            });
        } else {
            userLoginCheck(userLoginData.getUserName(), userLoginData.getPassword());
        }
    }

    public void userLoginCheck(String userName,String userPassword){


        Call<List<UserLoginInfo>> call = ApiController
                .getInstance()
                .getApi()
                .getUserLoginData(userName,userPassword);

        call.enqueue(new Callback<List<UserLoginInfo>>() {
            @Override
            public void onResponse(Call<List<UserLoginInfo>> call, Response<List<UserLoginInfo>> response) {
                List<UserLoginInfo> data = response.body();
                if(data == null){

                    Toast.makeText(MainActivity.this, "Invalid user name and password", Toast.LENGTH_LONG).show();
                } else {

                    dialog.dismiss();
                    UserLoginData userLoginData = new UserLoginData(getApplicationContext());
                    userLoginData.setUserName(data.get(0).getUsername());
                    userLoginData.setPassword(data.get(0).getSpassword());

                    ArrayList<UserLoginInfo> userDataArray = new ArrayList<>();
                    for(int i=0;i<data.size();i++){
                        UserLoginInfo userLoginInfo = new UserLoginInfo(data.get(i).getId(),data.get(i).getSname(),data.get(i).getUsername(),
                                data.get(i).getSphone_no(),data.get(i).getSmsg(),data.get(i).getSemail_id(),data.get(i).getSpassword(),
                                data.get(i).getSprofile_img());
                        userDataArray.add(userLoginInfo);
                        Constant.userName = data.get(i).getUsername();
                        Constant.userFullName = data.get(i).getSname();
                        Constant.userProfile = data.get(i).getSprofile_img();
                        Constant.userPhoneNumber = data.get(i).getSphone_no();
                        Constant.userEmailId = data.get(i).getSemail_id();

                    }

                    navigationView = (NavigationView) findViewById(R.id.nav_view);
                    View headerView  = navigationView.getHeaderView(0);
                    TextView userName = headerView.findViewById(R.id.userName);
                    TextView userPhoneNumber = headerView.findViewById(R.id.userPhoneNum);
                    ImageView userProfile = headerView.findViewById(R.id.userProfile);

                    userName.setText(Constant.userFullName);
                    userPhoneNumber.setText(String.valueOf(Constant.userPhoneNumber));
                    Glide.with(getApplicationContext()).load(Constant.MAIN_URL+Constant.userProfile).into(userProfile);
                }
            }

            @Override
            public void onFailure(Call<List<UserLoginInfo>> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Something went wrong. Please try again...", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void phoneNumberIsExist(){

        EditText phoneNo = loginDialog.findViewById(R.id.userPhoneN);
        userPhoneNumber = phoneNo.getText().toString().trim();
        if(userPhoneNumber.isEmpty()){
            phoneNo.setError("Empty Field...");
        } else if(userPhoneNumber.length() != 10){
            Toast.makeText(this, "Invalid Phone Number", Toast.LENGTH_LONG).show();
        } else {
            Call<DatabaseResponseMsg> call = ApiController
                    .getInstance()
                    .getApi()
                    .isPhoneNumberExist(userPhoneNumber);
            call.enqueue(new Callback<DatabaseResponseMsg>() {
                @Override
                public void onResponse(Call<DatabaseResponseMsg> call, Response<DatabaseResponseMsg> response) {
                    DatabaseResponseMsg data = response.body();
                    if(data.getMessage().equalsIgnoreCase("success")){

                       // Toast.makeText(MainActivity.this, "Success...", Toast.LENGTH_LONG).show();
                         sendOtp();
                    } else {
                        Toast.makeText(MainActivity.this, "Phone number not register....", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<DatabaseResponseMsg> call, Throwable t) {
                    Toast.makeText(MainActivity.this, toString().trim(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void sendOtp(){

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Verification field...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                progressDialog.dismiss();
                otpVerificationCode = verificationId;
                loginDialog.findViewById(R.id.phoneNoLyt).setVisibility(View.GONE);
                loginDialog.findViewById(R.id.otp_lyt).setVisibility(View.VISIBLE);

                et1 = loginDialog.findViewById(R.id.etC1);
                et2 = loginDialog.findViewById(R.id.etC2);
                et3 = loginDialog.findViewById(R.id.etC3);
                et4 = loginDialog.findViewById(R.id.etC4);
                et5 = loginDialog.findViewById(R.id.etC5);
                et6 = loginDialog.findViewById(R.id.etC6);

                edTextInput();

            }
        };
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(kAuth)
                .setPhoneNumber("+91"+userPhoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(MainActivity.this)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void verifyOtp(String otpVerificationCode) {


        String c1 = et1.getText().toString().trim();
        String c2 = et2.getText().toString().trim();
        String c3 = et3.getText().toString().trim();
        String c4 = et4.getText().toString().trim();
        String c5 = et5.getText().toString().trim();
        String c6 = et6.getText().toString().trim();


        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Verifying....");

        if (c1.isEmpty() || c2.isEmpty() || c3.isEmpty() || c4.isEmpty() || c5.isEmpty() || c6.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill opt...", Toast.LENGTH_SHORT).show();
        } else if (otpVerificationCode != null) {
            progressDialog.show();
            String code = c1 + c2 + c3 + c4 + c5 + c6;
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpVerificationCode, code);
            FirebaseAuth
                    .getInstance()
                    .signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                loginDialog.findViewById(R.id.otp_lyt).setVisibility(View.GONE);
                                loginDialog.findViewById(R.id.verifyOtpBtn).setVisibility(View.GONE);
                                loginDialog.findViewById(R.id.newPasswordLyt).setVisibility(View.VISIBLE);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Invalid OTP", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Please inter a valid otp", Toast.LENGTH_LONG).show();
        }
    }

    public void edTextInput() {
        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                et2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                et3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                et4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                et5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                et6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void changePassword(){

        TextInputEditText newPassword = loginDialog.findViewById(R.id.dvNewPassword);
        TextInputEditText confirmPassword = loginDialog.findViewById(R.id.dvConfirmPassword);

        String userNewPassword = newPassword.getText().toString().trim();
        String userConfirmPassword = confirmPassword.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Updating password...");

        if(userNewPassword.equalsIgnoreCase("")){
            newPassword.setError("New password is empty...");
        } else if(userNewPassword.equalsIgnoreCase("")){
            confirmPassword.setError("Confirm password is empty...");
        } else if(!userNewPassword.equals(userConfirmPassword)){
            Toast.makeText(getApplicationContext(), "Password not matching...", Toast.LENGTH_LONG).show();
        } else {
            progressDialog.show();
            Call<DatabaseResponseMsg> call = ApiController
                    .getInstance()
                    .getApi()
                    .changeUserPassword(userPhoneNumber,userConfirmPassword);
            call.enqueue(new Callback<DatabaseResponseMsg>() {
                @Override
                public void onResponse(Call<DatabaseResponseMsg> call, Response<DatabaseResponseMsg> response) {
                    DatabaseResponseMsg data = response.body();
                    if(data.getMessage().equalsIgnoreCase("success")){
                        progressDialog.dismiss();
                        loginDialog.findViewById(R.id.newPasswordLyt).setVisibility(View.GONE);
                        loginDialog.findViewById(R.id.login_lyt).setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this, "Password update successful...", Toast.LENGTH_LONG).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Something went wrong. Please try again...", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<DatabaseResponseMsg> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Something went wrong please try again after some time...", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void logOutDialog(){

                // Toast.makeText(getApplicationContext(), "Oye", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setCancelable(false);
                alertDialog.setIcon(R.drawable.ic_about);
                alertDialog.setTitle("Are you sure");
                alertDialog.setMessage("You want to logout...");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UserLoginData userLoginData = new UserLoginData(getApplicationContext());
                        userLoginData.logOutUser();
                        loginProcess();
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog logoutDialog = alertDialog.create();
                logoutDialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}