package br.com.rafaelwms.qualabastecer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.*;

public class HttpConnector {


    public static HttpURLConnection abrirConexao(String url, String method, boolean doOutput) throws Exception{

        URL urlCon = new URL(url);
        HttpURLConnection conexao = (HttpURLConnection)urlCon.openConnection();
        conexao.setReadTimeout(15000);
        conexao.setConnectTimeout(15000);
        conexao.setRequestMethod(method);
        conexao.setDoInput(true);
        conexao.setDoOutput(doOutput);
        if(doOutput){
            conexao.addRequestProperty("Content-Type", "application/json");
        }
        conexao.connect();
        return conexao;
    }

    public static String streamToString(InputStream is) throws IOException{
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int lidos;
        while((lidos = is.read(bytes)) > 0){
            baos.write(bytes, 0, lidos);
        }
        return new String(baos.toByteArray());
    }

}
