package com.example.boliu.mypdf3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


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
			InputStream mInput = null;
			try {
				mInput = getAssets().open("docs/nonRotation1.pdf");
			} catch (IOException e) {
				e.printStackTrace();
			}
			m_PDFViewer.loadDocument(mInput);
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
		InputStream mInput = null;
		switch (item.getItemId()) {
			case R.id.open1 :
				try {
					mInput = getAssets().open("docs/nonRotation1.pdf");
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case R.id.open2 :
				try {
					mInput = getAssets().open("docs/nonRotation2.pdf");
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case R.id.open3 :
				try {
					mInput = getAssets().open("docs/haveRotation.pdf");
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case R.id.open4 :
				showPathEntryDialog();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
		m_PDFViewer.loadDocument(mInput);
		return true;
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
				mInputPath = input.getText().toString();
                m_PDFViewer.loadDocument(mInputPath);
            }
        });
        // Open the PDF file
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
