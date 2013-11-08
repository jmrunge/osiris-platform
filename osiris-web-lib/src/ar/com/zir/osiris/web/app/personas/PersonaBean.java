/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.web.app.personas;

import ar.com.zir.osiris.api.personas.profiles.ProfileInterface;
import ar.com.zir.osiris.api.personas.*;
import ar.com.zir.osiris.api.security.SystemOption;
import ar.com.zir.osiris.api.security.SystemOptionSecurityValue;
import ar.com.zir.osiris.api.security.SystemUser;
import ar.com.zir.osiris.ejb.exceptions.ValidationException;
import ar.com.zir.osiris.ejb.services.OsirisConfig;
import ar.com.zir.osiris.ejb.services.PersonaService;
import ar.com.zir.osiris.ejb.services.SeguridadService;
import ar.com.zir.osiris.web.app.AbmBean;
import ar.com.zir.osiris.web.app.personas.security.DuplicatedSessionException;
import java.io.Serializable;
import java.util.*;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.NodeSelectEvent;

/**
 *
 * @author jmrunge
 */
@Named(value = "personaBean")
@ConversationScoped
public class PersonaBean extends AbmBean implements Serializable {
    private Calle calle;
    private String numero;
    private String unidadFuncional;
    private String codigoPostal;
    private String observaciones;
    private Direccion dir;
    private TipoDireccion tipoDireccion;
    private Localidad localidad;
    private String direccion;
    private Pais pais;
    private String provincia;
    private String localidadString;
    private ContactoPersona contactoPersona;
    private TipoContacto tipoContacto;
    private String contacto;
    private TipoPersona tipoPersona;
    private Map<String, ProfileInterface> perfilesDisponibles;
    private Map<String, ProfileInterface> perfilesUtilizados;
    private String tipoPerfil;
    private boolean profilesInited = false;
    private ProfileFactory profileFactory;
    @Inject
    private PersonaService personaService;
    @Inject
    private OsirisConfig config;
    @Inject
    private SeguridadService seguridadService;

    /** Creates a new instance of PersonaBean */
    public PersonaBean() {
    }

    @Override
    protected Object getNewObject() {
        if (tipoPersona.equals(TipoPersona.FISICA))
            return getNuevaPersonaFisica();
        else if (tipoPersona.equals(TipoPersona.JURIDICA))
            return getNuevaPersonaJuridica();
        else
            throw new UnsupportedOperationException("Tipo de persona inválido");
    }

    @Override
    protected boolean isNew(Object object) {
        return ((Persona)object).getId() == null;
    }

    @Override
    protected Object createObject(Object object) throws Exception {
        return personaService.createPersona((Persona)object, perfilesUtilizados.values(), getSystemUser());
    }

    @Override
    protected Object updateObject(Object object) throws Exception {
        return personaService.updatePersona((Persona)object, perfilesUtilizados.values(), getSystemUser());
    }

    @Override
    protected void deleteObject(Object object) throws Exception {
        personaService.deletePersona((Persona)object, getSystemUser());
    }

    @Override
    protected Object refreshObject(Object obj) {
        return obj;
    }

    @Override
    protected String getGetterMethod() {
        return "getPersona";
    }

    @Override
    protected String getSetterMethod() {
        return "setPersona";
    }

    @Override
    protected String getHolderName() {
        return "personaHolder";
    }

    @Override
    public String newObject() throws DuplicatedSessionException {
        String result = super.newObject();
        initPerfilesDisponibles();
        return result;
    }

    @Override
    public void selectObject(NodeSelectEvent evt) throws DuplicatedSessionException {
        super.selectObject(evt);
        initPerfilesDisponibles();
        if (getObject() != null) {
            List<String> keys = new ArrayList<>();
            for (String key : perfilesDisponibles.keySet()) {
                keys.add(key);
            }
            for (String key : keys) {
                ProfileInterface profile = perfilesDisponibles.get(key);
                Object aux = personaService.getProfileForPersona((Persona)getObject(), key, getSystemUser());
                if (aux != null) {
                    profile.setObject(aux);
                    perfilesDisponibles.remove(key);
                    perfilesUtilizados.put(key, profile);
                }
            }
        }
    }
    
    public List<Direccion> getDomiciliosParticulares() {
        return getDomicilios(TipoDireccion.PARTICULAR);
    }
    
    public List<Direccion> getDomiciliosComerciales() {
        return getDomicilios(TipoDireccion.COMERCIAL);
    }
    
