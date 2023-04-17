package standalone.eduqfix.qfixinfo.com.app.invoice_seller.database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import standalone.eduqfix.qfixinfo.com.app.invoice_seller.model.InvoiceProductModel;

@Dao
public interface InvoiceDao {

    @Query("SELECT * FROM product ORDER BY ID")
    LiveData<List<InvoiceProductModel>> loadAllPersons();

    @Insert
    void insertPerson(InvoiceProductModel person);

    @Update
    void updatePerson(InvoiceProductModel person);

    @Delete
    void delete(InvoiceProductModel person);

    @Query("SELECT * FROM product WHERE id = :id")
    InvoiceProductModel loadPersonById(int id);

    @Query("DELETE FROM product")
    void deleteAll();
}
