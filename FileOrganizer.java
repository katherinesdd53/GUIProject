package fileOrganizerGUI;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import java.util.Scanner;

public class FileOrganizer extends JFrame implements ActionListener{
	
	public static final int WIDTH = 400;
	public static final int HEIGHT = 200;
	public static final int NUMBER_OF_CHAR = 30;
	
	private JTextField fileNameField;
	private JTextField addLineField;
	private JTextField firstLineField;
	
	public FileOrganizer(){
		setSize(WIDTH, HEIGHT);
		WindowDestroyer listener = new WindowDestroyer();
		addWindowListener(listener);
		Container contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout());
		
		JButton showButton = new JButton("Show First Line");
		showButton.addActionListener(this);
		contentPane.add(showButton);
		
		JButton removeButton = new JButton("Remove File");
		removeButton.addActionListener(this);
		contentPane.add(removeButton);
		
		JButton resetButton = new JButton("Reset");
		resetButton.addActionListener(this);
		contentPane.add(resetButton);
		
		JButton addButton = new JButton("Add A Line");
		addButton.addActionListener(this);
		contentPane.add(addButton);
		
		JButton copyButton = new JButton("Make A Copy");
		copyButton.addActionListener(this);
		contentPane.add(copyButton);
		
		fileNameField = new JTextField(NUMBER_OF_CHAR);
		contentPane.add(fileNameField);
		fileNameField.setText("Enter File Name Here");
		
		addLineField = new JTextField(NUMBER_OF_CHAR);
		contentPane.add(addLineField);
		addLineField.setText("Enter New Line Here");
		
		firstLineField = new JTextField(NUMBER_OF_CHAR);
		contentPane.add(firstLineField);
		firstLineField.setText("Output");
	}
	
	public void actionPerformed(ActionEvent e){
		
		String actionCommand = e.getActionCommand();
		if (actionCommand.equals("Show First Line"))
			showFirstLine();
		else if (actionCommand.equalsIgnoreCase("Remove File"))
			removeFile();
		else if (actionCommand.equals("Reset"))
			resetFields();
		else if (actionCommand.equals("Add A Line"))
			addALine();
		else if (actionCommand.equals("Make A Copy"))
			makeACopy();
		else
			firstLineField.setText("Unexpected Error");
	}
	
	private void showFirstLine(){
		Scanner fileInput = null;
		String fileName = fileNameField.getText();
		File fileObject = new File(fileName);
		
		if (!fileObject.exists())
			firstLineField.setText("No Such File");
		else if (!fileObject.canRead())
			firstLineField.setText("That File Is Not Readable");
		else{
			try{
				fileInput = new Scanner(fileObject);
			}
			catch(FileNotFoundException e){
				firstLineField.setText("File Error " + fileName);
			}
			String firstLine = fileInput.nextLine();
			firstLineField.setText(firstLine);
			fileInput.close();
		}
	}
	
	private void removeFile(){
		Scanner fileInput = null;
		String firstLine;
		String fileName = fileNameField.getText();
		File fileObject = new File(fileName);
		
		if (!fileObject.exists())
			firstLineField.setText("No Such File");
		else if (!fileObject.canWrite())
			firstLineField.setText("Permission Denied");
		else{
			if (fileObject.delete())
				firstLineField.setText("File Deleted");
			else
				firstLineField.setText("Could Not Delete File");
		}
	}
	
	private void resetFields(){
		fileNameField.setText("Enter File Name Here");
		firstLineField.setText("Output");
		addLineField.setText("Enter New Line Here");
	}
	
	private void addALine(){
		String fileName = fileNameField.getText();
		String extraLine = addLineField.getText();
		File fileObject = new File(fileName);
		
		if (!fileObject.exists())
			firstLineField.setText("No Such File");
		else if (!fileObject.canWrite())
			firstLineField.setText("That File Is Not Writable");
		else{
			try{
				PrintWriter outputStream = new PrintWriter(new FileOutputStream(fileName, true));
				outputStream.println(extraLine);
				outputStream.close();
			}
			catch(FileNotFoundException e){
				firstLineField.setText("File Error " + fileName);
			}
			firstLineField.setText("The Line Was Added To The File");
		}
	}
	
	private void makeACopy(){
		Scanner fileInput = null;
		String fileName = fileNameField.getText();
		File fileObject = new File(fileName);
		File copyFile = new File("/Users/Ada Suazo/CMP326/ProjectTwo/test1.txt");
		
		InputStream inStream = null;
		OutputStream outStream = null;
		
		if (!fileObject.exists())
			firstLineField.setText("No Such File");
		else if (!fileObject.canRead())
			firstLineField.setText("That File Is Not Readable");
		else{
			try{
				inStream = new FileInputStream(fileObject);
				outStream = new FileOutputStream(copyFile);
				byte[] buffer = new byte[1024];
				int length;
				while((length = inStream.read(buffer)) > 0){
					outStream.write(buffer, 0, length);
				}
				if(inStream != null) inStream.close();
				if(outStream != null) outStream.close();
			}
			catch(IOException e){
				firstLineField.setText("File Error " + fileName);
			}
			firstLineField.setText("The File Was Copied");
		}
	}
	
	public static void main(String[] args){
		// TODO Auto-generated method stub
		FileOrganizer gui = new FileOrganizer();
		gui.setVisible(true);
		gui.setLocation(500,250);
		listFilesRec(new File("/Users/CMP326/ProjectTwo/"));
	}
	
	public static void listFilesRec(File f){
		if(f.exists()){
			System.out.println("The Path Is " + f.getAbsolutePath());
			if(f.isFile()){
				System.out.println("File Name Is " + f.getName());
			}
			else{
				File [] farr = f.listFiles();
				for(int i = 0; i<farr.length; i++){
					listFilesRec(farr[i]);
				}
			}
		}
	}
}