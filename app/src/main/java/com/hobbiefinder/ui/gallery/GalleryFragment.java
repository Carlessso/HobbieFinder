package com.hobbiefinder.ui.gallery;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hobbiefinder.R;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private Button btSaveHobbie = null;
    private TextView tfdNomeHobbie = null;
    private TextView tfdDetalhesHobbie = null;
    private TextView quantidade = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        tfdNomeHobbie = (TextView) root.findViewById(R.id.tfdNomeHobbie);
        tfdDetalhesHobbie = (TextView) root.findViewById(R.id.tfdDetalhesHobbie);
        quantidade = (TextView) root.findViewById(R.id.tfdVagas);

        btSaveHobbie = (Button) root.findViewById(R.id.btSaveHobbie);
        btSaveHobbie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveHobbie();
            }
        });

        return root;
    }

    public void saveHobbie()
    {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String nome = tfdNomeHobbie.getText().toString();
        String detalhes = tfdDetalhesHobbie.getText().toString();
        String vagas = quantidade.getText().toString();
        //categoria

        String urlConexao = "http://167.172.114.150:80/insereHobbie.php?nome="+nome+"&detalhes="+detalhes+
                "&capacidade="+vagas+"&categoria=1";   // link da API ou webpage

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