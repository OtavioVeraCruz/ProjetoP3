package com.example.otavio.newshowup.artista;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.otavio.newshowup.R;
import com.example.otavio.newshowup.utils.ArrayString;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BuscarEventoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener  {

    @BindView(R.id.spinner_preco)Spinner spinner_preco;
    @BindView(R.id.spinner_genero)Spinner spinner_genero;
    @BindView(R.id.auto_complete_city)AutoCompleteTextView autoCompleteTextView;
    @BindView(R.id.date_search_artist)EditText editDate;
    @BindView(R.id.btn_search_event)Button search;
    private DatePickerDialog dpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_evento);
        ButterKnife.bind(this);

        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        ArrayAdapter<String> adapterGenero=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, ArrayString.generos);
        spinner_genero.setAdapter(adapterGenero);
        ArrayAdapter<String> adapterPreco=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, ArrayString.faixas_preco);
        spinner_preco.setAdapter(adapterPreco);
        ArrayAdapter<String> adapterCidade=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, ArrayString.cidades);
        autoCompleteTextView.setAdapter(adapterCidade);

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
        spinner_preco.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                faixa_preco[0] =parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String query=genero[0]+"/"+faixa_preco[0]+"/"+autoCompleteTextView.getText().toString()+"/"
                            +editDate.getText().toString();

                    startActivity(new Intent(BuscarEventoActivity.this, ResultadoBuscaEventoActivity.class)
                            .putExtra("search",query));


            }
        });
        loadDatePicker();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
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
