package de.katharina.verbesfrancais.practice;
import de.katharina.verbesfrancais.data.*;
public class PracticeEngine {
 private static final long[] I={0,86400000L,259200000L,604800000L,1209600000L,2592000000L,5184000000L};
 public boolean isCorrect(ConjugationForm f,String a){ return a!=null&&norm(f.form).equals(norm(a)); }
 public ReviewProgress updateProgress(ReviewProgress p,boolean ok){ long now=System.currentTimeMillis();
  if(ok){p.correctCount++;p.boxLevel=Math.min(p.boxLevel+1,I.length-1);} else {p.wrongCount++;p.boxLevel=1;}
  p.nextReviewAt=now+I[p.boxLevel]; return p; }
 private String norm(String s){return s==null?"":s.trim().toLowerCase();}
}
