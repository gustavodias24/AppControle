package benicio.solucoes.appcontrole.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import benicio.solucoes.appcontrole.model.DoacaoModel;
import benicio.solucoes.appcontrole.model.FamiliaModel;

public class DoacaoUtil {
    public static final String prefs = "doacao_prefs";
    public static final String name = "doacao_name";

    public static void savedDoacao(List<DoacaoModel> lista, Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences(prefs, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(
                name,
                new Gson().toJson(lista)
        ).apply();
    }

    public static List<DoacaoModel> returnDoacao(Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences(prefs, Context.MODE_PRIVATE);
        return new Gson().fromJson(
                sharedPreferences.getString(name, ""),
                new TypeToken<List<DoacaoModel>>(){}.getType()
        );
    }
}
