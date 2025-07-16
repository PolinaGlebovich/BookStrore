INSERT INTO users (name, username, password)
VALUES
    ('Admin User', 'admin', 'adminPassword!'),
    ('Regular User', 'user', 'RegularUserPassword!');

INSERT INTO user_role (user_id, role)
VALUES
    (1, 'ADMIN'),
    (2, 'USER');

INSERT INTO books (title, description, genre, author, price, image_id)
VALUES
    ('Book 1', 'Description of Book 1', 'Fantasy', 'Author 1', 19.99, 'image1'),
    ('Book 2', 'Description of Book 2', 'Science Fiction', 'Author 2', 24.99, 'image2'),
    ('Book 3', 'Description of Book 3', 'Mystery', 'Author 3', 14.99, 'image3');

INSERT INTO carts (user_id)
VALUES
    (1),
    (2);

INSERT INTO cart_item (cart_id, book_id)
VALUES
    (1, 1),
    (1, 2),
    (2, 3);