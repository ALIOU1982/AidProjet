import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PageVisite {

	private String source;
	private int nbVisit;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getNbVisit() {
		return nbVisit;
	}

	public void setNbVisit(int nbVisit) {
		this.nbVisit = nbVisit;
	}

	public PageVisite(String source, int nbVisit) {
		this.source = source;
		this.nbVisit = nbVisit;
	}

	public int  AjouterVisit(int val1, int val2) {
		return val1 + val2;
	}



	public PageVisite() {
	}
	
	//Méthode qui pemet de compter le nombre de fois qu'une page a été source
	public List<PageVisite> NombreOccurancesPages(Map<Navigation, Integer> mapOcc){
		List<PageVisite> visitP = new ArrayList<PageVisite>();
		List<String> tab = new ArrayList<String>();
		for (Map.Entry<Navigation, Integer> entrySet : mapOcc.entrySet()){
			if(!tab.contains(entrySet.getKey().getSource())){
				PageVisite p = new PageVisite(entrySet.getKey().getSource(), entrySet.getValue());
				visitP.add(p);
				tab.add(entrySet.getKey().getSource());
			}
			else{ 
				int i = 0;
				boolean sortie = false;
				while(i < visitP.size() && !sortie){
					if(visitP.get(i).getSource().equals(entrySet.getKey().getSource())){
						visitP.get(i).setNbVisit(AjouterVisit(visitP.get(i).getNbVisit(), entrySet.getValue()));// += entrySet.getKey().getValue();
						sortie = true;
					}
					i++;
				}
			}
		}
		return visitP;
	}
	//Méthode qui pemet d'afficher le nombre de fois qu'une page a été source
	public void AffichagePageVisit(List<PageVisite> visitP){
		for(int i = 0; i < visitP.size(); i++){
			System.out.println(visitP.get(i).getSource()+" --> "+visitP.get(i).getNbVisit());
		}
	}
}
