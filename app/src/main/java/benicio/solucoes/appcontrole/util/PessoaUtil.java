package benicio.solucoes.appcontrole.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import benicio.solucoes.appcontrole.model.LocalidadeModel;
import benicio.solucoes.appcontrole.model.PessoaModel;

public class PessoaUtil {
    public static final String prefs = "pessoa_prefs";
    public static final String name = "pessoa_name";

    public static void savedPessoa(List<PessoaModel> lista, Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences(prefs, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(
                name,
                new Gson().toJson(lista)
        ).apply();
    }

    public static List<PessoaModel> returnPessoa(Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences(prefs, Context.MODE_PRIVATE);
        return new Gson().fromJson(
                sharedPreferences.getString(name, ""),
                new TypeToken<List<PessoaModel>>(){}.getType()
        );
    }
}
