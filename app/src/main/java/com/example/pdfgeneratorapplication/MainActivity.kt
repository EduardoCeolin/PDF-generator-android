package com.example.pdfgeneratorapplication

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pdfgeneratorapplication.PermissionsActivity.Companion.PERMISSION_REQUEST_CODE
import com.example.pdfgeneratorapplication.PermissionsChecker.Companion.REQUIRED_PERMISSION
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val checker = PermissionsChecker(this)

        buttonGerarPdf.setOnClickListener(View.OnClickListener {

            if (checker.lacksPermissions(*REQUIRED_PERMISSION)) {
                PermissionsActivity.startActivityForResult(
                    this,
                    PERMISSION_REQUEST_CODE,
                    *REQUIRED_PERMISSION
                )
            } else {
                createPDF()
            }
        })
    }

    fun createPDF() {
        val document = Document();

        // Location to save
        PdfWriter.getInstance(
            document,
            FileOutputStream(getAppPath(applicationContext) + "123.pdf")
        )

        // Open to write
        document.open();

        // Document Settings
        document.pageSize = PageSize.A4;
        document.addCreationDate();
        document.addAuthor("Android School");
        document.addCreator("Pratik Butani");

        //variables
        val mColorAccent = BaseColor(0, 153, 204, 255)
        val mHeadingFontSize = 20.0f

        val urName = BaseFont.createFont(BaseFont.HELVETICA, "UTF-8", BaseFont.EMBEDDED)

        // LINE SEPARATOR
        val lineSeparator = LineSeparator()
        lineSeparator.lineColor = BaseColor(0, 0, 0, 68)

        // Title Order Details...
        // Adding Title....
        val mOrderDetailsTitleFont = Font(urName, 36.0f, Font.NORMAL, BaseColor.BLACK)

        // Creating Chunk
        val mOrderDetailsTitleChunk = Chunk("Order Details", mOrderDetailsTitleFont)

        // Creating Paragraph to add...
        val mOrderDetailsTitleParagraph = Paragraph(mOrderDetailsTitleChunk)

        // Setting Alignment for Heading
        mOrderDetailsTitleParagraph.alignment = Element.ALIGN_CENTER

        // Finally Adding that Chunk
        document.add(mOrderDetailsTitleParagraph)

        // Adding Chunks for Title and value
        val mOrderIdFont = Font(urName, mHeadingFontSize, Font.NORMAL, mColorAccent)
        val mOrderIdChunk = Chunk("Order No:", mOrderIdFont)
        val mOrderIdParagraph = Paragraph(mOrderIdChunk)
        document.add(mOrderIdParagraph)

        document.add(Paragraph(""));
        document.add(Chunk(lineSeparator));
        document.add(Paragraph(""));

        document.close();

        Toast.makeText(this, "PDF GERADO !", Toast.LENGTH_LONG).show()
    }

    fun getAppPath(context: Context): String? {
        val dir = File(
            Environment.getExternalStorageDirectory()
                .toString() + File.separator
                    + "PDF"
                    + File.separator
        )
        if (!dir.exists()) {
            dir.mkdir()
        }
        return dir.path + File.separator
    }
}