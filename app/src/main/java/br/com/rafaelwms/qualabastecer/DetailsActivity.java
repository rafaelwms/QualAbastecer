package br.com.rafaelwms.qualabastecer;

import br.com.rafaelwms.qualabastecer.R;
import br.com.rafaelwms.qualabastecer.R.id;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DetailsActivity extends Activity implements OnClickListener {
	double gaso;
	TextView gasCost;
	TextView ethCost;
	TextView result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		
		gasCost = (TextView)findViewById(id.lblAct2GasPrice);
		ethCost = (TextView)findViewById(id.lblAct2EthPrice);
		result = (TextView)findViewById(id.lblAct2Result);
		
		Intent it = getIntent();
		gasCost.setText(it.getStringExtra("lblAct2GasPrice"));
		ethCost.setText(it.getStringExtra("lblAct2EthPrice"));
		gaso = (Double.parseDouble(gasCost.getText().toString()) * 0.7);
		if(it.getBooleanExtra("text", false) == true){
			result.setText(R.string.lblAct2ResultGas);
		}else{
			result.setText(R.string.lblAct2ResultValueEth);
		}
		
		


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId() == R.id.action_settings){
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setMessage(R.string.about).setTitle(
					R.string.action_settings);

			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
						
				
						}
					});
		
					

			AlertDialog dialog = builder.create();

			dialog.show();
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}

}
