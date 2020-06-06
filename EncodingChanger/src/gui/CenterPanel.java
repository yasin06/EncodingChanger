package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.MyFile;
import service.EncodingChanger;
import service.EncodingDetector;

public class CenterPanel extends JPanel {

	private static final long serialVersionUID = -3480161739913614089L;

	private Map<String, MyFile> fileMap = new TreeMap<>(); // selected file map
	private JScrollPane scrollPane; // main scrolling panel in center panel
	private JPanel filePanel; // file data (grid) panel
	private Map<String, JPanel> rowMap; // each selected file's row panel
	private JPanel emptyPanel; // empty panel to show when no files selected

	/***************************************************************
	 * CenterPanel[ ScrollPane[ FilePanel | EmptyPanel] ] 
	 * 
	 ***************************************************************/
	public CenterPanel() {
		super();

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 1;
		gbc.weightx = 1;

		add(getEmptyPanel(), gbc);
		add(getScrollPane(), gbc);

		getScrollPane().setVisible(false);
	}

	/***************************************************************
	 * Initialize scrollPane and filePanel  
	 * 
	 * @return JScrollPane
	 ***************************************************************/
	public JScrollPane getScrollPane() {
		if (scrollPane != null)
			return scrollPane;

		rowMap = new HashMap<>();

		// A panel to manage selected file panels as a grid
		filePanel = new JPanel();
		filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.Y_AXIS));
		filePanel.setBackground(new Color(255, 240, 230));

		// scrolling panel
		scrollPane = new JScrollPane(filePanel);
		scrollPane.setBackground(new Color(255, 210, 220));

		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		// when main panel is resized, handle row panel's width dynamically
		// but a row panel's height is limited to 30
		// can be gracefully solved with better layouts or JTable 
		scrollPane.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				if (rowMap != null && rowMap.size() > 0) {
					for (JPanel row : rowMap.values()) {
						row.setMaximumSize(new Dimension(scrollPane.getWidth(), 30));
						row.revalidate();
					}
				}
			}
		});

		return scrollPane;
	}

	// NO FILES SELECTED screen
	public JPanel getEmptyPanel() {
		if (emptyPanel != null)
			return emptyPanel;

		JLabel label = new JLabel("No files selected.");
		label.setVerticalAlignment(JLabel.CENTER);
		label.setHorizontalAlignment(JLabel.CENTER);

		emptyPanel = new JPanel(new BorderLayout());
		emptyPanel.setBackground(new Color(255, 161, 178));
		emptyPanel.add(label, BorderLayout.CENTER);

		return emptyPanel;
	}


	/************************************************************************
	 * If any file is selected, shift from empty panel to data panel
	 * if no file selected or all selected files are removed shift from data panel to empty panel 
	 ************************************************************************/
	public void toggleCenterPanel() {
		if (fileMap.isEmpty()) {
			this.getEmptyPanel().setVisible(true);
			this.getScrollPane().setVisible(false);
		} else {
			this.getEmptyPanel().setVisible(false);
			this.getScrollPane().setVisible(true);
		}
	}

	
	/************************************************************************
	 * When a file is selected, add it into data map and draw its panel into screen
	 * 
	 * @param fileAbsolutePath - key in data tracking, unique file info
	 ************************************************************************/
	public void handleInputFile(String fileAbsolutePath) {
		File file = new File(fileAbsolutePath);

		if (!file.exists()) {
			return;
		}
		
		// detect selected files encoding
		String sourceEncoding = EncodingDetector.detectEncoding(file);

		MyFile myFile = new MyFile();
		myFile.setFile(file);
		myFile.setSourceEncoding(sourceEncoding);

		// track data
		fileMap.put(fileAbsolutePath, myFile);

		// update screen with new selected file
		insertFile(myFile);

		// toggle between empty screen or data screen
		toggleCenterPanel();
	}

	/********************************************
	 * Add a Panel(row) into filePanel inside scrollPane. 
	 * From left to right show : <it> 
	 * <ul> <li>File Absolute Path</li> 
	 * 		<li>Detected Source Encoding</li> 
	 * 		<li>Remove Button</li>
	 * 		<li>Convert Button</li>
	 * </ul> 
	 * </it>      
	 * @param myFile - File data with source encoding and java file model
	 ********************************************/
	public void insertFile(MyFile myFile) {
		JPanel row = new JPanel(new GridBagLayout());

		row.setBackground(new Color(220, 240, 230));
		row.setBorder(BorderFactory.createRaisedBevelBorder());
		row.setMaximumSize(new Dimension(this.getWidth(), 30));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		int colInd = 0;

		// insets between columns
		Insets middle = new Insets(2, 2, 2, 2);

		JLabel label = new JLabel(myFile.getFile().getAbsolutePath());
		label.setHorizontalAlignment(JLabel.LEFT);
		label.setBackground(new Color(220, 240, 230));
		gbc.gridy = 0;
		gbc.gridx = colInd++;
		gbc.weightx = 1; // horizontally greedy column
		gbc.insets = new Insets(2, 0, 2, 2);
		row.add(label, gbc);

		// horizontal space filler to align file label to left and rest to the right
		gbc.gridy = 0;
		gbc.gridx = colInd++;
		gbc.weightx = 1; // take up remaining space
		gbc.insets = middle;
		row.add(new JLabel(), gbc);

		// Source Encoding Label
		JLabel encoding = new JLabel(myFile.getSourceEncoding());
		gbc.gridy = 0;
		gbc.gridx = colInd++;
		gbc.weightx = 0;
		gbc.insets = middle;
		row.add(encoding, gbc);

		// Button REMOVE : delete this panel and its tracked data 
		JButton remove = new JButton("remove");
		remove.setActionCommand(myFile.getFile().getAbsolutePath());

		remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeClick(e.getActionCommand());
			}
		});

		gbc.gridy = 0;
		gbc.gridx = colInd++;
		gbc.weightx = 0;
		gbc.insets = middle;
		row.add(remove, gbc);

		// Button CONVERT : convert this file to UTF-8
		JButton convert = new JButton("convert");
		convert.setActionCommand(myFile.getFile().getAbsolutePath());

		convert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				int result = JOptionPane.showConfirmDialog(row, "Convert file to UTF-8?", "Convert",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (result == JOptionPane.YES_OPTION) {
					convertClick(e.getActionCommand());
				}
//				else if (result == JOptionPane.NO_OPTION) {
//					System.out.println("You selected: No");
//				} else {
//					System.out.println("None selected");
//				}
			}
		});

		gbc.gridy = 0;
		gbc.gridx = colInd++;
		gbc.weightx = 0;
		gbc.insets = new Insets(2, 2, 2, 0);
		row.add(convert, gbc);

		// add new selected files panel(row) and refresh screen
		filePanel.add(row);
		filePanel.revalidate();
		scrollPane.revalidate();

		// update data track
		rowMap.put(myFile.getFile().getAbsolutePath(), row);
	}

	/*******************************************************
	 * Convert a selected file to UTF-8
	 * 
	 * @param fileAbsolutePath - String
	 * @return true if success, else false 
	 *******************************************************/
	public boolean convertClick(String fileAbsolutePath) {
		MyFile myFile = fileMap.get(fileAbsolutePath);
		boolean result = false;

		try {
			result = EncodingChanger.changeEncoding(myFile);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/*******************************************************
	 * Convert all currently selected file into UTF-8
	 * 
	 * @return successful conversion count
	 *******************************************************/
	public int convertAll() {
		boolean result = false;
		int count = 0;

		if (fileMap != null && fileMap.size() > 0) {
			for (MyFile myFile : fileMap.values()) {
				result = this.convertClick(myFile.getFile().getAbsolutePath());

				if (result) {
					count++;
				}
			}
		}

		return count;
	}

	/*******************************************************
	 * Remove selected file from screen and update data track
	 *  
	 * @param fileAbsolutePath - file to be unselected
	 *******************************************************/
	public void removeClick(String fileAbsolutePath) {
		JPanel row = rowMap.get(fileAbsolutePath);
		filePanel.remove(row);
		rowMap.remove(fileAbsolutePath);
		fileMap.remove(fileAbsolutePath);
		filePanel.revalidate();
		filePanel.repaint();
		toggleCenterPanel();
	}

	/*******************************************************
	 * Remove all selected files from screen and data track
	 * 
	 *******************************************************/
	public void removeAll() {
		if (fileMap != null && fileMap.size() > 0) {
			List<MyFile> list = new ArrayList<>();
			list.addAll(fileMap.values());

			for (MyFile myFile : list) {
				this.removeClick(myFile.getFile().getAbsolutePath());
			}
		}
	}

	//************************ Getter & Setter ************************
	public Map<String, MyFile> getFileMap() {
		return fileMap;
	}

	public void setFileMap(Map<String, MyFile> fileMap) {
		this.fileMap = fileMap;
	}

	public JPanel getFilePanel() {
		return filePanel;
	}

	public void setFilePanel(JPanel filePanel) {
		this.filePanel = filePanel;
	}

	public Map<String, JPanel> getRowMap() {
		return rowMap;
	}

	public void setRowMap(Map<String, JPanel> rowMap) {
		this.rowMap = rowMap;
	}

	public void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}

	public void setEmptyPanel(JPanel emptyPanel) {
		this.emptyPanel = emptyPanel;
	}

}
