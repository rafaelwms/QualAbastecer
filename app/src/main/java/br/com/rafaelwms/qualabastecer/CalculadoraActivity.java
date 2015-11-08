package br.com.rafaelwms.qualabastecer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import br.com.rafaelwms.qualabastecer.R;
import br.com.rafaelwms.qualabastecer.R.id;



public class CalculadoraActivity extends ActionBarActivity implements OnClickListener {

	TextView lblResult;
	TextView txtResult;
	EditText fieldGas;
	EditText fieldEth;
	Button btnAct;
	Button btnAct2;
	boolean gas = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calculadora);

		lblResult = (TextView) findViewById(id.textViewLblResult);
		txtResult = (TextView) findViewById(id.textViewResult);
		fieldGas = (EditText) findViewById(id.editTextGas);
		fieldEth = (EditText) findViewById(id.editTextEthanol);
		btnAct = (Button) findViewById(id.buttonAct);
		btnAct2 = (Button) findViewById(id.buttonAct2);
		

		btnAct2.setEnabled(false);

		btnAct.setOnClickListener(this);
		btnAct2.setOnClickListener(this);

		String res = "";
		boolean btn2 = false;

		if (savedInstanceState != null) {
			res = savedInstanceState.getString("resultado");
			btn2 = savedInstanceState.getBoolean("btn2");
		}

		if (btn2 == true) {
			btnAct2.setEnabled(true);
		}

		txtResult.setText(res);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId() == R.id.action_settings){
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setMessage(R.string.helpMsgCalculadora).setTitle(
					R.string.helpTituloCalculadora);

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
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("resultado", txtResult.getText().toString());

		if (btnAct2.isEnabled() == true) {
			outState.putBoolean("btn2", true);
		} else {
			outState.putBoolean("btn2", false);
		}

	}

	
	private void calcular(){
		try {

			if (fieldGas.getText().toString().trim().equals("")) {

				fieldGas.setError(getResources().getString(
						R.string.errorField));
				return;
			}

			if (fieldEth.getText().toString().trim().equals("")) {

				fieldEth.setError(getResources().getString(
						R.string.errorField));
				return;
			}

			double gasCost = Double.parseDouble(fieldGas.getText()
					.toString());
			double ethCost = Double.parseDouble(fieldEth.getText()
					.toString());

			if (ethCost < (gasCost * 0.7)) {
				txtResult.setText(R.string.ethResult);
			} else {
				txtResult.setText(R.string.gasResult);
				gas = true;
			}
			btnAct2.setEnabled(true);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage(getResources().getString(R.string.lblBestFuel)+" "+txtResult.getText().toString()).setTitle(
				getResources().getString(R.string.use)+" "+txtResult.getText().toString());

		builder.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					
			
					}
				});
	
				

		AlertDialog dialog = builder.create();

		dialog.show();
		
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.buttonAct) {

			calcular();

		}

		if (v.getId() == R.id.buttonAct2) {
			Intent inn = new Intent(this, DetailsActivity.class);
			inn.putExtra("lblAct2GasPrice", fieldGas.getText().toString());
			inn.putExtra("lblAct2EthPrice", fieldEth.getText().toString());
			inn.putExtra("text", gas);
			startActivity(inn);
		}

	}
	
	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}

}
