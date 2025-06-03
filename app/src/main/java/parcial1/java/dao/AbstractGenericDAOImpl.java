package parcial1.java.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import parcial1.java.util.LogUtil;

public abstract class AbstractGenericDAOImpl<T, K> implements GenericDAO<T, K> {
    protected abstract String getTableName();
    protected abstract T mapResultSet(ResultSet rs) throws SQLException;
    protected abstract void setInsertParams(PreparedStatement stmt, T entidad) throws SQLException;
    protected abstract void setUpdateParams(PreparedStatement stmt, T entidad) throws SQLException;
    protected abstract String getIdColumn();
    protected abstract void setIdParam(PreparedStatement stmt, K id) throws SQLException;

    @Override
    public T buscarPorId(K id) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE " + getIdColumn() + " = ?";
        try (Connection conn = parcial1.java.util.DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setIdParam(stmt, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            LogUtil.LOGGER.error("Error en buscarPorId", e);
        }
        return null;
    }

    @Override
    public List<T> listarTodos() {
        List<T> lista = new ArrayList<>();
        String sql = "SELECT * FROM " + getTableName();
        try (Connection conn = parcial1.java.util.DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            LogUtil.LOGGER.error("Error en listarTodos", e);
        }
        return lista;
    }

    @Override
    public boolean eliminar(K id) {
        String sql = "DELETE FROM " + getTableName() + " WHERE " + getIdColumn() + " = ?";
        try (Connection conn = parcial1.java.util.DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setIdParam(stmt, id);
            int filas = stmt.executeUpdate();
            if (filas > 0) {
                LogUtil.LOGGER.info("Eliminado de {} con id {}", getTableName(), id);
                return true;
            } else {
                LogUtil.LOGGER.warn("No se encontró {} con id {} para eliminar", getTableName(), id);
                return false;
            }
        } catch (SQLException e) {
            LogUtil.LOGGER.error("Error en eliminar", e);
            return false;
        }
    }
}
