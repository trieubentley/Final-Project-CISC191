package budgettracker.persistence;

import java.util.List;

// Module 6: Generics — a reusable interface for any data store.
// <T> is the type being stored, <ID> is the type of its identifier.
public interface Repository<T, ID> {
    void add(T item);
    void remove(ID id);
    T findById(ID id);
    List<T> findAll();
}
