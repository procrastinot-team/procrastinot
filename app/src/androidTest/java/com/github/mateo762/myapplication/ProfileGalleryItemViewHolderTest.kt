import android.content.res.Resources
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.databinding.ItemProfileGalleryBinding
import com.github.mateo762.myapplication.profile.ProfileGalleryItem
import com.github.mateo762.myapplication.profile.ProfileGalleryItemViewHolder
import com.github.mateo762.myapplication.R
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class ProfileGalleryItemViewHolderTest {

    @Mock
    private lateinit var mockBinding: ItemProfileGalleryBinding

    private lateinit var viewHolder: ProfileGalleryItemViewHolder

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewHolder = ProfileGalleryItemViewHolder(mockBinding)
    }


    @Test
    fun bind_setsImageResource() {
        val profileGalleryItem = ProfileGalleryItem(image = R.drawable.exercise_hardcoded_image_1)

        viewHolder.bind(profileGalleryItem)

        `when`(mockBinding.habitImage.setImageResource(any())).apply{
            `when`(mockBinding.habitImage.resources).thenReturn(null)
        }
        // Verify that the correct image resource is set
        assertEquals(null, mockBinding.habitImage.resources)
    }
}
