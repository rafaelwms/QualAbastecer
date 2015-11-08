package br.com.rafaelwms.qualabastecer;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.rafaelwms.qualabastecer.R;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class VeiculoActivity extends ActionBarActivity implements OnItemClickListener, OnClickListener, OnCheckedChangeListener {

	// Creating JSON Parser object
	Usuario usuario;
	private ProgressDialog pDialog;
	QualAbastecerDB db;
	SeekBar seekR, seekG, seekB;
	CheckBox ckbGasolina, ckbEtanol, ckbDiesel;
	Veiculo veiculo;
	List<Veiculo> veiculoss;
	EditText ediNomeCarro;
	ImageView imgVeiculo, imgCarro, imgMoto, imgCaminhao, imgOnibus;
	VeiculoAdapter adapter;
	ListView listaCarros;
	TextView txtCor;
	int r;
	int g;
	int b;
	int corAtual;
	int tipoVeiculo;
	int fav;

	private final String ESTADO_VEICULO = "estadoVeiculo";

	private final int CRIAR = 1;
	private final int ATUALIZAR = 2;
	private final int REMOVER = 3;
	private final int LISTAR = 4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_veiculo);

		db = new QualAbastecerDB(this);
		veiculo = new Veiculo();
		veiculoss = new ArrayList<Veiculo>();

		r = 0;
		g = 0;
		b = 0;
		corAtual = 0;
		fav = 0;
		tipoVeiculo = -1;

		usuario = new Usuario();
		Intent it = getIntent();
		if(it.hasExtra("usuario")) {
			usuario = (Usuario)it.getSerializableExtra("usuario");
		}else{
			Intent iit = new Intent(this, MainActivity.class);
			startActivity(iit);
			finish();
		}


		txtCor = (TextView)findViewById(R.id.txtColor);
		ediNomeCarro = (EditText)findViewById(R.id.edtCarroNome);
		ckbEtanol = (CheckBox)findViewById(R.id.chkbCarroEth);
		ckbGasolina = (CheckBox)findViewById(R.id.chkbCarroGas);
		ckbDiesel = (CheckBox)findViewById(R.id.chkbCarroDiesel);
		ckbGasolina.setOnCheckedChangeListener(this);
		ckbEtanol.setOnCheckedChangeListener(this);
		ckbDiesel.setOnCheckedChangeListener(this);

		seekR = (SeekBar)findViewById(R.id.seekBarR);
		seekG = (SeekBar)findViewById(R.id.seekBarG);
		seekB = (SeekBar)findViewById(R.id.seekBarB);
		listaCarros = (ListView)findViewById(R.id.listViewCarros);
		listaCarros.setOnItemClickListener(this);

		seekR.setOnSeekBarChangeListener(seekBarChangeListener);
		seekG.setOnSeekBarChangeListener(seekBarChangeListener);
		seekB.setOnSeekBarChangeListener(seekBarChangeListener);
		imgVeiculo = (ImageView)findViewById(R.id.imgVeiculo);
		imgCarro = (ImageView)findViewById(R.id.imgCarro);
		imgCaminhao = (ImageView)findViewById(R.id.imgCaminhao);
		imgMoto = (ImageView)findViewById(R.id.imgMoto);
		imgOnibus = (ImageView)findViewById(R.id.imgOnibus);

		imgCarro.setOnClickListener(this);
		imgCaminhao.setOnClickListener(this);
		imgMoto.setOnClickListener(this);
		imgOnibus.setOnClickListener(this);

		if(savedInstanceState != null){
			limparCampos();
			veiculo = (Veiculo) savedInstanceState.get(ESTADO_VEICULO);
			ediNomeCarro.setText(veiculo.getNome());
			selecionarCombustivel(veiculo.getCombustivel());
			atualizarSelecaoTipoVeiculos(veiculo.getTipo());
			imgVeiculo.setBackgroundColor(veiculo.getCor());
		}else{

			new VeiculoAsyncTasks(LISTAR).execute();

		}
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.carro, menu);
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub

		veiculo.setNome(ediNomeCarro.getText().toString());
		outState.putSerializable(ESTADO_VEICULO, veiculo);
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.salvar_carro) {
			try{
				if(veiculo.getId() <= 0){



					int combustivel = 0;

					if(ediNomeCarro.getText().toString().trim().equals("")){
						throw new Exception(getResources().getString(R.string.exceptionNomeVazio));
					}

					if(db.buscarCarroPorNome(ediNomeCarro.getText().toString()) != null){
						throw new Exception(getResources().getString(R.string.exceptionNomeUtilizadoCarro));
					}

					if(ckbEtanol.isChecked() == false && ckbGasolina.isChecked() == false && ckbDiesel.isChecked() == false){
						throw new Exception(getResources().getString(R.string.exceptionCombustivelVazio));
					}

					if(ckbEtanol.isChecked() == false && ckbGasolina.isChecked() == true){
						combustivel = 0;
					}
					if(ckbEtanol.isChecked() == true && ckbGasolina.isChecked() == false){
						combustivel = 1;
					}
					if(ckbEtanol.isChecked() == true && ckbGasolina.isChecked() == true){
						combustivel = 2;
					}
					if(ckbDiesel.isChecked() == true){
						combustivel = 3;
					}

					if(tipoVeiculo < 0){
						throw new Exception(getResources().getString(R.string.exceptionTipo));
					}


					veiculo = new Veiculo(ediNomeCarro.getText().toString(), tipoVeiculo, veiculo.getCor(), combustivel);
					new VeiculoAsyncTasks(CRIAR).execute();

				}else{
					int combustivel = -1;

					if(ediNomeCarro.getText().toString().trim().equals("")){
						throw new Exception(getResources().getString(R.string.exceptionNomeVazio));
					}

					Veiculo vehicle = db.buscarCarroPorNome(ediNomeCarro.getText().toString());

					if(vehicle != null && vehicle.getId() != veiculo.getId()){
						throw new Exception(getResources().getString(R.string.exceptionNomeUtilizadoCarro));
					}

					if(ckbEtanol.isChecked() == false && ckbGasolina.isChecked() == false && ckbDiesel.isChecked() == false){
						throw new Exception(getResources().getString(R.string.exceptionCombustivelVazio));
					}
					if(ckbEtanol.isChecked() == false && ckbGasolina.isChecked() == true){
						combustivel = 0;
					}
					if(ckbEtanol.isChecked() == true && ckbGasolina.isChecked() == false){
						combustivel = 1;
					}
					if(ckbEtanol.isChecked() == true && ckbGasolina.isChecked() == true){
						combustivel = 2;
					}
					if(ckbDiesel.isChecked() == true){
						combustivel = 3;
					}

					veiculo.setNome(ediNomeCarro.getText().toString());
					veiculo.setCor(corAtual);
					veiculo.setCombustivel(combustivel);
					veiculo.setTipo(tipoVeiculo);
					new VeiculoAsyncTasks(ATUALIZAR).execute();

				}
			}catch(Exception ex){
				Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
			}

			return true;
		}

		if(item.getItemId() == R.id.deletar_carro){
			if(veiculo.getId() <= 0){

				Toast.makeText(this, getResources().getString(R.string.toastSelecionarCarro), Toast.LENGTH_SHORT).show();

			}else{

				AlertDialog.Builder builder = new AlertDialog.Builder(this);

				builder.setMessage(R.string.dialogMsgDeleteCar).setTitle(
						R.string.dialogTitleDeleteCar);

				builder.setPositiveButton(R.string.dialogBtnOK,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {


								List<Abastecimento> abs = db.listarAbastecimentosPorCarro(veiculo);

								for(Abastecimento ab : abs){
									db.deleteabastecimento(ab);
								}


								new VeiculoAsyncTasks(REMOVER).execute();
							}
						});

				builder.setNegativeButton(R.string.dialogBtnCanclear,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								limparCampos();
							}
						});


				AlertDialog dialog = builder.create();

				dialog.show();

			}


			return true;

		}

		if(item.getItemId() == R.id.action_settings){

			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setMessage(R.string.helpMsgVeiculo).setTitle(
					R.string.helpTituloVeiculo);

			builder.setPositiveButton(R.string.dialogBtnOK,
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

	private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener()
	{

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
									  boolean fromUser) {
			// TODO Auto-generated method stub
			mudarCordoCarro();
			if(seekBar.getId() == R.id.seekBarR)
				txtCor.setText(getResources().getString(R.string.corRedValue)+" "+progress);

			if(seekBar.getId() == R.id.seekBarG)
				txtCor.setText(getResources().getString(R.string.corGreenValue)+" "+progress);

			if(seekBar.getId() == R.id.seekBarB)
				txtCor.setText(getResources().getString(R.string.corBlueValue)+" "+progress);

		}
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			if(seekBar.getId() == R.id.seekBarR)
				txtCor.setText(getResources().getString(R.string.corRed));

			if(seekBar.getId() == R.id.seekBarG)
				txtCor.setText(getResources().getString(R.string.corGreen));

			if(seekBar.getId() == R.id.seekBarB)
				txtCor.setText(getResources().getString(R.string.corBlue));
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			txtCor.setText("");
		}
	};


	private void limparCampos(){

		veiculo = new Veiculo();
		ediNomeCarro.setText("");
		seekR.setProgress(0);
		seekG.setProgress(0);
		seekB.setProgress(0);
		ckbGasolina.setChecked(false);
		ckbEtanol.setChecked(false);
		mudarCordoCarro();
		desligarSelecaoTipoVeiculos();
		imgVeiculo.setImageResource(R.drawable.ic_interrog);
		tipoVeiculo = -1;
	}

	private void desligarSelecaoTipoVeiculos(){
		imgCarro.setBackgroundColor(Color.rgb(0, 0, 0));
		imgCaminhao.setBackgroundColor(Color.rgb(0, 0, 0));
		imgOnibus.setBackgroundColor(Color.rgb(0, 0, 0));
		imgMoto.setBackgroundColor(Color.rgb(0, 0, 0));
		tipoVeiculo = 0;
	}

	private void atualizarSelecaoTipoVeiculos(int tipo){

		desligarSelecaoTipoVeiculos();

	/*
	 *
	 * Implementar Favorito em uma próxima versão usando a lógica semelhante abaixo.
	 *
	 * if(tipo == 1){
			imgVeiculo.setImageResource(R.drawable.ic_carro);
			imgCarro.setBackgroundColor(Color.rgb(255, 255, 255));
			if(veiculo.getTipo() == 1 && fav == 0){
			imgVeiculo.setImageResource(R.drawable.ic_fav_carro);
			fav = 1;
			return;
			}
			if(veiculo.getTipo() == 1 && fav == 1){
				imgVeiculo.setImageResource(R.drawable.ic_carro);
				fav = 0;
				return;
			}else{
			tipoVeiculo = 1;
			veiculo.setTipo(tipoVeiculo);
			fav = 0;
			}
			return;
		}
		*/

		if(tipo == 1 && tipoVeiculo != 1){
			imgCarro.setBackgroundColor(Color.rgb(255, 255, 255));
			imgVeiculo.setImageResource(R.drawable.ic_carro);
			tipoVeiculo = 1;
			veiculo.setTipo(tipoVeiculo);
			fav = 0;
			return;
		}else
		if(tipo == 1 && tipoVeiculo == 1 && fav == 0){
			imgVeiculo.setImageResource(R.drawable.ic_fav_carro);
			imgCarro.setImageResource(R.drawable.ic_fav_carro);
			veiculo.setTipo(tipoVeiculo);
			fav = 1;
			return;
		}else
		if (tipo == 1 && tipoVeiculo == 1 && fav == 1){
			imgVeiculo.setImageResource(R.drawable.ic_carro);
			imgCarro.setImageResource(R.drawable.ic_carro);
			veiculo.setTipo(tipoVeiculo);
			fav = 0;
			return;
		}else

		if(tipo == 2 && tipoVeiculo != 2){
			imgMoto.setBackgroundColor(Color.rgb(255, 255, 255));
			imgVeiculo.setImageResource(R.drawable.ic_moto);
			tipoVeiculo = 2;
			veiculo.setTipo(tipoVeiculo);
			fav = 0;
			return;
		}else

		if(tipo == 2 && tipoVeiculo == 2 && fav == 0){
			imgVeiculo.setImageResource(R.drawable.ic_fav_moto);
			imgMoto.setImageResource(R.drawable.ic_fav_moto);
			fav = 1;
			veiculo.setTipo(tipoVeiculo);
			return;
		}else
		if (tipo == 2 && tipoVeiculo == 2 && fav == 1){
			imgVeiculo.setImageResource(R.drawable.ic_moto);
			imgMoto.setImageResource(R.drawable.ic_moto);
			fav = 0;
			veiculo.setTipo(tipoVeiculo);
			return;
		}else


		if(tipo == 3 && tipoVeiculo != 3){
			imgCaminhao.setBackgroundColor(Color.rgb(255, 255, 255));
			imgVeiculo.setImageResource(R.drawable.ic_caminhao);
			tipoVeiculo = 3;
			fav = 0;
			veiculo.setTipo(tipoVeiculo);
			return;
		}else
		if(tipo == 3 && tipoVeiculo == 3 && fav == 0){
			imgVeiculo.setImageResource(R.drawable.ic_fav_caminhao);
			imgCaminhao.setImageResource(R.drawable.ic_fav_caminhao);
			fav = 1;
			veiculo.setTipo(tipoVeiculo);
			return;
		}else

		if (tipo == 3 && tipoVeiculo == 3 && fav == 1){
			imgVeiculo.setImageResource(R.drawable.ic_caminhao);
			imgCaminhao.setImageResource(R.drawable.ic_caminhao);
			fav = 0;
			veiculo.setTipo(tipoVeiculo);
			return;
		}else

		if(tipo == 4 && tipoVeiculo != 4){
			imgOnibus.setBackgroundColor(Color.rgb(255, 255, 255));
			imgVeiculo.setImageResource(R.drawable.ic_onibus);
			tipoVeiculo = 4;
			fav = 0;
			veiculo.setTipo(tipoVeiculo);
			return;
		}else
		if(tipo == 4 && tipoVeiculo == 4 && fav == 0){
			imgVeiculo.setImageResource(R.drawable.ic_fav_onibus);
			imgOnibus.setImageResource(R.drawable.ic_fav_onibus);
			fav = 1;
			veiculo.setTipo(tipoVeiculo);
			return;
		}else
		if (tipo == 4 && tipoVeiculo == 4 && fav == 1){
			imgVeiculo.setImageResource(R.drawable.ic_onibus);
			imgOnibus.setImageResource(R.drawable.ic_onibus);
			fav = 0;
			veiculo.setTipo(tipoVeiculo);
			return;
		}

	}


	private void mudarCordoCarro(){

		r = seekR.getProgress();
		g = seekG.getProgress();
		b = seekB.getProgress();

		imgVeiculo.setBackgroundColor(Color.rgb(r, g, b));


		veiculo.setCor(0xff000000
				+ r * 0x10000
				+ g * 0x100
				+ b);

		corAtual = 0xff000000
				+ r * 0x10000
				+ g * 0x100
				+ b;

	}

	private void selecionarCombustivel(int comb){
		switch (comb) {
			case 0:

				ckbGasolina.setChecked(true);
				ckbEtanol.setChecked(false);
				ckbDiesel.setChecked(false);

				break;

			case 1:

				ckbGasolina.setChecked(false);
				ckbEtanol.setChecked(true);
				ckbDiesel.setChecked(false);

				break;

			case 2:

				ckbGasolina.setChecked(true);
				ckbEtanol.setChecked(true);
				ckbDiesel.setChecked(false);

				break;

			case 3:

				ckbGasolina.setChecked(false);
				ckbEtanol.setChecked(false);
				ckbDiesel.setChecked(true);

				break;

			default:
				break;
		}
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {

		veiculo = (Veiculo) listaCarros.getItemAtPosition(position);
		ediNomeCarro.setText(veiculo.getNome());
		imgVeiculo.setBackgroundColor(veiculo.getCor());
		corAtual = veiculo.getCor();
		atualizarSelecaoTipoVeiculos(veiculo.getTipo());
		selecionarCombustivel(veiculo.getCombustivel());

	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if(v.getId() == R.id.imgCarro){

			atualizarSelecaoTipoVeiculos(1);

		} else if(v.getId() == R.id.imgMoto){

			atualizarSelecaoTipoVeiculos(2);

		}else if(v.getId() == R.id.imgCaminhao){

			atualizarSelecaoTipoVeiculos(3);

		}else if(v.getId() == R.id.imgOnibus){

			atualizarSelecaoTipoVeiculos(4);

		}

	}



	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if(buttonView.getId() == R.id.chkbCarroDiesel && isChecked){
			ckbEtanol.setChecked(false);
			ckbGasolina.setChecked(false);
			ckbDiesel.setChecked(true);
		}
		else if(buttonView.getId() == R.id.chkbCarroDiesel && isChecked == false){
			ckbEtanol.setChecked(false);
			ckbGasolina.setChecked(false);
			ckbDiesel.setChecked(false);
		}else

		if(buttonView.getId() == R.id.chkbCarroGas && isChecked){
			ckbDiesel.setChecked(false);
			ckbGasolina.setChecked(true);
		}else
		if(buttonView.getId() == R.id.chkbCarroEth && isChecked){
			ckbDiesel.setChecked(false);
			ckbEtanol.setChecked(true);
		}



	}


	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}

	/// ININIO


	class VeiculoAsyncTasks extends AsyncTask<String, String, String> {

		private int action;
		private final int CRIAR = 1;
		private final int ATUALIZAR = 2;
		private final int REMOVER = 3;
		private final int LISTAR = 4;

		protected VeiculoAsyncTasks(int action)
		{
			this.action = action;
		}
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(VeiculoActivity.this);
			pDialog.setMessage("Contactando Servidor...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating product
		 * */
		protected String doInBackground(String... args) {

			try {
				VeiculoDB vdb = new VeiculoDB();
				pDialog.setMessage("Realizando operação...");
				veiculo.setUsuario(usuario);
				switch (action) {
					case CRIAR:
						vdb.insertCarro(veiculo);
						veiculoss = new ArrayList<Veiculo>();
						veiculoss = vdb.listarCarros(usuario);
						break;
					case ATUALIZAR:
						vdb.alterarCarro(veiculo);
						veiculoss = new ArrayList<Veiculo>();
						veiculoss = vdb.listarCarros(usuario);
						break;
					case REMOVER:
						vdb.deleteCarro(veiculo);
						veiculoss = new ArrayList<Veiculo>();
						veiculoss = vdb.listarCarros(usuario);
						break;
					case LISTAR:
						veiculoss = new ArrayList<Veiculo>();
						veiculoss = vdb.listarCarros(usuario);
						break;

					default:
						break;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					adapter = new VeiculoAdapter(veiculoss);
					listaCarros.setAdapter(adapter);
					limparCampos();
				}
			});
		}

	}




	////FINO
}

