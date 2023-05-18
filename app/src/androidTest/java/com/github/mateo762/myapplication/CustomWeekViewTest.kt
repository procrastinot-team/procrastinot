import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.test.core.app.ApplicationProvider
import com.alamkanak.weekview.WeekView
import com.github.mateo762.myapplication.habits.fragments.week.CustomWeekView
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class CustomWeekViewTest {

    private lateinit var customWeekView: CustomWeekView

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val attrs = mock(AttributeSet::class.java)
        customWeekView = CustomWeekView(context, attrs)
    }

    @Test
    fun onTouchEvent_moveAction_ignoreHorizontalScrolling() {
        val distanceX = 20f
        val event = MotionEvent.obtain(0L, 0L, MotionEvent.ACTION_MOVE, distanceX, 0f, 0)
        val result = customWeekView.onTouchEvent(event)

        assertEquals(false, result)
    }

    @Test
    fun onTouchEvent_otherActions_delegateToSuper() {
        val event = MotionEvent.obtain(0L, 0L, MotionEvent.ACTION_UP, 0f, 0f, 0)
        val result = customWeekView.onTouchEvent(event)

        assertEquals(true, result)
    }
}