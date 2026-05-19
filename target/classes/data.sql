-- Initial admin account (so you can login after first deploy)
INSERT OR IGNORE INTO admins (admin_id, name, email, phone, address, password, access_level, role)
VALUES ('A001', 'Admin', 'admin@eduportal.com', '0712345678', 'Main Office', 'admin123', 1, 'ADMIN');
