package bots;

import java.awt.Graphics;

import utils.Vector2d;

/**
 * Petite interface "indicative" de ce qu'il peut y avoir dans un bot.
 * Il faut aussi g�rer l'ensemble des bots du jeu dans une structure � part.
 *  
 * @author L. Simon, Univ. Paris Sud, 2008
 *
 */
public interface IBot {
	
	/**
	 * @return Les coordonnées du bot, ou -1,-1 si pas sur la carte par ex.
	 */
	public Vector2d getCoord();
	
	/**
	 * On va repr�senter les bots en rond... Pour les collisions sur la carte,
	 * on a besoin du rayon du bot
	 * @return le rayon (en coordonn�es de cartes) du bot
	 */
	public float botRadius();
	
	/**
	 * Affiche le bot sur le dessin... A vos idées !
	 * @param g
	 */
	public void draw(Graphics g);
	
	/**
	 * @return la chaine representant le bot et son etat, pour debug ou autre
	 */
	public String toString();
	
	/**
	 * Mise à jour IA
	 * des decisions � prendre 
	 */
	public void AI();

	/**
	 * Suivant ses decisions et les autres, le bot peut mettre a jour ses positions
	 */
	public void updatePosition();
	
	
}
