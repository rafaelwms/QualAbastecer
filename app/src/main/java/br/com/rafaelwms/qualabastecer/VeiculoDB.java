package br.com.rafaelwms.qualabastecer;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.security.spec.ECFieldF2m;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;



public class VeiculoDB {

    private static final String SERVIDOR = "http://qualabastecer.rafaelwms.com.br/api/";
    private static final String CONTROLADOR = "veiculo/";
    private static final String LISTAR_VEICULOS = "listar/";
    private static final String SALVAR_VEICULO = "salvar";
    private static final String ATUALIZAR_VEICULO = "atualizar";
    private static final String REMOVER_VEICULO = "remover";

    public boolean insertCarro(Veiculo carro) throws Exception{

        HttpURLConnection conexao =	HttpConnector.abrirConexao(SERVIDOR+CONTROLADOR+SALVAR_VEICULO, "POST", true);

        OutputStream os = conexao.getOutputStream();
        os.write(veiculoToJsonByte(carro));
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




    private byte[] veiculoToJsonByte(Veiculo veiculo){
        try{
            JSONObject jsonVeicluo = new JSONObject();
            jsonVeicluo.put("usuario", veiculo.getUsuario().getId());
            jsonVeicluo.put("id", veiculo.getId());
            jsonVeicluo.put("nome", veiculo.getNome());
            jsonVeicluo.put("cor", veiculo.getCor());
            jsonVeicluo.put("tipo", veiculo.getTipo());
            jsonVeicluo.put("combustivel", veiculo.getCombustivel());
            String json = jsonVeicluo.toString();
            return json.getBytes();
        }catch(JSONException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }





    public boolean alterarCarro(Veiculo carro) throws Exception{
        HttpURLConnection conexao =	HttpConnector.abrirConexao(SERVIDOR+CONTROLADOR+ATUALIZAR_VEICULO, "POST", true);

        OutputStream os = conexao.getOutputStream();
        os.write(veiculoToJsonByte(carro));
        os.flush();
        os.close();
        int responseConde = conexao.getResponseCode();
        conexao.disconnect();
        if(responseConde == HttpURLConnection.HTTP_OK)
        {
            return true;
        }else{
            return false;
        }
    }

    public boolean deleteCarro(Veiculo carro) throws Exception{
        HttpURLConnection conexao =	HttpConnector.abrirConexao(SERVIDOR+CONTROLADOR+REMOVER_VEICULO, "POST", true);

        OutputStream os = conexao.getOutputStream();
        os.write(veiculoToJsonByte(carro));
        os.flush();
        os.close();
        int responseConde = conexao.getResponseCode();
        conexao.disconnect();
        if(responseConde == HttpURLConnection.HTTP_OK)
        {
            return true;
        }else{
            return false;
        }

    }

    public List<Veiculo> listarCarros(Usuario usuario){
        try{
            List<Veiculo> lista = new ArrayList<Veiculo>();
            HttpURLConnection conexao =	HttpConnector.abrirConexao(SERVIDOR+CONTROLADOR+LISTAR_VEICULOS+usuario.getId(), "GET", false);
            if(conexao.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream is = conexao.getInputStream();
                String jsonString = HttpConnector.streamToString(is);
                is.close();
                JSONArray json = new JSONArray(jsonString);

                for(int i =0; i < json.length(); i++){
                    JSONObject jsonObj = json.getJSONObject(i);
                    if(jsonObj != null){
                        Veiculo veic = new Veiculo();
                        veic.setUsuario(usuario);
                        veic.setId(jsonObj.getInt("id"));
                        veic.setNome(jsonObj.getString("nome"));
                        veic.setCor(jsonObj.getInt("cor"));
                        veic.setTipo(jsonObj.getInt("tipo"));
                        veic.setCombustivel(jsonObj.getInt("combustivel"));
                        lista.add(veic);
                    }
                }
                conexao.disconnect();
            }
            return lista;
        }catch(Exception ex)
        {
            return null;
        }

    }

}
