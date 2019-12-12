package com.hobbiefinder.ui.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hobbiefinder.R;
import com.hobbiefinder.ui.Hobbie;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class HobbieAdapter extends BaseAdapter {

    private final List<Hobbie> hobbies;
    private final Activity act;

    public HobbieAdapter(List<Hobbie> hobbies, Activity act) {
        this.hobbies = hobbies;
        this.act = act;
    }

    @Override
    public int getCount() {
        return hobbies.size();
    }


    @Override public Object getItem(int position) { return hobbies.get(position); }

    @Override public long getItemId(int position) { return 0; }


    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        final View view = act.getLayoutInflater().inflate(R.layout.lista_curso_personalizada, parent, false);

        final Hobbie hobbie = hobbies.get(position);

        final TextView nome = (TextView) view.findViewById(R.id.lista_curso_personalizada_nome);
        TextView detalhes = (TextView) view.findViewById(R.id.lista_curso_personalizada_detalhes);
        TextView vagas = (TextView) view.findViewById(R.id.lista_curso_personalizada_vagas);


        nome.setText( hobbie.getNome());
        detalhes.setText( hobbie.getDetalhes());
        vagas.setText(hobbie.getVagas().toString());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                inscreveEmHobbie(hobbie.getNome());
                                notificaoInscrito(hobbie.getNome());
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(act);
                builder.setMessage("Tem certeza que deseja participar de "+hobbie.getNome()+"?").setPositiveButton("Sim", dialogClickListener)
                        .setNegativeButton("Não", dialogClickListener).show();
                }
            }
        );

        return view;
    }

    public void inscreveEmHobbie(String hobbieNome)
    {
        RequestQueue queue = Volley.newRequestQueue(act);

        String urlConexao = "http://167.172.114.150:80/insereInscricao.php?hobbie="+hobbieNome;   // link da API ou webpage

        StringRequest stringRequest = new StringRequest
        (
                Request.Method.GET,                                        // Método
                urlConexao,                                                // link (acima)
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                    }
                },
                new Response.ErrorListener()
                {                            // se der erro
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        System.out.println(error.toString());
                    }
                }
        );

        queue.add(stringRequest);
    }

    public void notificaoInscrito(String nameHobbie)
    {

    }


}