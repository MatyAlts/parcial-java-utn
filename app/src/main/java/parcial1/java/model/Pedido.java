package parcial1.java.model;

import java.time.LocalDate;

public class Pedido {
    private int id;
    private LocalDate fecha;
    private double monto;
    private int clienteId;

    public Pedido() {}

    public Pedido(int id, LocalDate fecha, double monto, int clienteId) {
        this.id = id;
        this.fecha = fecha;
        this.monto = monto;
        this.clienteId = clienteId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", monto=" + monto +
                ", clienteId=" + clienteId +
                '}';
    }
}
