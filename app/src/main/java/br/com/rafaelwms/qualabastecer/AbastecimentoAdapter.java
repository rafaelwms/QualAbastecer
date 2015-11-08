package br.com.rafaelwms.qualabastecer;

import java.util.Date;
import java.util.List;

import br.com.rafaelwms.qualabastecer.R;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AbastecimentoAdapter extends BaseAdapter {
	
	private List<Abastecimento> abastecimentos;
	
	public AbastecimentoAdapter(List<Abastecimento> abss){
		abastecimentos = abss;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return abastecimentos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return abastecimentos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return abastecimentos.get(position).getId();
	}

	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
	
		
		Abastecimento abs = abastecimentos.get(position);
		
		if(convertView == null){
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_abastecimento_model, null);
		}
		
		ImageView imgComb = (ImageView)convertView.findViewById(R.id.imgAbsComb);
		TextView txtAbsData = (TextView)convertView.findViewById(R.id.txtAbsData);
		TextView txtAbsVeiculo = (TextView)convertView.findViewById(R.id.txtAbsVeiculo);
		TextView txtAbsPosto = (TextView)convertView.findViewById(R.id.txtAbsPosto);
		
		if(abs.getCombustivel() == 0){
			imgComb.setBackgroundColor(Color.rgb(255, 128, 0));
		}else if(abs.getCombustivel() == 1){
			imgComb.setBackgroundColor(Color.rgb(0, 160, 0));
		}else if(abs.getCombustivel() == 3){
			imgComb.setBackgroundColor(Color.rgb(108, 55, 0));
		}
		
		/*
		
				TENTANDO FORMATAR A DATA PRA VISUALIZAÇÃO REGIONAL

		if(parent.getResources().getString(R.string.id_lang).equals("PORTUGUÊS")){
			
			Date dataUS = Util.criarDataBR(abs.getData());
			
			String data = Util.formatarDataCompleta(dataUS);
			
			abs.setData(data);
			
		}
		*/
		
		txtAbsData.setText(parent.getResources().getString(R.string.data)+" "+abs.getData()+" - "+parent.getResources().getString(R.string.cifra)+" "+abs.getValorPago());
		txtAbsVeiculo.setText(parent.getResources().getString(R.string.veiculoDP)+" "+abs.getCarro().getNome()+" - "+parent.getResources().getString(R.string.kilometragemDP)+" "+abs.getKilometragem());
		txtAbsPosto.setText(parent.getResources().getString(R.string.postoDP)+" "+abs.getPosto().getNome()+" - "+parent.getResources().getString(R.string.litros)+" "+abs.getLitros());
		
		
		return convertView;
	}

}
