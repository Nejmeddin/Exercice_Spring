package tn.enis.app.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Classe utilitaire à exécuter directement (Run → main)
 * pour injecter des utilisateurs dans la base de données.
 *
 * Les mots de passe sont automatiquement encodés en BCrypt.
 */
public class InjectUsers {

    // ─── Paramètres de connexion (identiques à applicationContext.xml) ───
    private static final String DB_URL = "jdbc:mysql://localhost:3306/Exercice_Spring_PFE?createDatabaseIfNotExist=true";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    public static void main(String[] args) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // ─── Utilisateurs à injecter ───
        String[][] users = {
                // { login, mot_de_passe_en_clair, role }
                {"admin", "admin123", "ADMIN"},
                {"user", "user123", "USER"}
        };

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("✖ Driver MySQL introuvable ! Vérifiez que mysql-connector-j est dans le classpath.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {

            System.out.println("═══════════════════════════════════════════");
            System.out.println("  INJECTION DES UTILISATEURS");
            System.out.println("═══════════════════════════════════════════");
            System.out.println("Base : " + DB_URL);
            System.out.println();

            for (String[] u : users) {
                String login = u[0];
                String rawPassword = u[1];
                String role = u[2];

                // Vérifier si l'utilisateur existe déjà
                String checkSql = "SELECT COUNT(*) FROM users WHERE login = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                    checkStmt.setString(1, login);
                    ResultSet rs = checkStmt.executeQuery();
                    rs.next();
                    if (rs.getInt(1) > 0) {
                        System.out.println("⚠ Utilisateur '" + login + "' existe déjà → ignoré");
                        continue;
                    }
                }

                // Encoder le mot de passe en BCrypt
                String encodedPassword = encoder.encode(rawPassword);

                // Insérer
                String insertSql = "INSERT INTO users (login, password, role) VALUES (?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setString(1, login);
                    insertStmt.setString(2, encodedPassword);
                    insertStmt.setString(3, role);
                    insertStmt.executeUpdate();
                    System.out.println("✔ Utilisateur ajouté : login='" + login + "', password='" + rawPassword + "', role=" + role);
                }
            }

            System.out.println();
            System.out.println("═══════════════════════════════════════════");
            System.out.println("  LISTE DES UTILISATEURS EN BASE");
            System.out.println("═══════════════════════════════════════════");

            String selectSql = "SELECT id, login, role FROM users ORDER BY id";
            try (PreparedStatement stmt = conn.prepareStatement(selectSql)) {
                ResultSet rs = stmt.executeQuery();
                System.out.printf("  %-5s %-20s %-10s%n", "ID", "LOGIN", "ROLE");
                System.out.println("  " + "-".repeat(35));
                while (rs.next()) {
                    System.out.printf("  %-5d %-20s %-10s%n",
                            rs.getLong("id"),
                            rs.getString("login"),
                            rs.getString("role"));
                }
            }

            System.out.println();
            System.out.println("✔ Terminé ! Vous pouvez maintenant vous connecter :");
            System.out.println("  → admin / admin123  (rôle ADMIN)");
            System.out.println("  → user  / user123   (rôle USER)");

        } catch (Exception e) {
            System.err.println("✖ Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}

