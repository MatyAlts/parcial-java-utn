package parcial1.java.dao;

import parcial1.java.model.Cliente;
import java.util.List;

public interface ClienteDAO {
    void crear(Cliente cliente);
    Cliente buscarPorId(int id);
    List<Cliente> listarTodos();
    void actualizar(Cliente cliente);
    boolean eliminar(int id);
}
