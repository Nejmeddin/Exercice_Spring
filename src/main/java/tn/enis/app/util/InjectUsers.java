package tn.enis.app.util;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;
import java.sql.ResultSet;

public class InjectUsers {

    public static void main(String[] args) {

        // ─── 1. Chargement du contexte Spring XML ───
        // Spring va instancier et injecter automatiquement dataSource et passwordEncoder
        try (ClassPathXmlApplicationContext context =
                     new ClassPathXmlApplicationContext("inject-users-context.xml")) {

            // ─── 2. Récupération des beans depuis le contexte Spring ───
            DataSource dataSource             = context.getBean("dataSource", DataSource.class);
            BCryptPasswordEncoder encoder     = context.getBean("passwordEncoder", BCryptPasswordEncoder.class);
            JdbcTemplate jdbc                 = new JdbcTemplate(dataSource);

            // ─── 3. Utilisateurs à injecter ───
            String[][] users = {
                    // { login, mot_de_passe_en_clair, role }
                    {"admin2", "admin1234", "ADMIN"},
                    {"user2",  "user1234",  "USER"}
            };

            System.out.println("═══════════════════════════════════════════");
            System.out.println("  INJECTION DES UTILISATEURS (Spring XML)");
            System.out.println("═══════════════════════════════════════════");
            System.out.println();

            for (String[] u : users) {
                String login       = u[0];
                String rawPassword = u[1];
                String role        = u[2];

                // ─── Vérifier si l'utilisateur existe déjà ───
                Integer count = jdbc.queryForObject(
                        "SELECT COUNT(*) FROM users WHERE login = ?",
                        Integer.class, login);

                if (count != null && count > 0) {
                    System.out.println("⚠ Utilisateur '" + login + "' existe déjà → ignoré");
                    continue;
                }

                // ─── Encoder le mot de passe via le bean Spring ───
                String encodedPassword = encoder.encode(rawPassword);

                // ─── Insérer ───
                jdbc.update("INSERT INTO users (login, password, role) VALUES (?, ?, ?)",
                        login, encodedPassword, role);

                System.out.println("✔ Utilisateur ajouté : login='" + login
                        + "', password='" + rawPassword + "', role=" + role);
            }

            System.out.println();
            System.out.println("═══════════════════════════════════════════");
            System.out.println("  LISTE DES UTILISATEURS EN BASE");
            System.out.println("═══════════════════════════════════════════");
            System.out.printf("  %-5s %-20s %-10s%n", "ID", "LOGIN", "ROLE");
            System.out.println("  " + "-".repeat(35));

            // ─── Afficher tous les utilisateurs ───
            jdbc.query("SELECT id, login, role FROM users ORDER BY id",
                    (ResultSet rs) -> {
                        System.out.printf("  %-5d %-20s %-10s%n",
                                rs.getLong("id"),
                                rs.getString("login"),
                                rs.getString("role"));
                    });

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

