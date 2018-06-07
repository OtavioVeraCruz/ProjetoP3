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
import com.example.otavio.newshowup.utils.ArrayString;
import com.example.otavio.newshowup.utils.CircleTransform;
import com.example.otavio.newshowup.utils.Firebase;
import com.example.otavio.newshowup.utils.SnapshotContratante;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class CriarEventoActivity extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.edit_nome_evento)EditText editNomeEvento;
    @BindView(R.id.edit_descricao)EditText editDescricao;
    @BindView(R.id.edit_instrumentos)EditText editInstrumentos;
    @BindView(R.id.spinner_faixa_preco)Spinner spinner_faixa_preco;
    @BindView(R.id.auto_complete_city)AutoCompleteTextView autoCompleteTextView;
    @BindView(R.id.btn_create_event)Button btn_create_event;
    @BindViews({R.id.image_add_new,R.id.image_add_new1,
            R.id.image_add_new2,R.id.image_add_new3})List<ImageView> imgs;
    @BindView(R.id.edit_data)EditText editDate;
    private DatePickerDialog dpd;

    private String picturePath;
    static int pos=0;
    private static final int RESULT_TAKE_PICTURE = 123;
    private static final int RESULT_PICK_PICTURE = 283;
    private static final int MY_PERMISSIONS = 2;
    ArrayList<String> imgs_adress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_evento);
        ButterKnife.bind(this);
        imgs_adress=new ArrayList<>();

        final String[] faixa_preco = new String[1];
        ArrayAdapter<String> adapterPreco=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, ArrayString.faixas_preco);
        spinner_faixa_preco.setAdapter(adapterPreco);
        spinner_faixa_preco.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                faixa_preco[0] =parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> adapterCidade=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, ArrayString.cidades);
        autoCompleteTextView.setAdapter(adapterCidade);
        imgs.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(CriarEventoActivity.this,
                        Manifest.permission.CAMERA);
                if (permissionCheck== PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();

                }
                else {
                    ActivityCompat.requestPermissions(CriarEventoActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                            MY_PERMISSIONS);
                }

            }
        });

        btn_create_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id_contratante=SnapshotContratante.getId_contratante();
                String nome_event=editNomeEvento.getText().toString();
                String desc=editDescricao.getText().toString();
                ArrayList <String> instrum=new ArrayList<>();
                instrum.add(editInstrumentos.getText().toString());
                String city=autoCompleteTextView.getText().toString();
                String date=editDate.getText().toString();

               // if (!nome_event.equals("")&&!desc.equals("")&&!instrum.get(0).equals("")&&city.equals("")&&
                //    !faixa_preco[0].equals(ArrayString.faixas_preco[0])&&imgs_adress.size()>0&&!date.equals("")){

                    Firebase.insertEvento(id_contratante,nome_event,desc,instrum,faixa_preco[0],
                            imgs_adress,city,date, new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CriarEventoActivity.this,"Evento criado com sucesso!",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(CriarEventoActivity.this,HomeContratanteActivity.class));
                        }
                    });
               // }
            }
        });
        loadDatePicker();

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
            File file = new File(getCacheDir(),"foto_"+pos+".png");
            FileOutputStream fileOutputStream;
            try {
                fileOutputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //exibe bitmap
            imgs_adress.add(picturePath);
            imgs.get(updatePos()).setVisibility(View.VISIBLE);
            Picasso.with(getBaseContext()).invalidate(file.getPath());
            Picasso.with(getBaseContext()).load(file).memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE).transform(new CircleTransform()).into(imgs.get(pos));
        }
        else if(requestCode == RESULT_TAKE_PICTURE && resultCode == RESULT_OK && null != data){

            Bundle extras = data.getExtras();
            Bitmap bitmap=null;
            if (extras!=null){bitmap = (Bitmap) extras.get("data");}
            File file=new File(getCacheDir(),"foto_"+pos+".png");
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
            imgs_adress.add(picturePath);
            Log.d("Path",picturePath);

            //exibe bitmap
            imgs.get(updatePos()).setVisibility(View.VISIBLE);
            Picasso.with(getBaseContext()).invalidate(picturePath);
            Picasso.with(getBaseContext()).load(file).memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE).transform(new CircleTransform()).into(imgs.get(pos));
        }
    }

    private void dispatchTakePictureIntent() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Tirar Foto", "Galeria","Cancelar"};
                android.support.v7.app.AlertDialog.Builder builder =
                        new android.support.v7.app.AlertDialog.Builder(CriarEventoActivity.this);
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

    private int updatePos(){
        int retorno=0;
        switch (pos){
            case 0:
                pos=1;
                retorno=pos;
                break;
            case 1:
                pos=2;
                retorno=pos;
                break;
            case 2:
                pos=3;
                retorno=pos;
                break;
        }
        return retorno;
    }

    private void loadDatePicker() {
        Calendar now = Calendar.getInstance();
        dpd = DatePickerDialog.newInstance(this,now.get(Calendar.YEAR),now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));

        editDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
        editDate.setText(date);


    }
}
