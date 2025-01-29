public class Schueler extends Person {
  private int alter;
  
  private int jahrgangsstufe;
  
  private Kurs lk1;
  
  public Schueler(String vorname, String nachname, int alter, int jahrgangsstufe)
  {
    super(vorname, nachname); // Ruft den Konstr. der Oberklasse auf.
    this.alter = alter;
    this.jahrgangsstufe = jahrgangsstufe;
  }
  
  
  // Ende Attribute
  
  // Anfang Methoden


  public int getAlter() {
    return alter;
  }
  
  public void setAlter(int alter)
  {
    if(alter < 0 || alter >150)
    {
      this.alter = 12;
    }
    else {
      this.alter = alter;     
    } // end of if-else
    
  }

  

  public int getJahrgangsstufe() {
    return jahrgangsstufe;
  }

  public void versetze() {
    jahrgangsstufe++;
  }
  

  public Kurs getLk1() {
    return lk1;
  }

  public void setLk1(Kurs lk1Neu) {
    lk1 = lk1Neu;
  }
  
  public static void main(String[] args)
  {
  }

  // Ende Methoden
} // end of Schueler
