package parcial1.java.dao;

import java.util.List;

public interface GenericDAO<T, K> {
    void crear(T entidad);
    T buscarPorId(K id);
    List<T> listarTodos();
    void actualizar(T entidad);
    boolean eliminar(K id);
}
