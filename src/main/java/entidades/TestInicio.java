package entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author telev
 */
@Entity
@Table(name = "test_inicio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TestInicio.findAll", query = "SELECT t FROM TestInicio t"),
    @NamedQuery(name = "TestInicio.findByIdInicio", query = "SELECT t FROM TestInicio t WHERE t.idInicio = :idInicio"),
    @NamedQuery(name = "TestInicio.findByTexto", query = "SELECT t FROM TestInicio t WHERE t.texto = :texto")})
public class TestInicio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_inicio")
    private Integer idInicio;
    @Basic(optional = false)
    @Column(name = "texto")
    private String texto;

    public TestInicio() {
    }

    public TestInicio(Integer idInicio) {
        this.idInicio = idInicio;
    }

    public TestInicio(Integer idInicio, String texto) {
        this.idInicio = idInicio;
        this.texto = texto;
    }

    public Integer getIdInicio() {
        return idInicio;
    }

    public void setIdInicio(Integer idInicio) {
        this.idInicio = idInicio;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idInicio != null ? idInicio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TestInicio)) {
            return false;
        }
        TestInicio other = (TestInicio) object;
        if ((this.idInicio == null && other.idInicio != null) || (this.idInicio != null && !this.idInicio.equals(other.idInicio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.TestInicio[ idInicio=" + idInicio + " ]";
    }
    
}