    public List<Direccion> getDomiciliosPostales() {
        return getDomicilios(TipoDireccion.POSTAL);
    }
    
    public List<Direccion> getDomiciliosLegales() {
        return getDomicilios(TipoDireccion.LEGAL);
    }
    
    private List<Direccion> getDomicilios(TipoDireccion tipo) {
        List<Direccion> dirs = new ArrayList<>();
        Persona p = (Persona) getObject();
        if (p != null && p.getDireccionCollection() != null) {
            for (Direccion d : p.getDireccionCollection()) {
                if (d.getTipoDireccion().equals(tipo))
                    dirs.add(d);
            }
        }
        return dirs;        
    }
    
    public void cleanDomicilio() {
        calle = null;
        cleanCalle();
        numero = null;
        unidadFuncional = null;
        localidad = null;
        cleanLocalidad();
        direccion = null;
        codigoPostal = null;
        observaciones = null;
        tipoDireccion = null;
        pais = null;
        provincia = null;
        localidadString = null;
        dir = null;
    }
    
    public void addDomicilio() {
        dir.setCodigoPostal(codigoPostal);
        dir.setObservaciones(observaciones);
        dir.setTipoDireccion(tipoDireccion);
        if (dir instanceof DireccionTipificada) {
            DireccionTipificada dt = (DireccionTipificada)dir;
            dt.setCalle(calle);
            dt.setNumero(numero);
            dt.setUnidadFuncional(unidadFuncional);
            dir = dt;
        } else if (dir instanceof DireccionSinCalle) {
            DireccionSinCalle dsc = (DireccionSinCalle) dir;
            dsc.setLocalidad(localidad);
            dsc.setDireccion(direccion);
            dir = dsc;
        } else if (dir instanceof DireccionInternacional) {
            DireccionInternacional di = (DireccionInternacional) dir;
            di.setPais(pais);
            di.setDireccion(direccion);
            di.setLocalidad(localidadString);
            di.setProvincia(provincia);
            dir = di;
        }
        try {
            personaService.validateDireccion(dir);
            ((Persona)getObject()).addDireccion(dir);
        } catch (ValidationException ex) {
            showMessage(ex.getMessage(), false);
        }
        cleanDomicilio();
    }
    
    public void removeDomicilio(Direccion dir) {
        ((Persona)getObject()).getDireccionCollection().remove(dir);
    }

