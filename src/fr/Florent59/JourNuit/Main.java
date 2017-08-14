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
	public long ticksSuppl�mentaires;
	public boolean GarderPhaseLune = true;

	
	@Override
	public void onEnable(){ 
		
		
	if (!this.getDataFolder().exists()) { 
		 this.saveDefaultConfig();
		 this.getConfig().options().copyDefaults(true);
	} // S'il n'y a pas de dossier et de fichier de configuration, on cr�e ceux par d�faut. 	    
	    
	    
	tempsVouluJour = M�thodes.convertirSecondesEnTicks(getConfig().getLong("DureeJourEnSecondes"));
	tempsVouluNuit = M�thodes.convertirSecondesEnTicks(getConfig().getLong("DureeNuitEnSecondes"));
	tempsVouluAube = M�thodes.convertirSecondesEnTicks(getConfig().getLong("DureeAubeEnSecondes"));
	tempsVouluCrepuscule = M�thodes.convertirSecondesEnTicks(getConfig().getLong("DureeCrepusculeEnSecondes"));
	GarderPhaseLune = getConfig().getBoolean("GarderPhaseLune");
	world = Bukkit.getServer().getWorld(getConfig().getString("MondePrincipal"));
	// On r�cup�re les donn�es du fichier de configuration.
	
	    

	// On lance une t�che r�p�titive.
	taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			
			
	@Override
	public void run(){
				
	currentTicks = world.getTime();
	Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Nombre de ticks actuels du monde : " + currentTicks + ". compteur : " + compteur);
					
	
	if( (currentTicks == 0 || currentTicks == 12000 || currentTicks == 14500 || currentTicks == 23000)  && compteur != 1)
		compteur = 1; // Si on est au d�but d'une phase et que le compteur est diff�rent de 1, on le remet � 1.
	
	
	/* ----------------------- TRAITEMENT DU JOUR ----------------------- */
	
	
	if(currentTicks >= 0 && currentTicks < 12000){ // Si on est dans la phase de jour...
		rapportTempsVouluTempsNormal = ((double)tempsVouluJour/TEMPS_NORMAL_JOUR);
				
		
		if(tempsVouluJour >= 12000){ // Si on veut PROLONGER le jour...
			freqTempsNonFige = M�thodes.arrondirDouble(rapportTempsVouluTempsNormal);
									
			
				if(compteur < freqTempsNonFige && currentTicks != 0){
					
					// On emp�che le temps de s'�couler sauf � une certaine fr�quence.
					world.setTime(currentTicks-1);
					compteur++;
						
				} else 
				compteur = 1; // Lorsqu'on laisse le temps s'�couler, on remet le compteur � 1.
																				
						
		} else {	 // Sinon, si on veut RACCOURCIR le jour...
			ticksSuppl�mentaires = (long) (M�thodes.arrondirDouble((1/rapportTempsVouluTempsNormal)-1));
			world.setTime(currentTicks + ticksSuppl�mentaires); } // ... on acc�l�re le temps.
								
								
	} 
							
	
	/* ----------------------- TRAITEMENT DE LA NUIT ----------------------- */
	
						
	if(currentTicks >= 14500 && currentTicks < 23000){ // Si on est dans la phase de nuit...
		rapportTempsVouluTempsNormal = ((double)tempsVouluNuit/TEMPS_NORMAL_NUIT);
			 
		
		if(tempsVouluNuit >= 8500){ // Si l'on veut PROLONGER la nuit...
			freqTempsNonFige = M�thodes.arrondirDouble(rapportTempsVouluTempsNormal); 
					
							
				if(compteur < freqTempsNonFige && currentTicks != 14500){
					
					// On emp�che le temps de s'�couler sauf � une certaine fr�quence.
					
					
					if(GarderPhaseLune){
						
						for(int i = 1; i <=8; i++){
						world.setTime(world.getTime()-1);
						} // Au lieu d'enlever 1, on retire 8 avec une boucle puis on rajoute 7 (ce qui revient au m�me.)
						// Ceci afin d'�viter que la phase de la lune change (il y a 8 phases de la lune d'o� le 8.)
						// Ou du moins que les changements de phases soient moins visibles.
				
						world.setTime(world.getTime()+7);
						
					} else
					world.setTime(world.getTime()-1);
					
					compteur++;
						
				} else 
				compteur = 1; // Lorsqu'on laisse le temps s'�couler, on remet le compteur � 1.	
						
		} else	{ // Sinon, si on veut RACCOURCIR la nuit...
		ticksSuppl�mentaires = (long) (M�thodes.arrondirDouble((1/rapportTempsVouluTempsNormal)-1));
		world.setTime(currentTicks + ticksSuppl�mentaires); } // ... on acc�l�re le temps.
								
								
							
	} 
	
	/* ----------------------- TRAITEMENT DE L'AUBE ----------------------- */
	
	
	if(currentTicks >= 23000 && currentTicks <= 23999){ // Si on est dans la phase de l'aube...
		rapportTempsVouluTempsNormal = ((double)tempsVouluAube/TEMPS_NORMAL_AUBE);
				
		
		if(tempsVouluAube >= 1000){ // Si on veut PROLONGER l'aube...
			freqTempsNonFige = M�thodes.arrondirDouble(rapportTempsVouluTempsNormal);
									
			
				if(compteur < freqTempsNonFige && currentTicks != 23000){
					
					// On emp�che le temps de s'�couler sauf � une certaine fr�quence.
					world.setTime(currentTicks-1);
					compteur++;
						
				} else 
				compteur = 1; // Lorsqu'on laisse le temps s'�couler, on remet le compteur � 1.
																				
						
		} else {	 // Sinon, si on veut RACCOURCIR l'aube...
			ticksSuppl�mentaires = (long) (M�thodes.arrondirDouble((1/rapportTempsVouluTempsNormal)-1));
			world.setTime(currentTicks + ticksSuppl�mentaires); } // ... on acc�l�re le temps.
								
								
	} 
	
	/* ----------------------- TRAITEMENT DU CREPUSCULE ----------------------- */
	
	
	if(currentTicks >= 12000 && currentTicks < 14500){ // Si on est dans la phase du cr�puscule...
		rapportTempsVouluTempsNormal = ((double)tempsVouluCrepuscule/TEMPS_NORMAL_CREPUSCULE);
				
		
		if(tempsVouluCrepuscule >= 2500){ // Si on veut PROLONGER le cr�puscule...
			freqTempsNonFige = M�thodes.arrondirDouble(rapportTempsVouluTempsNormal);
									
			
				if(compteur < freqTempsNonFige && currentTicks != 12000){
					
					// On emp�che le temps de s'�couler sauf � une certaine fr�quence.
					world.setTime(currentTicks-1);
					compteur++;
						
				} else 
				compteur = 1; // Lorsqu'on laisse le temps s'�couler, on remet le compteur � 1.
																				
						
		} else {	 // Sinon, si on veut RACCOURCIR le cr�puscule...
			ticksSuppl�mentaires = (long) (M�thodes.arrondirDouble((1/rapportTempsVouluTempsNormal)-1));
			world.setTime(currentTicks + ticksSuppl�mentaires); } // ... on acc�l�re le temps.
								
								
	} 
	
	
			}
		}, 0L, 1L); 
		// On lance le code imm�diatement, on le r�p�te tous les 1 tick (0,05 seconde). 
	
	 }
	
	
	/* ----------------------- COMMANDES ----------------------- */
	 
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player player = (Player) sender;
		if(sender instanceof Player ){
		
			// On v�rifie que l'entit� qui ex�cute la commande est un joueur.
		
		 if(label.equalsIgnoreCase("setWorld")){ // Si c'est la commande /setWorld <NomMondePrincipal> ...
			if(args.length != 1){ // ... on v�rifie qu'un monde principal a bien �t� renseign�.
				player.sendMessage("Usage : /setWorld <NomMondePrincipal>"); // Sinon on envoie un message qui explique comment utiliser la commande.
			} else if(args.length == 1){
				 reloadConfig();
				 getConfig().set("MondePrincipal", args[0]);
				 saveConfig(); // On remplace le monde principal de la configuration par ce qu'a envoy� le joueur.
				 player.sendMessage(ChatColor.DARK_AQUA + args[0] + " a �t� d�fini comme �tant le monde principal sur lequel doit s'appliquer le plugin JourNuit.");
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
					player.sendMessage(ChatColor.DARK_AQUA +  "La dur�e de la journ�e a �t� d�finie � " + args[1] + " secondes.");
					
				} else if(args[0].equalsIgnoreCase("night")){
					reloadConfig();
					getConfig().set("DureeNuitEnSecondes", Long.parseLong(args[1]));
					saveConfig();
					player.sendMessage(ChatColor.DARK_AQUA +  "La dur�e de la nuit a �t� d�finie � " + args[1] + " secondes.");
				}
				
				 else if(args[0].equalsIgnoreCase("aube")){
					reloadConfig();
					getConfig().set("DureeAubeEnSecondes", Long.parseLong(args[1]));
					saveConfig();
					player.sendMessage(ChatColor.DARK_AQUA +  "La dur�e de l'aube a �t� d�finie � " + args[1] + " secondes.");
				}
				
				 else if(args[0].equalsIgnoreCase("crepuscule")){
					reloadConfig();
					getConfig().set("DureeCrepusculeEnSecondes", Long.parseLong(args[1]));
					saveConfig();
					player.sendMessage(ChatColor.DARK_AQUA +  "La dur�e du cr�puscule a �t� d�finie � " + args[1] + " secondes.");
				}
				
				}
			getServer().getPluginManager().disablePlugin(this);
            getServer().getPluginManager().enablePlugin(this);	
			} // On relance le plugin.
		 
		 
		}
		return false;
	}
	
	
	public void onDisable(){ 
		 M�thodes.stopTask();
	 }
	 
}
