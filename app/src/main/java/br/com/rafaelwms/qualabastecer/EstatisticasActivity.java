package br.com.rafaelwms.qualabastecer;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.ads.*;

public class EstatisticasActivity extends ActionBarActivity implements OnItemSelectedListener, OnItemClickListener, OnCheckedChangeListener {
	
	QualAbastecerDB db;
	
	int opcao;
	

	Spinner spValores;
	Button btnReset;
	RadioButton rbVeiculo;
	RadioButton rbPosto;
	RadioButton rbCombustivel;
	ListView listaAbs;
	
	AbastecimentoAdapter adapterLista;
	List<Abastecimento> abastecimentos;
	Abastecimento abs;
	
	List<Veiculo> veiculos;
	Veiculo veiculo;
	List<Posto> postos;
	Posto posto;
	
	//private AdView adView;
	
	public static final String ABASTECIMENTO_UPGRADE = "absUpg";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_estatisticas);
	/*
		// Criar o adView
	    adView = new AdView(this, AdSize.BANNER, "ca-app-pub-4671549534107534/6549452408");
	    
	  //AdRequest adRequest = new AdRequest();
	  //adRequest.addTestDevice(AdRequest.TEST_EMULATOR);         // Emulador
	  //adRequest.addTestDevice("TEST_DEVICE_ID");                // Dispositivo Android de teste
	    
	    // Pesquisar seu LinearLayout presumindo que ele foi dado
	    // o atributo android:id="@+id/mainLayout"
	    LinearLayout layout = (LinearLayout)findViewById(R.id.container);

	    // Adicionar o adView a ele
	    layout.addView(adView);

	    // Iniciar uma solicitação genérica para carregá-lo com um anúncio
	    adView.loadAd(new AdRequest());
		*/
		
		db = new QualAbastecerDB(this);

		rbVeiculo = (RadioButton)findViewById(R.id.rbVeiculo);
		rbPosto = (RadioButton)findViewById(R.id.rbPosto);
		rbCombustivel = (RadioButton)findViewById(R.id.rbCombustivel);
		
		rbVeiculo.setOnCheckedChangeListener(this);
		rbPosto.setOnCheckedChangeListener(this);
		rbCombustivel.setOnCheckedChangeListener(this);
		
		spValores = (Spinner)findViewById(R.id.spinnerValores);
		listaAbs = (ListView)findViewById(R.id.listViewResultados);
		

		spValores.setOnItemSelectedListener(this);
		listaAbs.setOnItemClickListener(this);
		
		carregarSpinnerOpcoes();
		

	}
	
