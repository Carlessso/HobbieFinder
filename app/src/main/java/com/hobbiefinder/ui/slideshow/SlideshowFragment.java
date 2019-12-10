package com.hobbiefinder.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hobbiefinder.R;

import java.util.ArrayList;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private TextView tfdNomeCat = null;
    private Button btSaveCategoria = null;

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
                salvarCategoria();
            }
        });

        return root;
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
}