/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.api.personas;

/**
 *
 * @author jmrunge
 */
public enum Sexo {
    MASCULINO("Masculino"),
    FEMENINO("Femenino");
    private String descripcion;
    
    private Sexo(String descripcion) {
        this.descripcion = descripcion;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
}
