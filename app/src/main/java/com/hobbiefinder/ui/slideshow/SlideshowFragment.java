package com.hobbiefinder.ui.slideshow;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.hobbiefinder.ui.Categoria;
import com.hobbiefinder.ui.HttpRequest;
import com.hobbiefinder.ui.adapters.CategoriaAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private TextView tfdNomeCat = null;
    private Button btSaveCategoria = null;
    private Adapter adapeter;
    private String jsonURL = "http://167.172.114.150:80/getCategorias.php";
    private final int jsoncode = 1;
    private static ProgressDialog mProgressDialog;
    private ArrayList<Categoria> modelDataArrayList;
    private ArrayList<String> names = new ArrayList<String>();

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
////            @Override
////            public void onChanged(@Nullable String s) {
////                textView.setText(getCategorias());
////            }
////        });

//        loadJSON();

//        initComponents(root);

        return root;
    }

    @SuppressLint("StaticFieldLeak")
    private void loadJSON(){

        showSimpleProgressDialog(getContext(), "Loading...","",false);

        new AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] params) {
                String response="";
                HashMap<String, String> map=new HashMap<>();
                try {
                    HttpRequest req = new HttpRequest(jsonURL);
                    response = req.prepare(HttpRequest.Method.POST).withData(map).sendAndReadString();
                } catch (Exception e) {
                    response=e.getMessage();
                }
                return response;
            }
            protected void onPostExecute(String result) {
                //do something with response
//                Log.d("newwwss",result);
                onTaskCompleted(result,jsoncode);
            }
        }.execute();
    }

    public void onTaskCompleted(String response, int serviceCode) {

        if (isSuccess(response)) {
            removeSimpleProgressDialog();  //will remove progress dialog

            modelDataArrayList = parseInfo(response);

            // Application of the Array to the Spinner

            for (int i = 0; i < modelDataArrayList.size(); i++){
                names.add(modelDataArrayList.get(i).getName());
            }


        }else {
            Toast.makeText(getContext(), getErrorCode(response), Toast.LENGTH_SHORT).show();
        }

    }

    public ArrayList<Categoria> parseInfo(String response) {
        ArrayList<Categoria> tennisModelArrayList = new ArrayList<>();
        try {
            JSONArray x = new  JSONArray(response);
            for (int i = 0 ; i < x.length(); i++)
            {
                Categoria playersModel = new Categoria();
                JSONObject ob = x.getJSONObject(i);
                playersModel.setId(Integer.parseInt(ob.getString("id")));

                playersModel.setName(ob.getString("nome"));
                tennisModelArrayList.add(playersModel);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tennisModelArrayList;
    }

    public boolean isSuccess(String response) {

        if (response.equals("")) {
            return false;
        }

        return true;
    }

    public String getErrorCode(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject.getString("message");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "No data";
    }

    public static void removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();

        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showSimpleProgressDialog(Context context, String title,
                                                String msg, boolean isCancelable) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(context, title, msg);
                mProgressDialog.setCancelable(isCancelable);
            }

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void initComponents(View root)
    {
        RecyclerView recyclerView         = (RecyclerView) root.findViewById(R.id.recicle_cat);
        List<String> categorias           = names;
        CategoriaAdapter adapter          = new CategoriaAdapter(categorias);
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