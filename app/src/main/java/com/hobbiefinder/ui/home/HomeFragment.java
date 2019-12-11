package com.hobbiefinder.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import com.hobbiefinder.ui.adapters.HobbieAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        ListView lista = (ListView) root.findViewById(R.id.list_hobbies);

        String[] strings = new String[] { "nome1", "nome2", "nome3", "nome4" };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,strings);

        lista.setAdapter(adapter);

        return root;
    }

    public List<JSONObject> getHobbies(final ArrayAdapter<String> adapter)
    {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String urlConexao = "http://167.172.114.150:80/getHobbies.php";   // link da API ou webpage

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
                                adapter.add(ob.toString());
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