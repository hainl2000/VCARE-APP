package com.example.vcare_app.present.sos

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.vcare_app.R
import com.example.vcare_app.databinding.FragmentSOSBinding
import com.example.vcare_app.utilities.CustomInformationDialog
import com.example.vcare_app.utilities.LoadingDialogManager
import com.example.vcare_app.utilities.LoadingStatus
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class SOSFragment : Fragment(), LocationListener {

    companion object {
        fun newInstance() = SOSFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    @SuppressLint("MissingPermission")
    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
                    val locationManager =
                        activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null)
                } else {
                    fusedLocationClient.lastLocation.addOnSuccessListener {
                        viewModel.getNearestHospital(it.latitude, it.longitude)
                    }
                }
            }

            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
                    val locationManager =
                        activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null)
                } else {
                    fusedLocationClient.lastLocation.addOnSuccessListener {
                        viewModel.getNearestHospital(it.latitude, it.longitude)
                    }
                }
            }

            else -> {
                CustomInformationDialog.showCustomInformationDialog(
                    requireContext(),
                    requireContext().resources.getString(R.string.phone_call_permission_denied)
                ) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val uri = Uri.fromParts("package", requireContext().packageName, null)
                    intent.data = uri
                    activity?.startActivity(intent)
                }
            }
        }
    }

    private lateinit var viewModel: SOSViewModel
    private lateinit var binding: FragmentSOSBinding

    private var phoneCallNumber = "119"

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[SOSViewModel::class.java]

        binding = FragmentSOSBinding.inflate(inflater)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.callBtn.setOnClickListener {
            requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
        }

        viewModel.status.observe(viewLifecycleOwner){
            if(it == LoadingStatus.Loading){
                LoadingDialogManager.showDialog(requireContext())
            }else{
                LoadingDialogManager.dismissLoadingDialog()
            }
        }

        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )



        viewModel.nearestHospital.observe(viewLifecycleOwner) {
            binding.nearestHospital = it
            phoneCallNumber = it.hospitalNumber
        }

        return binding.root
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                makePhoneCall(phoneCallNumber)
            } else {
                CustomInformationDialog.showCustomInformationDialog(
                    requireContext(),
                    requireContext().resources.getString(R.string.phone_call_permission_denied)
                ) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val uri = Uri.fromParts("package", requireContext().packageName, null)
                    intent.data = uri
                    activity?.startActivity(intent)
                }
            }
        }

    private fun makePhoneCall(myPhoneNumber: String) {
        // Replace "phoneNumber" with the actual phone number you want to call
        val phoneNumber = "tel:$myPhoneNumber" // Example phone number

        val callIntent = Intent(Intent.ACTION_CALL, Uri.parse(phoneNumber))
        startActivity(callIntent)

    }

    override fun onLocationChanged(location: Location) {
        viewModel.getNearestHospital(location.latitude, location.longitude)
    }

}