package com.android.llc.proringer.fragments.postProject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.LandScreenActivity;
import com.android.llc.proringer.activities.LogInActivity;
import com.android.llc.proringer.activities.PostProjectActivity;
import com.android.llc.proringer.activities.PostedFinishActivity;
import com.android.llc.proringer.activities.TermsPrivacyActivity;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.helper.CustomAlert;
import com.android.llc.proringer.helper.MyCustomAlertListener;
import com.android.llc.proringer.helper.MyLoader;
import com.android.llc.proringer.helper.ProServiceApiHelper;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.edittext.ProLightEditText;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;
import com.android.llc.proringer.viewsmod.textview.ProSemiBoldTextView;

/**
 * Created by su on 7/13/17.
 */

public class PostProjectRegistrationAndFinalizeFragment extends Fragment implements MyCustomAlertListener{
    private ProLightEditText first_name, last_name, email, password, confirm_password
//            , zip_code
            ;
    private LinearLayout content_post_form_submit;

    ProRegularTextView terms_and_policy;
    ProSemiBoldTextView tv_ok;
    private MyLoader myLoader = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_post_project_registration, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        first_name = (ProLightEditText) view.findViewById(R.id.first_name);
        last_name = (ProLightEditText) view.findViewById(R.id.last_name);
        email = (ProLightEditText) view.findViewById(R.id.email);
        password = (ProLightEditText) view.findViewById(R.id.password);
        confirm_password = (ProLightEditText) view.findViewById(R.id.confirm_password);

        //zip_code = (ProLightEditText) view.findViewById(R.id.zip_code);
        //zip_code.setEnabled(false);
        //zip_code.setClickable(false);

        content_post_form_submit = (LinearLayout) view.findViewById(R.id.content_post_form_submit);


        terms_and_policy = (ProRegularTextView) view.findViewById(R.id.terms_and_policy);
        terms_and_policy.setMovementMethod(LinkMovementMethod.getInstance());

        myLoader=new MyLoader(getActivity());
        /**
         * Contact us spannable text with click listener
         */
        String TextOne = "By Signing in up with ProRinger you agree with our  \n";
        String TextTermsClick = "Terms of Use ";
        String TextTwo = "and ";
        String TextPolicyClick = "Privacy Policy";


        Spannable word1 = new SpannableString(TextOne);
        word1.setSpan(new ForegroundColorSpan(getActivity().getResources().getColor(R.color.colorTextDark)), 0, TextOne.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        terms_and_policy.setText(word1);

        Spannable word2 = new SpannableString(TextTermsClick);
        ClickableSpan myClickableTermsSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // There is the OnCLick. put your intent to Register class here
                widget.invalidate();
                Logger.printMessage("SpanHello", "click");
                Intent intent = new Intent(getActivity(), TermsPrivacyActivity.class);
                intent.putExtra("value", "term");
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(getActivity().getResources().getColor(R.color.colorAccent));
                ds.setUnderlineText(false);
            }
        };
        word2.setSpan(myClickableTermsSpan, 0, TextTermsClick.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        terms_and_policy.append(word2);


        Spannable word3 = new SpannableString(TextTwo);
        word3.setSpan(new ForegroundColorSpan(getActivity().getResources().getColor(R.color.colorTextDark)), 0, TextTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        terms_and_policy.append(word3);


        Spannable word4 = new SpannableString(TextPolicyClick);
        ClickableSpan myClickablePolicySpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // There is the OnCLick. put your intent to Register class here
                widget.invalidate();
                Logger.printMessage("SpanHello", "click");
                Intent intent = new Intent(getActivity(), TermsPrivacyActivity.class);
                intent.putExtra("value", "policy");
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(getActivity().getResources().getColor(R.color.colorAccent));
                ds.setUnderlineText(false);
            }
        };
        word4.setSpan(myClickablePolicySpan, 0, TextPolicyClick.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        terms_and_policy.append(word4);


        view.findViewById(R.id.content_post_form_submit).setVisibility(View.GONE);

        view.findViewById(R.id.container_registration).setVisibility(View.GONE);

        if (ProApplication.getInstance().getUserId().equals("")) {
/**
 * IF no user is login then visible registration process or visible last part
 */
            view.findViewById(R.id.container_registration).setVisibility(View.VISIBLE);
            //zip_code.setText(((PostProjectActivity) getActivity()).selectedAddressData.getZip_code());

        } else {
            if (((PostProjectActivity) getActivity()).completePostProject()) {
                view.findViewById(R.id.content_post_form_submit).setVisibility(View.VISIBLE);
            }
        }

        view.findViewById(R.id.close_project).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ProApplication.getInstance().getUserId().equals("")) {
                    startActivity(new Intent((PostProjectActivity) getActivity(), PostedFinishActivity.class));
                    ((PostProjectActivity) getActivity()).finish();
                } else {
                    Intent intent = new Intent(getActivity(), LandScreenActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    ((PostProjectActivity) getActivity()).finish();
                }

            }
        });

        view.findViewById(R.id.tv_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateRegistration()) {

                    ((PostProjectActivity) getActivity()).first_name = first_name.getText().toString().trim();
                    ((PostProjectActivity) getActivity()).last_name = last_name.getText().toString().trim();
                    ((PostProjectActivity) getActivity()).email = email.getText().toString().trim();
                    ((PostProjectActivity) getActivity()).confirm_password = confirm_password.getText().toString().trim();
                    synchronized (new Object()) {
                        ((PostProjectActivity) getActivity()).completePostProject();
                    }

                }
            }
        });

        view.findViewById(R.id.tv_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginDialog();
            }
        });
    }

    private boolean validateRegistration() {
        if (first_name.getText().toString().trim().equals("")) {
            first_name.setError("Please enter First name.");
            return false;
        } else {
            if (last_name.getText().toString().trim().equals("")) {
                last_name.setError("Please enter Last name.");
                return false;
            } else {
                if (email.getText().toString().trim().equals("")) {
                    email.setError("Please enter Email.");
                    return false;

                } else {
                    if (Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
                        if (password.getText().toString().trim().equals("")) {
                            password.setError("Please enter Password.");
                            return false;

                        } else {
                            if (confirm_password.getText().toString().trim().equals("")) {
                                confirm_password.setError("Please enter Confirm password.");
                                return false;

                            } else {

                                if (confirm_password.getText().toString().trim().equals(password.getText().toString().trim())) {

                                    if (confirm_password.getText().toString().trim().length() > 6 &&
                                            confirm_password.getText().toString().trim().matches(".*[^0-9].*")) {
//                                        if (zip_code.getText().toString().trim().equals("")) {
//                                            zip_code.setError("Please enter Zip.");
//                                            return false;
//                                        } else {
                                            return true;
//                                        }
                                    } else {

                                        if (confirm_password.getText().toString().trim().length() < 6) {
                                            confirm_password.setError("Password should contains at least 6 character.");
                                        } else if (!confirm_password.getText().toString().trim().matches(".*[^0-9].*")) {
                                            confirm_password.setError("Password should combination of Alphanumeric and Number.");
                                        }
                                        return false;
                                    }


                                } else {
                                    confirm_password.setError("Password and Confirm password does not matched.");
                                    return false;
                                }
                            }
                        }
                    } else {
                        email.setError("Please enter valid Email address.");
                        return false;

                    }
                }
            }
        }
    }
    public void showProjectPosted() {
        content_post_form_submit.setVisibility(View.VISIBLE);
    }


    public void loginDialog(){

        final Dialog  dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_login);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        LinearLayout LLMain = (LinearLayout) dialog.findViewById(R.id.LLMain);

        ProRegularTextView tv_tittle = (ProRegularTextView) dialog.findViewById(R.id.tv_tittle);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        LLMain.getLayoutParams().width = (width - 30);
        LLMain.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
