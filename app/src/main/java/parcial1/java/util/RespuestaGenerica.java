package parcial1.java.util;

public class RespuestaGenerica<T> {
    private final boolean exito;
    private final String mensaje;
    private final T dato;

    public RespuestaGenerica(boolean exito, String mensaje, T dato) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.dato = dato;
    }

    public boolean isExito() { return exito; }
    public String getMensaje() { return mensaje; }
    public T getDato() { return dato; }
}
