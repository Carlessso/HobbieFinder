package com.hobbiefinder.ui.slideshow;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hobbiefinder.R;
import com.hobbiefinder.ui.adapters.CategoriaAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.List;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private TextView tfdNomeCat = null;
    private Button btSaveCategoria = null;
    private Adapter adapeter;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);

        tfdNomeCat = (TextView) root.findViewById(R.id.tfdNomeCategoria);
        btSaveCategoria = (Button) root.findViewById(R.id.btSaveCategoria);

        btSaveCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tfdNomeCat.getText().toString().equals(""))
                {
                    new AlertDialog.Builder(getContext()).setTitle("Aviso").setMessage("Preencha o nome da categoria!").setNegativeButton("Voltar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
//                                    tfdNomeCat.setText("");
                                }
                            }).show();
                    return;
                }

                salvarCategoria();
                new AlertDialog.Builder(getContext()).setTitle("Sucesso").setMessage("Categoria criada com sucesso!").setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        tfdNomeCat.setText("");
                    }
                }).show();
            }
        });

//        slideshowViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(getCategorias());
//            }
//        });


//        initComponents(root);
        return root;
    }

    public  void initComponents(View root)
    {
        RecyclerView recyclerView         = (RecyclerView) root.findViewById(R.id.recicle_cat);
        List<JSONObject> categorias       = getCategorias();
        CategoriaAdapter adapter         = new CategoriaAdapter(categorias);
        LinearLayoutManager layoutManager = new LinearLayoutManager(root.getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void salvarCategoria()
    {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String urlConexao = "http://167.172.114.150:80/insere.php?nome="+this.tfdNomeCat.getText().toString();   // link da API ou webpage

        StringRequest stringRequest = new StringRequest
        (
                Request.Method.GET,                                        // Método
                urlConexao,                                                // link (acima)
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {             // o que fazer com a resposta

                    }
                },
                new Response.ErrorListener()
                {                            // se der erro
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }
                }
        );

        queue.add(stringRequest);

    }

    public List<JSONObject> getCategorias()
    {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String urlConexao = "http://167.172.114.150:80/getCategorias.php";   // link da API ou webpage

        final List<JSONObject> aux = new ArrayList<JSONObject>();

        StringRequest stringRequest = new StringRequest
                (
                        Request.Method.GET,                                        // Método
                        urlConexao,                                                // link (acima)
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response)
                            {
                                try {
                                    JSONArray x = new  JSONArray(response);
                                    for (int i = 0 ; i < x.length(); i++)
                                    {
                                        JSONObject ob = x.getJSONObject(i);
                                        aux.add(ob);
//                                        System.out.println(ob.get("id"));
//                                        System.out.println(ob.get("nome"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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

        return aux;
    }

}