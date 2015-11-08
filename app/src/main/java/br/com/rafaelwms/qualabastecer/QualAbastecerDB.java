package br.com.rafaelwms.qualabastecer;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class QualAbastecerDB {

	private QualAbastecerDBHelper helper;
	
	public QualAbastecerDB(Context ctx){
		helper = new QualAbastecerDBHelper(ctx);
	}
	
	
	/*
	 * VALORES DE CONTE�DO DOS OBJETOS; 
	 */
	
	
	private ContentValues carroValues(Veiculo carro){
		
		ContentValues values = new ContentValues();
		
		values.put("nome", carro.getNome());
		values.put("tipo", carro.getTipo());
		values.put("cor", carro.getCor());
		values.put("combustivel", carro.getCombustivel());
		
		return values;
	}

	private ContentValues usuarioValues(Usuario usuario){

		ContentValues values = new ContentValues();

		values.put("email", usuario.getEmail());
		values.put("login", usuario.getLogin());
		values.put("senha", usuario.getSenha());
		values.put("_id", usuario.getId());

		return values;
	}

	
	private ContentValues postoValues(Posto posto){
		
		ContentValues values = new ContentValues();
		
		values.put("nome", posto.getNome());
		values.put("atendimento", posto.getAtendimento());
		values.put("alcool", posto.getLitroEtanol());
		values.put("gasolina", posto.getLitroGasolina());
		values.put("diesel", posto.getLitroGasolina());
		
		return values;
	}
	
	
	private ContentValues abastecimentoValues(Abastecimento abs){
		
		ContentValues values = new ContentValues();
		values.put("data", abs.getData());
		values.put("carro", abs.getCarro().getId());
		values.put("posto", abs.getPosto().getId());
		values.put("combustivel", abs.getCombustivel());
		values.put("valorpago", abs.getValorPago());
		values.put("litros", abs.getLitros());
		values.put("kilometragem", abs.getKilometragem());
		
		return values;
	}
	
	/*
	 * INSER��O DOS OBJETOS; 
	 */
	
	public void insertCarro(Veiculo carro) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = carroValues(carro);
		db.insert("carro", null, values);
		db.close();
	}

	public void registrarUsuario(Usuario usuario){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = usuarioValues(usuario);
		db.insert("usuario", null, values);
		db.close();
	}
	
	public void insertPosto(Posto posto) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = postoValues(posto);
		db.insert("posto", null, values);
		db.close();
	}
	
	public void insertAbastecimento(Abastecimento abs) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = abastecimentoValues(abs);
		db.insert("abastecimento", null, values);
		db.close();
	}
	
	public void alterarCarro(Veiculo carro) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = carroValues(carro);
		db.update("carro", values, "_id = ?", new String[]{carro.getId() + ""});
		db.close();
	}

	public void alterarUsuario(Usuario usuario) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = usuarioValues(usuario);
		db.update("usuario", values, "_id = ?", new String[] { usuario.getId()+""});
		db.close();
	}
	
	public void alterarPosto(Posto posto) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = postoValues(posto);
		db.update("posto", values, "_id = ?", new String[]{posto.getId() + ""});
		db.close();
	}
	
	public void alterarAbastecimento(Abastecimento abs) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = abastecimentoValues(abs);
		db.update("abastecimento", values, "_id = ?", new String[]{abs.getId() + ""});
		db.close();
	}
	
	public void deleteCarro(Veiculo carro){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("carro", "_id = ?", new String[]{carro.getId() + ""});
		db.close();
	}

	public void deleteUsuario(Usuario usuario){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("usuario", "_id = ?", new String[] { usuario.getId()+""});
		db.close();
	}
	
	public void deletePosto(Posto posto){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("posto", "_id = ?", new String[] { posto.getId()+""});
		db.close();
	}
	
	public void deleteabastecimento(Abastecimento abs){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("abastecimento", "_id = ?", new String[] { abs.getId()+""});
		db.close();
	}
	
	
	/*
	 * PREENCHIMENTO DOS OBJETOS; 
	 */
	
	
	private Veiculo preencheCarro(Cursor cursor){

		int id = cursor.getInt(cursor.getColumnIndex("_id"));
		String nome = cursor.getString(cursor.getColumnIndex("nome"));
		int tipo = cursor.getInt(cursor.getColumnIndex("tipo"));
		int cor = cursor.getInt(cursor.getColumnIndex("cor"));
		int combustivel = cursor.getInt(cursor.getColumnIndex("combustivel"));

		Veiculo carro = new Veiculo(id, nome, tipo,cor, combustivel);

		return carro;
	}

	private Usuario preencheUsuario(Cursor cursor){
		Usuario usuario = new Usuario();
		usuario.setId(cursor.getInt(cursor.getColumnIndex("_id")));
		usuario.setEmail(cursor.getString(cursor.getColumnIndex("email")));
		usuario.setLogin(cursor.getString(cursor.getColumnIndex("login")));
		usuario.setSenha(cursor.getString(cursor.getColumnIndex("senha")));
		return usuario;
	}
	
	private Posto preenchePosto(Cursor cursor){
		
		int id = cursor.getInt(cursor.getColumnIndex("_id"));
		String nome = cursor.getString(cursor.getColumnIndex("nome"));
		int atendimento = cursor.getInt(cursor.getColumnIndex("atendimento"));
		double alcool = cursor.getDouble(cursor.getColumnIndex("alcool"));
		double gasolina = cursor.getDouble(cursor.getColumnIndex("gasolina"));
		double diesel = cursor.getDouble(cursor.getColumnIndex("diesel"));
		
		Posto posto = new Posto(id, nome, atendimento, gasolina, alcool, diesel);
		
		return posto;
	}
	
	private Abastecimento preencherAbastecimento(Cursor cursor){
		
		int id = cursor.getInt(cursor.getColumnIndex("_id"));
		String data = cursor.getString(cursor.getColumnIndex("data"));
		Veiculo carro = buscarCarroPorId(cursor.getInt(cursor.getColumnIndex("carro")));
		Posto posto = buscarPostoPorId(cursor.getInt(cursor.getColumnIndex("posto")));
		int combustivel = cursor.getInt(cursor.getColumnIndex("combustivel"));
		double valorpago = cursor.getDouble(cursor.getColumnIndex("valorpago"));
		double litros = cursor.getDouble(cursor.getColumnIndex("litros"));
		double km = cursor.getDouble(cursor.getColumnIndex("kilometragem"));
		
		Abastecimento abs= new Abastecimento(id, data, carro, posto, combustivel, valorpago, litros, km);
		
		return abs;
	}
	
	
	
	public Veiculo buscarCarroPorId(int id){
		Veiculo carro = new Veiculo();
		SQLiteDatabase db = helper.getReadableDatabase();
		
		Cursor cursor = db.rawQuery(
				"SELECT * FROM carro WHERE _id = ? ",
				new String[]{id + ""});
		
		if (cursor.moveToFirst() && cursor.getCount() >= 1) {
			
			carro = preencheCarro(cursor);
			cursor.close();
			db.close();
			return carro;
			
		}else{
			cursor.close();
			db.close();
			return null;
		}
	}

	public Usuario buscarUsuarioLogado(){
		Usuario usuario = new Usuario();
		SQLiteDatabase db = helper.getReadableDatabase();

		Cursor cursor = db.rawQuery(
				"SELECT * FROM usuario",
				null);

		if (cursor.moveToFirst() && cursor.getCount() >= 1) {
			usuario = preencheUsuario(cursor);
			cursor.close();
			db.close();
			return usuario;

		}else{
			cursor.close();
			db.close();
			return null;
		}
	}
	
	public Veiculo buscarCarroPorNome(String nome){
		Veiculo carro = new Veiculo();
		SQLiteDatabase db = helper.getReadableDatabase();
		
		Cursor cursor = db.rawQuery(
				"SELECT * FROM carro WHERE nome = ? ",
				new String[] { nome });
		
		if (cursor.moveToFirst() && cursor.getCount() >= 1) {
			
			carro = preencheCarro(cursor);
			cursor.close();
			db.close();
			return carro;
			
		}else{
			cursor.close();
			db.close();
			return null;
		}
	}
	
	public Posto buscarPostoPorId(int id){
		Posto posto = new Posto();
		SQLiteDatabase db = helper.getReadableDatabase();
		
		Cursor cursor = db.rawQuery(
				"SELECT * FROM posto WHERE _id = ? ",
				new String[] { id+""});
		
		if (cursor.moveToFirst() && cursor.getCount() >= 1) {		
			posto = preenchePosto(cursor);
			cursor.close();
			db.close();
			return posto;
			
		}else{
			cursor.close();
			db.close();
			return null;
		}
	}
	
	public Posto buscarPostoPorNome(String nome){
		Posto posto = new Posto();
		SQLiteDatabase db = helper.getReadableDatabase();
		
		Cursor cursor = db.rawQuery(
				"SELECT * FROM posto WHERE nome = ? ",
				new String[] { nome});
		
		if (cursor.moveToFirst() && cursor.getCount() >= 1) {		
			posto = preenchePosto(cursor);
			cursor.close();
			db.close();
			return posto;
			
		}else{
			cursor.close();
			db.close();
			return null;
		}
	}
		
		public List<Veiculo> listarCarros(){
			List<Veiculo> carros = new ArrayList<Veiculo>();
			SQLiteDatabase db = helper.getReadableDatabase();
			
			Cursor cursor = db.rawQuery(
					"SELECT * FROM carro ORDER BY nome", null);
			
			while (cursor.moveToNext()) {
				
				Veiculo carro = preencheCarro(cursor);
				carros.add(carro);
			}
				cursor.close();
				db.close();
				return carros;
			}
		
		
		public List<Posto> listarPostos(){
			List<Posto> postos = new ArrayList<Posto>();
			SQLiteDatabase db = helper.getReadableDatabase();
			
			Cursor cursor = db.rawQuery(
					"SELECT * FROM posto ORDER BY nome", null);
			
			while (cursor.moveToNext()) {
				
				Posto posto = preenchePosto(cursor);
				postos.add(posto);
			}
				cursor.close();
				db.close();
				return postos;
			}
		
		public List<Abastecimento> listarAbastecimentos(){
			List<Abastecimento> abss = new ArrayList<Abastecimento>();
			SQLiteDatabase db = helper.getReadableDatabase();
			
			Cursor cursor = db.rawQuery(
					"SELECT * FROM abastecimento", null);
			
			while (cursor.moveToNext()) {	
				Abastecimento abs = preencherAbastecimento(cursor);
				abss.add(abs);
			}
				cursor.close();
				db.close();
				return abss;
			}
	
		public List<Abastecimento> listarAbastecimentosPorCarro(Veiculo carro){
			List<Abastecimento> abss = new ArrayList<Abastecimento>();
			SQLiteDatabase db = helper.getReadableDatabase();
			
			Cursor cursor = db.rawQuery(
					"SELECT * FROM abastecimento WHERE carro = ?", new String[]{String.valueOf(carro.getId())});
			
			while (cursor.moveToNext()) {	
				Abastecimento abs = preencherAbastecimento(cursor);
				abss.add(abs);
			}
				cursor.close();
				db.close();
				return abss;
			}
		
		public List<Abastecimento> listarAbastecimentosPorPosto(Posto posto){
			List<Abastecimento> abss = new ArrayList<Abastecimento>();
			SQLiteDatabase db = helper.getReadableDatabase();
			
			Cursor cursor = db.rawQuery(
					"SELECT * FROM abastecimento WHERE posto = ?", new String[]{String.valueOf(posto.getId())});
			
			while (cursor.moveToNext()) {	
				Abastecimento abs = preencherAbastecimento(cursor);
				abss.add(abs);
			}
				cursor.close();
				db.close();
				return abss;
			}
		
		public List<Abastecimento> listarAbastecimentosPorCombustivel(int combustivel){
			List<Abastecimento> abss = new ArrayList<Abastecimento>();
			SQLiteDatabase db = helper.getReadableDatabase();
			
			Cursor cursor = db.rawQuery(
					"SELECT * FROM abastecimento WHERE combustivel = ?", new String[]{String.valueOf(combustivel)});
			
			while (cursor.moveToNext()) {	
				Abastecimento abs = preencherAbastecimento(cursor);
				abss.add(abs);
			}
				cursor.close();
				db.close();
				return abss;
			}
		
		public List<Abastecimento> listarAbastecimentosPorValorMaiorIgual(double valor){
			List<Abastecimento> abss = new ArrayList<Abastecimento>();
			SQLiteDatabase db = helper.getReadableDatabase();

				Cursor cursor = db.rawQuery("SELECT * FROM abastecimento WHERE valorpago >= ?", new String[]{String.valueOf(valor)});	
			
			while (cursor.moveToNext()) {	
				Abastecimento abs = preencherAbastecimento(cursor);
				abss.add(abs);
			}
				cursor.close();
				db.close();
				return abss;
			}
		
		public List<Abastecimento> listarAbastecimentosPorKilometragem(int veiculo, double valor){
			List<Abastecimento> abss = new ArrayList<Abastecimento>();
			SQLiteDatabase db = helper.getReadableDatabase();

				Cursor cursor = db.rawQuery("SELECT * FROM abastecimento WHERE carro = ? AND kilometragem > ?", new String[]{String.valueOf(veiculo), String.valueOf(valor)});	
			
			while (cursor.moveToNext()) {	
				Abastecimento abs = preencherAbastecimento(cursor);
				abss.add(abs);
			}
				cursor.close();
				db.close();
				return abss;
			}
		
		public List<Abastecimento> listarAbastecimentosPorValorMenorIgual(double valor){
			List<Abastecimento> abss = new ArrayList<Abastecimento>();
			SQLiteDatabase db = helper.getReadableDatabase();

				Cursor cursor = db.rawQuery("SELECT * FROM abastecimento WHERE valorpago <= ?", new String[]{String.valueOf(valor)});	
			
			while (cursor.moveToNext()) {	
				Abastecimento abs = preencherAbastecimento(cursor);
				abss.add(abs);
			}
				cursor.close();
				db.close();
				return abss;
			}
}
