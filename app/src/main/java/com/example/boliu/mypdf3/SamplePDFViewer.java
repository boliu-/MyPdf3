package com.example.boliu.mypdf3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.qoppa.android.pdf.annotations.AnnotationFactory;
import com.qoppa.android.pdf.annotations.FreeText;
import com.qoppa.pdf.IPassword;
import com.qoppa.pdf.PDFException;
import com.qoppa.pdfImages.PDFImages;


public class SamplePDFViewer extends Activity {
	private PDFViewer m_PDFViewer;
    private static final String path = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
		m_PDFViewer = new PDFViewer(this);
		
		Intent intent = getIntent();
		if(intent.getAction() != null && intent.getAction().equals("android.intent.action.VIEW"))
		{
			//This activity was launched with an Intent to view a specific file
			loadFromUri(intent.getData());
		}
		else
		{
			//Display a dialog to prompt for a file path to load
			showPathEntryDialog();
			
			//Alternativetly, some default document could be loaded with:
			//m_PDFViewer.loadDocument("/sdcard/default.pdf");
		}
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		//this occurs when device is rotated or keyboard is exposed/hidden
		super.onConfigurationChanged(newConfig);
		
		m_PDFViewer.onConfigurationChanged(newConfig);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		//this occurs the first time the "menu" button is pressed
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.optionsmenu, menu);
	    return true;
	}
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		if(item.getItemId() == R.id.open)
		{
			showPathEntryDialog();
			return true;
		}
		else
		{
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void loadFromUri(Uri uri)
	{
		File file = new File(uri.getPath());	
		if(file.exists())
		{
			//intent refers to a local file
			m_PDFViewer.loadDocument(uri.getPath());
		}
		else
		{
			//intent contains an inputstream
			try
			{
				InputStream stream = getContentResolver().openInputStream(uri);
				m_PDFViewer.loadDocument(stream);
			}
			catch (FileNotFoundException e)
			{
				PDFViewer.handleException(this, e);
			}
		}
	}
	
	private void showPathEntryDialog()
	{
		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		final EditText input = new EditText(this);
		input.setText("/sdcard/signnow/9082502_blank.pdf");
		alert.setView(input);
        AlertDialog.Builder ok = alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            String mInputPath = input.getText().toString();

            public void onClick(DialogInterface dialog, int whichButton) {
                m_PDFViewer.loadDocument(mInputPath);
            }
        });
        // Open the PDF file
;
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int whichButton) 
			{
				dialog.cancel();
			}
		});

		alert.show();
	}

}
