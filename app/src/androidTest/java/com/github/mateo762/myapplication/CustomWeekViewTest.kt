import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import com.alamkanak.weekview.WeekView
import com.github.mateo762.myapplication.habits.fragments.week.CustomWeekView
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.`when`

class CustomWeekViewTest {

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockResources: Resources


    @Mock
    private lateinit var mockConfiguration: Configuration

    @Mock
    private lateinit var mockAttributeSet: AttributeSet

    private lateinit var customWeekView: CustomWeekView

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        `when`(WeekView(any(), any())).thenReturn(null)
        `when`(mockContext.theme.obtainStyledAttributes(any(), any(), anyInt(), anyInt())).thenReturn(null)
//        `when`(mockContext.resources).thenReturn(mockResources)
//        `when`(mockResources.displayMetrics).thenReturn(mock(DisplayMetrics::class.java))
//        `when`(mockConfiguration.isLayoutSizeAtLeast(anyInt())).thenReturn(true)

        customWeekView = CustomWeekView(mockContext, mockAttributeSet)
    }

    @Test
    fun onTouchEvent_downAction_setsStartX() {
        // Simulate MotionEvent.ACTION_DOWN event
        val mockEvent = mock(MotionEvent::class.java)
        `when`(mockEvent.action).thenReturn(MotionEvent.ACTION_DOWN)
        `when`(mockEvent.x).thenReturn(10f)

        customWeekView.onTouchEvent(mockEvent)
    }

    @Test
    fun onTouchEvent_moveAction_ignoreHorizontalScrolling() {
        // Simulate MotionEvent.ACTION_MOVE event
        val mockEvent = mock(MotionEvent::class.java)
        `when`(mockEvent.action).thenReturn(MotionEvent.ACTION_MOVE)
        `when`(mockEvent.x).thenReturn(30f)

        val result = customWeekView.onTouchEvent(mockEvent)

        // Verify that horizontal scrolling is ignored and returns false
        assertEquals(false, result)
    }

    @Test
    fun onTouchEvent_otherActions_delegateToSuper() {
        // Simulate MotionEvent.ACTION_UP event
        val mockEvent = mock(MotionEvent::class.java)
        `when`(mockEvent.action).thenReturn(MotionEvent.ACTION_UP)

        val result = customWeekView.onTouchEvent(mockEvent)

        // Verify that other actions delegate to super and return true
        assertEquals(true, result)
    }
}
