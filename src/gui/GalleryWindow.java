package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;

public class GalleryWindow extends JFrame{

	JPanel panelPhoto;
	
	public GalleryWindow(String numGallery){
		setTitle("Gallery");

		setBounds(100, 100, 450, 450);
		
		panelPhoto = new JPanel();
		JScrollPane scr = new JScrollPane(panelPhoto);	
		
		panelPhoto.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		int num = 1;
		for (int i = 0; i < 3; i++) {
			gbc.gridy = i;
			for (int j = 0; j < 3; j++) {

				String path = "gallery\\" + numGallery + "_" + num + ".JPG";
				File file = new File(path);
				
				if (file.exists()) {

					JButton butt = new JButton();

					int sizePicture = 130;
					butt.setPreferredSize(new Dimension(sizePicture + 10, sizePicture + 10));

					ImageIcon star = new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(sizePicture,
							sizePicture, Image.SCALE_SMOOTH));
					butt.setIcon(star);

					gbc.gridx = j;
					panelPhoto.add(butt, gbc);
				}
				num++;
			}
		}
		
		add(scr);
	
		setUndecorated(true);
		getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		
		setLocationRelativeTo(null);  
		pack();
	}
	
}
