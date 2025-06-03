package parcial1.java.dao;

import parcial1.java.model.Pedido;
import parcial1.java.util.DatabaseUtil;


import java.sql.*;

import java.util.ArrayList;
import java.util.List;


public class PedidoDAOImpl extends AbstractGenericDAOImpl<Pedido, Integer> implements PedidoDAO {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PedidoDAOImpl.class);
    @Override
    protected String getTableName() { return "pedidos"; }

    @Override
    protected Pedido mapResultSet(ResultSet rs) throws SQLException {
        return new Pedido(
            rs.getInt("id"),
            rs.getDate("fecha").toLocalDate(),
            rs.getDouble("monto"),
            rs.getInt("cliente_id")
        );
    }

    @Override
    protected void setInsertParams(PreparedStatement stmt, Pedido pedido) throws SQLException {
        stmt.setDate(1, java.sql.Date.valueOf(pedido.getFecha()));
        stmt.setDouble(2, pedido.getMonto());
        stmt.setInt(3, pedido.getClienteId());
    }

    @Override
    protected void setUpdateParams(PreparedStatement stmt, Pedido pedido) throws SQLException {
        stmt.setDate(1, java.sql.Date.valueOf(pedido.getFecha()));
        stmt.setDouble(2, pedido.getMonto());
        stmt.setInt(3, pedido.getClienteId());
        stmt.setInt(4, pedido.getId());
    }

    @Override
    protected String getIdColumn() { return "id"; }

    @Override
    protected void setIdParam(PreparedStatement stmt, Integer id) throws SQLException {
        stmt.setInt(1, id);
    }

    // Métodos CRUD específicos y avanzados pueden ir aquí

    @Override
    public void crear(Pedido pedido) {
        String sql = "INSERT INTO pedidos (fecha, monto, cliente_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, Date.valueOf(pedido.getFecha()));
            stmt.setDouble(2, pedido.getMonto());
            stmt.setInt(3, pedido.getClienteId());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    pedido.setId(rs.getInt(1));
                }
            }
            LOGGER.info("Pedido creado: {}", pedido);
        } catch (SQLException e) {
            LOGGER.error("Error al crear pedido", e);
        }
    }

    @Override
    public Pedido buscarPorId(int id) {
        String sql = "SELECT * FROM pedidos WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Pedido(
                        rs.getInt("id"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getDouble("monto"),
                        rs.getInt("cliente_id")
                    );
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error al buscar pedido", e);
        }
        return null;
    }

    @Override
    public List<Pedido> listarTodos() {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedidos";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Pedido(
                    rs.getInt("id"),
                    rs.getDate("fecha").toLocalDate(),
                    rs.getDouble("monto"),
                    rs.getInt("cliente_id")
                ));
            }
        } catch (SQLException e) {
            LOGGER.error("Error al listar pedidos", e);
        }
        return lista;
    }

    @Override
    public List<Pedido> listarPorCliente(int clienteId) {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedidos WHERE cliente_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Pedido(
                        rs.getInt("id"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getDouble("monto"),
                        rs.getInt("cliente_id")
                    ));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error al listar pedidos por cliente", e);
        }
        return lista;
    }

    @Override
    public void actualizar(Pedido pedido) {
        String sql = "UPDATE pedidos SET fecha=?, monto=?, cliente_id=? WHERE id=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(pedido.getFecha()));
            stmt.setDouble(2, pedido.getMonto());
            stmt.setInt(3, pedido.getClienteId());
            stmt.setInt(4, pedido.getId());
            stmt.executeUpdate();
            LOGGER.info("Pedido actualizado: {}", pedido);
        } catch (SQLException e) {
            LOGGER.error("Error al actualizar pedido", e);
        }
    }

    @Override
    public boolean eliminar(int id) {
        return super.eliminar(id);
    }
}
