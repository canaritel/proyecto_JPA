package entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author telev
 */
@Entity
@Table(name = "distribuye")
@NamedQueries({
    @NamedQuery(name = "Distribuye.findAll", query = "SELECT d FROM Distribuye d"),
    @NamedQuery(name = "Distribuye.findByIdDistribuidor", query = "SELECT d FROM Distribuye d WHERE d.idDistribuidor = :idDistribuidor"),
    @NamedQuery(name = "Distribuye.findByDireccion", query = "SELECT d FROM Distribuye d WHERE d.direccion = :direccion"),
    @NamedQuery(name = "Distribuye.findByCiudad", query = "SELECT d FROM Distribuye d WHERE d.ciudad = :ciudad"),
    @NamedQuery(name = "Distribuye.findByPais", query = "SELECT d FROM Distribuye d WHERE d.pais = :pais")})
public class Distribuye implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idDistribuidor")
    private String idDistribuidor;
    @Basic(optional = false)
    @Column(name = "direccion")
    private String direccion;
    @Basic(optional = false)
    @Column(name = "ciudad")
    private String ciudad;
    @Basic(optional = false)
    @Column(name = "pais")
    private String pais;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "distribuidor")
    private List<Juego> juegoList;

    public Distribuye() {
    }

    public Distribuye(String idDistribuidor) {
        this.idDistribuidor = idDistribuidor;
    }

    public Distribuye(String idDistribuidor, String direccion, String ciudad, String pais) {
        this.idDistribuidor = idDistribuidor;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.pais = pais;
    }

    public String getIdDistribuidor() {
        return idDistribuidor;
    }

    public void setIdDistribuidor(String idDistribuidor) {
        this.idDistribuidor = idDistribuidor;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public List<Juego> getJuegoList() {
        return juegoList;
    }

    public void setJuegoList(List<Juego> juegoList) {
        this.juegoList = juegoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDistribuidor != null ? idDistribuidor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Distribuye)) {
            return false;
        }
        Distribuye other = (Distribuye) object;
        if ((this.idDistribuidor == null && other.idDistribuidor != null) || (this.idDistribuidor != null && !this.idDistribuidor.equals(other.idDistribuidor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.idDistribuidor;
    }

}
