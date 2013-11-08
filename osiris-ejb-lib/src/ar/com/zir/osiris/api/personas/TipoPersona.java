/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.api.personas;

/**
 *
 * @author jmrunge
 */
public enum TipoPersona {
    FISICA("Persona Física"),
    JURIDICA("Persona Jurídica");
    private String descripcion;
    
    private TipoPersona(String descripcion) {
        this.descripcion = descripcion;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
}
