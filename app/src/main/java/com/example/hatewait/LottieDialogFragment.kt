
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.example.hatewait.R

class LottieDialogFragment : DialogFragment() {
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.loading_lottie, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme)
    }


    companion object {
        var fragment:LottieDialogFragment? = null
        fun newInstance(): LottieDialogFragment {
            if(fragment == null){
                val args = Bundle()
                fragment = LottieDialogFragment()
                fragment!!.arguments = args
            }
            return fragment as LottieDialogFragment
        }
    }

}