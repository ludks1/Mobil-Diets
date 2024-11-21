package com.example.diets.activitys;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diets.R;
import com.example.diets.model.WeightRecord;
import com.example.diets.adapters.WeightAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WeightActivity extends AppCompatActivity {

    private EditText weightInput;
    private TextView selectedDateText;
    private Button selectDateButton, saveWeightButton;
    private RecyclerView weightRecyclerView;
    private LineChart weightChart;  // Agregar el gráfico

    private String selectedDate;
    private List<WeightRecord> weightRecords;
    private WeightAdapter weightAdapter;

    private DatabaseReference databaseReference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);

        // Inicializar Firebase
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("weightRecords").child(userId);

        // Inicializar UI
        weightInput = findViewById(R.id.weightInput);
        selectedDateText = findViewById(R.id.selectedDateText);
        selectDateButton = findViewById(R.id.selectDateButton);
        saveWeightButton = findViewById(R.id.saveWeightButton);
        weightRecyclerView = findViewById(R.id.weightRecyclerView);
        weightChart = findViewById(R.id.weightChart);  // Inicializar el gráfico

        // Inicializar RecyclerView
        weightRecords = new ArrayList<>();
        weightAdapter = new WeightAdapter(weightRecords);
        weightRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        weightRecyclerView.setAdapter(weightAdapter);

        // Selector de fecha
        selectDateButton.setOnClickListener(v -> showDatePickerDialog());

        // Guardar peso
        saveWeightButton.setOnClickListener(v -> saveWeightRecord());

        // Cargar registros existentes
        loadWeightRecords();
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    selectedDateText.setText("Fecha seleccionada: " + selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void saveWeightRecord() {
        String weightStr = weightInput.getText().toString().trim();
        if (weightStr.isEmpty() || selectedDate == null) {
            Toast.makeText(this, "Ingresa el peso y selecciona una fecha.", Toast.LENGTH_SHORT).show();
            return;
        }

        double weight = Double.parseDouble(weightStr);
        WeightRecord record = new WeightRecord(weight, selectedDate);

        // Crear un nuevo registro con un ID único
        String recordId = databaseReference.push().getKey();
        if (recordId != null) {
            // Guardar el registro en Firebase
            databaseReference.child(recordId).setValue(record).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    weightRecords.add(record); // Añadir el registro a la lista
                    weightAdapter.notifyDataSetChanged(); // Notificar al adaptador
                    updateChart();  // Actualizar el gráfico
                    Toast.makeText(this, "Registro guardado.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error al guardar el registro.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void loadWeightRecords() {
        // Cargar los registros de Firebase
        databaseReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    WeightRecord record = snapshot.getValue(WeightRecord.class);
                    if (record != null) {
                        weightRecords.add(record); // Añadir cada registro a la lista
                    }
                }
                weightAdapter.notifyDataSetChanged(); // Notificar al adaptador
                updateChart();  // Actualizar el gráfico
            }
        });
    }

    // Método para actualizar el gráfico con los datos cargados
    private void updateChart() {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < weightRecords.size(); i++) {
            WeightRecord record = weightRecords.get(i);
            // Convertir la fecha a un valor numérico (e.g., índice) y el peso como el valor Y
            entries.add(new Entry(i, (float) record.getWeight()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Peso (kg)");
        dataSet.setColor(getResources().getColor(R.color.colorPrimary));
        dataSet.setValueTextColor(getResources().getColor(R.color.colorPrimaryDark));

        LineData lineData = new LineData(dataSet);
        weightChart.setData(lineData);
        weightChart.invalidate(); // Refrescar el gráfico
    }
}
