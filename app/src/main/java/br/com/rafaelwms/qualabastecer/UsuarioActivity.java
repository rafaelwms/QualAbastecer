package br.com.rafaelwms.qualabastecer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UsuarioActivity extends ActionBarActivity implements View.OnClickListener {

    EditText edtEmail;
    EditText edtLogin;
    EditText edtSenha;
    Button btnCadastrar;
    Button btnLogin;
    Usuario usuario;
    ProgressDialog pDialog;
    private final int CADASTRAR = 1;
    private final int LOGAR = 2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        edtEmail = (EditText)findViewById(R.id.edtEmail);
        edtLogin = (EditText)findViewById(R.id.edtLogin);
        edtSenha = (EditText)findViewById(R.id.edtPass);
        btnCadastrar = (Button)findViewById(R.id.btnCadastrar);
        btnLogin = (Button)findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(this);
        btnCadastrar.setOnClickListener(this);

        Intent it = getIntent();
        if(it.hasExtra("usuario")){
            usuario = (Usuario) it.getSerializableExtra("usuario");
        }

        atualizarCamposActivity();

    }

    public void atualizarCamposActivity() {
        if(usuario != null && usuario.getId() > 0){
            edtEmail.setVisibility(View.INVISIBLE);
            edtLogin.setVisibility(View.INVISIBLE);
            edtSenha.setVisibility(View.INVISIBLE);
            btnLogin.setText(usuario.getLogin()+" "+ getResources().getString(R.string.btnSair));
        }else {
            edtEmail.setVisibility(View.VISIBLE);
            edtLogin.setVisibility(View.VISIBLE);
            edtSenha.setVisibility(View.VISIBLE);
            btnLogin.setText(getResources().getString(R.string.btnLogin));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_usuario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

        private void cadastrarUsuario(){
            usuario = new Usuario();
            usuario.setEmail(edtEmail.getText().toString());
            usuario.setLogin(edtLogin.getText().toString());
            usuario.setSenha(edtSenha.getText().toString());

            new UsuarioAsyncTasks(CADASTRAR).execute();
        }

        public void registrarUsuario()
        {
            SharedPreferences prefs = getSharedPreferences("infologin", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("id", usuario.getId());
            editor.putString("login", usuario.getLogin());
            editor.commit();
        }

    @Override
    public void onClick(View v) {
       if(v.getId() == R.id.btnCadastrar) {
           cadastrarUsuario();
       }
        if(v.getId() ==  R.id.btnLogin){
            if(usuario == null || usuario.getId() == 0) {
                usuario = new Usuario();
                usuario.setLogin(edtLogin.getText().toString());
                usuario.setSenha(edtSenha.getText().toString());
                new UsuarioAsyncTasks(LOGAR).execute();
            }else{
                usuario = new Usuario();
            }
            atualizarCamposActivity();
        }

    }

    public void retornarMainActivity(){
        Intent it = new Intent(this, MainActivity.class);
        if(usuario.getId() > 0) {
            it.putExtra("usuario", usuario);
        }
        startActivity(it);
        finish();
    }

    public void emitirMensagem(String mensagem){
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

    public void limparCampos()
    {
        edtLogin.setText("");
        edtSenha.setText("");
        edtEmail.setText("");
    }

    class UsuarioAsyncTasks extends AsyncTask<String, String, String>
    {

        private int action;
        private final int CADASTRAR = 1;
        private final int LOGAR = 2;

        public UsuarioAsyncTasks(int action){
            this.action = action;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UsuarioActivity.this);
            pDialog.setMessage("Contactando Servidor...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                UsuarioDB udb = new UsuarioDB();
                switch (action){
                    case CADASTRAR:
                        boolean cadastro = udb.cadastrarUsuario(usuario);
                      if (cadastro)
                      {
                          emitirMensagem("Usuário cadastrado cm êxito.");
                      }
                        usuario =  udb.login(usuario.getLogin(), usuario.getSenha());

                        break;
                    case LOGAR:
                        usuario =  udb.login(usuario.getLogin(), usuario.getSenha());

                        break;
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done

            pDialog.dismiss();
            limparCampos();
            retornarMainActivity();

        }


    }



}