    public void cleanCalle() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("calleHolder", null);
    }

    public void selectCalle() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("calleHolder", this);
    }

    public void cleanLocalidad() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("localidadHolder", null);
    }

    public void selectLocalidad() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("localidadHolder", this);
    }

    public void cleanPais() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paisHolder", null);
    }

    public void selectPais() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paisHolder", this);
    }

    public Localidad getLocalidad() {
        return localidad;
    }

    public void setLocalidad(Localidad localidad) {
        this.localidad = localidad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Calle getCalle() {
        return calle;
    }

    public void setCalle(Calle calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getUnidadFuncional() {
        return unidadFuncional;
    }

    public void setUnidadFuncional(String unidadFuncional) {
        this.unidadFuncional = unidadFuncional;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public String getLocalidadString() {
        return localidadString;
    }

    public void setLocalidadString(String localidadString) {
        this.localidadString = localidadString;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }
    
    public void selectDireccion(int tipo) {
//        System.out.println("ENTRO Y LA PUTA MADRE!");
        switch (tipo) {
            case 0:
                dir = new DireccionTipificada();
                break;
            case 1:
                dir = new DireccionSinCalle();
                break;
            case 2:
                dir = new DireccionInternacional();
                break;
        }
    }

    public TipoDireccion getTipoDireccion() {
        return tipoDireccion;
    }

    public void setTipoDireccion(TipoDireccion tipoDireccion) {
        this.tipoDireccion = tipoDireccion;
    }
    
    public Collection<TipoDireccion> getTipoDirecciones() {
        return Arrays.asList(TipoDireccion.values());
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public Collection<ContactoPersona> getEmails() {
        return getContactos(0);
    }
    
    public Collection<ContactoPersona> getTelefonos() {
        return getContactos(1);
    }
    
    public Collection<ContactoPersona> getWebs() {
        return getContactos(2);
    }
    
    private Collection<ContactoPersona> getContactos(int tipo) {
        Collection<ContactoPersona> contactos = new ArrayList<>();
        Persona p = (Persona) getObject();
        if (p != null && p.getContactoPersonaCollection() != null) {
            for (ContactoPersona cp : p.getContactoPersonaCollection()) {
                if ((tipo == 0) && (cp instanceof EmailPersona)) 
                        contactos.add(cp);
                else if ((tipo == 1) && (cp instanceof TelefonoPersona))
                    contactos.add(cp);
                else if ((tipo == 2) && (cp instanceof WebPersona))
                    contactos.add(cp);
            }
        }
        return contactos;
    }
    
    public void removeContacto(ContactoPersona contacto) {
        ((Persona)getObject()).getContactoPersonaCollection().remove(contacto);
    }
    
    public String getContactoDiagHeader() {
        return "Agregar " + getContactoLabel();
    }
    
    public TipoContacto getTipoContacto() {
        return tipoContacto;
    }
    
    public void setTipoContacto(TipoContacto tipo) {
        tipoContacto = tipo;
    }
    
    public Collection<TipoContacto> getTipoContactos() {
        return Arrays.asList(TipoContacto.values());
    }
    
    public String getContactoLabel() {
        if (contactoPersona instanceof EmailPersona)
            return "Email";
        else if (contactoPersona instanceof TelefonoPersona)
            return "Teléfono";
        else if (contactoPersona instanceof WebPersona)
            return "Página Web";
        else
            return null;
    }
    
    public String getContacto() {
        return contacto;
    }
    
    public void setContacto(String contacto) {
        this.contacto = contacto;
    }
    
    public void addContacto() {
        contactoPersona.setTipo(tipoContacto);
        if (contactoPersona instanceof EmailPersona) {
            EmailPersona ep = (EmailPersona)contactoPersona;
            ep.setEmail(contacto);
            contactoPersona = ep;
        } else if (contactoPersona instanceof TelefonoPersona) {
            TelefonoPersona tp = (TelefonoPersona) contactoPersona;
            tp.setTelefono(contacto);
            contactoPersona = tp;
        } else if (contactoPersona instanceof WebPersona) {
            WebPersona wp = (WebPersona) contactoPersona;
            wp.setUrl(contacto);
            contactoPersona = wp;
        }
        try {
            personaService.validateContacto(contactoPersona);
            ((Persona)getObject()).addContactoPersona(contactoPersona);
        } catch (ValidationException ex) {
            showMessage(ex.getMessage(), false);
        }
        cleanDomicilio();
    }
    
    public void selectContacto(int tipo) {
        switch (tipo) {
            case 0:
                contactoPersona = new EmailPersona();
                break;
            case 1:
                contactoPersona = new TelefonoPersona();
                break;
            case 2:
                contactoPersona = new WebPersona();
                break;
        }
    }
    
    public void cleanContacto() {
        contacto = null;
        tipoContacto = null;
        contactoPersona = null;
    }
    
    private PersonaFisica getNuevaPersonaFisica() {
        PersonaFisica pf = new PersonaFisica();
        pf.setCondicionIVA(CondicionIVA.CONSUMIDOR_FINAL);
        return pf;
    }

    private PersonaJuridica getNuevaPersonaJuridica() {
        return new PersonaJuridica();
    }

    public TipoPersona getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(TipoPersona tipoPersona) {
        this.tipoPersona = tipoPersona;
    }
    
    public Collection<TipoPersona> getTipoPersonas() {
        return Arrays.asList(TipoPersona.values());
    }
    
    public void cleanTipoPersona() {
        tipoPersona = null;
    }
    
    public Boolean isPersonaFisica() {
        if (getObject() == null) return true;
        else return (getObject() instanceof PersonaFisica);
    }
    
    public Collection<CondicionIVA> getCondicionesIVA() {
        Collection<CondicionIVA> condiciones = new ArrayList<>();
        if (getObject() != null) {
            if (getObject() instanceof PersonaFisica) {
                condiciones.add(CondicionIVA.CONSUMIDOR_FINAL);
            } else {
                condiciones.add(CondicionIVA.RESPONSABLE_INSCRIPTO);
                condiciones.add(CondicionIVA.MONOTRIBUTO);
                condiciones.add(CondicionIVA.EXENTO);
            }
        }
        return condiciones;
    }
    
    public CondicionIVA getCondicionIVA() {
        if (getObject() == null) return null;
        else return ((Persona)getObject()).getCondicionIVA();
    }
    
    public void setCondicionIVA(CondicionIVA condicionIVA) {
        if (getObject() != null)
            ((Persona)getObject()).setCondicionIVA(condicionIVA);
    }
    
    public Date getMaxDate() {
        return Calendar.getInstance().getTime();
    }
    
    public Sexo getSexo() {
        if (getObject() == null) return null;
        else return ((PersonaFisica)getObject()).getSexo();
    }
    
    public void setSexo(Sexo sexo) {
        if (getObject() != null)
            ((PersonaFisica)getObject()).setSexo(sexo);
    }
    
    public Collection<Sexo> getSexos() {
        return Arrays.asList(Sexo.values());
    }
    
    public boolean getRenderProfiles() throws DuplicatedSessionException {
        if (!profilesInited)
            initPerfilesDisponibles();
        if (perfilesDisponibles.isEmpty()) {
            if (perfilesUtilizados == null) {
                return false;
            } else {
                if (perfilesUtilizados.isEmpty()) {
                    return false;
                } else {
                    return true;
                }
            }
        } else {
            if (perfilesDisponibles.get("none") != null) {
                return false;
            } else {
                return true;
            }
        }
    }
    
    public boolean canAddMoreProfiles() throws DuplicatedSessionException {
        if (!profilesInited)
            initPerfilesDisponibles();
        return !getTipoPerfiles().isEmpty();
    }
    
    private void initPerfilesDisponibles() {
        perfilesDisponibles = new HashMap<>();
        perfilesUtilizados = new HashMap<>();
        if (getObject() != null) {
            profileFactory = new ProfileFactory(config);
            boolean isPersonaFisica = true;
            if (getObject() instanceof PersonaJuridica)
                isPersonaFisica = false;
            for (String key : profileFactory.getProfiles().keySet()) {
                ProfileInterface profile = profileFactory.getProfiles().get(key);
                if (isPersonaFisica && profile.isForPersonaFisica())
                    perfilesDisponibles.put(key, profile);
                else if (!isPersonaFisica && profile.isForPersonaJuridica())
                    perfilesDisponibles.put(key, profile);
            }
            profilesInited = true;
        }
    }
    
    public String getTipoPerfil() {
        return tipoPerfil;
    }
    
    public void setTipoPerfil(String tipo) {
        tipoPerfil = tipo;
    }
    
    public Collection<String> getTipoPerfiles() throws DuplicatedSessionException {
        if (!profilesInited)
            initPerfilesDisponibles();
        Collection<String> profiles = new ArrayList<>();
        for (String key : perfilesDisponibles.keySet()) {
            ProfileInterface profile = perfilesDisponibles.get(key);
            if (canDoIt(profile.getPermisoName())) 
                profiles.add(key);
        }
        return profiles;
    }
    
    public void addPerfil() {
        if (perfilesUtilizados == null)
            perfilesUtilizados = new HashMap<>();
        ProfileInterface profile = perfilesDisponibles.get(tipoPerfil);
        profile.newObject((Persona)getObject());
        perfilesUtilizados.put(tipoPerfil, profile);
        perfilesDisponibles.remove(tipoPerfil);
    }
    
    public void cleanPerfil() {
        tipoPerfil = null;
    }
    
    public boolean isProfileVisible(String profile) {
        if (perfilesUtilizados == null) return false;
        return perfilesUtilizados.get(profile) != null;
    }
    
    public boolean showTabView() {
        if (perfilesUtilizados == null || perfilesUtilizados.isEmpty())
            return false;
        else
            return true;
    }
    
    public Object getProfileObject(String profile) {
        if (perfilesUtilizados == null) return null;
        return perfilesUtilizados.get(profile).getObject();
    }
    
    public boolean showProfileContent(String profile) throws DuplicatedSessionException {
        if (perfilesUtilizados == null) return false;
        String permiso = perfilesUtilizados.get(profile).getPermisoName();
        return canDoIt(permiso);
    }

    private boolean canDoIt(String optionCode) throws DuplicatedSessionException {
        SystemOption so = seguridadService.getSystemOption("PERSONA", getSystemUser());
        SystemUser su = getSystemUser();

        for (SystemOptionSecurityValue sosv : su.getSystemOptionSecurityValues(so.getCodigo())) {
            if (sosv.getSystemOptionSecurityOption().getSystemOptionSecurity().getOptionCode().toLowerCase().trim().equals(optionCode.toLowerCase())) {
                return sosv.getCanDoIt();
            }
        }
        return false;
    }
    
}
