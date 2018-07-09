package com.example.otavio.newshowup.contratante;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.otavio.newshowup.R;
import com.example.otavio.newshowup.utils.ArrayString;
import com.example.otavio.newshowup.utils.Firebase;
import com.example.otavio.newshowup.utils.SnapshotContratante;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditarEventoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.edit_nome_evento)EditText editNomeEvento;
    @BindView(R.id.edit_descricao)EditText editDescricao;
    @BindView(R.id.edit_instrumentos)EditText editInstrumentos;
    @BindView(R.id.spinner_faixa_preco)Spinner spinner_faixa_preco;
    @BindView(R.id.auto_complete_city)AutoCompleteTextView autoCompleteTextView;
    @BindView(R.id.btn_edit_event)Button btn_edit_event;
    @BindView(R.id.edit_data)EditText editDate;
    private DatePickerDialog dpd;
    ArrayList<String> imgs_adress;
    @BindView(R.id.progressbarEditEvent)ProgressBar progressBar;
    @BindView(R.id.scrollViewEditEvent)ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_evento);
        ButterKnife.bind(this);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        final Firebase.Evento evento= (Firebase.Evento) getIntent().getSerializableExtra("evento");
        SnapshotContratante.setEvento(evento);

        imgs_adress=new ArrayList<>(evento.fotos);
        editNomeEvento.setText(evento.nome);
        editDescricao.setText(evento.descricao);
        editDate.setText(evento.data);
        StringBuilder stringBuilder=new StringBuilder();
        for (String inst:evento.instrumentos){
            stringBuilder.append(inst).append(",");
        }
        editInstrumentos.setText(stringBuilder);
        autoCompleteTextView.setText(evento.cidade);

        final String[] faixa_preco = new String[1];
        ArrayAdapter<String> adapterPreco=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,
                ArrayString.faixas_preco);
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
        
        btn_edit_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.setAlpha(0.4f);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setAlpha(1f);
                btn_edit_event.setEnabled(false);
                //disable user interaction
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                String nome_event=editNomeEvento.getText().toString();
                String desc=editDescricao.getText().toString();
                ArrayList <String> instrum=new ArrayList<>();
                instrum.add(editInstrumentos.getText().toString());
                String city=autoCompleteTextView.getText().toString();
                String date=editDate.getText().toString();

                // if (!nome_event.equals("")&&!desc.equals("")&&!instrum.get(0).equals("")&&city.equals("")&&
                //    !faixa_preco[0].equals(ArrayString.faixas_preco[0])&&imgs_adress.size()>0&&!date.equals("")){
                Firebase.Evento evento1=new Firebase.Evento(evento.id,evento.id_contratante,nome_event,desc,
                        instrum,faixa_preco[0],evento.fotos,city,date,evento.candidatos);

                Firebase.updateEvento(evento1, new Runnable() {
                            @Override
                            public void run() {
                                scrollView.setAlpha(1f);
                                progressBar.setVisibility(View.INVISIBLE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                Toast.makeText(EditarEventoActivity.this,"Evento editado com sucesso!",
                                        Toast.LENGTH_LONG).show();
                                startActivity(new Intent(EditarEventoActivity.this,
                                        MeusEventosActivity.class));
                            }
                        });
                // }
                btn_edit_event.setEnabled(true);

            }
        });
        loadDatePicker();

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}