import java.sql.*;
import java.util.Scanner;

public class LibraryManagementSystem {

    static final String URL = "jdbc:mysql://localhost:3306/library_db";
    static final String USER = "root";
    static final String PASSWORD = "Pandu@19";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Connected to Library Database!");
            while (true) {
                System.out.println("\n1. Add Book\n2. View Books\n3. Update Status\n4. Delete Book\n5. Exit");
                System.out.print("Enter choice: ");
                int ch = sc.nextInt();
                sc.nextLine();
                switch (ch) {
                    case 1 -> addBook(con, sc);
                    case 2 -> viewBooks(con);
                    case 3 -> updateBook(con, sc);
                    case 4 -> deleteBook(con, sc);
                    case 5 -> { System.out.println("Exiting..."); return; }
                    default -> System.out.println("Invalid choice!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void addBook(Connection con, Scanner sc) throws SQLException {
        System.out.print("Title: "); String title = sc.nextLine();
        System.out.print("Author: "); String author = sc.nextLine();
        String sql = "INSERT INTO books(title, author, status) VALUES(?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, author);
        ps.setString(3, "Available");
        ps.executeUpdate();
        System.out.println("Book added!");
    }

    static void viewBooks(Connection con) throws SQLException {
        ResultSet rs = con.createStatement().executeQuery("SELECT * FROM books");
        while (rs.next()) {
            System.out.printf("%d | %s | %s | %s%n",
                    rs.getInt("id"), rs.getString("title"),
                    rs.getString("author"), rs.getString("status"));
        }
    }

    static void updateBook(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter Book ID: "); int id = sc.nextInt(); sc.nextLine();
        System.out.print("Enter new status: "); String status = sc.nextLine();
        PreparedStatement ps = con.prepareStatement("UPDATE books SET status=? WHERE id=?");
        ps.setString(1, status); ps.setInt(2, id);
        System.out.println(ps.executeUpdate() > 0 ? "Updated!" : "Book not found!");
    }

    static void deleteBook(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter Book ID to delete: "); int id = sc.nextInt();
        PreparedStatement ps = con.prepareStatement("DELETE FROM books WHERE id=?");
        ps.setInt(1, id);
        System.out.println(ps.executeUpdate() > 0 ? "Deleted!" : "Book not found!");
    }
}
