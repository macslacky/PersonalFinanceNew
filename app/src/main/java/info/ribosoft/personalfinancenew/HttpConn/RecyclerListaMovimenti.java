package info.ribosoft.personalfinancenew.HttpConn;

public class RecyclerListaMovimenti {
    String strIdMovimento, strImporto, strData, strValuta, strNote;

    // reads the fields
    public String  getIdMovimento() {return strIdMovimento;}
    public String getImporto() {return strImporto;}
    public String getData() {return strData;}
    public String getValuta() {return strValuta;}
    public String getNote() {return strNote;}

    // writes the fields
    public void setIdMovimento(String idMovimento) {this.strIdMovimento = idMovimento;}
    public void setImporto(String Importo) {this.strImporto = Importo;}
    public void setData(String Data) {this.strData = Data;}
    public void setValuta(String Valuta) {this.strValuta = Valuta;}
    public void setStrNote(String Note) {this.strNote = Note;}

    // set the fields
    public RecyclerListaMovimenti(String idMovimento, String Importo, String Data, String Valuta,
        String Note) {
        this.strIdMovimento = idMovimento;
        this.strImporto = Importo;
        this.strData = Data;
        this.strValuta = Valuta;
        this.strNote = Note;
    }
}
