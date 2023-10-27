package com.dicoding.challange4.view

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.dicoding.challange4.MainActivity
import com.dicoding.challange4.R
import com.dicoding.challange4.data.RegisterBody
import com.dicoding.challange4.data.ValidateEmailBody
import com.dicoding.challange4.databinding.ActivityRegisterBinding
import com.dicoding.challange4.repository.AuthRepository
import com.dicoding.challange4.utils.APIService
import com.dicoding.challange4.viewmodel.RegisterActivityViewModel
import com.dicoding.challange4.viewmodel.RegisterActivityViewModelFactory

class RegisterActivity: AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener,
    TextWatcher {

    private lateinit var mBinding: ActivityRegisterBinding
    private lateinit var mViewModel: RegisterActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mBinding.etFullname.onFocusChangeListener = this
        mBinding.etEmail.onFocusChangeListener = this
        mBinding.etPassword.onFocusChangeListener = this
        mBinding.etConfirmPassword.onFocusChangeListener = this
        mBinding.etConfirmPassword.setOnKeyListener(this)
        mBinding.etConfirmPassword.addTextChangedListener(this)
        mBinding.btnRegister.setOnClickListener(this)

        mViewModel = ViewModelProvider(this, RegisterActivityViewModelFactory(AuthRepository(APIService.getService()), application)).get(RegisterActivityViewModel::class.java)
        setupObservers()
    }

    private fun setupObservers() {
        mViewModel.getIsLoading().observe(this){
                mBinding.progressBar.isVisible = it
        }

        mViewModel.getIsUniqueEmail().observe(this) {
            if(validateEmail(shouldUpdateView = false)) {
                if (it) {
                    mBinding.tvEmail.apply {
                        if (isErrorEnabled) isErrorEnabled = false
                        setStartIconDrawable(R.drawable.ic_check)
                        setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                    }
                } else {
                    mBinding.tvEmail.apply {
                        if (startIconDrawable != null) startIconDrawable = null
                        isErrorEnabled = true
                        error = "Email Address is already taken!"
                    }
                }
            }
        }

        mViewModel.getErrorMessage().observe(this) {
            val fromErrorKeys = arrayOf("fullname", "email", "password")
            val message = StringBuilder()
            it.map {
                if (fromErrorKeys.contains(it.key)) {
                    when (it.key) {
                        "fullName" -> {
                            mBinding.tvFullname.apply {
                                isErrorEnabled = true
                                error = it.value
                            }
                        }
                        "email" -> {
                            mBinding.tvEmail.apply {
                                isErrorEnabled = true
                                error = it.value
                            }
                        }
                        "password" -> {
                            mBinding.tvPassword.apply {
                                isErrorEnabled = true
                                error = it.value
                            }
                        }
                    }
                }else {
                    message.append(it.value).append("\n")
                }

                if(message.isNotEmpty()) {
                    AlertDialog.Builder(this)
                        .setIcon(R.drawable.ic_info)
                        .setTitle("Information")
                        .setMessage(message)
                        .setPositiveButton("OK") { dialog, _ -> dialog!!.dismiss()}
                        .show()

                }
            }
        }

        mViewModel.getUser().observe(this) {
            if(it != null) {
                startActivity(Intent(this, MainActivity::class.java))
        }
        }
    }

    private fun valideteFullName(): Boolean {
        var errorMessage: String? = null
        val value: String = mBinding.etFullname.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Fullname is required!"
        }

        if (errorMessage != null) {
            mBinding.tvFullname.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    private fun validateEmail(shouldUpdateView: Boolean = true): Boolean {
        var errorMessage: String? = null
        val value = mBinding.etEmail.text.toString()
        if(value.isEmpty()) {
            errorMessage = "Email is required!"
        } else if (Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            errorMessage = "Email Address is invalid!"
        }

        if (errorMessage != null && shouldUpdateView) {
            mBinding.tvEmail.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }

        return errorMessage == null
    }

    private fun validatePassword(shouldUpdateView: Boolean = true): Boolean {
        var errorMessage: String? = null
        val value = mBinding.etPassword.text.toString()
        if(value.isEmpty()) {
            errorMessage = "Password is required!"
        } else if (value.length < 6) {
            errorMessage = "Password must be at least 6 characters!"
        }

        if (errorMessage != null && shouldUpdateView) {
            mBinding.tvPassword.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    private fun validateConfirmPassword(shouldUpdateView: Boolean = true): Boolean {
        var errorMessage: String? = null
        val value = mBinding.etConfirmPassword.text.toString()
        if(value.isEmpty()) {
            errorMessage = "Confirm Password is required!"
        } else if (value != mBinding.etPassword.text.toString()) {
            errorMessage = "Confirm Password must be same with Password!"
        }

        if (errorMessage != null && shouldUpdateView) {
            mBinding.tvConfirmPassword.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    private fun validatePasswordAndConfirmPassword(shouldUpdateView: Boolean = true): Boolean {
        var errorMessage: String? = null
        val value = mBinding.etPassword.text.toString()
        val value2 = mBinding.etConfirmPassword.text.toString()
        if(value.isEmpty() && value2.isEmpty()) {
            errorMessage = "Password and Confirm Password is required!"
        } else if (value != value2) {
            errorMessage = "Password and Confirm Password must be same!"
        }

        if (errorMessage != null && shouldUpdateView) {
            mBinding.tvConfirmPassword.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    override fun onClick(view: View?) {
        if(view != null && view.id == R.id.btn_register)
            onSubmit()
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if(view != null) {
            when(view.id) {
                R.id.et_fullname -> {
                    if(hasFocus) {
                        if (mBinding.tvFullname.isErrorEnabled) {
                            mBinding.tvFullname.isErrorEnabled = false
                        }
                    }else {
                        valideteFullName()
                    }
                }
                R.id.et_email -> {
                    if(hasFocus) {
                        if (mBinding.tvEmail.isErrorEnabled) {
                            mBinding.tvEmail.isErrorEnabled = false
                        }
                    }else {
                        if (validateEmail()) {
                            mViewModel.validateEmailAddress(ValidateEmailBody(mBinding.etEmail.text!!.toString()))
                        }
                    }
                }
                R.id.et_password -> {
                    if(!hasFocus) {
                        if (validatePassword() && mBinding.etConfirmPassword.text!!.isNotEmpty() && validateConfirmPassword() && validatePasswordAndConfirmPassword()) {
                            if (mBinding.tvConfirmPassword.isErrorEnabled) {
                                mBinding.tvConfirmPassword.isErrorEnabled = false
                            }
                            mBinding.tvConfirmPassword.apply {
                                setStartIconDrawable(R.drawable.ic_check)
                                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                            }
                        }
                    }else {
                        if (mBinding.tvPassword.isErrorEnabled) {
                            mBinding.tvPassword.isErrorEnabled = false
                        }
                    }
                }
                R.id.et_confirmPassword -> {
                    if(!hasFocus) {
                        if(validateConfirmPassword() && validatePassword() && validatePasswordAndConfirmPassword()) {
                            if(mBinding.tvPassword.isErrorEnabled) {
                                mBinding.tvPassword.isErrorEnabled = false
                            }
                            mBinding.tvConfirmPassword.apply {
                                setStartIconDrawable(R.drawable.ic_check)
                                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                            }
                        }
                    }else {
                        if (mBinding.tvConfirmPassword.isErrorEnabled) {
                            mBinding.tvConfirmPassword.isErrorEnabled = false
                        }
                    }
                }
            }
        }
    }

    override fun onKey(view: View?, keyCode: Int, keyEvent: KeyEvent?): Boolean {
        if(KeyEvent.KEYCODE_ENTER == keyCode && keyEvent!!.action == KeyEvent.ACTION_UP){
            onSubmit()
        }
        return false
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if(validatePassword(shouldUpdateView = false) && validateConfirmPassword(shouldUpdateView = false) && validatePasswordAndConfirmPassword(shouldUpdateView = false)) {
            mBinding.tvConfirmPassword.apply {
                if(isErrorEnabled) isErrorEnabled = false
                setStartIconDrawable(R.drawable.ic_check)
                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
            }
        }else{
            if(mBinding.tvConfirmPassword.startIconDrawable != null)
                mBinding.tvConfirmPassword.startIconDrawable = null
        }
    }

    override fun afterTextChanged(s: Editable?) {}

    private fun onSubmit() {
        if(validate()) {
            mViewModel.registerUser(RegisterBody(mBinding.etFullname.text!!.toString(), mBinding.etEmail.text!!.toString(), mBinding.etPassword.text!!.toString()))
        }
    }

    private fun validate(): Boolean {
        var isValid = true

        if(!valideteFullName()) isValid = false
        if(!validateEmail()) isValid = false
        if(!validatePassword()) isValid = false
        if(!validateConfirmPassword()) isValid = false
        if(isValid && !validatePasswordAndConfirmPassword()) isValid = false

        return isValid
    }

}