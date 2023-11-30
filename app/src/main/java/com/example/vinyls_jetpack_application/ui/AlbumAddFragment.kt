package com.example.vinyls_jetpack_application.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.vinyls_jetpack_application.R
import com.example.vinyls_jetpack_application.database.dao.AppDatabase
import com.example.vinyls_jetpack_application.databinding.AlbumAddDataFragmentBinding
import com.example.vinyls_jetpack_application.viewmodels.AlbumViewModel
import java.util.Calendar

class AlbumAddFragment:Fragment() {

    private var _binding: AlbumAddDataFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AlbumViewModel

    private lateinit var datePickerDialog: DatePickerDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AlbumAddDataFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val buttonAddAlbum = binding.btnAddAlbum

        val editReleaseDate = binding.editTextReleaseDate

        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }

        val database = AppDatabase.getDatabase(activity.application)
        val albumsDao = database.albumDao()

        viewModel = ViewModelProvider(this, AlbumViewModel.Factory(activity.application, albumsDao)).get(AlbumViewModel::class.java)

        // Obtener la fecha actual
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        datePickerDialog = DatePickerDialog(
            requireActivity(), // Cambio aquí: Utilizar requireActivity() en lugar de this
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                editReleaseDate.setText(selectedDate)
            },
            year,
            month,
            day
        )

        editReleaseDate.isFocusable = false
        editReleaseDate.isClickable = true

        viewModel.eventAlbumCreated.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(context, "Album creado", Toast.LENGTH_SHORT).show()

                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    val navController = findNavController()

                    // Navega al fragmento deseado después del retraso
                    navController.navigate(R.id.albumFragment)
                }, 2000)
            }
        }

        editReleaseDate.setOnClickListener {
            datePickerDialog.show()
        }

        buttonAddAlbum.setOnClickListener {
            addAlbum()
        }

        viewModel.eventNetworkError.observe(viewLifecycleOwner) {
            if (it) onNetworkError()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addAlbum() {
        val textAlbumName = binding.editTextAlbumName.text.toString()
        val textAlbumCover = binding.editTextAlbumCover.text.toString()
        val textAlbumReleaseDate = binding.editTextReleaseDate.text.toString()
        val textAlbumDescription = binding.editTextDescription.text.toString()
        val textAlbumGenre = binding.spinnerGenre.selectedItem.toString()
        val textAlbumRecordLabel = binding.spinnerRecordLabel.selectedItem.toString()


        viewModel.addAlbum(
            textAlbumName,
            textAlbumCover,
            textAlbumReleaseDate,
            textAlbumDescription,
            textAlbumGenre,
            textAlbumRecordLabel
        )
    }

    private fun onNetworkError() {
        if(!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }

}