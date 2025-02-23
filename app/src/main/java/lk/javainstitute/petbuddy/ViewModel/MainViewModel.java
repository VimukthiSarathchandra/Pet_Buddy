package lk.javainstitute.petbuddy.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import lk.javainstitute.petbuddy.Domain.CategroyModel;
import lk.javainstitute.petbuddy.Repository.MainRepository;

public class MainViewModel extends ViewModel {

    private final MainRepository repository;

    public MainViewModel() {
        this.repository = new MainRepository();
    }

    public LiveData<List<CategroyModel>> loadCategory(){return repository.localCategory();}
}
