package com.example.otavio.newshowup.contratante;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.otavio.newshowup.R;
import com.example.otavio.newshowup.utils.ArrayString;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BuscarArtistaActivity extends AppCompatActivity {
    @BindView(R.id.spinner_preco)Spinner spinner_preco;
    @BindView(R.id.spinner_genero)Spinner spinner_genero;
    @BindView(R.id.auto_complete_city)AutoCompleteTextView autoCompleteTextView;
    @BindView(R.id.btn_search_event)Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_artista);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        ButterKnife.bind(this);

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
                if (!autoCompleteTextView.getText().toString().equals("")) {
                    String query = genero[0] + "/" + faixa_preco[0] + "/" + autoCompleteTextView.getText().toString();

                    startActivity(new Intent(BuscarArtistaActivity.this, ResultadoBuscaContratanteActivity.class)
                            .putExtra("query", "query").putExtra("search", query));
                }
                else {
                    Toast.makeText(BuscarArtistaActivity.this,"Campo cidade obrigatório!",
                            Toast.LENGTH_LONG).show();
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

}
