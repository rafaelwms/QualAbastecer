package br.com.rafaelwms.qualabastecer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Util {
	
	
	public static double dividirDoubles(double dividendo, double divisor) throws Exception{
		
		if(divisor == 0){
			throw new Exception();
		}

		return Double.parseDouble(String.format("%.2f", (dividendo / divisor)));
		
	}
	
	public static double multiplicarDoubles(double multiplicando, double multiplicador) throws Exception{

		return Double.parseDouble(String.format("%.2f", (multiplicando * multiplicador)));
		
	}
	
	
	public static Date criarDataBR(String dataFormatada){
		Date data = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
			data = format.parse(dataFormatada);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return data; 
	}
	
	public static Date criarDataUS(String dataFormatada){
		Date data = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss");
			data = format.parse(dataFormatada);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return data; 
	}
	
	public static String formatarData(Date data) {
		return formatarData(data, "dd/MM/yyyy");
	}
	
	public static String formatarDataCompleta(Date data) {
		return formatarData(data, "dd/MM/yyyy - HH:mm:ss");
	}
	
	public static String formatarDataCompletaUS(Date data) {
		return formatarData(data, "yyyy/MM/dd - HH:mm:ss");
	}
	
	
	public static String formatarData(Date data, String formato){
		SimpleDateFormat format;
		String dataFormatada;

		if (data != null) {
			format = new SimpleDateFormat(formato);
			dataFormatada = format.format(data);
		} else {
			dataFormatada = "";
		}
		return dataFormatada;
	}

}
