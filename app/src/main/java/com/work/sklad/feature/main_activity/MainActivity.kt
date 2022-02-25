package com.work.sklad.feature.main_activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.print.PDFPrint
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.work.sklad.R
import com.work.sklad.feature.common.Event
import com.work.sklad.feature.common.Events
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.google.android.material.snackbar.Snackbar
import com.tejpratapsingh.pdfcreator.activity.PDFViewerActivity
import com.tejpratapsingh.pdfcreator.utils.FileManager
import com.tejpratapsingh.pdfcreator.utils.PDFUtil
import com.work.sklad.feature.pdf_activity.PdfActivity
import java.io.File
import java.lang.Exception

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var events: Events

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycleScope.launch {
            events.getFlow().collectLatest {
                when (it) {
                    is ShowMessage -> Toast.makeText(this@MainActivity, it.text, Toast.LENGTH_SHORT).show()
                    is ShowNavSnackbar -> {
                        Snackbar.make(findViewById(R.id.nav_host_fragment_container), it.message, Snackbar.LENGTH_SHORT).apply {
                            setAction("Перейти") { _ ->
                                findNavController(R.id.nav_host_fragment_container).navigate(it.destination)
                            }
                            show()
                        }
                    }
                    is OpenPdf -> {
                        FileManager.getInstance()
                            .cleanTempFolder(applicationContext)
                        val savedPDFFile = FileManager.getInstance()
                            .createTempFile(applicationContext, "pdf", false)
                        PDFUtil.generatePDFFromHTML(applicationContext,
                            savedPDFFile,
                            it.html,
                            object : PDFPrint.OnPDFPrintListener {

                                override fun onSuccess(file: File?) {
                                    val pdfUri = Uri.fromFile(savedPDFFile)

                                    val intentPdfViewer = Intent(this@MainActivity, PdfActivity::class.java).apply {
                                        putExtra(
                                            PDFViewerActivity.PDF_FILE_URI,
                                            pdfUri
                                        )
                                    }
                                    startActivity(intentPdfViewer);
                                }

                                override fun onError(exception: Exception?) {
                                    exception?.printStackTrace();
                                }
                            })
                    }
                }
            }
        }
    }
}

class ShowMessage(val text: String): Event
class ShowNavSnackbar(val message: String, @IdRes val destination: Int) : Event
class OpenPdf(val html: String) : Event