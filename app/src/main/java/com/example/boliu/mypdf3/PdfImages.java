package com.example.boliu.mypdf3;

import android.content.Context;
import android.widget.Toast;

import com.qoppa.pdf.IPassword;
import com.qoppa.pdf.PDFException;
import com.qoppa.pdfImages.PDFImages;

import java.io.File;
import java.io.IOException;

/**
 * Created by boliu on 2/10/15.
 */
public class PdfImages implements IPassword {

    // Sample DPI exports at 144 pixels per inch (double native PDF resolution)
    private final static int EXPORT_DPI = 144;

    Context mContext;
    String openFilePath = null;
    String outFilePath = null;

    public PdfImages(Context context, String openFilePath, String outFilePath) {
        mContext = context;
        this.openFilePath = openFilePath;
        this.outFilePath = outFilePath;
    }

    public void exportPNGs ()
    {

        // Get the pdf file to export
        File file1 = new File(openFilePath);
        if (file1 == null)
        {
            return;
        }

        try
        {
            // Make sure there is an exports folder
            File exportsDir = new File(outFilePath);
            if (exportsDir == null)
            {
                return;
            }

            // Load the document
            PDFImages images = new PDFImages (file1.getAbsolutePath(), this);

            // get document pages
            for (int count = 0; count < images.getPageCount(); ++count)
            {
                // Save the buffered image as a JPEG
                File outFile = new File (exportsDir, "page" + count + ".png");
                images.savePageAsPNG(count, outFile.getAbsolutePath(), EXPORT_DPI);
            }

            // Show message
            Toast.makeText(mContext, "Files were exported to:\n" + exportsDir.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        }
        catch (PDFException pdfE)
        {
            pdfE.printStackTrace();
        }
        catch (IOException ioE)
        {
            ioE.printStackTrace();
        }
    }

    @Override
    public String[] getPasswords() {
        return new String[0];
    }
}
