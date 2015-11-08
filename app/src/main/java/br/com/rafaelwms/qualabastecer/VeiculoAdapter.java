package br.com.rafaelwms.qualabastecer;

import java.util.List;

import br.com.rafaelwms.qualabastecer.R;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VeiculoAdapter extends BaseAdapter {

	private List<Veiculo> carros;

	public VeiculoAdapter(List<Veiculo> cars) {
		carros = cars;
	}

	@Override
	public int getCount() {
		return carros.size();
	}

	@Override
	public Object getItem(int position) {
		return carros.get(position);
	}

	@Override
	public long getItemId(int position) {
		return carros.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Veiculo carro = carros.get(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_carros_model, null);
		}

		ImageView imgCarro = (ImageView) convertView.findViewById(R.id.imgCarroList);
		ImageView imgComb = (ImageView) convertView.findViewById(R.id.imgVeiculoComb);
		TextView txtNomeCarro = (TextView) convertView.findViewById(R.id.txtNomeCarroList);
		TextView txtCombCarro = (TextView) convertView.findViewById(R.id.txtCarroComb);

		imgCarro.setBackgroundColor(carro.getCor());
		
		switch (carro.getTipo()) {
		case 1:
			imgCarro.setImageResource(R.drawable.ic_carro);
			break;
		case 2:
			imgCarro.setImageResource(R.drawable.ic_moto);
			break;	
		case 3:
			imgCarro.setImageResource(R.drawable.ic_caminhao);
			break;
		case 4:
			imgCarro.setImageResource(R.drawable.ic_onibus);
			break;
		default:
			break;
		}
		
		if(carro.getCombustivel() == 0){
			imgComb.setBackgroundColor(Color.rgb(255, 128, 0));
		}else if(carro.getCombustivel() == 1){
			imgComb.setBackgroundColor(Color.rgb(0, 160, 0));
		}else if(carro.getCombustivel() == 2){
			imgComb.setBackgroundColor(Color.rgb(0, 128, 255));
		}else if(carro.getCombustivel() == 3){
			imgComb.setBackgroundColor(Color.rgb(108, 55, 0));
		}
		
		
		txtNomeCarro.setText(carro.getNome().toString());
		if (carro.getCombustivel() == 0) {
			txtCombCarro.setText(parent.getResources().getString(R.string.gasResult));
		} else if (carro.getCombustivel() == 1) {
			txtCombCarro.setText(parent.getResources().getString(R.string.ethResult));
		} else if (carro.getCombustivel() == 2) {
			txtCombCarro.setText(parent.getResources().getString(R.string.flexResult));
		}else if (carro.getCombustivel() == 3) {
			txtCombCarro.setText(parent.getResources().getString(R.string.dieselResult));
		}
		return convertView;
	}

}
