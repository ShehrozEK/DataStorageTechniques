package shehroz.com.datastoragetechniques;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ExternalStorageFragment extends Fragment {
    public ExternalStorageFragment() {
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
        return inflater.inflate(R.layout.fragment_external_storage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final EditText filename = view.findViewById(R.id.filenameEditText);
        final RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        final RadioButton publicType = view.findViewById(R.id.publicRadioBtn);
        final RadioButton privateType = view.findViewById(R.id.privateRadioBtn);
        final EditText data = view.findViewById(R.id.dataEditText);
        final TextView resultArea = view.findViewById(R.id.resultArea);
        Button save = view.findViewById(R.id.saveBtn);
        Button load = view.findViewById(R.id.loadBtn);
        Button delete = view.findViewById(R.id.deleteBtn);
        radioGroup.check(publicType.getId());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filename.getText().toString().isEmpty()) {
                    filename.setError("Filename Missing");
                } else if (data.getText().toString().isEmpty()) {
                    data.setError("Data is Required");
                } else {
                    File file;
                    if (publicType.isChecked()) {
                        //      file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),filename.getText().toString()+".txt");
                        file = new File(Environment.getExternalStorageDirectory(), filename.getText().toString() + ".txt");
                    } else {
                        file = new File(getContext().getExternalFilesDir(null), filename.getText().toString() + ".txt");
                    }
                    if (!writeDataToExternalStorage(file, data.getText().toString())) {
                        resultArea.setText("Oops Something Gone Wrong");
                    } else {
                        resultArea.setText("Data Successfully Stored");
                    }
                }
            }
        });
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filename.getText().toString().isEmpty()) {
                    filename.setError("Filename Missing");
                } else {
                    File file;
                    if (publicType.isChecked()) {
                        //      file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),filename.getText().toString()+".txt");
                        file = new File(Environment.getExternalStorageDirectory(), filename.getText().toString() + ".txt");
                    } else {
                        file = new File(getContext().getExternalFilesDir(null), filename.getText().toString() + ".txt");
                    }
                    resultArea.setText(readDataFromExternalStorage(file));
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filename.getText().toString().isEmpty()) {
                    filename.setError("Filename Missing");
                }
                else {
                    File file;
                    if (publicType.isChecked()) {
                        //      file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),filename.getText().toString()+".txt");
                        file = new File(Environment.getExternalStorageDirectory(), filename.getText().toString() + ".txt");
                    } else {
                        file = new File(getContext().getExternalFilesDir(null), filename.getText().toString() + ".txt");
                    }
                    resultArea.setText(deleteFile(file));
                }
            }
        });
    }

    private boolean writeDataToExternalStorage(File file, String data) {
        if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) {
            if (file != null) {
                if (!file.exists()) {
                    try {
                        if (!file.createNewFile()) return false;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
                try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                    fileOutputStream.write(data.getBytes());
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private String readDataFromExternalStorage(File file) {
        if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) {
            if (file != null) {
                if (!file.exists()) {
                    return "File doesn't exist";
                }
                try (FileInputStream fileInputStream = new FileInputStream(file); Scanner scanner = new Scanner(fileInputStream)) {
                    scanner.useDelimiter("\\Z");
                    return scanner.next();
                } catch (IOException e) {
                    return e.getMessage();
                }
            }
        }
        return "Please check if External Storage is accessible and file permission is not set to readonly";
    }

    private String deleteFile(File file) {
        if (isExternalStorageAvailable()) {
            if (file != null) {
                if (!file.exists()) {
                    return "File doesn't exist";
                }
                return file.delete()?"File Deleted Successfully":"File Not Deleted";
            }
        }
        return "Please check if External Storage is accessible";
    }

    private boolean isExternalStorageReadOnly() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);
    }

    private boolean isExternalStorageAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
}
