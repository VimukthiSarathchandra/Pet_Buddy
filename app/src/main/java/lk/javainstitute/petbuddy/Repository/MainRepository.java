package lk.javainstitute.petbuddy.Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import lk.javainstitute.petbuddy.Domain.CategroyModel;

public class MainRepository {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public LiveData<List<CategroyModel>> localCategory() {
        final MutableLiveData<List<CategroyModel>> listMutableLiveData = new MutableLiveData<>();

        DatabaseReference ref = firebaseDatabase.getReference("Category");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<CategroyModel> categoryList = new ArrayList<>();
                // Loop through the data in the "Category" node
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // Convert each snapshot into a CategroyModel object
                    CategroyModel categoryModel = dataSnapshot.getValue(CategroyModel.class);
                    if (categoryModel != null) {
                        categoryList.add(categoryModel);
                    }
                }
                // Update the LiveData object with the list of category models
                listMutableLiveData.setValue(categoryList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle the error if the database read fails
                listMutableLiveData.setValue(null);
            }
        });

        // Return the LiveData object containing the categories
        return listMutableLiveData;
    }
}
