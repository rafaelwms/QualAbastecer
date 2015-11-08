package br.com.rafaelwms.qualabastecer;

import java.util.List;

import br.com.rafaelwms.qualabastecer.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerAdapter extends BaseAdapter {
	
	private List<String> opcoes;
	
	public DrawerAdapter(List<String> opcs){
		opcoes = opcs;
	}

	@Override
	public int getCount() {
		return opcoes.size();
	}

	@Override
	public Object getItem(int position) {
		return opcoes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		String opcao = opcoes.get(position);
		
		if(convertView == null){
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_drawer_model, null);
		}
		
		ImageView imgDrawer = (ImageView)convertView.findViewById(R.id.imageViewDrawer);
		TextView txtDrawer = (TextView)convertView.findViewById(R.id.textViewDrawer);
		
		switch (position) {
		
		case 0:
			imgDrawer.setImageResource(R.drawable.ic_drawer_calculadora);
			break;
		case 1:
			imgDrawer.setImageResource(R.drawable.ic_drawer_veiculo);
			break;			
		case 2:
			imgDrawer.setImageResource(R.drawable.ic_drawer_posto);
			break;			
		case 3:
			imgDrawer.setImageResource(R.drawable.ic_drawer_pingo);
			break;			
		case 4:
			imgDrawer.setImageResource(R.drawable.ic_drawer_abastecimento);
			break;
		case 5:
			imgDrawer.setImageResource(R.drawable.ic_drawer_info);
			break;
		default:
			break;
		}
		
		txtDrawer.setText(opcao);
		
		
		return convertView;
	}

}
