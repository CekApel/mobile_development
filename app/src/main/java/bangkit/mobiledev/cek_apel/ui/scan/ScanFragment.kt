package bangkit.mobiledev.cek_apel.ui.scan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import bangkit.mobiledev.cek_apel.R
import bangkit.mobiledev.cek_apel.databinding.FragmentScanBinding
import bangkit.mobiledev.cek_apel.utils.getImageUri
import bangkit.mobiledev.cek_apel.utils.reduceFileImage
import bangkit.mobiledev.cek_apel.utils.uriToFile

class ScanFragment : Fragment() {

    private lateinit var binding: FragmentScanBinding
    private val scanViewModel: ScanViewModel by viewModels()
    private var currentImageUri: Uri? = null

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Toast.makeText(requireContext(), getString(R.string.img_cant_found), Toast.LENGTH_SHORT).show()
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess && currentImageUri != null) {
            showImage()
        } else {
            Toast.makeText(requireContext(), getString(R.string.img_cant_found), Toast.LENGTH_SHORT).show()
            currentImageUri = null
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            Toast.makeText(requireContext(), getString(R.string.camera_permission), Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentScanBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
//        setHasOptionsMenu(true)

        currentImageUri = savedInstanceState?.getParcelable(STATE_IMAGE_URI)

        if (currentImageUri != null) {
            showImage()
        }

        checkAndRequestPermission()

        binding.btnGaleri.setOnClickListener { startGallery() }
        binding.btnKamera.setOnClickListener { startCamera() }
        binding.btnCekApel.setOnClickListener { analyzeImage() }

        return binding.root
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        if (currentImageUri == null) {
            Toast.makeText(requireContext(), getString(R.string.uri_photo_fail), Toast.LENGTH_SHORT).show()
            return
        }
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.placeholderImage.setImageURI(it)
        } ?: run {
            Toast.makeText(requireContext(), getString(R.string.img_cant_found), Toast.LENGTH_SHORT).show()
        }
    }

    private fun analyzeImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, requireContext())
            if (imageFile.exists()) {
                moveToResult(uri)
            } else {
                Toast.makeText(requireContext(), getString(R.string.img_empty), Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(requireContext(), getString(R.string.img_empty), Toast.LENGTH_SHORT).show()
        }
    }

    private fun moveToResult(uri: Uri) {
        val intent = Intent(requireContext(), HasilScanActivity::class.java).apply {
            putExtra(HasilScanActivity.KEY_IMG_URI, uri.toString())
        }
        startActivity(intent)
    }

    private fun checkAndRequestPermission() {
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(requireContext(), REQUIRED_PERMISSION) == PackageManager.PERMISSION_GRANTED

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(STATE_IMAGE_URI, currentImageUri)
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//        inflater.inflate(R.menu.menu_scan, menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.action_history -> {
//                navigateToHistory()
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
//
//    private fun navigateToHistory() {
//        val intent = Intent(requireContext(), HistoryScanActivity::class.java)
//        startActivity(intent)
//    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
        private const val STATE_IMAGE_URI = "state_image_uri"
    }
}
