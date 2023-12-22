package benicio.solucoes.appcontrole;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import benicio.solucoes.appcontrole.adapter.AdapterLocalidade;
import benicio.solucoes.appcontrole.adapter.AdapterPessoa;
import benicio.solucoes.appcontrole.databinding.ActivityLocalidadeBinding;
import benicio.solucoes.appcontrole.databinding.ActivityPessoaBinding;
import benicio.solucoes.appcontrole.databinding.DialogCadastroLocalidadeLayoutBinding;
import benicio.solucoes.appcontrole.databinding.DialogCadastroPessoaLayoutBinding;
import benicio.solucoes.appcontrole.databinding.LoadingLayoutBinding;
import benicio.solucoes.appcontrole.databinding.TakeProfileImageLayoutBinding;
import benicio.solucoes.appcontrole.model.LocalidadeModel;
import benicio.solucoes.appcontrole.model.PessoaModel;
import benicio.solucoes.appcontrole.model.ResponseIngurModel;
import benicio.solucoes.appcontrole.services.ServiceIngur;
import benicio.solucoes.appcontrole.util.ImageUtils;
import benicio.solucoes.appcontrole.util.LocalidadeUtil;
import benicio.solucoes.appcontrole.util.PessoaUtil;
import benicio.solucoes.appcontrole.util.RetrofitUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PessoaActivity extends AppCompatActivity {

    String linkImage = "";
    Uri imageUri;
    Retrofit retrofitIngur = RetrofitUtils.createRetrofitIngur();
    ServiceIngur serviceIngur = RetrofitUtils.createServiceIngur(retrofitIngur);
    ImageView profileImage;
    private static final String TOKEN = "282c6d7932402b7665da78ee7c51311556ce6c8a";
    private static final int PERMISSON_CODE = 1000;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private static final int PICK_IMAGE_REQUEST = 2;
    private Dialog  dialogSelecionarFoto, dialogCarregando;
    ActivityPessoaBinding mainBinding;
    RecyclerView r;
    AdapterPessoa adapter;
    List<PessoaModel> lista = new ArrayList<>();

    Bundle b;
    String idFamilia;
    Dialog d;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityPessoaBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Cadastro pessoa");

        b = getIntent().getExtras();
        idFamilia = b.getString("idFamilia", "");

        Log.d("familiaID", "onCreate: " + idFamilia);

        configurarDialog();
        configurarRecycler();
        configurarRecycler();
        configurarDialogSelecionarFoto();
        configurarDialogCarregando();
        mainBinding.fabAdd.setOnClickListener( view -> d.show());

    }

    private void configurarDialogCarregando() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setView(LoadingLayoutBinding.inflate(getLayoutInflater()).getRoot());
        dialogCarregando = b.create();
    }

    private void configurarDialogSelecionarFoto() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        TakeProfileImageLayoutBinding cameraOrGalery = TakeProfileImageLayoutBinding.inflate(getLayoutInflater());
        b.setTitle("Selecione: ");

        cameraOrGalery.btnCamera.setOnClickListener( view -> {
            baterFoto();
            dialogSelecionarFoto.dismiss();
        });

        cameraOrGalery.btnGaleria.setOnClickListener( view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
            dialogSelecionarFoto.dismiss();
        });

        b.setView(cameraOrGalery.getRoot());
        dialogSelecionarFoto = b.create();
    }

    private void configurarRecycler() {
        r = mainBinding.recyclerPessoa;
        r.setLayoutManager(new LinearLayoutManager(this));
        r.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        r.setHasFixedSize(true);

        listarPessoaDeFamilia();

        adapter = new AdapterPessoa(lista, this);
        r.setAdapter(adapter);
    }

    public void baterFoto(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if ( checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                String[] permissions = {android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSON_CODE);
            }
            else {
                // already permisson
                openCamera();
            }
        }
        else{
            // system < M
            openCamera();
        }
    }

    public void openCamera(){
        ContentValues values  = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "nova picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Imagem tirada da câmera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intentCamera =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intentCamera, REQUEST_IMAGE_CAPTURE);
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            dialogCarregando.show();
            File imageFile = ImageUtils.uriToFile(getApplicationContext(), imageUri);
            RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "Image Description");
            RequestBody image = RequestBody.create(MediaType.parse("image/png"), imageFile);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), image);

            serviceIngur.postarImage("Bearer " + TOKEN, description, imagePart).enqueue(new Callback<ResponseIngurModel>() {
                @Override
                public void onResponse(Call<ResponseIngurModel> call, Response<ResponseIngurModel> response) {
                    if (response.isSuccessful()){
                        assert response.body() != null;
                        Picasso.get().load(response.body().getData().getLink()).into(
                                profileImage
                        );

                        dialogCarregando.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseIngurModel> call, Throwable t) {
                    Picasso.get().load(imageUri).into(
                            profileImage
                    );
                    dialogCarregando.dismiss();
                }
            });
        }
        else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            dialogCarregando.show();
            File imageFile = ImageUtils.uriToFile(getApplicationContext(), Objects.requireNonNull(data.getData()));
            RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "Image Description");
            assert imageFile != null;
            RequestBody image = RequestBody.create(MediaType.parse("image/png"), imageFile);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), image);

            serviceIngur.postarImage("Bearer " + TOKEN, description, imagePart).enqueue(new Callback<ResponseIngurModel>() {
                @Override
                public void onResponse(Call<ResponseIngurModel> call, Response<ResponseIngurModel> response) {
                    if (response.isSuccessful()){
                        assert response.body() != null;
                        Picasso.get().load(response.body().getData().getLink()).into(
                                profileImage
                        );
                        dialogCarregando.dismiss();

                    }
                }

                @Override
                public void onFailure(Call<ResponseIngurModel> call, Throwable t) {
                    Picasso.get().load(imageUri).into(
                            profileImage
                    );
                    dialogCarregando.dismiss();
                }
            });
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if ( requestCode == PERMISSON_CODE){
            if( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCamera();
            }else{
                Toast.makeText(this, "Conceda as permissões!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint({"NotifyDataSetChanged", "ResourceType"})
    private void configurarDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        DialogCadastroPessoaLayoutBinding dialogBinding = DialogCadastroPessoaLayoutBinding.inflate(getLayoutInflater());

        profileImage =dialogBinding.profileImage;

        dialogBinding.profileImage.setOnClickListener( view -> dialogSelecionarFoto.show() );

        dialogBinding.cadastrar.setOnClickListener( view -> {
            String id = UUID.randomUUID().toString();

            List<PessoaModel> listaSave = new ArrayList<>();

            if (PessoaUtil.returnPessoa(this) != null){
                listaSave.addAll(PessoaUtil.returnPessoa(this));
            }
            String imageUriString2 = "";
            if (imageUri != null){
                imageUriString2 = imageUri.toString();
            }
            listaSave.add(
                    new PessoaModel(
                            id,
                            idFamilia,
                            dialogBinding.edtNome.getText().toString(),
                            dialogBinding.edtNasc.getText().toString(),
                            dialogBinding.edtUnif.getText().toString(),
                            dialogBinding.edtMuni.getText().toString(),
                            dialogBinding.edtZona.getText().toString(),
                            dialogBinding.edtSecao.getText().toString(),
                            dialogBinding.edtNumero.getText().toString(),
                            dialogBinding.edtDataEmissao.getText().toString(),
                            dialogBinding.edtIdade.getText().toString(),
                            dialogBinding.edtRg.getText().toString(),
                            dialogBinding.edtCpf.getText().toString(),
                            linkImage,
                            imageUriString2
                            )
            );

                    Picasso.get().load(R.raw.imagedefault).into(profileImage);
                    dialogBinding.edtNome.setText("");
                    dialogBinding.edtNasc.setText("");
                    dialogBinding.edtUnif.setText("");
                    dialogBinding.edtMuni.setText("");
                    dialogBinding.edtZona.setText("");
                    dialogBinding.edtSecao.setText("");
                    dialogBinding.edtNumero.setText("");
                    dialogBinding.edtDataEmissao.setText("");
                    dialogBinding.edtIdade.setText("");
                    dialogBinding.edtRg.setText("");
                    dialogBinding.edtCpf.setText("");

            PessoaUtil.savedPessoa(listaSave, this);

            Toast.makeText(this, "Cadastro com sucesso!", Toast.LENGTH_SHORT).show();
            dialogBinding.edtNome.setText("");
            d.dismiss();
            mainBinding.textInfo.setVisibility(View.GONE);

            listarPessoaDeFamilia();
            adapter.notifyDataSetChanged();

        });


        b.setView(dialogBinding.getRoot());
        d = b.create();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ( item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void listarPessoaDeFamilia(){
        lista.clear();
        if ( PessoaUtil.returnPessoa(this) != null){
            for ( PessoaModel pessoaModel : PessoaUtil.returnPessoa(this)){
                if( pessoaModel.getIdFamilia().equals(idFamilia)){
                    lista.add(pessoaModel);
                    mainBinding.textInfo.setVisibility(View.GONE);
                }
            }
        }
    }
}