import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.yandex.loginapp.LoginScreenState
import ru.yandex.loginapp.LoginViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private lateinit var viewModel: LoginViewModel
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login with empty fields sets EmptyFieldsError`() = runTest {
        viewModel.login("", "")
        Assert.assertEquals(viewModel.state.value, LoginScreenState.EmptyFieldsError)
    }

    @Test
    fun `login with invalid email sets EmailValidationError`() = runTest {
        viewModel.login("testcat.org", "password")
        Assert.assertEquals(viewModel.state.value, LoginScreenState.EmailValidationError)
    }

    @Test
    fun `login with valid data sets Loading`() = runTest {
        viewModel.login("test@cat.org", "password")
        testDispatcher.scheduler.runCurrent()
        Assert.assertEquals(viewModel.state.value, LoginScreenState.Loading)
    }

    @Test
    fun `login with valid data sets Loading then Success`() = runTest {
        viewModel.login("test@cat.org", "password")
        testDispatcher.scheduler.runCurrent()
        testDispatcher.scheduler.advanceTimeBy(4000)
        Assert.assertEquals(viewModel.state.value, LoginScreenState.Success)
    }
}