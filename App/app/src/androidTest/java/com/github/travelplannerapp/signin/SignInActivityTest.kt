package com.github.travelplannerapp.signin

import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import com.github.travelplannerapp.R
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.utils.PasswordUtils
import io.mockk.every
import io.mockk.mockkObject
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.platform.app.InstrumentationRegistry
import com.github.travelplannerapp.communication.model.*
import com.github.travelplannerapp.travels.TravelsActivity
import com.github.travelplannerapp.utils.SharedPreferencesUtils
import junit.framework.TestCase.assertEquals
import android.content.ComponentName
import androidx.test.espresso.intent.Intents
import org.junit.After
import org.junit.Before


class SignInActivityTest {

    @get:Rule
    var signInRule = activityScenarioRule<SignInActivity>()

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearUp() {
        Intents.release()
    }

    @Test
    fun should_LaunchSignUpActivity_When_SignUpButtonWasClicked() {

        // when
        onView(withId(R.id.buttonSignUp)).perform(click())

        //then
        assertEquals(Lifecycle.State.STARTED, signInRule.scenario.state)
    }

    @Test
    fun should_DisplaySnackBar_When_SignInButtonWasClickedAndConnectionToServerFailed() {

        // given
        val email = "email"
        val password = "password"
        val hashedPassword = "hashed password"

        mockkObject(PasswordUtils)
        every { PasswordUtils.hashPassword(password) } returns hashedPassword

        val signInRequest = SignInRequest(email, hashedPassword)
        mockkObject(CommunicationService)
        every { CommunicationService.serverApi.authenticate(signInRequest) } returns Single.error(Exception())

        // when
        onView(withId(R.id.editTextEmail)).perform(ViewActions.typeText(email))
        onView(withId(R.id.editTextPassword)).perform(ViewActions.typeText(password), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.buttonSignIn)).perform(click())

        // then
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(R.string.server_connection_error)))
    }

    @Test
    fun should_DisplaySnackBar_When_WrongCredentialsWereGivenAndSignInButtonWasClicked() {

        // given
        val email = "email"
        val password = "password"
        val hashedPassword = "hashed password"

        mockkObject(PasswordUtils)
        every { PasswordUtils.hashPassword(password) } returns hashedPassword

        val signInRequest = SignInRequest(email, hashedPassword)
        val signInResponse: SignInResponse? = null
        mockkObject(CommunicationService)
        every { CommunicationService.serverApi.authenticate(signInRequest) } returns Single.just(
                Response(ResponseCode.AUTHENTICATION_ERROR, signInResponse))

        // when
        onView(withId(R.id.editTextEmail)).perform(ViewActions.typeText(email))
        onView(withId(R.id.editTextPassword)).perform(ViewActions.typeText(password), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.buttonSignIn)).perform(click())

        // then
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(R.string.sign_in_error)))
    }

    @Test
    fun should_LaunchTravelsActivity_When_CorrectCredentialsWereGivenAndSignInButtonWasClicked() {

        // given
        val email = "email"
        val password = "password"
        val hashedPassword = "hashed password"
        val token = "token"
        val userId = 1

        mockkObject(PasswordUtils)
        every { PasswordUtils.hashPassword(password) } returns hashedPassword

        val signInRequest = SignInRequest(email, hashedPassword)
        mockkObject(CommunicationService)
        every { CommunicationService.serverApi.authenticate(signInRequest) } returns Single.just(
                Response(ResponseCode.OK, SignInResponse(token, userId)))
        every { CommunicationService.serverApi.getTravels(userId) } returns Single.just(
                Response(ResponseCode.OK, emptyList()))

        SharedPreferencesUtils.initialize(InstrumentationRegistry.getInstrumentation().targetContext)

        // when
        onView(withId(R.id.editTextEmail)).perform(ViewActions.typeText(email))
        onView(withId(R.id.editTextPassword)).perform(ViewActions.typeText(password), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.buttonSignIn)).perform(click())

        // then
        assertEquals(SharedPreferencesUtils.getUserId(), userId)
        assertEquals(SharedPreferencesUtils.getAccessToken(), token)
        assertEquals(SharedPreferencesUtils.getEmail(), email)

        intended(hasComponent(ComponentName(InstrumentationRegistry.getInstrumentation().targetContext, TravelsActivity::class.java)))
    }

}
