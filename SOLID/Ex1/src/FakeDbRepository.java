import java.util.*;

public class FakeDbRepository implements StudentRepository {

    private final FakeDb db;

    public FakeDbRepository(FakeDb db) {
        this.db = db;
    }

    @Override
    public void save(StudentRecord r) {
        db.save(r);
    }

    @Override
    public int count() {
        return db.count();
    }

    @Override
    public List<StudentRecord> all() {
        return db.all();
    }
}