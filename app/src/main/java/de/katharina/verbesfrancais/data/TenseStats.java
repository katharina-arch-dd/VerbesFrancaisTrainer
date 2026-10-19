package de.katharina.verbesfrancais.data;

public class TenseStats {
    public String tense;
    public int total;
    public int wrong;

    public TenseStats(String tense, int total, int wrong) {
        this.tense = tense;
        this.total = total;
        this.wrong = wrong;
    }
}


