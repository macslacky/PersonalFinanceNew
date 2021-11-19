package info.ribosoft.personalfinancenew.HttpConn;

public class RecyclerListaBanche {
    String strIdBanca, strNomeBanca, strSaldoEntrata, strSaldoUscita;

    // reads the fields
    public String getIdBanca() {return strIdBanca;}
    public String getNomeBanca() {return strNomeBanca;}
    public String getSaldoEntrata() {return strSaldoEntrata;}
    public String getSaldoUscita() {return strSaldoUscita;}

    // writes the fields
    public void setIdBanca(String idBanca) {this.strIdBanca = idBanca;}
    public void setNomeBanca(String nomeBanca) {this.strNomeBanca = nomeBanca;}
    public void setStrSaldoEntrata(String saldoEntrata) {this.strSaldoEntrata = saldoEntrata;}
    public void setStrSaldoUscita(String saldoUscita) {this.strSaldoUscita = saldoUscita;}

    // set the fields
    public RecyclerListaBanche(String idBanca, String nomeBanca, String saldoEntrata,
        String saldoUscita) {
        this.strIdBanca = idBanca;
        this.strNomeBanca = nomeBanca;
        this.strSaldoEntrata = saldoEntrata;
        this.strSaldoUscita = saldoUscita;
    }
}
