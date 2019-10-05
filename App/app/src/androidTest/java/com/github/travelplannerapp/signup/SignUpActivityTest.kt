package com.github.travelplannerapp.signup

import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.communication.model.Response
import com.github.travelplannerapp.communication.model.ResponseCode
import com.github.travelplannerapp.communication.model.SignUpRequest
import com.github.travelplannerapp.signin.SignInActivity
import com.github.travelplannerapp.utils.PasswordUtils
import io.mockk.every
import io.mockk.mockkObject
import io.reactivex.Single
import junit.framework.TestCase.assertEquals
import org.junit.Before

import org.junit.Rule
import org.junit.Test
import java.lang.Exception


class SignUpActivityTest {

    @get:Rule var signInRule = activityScenarioRule<SignInActivity>()

    @Before
    fun setUp() {
        signInRule.scenario.onActivity { activity -> activity.startActivityForResult(
                Intent(activity, SignUpActivity::class.java), SignUpActivity.REQUEST_SIGN_UP) }
    }

    @Test
    fun should_GoBackToSignInScreen_When_SignInButtonWasClicked() {

        // when
        onView(withId(R.id.buttonSignIn)).perform(click())

        //then
        assertEquals(Lifecycle.State.RESUMED, signInRule.scenario.state)
    }

    @Test
    fun should_DisplaySnackBar_When_SignUpButtonWasClickedAndConnectionToServerFailed() {

        // given
        val email = "email"
        val password = "password"
        val hashedPassword = "hashed password"

        mockkObject(PasswordUtils)
        every { PasswordUtils.hashPassword(password) } returns hashedPassword

        val signUpRequest = SignUpRequest(email, hashedPassword)
        mockkObject(CommunicationService)
        every { CommunicationService.serverApi.register(signUpRequest) } returns Single.error(Exception())

        // when
        onView(withId(R.id.editTextEmail)).perform(typeText(email))
        onView(withId(R.id.editTextPassword)).perform(typeText(password))
        onView(withId(R.id.editTextConfirmPassword)).perform(typeText(password), closeSoftKeyboard())
        onView(withId(R.id.buttonSignUp)).perform(click())

        // then
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(R.string.server_connection_error)))
    }

    @Test
    fun should_DisplaySnackBar_When_PasswordsAreDifferentAndSignUpButtonWasClicked() {

        // given
        val password = "password"
        val confirmPassword = "different password"

        // when
        onView(withId(R.id.editTextPassword)).perform(typeText(password))
        onView(withId(R.id.editTextConfirmPassword)).perform(typeText(confirmPassword), closeSoftKeyboard())
        onView(withId(R.id.buttonSignUp)).perform(click())

        // then
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(R.string.sign_up_diff_passwords)))
    }

    @Test
    fun should_DisplaySnackBar_When_EmailIsAlreadyTaken() {

        // given
        val email = "email"
        val password = "password"
        val confirmPassword = "password"
        val hashedPassword = "hashed password"

        mockkObject(PasswordUtils)
        every { PasswordUtils.hashPassword(password) } returns hashedPassword

        val signUpRequest = SignUpRequest(email, hashedPassword)
        mockkObject(CommunicationService)
        every { CommunicationService.serverApi.register(signUpRequest) } returns Single.just(
                Response(ResponseCode.EMAIL_TAKEN_ERROR, Unit))

        // when
        onView(withId(R.id.editTextEmail)).perform(typeText(email))
        onView(withId(R.id.editTextPassword)).perform(typeText(password))
        onView(withId(R.id.editTextConfirmPassword)).perform(typeText(confirmPassword), closeSoftKeyboard())
        onView(withId(R.id.buttonSignUp)).perform(click())

        // then
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(R.string.sign_up_email_error)))
    }

    @Test
    fun should_DisplaySnackBarAndRedirectToSignIn_When_SignUpWasSuccessful() {

        // given
        val email = "email"
        val password = "password"
        val confirmPassword = "password"
        val hashedPassword = "hashed password"

        mockkObject(PasswordUtils)
        every { PasswordUtils.hashPassword(password) } returns hashedPassword

        val signUpRequest = SignUpRequest(email, hashedPassword)
        mockkObject(CommunicationService)
        every { CommunicationService.serverApi.register(signUpRequest) } returns Single.just(
                Response(ResponseCode.OK, Unit))

        // when
        onView(withId(R.id.editTextEmail)).perform(typeText(email))
        onView(withId(R.id.editTextPassword)).perform(typeText(password))
        onView(withId(R.id.editTextConfirmPassword)).perform(typeText(confirmPassword), closeSoftKeyboard())
        onView(withId(R.id.buttonSignUp)).perform(click())

        // then
        assertEquals(Lifecycle.State.RESUMED, signInRule.scenario.state)
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(R.string.sign_up_successful)))
    }

}
