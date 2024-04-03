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
		    for (int i = 0; i < etals.length; i++) {
		        etals[i] = new Etal();
		    }
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
				if (etals[i].isEtalOccupe()) {
					if (etals[i].contientProduit(produit)) {
						nbproduits++;
					}
				}
			}
			Etal[] etalsTrouve = new Etal[nbproduits];
			int j = 0;
			for (int i = 0; i < etals.length; i++) {
				if (etals[i].isEtalOccupe() && (etals[i].contientProduit(produit))) {
						etalsTrouve[j] = etals[i];
						j++;
					}
			}	
			return etalsTrouve;
		}
		
		private  Etal trouverVendeur(Gaulois gaulois) {
			for (int i = 0; i < etals.length; i++) {
				if (etals[i].isEtalOccupe() && etals[i].getVendeur() == gaulois) 
					return etals[i];
			}
			return null;
		}
		
		private String afficherMarche() {
			int nbEtalsNonUtilises = 0;
			StringBuilder chaine = new StringBuilder();

			for (int i = 0; i < etals.length; i++) {
				if (etals[i].isEtalOccupe()) {
					chaine.append(etals[i].afficherEtal());
				} else {
					nbEtalsNonUtilises++;	
				}
			}
			if (nbEtalsNonUtilises>0) {
				chaine.append("Il reste " + nbEtalsNonUtilises + " étals non utilisés dans le marché.");
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

	public String afficherVillageois() throws VillageSansChefException {
	    if (chef == null) {
	        throw new VillageSansChefException("Le village " + nom + " n'a pas de chef.\n");
	    }
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
		etalLibre++;
		chaine.append("Le vendeur " + vendeur.getNom()+ " vend des " + produit + " à l'étal n°" + etalLibre + ".\n");
		return chaine.toString();
	}
	
	public String rechercherVendeursProduit(String produit) {
		Etal[] etalsProposantLeProduit = marche.trouverEtals(produit);
		StringBuilder chaine = new StringBuilder();
		switch (etalsProposantLeProduit.length) {
			case 0: {
				chaine.append("Il n'y a pas de vendeur qui propose des " + produit + " au marché.\n");
				break;
			}
			case 1:{
				chaine.append("Seul le vendeur "+ etalsProposantLeProduit[0].getVendeur().getNom() +" propose des fleurs au marché.\n");
				break;
			}
			default:
				chaine.append("Les vendeurs qui proposent des fleurs sont :\n");
				for (int i = 0; i < etalsProposantLeProduit.length; i++) {
					chaine.append("- " + etalsProposantLeProduit[i].getVendeur().getNom() + "\n");
				}
		}	
		return chaine.toString();
	}
	
	public Etal rechercherEtal(Gaulois vendeur) {
	    Etal etalduvendeur = marche.trouverVendeur(vendeur);
	    if (etalduvendeur != null) {
	    	return etalduvendeur;
		} 
	    return null;
	}
	
	public String partirVendeur(Gaulois vendeur) {
		StringBuilder chaine = new StringBuilder();
		Etal etalDuVendeur = rechercherEtal(vendeur);
		chaine.append(etalDuVendeur.libererEtal());
		return chaine.toString();
	}
	
	public String afficherMarche() {
		StringBuilder chaine = new StringBuilder();
		chaine.append("Le marché du village " + nom +" possède plusieurs étals :\n");
		chaine.append(marche.afficherMarche());
		return chaine.toString();
	}
	
}