package shehroz.com.datastoragetechniques;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class InternalStorageFragment extends Fragment {
    public InternalStorageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_internal_storage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final EditText filename = view.findViewById(R.id.filenameEditText);
        final EditText data = view.findViewById(R.id.dataEditText);
        final TextView resultArea = view.findViewById(R.id.resultArea);
        Button save = view.findViewById(R.id.saveBtn);
        Button retrieve = view.findViewById(R.id.retrieveBtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filename.getText().toString().isEmpty()) {
                    writeDataToInternalStorage(Context.MODE_APPEND,"default",data.getText().toString());
                }
                else{
                    writeDataToInternalStorage(Context.MODE_PRIVATE,filename.getText().toString(),data.getText().toString());
                }
            }
        });
        retrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filename.getText().toString().isEmpty()) {
                    resultArea.setText(readDataFromInternalStorage("default"));
                }
                else{
                    resultArea.setText(readDataFromInternalStorage(filename.getText().toString()));
                }
            }
        });

    }

    private void writeDataToInternalStorage(int mode,String... strings) {
        try (FileOutputStream fileOutputStream = getContext().openFileOutput(strings[0], mode)) {
            fileOutputStream.write(strings[1].getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readDataFromInternalStorage(String filename) {
        try (FileInputStream fileInputStream = getContext().openFileInput(filename);Scanner scanner = new Scanner(fileInputStream)) {
            scanner.useDelimiter("\\Z");
            return scanner.next();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
