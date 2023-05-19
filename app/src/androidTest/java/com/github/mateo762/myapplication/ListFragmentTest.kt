import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.test.core.app.ApplicationProvider
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.habits.fragments.ListFragment
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class ListFragmentTest {

    private lateinit var fragment: ListFragment

    @Before
    fun setup() {
        fragment = ListFragment()
    }

    @Test
    fun testFragmentCreation() {
        val param1 = "param1"
        val param2 = "param2"
        val bundle = Bundle().apply {
            putString("ARG_PARAM1", param1)
            putString("ARG_PARAM2", param2)
        }
        fragment.arguments = bundle

        val view = fragment.onCreateView(
            LayoutInflater.from(ApplicationProvider.getApplicationContext()),
            null,
            null
        )

        assertNotNull(view)
    }
}
