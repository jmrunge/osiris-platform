/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.api.personas;

/**
 *
 * @author jmrunge
 */
public enum TipoDireccion {
    PARTICULAR("Particular"),
    COMERCIAL("Comercial"),
    POSTAL("Postal"),
    LEGAL("Legal");
    
    private String descripcion;
    
    private TipoDireccion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
}
