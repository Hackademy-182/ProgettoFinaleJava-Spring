INSERT INTO users(username, email, password, created_at) VALUES("admin", "admin@aulab.it", "Sonol'admin1", 20260504);

INSERT INTO roles(name) VALUES("role_admin"), ("role_revisor"), ("role_writer"), ("role_user");

INSERT INTO users_roles(user_id, role_id) VALUES(1,1);

INSERT INTO categories(name) VALUES("Politica"), ("Economia"), ("Food&drinks"), ("Sport"), ("Intrattenimento"), ("Tech");