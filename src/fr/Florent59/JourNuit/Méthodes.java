package fr.Florent59.JourNuit;

import org.bukkit.Bukkit;

public class M�thodes{

	
	 public static long convertirSecondesEnTicks(long Secondes){
		 return Secondes*20; 
		 // Retourne un nombre de ticks � partir d'un nombre de secondes fourni en param�tre.
	 }
	 
	 
	 public static long convertirTicksEnSecondes(long Ticks){
		 return Ticks/20;
		 // Retourne un nombre de secondes � partir d'un nombre de ticks fourni en param�tre.
	 }
	 
	 
	 public static double arrondirDouble(double nombre)
	 {
			double decimal = nombre - (int) nombre;
			// On r�cup�re la partie d�cimale du nombre
			
			if(decimal < 0.5) // On arrondi par le haut ou par le bas selon le seuil de 0.5
				nombre = Math.floor(nombre);
			else
				nombre = Math.ceil(nombre);
			
			// Math.floor retourne l'entier inf�rieur le plus proche.
			// Math.ceil retourne l'entier sup�rieur le plus proche	
			
		 return nombre;
	 }
	 
	public static void stopTask(){
		Bukkit.getScheduler().cancelTask(Main.taskId);
		// Pour arr�ter la t�che proprement. stopTask() sera appel�e dans OnDisable().
	}
	 
}
