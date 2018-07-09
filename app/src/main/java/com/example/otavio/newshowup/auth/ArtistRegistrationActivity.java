package com.example.otavio.newshowup.auth;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.otavio.newshowup.R;
import com.example.otavio.newshowup.artista.HomeArtistaActivity;
import com.example.otavio.newshowup.utils.ArrayString;
import com.example.otavio.newshowup.utils.CircleTransform;
import com.example.otavio.newshowup.utils.Firebase;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArtistRegistrationActivity extends AppCompatActivity {

    @BindView(R.id.img_profile)ImageView img_artista;
    @BindView(R.id.editNome)EditText editNome;
    @BindView(R.id.editEstado)EditText editEstado;
    @BindView(R.id.editCidade)AutoCompleteTextView autoCompleteTextView;
    @BindView(R.id.spinner_genero)Spinner spinner_genero;
    @BindView(R.id.spinner_faixa_preco)Spinner spinner_faixa_preco;
    @BindView(R.id.editTelefone)EditText editTelefone;
    @BindView(R.id.btn_next)Button btn_next;
    private String picturePath;
    private static final int RESULT_TAKE_PICTURE = 123;
    private static final int RESULT_PICK_PICTURE = 283;
    private static final int MY_PERMISSIONS = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_registration);
        ButterKnife.bind(this);
        ArrayAdapter<String> adapterGenero=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, ArrayString.generos);
        spinner_genero.setAdapter(adapterGenero);
        ArrayAdapter<String> adapterPreco=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, ArrayString.faixas_preco);
        spinner_faixa_preco.setAdapter(adapterPreco);

        final String[] genero = new String[1];
        final String[] faixa_preco = new String[1];
        spinner_genero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genero[0] = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_faixa_preco.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                faixa_preco[0] = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> adapterCidade=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, ArrayString.cidades);
        autoCompleteTextView.setAdapter(adapterCidade);

        editTelefone.addTextChangedListener(new TextWatcher() {
            boolean editedFlag;
            boolean backspacingFlag;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                backspacingFlag = count > after;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                String newText;
                String phoneString = text.replaceAll("[\\D]","");
                int phoneLength = phoneString.length();
                int cursor = 0;
                if(!editedFlag) {
                    editedFlag = true;

                    if(!backspacingFlag && phoneLength > 11) {
                        phoneString = phoneString.substring(0,phoneLength-1);
                        phoneLength = phoneString.length();
                    }

                    String underline = "___________";
                    phoneString = phoneString + underline.substring(0,11-phoneLength);
                    newText = "(" + phoneString.substring(0,2) + ") " + phoneString.substring(2,7) + "-" + phoneString.substring(7,11);
                    editTelefone.setText(newText);

                    if(phoneLength >= 0) cursor++;
                    if(phoneLength > 2) cursor+=2;
                    if(phoneLength > 7) cursor++;
                    cursor+=phoneLength;
                    editTelefone.setSelection(cursor);
                }
                else {
                    editedFlag = false;
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!editNome.getText().toString().equals("")&&!autoCompleteTextView.getText().toString().equals("")&&
                        !editEstado.getText().toString().equals("")&&!editTelefone.getText().toString().equals("")&&
                       !genero[0].equals(ArrayString.generos[0])&&!faixa_preco[0].equals(ArrayString.faixas_preco[0])){

                    Firebase.DadosArtista dadosArtista=new Firebase.DadosArtista(editTelefone.getText().toString(),
                            autoCompleteTextView.getText().toString(),editEstado.getText().toString(),genero[0],faixa_preco[0]);
                    Firebase.insertArtist(editNome.getText().toString(),picturePath,dadosArtista,Firebase.getmAuth().
                            getCurrentUser().getUid(),new Runnable(){
                        @Override
                        public void run() {
                            Intent i=new Intent(ArtistRegistrationActivity.this, HomeArtistaActivity.class);
                            startActivity(i);
                        }
                    });


                }
                else if(editNome.getText().toString().equals("")){
                    Toast.makeText(ArtistRegistrationActivity.this, "Preencha o campo nome!", Toast.LENGTH_SHORT).show();
                }
                else if(editEstado.getText().toString().equals("")){
                    Toast.makeText(ArtistRegistrationActivity.this, "Preencha o campo estado!", Toast.LENGTH_SHORT).show();
                }
                else if(autoCompleteTextView.getText().toString().equals("")){
                    Toast.makeText(ArtistRegistrationActivity.this, "Preencha o campo cidade!", Toast.LENGTH_SHORT).show();
                }
                else if(editTelefone.getText().toString().equals("")){
                    Toast.makeText(ArtistRegistrationActivity.this, "Preencha o campo telefone!", Toast.LENGTH_SHORT).show();
                }
                else if(picturePath==null){
                    Toast.makeText(ArtistRegistrationActivity.this, "Escolha ou tire uma foto!", Toast.LENGTH_SHORT).show();
                }
                else if (genero[0].equals(ArrayString.generos[0])){
                    Toast.makeText(ArtistRegistrationActivity.this, "Escolha um gênero da lista!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ArtistRegistrationActivity.this, "Escolha uma faixa de preço da lista!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        img_artista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(ArtistRegistrationActivity.this,
                        Manifest.permission.CAMERA);
                if (permissionCheck== PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                }
                else {
                    ActivityCompat.requestPermissions(ArtistRegistrationActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                            MY_PERMISSIONS);
                }

            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_PICK_PICTURE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            assert selectedImage != null;
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            int columnIndex;
            if (cursor!=null){
                cursor.moveToFirst();
                columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                cursor.close();
            }
            Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(picturePath), 100, 100, false);
            android.support.media.ExifInterface exif = null;
            try {
                File pictureFile = new File(picturePath);
                exif = new android.support.media.ExifInterface(pictureFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = android.support.media.ExifInterface.ORIENTATION_NORMAL;
            if (exif != null)
                orientation = exif.getAttributeInt(android.support.media.ExifInterface.TAG_ORIENTATION, android.support.media.ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case android.support.media.ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateBitmap(bitmap, 90);
                    break;
                case android.support.media.ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateBitmap(bitmap, 180);
                    break;

                case android.support.media.ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateBitmap(bitmap, 270);
                    break;
            }

            //salva bitmap no cache
            File file = new File(getCacheDir(),"foto_anjo.png");
            FileOutputStream fileOutputStream;
            try {
                fileOutputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //exibe bitmap no img_artista
            Picasso.with(getBaseContext()).invalidate(file.getPath());
            Picasso.with(getBaseContext()).load(file).memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE).transform(new CircleTransform()).into(img_artista);
        }
        else if(requestCode == RESULT_TAKE_PICTURE && resultCode == RESULT_OK && null != data){

            Bundle extras = data.getExtras();
            Bitmap bitmap=null;
            if (extras!=null){bitmap = (Bitmap) extras.get("data");}
            File file=new File(getCacheDir(),"foto_anjo.png");
            FileOutputStream fileOutputStream;
            try {
                fileOutputStream=new FileOutputStream(file);
                assert bitmap != null;
                bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.picturePath = file.getPath();
            Log.d("Path",picturePath);

            //exibe bitmap no img_artista
            Picasso.with(getBaseContext()).invalidate(picturePath);
            Picasso.with(getBaseContext()).load(file).memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE).transform(new CircleTransform()).into(img_artista);
        }
    }

    private void dispatchTakePictureIntent() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Tirar Foto", "Galeria","Cancelar"};
                android.support.v7.app.AlertDialog.Builder builder =
                        new android.support.v7.app.AlertDialog.Builder(ArtistRegistrationActivity.this);
                builder.setTitle("Selecione uma Opção:");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Tirar Foto")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent,RESULT_TAKE_PICTURE );
                        } else if (options[item].equals("Galeria")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, RESULT_PICK_PICTURE);
                        } else if (options[item].equals("Cancelar")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("nome",editNome.getText().toString());
        outState.putString("img_artista",picturePath);
        outState.putString("estado",editEstado.getText().toString());
        outState.putString("cidade",autoCompleteTextView.getText().toString());
        outState.putString("telefone",editTelefone.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState!=null){
            editNome.setText(savedInstanceState.getString("nome"));
            picturePath=savedInstanceState.getString("img_artista");
            editEstado.setText(savedInstanceState.getString("estado"));
            autoCompleteTextView.setText(savedInstanceState.getString("cidade"));
            editTelefone.setText(savedInstanceState.getString("telefone"));
        }
    }
}
