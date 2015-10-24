import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Navigation {

	private String source;
	private String cible;
	private int occ;

	public Navigation(String source, String cible, int occ) {
		this.source = source;
		this.cible = cible;
		this.occ = occ;
	}

	public Navigation() {
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getCible() {
		return cible;
	}

	public void setCible(String cible) {
		this.cible = cible;
	}

	public int getOcc() {
		return occ;
	}

	public void setOcc(int occ) {
		this.occ = occ;
	}

	//définition d'une méthode permettant de comparer deux transitions de navigation
	public boolean equalss(Navigation x) {
		return x.source.hashCode() == this.source.hashCode()&& x.cible.hashCode() == this.cible.hashCode();
	}

	//comparaison de deux sources de navigations
	public int CompareToNav(Navigation nav){
		int resultat;
		if(this.source.compareTo(nav.source)>0)
			resultat = 1;
		else if(this.source.compareTo(nav.source)<0)
			resultat = - 1;
		else resultat = 0;
		return resultat;
	}

	//Constitution des pages consultées par chaque seqID
	public Map<Integer, List<Navigation>> pageconsulteparID(List<PageWeb> pages){
		Map<Integer, List<Navigation>> nav = new HashMap<Integer, List<Navigation>>();
		int sqID = pages.get(0).getSeqID();
		List<Navigation> navs = new ArrayList<Navigation>();
		for(int i = 1; i < pages.size(); i++){			
			if(pages.get(i).getSeqID()==sqID){
				int occ = 0;
				if(!pages.get(i-1).getPage().equals(pages.get(i).getPage())){
					Navigation n = new Navigation(pages.get(i-1).getPage(), pages.get(i).getPage(), ++occ);
					//System.out.println(sqID);
					navs.add(n);
					if(i==pages.size()-1){
						int occ1=0;
						Navigation n2 = new Navigation(pages.get(i).getPage(), "sortie", ++occ1);
						navs.add(n2);
						nav.put(sqID, navs);
					}
				}
			}
			else{
				int occ=0;
				Navigation n2 = new Navigation(pages.get(i-1).getPage(), "sortie", ++occ);
				navs.add(n2);
				nav.put(sqID, navs);
				sqID = pages.get(i).getSeqID();
				navs = new ArrayList<Navigation>();
			}

		}
		return nav;
	}

	// Affichage des différentes transition avec leurs occurrances pour chaque seqID
	public void AffichageNavigation(Map<Integer, List<Navigation>> navs){
		navs = doSort(navs);
		for (Map.Entry<Integer, List<Navigation>> entrySet : navs.entrySet()) {
			Integer k = entrySet.getKey();
			List<Navigation> val = navs.get(k);
			System.out.println(k+" ------> ");
			for(int i = 0; i < val.size(); i++){
				System.out.println("\t\t"+val.get(i).getSource()+" ==> "+val.get(i).getCible()+" : "+val.get(i).getOcc());
			}
			System.out.println();
		}
	}

	//Méthode qui pemet trier les navigations en fonction de leurs nombres d'occurrances
	public Map<Integer, List<Navigation>>  doSort(Map<Integer, List<Navigation>>  map) {
		Map<Integer, List<Navigation>>  sortedMap = new TreeMap<Integer, List<Navigation>> ();
		sortedMap.putAll(map);
		return sortedMap;
	}

	//Méthode qui pemert de liste toutes les navigationns
	public List<Navigation> lesPageSourceCible(Map<Integer, List<Navigation>>  map){
		List<Navigation> listNav = new ArrayList<Navigation>();		
		for (Map.Entry<Integer, List<Navigation>> entrySet : map.entrySet()) {
			List<Navigation> val = entrySet.getValue();
			for(int i = 0; i < val.size(); i++){
				listNav.add(val.get(i));
			}
		}
		return listNav;
	}

	// méthode qui permet de tester si une transition est contenu dans une liste de transitions
	public boolean contenir(List<Navigation> tabou, Navigation nav){
		boolean truv = false;
		int i = 0;
		if(tabou!=null){
			while(!truv && i < tabou.size() ){
				if(tabou.get(i).equalss(nav)){
					truv = true;
				}
				i++;
			}
		}
		return truv;
	}

	//Méthode qui permet de calculer le nombre d'occurances total pour toutes les transitions
	public Map<Navigation, Integer> NombreOccurancesNavigation(Map<Integer, List<Navigation>>  map){
		Map<Navigation, Integer> mapOcc = new HashMap<Navigation, Integer>();
		List<Navigation> list = new ArrayList<Navigation>();
		List<Navigation> tabou = new ArrayList<Navigation>();
		list = lesPageSourceCible(map);
		for(int i = 0; i < list.size(); i++){			
			if(!contenir(tabou, list.get(i))){
				int cpt = list.get(i).getOcc();;
				for(int j = i+1; j < list.size(); j++){
					if(list.get(i).equalss(list.get(j))){
						cpt = cpt + list.get(j).getOcc();
					}
				}
				tabou.add(list.get(i));
				mapOcc.put(list.get(i), cpt);
			}
		}
		return mapOcc;
	}
	
	public Map<Navigation, Float> TrieDesMaps(Map<Navigation, Float> map){
		Map<Navigation, Float> sortedMap = new HashMap<Navigation, Float>();
		List<Navigation> list = new ArrayList<Navigation>();
		for (Navigation str : map.keySet()) {
			list.add(str);
		}
		Collections.sort(list, new Comparator<Navigation>() { 
			public int compare(final Navigation object1, final Navigation object2) {
				return object1.getSource().compareTo(object2.getSource());
			}
		} );
		for(int i = 0; i < list.size(); i++){
			for (Map.Entry<Navigation, Float> entrySet : map.entrySet()) {			
				if(list.get(i).getSource().equals(entrySet.getKey().getSource()) && list.get(i).getCible().equals(entrySet.getKey().getCible())){
					sortedMap.put(entrySet.getKey(), entrySet.getValue());
				}				
			}
		}
		return sortedMap;
	}

	// Affichage de toutes les transitions avec le total des occurances pour l'ensembles des seqID
	public void AffichagePageOcc(Map<Navigation, Integer> mapOcc){
		//Map<Navigation, Integer> sortedMap = new HashMap<Navigation, mapOcc.>();
		//mapOcc = TrieDesMaps(mapOcc);
		List<Navigation> list = new ArrayList<Navigation>();
		for (Navigation str : mapOcc.keySet()) {
			list.add(str);
		}
		Collections.sort(list, new Comparator<Navigation>() {
			public int compare(final Navigation object1, final Navigation object2) {
				return object1.getSource().compareTo(object2.getSource());
			}
		} );
		for(int i = 0; i < list.size(); i++){
			for (Map.Entry<Navigation, Integer> entrySet : mapOcc.entrySet()) {
				if(list.get(i).getSource().equals(entrySet.getKey().getSource()) && list.get(i).getCible().equals(entrySet.getKey().getCible())){
					System.out.println(entrySet.getKey().getSource()+" ==> "+entrySet.getKey().getCible()+" : "+entrySet.getValue());
				}
			}
		}
	}
}
