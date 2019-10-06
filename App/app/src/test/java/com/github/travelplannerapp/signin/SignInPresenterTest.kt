package com.github.travelplannerapp.signin

import com.github.travelplannerapp.R
import com.github.travelplannerapp.RxImmediateSchedulerRule
import com.github.travelplannerapp.communication.ApiException
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.communication.commonmodel.Response
import com.github.travelplannerapp.communication.commonmodel.ResponseCode
import com.github.travelplannerapp.communication.commonmodel.SignInRequest
import com.github.travelplannerapp.communication.commonmodel.SignInResponse
import com.github.travelplannerapp.utils.PasswordUtils
import com.github.travelplannerapp.utils.SharedPreferencesUtils
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception

class SignInPresenterTest {

    @MockK(relaxUnitFun = true)
    private lateinit var view: SignInContract.View

    private lateinit var presenter: SignInPresenter

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        presenter = spyk(SignInPresenter(view), recordPrivateCalls = true)
    }

    @Test
    fun `Should show sign up when sign up was clicked`() {

        // when
        presenter.onSignUpClicked()

        // then
        verify { view.showSignUp() }
    }

    @Test
    fun `Should display snackBar with info message when hash of the given password is null`() {

        // given
        val email = "email"
        val password = "password"
        mockkObject(PasswordUtils)
        every { PasswordUtils.hashPassword(password) } returns null

        // when
        presenter.onSignInClicked(email, password)

        // then
        verify { view.showSnackbar(R.string.try_again) }
    }

    @Test
    fun `Should send request to server when passwords are the same and hashed password is not null`() {

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

        // when
        presenter.onSignInClicked(email, password)

        // then
        verify { CommunicationService.serverApi.authenticate(signInRequest) }
    }

    @Test
    fun `Should successfully sign in when server returns 200 on sign in`() {

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

        // when
        presenter.onSignInClicked(email, password)

        // then
        verifyOrder {
            presenter["handleSignInResponse"](SignInResponse(token, userId))
            view.signIn(SharedPreferencesUtils.Credentials(token, userId, email))
        }
    }

    @Test
    fun `Should show snackBar with error message when server returns error code on sign in`() {

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
        presenter.onSignInClicked(email, password)

        // then
        verifyOrder {
            presenter["handleErrorResponse"](any<ApiException>())
            view.showSnackbar(ApiException(ResponseCode.AUTHENTICATION_ERROR).getErrorMessageCode())
        }
    }

    @Test
    fun `Should show snackBar with error message when server connection failed on sign up`() {

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
        presenter.onSignInClicked(email, password)

        // then
        verifyOrder {
            presenter["handleErrorResponse"](any<Throwable>())
            view.showSnackbar(R.string.server_connection_error)
        }
    }

}
