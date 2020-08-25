package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class MainWindow extends JFrame {

	private JPanel panelMain, panelPhoto, panelVideo, panelButton, panelGallery;
	private JTabbedPane infoTabPane;
	private JMenuBar menuBar;
	private JMenu directoryMenu;
	private JMenuItem changeInstItem, changeVkItem, exitItem;

	// private Dimension dimTextDigitInfo = new Dimension(200, 35);
	// private Dimension dimTextLoginRigidArea = new Dimension(40, 0);
	// private Dimension dimTextPasswordRigidArea = new Dimension(26, 0);

	private GridBagConstraints gbc;
	DefaultListModel listModel;
	JList list;

	// private Font textFont = new Font("Comic Sans MS", Font.BOLD, 20);
	// private Image imgNew;

	private Vector<String> resultVector = new Vector<String>();
	private Vector<ButtonClass> buttVector = new Vector<ButtonClass>();

	public MainWindow() {

		setTitle("FourMA");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 570);

		panelMain = new JPanel();
		panelMain.setLayout(new BorderLayout());
		add(panelMain);

		// Menu
		menuBar = new JMenuBar();
		panelMain.add(menuBar, BorderLayout.PAGE_START);

		directoryMenu = new JMenu("Меню");
		menuBar.add(directoryMenu);

		changeInstItem = new JMenuItem("Изменить Instagram");
		changeInstItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				AuthorizationWindowInst instWind = new AuthorizationWindowInst("Change");
				instWind.setVisible(true);
				dispose();
			}
		});
		directoryMenu.add(changeInstItem);

		changeVkItem = new JMenuItem("Изменить VK");
		changeVkItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				AuthorizationWindowVK instWind = new AuthorizationWindowVK("Change");
				instWind.setVisible(true);
			}
		});
		directoryMenu.add(changeVkItem);

		directoryMenu.addSeparator();

		exitItem = new JMenuItem("Выход");
		exitItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				System.exit(0);
			}
		});
		directoryMenu.add(exitItem);

		// TabPane
		infoTabPane = new JTabbedPane();
		panelMain.add(infoTabPane, BorderLayout.CENTER);

		// Button Panel
		panelButton = new JPanel();
		panelMain.add(panelButton, BorderLayout.PAGE_END);

		JButton OKbut = new JButton("Экспортировать в ВК");
		panelButton.add(OKbut);
		OKbut.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				for (int i = 0; i < list.getSelectedIndices().length; i++) {
					resultVector.addElement("videos/" + (list.getSelectedIndices()[i] + 1) + ".mp4");
				}
				ExportWindowVk exportWindow = new ExportWindowVk(resultVector, buttVector);
				exportWindow.setVisible(true);
				list.clearSelection();
			}
		});

		// Photo Panel
		panelPhoto = new JPanel();

		panelPhoto.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		int num = 1;
		for (int i = 0; i < 3; i++) {
			gbc.gridy = i;
			for (int j = 0; j < 3; j++) {

				String path = "pictures/" + num + ".JPG";
				File file = new File(path);

				if (file.exists()) {

					JButton butt = new JButton();
					ButtonClass buttClass = new ButtonClass(butt, path);
					buttVector.addElement(buttClass);
					butt.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent evt) {
							if (!buttClass.getFlag()) {
								butt.setBackground(new Color(0, 255, 0));
								buttClass.setFlag(true);
								resultVector.addElement(buttClass.getPath());
							} else {
								butt.setBackground(new Color(240, 240, 240));
								buttClass.setFlag(false);
								resultVector.removeElement(buttClass.getPath());
							}
						}
					});

					int sizePicture = 130;
					butt.setPreferredSize(new Dimension(sizePicture + 10, sizePicture + 10));

					//Image img = new ImageIcon(getClass().getResource(path)).getImage();
					//ImageIcon star = new ImageIcon (img.getScaledInstance(sizePicture, sizePicture, Image.SCALE_SMOOTH));
					
					ImageIcon star = new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(sizePicture,
							sizePicture, Image.SCALE_SMOOTH));
					butt.setIcon(star);

					gbc.gridx = j;
					panelPhoto.add(butt, gbc);
				}
				num++;
			}
		}

		infoTabPane.add("      Photo      ", panelPhoto);

		// Gallery Panel
		panelGallery = new JPanel();

		panelGallery.setLayout(new GridBagLayout());
		GridBagConstraints gbcGallery = new GridBagConstraints();

		int numGallery = 1;

		for (int i = 0; i < 3; i++) {

			gbcGallery.gridy = i;
			for (int j = 0; j < 3; j++) {
				String pathGallery = numGallery + "";
				String path = "gallery/" + numGallery + "_1.JPG";
				File file = new File(path);

				if (file.exists()) {

					JButton butt = new JButton();
					ButtonClass buttClass = new ButtonClass(butt, path);
					buttVector.addElement(buttClass);
					butt.addMouseListener(new java.awt.event.MouseListener() {
						GalleryWindow galleryWind = new GalleryWindow(pathGallery);

						@Override
						public void mouseClicked(MouseEvent e) {
						}

						@Override
						public void mouseEntered(MouseEvent e) {
							galleryWind.setVisible(true);
						}

						@Override
						public void mouseExited(MouseEvent e) {
							galleryWind.dispose();
						}

						@Override
						public void mousePressed(MouseEvent e) {
						}

						@Override
						public void mouseReleased(MouseEvent e) {
						}

					});

					butt.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent evt) {

							if (!buttClass.getFlag()) {
								butt.setBackground(new Color(0, 255, 0));
								buttClass.setFlag(true);
								resultVector.add(buttClass.getPath());
							} else {
								butt.setBackground(new Color(240, 240, 240));
								buttClass.setFlag(false);
								resultVector.removeElement(buttClass.getPath());
							}
						}
					});

					int sizePicture = 130;
					butt.setPreferredSize(new Dimension(sizePicture + 10, sizePicture + 10));

					ImageIcon star = new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(sizePicture,
							sizePicture, Image.SCALE_SMOOTH));
					butt.setIcon(star);

					gbcGallery.gridx = j;
					panelGallery.add(butt, gbcGallery);
				}
				numGallery++;
			}
		}

		infoTabPane.add("      Gallery      ", panelGallery);

		// Video Panel
		panelVideo = new JPanel();

		// panelVideo.setLayout(new GridBagLayout());
		panelVideo.setLayout(new BorderLayout(5, 5));
		panelVideo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		GridBagConstraints gbcVideo = new GridBagConstraints();
		listModel = new DefaultListModel();
		list = new JList(listModel);
		list.setSelectedIndex(0);
		list.setFocusable(false);
		panelVideo.add(new JScrollPane(list));
		
		JButton buttonOK = new JButton("Clear");
		buttonOK.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				list.clearSelection();
			}
		});
		panelVideo.add(buttonOK, BorderLayout.EAST);
		int numVideo = 1;
		for (int i = 0; i < 3; i++) {
			gbcVideo.gridy = i;
			for (int j = 0; j < 3; j++) {

				String pathVideo = "videos/" + numVideo + ".mp4";
				String pathTxt = "videos/" + numVideo + ".txt";
				File file = new File(pathVideo);

				if (file.exists()) {

					try {
						File file1 = new File(pathTxt);
						FileReader fr = new FileReader(file1);
						BufferedReader reader = new BufferedReader(fr);
						String line = reader.readLine();
						StringBuilder textFromTxt = new StringBuilder();
						while (line != null) {
							textFromTxt = textFromTxt.append(line);
							line = reader.readLine();
						}
						listModel.addElement(textFromTxt);
						int index = listModel.size() - 1;
						list.ensureIndexIsVisible(index);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				numVideo++;
			}
		}

		infoTabPane.add("      Video      ", panelVideo);

		pack();
	}

	public static void main(String[] args) {
		MainWindow window = new MainWindow();
		window.setVisible(true);
	}
}