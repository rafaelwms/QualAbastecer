package br.com.rafaelwms.qualabastecer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QualAbastecerDBHelper extends SQLiteOpenHelper{
	
	public QualAbastecerDBHelper(Context ctx){
		super(ctx, "WhichFuel", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Remover as tabelas quando o sistema esteja completamente WEB

		db.execSQL("CREATE TABLE carro (" +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"nome TEXT NOT NULL UNIQUE, " +
				"tipo INTEGER NOT NULL, " +
				"cor INTEGER NOT NULL, " +
				"combustivel INTEGER NOT NULL);");

		db.execSQL("CREATE TABLE posto (" +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"nome TEXT NOT NULL UNIQUE, " +
				"atendimento INTEGER NOT NULL, " +
				"alcool REAL NOT NULL, " +
				"diesel REAL NOT NULL, " +
				"gasolina REAL NOT NULL);");

		db.execSQL("CREATE TABLE abastecimento (" +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"data TEXT NOT NULL, "+
				"carro INTEGER NOT NULL REFERENCES carro(_id) ON DELETE CASCADE, " +
				"posto INTEGER NOT NULL REFERENCES posto(_id) ON DELETE CASCADE, " +
				"combustivel INTEGER NOT NULL, " +
				"valorpago REAL NOT NULL, " +
				"litros REAL NOT NULL, " +
				"kilometragem REAL);");

		db.execSQL("CREATE TABLE usuario (" +
											"_id INTEGER," +
											"email TEXT NOT NULL," +
											"login TEXT NOT NULL," +
											"senha TEXT NOT NULL," +
											"datacadastro TEXT NOT NULL," +
											"dataalteracao TEXT" +
											");");

		/*
		  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Id do usuário do sistema.',
		  `email` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Email de referencia do usuário.',
		  `login` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
		  `senha` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
		  `datacadastro` datetime NOT NULL,
		  `dataalteracao` datetime DEFAULT NULL,

		*/



	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
