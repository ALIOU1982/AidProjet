import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

public class PageWeb {

	private int seqID;
	private Date datecon;
	private String page;

	public int getSeqID() {
		return seqID;
	}

	public void setSeqID(int seqID) {
		this.seqID = seqID;
	}

	public Date getDatecon() {
		return datecon;
	}

	public void setDatecon(Date datecon) {
		this.datecon = datecon;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public PageWeb() {
	}

	public PageWeb(int seqID, Date datecon, String page) {
		this.seqID = seqID;
		this.datecon = datecon;
		this.page = page;
	}

	// méthode qui permet de charger le fichier
	public List<PageWeb> lirefichiers(FileReader file){
		StringTokenizer st = null;
		BufferedReader fichier = new BufferedReader(file);
		List<PageWeb> pages = new ArrayList<PageWeb>();
		String ligne;
		try {
			ligne = fichier.readLine();		
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");
			ligne = fichier.readLine();
			while(ligne!=null){
				try{		
					PageWeb pw = new PageWeb();
					st = new StringTokenizer(ligne);
					int i = 0;
					while(st.hasMoreElements()){
						if(i==0){ 
							pw.setSeqID(Integer.parseInt(st.nextElement().toString()));
							i++;
						}
						else if(i==1){
							try {
								pw.setDatecon(df.parse(st.nextToken().toString()+" "+st.nextToken().toString()));
								i++;
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}							
						}
						else{
							pw.setPage(st.nextToken().toString());
						}
					}
					pages.add(pw);
					ligne = fichier.readLine();										
				}
				catch(IOException e1){
					e1.printStackTrace();			
				}			
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pages;
	}

	public int CompareIDTo(PageWeb pg){
		int resultat;
		if(this.seqID > pg.seqID)
			resultat = 1;
		else if(this.seqID < pg.seqID)
			resultat = - 1;
		else resultat = 0;
		return resultat;
	}

	// Trie des pages chargées depuis le fichier .txt par date
	public int CompareDateTo(PageWeb pg){
		int resultat = this.datecon.compareTo(pg.datecon);
		return resultat;
	}

	// Trie des pages chargées depuis le fichier .txt par seqID
	public List<PageWeb> trieDesPagesWebParIDate(List<PageWeb> pages){
		Collections.sort(pages, new Comparator<PageWeb>(){
			public int compare(PageWeb p1, PageWeb p2){
				return p1.CompareIDTo(p2);
			}
		});
		return pages;
	}

	// méthode qui permet de calculer les probabilités de chaque transition existante
	public Map<Navigation , Float> probabilitesPages(Map<Navigation, Integer> mapOcc, List<PageVisite> visitP){
		Map<Navigation, Float> lesProba = new HashMap<Navigation, Float>();
		for (Map.Entry<Navigation, Integer> entrySet : mapOcc.entrySet()) {
			int i = 0;
			boolean sortie = false;
			while(i < visitP.size() && !sortie){
				if(visitP.get(i).getSource().equals(entrySet.getKey().getSource())){
					lesProba.put(entrySet.getKey(),(float)entrySet.getValue()/visitP.get(i).getNbVisit());
					sortie = true;
				}
				i++;
			}
		}
		return lesProba;
	}

	// Affichage de toutes les transitions en pourcentages avec 2 chiffres après la virgule
	public void AffichageProbabilité(Map<Navigation, Float> lesProba) {		
		List<Navigation> list = new ArrayList<Navigation>();
		for (Navigation str : lesProba.keySet()) {
			list.add(str);
		}
		Collections.sort(list, new Comparator<Navigation>() {
			public int compare(final Navigation object1, final Navigation object2) {
				return object1.getSource().compareTo(object2.getSource());
			}
		} );
		DecimalFormat df = new DecimalFormat ( ) ;
		df.setMaximumFractionDigits ( 2 ) ; //arrondi à 2 chiffres apres la virgules
		df.setMinimumFractionDigits ( 2 ) ; 
		for(int i = 0; i < list.size(); i++){
			for (Map.Entry<Navigation, Float> entrySet : lesProba.entrySet()) {
				if(list.get(i).getSource().equals(entrySet.getKey().getSource()) && list.get(i).getCible().equals(entrySet.getKey().getCible())){
					System.out.println(entrySet.getKey().getSource()+" --> "+entrySet.getKey().getCible()+" : "+df.format(entrySet.getValue()*100)+" %");
				}
			}
		}
	}

	// Affichage des meilleurs transitions en pourcentages avec 2 chiffres après la virgule
	public void AffichageBestProbabilite(Map<Navigation, Float> lesProba, List<PageVisite> visitP){
		DecimalFormat df = new DecimalFormat ( ) ;
		df.setMaximumFractionDigits ( 2 ) ; //arrondi à 2 chiffres apres la virgules
		df.setMinimumFractionDigits ( 2 ) ; 
		Vector<String> lisRed = new Vector<String>();	
		Map<Navigation, Float> Best = new HashMap<Navigation, Float>();
		for(int i = 0; i < visitP.size(); i++){
			if(!lisRed.contains(visitP.get(i))){			
				Float temp = Float.MIN_VALUE; 
				Navigation pages = null;
				for (Map.Entry<Navigation, Float> entrySet : lesProba.entrySet()) {
					if(entrySet.getKey().getSource().equals(visitP.get(i).getSource()) && entrySet.getValue()>temp){
						temp = entrySet.getValue();
						pages  = entrySet.getKey();
					}
				}
				Best.put(pages, temp);
				lisRed.add(visitP.get(i).getSource());
			}
		}
		List<Navigation> list = new ArrayList<Navigation>();
		for (Navigation str : Best.keySet()) {
			list.add(str);
		}
		Collections.sort(list, new Comparator<Navigation>() {
			public int compare(final Navigation object1, final Navigation object2) {
				return object1.getSource().compareTo(object2.getSource());
			}
		} );
		for(int i = 0; i < list.size(); i++){
			for (Map.Entry<Navigation, Float> entrySet : Best.entrySet()) {
				if(list.get(i).getSource().equals(entrySet.getKey().getSource()) && list.get(i).getCible().equals(entrySet.getKey().getCible())){
					System.out.println(entrySet.getKey().getSource()+" --> "+entrySet.getKey().getCible()+" : "+df.format(entrySet.getValue()*100)+" %");
				}
			}
		}
	}
}
