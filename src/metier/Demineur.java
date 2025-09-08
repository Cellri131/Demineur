package metier;
import ihm.DemineurFrame;
import javax.swing.*;

public class Demineur {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String input = JOptionPane.showInputDialog(null, "Entrez la taille du plateau (ex: 10 pour 10x10):", "Taille du plateau", JOptionPane.QUESTION_MESSAGE);
            if (input == null) return; // Annulation
            int size = 10;
            try {
                size = Integer.parseInt(input);
                if (size < 5) size = 5;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Entrée invalide, taille par défaut 5x5.");
            }
            DemineurFrame frame = new DemineurFrame(size);
            frame.setVisible(true);
        });
    }
}
