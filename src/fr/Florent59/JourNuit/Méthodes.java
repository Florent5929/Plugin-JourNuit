package fr.Florent59.JourNuit;

import org.bukkit.Bukkit;

public class Méthodes{

	
	 public static long convertirSecondesEnTicks(long Secondes){
		 return Secondes*20; 
		 // Retourne un nombre de ticks à partir d'un nombre de secondes fourni en paramètre.
	 }
	 
	 
	 public static long convertirTicksEnSecondes(long Ticks){
		 return Ticks/20;
		 // Retourne un nombre de secondes à partir d'un nombre de ticks fourni en paramètre.
	 }
	 
	 
	 public static double arrondirDouble(double nombre)
	 {
			double decimal = nombre - (int) nombre;
			// On récupère la partie décimale du nombre
			
			if(decimal < 0.5) // On arrondi par le haut ou par le bas selon le seuil de 0.5
				nombre = Math.floor(nombre);
			else
				nombre = Math.ceil(nombre);
			
			// Math.floor retourne l'entier inférieur le plus proche.
			// Math.ceil retourne l'entier supérieur le plus proche	
			
		 return nombre;
	 }
	 
	public static void stopTask(){
		Bukkit.getScheduler().cancelTask(Main.taskId);
		// Pour arrêter la tâche proprement. stopTask() sera appelée dans OnDisable().
	}
	 
}
