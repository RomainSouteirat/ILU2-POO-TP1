package villagegaulois;

import java.util.Iterator;

import personnages.Chef;
import personnages.Gaulois;

public class Village {
	private String nom;
	private Chef chef;
	private Gaulois[] villageois;
	private int nbVillageois = 0;
	private Marche marche;

	public Village(String nom, int nbVillageoisMaximum, int etals) {
		this.nom = nom;
		villageois = new Gaulois[nbVillageoisMaximum];
		marche = new Marche(etals);
	}
	
	private static class Marche {
		private Etal[] etals;
		
		private Marche(int nombreEtals) {
			etals = new Etal[nombreEtals];
		}
		
		private void utiliserEtal(int indiceEtal, Gaulois vendeur, String produit, int nbProduit) {
			etals[indiceEtal].occuperEtal(vendeur, produit, nbProduit);
		}
		
		private int trouverEtalLibre() {
			for (int i = 0; i < etals.length; i++) {
				if (!etals[i].isEtalOccupe()) {
					return i;
				}
			}
			return -1;
		}
		
		private Etal[] trouverEtals(String produit){
			int nbproduits = 0;
			for (int i = 0; i < etals.length; i++) {
				if (etals[i].contientProduit(produit)) {
					nbproduits++;
				}
			}
			Etal etalsTrouve[] = new Etal[nbproduits-1];
			for (int i = 0; i < etals.length; i++) {
				if (etals[i].contientProduit(produit))
					etalsTrouve[i] = etals[i];
			}	
			return etalsTrouve;
		}
		
		private  Etal trouverVendeur(Gaulois gaulois) {
			for (int i = 0; i < etals.length; i++) {
				if (etals[i].getVendeur() == gaulois) 
					return etals[i];
			}
			return null;
		}
		
		private String afficherMarche() {
			int nbEtalsNonUtilisés = 0;
			StringBuilder chaine = new StringBuilder();
			for (int i = 0; i < etals.length; i++) {
				if (etals[i].isEtalOccupe()) {
					chaine.append(etals[i].afficherEtal());
				} else {
					nbEtalsNonUtilisés++;	
				}
			}
			if (nbEtalsNonUtilisés>0) {
				chaine.append("Il reste " + nbEtalsNonUtilisés + " étals non utilisés dans le marché.");
			}
			return chaine.toString();
		}
	}
	
	public String getNom() {
		return nom;
	}

	public void setChef(Chef chef) {
		this.chef = chef;
	}

	public void ajouterHabitant(Gaulois gaulois) {
		if (nbVillageois < villageois.length) {
			villageois[nbVillageois] = gaulois;
			nbVillageois++;
		}
	}

	public Gaulois trouverHabitant(String nomGaulois) {
		if (nomGaulois.equals(chef.getNom())) {
			return chef;
		}
		for (int i = 0; i < nbVillageois; i++) {
			Gaulois gaulois = villageois[i];
			if (gaulois.getNom().equals(nomGaulois)) {
				return gaulois;
			}
		}
		return null;
	}

	public String afficherVillageois() {
		StringBuilder chaine = new StringBuilder();
		if (nbVillageois < 1) {
			chaine.append("Il n'y a encore aucun habitant au village du chef "
					+ chef.getNom() + ".\n");
		} else {
			chaine.append("Au village du chef " + chef.getNom()
					+ " vivent les légendaires gaulois :\n");
			for (int i = 0; i < nbVillageois; i++) {
				chaine.append("- " + villageois[i].getNom() + "\n");
			}
		}
		return chaine.toString();
	}
	
	public String installerVendeur(Gaulois vendeur, String produit,int nbProduit) {
		StringBuilder chaine = new StringBuilder();
		chaine.append(vendeur.getNom() + " cherche un endroit pour vendre " + nbProduit + " " + produit + ".\n");
		int etalLibre = marche.trouverEtalLibre();
		marche.utiliserEtal(etalLibre, vendeur, produit, nbProduit);
		chaine.append("Le vendeur " + vendeur.getNom()+ " vend des " + produit + " à l'étal n°" + etalLibre + ".");
		return chaine.toString();
	}
}