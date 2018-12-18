import GUI.MyFrame;

public class mainFile {
	public static void main(String[] args) {
		System.out.println("Main starting...");

		MyFrame.getInstance().setVisible(true);
		
		System.out.println("Making MyFrame visible...");
	}
}
