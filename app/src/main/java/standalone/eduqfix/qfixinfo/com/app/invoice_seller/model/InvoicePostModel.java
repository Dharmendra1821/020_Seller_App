package standalone.eduqfix.qfixinfo.com.app.invoice_seller.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import standalone.eduqfix.qfixinfo.com.app.invoice_seller.database.AppDatabase;
import standalone.eduqfix.qfixinfo.com.app.invoice_seller.database.InvoiceDao;

public class InvoicePostModel extends AndroidViewModel {

    private InvoiceDao postDao;
    private ExecutorService executorService;

    public InvoicePostModel(@NonNull Application application) {
        super(application);
        postDao = AppDatabase.getInstance(application).personDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<InvoiceProductModel>> getAllPosts() {
        return postDao.loadAllPersons();
    }

    void savePost(InvoiceProductModel post) {
        executorService.execute(() -> postDao.insertPerson(post));
    }

    public void deletePost(InvoiceProductModel post) {
        executorService.execute(() -> postDao.delete(post));
    }


}
