package com.hobbiefinder.ui.home;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.hobbiefinder.ui.Categoria;
import com.hobbiefinder.ui.Hobbie;
import com.hobbiefinder.ui.HttpRequest;
import com.hobbiefinder.ui.adapters.HobbieAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private String jsonURL = "http://167.172.114.150:80/getHobbies.php";
    private final int jsoncode = 1;
    private static ProgressDialog mProgressDialog;
    private ArrayList<Hobbie> modelDataArrayList;
    private ArrayList<String> names = new ArrayList<String>();
    private ListView lista;
    private List<Hobbie> hobbiesList;
    private HobbieAdapter adapter;

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

        lista = (ListView) root.findViewById(R.id.list_hobbies);

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
                names.add(modelDataArrayList.get(i).getNome()+ " - " +modelDataArrayList.get(i).getDetalhes());
            }
//
            String[] strings = new String[names.size()];
//
            for (int i = 0; i < names.size(); i++)
            {
//                System.out.println(names.get(i));
                strings[i] = names.get(i);
            }

//            ArrayAdapter<Hobbie> adapter = new ArrayAdapter<Hobbie>(getContext(), android.R.layout.simple_list_item_1, modelDataArrayList);

              adapter = new HobbieAdapter(modelDataArrayList, getActivity());

              lista.setAdapter(adapter);


//            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(),  android.R.layout.simple_spinner_item, names);
//            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item); // The drop down view
//            spinner.setAdapter(spinnerArrayAdapter);

        }else {
            Toast.makeText(getContext(), getErrorCode(response), Toast.LENGTH_SHORT).show();
        }

    }

    public ArrayList<Hobbie> parseInfo(String response) {
        ArrayList<Hobbie> tennisModelArrayList = new ArrayList<>();
        try {
            JSONArray x = new  JSONArray(response);
            for (int i = 0 ; i < x.length(); i++)
            {
                Hobbie playersModel = new Hobbie();
                JSONObject ob = x.getJSONObject(i);
                playersModel.setId(Integer.parseInt(ob.getString("id")));
                playersModel.setNome(ob.getString("nome"));
                playersModel.setDetalhes(ob.getString("detalhes"));
                playersModel.setVagas(ob.getInt("vagas"));
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