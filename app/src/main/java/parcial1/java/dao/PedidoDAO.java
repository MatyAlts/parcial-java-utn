package parcial1.java.dao;

import parcial1.java.model.Pedido;
import java.util.List;

public interface PedidoDAO {
    void crear(Pedido pedido);
    Pedido buscarPorId(int id);
    List<Pedido> listarTodos();
    List<Pedido> listarPorCliente(int clienteId);
    void actualizar(Pedido pedido);
    boolean eliminar(int id);
}
