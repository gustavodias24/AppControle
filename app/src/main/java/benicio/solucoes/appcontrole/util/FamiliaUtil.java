package benicio.solucoes.appcontrole.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import benicio.solucoes.appcontrole.model.FamiliaModel;

public class FamiliaUtil {
    public static final String prefs = "familia_prefs";
    public static final String name = "localidade_name";

    public static void savedFamilia(List<FamiliaModel> lista, Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences(prefs, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(
                name,
                new Gson().toJson(lista)
        ).apply();
    }

    public static List<FamiliaModel> returnFamilia(Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences(prefs, Context.MODE_PRIVATE);
        return new Gson().fromJson(
                sharedPreferences.getString(name, ""),
                new TypeToken<List<FamiliaModel>>(){}.getType()
        );
    }
}
