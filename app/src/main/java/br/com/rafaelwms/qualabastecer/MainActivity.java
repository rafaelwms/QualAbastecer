package br.com.rafaelwms.qualabastecer;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.google.ads.*;

import br.com.rafaelwms.qualabastecer.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements OnItemClickListener  {
	
	public static final int CALCULADORA = 0;
	public static final int MEUS_CARROS = 1;
	public static final int POSTOS = 2;
	public static final int ABASTECIMENTOS = 3;
	public static final int ESTATISTICAS = 4;
	public static final int INFO = 5;


	Usuario usuario;



	DrawerLayout mDrawer;
	DrawerAdapter adapter;
    TextView txtSessao;
	ListView mListView;
	ActionBarDrawerToggle mDrawerToggle;
	String mTitle;
	private AdView adView;
//

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		txtSessao = (TextView)findViewById(R.id.txtSessao);
		txtSessao.setText("");
		usuario = new Usuario();


		Intent it = getIntent();
		if(it.hasExtra("usuario")) {
			usuario = (Usuario) it.getSerializableExtra("usuario");
		}

		if(usuario != null && usuario.getId() > 0){
			txtSessao.setText("Usuario: "+ usuario.getLogin());
		}

		
		 // Criar o adView
	    adView = new AdView(this, AdSize.BANNER, "ca-app-pub-4671549534107534/6549452408");
	    
	  //AdRequest adRequest = new AdRequest();
	  //adRequest.addTestDevice(AdRequest.TEST_EMULATOR);         // Emulador
	  //adRequest.addTestDevice("TEST_DEVICE_ID");                // Dispositivo Android de teste
	    
	    // Pesquisar seu LinearLayout presumindo que ele foi dado
	    // o atributo android:id="@+id/mainLayout"
	    LinearLayout layout = (LinearLayout)findViewById(R.id.content_frame);

	    // Adicionar o adView a ele
	    layout.addView(adView);

	    // Iniciar uma solicita��o gen�rica para carreg�-lo com um an�ncio
	    adView.loadAd(new AdRequest());
	  
		

		
		// mTitle = getResources().getString(R.string.app_name);
				mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
				mListView = (ListView) findViewById(R.id.left_drawer);

				mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer,
						R.drawable.ic_navigation_drawer, R.string.app_name,
						R.string.app_name) {

					/** Called when a drawer has settled in a completely closed state. */
					public void onDrawerClosed(View view) {
						super.onDrawerClosed(view);
						getSupportActionBar().setTitle(R.string.app_name);
						supportInvalidateOptionsMenu(); // creates call to
														// onPrepareOptionsMenu()
					}

					/** Called when a drawer has settled in a completely open state. */
					public void onDrawerOpened(View drawerView) {
						super.onDrawerOpened(drawerView);
						getSupportActionBar().setTitle(mTitle);
						supportInvalidateOptionsMenu(); // creates call to
														// onPrepareOptionsMenu()
					}
				};
				
				getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				getSupportActionBar().setHomeButtonEnabled(true);

				mDrawer.setDrawerListener(mDrawerToggle);
				String[] mOpcoes = getResources().getStringArray(
						R.array.drawer_options);
				
				List<String> opcoes = new ArrayList<String>();
				//Transformando um Array em List abaixo:				
				opcoes = Arrays.asList(mOpcoes);
				adapter = new DrawerAdapter(opcoes);
				mListView.setAdapter(adapter);
				
				mListView.setOnItemClickListener(this);
	}

	@Override
	public void onDestroy() {
	  if (adView != null) {
	    adView.destroy();
	  }
	  super.onDestroy();
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
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		
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
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		mTitle = mListView.getItemAtPosition(position).toString();
		switch (position){
		
		case CALCULADORA:

			Intent it0 = new Intent(this, CalculadoraActivity.class);
			startActivity(it0);
			mDrawer.closeDrawer(mListView);

		break;

		case MEUS_CARROS:
			if (usuario != null && usuario.getId() > 0) {
				Intent it1 = new Intent(this, VeiculoActivity.class);
				it1.putExtra("usuario", usuario);
				startActivity(it1);
			}else{
				Toast.makeText(this,getResources().getString(R.string.msgPrecisaLogar) , Toast.LENGTH_SHORT).show();
			}
			mDrawer.closeDrawer(mListView);
			
		break;
		
		case POSTOS:

			if (usuario != null && usuario.getId() > 0) {
				Intent it2 = new Intent(this, PostoActivity.class);
				it2.putExtra("usuario", usuario);
				startActivity(it2);
			}else{
				Toast.makeText(this,getResources().getString(R.string.msgPrecisaLogar) , Toast.LENGTH_SHORT).show();
			}
			mDrawer.closeDrawer(mListView);
			
		break;
		
		case ABASTECIMENTOS:

			if (usuario != null && usuario.getId() > 0) {
				Intent it3 = new Intent(this, AbastecimentoActivity.class);
				it3.putExtra("usuario", usuario);
				startActivity(it3);
			}else{
				Toast.makeText(this,getResources().getString(R.string.msgPrecisaLogar) , Toast.LENGTH_SHORT).show();
			}
			mDrawer.closeDrawer(mListView);
			
		break;
		
		case ESTATISTICAS:

			if (usuario != null && usuario.getId() > 0) {
				Intent it4 = new Intent(this, EstatisticasActivity.class);
				it4.putExtra("usuario", usuario);
				startActivity(it4);
			}else{
				Toast.makeText(this,getResources().getString(R.string.msgPrecisaLogar) , Toast.LENGTH_SHORT).show();
			}
			mDrawer.closeDrawer(mListView);
			
		break;

			case INFO:

				Intent it5 = new Intent(this, UsuarioActivity.class);
				if(usuario.getId() > 0) {
					it5.putExtra("usuario", usuario);
				}
				startActivity(it5);
				mDrawer.closeDrawer(mListView);
				finish();
				break;
		
		default:
		mDrawer.closeDrawer(mListView);
		
		
		
		}
		
		
	}



}
