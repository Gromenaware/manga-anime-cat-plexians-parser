package cat.plexians.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Informacio {
    @JsonProperty("capitol")
    private int capitol;
    @JsonProperty("data_emissio")
    private String dataEmissio;
    @JsonProperty("data_modificacio")
    private Object dataModificacio;
    @JsonProperty("data_publicacio")
    private Object dataPublicacio;
    @JsonProperty("descripcio")
    private String descripcio;
    @JsonProperty("durada")
    private Durada durada;
    @JsonProperty("id")
    private int id;
    @JsonProperty("programa")
    private String programa;
    @JsonProperty("programa_id")
    private int programaId;
    @JsonProperty("slug")
    private String slug;
    @JsonProperty("temporada")
    private Object temporada;
    @JsonProperty("tipus_contingut")
    private String tipusContingut;
    @JsonProperty("titol")
    private String titol;
    @JsonProperty("titol_complet")
    private String titolComplet;

    // Getters and Setters
    public int getCapitol() {
        return capitol;
    }

    public void setCapitol(int capitol) {
        this.capitol = capitol;
    }

    public String getDataEmissio() {
        return dataEmissio;
    }

    public void setDataEmissio(String dataEmissio) {
        this.dataEmissio = dataEmissio;
    }

    public Object getDataModificacio() {
        return dataModificacio;
    }

    public void setDataModificacio(Object dataModificacio) {
        this.dataModificacio = dataModificacio;
    }

    public Object getDataPublicacio() {
        return dataPublicacio;
    }

    public void setDataPublicacio(Object dataPublicacio) {
        this.dataPublicacio = dataPublicacio;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public Durada getDurada() {
        return durada;
    }

    public void setDurada(Durada durada) {
        this.durada = durada;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public int getProgramaId() {
        return programaId;
    }

    public void setProgramaId(int programaId) {
        this.programaId = programaId;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Object getTemporada() {
        return temporada;
    }

    public void setTemporada(Object temporada) {
        this.temporada = temporada;
    }

    public String getTipusContingut() {
        return tipusContingut;
    }

    public void setTipusContingut(String tipusContingut) {
        this.tipusContingut = tipusContingut;
    }

    public String getTitol() {
        return titol;
    }

    public void setTitol(String titol) {
        this.titol = titol;
    }

    public String getTitolComplet() {
        return titolComplet;
    }

    public void setTitolComplet(String titolComplet) {
        this.titolComplet = titolComplet;
    }

    @Override
    public String toString() {
        return "Informacio{" + "capitol=" + capitol + ", dataEmissio='" + dataEmissio + '\'' + ", dataModificacio=" + dataModificacio + ", dataPublicacio=" + dataPublicacio + ", descripcio='" + descripcio + '\'' + ", durada=" + durada + ", id=" + id + ", programa='" + programa + '\'' + ", programaId=" + programaId + ", slug='" + slug + '\'' + ", temporada=" + temporada + ", tipusContingut='" + tipusContingut + '\'' + ", titol='" + titol + '\'' + ", titolComplet='" + titolComplet + '\'' + '}';
    }
}
