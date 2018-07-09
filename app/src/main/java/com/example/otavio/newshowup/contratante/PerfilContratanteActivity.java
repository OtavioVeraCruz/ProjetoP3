package com.example.otavio.newshowup.contratante;

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
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.otavio.newshowup.R;
import com.example.otavio.newshowup.utils.ArrayString;
import com.example.otavio.newshowup.utils.CircleTransform;
import com.example.otavio.newshowup.utils.Firebase;
import com.example.otavio.newshowup.utils.LoadImg;
import com.example.otavio.newshowup.utils.SnapshotContratante;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PerfilContratanteActivity extends AppCompatActivity {

    @BindView(R.id.progressbarHolderContratante)LinearLayout progressBarHolder;
    @BindView(R.id.progressBarContratante)ProgressBar progressBar;
    @BindView(R.id.editNome)EditText editNome;
    @BindView(R.id.editEstado)AutoCompleteTextView completeEstado;
    @BindView(R.id.editCidade)AutoCompleteTextView completeCidade;
    @BindView(R.id.img_profile)ImageView imageView;
    @BindView(R.id.editTelefone)EditText editTelefone;
    @BindView(R.id.btn_next)Button btn_next;
    private String picturePath;
    private static final int RESULT_TAKE_PICTURE = 123;
    private static final int RESULT_PICK_PICTURE = 283;
    private static final int MY_PERMISSIONS = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_contratante);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        ButterKnife.bind(this);

        String telefone= SnapshotContratante.getContratante().telefone;
        String nome=SnapshotContratante.getContratante().nome;
        String url=SnapshotContratante.getContratante().foto;
        String cidade=SnapshotContratante.getContratante().cidade;
        String estado=SnapshotContratante.getContratante().estado;

        editNome.setText(nome);
        editTelefone.setText(telefone);
        completeCidade.setText(cidade);
        completeEstado.setText(estado);

        LoadImg.loadImage(url,imageView,this);
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

        ArrayAdapter<String> estados=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, ArrayString.estados);
        completeEstado.setAdapter(estados);
        ArrayAdapter<String>cidades=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, ArrayString.cidades);
        completeCidade.setAdapter(cidades);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarHolder.setAlpha(0.4f);
                btn_next.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);

                if (!editNome.getText().toString().equals("")&&!completeEstado.getText().toString().equals("")&&
                        !completeCidade.getText().toString().equals("")&&!editTelefone.getText().toString().equals("")){

                    //disable user interaction
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    if (picturePath==null){
                        picturePath=SnapshotContratante.getContratante().foto;
                    }
                    Firebase.Contratante contratante=new Firebase.Contratante(SnapshotContratante.getContratante().id
                            ,editNome.getText().toString(), picturePath,SnapshotContratante.getContratante().token, Firebase.getmAuth()
                            .getCurrentUser().getUid(), editTelefone.getText().toString(),
                            completeEstado.getText().toString(), completeCidade.getText().toString());

                    Firebase.updateContratante(contratante, new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(PerfilContratanteActivity.this,
                                            HomeContratanteActivity.class));
                                }
                            });

                }
                else if(editNome.getText().toString().equals("")){
                    progressBarHolder.setAlpha(1f);
                    progressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    btn_next.setEnabled(true);
                    Toast.makeText(PerfilContratanteActivity.this, "Preencha o campo nome!", Toast.LENGTH_SHORT).show();
                }
                else if(completeEstado.getText().toString().equals("")){
                    progressBarHolder.setAlpha(1f);
                    progressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    btn_next.setEnabled(true);
                    Toast.makeText(PerfilContratanteActivity.this, "Preencha o campo estado!", Toast.LENGTH_SHORT).show();
                }
                else if(completeCidade.getText().toString().equals("")){
                    progressBarHolder.setAlpha(1f);
                    progressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    btn_next.setEnabled(true);
                    Toast.makeText(PerfilContratanteActivity.this, "Preencha o campo cidade!", Toast.LENGTH_SHORT).show();
                }
                else if(editTelefone.getText().toString().equals("")){
                    progressBarHolder.setAlpha(1f);
                    progressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    btn_next.setEnabled(true);
                    Toast.makeText(PerfilContratanteActivity.this, "Preencha o campo telefone!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(PerfilContratanteActivity.this,
                        Manifest.permission.CAMERA);
                if (permissionCheck== PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                }
                else {
                    ActivityCompat.requestPermissions(PerfilContratanteActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                            MY_PERMISSIONS);
                }

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
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
                    .networkPolicy(NetworkPolicy.NO_CACHE).transform(new CircleTransform()).into(imageView);
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
                    .networkPolicy(NetworkPolicy.NO_CACHE).transform(new CircleTransform()).into(imageView);
        }
    }

    private void dispatchTakePictureIntent() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Tirar Foto", "Galeria","Cancelar"};
                android.support.v7.app.AlertDialog.Builder builder =
                        new android.support.v7.app.AlertDialog.Builder(PerfilContratanteActivity.this);
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
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
