package com.hobbiefinder.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hobbiefinder.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class HobbieAdapter extends BaseAdapter {

    private final List<JSONObject> hobbies;
    private final Activity act;

    public HobbieAdapter(List<JSONObject> hobbies, Activity act) {
        this.hobbies = hobbies;
        this.act = act;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = act.getLayoutInflater().inflate(R.layout.lista_curso_personalizada, parent, false);

        JSONObject hobbie = hobbies.get(position);

        TextView nome = (TextView) view.findViewById(R.id.lista_curso_personalizada_nome);
        TextView descricao = (TextView) view.findViewById(R.id.lista_curso_personalizada_descricao);
//        ImageView imagem = (ImageView) view.findViewById(R.id.lista_curso_personalizada_imagem);

        nome.setText("nome");
        descricao.setText("detalhes");

        //populando as Views
//        try {
//            nome.setText((Integer) hobbie.get("nome"));
//            descricao.setText((Integer) hobbie.get("detalhes"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        imagem.setImageResource(R.drawable.java);

        return view;
    }


}