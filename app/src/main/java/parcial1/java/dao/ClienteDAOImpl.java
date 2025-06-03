package parcial1.java.dao;

import parcial1.java.model.Cliente;
import parcial1.java.util.DatabaseUtil;


import java.sql.*;

import java.util.ArrayList;
import java.util.List;


public class ClienteDAOImpl extends AbstractGenericDAOImpl<Cliente, Integer> implements ClienteDAO {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ClienteDAOImpl.class);
    @Override
    protected String getTableName() { return "clientes"; }

    @Override
    protected Cliente mapResultSet(ResultSet rs) throws SQLException {
        return new Cliente(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("email"),
            rs.getDate("fecha_registro").toLocalDate()
        );
    }

    @Override
    protected void setInsertParams(PreparedStatement stmt, Cliente cliente) throws SQLException {
        stmt.setString(1, cliente.getNombre());
        stmt.setString(2, cliente.getEmail());
        stmt.setDate(3, java.sql.Date.valueOf(cliente.getFechaRegistro()));
    }

    @Override
    protected void setUpdateParams(PreparedStatement stmt, Cliente cliente) throws SQLException {
        stmt.setString(1, cliente.getNombre());
        stmt.setString(2, cliente.getEmail());
        stmt.setDate(3, java.sql.Date.valueOf(cliente.getFechaRegistro()));
        stmt.setInt(4, cliente.getId());
    }

    @Override
    protected String getIdColumn() { return "id"; }

    @Override
    protected void setIdParam(PreparedStatement stmt, Integer id) throws SQLException {
        stmt.setInt(1, id);
    }

    // Métodos CRUD específicos y avanzados pueden ir aquí
    public int contarPedidosPorCliente(int clienteId) {
        String sql = "SELECT COUNT(*) FROM pedidos WHERE cliente_id = ?";
        try (Connection conn = parcial1.java.util.DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.error("Error contando pedidos por cliente", e);
        }
        return 0;
    }

    @Override
    public void crear(Cliente cliente) {
        String sql = "INSERT INTO clientes (nombre, email, fecha_registro) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getEmail());
            stmt.setDate(3, Date.valueOf(cliente.getFechaRegistro()));
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setId(rs.getInt(1));
                }
            }
            LOGGER.info("Cliente creado: {}", cliente);
        } catch (SQLException e) {
            LOGGER.error("Error al crear cliente", e);
        }
    }

    @Override
    public Cliente buscarPorId(int id) {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getDate("fecha_registro").toLocalDate()
                    );
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error al buscar cliente", e);
        }
        return null;
    }

    @Override
    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Cliente(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("email"),
                    rs.getDate("fecha_registro").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            LOGGER.error("Error al listar clientes", e);
        }
        return lista;
    }

    @Override
    public void actualizar(Cliente cliente) {
        String sql = "UPDATE clientes SET nombre=?, email=?, fecha_registro=? WHERE id=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getEmail());
            stmt.setDate(3, Date.valueOf(cliente.getFechaRegistro()));
            stmt.setInt(4, cliente.getId());
            stmt.executeUpdate();
            LOGGER.info("Cliente actualizado: {}", cliente);
        } catch (SQLException e) {
            LOGGER.error("Error al actualizar cliente", e);
        }
    }

    @Override
    public boolean eliminar(int id) {
        return super.eliminar(id);
    }
}
