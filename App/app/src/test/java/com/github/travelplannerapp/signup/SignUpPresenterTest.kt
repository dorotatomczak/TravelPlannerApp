package com.github.travelplannerapp.signup

import com.github.travelplannerapp.R
import com.github.travelplannerapp.RxImmediateSchedulerRule
import com.github.travelplannerapp.communication.ApiException
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.communication.commonmodel.Response
import com.github.travelplannerapp.communication.commonmodel.ResponseCode
import com.github.travelplannerapp.communication.commonmodel.SignUpRequest
import com.github.travelplannerapp.utils.PasswordUtils
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception

class SignUpPresenterTest {

    @MockK(relaxUnitFun = true)
    private lateinit var view: SignUpContract.View

    private lateinit var presenter: SignUpPresenter

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        presenter = spyk(SignUpPresenter(view), recordPrivateCalls = true)
    }

    @Test
    fun `Should show sign in when sign in was clicked`() {

        // when
        presenter.onSignInClicked()

        // then
        verify { view.showSignIn() }
    }

    @Test
    fun `Should display snackBar with info message when given passwords in SignUp are different`() {

        // given
        val email = "email"
        val password = "password"
        val confirmPassword = "different password"

        // when
        presenter.onSignUpClicked(email, password, confirmPassword)

        // then
        verify { view.showSnackbar(R.string.sign_up_diff_passwords) }
    }

    @Test
    fun `Should display snackBar with info message when hash of the given password is null`() {

        // given
        val email = "email"
        val password = "password"
        val confirmPassword = "password"
        mockkObject(PasswordUtils)
        every { PasswordUtils.hashPassword(password) } returns null

        // when
        presenter.onSignUpClicked(email, password, confirmPassword)

        // then
        verify { view.showSnackbar(R.string.try_again) }
    }

    @Test
    fun `Should send request to server when passwords are the same and hashed password is not null`() {

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
        presenter.onSignUpClicked(email, password, confirmPassword)

        // then
        verify { CommunicationService.serverApi.register(signUpRequest) }
    }

    @Test
    fun `Should return result and finish when server returns 200 on sign up`() {

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
        presenter.onSignUpClicked(email, password, confirmPassword)

        // then
        verifyOrder {
            presenter["handleSignUpResponse"]()
            view.returnResultAndFinish(R.string.sign_up_successful)
        }
    }

    @Test
    fun `Should show snackBar with error message when server returns error code on sign up`() {

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
        presenter.onSignUpClicked(email, password, confirmPassword)

        // then
        verifyOrder {
            presenter["handleErrorResponse"](any<ApiException>())
            view.showSnackbar(ApiException(ResponseCode.EMAIL_TAKEN_ERROR).getErrorMessageCode())
        }
    }

    @Test
    fun `Should show snackBar with error message when server connection failed on sign up`() {

        // given
        val email = "email"
        val password = "password"
        val confirmPassword = "password"
        val hashedPassword = "hashed password"

        mockkObject(PasswordUtils)
        every { PasswordUtils.hashPassword(password) } returns hashedPassword

        val signUpRequest = SignUpRequest(email, hashedPassword)
        mockkObject(CommunicationService)
        every { CommunicationService.serverApi.register(signUpRequest) } returns Single.error(Exception())

        // when
        presenter.onSignUpClicked(email, password, confirmPassword)

        // then
        verifyOrder {
            presenter["handleErrorResponse"](any<Throwable>())
            view.showSnackbar(R.string.server_connection_error)
        }
    }

}
