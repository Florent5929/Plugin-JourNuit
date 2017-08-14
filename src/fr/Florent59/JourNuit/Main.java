package fr.Florent59.JourNuit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	
	public final double TEMPS_NORMAL_JOUR = 12000;
	public final double TEMPS_NORMAL_NUIT = 8500;
	public final double TEMPS_NORMAL_AUBE = 1000;
	public final double TEMPS_NORMAL_CREPUSCULE = 2500;
	public long tempsVouluJour;
	public long tempsVouluNuit;
	public long tempsVouluAube;
	public long tempsVouluCrepuscule;
	public double rapportTempsVouluTempsNormal;
	public World world;
	public static int taskId;
	public long currentTicks;
	public double compteur = 1;
	public double freqTempsNonFige;
	public long ticksSupplémentaires;
	public boolean GarderPhaseLune = true;

	
	@Override
	public void onEnable(){ 
		
		
	if (!this.getDataFolder().exists()) { 
		 this.saveDefaultConfig();
		 this.getConfig().options().copyDefaults(true);
	} // S'il n'y a pas de dossier et de fichier de configuration, on crée ceux par défaut. 	    
	    
	    
	tempsVouluJour = Méthodes.convertirSecondesEnTicks(getConfig().getLong("DureeJourEnSecondes"));
	tempsVouluNuit = Méthodes.convertirSecondesEnTicks(getConfig().getLong("DureeNuitEnSecondes"));
	tempsVouluAube = Méthodes.convertirSecondesEnTicks(getConfig().getLong("DureeAubeEnSecondes"));
	tempsVouluCrepuscule = Méthodes.convertirSecondesEnTicks(getConfig().getLong("DureeCrepusculeEnSecondes"));
	GarderPhaseLune = getConfig().getBoolean("GarderPhaseLune");
	world = Bukkit.getServer().getWorld(getConfig().getString("MondePrincipal"));
	// On récupère les données du fichier de configuration.
	
	    

	// On lance une tâche répétitive.
	taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			
			
	@Override
	public void run(){
				
	currentTicks = world.getTime();
	Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Nombre de ticks actuels du monde : " + currentTicks + ". compteur : " + compteur);
					
	
	if( (currentTicks == 0 || currentTicks == 12000 || currentTicks == 14500 || currentTicks == 23000)  && compteur != 1)
		compteur = 1; // Si on est au début d'une phase et que le compteur est différent de 1, on le remet à 1.
	
	
	/* ----------------------- TRAITEMENT DU JOUR ----------------------- */
	
	
	if(currentTicks >= 0 && currentTicks < 12000){ // Si on est dans la phase de jour...
		rapportTempsVouluTempsNormal = ((double)tempsVouluJour/TEMPS_NORMAL_JOUR);
				
		
		if(tempsVouluJour >= 12000){ // Si on veut PROLONGER le jour...
			freqTempsNonFige = Méthodes.arrondirDouble(rapportTempsVouluTempsNormal);
									
			
				if(compteur < freqTempsNonFige && currentTicks != 0){
					
					// On empêche le temps de s'écouler sauf à une certaine fréquence.
					world.setTime(currentTicks-1);
					compteur++;
						
				} else 
				compteur = 1; // Lorsqu'on laisse le temps s'écouler, on remet le compteur à 1.
																				
						
		} else {	 // Sinon, si on veut RACCOURCIR le jour...
			ticksSupplémentaires = (long) (Méthodes.arrondirDouble((1/rapportTempsVouluTempsNormal)-1));
			world.setTime(currentTicks + ticksSupplémentaires); } // ... on accélère le temps.
								
								
	} 
							
	
	/* ----------------------- TRAITEMENT DE LA NUIT ----------------------- */
	
						
	if(currentTicks >= 14500 && currentTicks < 23000){ // Si on est dans la phase de nuit...
		rapportTempsVouluTempsNormal = ((double)tempsVouluNuit/TEMPS_NORMAL_NUIT);
			 
		
		if(tempsVouluNuit >= 8500){ // Si l'on veut PROLONGER la nuit...
			freqTempsNonFige = Méthodes.arrondirDouble(rapportTempsVouluTempsNormal); 
					
							
				if(compteur < freqTempsNonFige && currentTicks != 14500){
					
					// On empêche le temps de s'écouler sauf à une certaine fréquence.
					
					
					if(GarderPhaseLune){
						
						for(int i = 1; i <=8; i++){
						world.setTime(world.getTime()-1);
						} // Au lieu d'enlever 1, on retire 8 avec une boucle puis on rajoute 7 (ce qui revient au même.)
						// Ceci afin d'éviter que la phase de la lune change (il y a 8 phases de la lune d'où le 8.)
						// Ou du moins que les changements de phases soient moins visibles.
				
						world.setTime(world.getTime()+7);
						
					} else
					world.setTime(world.getTime()-1);
					
					compteur++;
						
				} else 
				compteur = 1; // Lorsqu'on laisse le temps s'écouler, on remet le compteur à 1.	
						
		} else	{ // Sinon, si on veut RACCOURCIR la nuit...
		ticksSupplémentaires = (long) (Méthodes.arrondirDouble((1/rapportTempsVouluTempsNormal)-1));
		world.setTime(currentTicks + ticksSupplémentaires); } // ... on accélère le temps.
								
								
							
	} 
	
	/* ----------------------- TRAITEMENT DE L'AUBE ----------------------- */
	
	
	if(currentTicks >= 23000 && currentTicks <= 23999){ // Si on est dans la phase de l'aube...
		rapportTempsVouluTempsNormal = ((double)tempsVouluAube/TEMPS_NORMAL_AUBE);
				
		
		if(tempsVouluAube >= 1000){ // Si on veut PROLONGER l'aube...
			freqTempsNonFige = Méthodes.arrondirDouble(rapportTempsVouluTempsNormal);
									
			
				if(compteur < freqTempsNonFige && currentTicks != 23000){
					
					// On empêche le temps de s'écouler sauf à une certaine fréquence.
					world.setTime(currentTicks-1);
					compteur++;
						
				} else 
				compteur = 1; // Lorsqu'on laisse le temps s'écouler, on remet le compteur à 1.
																				
						
		} else {	 // Sinon, si on veut RACCOURCIR l'aube...
			ticksSupplémentaires = (long) (Méthodes.arrondirDouble((1/rapportTempsVouluTempsNormal)-1));
			world.setTime(currentTicks + ticksSupplémentaires); } // ... on accélère le temps.
								
								
	} 
	
	/* ----------------------- TRAITEMENT DU CREPUSCULE ----------------------- */
	
	
	if(currentTicks >= 12000 && currentTicks < 14500){ // Si on est dans la phase du crépuscule...
		rapportTempsVouluTempsNormal = ((double)tempsVouluCrepuscule/TEMPS_NORMAL_CREPUSCULE);
				
		
		if(tempsVouluCrepuscule >= 2500){ // Si on veut PROLONGER le crépuscule...
			freqTempsNonFige = Méthodes.arrondirDouble(rapportTempsVouluTempsNormal);
									
			
				if(compteur < freqTempsNonFige && currentTicks != 12000){
					
					// On empêche le temps de s'écouler sauf à une certaine fréquence.
					world.setTime(currentTicks-1);
					compteur++;
						
				} else 
				compteur = 1; // Lorsqu'on laisse le temps s'écouler, on remet le compteur à 1.
																				
						
		} else {	 // Sinon, si on veut RACCOURCIR le crépuscule...
			ticksSupplémentaires = (long) (Méthodes.arrondirDouble((1/rapportTempsVouluTempsNormal)-1));
			world.setTime(currentTicks + ticksSupplémentaires); } // ... on accélère le temps.
								
								
	} 
	
	
			}
		}, 0L, 1L); 
		// On lance le code immédiatement, on le répète tous les 1 tick (0,05 seconde). 
	
	 }
	
	
	/* ----------------------- COMMANDES ----------------------- */
	 
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player player = (Player) sender;
		if(sender instanceof Player ){
		
			// On vérifie que l'entité qui exécute la commande est un joueur.
		
		 if(label.equalsIgnoreCase("setWorld")){ // Si c'est la commande /setWorld <NomMondePrincipal> ...
			if(args.length != 1){ // ... on vérifie qu'un monde principal a bien été renseigné.
				player.sendMessage("Usage : /setWorld <NomMondePrincipal>"); // Sinon on envoie un message qui explique comment utiliser la commande.
			} else if(args.length == 1){
				 reloadConfig();
				 getConfig().set("MondePrincipal", args[0]);
				 saveConfig(); // On remplace le monde principal de la configuration par ce qu'a envoyé le joueur.
				 player.sendMessage(ChatColor.DARK_AQUA + args[0] + " a été défini comme étant le monde principal sur lequel doit s'appliquer le plugin JourNuit.");
				 } 
					
			getServer().getPluginManager().disablePlugin(this);
            getServer().getPluginManager().enablePlugin(this);
		} // On relance le plugin.
		
	
		 
		else if(label.equalsIgnoreCase("set")){
			if(args.length != 2){
				player.sendMessage("Usage : /set <day|night> <secondes>");
			} else {
				
				if(args[0].equalsIgnoreCase("day")){
					reloadConfig();
					getConfig().set("DureeJourEnSecondes", Long.parseLong(args[1]));
					saveConfig();
					player.sendMessage(ChatColor.DARK_AQUA +  "La durée de la journée a été définie à " + args[1] + " secondes.");
					
				} else if(args[0].equalsIgnoreCase("night")){
					reloadConfig();
					getConfig().set("DureeNuitEnSecondes", Long.parseLong(args[1]));
					saveConfig();
					player.sendMessage(ChatColor.DARK_AQUA +  "La durée de la nuit a été définie à " + args[1] + " secondes.");
				}
				
				 else if(args[0].equalsIgnoreCase("aube")){
					reloadConfig();
					getConfig().set("DureeAubeEnSecondes", Long.parseLong(args[1]));
					saveConfig();
					player.sendMessage(ChatColor.DARK_AQUA +  "La durée de l'aube a été définie à " + args[1] + " secondes.");
				}
				
				 else if(args[0].equalsIgnoreCase("crepuscule")){
					reloadConfig();
					getConfig().set("DureeCrepusculeEnSecondes", Long.parseLong(args[1]));
					saveConfig();
					player.sendMessage(ChatColor.DARK_AQUA +  "La durée du crépuscule a été définie à " + args[1] + " secondes.");
				}
				
				}
			getServer().getPluginManager().disablePlugin(this);
            getServer().getPluginManager().enablePlugin(this);	
			} // On relance le plugin.
		 
		 
		}
		return false;
	}
	
	
	public void onDisable(){ 
		 Méthodes.stopTask();
	 }
	 
}
