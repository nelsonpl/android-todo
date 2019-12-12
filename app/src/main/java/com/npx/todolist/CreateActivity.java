package com.npx.todolist;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateActivity extends AppCompatActivity {

    DatePickerDialog datePickerDialog;
    EditText etTitle;
    EditText etDescription;
    EditText etWhen;
    Button btAdd;
    Button btDelete;
    TodoBus bus;
    Long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        bus = new TodoBus(this);

        Intent intent = getIntent();
        id = intent.getLongExtra(Const.ID, 0);

        configViews();
        configBtnAdd();
        configEtWhen();
        configBtnDelete();

        getEntity();
    }

    private void getEntity() {
        if (id == 0) {
            return;
        }

        DateFormat sdf = SimpleDateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());

        Todo entity = bus.get(id);
        etTitle.setText(entity.getTitle());
        if (entity.getWhen() != null && entity.getWhen() > 0)
            etWhen.setText(sdf.format(entity.getWhen()));
    }

    private void configEtWhen() {
        etWhen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(CreateActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                final DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
                                final Calendar c = Calendar.getInstance();
                                c.set(year, monthOfYear, dayOfMonth);
                                etWhen.setText(df.format(c.getTime()));
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }

    private void configBtnAdd() {
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
                Todo entity = new Todo();
                entity.setId(id);
                entity.setTitle(etTitle.getText().toString());
                String when = etWhen.getText().toString();
                Date dtWhen = null;
                try {
                    dtWhen = df.parse(when);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (dtWhen != null)
                    entity.setWhen(dtWhen.getTime());

                bus.save(entity);

                Intent i = new Intent(CreateActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    private void configBtnDelete() {

        if (id == 0) {
            btDelete.setVisibility(View.INVISIBLE);
        } else {
            btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bus.delete(id);
                    Intent i = new Intent(CreateActivity.this, MainActivity.class);
                    startActivity(i);
                }
            });
        }
    }

    private void configViews() {
        etTitle = findViewById(R.id.etTitle);
        etWhen = findViewById(R.id.etWhen);
        btAdd = findViewById(R.id.btAdd);
        btDelete = findViewById(R.id.btDelete);
    }
}
