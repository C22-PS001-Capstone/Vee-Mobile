package id.vee.android.ui.activity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import id.vee.android.R
import id.vee.android.databinding.FragmentAddActivityBinding
import id.vee.android.domain.model.Token
import id.vee.android.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class AddActivityFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentAddActivityBinding? = null
    private val binding get() = _binding

    private var userToken: Token? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var currentLocation: Location? = null
    private val viewModel: ActivityViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddActivityBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.title_add_activity)
        setupBackButton()
        return binding?.root
    }

    private fun setupBackButton() {
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentTime: Date = Calendar.getInstance().time
        val formattedDate =
            SimpleDateFormat(getString(R.string.date_format), Locale.getDefault()).format(
                currentTime
            )
        Log.d(TAG, "onViewCreated: $formattedDate")

        context?.apply {
            viewModelListener(this)
            viewModel.getToken()
            binding?.apply {
                btnDpd.setOnClickListener(this@AddActivityFragment)
                edtDate.setText(formattedDate)
                btnAddActivity.setOnClickListener {
                    if (!checkEmptyEditText(edtDate) && !checkEmptyEditText(edtDistance) && !checkEmptyEditText(
                            edtLitre
                        ) && !checkEmptyEditText(edtExpense)
                    ) {
                        return@setOnClickListener
                    }
                    insertActivity(viewModel)
                }
                btnContinue.setOnClickListener {
                    available.visibility = View.VISIBLE
                    notAvailable.visibility = View.GONE
                }
            }
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            getMyLastLocation(this)
        }
    }

    private fun viewModelListener(context: Context) {
        viewModel.tokenResponse.observe(viewLifecycleOwner) { tokenData ->
            userToken = tokenData
        }
        viewModel.actionResponse.observe(viewLifecycleOwner) { response ->
            if (response.status == "success") {
                AlertDialog.Builder(context)
                    .setTitle(getString(R.string.success))
                    .setMessage(getString(R.string.success_add_activity))
                    .setPositiveButton(getString(R.string.positive_dialog_btn_text)) { dialog, _ ->
                        dialog.dismiss()
                        Navigation.createNavigateOnClickListener(R.id.action_navigation_add_activity_to_navigation_home)
                    }
                    .show()
            } else {
                AlertDialog.Builder(context)
                    .setTitle(getString(R.string.error))
                    .setMessage(getString(R.string.error_add_activity) + response.message)
                    .setPositiveButton(getString(R.string.positive_dialog_btn_text)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }

    private fun insertActivity(viewModel: ActivityViewModel) {
        var lat = 0.0
        var lon = 0.0
        currentLocation?.apply {
            lat = latitude
            lon = longitude
        }
        val initDate = SimpleDateFormat(
            getString(R.string.date_format),
            Locale.getDefault()
        ).parse(binding?.edtDate?.text.toString())
        val formatter = initDate?.let {
            SimpleDateFormat(getString(R.string.date_format_reversed), Locale.getDefault()).format(
                it
            )
        }
        binding?.apply {
            userToken?.let {
                checkTokenAvailability(viewModel, it, viewLifecycleOwner) {
                    viewModel.insertActivity(
                        it.accessToken,
                        formatter.toString(),
                        edtDistance.text.toString().trimDottedString().toInt(),
                        edtLitre.text.toString().trimDottedString().toInt(),
                        edtExpense.text.toString().trimDottedString().toInt(),
                        lat,
                        lon
                    )
                }
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    context?.let { getMyLastLocation(it) }
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    context?.let { getMyLastLocation(it) }
                }
                else -> {
                    // No location access granted.
                }
            }
        }

    private fun checkPermission(permission: String, context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation(context: Context) {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, context) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, context)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    currentLocation = location
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.location_not_found),
                        Toast.LENGTH_SHORT
                    ).show()
                    showLocationNotAvailable()
                }
            }.addOnFailureListener {
                Log.d(TAG, "getMyLastLocation: $it")
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun showLocationNotAvailable() {
        binding?.apply {
            notAvailable.visibility = View.VISIBLE
            available.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_dpd -> {
                activity?.takeIf { !it.isFinishing && !it.isDestroyed }?.let { activity ->
                    MyDatePickerDialog(activity, ::showDate).show()
                }
            }
        }
    }

    private fun showDate(year: Int, month: Int, day: Int) {
        Log.d("Picked Date", "$year-$month-$day")
        val formatedDate = "${day.padStart(2)}-${(month + 1).padStart(2)}-$year"
        // Implement to text view
        binding?.apply {
            edtDate.setText(formatedDate)
        }
    }

    companion object {
        private const val TAG = "AddActivityFragment"
    }
}