//        scrollView.getLayoutParams().height = (height-30)/2;

        dialog.findViewById(R.id.img_cancel_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tv_ok= (ProSemiBoldTextView) dialog.findViewById(R.id.tv_ok);

        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((EditText)dialog.findViewById(R.id.edt_email)).getText().toString().trim().equals("")) {
                    ((EditText)dialog.findViewById(R.id.edt_email)).setError("Please enter your email address.");
                } else if (((EditText)dialog.findViewById(R.id.edt_password)).getText().toString().trim().equals("")) {
                    ((EditText)dialog.findViewById(R.id.edt_password)).setError("Please enter password.");
                } else {
                    ProServiceApiHelper.getInstance(getActivity()).getUserLoggedIn(
                            new ProServiceApiHelper.getApiProcessCallback() {
                                @Override
                                public void onStart() {
                                    myLoader.showLoader();
                                }

                                @Override
                                public void onComplete(String message) {
                                    if (myLoader != null && myLoader.isMyLoaderShowing())
                                        myLoader.dismissLoader();

                                    ProApplication.getInstance().setUserEmail(email.getText().toString().trim());


                                    ((PostProjectActivity) getActivity()).first_name = "";
                                    ((PostProjectActivity) getActivity()).last_name = "";
                                    ((PostProjectActivity) getActivity()).email = "";
                                    ((PostProjectActivity) getActivity()).confirm_password = "";
                                    synchronized (new Object()) {
                                        ((PostProjectActivity) getActivity()).completePostProject();
                                    }

                                    dialog.dismiss();
                                }

                                @Override
                                public void onError(String error) {
                                    if (myLoader != null && myLoader.isMyLoaderShowing())
                                        myLoader.dismissLoader();

                                    CustomAlert customAlert = new CustomAlert(getActivity(), "Sign in error", "" + error, PostProjectRegistrationAndFinalizeFragment.this);
                                    customAlert.getListenerRetryCancelFromNormalAlert("retry","abort",1);
                                }
                            },
                            ((EditText)dialog.findViewById(R.id.edt_email)).getText().toString().trim(),
                            ((EditText)dialog.findViewById(R.id.edt_password)).getText().toString().trim());
                }
            }
        });
        dialog.show();
    }

    @Override
    public void callbackForAlert(String result, int i) {
        if (result.equalsIgnoreCase("retry")&&i==1) {
            tv_ok.performClick();
        }
    }
}
