import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;



/*import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;*/

public class Programme {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
		FileReader file = new FileReader(args[0]);
		PageWeb p = new PageWeb();
		List<PageWeb> pages = new ArrayList<PageWeb>();
		long start = System.currentTimeMillis();
		pages = p.lirefichiers(file);
		pages = p.trieDesPagesWebParIDate(pages);
		Navigation nav = new Navigation();
		Map<Integer, List<Navigation>> navs = new HashMap<Integer, List<Navigation>>();
		navs = nav.pageconsulteparID(pages);
		Map<Navigation, Integer> mapOcc = new HashMap<Navigation, Integer>();
		mapOcc = nav.NombreOccurancesNavigation(navs);
		PageVisite pv = new PageVisite();
		List<PageVisite> visitP = new ArrayList<PageVisite>();
		visitP = pv.NombreOccurancesPages(mapOcc);
		//pv.AffichagePageVisit(visitP);
		Map<Navigation, Float> lesProba = new HashMap<Navigation, Float>();
		lesProba = p.probabilitesPages(mapOcc, visitP);
		long end = System.currentTimeMillis();
		//lesProba  = p.doSort(lesProba);
		System.out.println("Tapez 1: Afficher le fichier, 2: Afficher les navigations par seqID\n"+
				"\t 3: Afficher les Ocurrances des differentes pages, 4: Afficher  toutes les transiions,\n"+
				"\t 5: Affichier les transitions les plus probables des pages et 0: pour Quitter");
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		int arret = sc.nextInt();
		if(arret>6){
			System.out.println("Erreur Tapez 1: Afficher le fichier, 2: Afficher les navigations par seqID\n"+
					"\t 3: Afficher les Ocurrances des differentes pages, 4: Afficher  toutes les transiions,\n"+
					"\t 5: Affichier les transitions les plus probables des pages et 0: pour Quitter");
			sc = new Scanner(System.in);
		}
		while(arret!=0){			
			if(arret==1){
				long debut = System.currentTimeMillis();
				for(int i = 0; i < pages.size(); i++){
					System.out.println(pages.get(i).getSeqID()+" à "+pages.get(i).getDatecon()+" --> "+pages.get(i).getPage());
				}
				System.out.println("La durée de l'exécution du programme avec l'affichage du choix est: "+ (end-start)/1000+(System.currentTimeMillis()-debut)/1000+ " secondes");
			}
			else if(arret==2){
				long debut = System.currentTimeMillis();
				nav.AffichageNavigation(navs);
				System.out.println("La durée de l'exécution du programme avec l'affichage du choix est: "+ (end-start)/1000+(System.currentTimeMillis()-debut)/1000+ " secondes");
			}
			else if(arret==3){
				long debut = System.currentTimeMillis();
				nav.AffichagePageOcc(mapOcc);
				System.out.println("La durée de l'exécution du programme avec l'affichage du choix est: "+ (end-start)/1000+ (System.currentTimeMillis()-debut)/1000+ " secondes");
			}
			else if(arret==4){
				long debut = System.currentTimeMillis();
				p.AffichageProbabilité(lesProba);
				System.out.println("La durée de l'exécution du programme avec l'affichage du choix est: "+ (end-start)/1000+ (System.currentTimeMillis()-debut)/1000+ " secondes");
			}
			else if(arret==5){
				long debut = System.currentTimeMillis();
				p.AffichageBestProbabilite(lesProba, visitP);
				System.out.println("La durée de l'exécution du programme avec l'affichage du choix est: "+ (end-start)/1000+ (System.currentTimeMillis()-debut)/1000+" secondes");
			}
			else{
				System.out.println("Erreur Tapez 1: Afficher le fichier, 2: Afficher les navigations par seqID\n"+
						"\t 3: Afficher les Ocurrances des differentes pages, 4: Afficher  toutes les transiions,\n"+
						"\t 5: Affichier les transitions les plus probables des pages et 0: pour Quitter");
				arret = sc.nextInt();
			}
			System.out.println("Tapez 1: Afficher le fichier, 2: Afficher les navigations par seqID\n"+
					"\t 3: Afficher les Ocurrances des differentes pages, 4: Afficher  toutes les transiions,\n"+
					"\t 5: Affichier les transitions les plus probables des pages et 0: pour Quitter");
			arret = sc.nextInt();
		}
	}
}