/*
	@Override
	public void onDestroy() {
	  if (adView != null) {
	    adView.destroy();
	  }
	  super.onDestroy();
	}
*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.estatisticas, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		if(item.getItemId() == R.id.deletar_abastecimento){
			try{
			if(abs == null || abs.getId() < 1){
				throw new Exception(getResources().getString(R.string.exceptionAbastecimentoSelecionado));
			}else if(abs.getId() > 0){
				
				AlertDialog.Builder builder = new AlertDialog.Builder(this);

				builder.setMessage(R.string.dialogMsgDeleteFeed).setTitle(
						R.string.dialogTitleDeleteFeed);

				builder.setPositiveButton(R.string.dialogBtnOK,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							
								db.deleteabastecimento(abs);
								Toast.makeText(getApplication(), getResources().getString(R.string.abastecimentoDeleted), Toast.LENGTH_SHORT).show();
								carregarSpinnerOpcoes();
							}
						});
				
				builder.setNegativeButton(R.string.dialogBtnCanclear,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							
								carregarSpinnerOpcoes();
							}
						});
						

				AlertDialog dialog = builder.create();

				dialog.show();
				
				
			}
							
			}catch(Exception ex){
				Toast.makeText(getApplication(), ex.getMessage(), Toast.LENGTH_SHORT).show();
			}
			
			
			return true;
		}
		
		if(item.getItemId() == R.id.atualizar_abastecimento){
			try{
			if(abs == null || abs.getId() < 1){
				throw new Exception(getResources().getString(R.string.exceptionAbastecimentoSelecionado));
			}else if(abs.getId() > 0){
				
				AlertDialog.Builder builder = new AlertDialog.Builder(this);

				builder.setMessage(R.string.dialogMsgUpgradeFeed).setTitle(
						R.string.dialogTitleUpgradeFeed);

				builder.setPositiveButton(R.string.dialogBtnOK,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							
							Intent it = new Intent(getApplicationContext(), AbastecimentoActivity.class);
							it.putExtra(ABASTECIMENTO_UPGRADE, abs);
							startActivity(it);
							finish();
							
							}
						});
				
				builder.setNegativeButton(R.string.dialogBtnCanclear,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							
								carregarSpinnerOpcoes();
							}
						});
						

				AlertDialog dialog = builder.create();

				dialog.show();
				
				
			}
							
			}catch(Exception ex){
				Toast.makeText(getApplication(), ex.getMessage(), Toast.LENGTH_SHORT).show();
			}
			
			
			return true;
		}
		
		if(item.getItemId() == R.id.action_settings){
					
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
					builder.setMessage(R.string.helpMsgConsultas).setTitle(
							R.string.helpTituloConsultas);
		
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

	private void carregarSpinnerVeiculo(){
		veiculos = new ArrayList<Veiculo>();
		veiculos.add(new Veiculo(-1,
				getResources().getString(R.string.veiculo), -1, -1, -1));
		for (Veiculo ve : db.listarCarros()) {
			veiculos.add(ve);
		}

		ArrayAdapter<Veiculo> adapterVeiculos = new ArrayAdapter<Veiculo>(this,
				android.R.layout.simple_spinner_item, veiculos);
		adapterVeiculos
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spValores.setAdapter(adapterVeiculos);
	}
	
	private void carregarSpinnerPosto(){
		
		postos = new ArrayList<Posto>();
		postos.add(new Posto(-1, getResources().getString(R.string.Posto), 0,
				0, 0, 0));
		for (Posto po : db.listarPostos()) {
			postos.add(po);
		}

		ArrayAdapter<Posto> adapterPostos = new ArrayAdapter<Posto>(this,
				android.R.layout.simple_spinner_item, postos);
		adapterPostos
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spValores.setAdapter(adapterPostos);
		
	}
	
	private void carregarListaVeiculos(Veiculo veic){
		
		abs = new Abastecimento();
		abastecimentos = new ArrayList<Abastecimento>();
		abastecimentos = db.listarAbastecimentosPorCarro(veic);
		adapterLista = new AbastecimentoAdapter(abastecimentos);
		listaAbs.setAdapter(adapterLista);
				
	}
	
	private void carregarListaCombustivel(int comb){
		
		comb = (comb - 1);
		
		if(comb == 2){
			comb = 3;
		}
		abs = new Abastecimento();
		abastecimentos = new ArrayList<Abastecimento>();
		abastecimentos = db.listarAbastecimentosPorCombustivel(comb);
		adapterLista = new AbastecimentoAdapter(abastecimentos);
		listaAbs.setAdapter(adapterLista);
		
	}
	
	private void carregarListaPostos(Posto posto){
		
		abs = new Abastecimento();
		abastecimentos = new ArrayList<Abastecimento>();
		abastecimentos = db.listarAbastecimentosPorPosto(posto);
		adapterLista = new AbastecimentoAdapter(abastecimentos);
		listaAbs.setAdapter(adapterLista);
		
	}
	
	private void carregarSpinnerCombustivel(){
		
		String[] combs =  getResources().getStringArray(R.array.spinnerCobustivel);
		ArrayAdapter<String> adapterComb = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, combs);
		spValores.setAdapter(adapterComb);
		
	}
	
	private void carregarSpinnerOpcoes(){
		posto = new Posto();
		veiculo = new Veiculo();
		abs = new Abastecimento();
		opcao = 0;
		
		rbVeiculo.setChecked(false);
		rbPosto.setChecked(false);
		rbCombustivel.setChecked(false);
		
		String[] opcoes =  getResources().getStringArray(R.array.spinnerValores);
		ArrayAdapter<String> adapterOpc = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, opcoes);
		spValores.setAdapter(adapterOpc);
		
		abastecimentos = new ArrayList<Abastecimento>();
		adapterLista = new AbastecimentoAdapter(abastecimentos);
		listaAbs.setAdapter(adapterLista);
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
			
		
		if(parent == spValores){
		
				if(rbVeiculo.isChecked()){
				carregarListaVeiculos((Veiculo) spValores.getItemAtPosition(position));
				return;
				}

				if(rbPosto.isChecked()){
				carregarListaPostos((Posto) spValores.getItemAtPosition(position));
				return;
				}
				
				if(rbCombustivel.isChecked()){
				carregarListaCombustivel(position);
				return;
				}
		
			}

		}
		
	

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		abs = (Abastecimento) listaAbs.getItemAtPosition(position);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		
		if(buttonView.getId() == R.id.rbVeiculo && rbVeiculo.isChecked()){
			rbPosto.setChecked(false);
			rbCombustivel.setChecked(false);
			carregarSpinnerVeiculo();			
		}else if(buttonView.getId() == R.id.rbPosto && rbPosto.isChecked()){
			rbVeiculo.setChecked(false);
			rbCombustivel.setChecked(false);
			carregarSpinnerPosto();			
		}else if(buttonView.getId() == R.id.rbCombustivel && rbCombustivel.isChecked()){
			rbVeiculo.setChecked(false);
			rbPosto.setChecked(false);
			carregarSpinnerCombustivel();			
		}
		
	}

}
