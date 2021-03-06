/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author jmche
 */public class Commercial {

	public String getEmail() {
		return email;
	}

	private int noCommercial;
	private String nom;
	private String email;
	private String mdp;

	public String getMdp() {
		return mdp;
	}

	public Commercial(int noCommercial, String nom, String email, String mdp) {
		this.noCommercial = noCommercial;
		this.nom = nom;
		this.email = email;
		this.mdp = mdp;
	}

	public int getNoCommercial() {
		return noCommercial;
	}

	public String getNom() {
		return nom;
	}

	public static Commercial getCommercial(int noCommercial) throws SQLException {
		Commercial result = null;
		Connection connexion = Database.getConnection();
		Statement ordre = connexion.createStatement();
		String sql = " SELECT * FROM commercial WHERE no_commercial = " + noCommercial;
		ResultSet rs = ordre.executeQuery(sql);
		if (rs.next()) {
			result = new Commercial(rs.getInt("no_commercial"),
					  rs.getString("nom"),
					  rs.getString("email"),
					  rs.getString("mdp"));
			rs.close();
			ordre.close();
			connexion.close();
		}
		return result;
	}

	public ArrayList<Client> getListClient() throws SQLException {
		ArrayList<Client> listClient = new ArrayList();
		Client result = null;

		Connection connexion = Database.getConnection();
		String sql = "SELECT * FROM client c INNER JOIN portefeuille p	ON c.no_client = p.no_client WHERE (p.no_client, date_attribution) IN ( SELECT no_client, MAX(date_attribution) FROM portefeuille GROUP BY no_client ) AND p.no_commercial = ? ; ";
		PreparedStatement ordre = connexion.prepareStatement(sql);
		ordre.setInt(1, noCommercial);

		ResultSet rs = ordre.executeQuery();
		while (rs.next()) {
			result = new Client(rs.getInt("no_client"),
					  rs.getString("nom"),
					  rs.getString("email"), null);
			listClient.add(result);
		}
		rs.close();
		ordre.close();
		connexion.close();
		return listClient;
	}
	
	public static Commercial getByEmailMpd(String email, String mdp) throws SQLException{
		Commercial result = null;
		Connection connexion = Database.getConnection();
		Statement ordre = connexion.createStatement();
		String sql = " SELECT * FROM commercial WHERE email = '"+email+"' AND mdp = '"+mdp+"';";
		ResultSet rs = ordre.executeQuery(sql);
		if (rs.next()) {
			result = new Commercial(rs.getInt("no_commercial"),
					  rs.getString("nom"),
					  rs.getString("email"),
					  rs.getString("mdp"));
			rs.close();
			ordre.close();
			connexion.close();
		}
		return result;
	}
	
	public static ArrayList<Commercial> getListCommercial() throws SQLException {
		ArrayList<Commercial> listCommercial = new ArrayList();
		Commercial result = null;

		Connection connexion = Database.getConnection();
		String sql = "SELECT * FROM commercial";
		Statement ordre = connexion.createStatement();
		ResultSet rs = ordre.executeQuery(sql);
		while (rs.next()) {
			result = new Commercial(rs.getInt("no_commercial"),
					  rs.getString("nom"),
					  rs.getString("email"),
					  rs.getString("mdp"));
			listCommercial.add(result);
		}
		rs.close();
		ordre.close();
		connexion.close();
		return listCommercial;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + this.noCommercial;
		hash = 31 * hash + Objects.hashCode(this.nom);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Commercial other = (Commercial) obj;
		if (this.noCommercial != other.noCommercial) {
			return false;
		}
		if (!Objects.equals(this.nom, other.nom)) {
			return false;
		}
		return true;
	}

}
