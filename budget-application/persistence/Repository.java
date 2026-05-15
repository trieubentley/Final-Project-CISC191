// let's rename this one later
// also don't know what this one is used for; reference later

public interface Repository {
    void save(item : T);

    void delete(id : int);

    public List<T> findAll() {
        //...
    }
}