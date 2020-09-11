package com.example.pdfgeneratorapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), PackageManager.PERMISSION_GRANTED
        )
        createPDF()
    }

    @SuppressLint("ResourceAsColor")
    fun createPDF() {

        buttonGerarPdf.setOnClickListener(View.OnClickListener {
            
            val btm1 = BitmapFactory.decodeResource(resources, R.drawable.croqui)
            val btm2 = BitmapFactory.decodeResource(resources, R.drawable.maps)
            val scaledBitmap1 = Bitmap.createScaledBitmap(btm1, 300,300, false)
            val scaledBitmap2 = Bitmap.createScaledBitmap(btm2, 300,300, false)

            val document = PdfDocument()
            val paint = Paint().apply {
                isAntiAlias = true
                color = Color.BLUE
                style = Paint.Style.FILL
                textAlign = Paint.Align.CENTER
            }

            var i = 1
            while (i <= 6) {
                val pageInfo = PdfDocument.PageInfo.Builder(595, 842, i).create()
                val page = document.startPage(pageInfo)
                val canvas = page.canvas

                paint.color = Color.BLUE
                paint.textSize = 18.0F
                canvas.drawText(
                    "Maptriz Smart City",
                    (pageInfo.pageWidth / 2).toFloat(),
                    30F,
                    paint
                )

                paint.textSize = 10.0F
                paint.color = Color.rgb(112, 119, 119)
                canvas.drawText(
                    "Av. Higienópolis 110, Londrina-PR",
                    (pageInfo.pageWidth / 2).toFloat(),
                    50F,
                    paint
                )
                canvas.drawBitmap(scaledBitmap1, (pageInfo.pageWidth / 4).toFloat(), 80f, paint)
                canvas.drawLine(
                    0f,
                    (pageInfo.pageHeight / 2).toFloat(),
                    595f,
                    (pageInfo.pageHeight / 2).toFloat(),
                    paint
                )

                paint.color = Color.BLUE
                paint.textSize = 18.0F
                canvas.drawText(
                    "Maptriz Smart City",
                    (pageInfo.pageWidth / 2).toFloat(),
                    (pageInfo.pageHeight/2).toFloat() + 30F,
                    paint
                )

                paint.textSize = 10.0F
                paint.color = Color.rgb(112, 119, 119)
                canvas.drawText(
                    "Av. Higienópolis 110, Londrina-PR",
                    (pageInfo.pageWidth / 2).toFloat(),
                    (pageInfo.pageHeight/2).toFloat() + 50F,
                    paint
                )
                canvas.drawBitmap(scaledBitmap2, (pageInfo.pageWidth / 4).toFloat(), (pageInfo.pageHeight/2).toFloat() + 80f, paint)

                document.finishPage(page)

                i++;
            }

            val file = getOutputMediaFile()

            try {
                document.writeTo(FileOutputStream(file))
            }catch(e: IOException){
                e.printStackTrace()
            }

            document.close();

            Toast.makeText(this, "PDF GERADO !", Toast.LENGTH_LONG).show()
        })
    }

    fun getOutputMediaFile() : File? {
        val mediaStorageDir = File(
            Environment.getDataDirectory(),
            "/data/"
                    + applicationContext.packageName
                    + "/documents"
        );
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        val timeStamp = SimpleDateFormat("ddMMyyyyHHmm", Locale.US).format(Date());
        val mPdfName = "TESTE-PDF$timeStamp.pdf";
        val mediaFile = File(mediaStorageDir.path + File.separator + mPdfName);
        return mediaFile;
    }
}