package main;

import javax.swing.SwingUtilities;

import gui.MainFrame;

public class Main {

	/** @TODO DRAG AND DROP files
	 *  @TODO when no files are available deactivate convert all button
	 *  @todo rearrange colors and refactor code (use a constants file etc.)  
	 *  @todo add select all checkbox ability into center panel (for file selection)
	 *  @todo add column titles and colum borders into center panel
	 *  @todo error handling tests for exceptional cases, 
	 *  	  like when source encoding is not available
	 *  @todo design and develop special character changing support in encodingChanger
	 *  @todo when a file's encoding is changed, update its row in GUI 
	 */
	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainFrame("File Chooser Changer");
			}
		});

		System.out.println("\nLebron James");
	}

}
