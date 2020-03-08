package shehroz.com.datastoragetechniques;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferenceFragment extends Fragment {
    public SharedPreferenceFragment() {
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
        return inflater.inflate(R.layout.fragment_shared_preference, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button loadBtn = view.findViewById(R.id.loadBtn);
        Button addBtn = view.findViewById(R.id.addBtn);
        final TextView resultArea = view.findViewById(R.id.resultArea);
        final EditText fileName = view.findViewById(R.id.filenameEditText);
        final EditText sp_Key = view.findViewById(R.id.keyEditText);
        final EditText sp_Value = view.findViewById(R.id.valueEditText);
        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp;
                if(!fileName.getText().toString().isEmpty()){
                    sp = getActivity().getSharedPreferences(fileName.getText().toString(), MODE_PRIVATE);
                }
                else{
                    sp = getActivity().getPreferences(MODE_PRIVATE);
                }
                if(!sp_Key.getText().toString().isEmpty()){
                    resultArea.setText(sp.getString(sp_Key.getText().toString(),"Default Value"));
                }
                else{
                    Map<String,?> allPrefs = sp.getAll();
                    Set<String> keySet = allPrefs.keySet();
                    StringBuilder result = new StringBuilder();
                    for(String key:keySet){
                        result.append(key).append("-->").append(allPrefs.get(key).toString()).append("\n");
                    }
                    resultArea.setText(result.toString());
                }
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp;
                if(TextUtils.isEmpty(sp_Key.getText())){
                    sp_Key.setError("Required");
                }
                else if(TextUtils.isEmpty(sp_Value.getText())){
                    sp_Value.setError("Required");
                }
                else{
                    if(!fileName.getText().toString().isEmpty()){
                        sp = getActivity().getSharedPreferences(fileName.getText().toString(), MODE_PRIVATE);
                    }
                    else{
                        sp = getActivity().getPreferences(MODE_PRIVATE);
                    }
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(sp_Key.getText().toString(),sp_Value.getText().toString());
                    editor.apply();
                }
            }
        });
    }
}
