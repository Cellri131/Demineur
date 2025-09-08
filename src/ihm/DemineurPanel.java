package ihm;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import metier.Case;
import metier.Plateau;
public class DemineurPanel extends JPanel {
	private final Plateau plateau;
	private final CaseButton[][] boutons;
	private final ImageIcon[] numberIcons;
	private final ImageIcon blockIcon, blankIcon, flagIcon, bombIcon;

	public DemineurPanel(int size) {
		this.plateau = new Plateau(size);
		this.boutons = new CaseButton[size][size];
		this.numberIcons = new ImageIcon[5];
	    this.blockIcon = new ImageIcon(new ImageIcon("../asset/block.png").getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH));
	    this.blankIcon = new ImageIcon(new ImageIcon("../asset/blank.png").getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH));
	    this.flagIcon = new ImageIcon(new ImageIcon("../asset/drapeau.png").getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH));
	    this.bombIcon = new ImageIcon(new ImageIcon("../asset/bomb.png").getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH));
		for (int i = 1; i <= 4; i++) {
			numberIcons[i] = new ImageIcon(new ImageIcon("../asset/" + i + ".png").getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH));
		}
		setLayout(new GridLayout(size, size));
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				boutons[y][x] = new CaseButton(x, y);
				add(boutons[y][x]);
			}
		}
		setPreferredSize(new Dimension(size * 48, size * 48));
	}

	private void updateButton(int x, int y) {
		Case c = plateau.cases[y][x];
		CaseButton b = boutons[y][x];
		if (c.flagged) {
			b.setIcon(flagIcon);
		} else if (!c.revealed) {
			b.setIcon(blockIcon);
		} else if (c.isMine) {
			b.setIcon(bombIcon);
		} else if (c.number > 0 && c.number < numberIcons.length) {
			b.setIcon(numberIcons[c.number]);
		} else {
			b.setIcon(blankIcon);
		}
	}

	private void floodFill(int x, int y) {
		if (x < 0 || x >= plateau.size || y < 0 || y >= plateau.size) return;
		Case c = plateau.cases[y][x];
		if (c.revealed || c.isMine || c.flagged) return;
		c.revealed = true;
		updateButton(x, y);
		// Révéler toutes les cases blank (number==0) ET leurs voisines non-minées
		if (c.number == 0) {
			for (int dy = -1; dy <= 1; dy++) {
				for (int dx = -1; dx <= 1; dx++) {
					int nx = x + dx, ny = y + dy;
					if ((dx != 0 || dy != 0) && nx >= 0 && nx < plateau.size && ny >= 0 && ny < plateau.size) {
						Case voisin = plateau.cases[ny][nx];
						if (!voisin.revealed && !voisin.isMine) {
							if (voisin.number == 0) {
								floodFill(nx, ny);
							} else {
								voisin.revealed = true;
								updateButton(nx, ny);
							}
						}
					}
				}
			}
		}
	}

	private void gameLost() {
		plateau.gameOver = true;
		for (int y = 0; y < plateau.size; y++)
			for (int x = 0; x < plateau.size; x++) {
				if (plateau.cases[y][x].isMine) {
					plateau.cases[y][x].revealed = true;
				}
				updateButton(x, y);
			}
		JOptionPane.showMessageDialog(this, "Perdu !");
	}

	private void checkWin() {
		if (plateau.checkWin()) {
			JOptionPane.showMessageDialog(this, "Gagné !");
		}
	}

	private class CaseButton extends JButton {
		final int x, y;
		public CaseButton(int x, int y) {
			this.x = x;
			this.y = y;
			setIcon(blockIcon);
			setMargin(new Insets(0,0,0,0));
			addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					if (plateau.gameOver) return;
					Case c = plateau.cases[y][x];
					if (SwingUtilities.isRightMouseButton(e)) {
						if (!c.revealed) {
							c.flagged = !c.flagged;
							updateButton(x, y);
						}
					} else if (SwingUtilities.isLeftMouseButton(e)) {
						if (c.flagged || c.revealed) return;
						if (plateau.firstClick) {
							plateau.placerMines(x, y);
							plateau.firstClick = false;
						}
						if (c.isMine) {
							c.revealed = true;
							updateButton(x, y);
							gameLost();
						} else {
							floodFill(x, y);
							checkWin();
						}
					}
				}
			});
		}
	}
}
