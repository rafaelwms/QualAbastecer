package br.com.rafaelwms.qualabastecer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rafael on 04/11/2015.
 */
public class UsuarioDB {

    private static final String SERVIDOR = "http://qualabastecer.rafaelwms.com.br/api/";
    private static final String CONTROLADOR = "usuario/";
    private static final String EFETUAR_LOGIN = "login/";
    private static final String CADASTRO = "salvar";


    public boolean cadastrarUsuario(Usuario usuario) throws Exception{

        HttpURLConnection conexao =	HttpConnector.abrirConexao(SERVIDOR+CONTROLADOR+CADASTRO, "POST", true);

        OutputStream os = conexao.getOutputStream();
        os.write(usuarioToJsonByte(usuario));
        os.flush();
        os.close();
        int responseCode = conexao.getResponseCode();
        conexao.disconnect();
        if(responseCode == HttpURLConnection.HTTP_OK)
        {
            return true;
        }else{
            return false;
        }
    }

    public Usuario login(String login, String senha){
        try{
            Usuario usuario = new Usuario();
            HttpURLConnection conexao =	HttpConnector.abrirConexao(SERVIDOR+CONTROLADOR+EFETUAR_LOGIN+login+"/"+senha, "GET", false);
            if(conexao.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream is = conexao.getInputStream();
                String jsonString = HttpConnector.streamToString(is);
                is.close();
                JSONArray json = new JSONArray(jsonString);


                    JSONObject jsonObj = json.getJSONObject(0);
                    if(jsonObj != null){
                        usuario.setId(jsonObj.getInt("id"));
                        usuario.setEmail(jsonObj.getString("email"));
                        usuario.setLogin(jsonObj.getString("login"));
                        usuario.setSenha(jsonObj.getString("senha"));
                }
                conexao.disconnect();
            }
            return usuario;
        }catch(Exception ex)
        {
            return null;
        }

    }


    private byte[] usuarioToJsonByte(Usuario usuario){
        try{
            JSONObject jsonUsuario = new JSONObject();
            jsonUsuario.put("id", usuario.getId());
            jsonUsuario.put("email", usuario.getEmail());
            jsonUsuario.put("login", usuario.getLogin());
            jsonUsuario.put("senha", usuario.getSenha());
            String json = jsonUsuario.toString();
            return json.getBytes();
        }catch(JSONException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }




}
