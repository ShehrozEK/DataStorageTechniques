package shehroz.com.datastoragetechniques;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CacheFragment extends Fragment {
    public CacheFragment() {
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
        return inflater.inflate(R.layout.fragment_cache, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final EditText filename = view.findViewById(R.id.filenameEditText);
        final RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        final RadioButton external = view.findViewById(R.id.externalRadioBtn);
        final RadioButton internal = view.findViewById(R.id.internalRadioBtn);
        final EditText data = view.findViewById(R.id.dataEditText);
        final TextView resultArea = view.findViewById(R.id.resultArea);
        Button save = view.findViewById(R.id.saveBtn);
        Button retrieve = view.findViewById(R.id.retrieveBtn);
        Button delete = view.findViewById(R.id.deleteBtn);
        radioGroup.check(external.getId());
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = null;
                if (filename.getText().toString().isEmpty()) {
                    filename.setError("Name of the file is missing");
                } else {
                    if (external.isChecked()) {
                        file = new File(getContext().getExternalCacheDir().getAbsolutePath() + "/" + filename.getText().toString() + ".txt");
                    } else if (internal.isChecked()) {
                        file = new File(getContext().getCacheDir().getAbsolutePath() + "/" + filename.getText().toString() + ".txt");
                    }
                    if (data.getText().toString().isEmpty()) {
                        data.setError("Required");
                        return;
                    }
                    if (file != null) {
                        if (!file.exists()) {
                            try {
                                file.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (!writeDataToFile(file, data.getText().toString())) {
                            Toast.makeText(getContext(), "Oops something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
        retrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = null;
                if (filename.getText().toString().isEmpty()) {
                    filename.setError("Name of the file is missing");
                }
                else {
                    if (external.isChecked()) {
                        file = new File(getContext().getExternalCacheDir().getAbsolutePath()+"/"+filename.getText().toString()+".txt");
                    } else if (internal.isChecked()) {
                        file = new File(getContext().getCacheDir().getAbsolutePath()+"/"+filename.getText().toString()+".txt");
                    }
                    if(!file.exists()) {
                        Toast.makeText(getContext(),"File doesn't exist ",Toast.LENGTH_LONG);
                        return;
                    }
                    if(!file.canRead()) {
                        Toast.makeText(getContext(),"Cannot read file "+file.getName(),Toast.LENGTH_LONG);
                        return;
                    }
                    resultArea.setText(readDataFromFile(file));
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filename.getText().toString().isEmpty()) {
                    filename.setError("Filename missing");
                }
                else{
                    File file;
                    if(external.isChecked()){
                        file = new File(getContext().getExternalCacheDir(),filename.getText().toString()+".txt");
                    }
                    else {
                        file = new File(getContext().getCacheDir(),filename.getText().toString()+".txt");
                    }
                    if(file!=null && file.exists()) {
                        Toast.makeText(getContext(),deleteFile(file)?"File Deleted Successfully":"Oops File Not Deleted",Toast.LENGTH_LONG).show();
                    }
                    else{
                        resultArea.setText(String.format("No file having %s name exist in cache directory",filename.getText().toString()));
                    }
                }
            }
        });
    }

    private boolean deleteFile(File file) {
        return file.delete();
    }

    private boolean writeDataToFile(File file, String data) {
        boolean success = false;
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(data);
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    private String readDataFromFile(File file) {
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
            return stringBuilder.toString();
        } catch (IOException e) {

            return e.getMessage();
        }
    }
}
