/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.api.personas;

/**
 *
 * @author jmrunge
 */
public enum CondicionIVA {
    CONSUMIDOR_FINAL("Consumidor Final"),
    RESPONSABLE_INSCRIPTO("Responsable Inscripto"),
    MONOTRIBUTO("Monotributo"),
    EXENTO("Exento");
    private String descripcion;
    
    private CondicionIVA(String descripcion) {
        this.descripcion = descripcion;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
}
