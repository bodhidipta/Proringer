package com.android.llc.proringer.fragments.postProject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.llc.proringer.R;
import com.android.llc.proringer.activities.PostProjectActivity;
import com.android.llc.proringer.activities.PostedFinishActivity;
import com.android.llc.proringer.activities.SignUpActivity;
import com.android.llc.proringer.activities.TermsPrivacyActivity;
import com.android.llc.proringer.appconstant.ProApplication;
import com.android.llc.proringer.utils.Logger;
import com.android.llc.proringer.viewsmod.edittext.ProLightEditText;
import com.android.llc.proringer.viewsmod.textview.ProRegularTextView;

/**
 * Created by su on 7/13/17.
 */

public class PostProjectRegistrationAndFinalizeFragment extends Fragment {
    private ProLightEditText first_name, last_name, email, password, confirm_password, zip_code;
    private LinearLayout content_post_form_submit;

    ProRegularTextView terms_and_policy;



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

        zip_code = (ProLightEditText) view.findViewById(R.id.zip_code);
        zip_code.setEnabled(false);
        zip_code.setClickable(false);

        content_post_form_submit = (LinearLayout) view.findViewById(R.id.content_post_form_submit);



        terms_and_policy = (ProRegularTextView) view.findViewById(R.id.terms_and_policy);
        terms_and_policy.setMovementMethod(LinkMovementMethod.getInstance());
        /**
         * Contact us spannable text with click listener
         */
        String TextOne = "By Signing in up with ProRinger you agree with our  \n";
        String TextTermsClick = "Terms of Use ";
        String TextTwo = "and ";
        String TextPolicyClick = "Privacy Policy";



        Spannable word1 = new SpannableString(TextOne);
        word1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorTextDark)), 0, TextOne.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
                ds.setColor(getResources().getColor(R.color.colorAccent));
                ds.setUnderlineText(false);
            }
        };
        word2.setSpan(myClickableTermsSpan, 0, TextTermsClick.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        terms_and_policy.append(word2);


        Spannable word3 = new SpannableString(TextTwo);
        word3.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorTextDark)), 0, TextTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
                ds.setColor(getResources().getColor(R.color.colorAccent));
                ds.setUnderlineText(false);
            }
        };
        word4.setSpan(myClickablePolicySpan, 0, TextPolicyClick.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        terms_and_policy.append(word4);







        if (ProApplication.getInstance().getUserId().equals("")) {
/**
 * IF no user is login then visible registration process or visible last part
 */
            view.findViewById(R.id.container_registration).setVisibility(View.VISIBLE);
            zip_code.setText(((PostProjectActivity)getActivity()).selectedAddressData.getZip_code());

        } else {
            view.findViewById(R.id.content_post_form_submit).setVisibility(View.VISIBLE);
        }

        view.findViewById(R.id.close_project).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ProApplication.getInstance().getUserId().equals("")) {
                    startActivity(new Intent((PostProjectActivity) getActivity(), PostedFinishActivity.class));
                    ((PostProjectActivity) getActivity()).finish();
                } else {
                    ((PostProjectActivity) getActivity()).finish();
                }

            }
        });

        view.findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
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
                                        if (zip_code.getText().toString().trim().equals("")) {
                                            zip_code.setError("Please enter Zip.");
                                            return false;
                                        } else {
                                            return true;
                                        }
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
}
