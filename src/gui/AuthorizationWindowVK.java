package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.json.JSONException;

import backend.Driver;

public class AuthorizationWindowVK extends JFrame {

	private JPanel panelMain;
	private Dimension dimTextDigitInfo = new Dimension(160, 30);
	private Dimension dimTextLoginRigidArea = new Dimension(20, 0);
	private Dimension dimTextPasswordRigidArea = new Dimension(6, 0);

	private GridBagConstraints gbc;

	private Font textFont = new Font("Comic Sans MS", Font.BOLD, 16);
	private Image imgNew;

	public AuthorizationWindowVK(String act) {

		setTitle("FourMA");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 600);

		panelMain = new JPanel();
		panelMain.setLayout(new BorderLayout());
		add(panelMain);

		imgNew = new ImageIcon(this.getClass().getResource("vk.jpg")).getImage();
		imgNew = imgNew.getScaledInstance(290, 120, Image.SCALE_SMOOTH);
		JLabel imgLabel = new JLabel("");
		imgLabel.setIcon(new ImageIcon(imgNew));
		panelMain.add(imgLabel, BorderLayout.PAGE_START);

		JPanel centralPanel = new JPanel();
		centralPanel.setLayout(new GridBagLayout());
		centralPanel.setBackground(Color.WHITE);

		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(2, 0, 5, 10);

		JPanel loginPanel = new JPanel();
		loginPanel.setBackground(Color.WHITE);
		loginPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel loginLabel = new JLabel("Логин:");
		loginLabel.setFont(textFont);
		loginPanel.add(loginLabel);
		loginPanel.add(Box.createRigidArea(dimTextLoginRigidArea));

		JTextField loginTextField = new JTextField();
		loginTextField.setFont(textFont);
		loginTextField.setPreferredSize(dimTextDigitInfo);
		loginPanel.add(loginTextField);

		JPanel passwordPanel = new JPanel();
		passwordPanel.setBackground(Color.WHITE);
		passwordPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel passwordLabel = new JLabel("Пароль:");
		passwordLabel.setFont(textFont);
		passwordPanel.add(passwordLabel);
		passwordPanel.add(Box.createRigidArea(dimTextPasswordRigidArea));

		JPasswordField passwordTextField = new JPasswordField();
		passwordTextField.setPreferredSize(dimTextDigitInfo);
		passwordPanel.add(passwordTextField);

		centralPanel.add(loginPanel, gbc);
		gbc.gridy = 1;
		centralPanel.add(passwordPanel, gbc);

		JButton enterButton = new JButton("Войти");
		enterButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (act.equals("Enter")) {
					if (!loginTextField.getText().equals("") && !passwordTextField.getText().equals("")) {
						try {
							Driver.connectToVk(loginTextField.getText(), passwordTextField.getText());
							AuthorizationWindowInst instWind = new AuthorizationWindowInst("Enter");
							instWind.setVisible(true);
							dispose();
						} catch (IOException | JSONException e) {
							JOptionPane.showMessageDialog(null, "Невозможно подключиться!", "Ошибка",
									JOptionPane.INFORMATION_MESSAGE);
							e.printStackTrace();
						}
					} else {
						JOptionPane.showMessageDialog(null, "Поле не заполнено!", "Ошибка",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});

		enterButton.setPreferredSize(new Dimension(250, 40));
		enterButton.setFont(new Font("Comic Sans MS", Font.BOLD, 17));
		enterButton.setBackground(new Color(80, 114, 153));
		enterButton.setForeground(Color.WHITE);
		panelMain.add(enterButton, BorderLayout.PAGE_END);

		panelMain.add(centralPanel, BorderLayout.CENTER);

		setPreferredSize(new Dimension(290, 400));
		setResizable(false);
		pack();
	}

	public static void main(String[] args) {
		AuthorizationWindowVK window = new AuthorizationWindowVK("Enter");
		window.setVisible(true);
	}

}
