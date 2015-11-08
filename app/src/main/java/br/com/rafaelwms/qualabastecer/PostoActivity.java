package br.com.rafaelwms.qualabastecer;

import java.util.ArrayList;
import java.util.List;

import br.com.rafaelwms.qualabastecer.R;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class PostoActivity extends ActionBarActivity implements OnItemClickListener, OnRatingBarChangeListener {
	
	
	QualAbastecerDB db;
	Posto posto;
	List<Posto> postos;
	PostoAdapter adapter;
	
	EditText nomePosto;
	EditText precoGasolina;
	EditText precoEtanol;
	EditText precoDiesel;
	RatingBar ratingAtendimento;
	ListView postosListView;
	
	Double gasolina;
	Double etanol;
	Double diesel;
	
	private final String INSTANCIA_POSTO = "instanciaPosto";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_posto);
		
		db = new QualAbastecerDB(this);
		posto = new Posto();
		
		postos = new ArrayList<Posto>();
		
		nomePosto = (EditText)findViewById(R.id.editPostoNome);
		precoGasolina = (EditText)findViewById(R.id.editPostoGasolina);
		precoEtanol = (EditText)findViewById(R.id.editPostoEtanol);
		precoDiesel = (EditText)findViewById(R.id.editPostoDiesel);
		precoDiesel.setOnEditorActionListener(new TextView.OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        if (actionId == EditorInfo.IME_ACTION_DONE) {
		           salvarPosto();
		            return true;
		        }
		        return false;
		    }
		});
		ratingAtendimento = (RatingBar)findViewById(R.id.ratingBarAtendimentoPosto);
		postosListView = (ListView)findViewById(R.id.listViewPostos);
		
		postosListView.setOnItemClickListener(this);
		ratingAtendimento.setOnRatingBarChangeListener(this);
		ratingAtendimento.setMax(5);
		ratingAtendimento.setStepSize(1);
		
		atualizarlista();
		
		if(savedInstanceState != null){
			
			posto = (Posto)savedInstanceState.get(INSTANCIA_POSTO);
			nomePosto.setText(posto.getNome());
			precoGasolina.setText(String.valueOf(posto.getLitroGasolina()));
			precoEtanol.setText(String.valueOf(posto.getLitroEtanol()));
			precoDiesel.setText(String.valueOf(posto.getLitroDiesel()));
		
		}
		

	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
		if(!nomePosto.getText().toString().trim().equals("")){
			posto.setNome(nomePosto.getText().toString());
		}
		if(!precoGasolina.getText().toString().trim().equals("")){
			posto.setLitroGasolina(Double.parseDouble(precoGasolina.getText().toString()));
		}
		if(!precoEtanol.getText().toString().trim().equals("")){
			posto.setLitroEtanol(Double.parseDouble(precoEtanol.getText().toString()));
		}
		if(!precoDiesel.getText().toString().trim().equals("")){
			posto.setLitroDiesel(Double.parseDouble(precoDiesel.getText().toString()));
		}
		
		outState.putSerializable(INSTANCIA_POSTO, posto);
		
		super.onSaveInstanceState(outState);
	}
	
	
	private void atualizarlista(){
		
		postos = new ArrayList<Posto>();
		postos = db.listarPostos();
		adapter = new PostoAdapter(postos);
		postosListView.setAdapter(adapter);
		limparCampos();
				
	}
	
	private void limparCampos(){
		
		posto = new Posto();
		nomePosto.setText("");
		precoGasolina.setText("");
		precoEtanol.setText("");
		precoDiesel.setText("");
		ratingAtendimento.setProgress(0);
		
	}
	
	private void preencherCampos(){
		
		nomePosto.setText(posto.getNome());
		precoGasolina.setText(posto.getLitroGasolina()+"");
		precoEtanol.setText(posto.getLitroEtanol()+"");
		precoDiesel.setText(posto.getLitroDiesel()+"");
		ratingAtendimento.setProgress(posto.getAtendimento());
		
		
	}
	
	
	private void salvarPosto(){
		if(posto == null || posto.getId() <= 0){
			try{
			if(nomePosto.getText().toString().trim().equals("")){
				throw new Exception(getResources().getString(R.string.exceptionNomePostoVazio));
			}
			String nome = nomePosto.getText().toString();
			Posto result = db.buscarPostoPorNome(nome);
			if(result != null && nomePosto.getText().toString().trim().equals(db.buscarPostoPorNome(nome).getNome())){
				throw new Exception(getResources().getString(R.string.exceptionNomeUtilizadoPosto));
			}
			
			if(precoGasolina.getText().toString().trim().equals("")){
				throw new Exception(getResources().getString(R.string.exceptionPostoCombustivelVazio));
			}
			
			if(precoEtanol.getText().toString().trim().equals("")){
				throw new Exception(getResources().getString(R.string.exceptionPostoCombustivelVazio));
			}
			
			if(precoDiesel.getText().toString().trim().equals("")){
				throw new Exception(getResources().getString(R.string.exceptionPostoCombustivelVazio));
			}
			
			gasolina = Double.parseDouble(precoGasolina.getText().toString());
			etanol = Double.parseDouble(precoEtanol.getText().toString());
			diesel = Double.parseDouble(precoDiesel.getText().toString());
			
			posto = new Posto(nomePosto.getText().toString(), ratingAtendimento.getProgress(), gasolina, etanol, diesel);
			db.insertPosto(posto);
			Toast.makeText(getApplication(), getResources().getString(R.string.toastSalvarPosto), Toast.LENGTH_LONG).show();
			atualizarlista();
			}catch (Exception ex){
				Toast.makeText(getApplication(), ex.getMessage(), Toast.LENGTH_LONG).show();
			}
			
			
		}else if (posto.getId() >= 1){
			try{
				if(nomePosto.getText().toString().trim().equals("")){
					throw new Exception(getResources().getString(R.string.exceptionNomePostoVazio));
				}
				String nome = nomePosto.getText().toString();
				Posto result = db.buscarPostoPorNome(nome);
				if(result != null && result.getId() != posto.getId() && nomePosto.getText().toString().trim().equals(db.buscarPostoPorNome(nome).getNome())){
					throw new Exception(getResources().getString(R.string.exceptionNomeUtilizadoPosto));
				}
				
				if(precoGasolina.getText().equals("") || precoEtanol.getText().equals("") || precoDiesel.getText().equals("")){
					throw new Exception(getResources().getString(R.string.exceptionPostoCombustivelVazio));
				}
				
				gasolina = Double.parseDouble(precoGasolina.getText().toString());
				etanol = Double.parseDouble(precoEtanol.getText().toString());
				diesel = Double.parseDouble(precoDiesel.getText().toString());
				
				posto.setAtendimento(ratingAtendimento.getProgress());
				posto.setNome(nomePosto.getText().toString());
				posto.setLitroGasolina(gasolina);
				posto.setLitroEtanol(etanol);
				posto.setLitroDiesel(diesel);
				
				db.alterarPosto(posto);
				Toast.makeText(getApplication(), getResources().getString(R.string.toastAtualizarPosto), Toast.LENGTH_LONG).show();
				atualizarlista();
				}catch (Exception ex){
					Toast.makeText(getApplication(), ex.getMessage(), Toast.LENGTH_LONG).show();
				}

		}
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.posto, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
			if (id == R.id.salvar_posto) {
				
				salvarPosto();
				
			}			
			
			if(id == R.id.deletar_posto){
				if(posto.getId() <= 0){
					
					Toast.makeText(this, getResources().getString(R.string.toastSelecionarPosto), Toast.LENGTH_SHORT).show();
					atualizarlista();
					
				}else{
					
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setMessage(R.string.dialogMsgDeletePosto).setTitle(
							R.string.dialogTitleDeletePosto);
					builder.setPositiveButton(R.string.dialogBtnOK,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									posto = db.buscarPostoPorId(posto.getId());
									
									List<Abastecimento> abs = db.listarAbastecimentosPorPosto(posto);
									
									for(Abastecimento ab : abs){
										db.deleteabastecimento(ab);
									}
									
									db.deletePosto(posto);
									Toast.makeText(getApplication(), getResources().getString(R.string.toastDeletarPosto), Toast.LENGTH_SHORT).show();
									atualizarlista();
								}
							});					
					builder.setNegativeButton(R.string.dialogBtnCanclear,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
								
									atualizarlista();
								}
							});
					AlertDialog dialog = builder.create();
					dialog.show();					
				}
				return true;
			}
			
			if(item.getItemId() == R.id.action_settings){
				
				AlertDialog.Builder builder = new AlertDialog.Builder(this);

				builder.setMessage(R.string.helpMsgPosto).setTitle(
						R.string.helpTituloPosto);

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


	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating,
			boolean fromUser) {
		// TODO Auto-generated method stub



	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		posto = (Posto) postosListView.getItemAtPosition(position);
		preencherCampos();
	}
	
	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}

}
