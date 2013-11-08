/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.api.personas;

/**
 *
 * @author jmrunge
 */
public enum TipoContacto {
    PARTICULAR("Particular"),
    COMERCIAL("Comercial"),
    LABORAL("Laboral");
    
    private String descripcion;
    
    private TipoContacto(String descripcion) {
        this.descripcion = descripcion;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
}
