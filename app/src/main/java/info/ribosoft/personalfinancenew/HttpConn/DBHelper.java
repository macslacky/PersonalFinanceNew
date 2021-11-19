package info.ribosoft.personalfinancenew.HttpConn;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NOME = "dbGestioneConti.db";
    // tabella lista banche
    private static final String TABELLA_BANCHE = "tblListaBanche";
    private static final String BANCHE_ID = "idBanca";
    private static final String BANCHE_NOME = "NomeBanca";
    private static final String BANCHE_SALDOENTRATA = "SaldoEntrata";
    private static final String BANCHE_SALDOUSCITA = "SaldoUscita";
    // tabella lista movimenti
    private static final String TABELLA_MOVIMENTI = "tblListaMovimenti";
    private static final String MOVIMENTI_ID = "idMovimento";
    private static final String MOVIMENTI_IDBANCA = "idBanca";
    private static final String MOVIMENTI_IMPORTO = "Importo";
    private static final String MOVIMENTI_DATA = "Data";
    private static final String MOVIMENTI_VALUTA = "Valuta";
    private static final String MOVIMENTI_NOTE = "Note";

    public DBHelper(Context context) {
        super(context, DATABASE_NOME, null, 1);
    }

    // create the tables
    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        sqlDB.execSQL("CREATE TABLE " + TABELLA_BANCHE + "(" + BANCHE_ID + " INTEGER PRIMARY KEY, "
                + BANCHE_NOME + " TEXT, " + BANCHE_SALDOENTRATA + " TEXT, " + BANCHE_SALDOUSCITA
                + " TEXT)");
        sqlDB.execSQL("CREATE TABLE " + TABELLA_MOVIMENTI + "(" + MOVIMENTI_ID +
                " INTEGER PRIMARY KEY," + MOVIMENTI_IDBANCA + " TEXT, " + MOVIMENTI_IMPORTO +
                " TEXT, " + MOVIMENTI_DATA + " TEXT, " + MOVIMENTI_VALUTA + " TEXT, " +
                MOVIMENTI_NOTE + " TEXT)");
    }

    public void onUpgrade(SQLiteDatabase sqlDB, int oldVersion, int newVersion) {}

    // opens the table and returns the cursor to the first element
    private Cursor openReadTable(String stringSql) {
        // opens the database for reading
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        // performs a read sql query
        Cursor cursor = sqLiteDatabase.rawQuery(stringSql, null);
        // move the cursor to the first row
        cursor.moveToFirst();
        return cursor;
    }

    // writes a new bank in the table
    public void scriveNuovaBanca(String nomeBanca) {
        // prepares the data to write in into the table
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        contentValues.put(BANCHE_NOME, nomeBanca);
        contentValues.put(BANCHE_SALDOENTRATA, "0.00");
        contentValues.put(BANCHE_SALDOUSCITA, "0.00");
        sqlDB.insert(TABELLA_BANCHE, null, contentValues);
    }

    // reads the banks table
    public ArrayList<RecyclerListaBanche> leggiBanche() {
        ArrayList<RecyclerListaBanche> arrayListaBanche = new ArrayList<>();

        // opens the database for reading and move the cursor to the first row
        String stringSql = "SELECT * FROM " + TABELLA_BANCHE + " ORDER BY " + BANCHE_NOME + " ASC";
        Cursor cursor = openReadTable(stringSql);

        int i;
        while (!cursor.isAfterLast()) {
            // returns the value of the requested column as a string
            i = cursor.getColumnIndex(BANCHE_ID);
            String idBanca = cursor.getString(i);
            i = cursor.getColumnIndex(BANCHE_NOME);
            String nomeBanca = cursor.getString(i);
            i = cursor.getColumnIndex(BANCHE_SALDOENTRATA);
            String saldoEntrata = cursor.getString(i);
            i = cursor.getColumnIndex(BANCHE_SALDOUSCITA);
            String saldoUscita = cursor.getString(i);

            double saldoBanca = Double.parseDouble(saldoEntrata) - Double.parseDouble(saldoUscita);

            // add the string to the list
            arrayListaBanche.add(new RecyclerListaBanche(idBanca, nomeBanca, saldoEntrata,
                    saldoUscita));

            // move the cursor to the next row
            cursor.moveToNext();
        }
        return arrayListaBanche;
    }

    // read single bank
    public DBDatiBanca leggiSaldoBanca(String idBanca) {
        // opens the database for reading the indicated movement
        String strSql = "SELECT * FROM " + TABELLA_BANCHE + " WHERE " + BANCHE_ID + " = '" +
                idBanca + "'";
        Cursor cursor = openReadTable(strSql);

        // returns the value of the requested column as a string
        DBDatiBanca dbDatiBanca = new DBDatiBanca();
        int i = cursor.getColumnIndex(BANCHE_NOME);
        dbDatiBanca.strNomeBanca = cursor.getString(i);
        i = cursor.getColumnIndex(BANCHE_SALDOENTRATA);
        dbDatiBanca.strSaldoEntrata = cursor.getString(i);
        i = cursor.getColumnIndex(BANCHE_SALDOUSCITA);
        dbDatiBanca.strSaldoUscita = cursor.getString(i);
        return dbDatiBanca;
    }

    // updates the bank balance
    public void ScriviSaldoBanca(String idBanca, double saldoEntrata, double saldoUscita) {
        // open the database to write the balance for the indicated bank
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        // prepares the data to write it into the table
        ContentValues contentValues = new ContentValues();
        contentValues.put(BANCHE_ID, idBanca);

        ClassFunzioni cFunzioni = new ClassFunzioni();
        contentValues.put(BANCHE_SALDOENTRATA, cFunzioni.strImporto(saldoEntrata));
        contentValues.put(BANCHE_SALDOUSCITA, cFunzioni.strImporto(saldoUscita));
        sqlDB.update(TABELLA_BANCHE, contentValues, BANCHE_ID + "=" + idBanca,
            null);
    }

    // writes a new movment in the table
    public void scriviNuovoMovimento(String idBanca, String importo, String data, String valuta,
        String note) {
        // prepares the data to write it into the table
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MOVIMENTI_IDBANCA, idBanca);
        contentValues.put(MOVIMENTI_IMPORTO, importo);
        ClassFunzioni cFunzioni = new ClassFunzioni();
        contentValues.put(MOVIMENTI_DATA, cFunzioni.filtroData(data));
        contentValues.put(MOVIMENTI_VALUTA, cFunzioni.filtroData(valuta));
        contentValues.put(MOVIMENTI_NOTE, note);
        sqlDB.insert(TABELLA_MOVIMENTI, null, contentValues);
    }

    // updates the movement
    public void aggiornaMovimento(String idMovimento, String importo, String data, String valuta,
        String note) {
        // open the database to write the indicated movement
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MOVIMENTI_IMPORTO, importo);
        contentValues.put(MOVIMENTI_DATA, data);
        contentValues.put(MOVIMENTI_VALUTA, valuta);
        contentValues.put(MOVIMENTI_NOTE, note);
        sqlDB.update(TABELLA_MOVIMENTI, contentValues, MOVIMENTI_ID + "=" +
            idMovimento, null);
    }

    // cancels the indicated movement
    public void CancellaMovimento(String idMovimento) {
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        sqlDB.delete(TABELLA_MOVIMENTI, "idmovimento = ? ", new String[] {idMovimento});
    }

    // read the movements related to a bank
    public ArrayList<RecyclerListaMovimenti> leggiMovimenti(String idBanca, boolean blnFiltro,
        String strInizio, String strFine) {
        ArrayList<RecyclerListaMovimenti> arrayListaMovimenti = new ArrayList<>();

        // opens the database for reading and move the cursor to the first row
        String stringSql = "SELECT * FROM " + TABELLA_MOVIMENTI + " WHERE " + MOVIMENTI_IDBANCA +
            " = '" + idBanca + "'";
        if (blnFiltro) stringSql += " AND " + MOVIMENTI_VALUTA + " >= '" + strInizio + "'" + " AND "
            + MOVIMENTI_VALUTA + " <= '" + strFine + "'";
        stringSql += " ORDER BY " + MOVIMENTI_VALUTA + " ASC";
        Cursor cursor = openReadTable(stringSql);

        int i;
        while (!cursor.isAfterLast()) {
            // returns the value of the requested column as a string
            i = cursor.getColumnIndex(MOVIMENTI_ID);
            String idMovimento = cursor.getString(i);
            ClassFunzioni cFunzioni = new ClassFunzioni();
            i = cursor.getColumnIndex(MOVIMENTI_DATA);
            String dataMovimento = cFunzioni.filtroData(cursor.getString(i));
            i = cursor.getColumnIndex(MOVIMENTI_VALUTA);
            String dataValuta = cFunzioni.formatData(cursor.getString(i));
            i = cursor.getColumnIndex(MOVIMENTI_IMPORTO);
            String importo = cursor.getString(i);
            i = cursor.getColumnIndex(MOVIMENTI_NOTE);
            String note = cursor.getString(i);

            // add the string to the list
            arrayListaMovimenti.add(new RecyclerListaMovimenti(idMovimento, importo,dataMovimento,
                dataValuta, note));

            // move the cursor to the next row
            cursor.moveToNext();
        }
        return arrayListaMovimenti;
    }

    // read single movement
    public RecyclerListaMovimenti leggiSingoloMov(String idMovimento) {
        String  strImporto="", strData="", strValuta="", strNote="";
        int i;
        // opens the database for reading the indicated movement
        String strSql = "SELECT * FROM " + TABELLA_MOVIMENTI + " WHERE " + MOVIMENTI_ID + " = '" +
            idMovimento + "'";
        Cursor cursor = openReadTable(strSql);

        // returns the value of the requested column as a string
        RecyclerListaMovimenti dbDatiMovimento =new RecyclerListaMovimenti(idMovimento, strImporto,
            strData, strValuta, strNote);
        ClassFunzioni cFunzioni = new ClassFunzioni();
        i = cursor.getColumnIndex(MOVIMENTI_DATA);
        dbDatiMovimento.strData = cFunzioni.formatData(cursor.getString(i));
        i = cursor.getColumnIndex(MOVIMENTI_VALUTA);
        dbDatiMovimento.strValuta = cFunzioni.formatData(cursor.getString(i));
        i = cursor.getColumnIndex(MOVIMENTI_IMPORTO);
        dbDatiMovimento.strImporto = cursor.getString(i);
        i = cursor.getColumnIndex(MOVIMENTI_NOTE);
        dbDatiMovimento.strNote = cursor.getString(i);
        return dbDatiMovimento;
    }
}
