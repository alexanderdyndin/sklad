package com.work.sklad.feature.pdf_activity

import com.tejpratapsingh.pdfcreator.activity.PDFCreatorActivity
import android.content.Intent
import android.widget.Toast
import android.os.Bundle
import com.work.sklad.R
import java.io.File
import java.util.*
import androidx.core.content.FileProvider
import com.tejpratapsingh.pdfcreator.utils.PDFUtil
import android.print.PrintAttributes
import android.graphics.drawable.ColorDrawable
import android.view.Menu
import android.view.MenuItem
import com.tejpratapsingh.pdfcreator.activity.PDFViewerActivity
import java.net.URLConnection


class PdfActivity: PDFViewerActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Sklad1)
        super.onCreate(savedInstanceState)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = "Pdf Viewer"
            supportActionBar!!.setBackgroundDrawable(
                ColorDrawable(getColor(R.color.colorTransparentBlack)))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_pdf_viewer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.menuPrintPdf -> {
                val fileToPrint: File = pdfFile
                if (!fileToPrint.exists()) {
                    Toast.makeText(this, "ошибка", Toast.LENGTH_SHORT).show()
                    return super.onOptionsItemSelected(item)
                }
                val printAttributeBuilder = PrintAttributes.Builder()
                printAttributeBuilder.setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                printAttributeBuilder.setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                PDFUtil.printPdf(
                    this,
                    fileToPrint,
                    printAttributeBuilder.build()
                )
            }
            R.id.menuSharePdf -> {
                val fileToShare: File = pdfFile
                if (!fileToShare.exists()) {
                    Toast.makeText(this, "ошибка", Toast.LENGTH_SHORT).show()
                    return super.onOptionsItemSelected(item)
                }
                val intentShareFile = Intent(Intent.ACTION_SEND)
                val apkURI = FileProvider.getUriForFile(
                    applicationContext,
                    applicationContext
                        .packageName + ".provider", fileToShare
                )
                intentShareFile.setDataAndType(
                    apkURI,
                    URLConnection.guessContentTypeFromName(fileToShare.name)
                )
                intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intentShareFile.putExtra(
                    Intent.EXTRA_STREAM,
                    apkURI
                )
                startActivity(Intent.createChooser(intentShareFile, "Share File"))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}