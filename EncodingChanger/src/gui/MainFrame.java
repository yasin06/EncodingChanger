package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileSystemView;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = -8026416994513756565L;

	private CenterPanel centerPanel; // Center Panel : main data grid 
	private JPanel bottomPanel;		 // Bottom Panel : command buttons

	// MAIN GUI FRAME 
	public MainFrame(String title) throws HeadlessException {
		super(title);

		Container contentPane = getContentPane();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(1024, 768));

		contentPane.setLayout(new BorderLayout());
		contentPane.add(getCenterPanel(), BorderLayout.CENTER);
		contentPane.add(getBottomPanel(), BorderLayout.PAGE_END);

		pack();
		setVisible(true);
	}

	public JPanel getCenterPanel() {
		if (centerPanel != null) {
			return centerPanel;
		}

		centerPanel = new CenterPanel();
		return centerPanel;
	}

	public JPanel getBottomPanel() {
		if (bottomPanel != null) {
			return bottomPanel;
		}

		bottomPanel = new JPanel();

		// ****************** Choose Files Button ******************
		JButton chooseFiles = new JButton("Choose Files");

		chooseFiles.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

				jfc.setMultiSelectionEnabled(true);
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.setSize(1200, 800);

				int returnValue = jfc.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File[] selectedFiles = jfc.getSelectedFiles();

					// for each selected file, insert into data and update GUI panel
					for (File selectedFile : selectedFiles) {
						if (!centerPanel.getFileMap().containsKey(selectedFile.getAbsolutePath())) {
							centerPanel.handleInputFile(selectedFile.getAbsolutePath());
						}
					}
				}
			}
		});

		// ****************** Remove All Button ******************
		JButton removeAll = new JButton("Remove All");

		removeAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				centerPanel.removeAll();
			}
		});

		// ****************** Convert All Button ******************
		JButton convertAll = new JButton("Convert All");

		convertAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int count = centerPanel.convertAll();

				JOptionPane.showMessageDialog(null, count + " files transformed.");
			}
		});

		// add buttons into bottom command panel
		bottomPanel.add(chooseFiles);
		bottomPanel.add(removeAll);
		bottomPanel.add(convertAll);

		return bottomPanel;
	}

}
