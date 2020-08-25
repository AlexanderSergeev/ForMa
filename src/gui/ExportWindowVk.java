package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

import backend.Driver;

public class ExportWindowVk extends JFrame {

	private JPanel mainPanel, idVkPanel, panelButton;
	private JTextField idVkTextField;

	private Font textFont = new Font("Comic Sans MS", Font.BOLD, 16);

	public ExportWindowVk(Vector<String> resultVector, Vector<ButtonClass> buttVector) {

		setTitle("ID группы");
		setBounds(100, 100, 350, 160);

		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		add(mainPanel);

		idVkPanel = new JPanel();
		mainPanel.add(idVkPanel, BorderLayout.CENTER);
		idVkPanel.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(2, 0, 5, 10);

		JLabel idVkLabel = new JLabel("ID:");
		idVkLabel.setFont(textFont);
		idVkPanel.add(idVkLabel, gbc);

		idVkTextField = new JTextField();
		idVkTextField.setFont(textFont);
		idVkTextField.setPreferredSize(new Dimension(200, 30));
		gbc.gridx = 1;
		idVkPanel.add(idVkTextField, gbc);

		panelButton = new JPanel();
		mainPanel.add(panelButton, BorderLayout.PAGE_END);

		JButton OKbut = new JButton("OK");
		OKbut.setPreferredSize(new Dimension(110, 30));
		panelButton.add(OKbut);
		OKbut.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (idVkTextField.getText().equals(""))
					JOptionPane.showMessageDialog(null, "Поле не заполнено!", "Ошибка",
							JOptionPane.INFORMATION_MESSAGE);
				else {

					int vkGroupId = new Integer(idVkTextField.getText());

					for (int i = 0; i < resultVector.size(); i++) {
						String path = "";
						if (resultVector.get(i).contains("pictures")) {
							path = "./pictures/" + resultVector.get(i).substring((resultVector.get(i).indexOf("/") + 1),
									resultVector.get(i).indexOf("."));
							try {
								Driver.postPhoto(vkGroupId, path);
							} catch (FileNotFoundException | ApiException | ClientException e) {
								e.printStackTrace();
							}
						}
						if (resultVector.get(i).contains("gallery")) {
							path = "./gallery/" + resultVector.get(i).substring((resultVector.get(i).indexOf("/") + 1),
									resultVector.get(i).indexOf("_"));
							int size = 1;

							while (true) {
								String gallerypath = "gallery/"
										+ resultVector.get(i).substring((resultVector.get(i).indexOf("/") + 1),
												resultVector.get(i).indexOf("_") + 1)
										+ size + ".JPG";
								File file = new File(gallerypath);
								if (file.exists()) {
									size++;
								} else
									break;
							}

							try {
								Driver.postGallery(vkGroupId, size, path);
							} catch (FileNotFoundException | ApiException | ClientException e) {
								e.printStackTrace();
							}
						}
						if (resultVector.get(i).contains("videos")) {
							path = "./videos/" + resultVector.get(i).substring((resultVector.get(i).indexOf("/") + 1),
									resultVector.get(i).indexOf("."));
							try {
								Driver.postVideo(vkGroupId, path);
							} catch (FileNotFoundException | ApiException | ClientException e) {
								e.printStackTrace();
							}
						}
					}

					// убираем цвет у кнопки и чистим вектор
					for (int i = 0; i < buttVector.size(); i++) {
						buttVector.get(i).setFlag(false);
						buttVector.get(i).getButton().setBackground(new Color(240, 240, 240));
						
					}
					resultVector.clear();
					dispose();
				}

			}
		});
	}

	/*
	 * protected String getIdVk() { return idVkTextField.getText(); }
	 */

}
