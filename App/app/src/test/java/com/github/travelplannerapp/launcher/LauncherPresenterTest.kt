package com.github.travelplannerapp.launcher

import com.github.travelplannerapp.RxImmediateSchedulerRule
import com.github.travelplannerapp.communication.CommunicationService
import com.github.travelplannerapp.communication.commonmodel.Response
import com.github.travelplannerapp.communication.commonmodel.ResponseCode
import com.github.travelplannerapp.utils.SharedPreferencesUtils
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.Single
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
class LauncherPresenterTest {

    @MockK(relaxUnitFun = true)
    private lateinit var view: LauncherContract.View

    private lateinit var presenter: LauncherPresenter

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        presenter = spyk(LauncherPresenter(view), recordPrivateCalls = true)
    }

    @Test
    @Parameters(value = [
            ",1,email",
            "token,-1,email",
            "token,1,"])
    fun `Should show sign in when user is not signed in`(token: String, userId: Int, email: String) {

        // given
        val credentials = SharedPreferencesUtils.Credentials(token, userId, email)

        // when
        presenter.redirect(credentials)

        // then
        verify { view.showSignIn() }
    }

    @Test
    fun `Should show sign in when token is expired`() {

        // given
        val token = "i_am_expired"
        val userId = 1
        val email = "email"

        mockkObject(SharedPreferencesUtils)
        val credentials = SharedPreferencesUtils.Credentials(token, userId, email)
        every { SharedPreferencesUtils.getCredentials() } returns credentials

        mockkObject(CommunicationService)
        every { CommunicationService.serverApi.authorize() } returns Single.just(
                Response(ResponseCode.AUTHORIZATION_ERROR, Unit))

        // when
        presenter.redirect(credentials)

        // then
        verify { view.showSignIn() }
    }

    @Test
    fun `Should show travels when user is signed in`() {

        // given
        val token = "token"
        val userId = 1
        val email = "email"

        mockkObject(SharedPreferencesUtils)
        val credentials = SharedPreferencesUtils.Credentials(token, userId, email)
        every { SharedPreferencesUtils.getCredentials() } returns credentials

        mockkObject(CommunicationService)
        every { CommunicationService.serverApi.authorize() } returns Single.just(
                Response(ResponseCode.OK, Unit))

        // when
        presenter.redirect(credentials)

        // then
        verify { view.showTravels() }
    }
}
