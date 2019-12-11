package com.hobbiefinder.ui.gallery;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import com.hobbiefinder.ui.Categoria;
import com.hobbiefinder.ui.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private Button btSaveHobbie = null;
    private TextView tfdNomeHobbie = null;
    private TextView tfdDetalhesHobbie = null;
    private TextView quantidade = null;
    private Spinner spnCategorias = null;
    private String jsonURL = "http://167.172.114.150:80/getCategorias.php";
    private final int jsoncode = 1;
    private static ProgressDialog mProgressDialog;
    private ArrayList<Categoria> modelDataArrayList;
    private ArrayList<String> names = new ArrayList<String>();
    private Spinner spinner;



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
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (tfdNomeHobbie.getText().toString().equals("") ||
                        tfdDetalhesHobbie.getText().toString().equals("") ||
                        quantidade.getText().toString().equals("") ||
                         spinner.getSelectedItem().toString().equals("")
                          )
                {
                    new AlertDialog.Builder(getContext()).setTitle("Aviso").setMessage("Preencha todos os campos!").setNegativeButton("Voltar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
//                                    tfdNomeCat.setText("");
                                }
                            }).show();
                    return;
                }
                saveHobbie();
                new AlertDialog.Builder(getContext()).setTitle("Aviso").setMessage("Hobbie criado com sucesso!").setNegativeButton("ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
//                                    tfdNomeCat.setText("");
                            }
                        }).show();
                return;
            }
        });

        spinner = (Spinner) root.findViewById(R.id.spnCategoria);

        loadJSON();

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

            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(),  android.R.layout.simple_spinner_item, names);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item); // The drop down view
            spinner.setAdapter(spinnerArrayAdapter);

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void saveHobbie()
    {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String nome = tfdNomeHobbie.getText().toString();
        String detalhes = tfdDetalhesHobbie.getText().toString();
        String vagas = quantidade.getText().toString();

        String categoria = spinner.getSelectedItem().toString();

        String urlConexao = "http://167.172.114.150:80/insereHobbie.php?nome="+nome+"&detalhes="+detalhes+
                "&capacidade="+vagas+"&categoria="+categoria;   // link da API ou webpage

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