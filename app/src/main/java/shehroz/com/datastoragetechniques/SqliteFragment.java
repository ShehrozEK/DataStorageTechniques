package shehroz.com.datastoragetechniques;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class SqliteFragment extends Fragment {
    SQLiteDBHelper sqLiteDBHelper;
    SQLiteDatabase sqLiteDatabase;
    TextView resultArea;

    public SqliteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sqLiteDBHelper = new SQLiteDBHelper(this.getContext());
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sqlite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final EditText name = view.findViewById(R.id.nameEditText);
        final EditText age = view.findViewById(R.id.ageEditText);
        final EditText newAge = view.findViewById(R.id.newAgeEditText);
        Button updateBtn = view.findViewById(R.id.updateBtn);
        Button loadBtn = view.findViewById(R.id.loadBtn);
        Button deleteBtn = view.findViewById(R.id.deleteBtn);
        Button addBtn = view.findViewById(R.id.addBtn);
        Button findBtn = view.findViewById(R.id.findBtn);
        resultArea = view.findViewById(R.id.resultArea);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!name.getText().toString().isEmpty() && !age.getText().toString().isEmpty()) {
                    if (sqLiteDBHelper != null) {
                        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getWritableDatabase();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(SQLiteDBHelper.COLUMN_NAME, name.getText().toString());
                        contentValues.put(SQLiteDBHelper.COLUMN_AGE, Integer.valueOf(age.getText().toString()));
                        long rowId = sqLiteDatabase.insert(SQLiteDBHelper.TABLE_NAME, null, contentValues);
                        Toast.makeText(view.getContext(), rowId + "", Toast.LENGTH_SHORT).show();
                        if (rowId > 0) resultArea.setText("Row Inserted");
                        sqLiteDatabase.close();
                    }
                }
            }
        });
        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sqLiteDBHelper != null) {
                    String result = "";
                    SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
                    String[] projection = {SQLiteDBHelper.COLUMN_NAME, SQLiteDBHelper.COLUMN_AGE};
                    Cursor cursor = sqLiteDatabase.query(SQLiteDBHelper.TABLE_NAME, projection, null, null, null, null, null);
                    int nameIndex = cursor.getColumnIndex(SQLiteDBHelper.COLUMN_NAME);
                    int ageIndex = cursor.getColumnIndex(SQLiteDBHelper.COLUMN_AGE);
                    if (cursor.moveToFirst()) {
                        do {
                            result += String.format("Name=%s\nAge=%d\n\n", cursor.getString(nameIndex), cursor.getInt(ageIndex));
                            resultArea.setText(result.isEmpty() ? "Nothing Found" : result);
                        }
                        while (cursor.moveToNext());
                        cursor.close();
                    }
                    sqLiteDatabase.close();
                }
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sqLiteDBHelper != null) {
                    SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getWritableDatabase();
                    if (name.getText().toString().isEmpty() && age.getText().toString().isEmpty()) {
                        boolean check = sqLiteDatabase.delete(sqLiteDBHelper.TABLE_NAME, "1=1", null) > 0;
                        resultArea.setText(check ? "All data deleted" : "Oops something went wrong");
                    } else {
                        String query;
                        if (name.getText().toString().isEmpty() && !age.getText().toString().isEmpty()) {
                            query = "delete from " + SQLiteDBHelper.TABLE_NAME + " where " + SQLiteDBHelper.COLUMN_AGE + "=" + age.getText().toString();
                        } else if (!name.getText().toString().isEmpty() && age.getText().toString().isEmpty()) {
                            query = "delete from " + SQLiteDBHelper.TABLE_NAME + " where " + SQLiteDBHelper.COLUMN_NAME + " like " + "'" + name.getText().toString() + "'";
                        } else {
                            query = "delete from " + SQLiteDBHelper.TABLE_NAME + " where " + SQLiteDBHelper.COLUMN_AGE + "=" + age.getText().toString() + " AND " + SQLiteDBHelper.COLUMN_NAME + " like " + "'" + name.getText().toString() + "'";
                        }
                        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                        resultArea.setText(cursor.getCount() >= 0 ? cursor.getCount() + " rows deleted successfully" : "No row affected");
                    }
                }
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!name.getText().toString().isEmpty() && !age.getText().toString().isEmpty()) {
                    if (sqLiteDBHelper != null) {
                        SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
                        Cursor cursor = sqLiteDatabase.rawQuery("SELECT *," + sqLiteDBHelper.TABLE_NAME + ".rowid AS rowID" + " FROM " + sqLiteDBHelper.TABLE_NAME + " WHERE " + sqLiteDBHelper.TABLE_NAME + "." + sqLiteDBHelper.COLUMN_AGE + "=" + age.getText().toString() + " AND " + sqLiteDBHelper.TABLE_NAME + "." + sqLiteDBHelper.COLUMN_NAME + " LIKE '%" + name.getText().toString() + "%'", null);
                        long rowID = 0;
                        while (cursor.moveToNext()) {
                            rowID = cursor.getLong(cursor.getColumnIndexOrThrow("rowID"));
                        }
                        if (rowID > 0) {
                            if(!newAge.getText().toString().isEmpty()) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(SQLiteDBHelper.COLUMN_AGE, Integer.valueOf(newAge.getText().toString()));
                                sqLiteDatabase = sqLiteDBHelper.getWritableDatabase();
                                resultArea.setText(sqLiteDatabase.update(SQLiteDBHelper.TABLE_NAME, contentValues, "rowID=" + rowID, null) > 0 ? "Row updated" : "No row affected");
                            }
                            else{
                                  newAge.setError("Field Required");
                            }
                        }
                        else{
                            Toast.makeText(getContext(),"User Not Found",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sqLiteDBHelper != null) {
                    SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getWritableDatabase();
                    String query = "", result = "";
                    if (name.getText().toString().isEmpty() && !age.getText().toString().isEmpty()) {
                        query = "select * from " + SQLiteDBHelper.TABLE_NAME + " where " + SQLiteDBHelper.COLUMN_AGE + "=" + age.getText().toString();
                    } else if (!name.getText().toString().isEmpty() && age.getText().toString().isEmpty()) {
                        query = "select * from " + SQLiteDBHelper.TABLE_NAME + " where " + SQLiteDBHelper.COLUMN_NAME + " like " + "'%" + name.getText().toString() + "%'";
                    } else if (!name.getText().toString().isEmpty() && !age.getText().toString().isEmpty()) {
                        query = "select * from " + SQLiteDBHelper.TABLE_NAME + " where " + SQLiteDBHelper.COLUMN_AGE + "=" + age.getText().toString() + " AND " + SQLiteDBHelper.COLUMN_NAME + " like " + "'%" + name.getText().toString() + "%'";
                    }
                    if (!query.isEmpty()) {
                        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                        if (cursor.moveToFirst()) {
                            do {
                                result += String.format("Name=%s\nAge=%d\n\n", cursor.getString(0), cursor.getInt(1));
                            }
                            while (cursor.moveToNext());
                            resultArea.setText(result);
                        }
                    }
                }
            }
        });
    }
}
