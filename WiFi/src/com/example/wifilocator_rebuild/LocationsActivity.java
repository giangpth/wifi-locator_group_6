package com.example.wifilocator_rebuild;

import java.util.ArrayList;

import com.example.wifilocator_rebuild.module.Location;
import com.example.wifilocator_rebuild.module.LocationForm;
import com.example.wifilocator_rebuild.module.LocationFormEdit;
import com.example.wifilocator_rebuild.module.StorageManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class LocationsActivity extends Activity {
	/**
	 * this activity for managing all of saved location.
	 * using ListView to display all location which is "children" of  clicked location.
	 */
	
	//---variable--
	Location root,clickedLocation;
	TextView locationName;
	Button Edit;
	EditText editField;
	boolean isEdit; // this for checking Edit button has been clicked or not.
	
	//-----------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locations_manager_layout);
		
		//--- set value for all variable---
		isEdit=false;
		root=StorageManager.loadLocation();
		clickedLocation=root;
		locationName= (TextView) findViewById(R.id.location_name);
		locationName.setText(clickedLocation.printInfo());
		Edit =(Button) findViewById(R.id.edit_location);
		editField= new EditText(this);
		editField.setLines(4);
		//--- make ListView of Location---
		setListView(root);
	}
	

	public void setListView(Location currentChoice) {
		/*
		 * make locations ListView.
		 * using children list of current chosen locations to generate.
		 * 
		 */
		ArrayList<Location> allChildren= currentChoice.getAllLocationChildren();
		locationName.setText(currentChoice.printInfo());
		final ListView lv1 = (ListView) findViewById(R.id.listview_location);
		lv1.setAdapter(new LocationForm(this,allChildren));
		//set on item click
		lv1.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> a, View v, int position, long id) { 
        		clickedLocation=(Location)lv1.getItemAtPosition(position);
        		setListView(clickedLocation);
        	}
		});//indentation (giang)
	}
	
	public void setListViewEdit (Location currentChoice) {
		/*
		 * make locations ListView when Edit button is clicked.
		 * using children list of current chosen locations to generate.
		 * 
		 */
		ArrayList<Location> allChildren= currentChoice.getAllLocationChildren();
		locationName.setText(currentChoice.printInfo());
		final ListView lv1 = (ListView) findViewById(R.id.listview_location);
		lv1.setAdapter(new LocationFormEdit(this,allChildren));
		//set on item click
		lv1.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> a, View v, int position, long id) { 
        		clickedLocation=(Location)lv1.getItemAtPosition(position);
        		setListView((Location)clickedLocation.getParent());
        	}
		});//indentation (giang)
	}
	
	
	@SuppressWarnings("deprecation")
	public void onClickCross(View v) {
		/*
		 * set the function of "+" button.
		 * add a location without associated signals.
		 */	
		showDialog(0);
	}
	

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case 0:
			return new AlertDialog.Builder(this)
			.setIcon(R.drawable.wi_4)
			.setTitle("place name:")
			.setView(editField)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					String currentPlaceName = editField.getText().toString();
					Location currentLocation = new Location(currentPlaceName,null);
					clickedLocation.add(currentLocation);
					StorageManager.saveLocation(root);
					//reset ListView
					setListView(clickedLocation);
					editField.setText("");
					
				}
			})
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					
				}
			})		
			.create();
		}
		return null;
	}


	public void onClickBack(View v) {
		/*
		 * set the function of back button.
		 * change clickedLoction to it parent then setListView it.
		 */
		if(clickedLocation.getParent()!=null) {
			clickedLocation=(Location)clickedLocation.getParent();
			setListView(clickedLocation);
		}else{
			Toast.makeText(getBaseContext(),
					"Parent location is null"+clickedLocation.toString(),
					Toast.LENGTH_SHORT).show();
			root= StorageManager.loadLocation();
			clickedLocation=root;
			setListView(root);
		}
	}
	public void onClickEdit(View v) {
		/*
		 * set the function of Edit button
		 * change setListView to setListViewEdit when "edit" button clicked.
		 * restore to setListView then save Location when "done". 
		 */
		if(!isEdit){
			isEdit=true;
			setListViewEdit(clickedLocation);
			Edit.setText("Done");
		}else{
			isEdit=false;
			setListView(clickedLocation);
			Edit.setText("Edit");
			StorageManager.saveLocation(root);
		}
	}
	
	
}
//need more explanation (giang)
