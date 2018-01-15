package com.ikags.ikamulphoto;
import java.awt.Container;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import com.ikags.ikamulphoto.utils.CommonUtil;

/**
 * ��Ҫ����
 * 
 * @author airzhangfish
 *
 */
public class AppApplication extends JFrame implements ActionListener {

	String mVerion = "-Version 1.0.0";
	String titlename = "restore photo  " + mVerion;
	// ����
	private String aboutStr = "Creator by \n airzhangfish";

	private static final long serialVersionUID = 1L;
	private JTabbedPane jtp;

	private JMenuBar jMenuBar1 = new JMenuBar();
	private JMenu jMenuFile = new JMenu("File");
	private JMenuItem jMenuFileExit = new JMenuItem("Exit");
	private JMenuItem JMenutools = new JMenu("step2step");
	private JMenu jMenuHelp = new JMenu("Help");
	private JMenuItem jMenuHelpAbout = new JMenuItem("About");
	private JMenuItem jMenuHelpHomepage = new JMenuItem("Homepage");

	private JMenuItem jMenuFileLoad1 = new JMenuItem("step1.  ����ԭʼͼƬ����Ŀ¼");
	private JMenuItem jMenuFileLoad2 = new JMenuItem("step2.  ����Ҫ����ͼƬ���ڵ�Ŀ¼");

	private JMenuItem jMenuFileLoad3 = new JMenuItem("step3.  ����ɸѡ���棨�����ú�������������ִ�У�");

	TestMain testmian = new TestMain();
	public void actionPerformed(ActionEvent actionEvent) {
		Object source = actionEvent.getSource();

		if (source == jMenuFileLoad1) {

			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);// ֻ��ѡ��Ŀ¼
			fc.setDialogTitle("����ԭʼͼƬ����Ŀ¼");
			String path = null;
			File f = null;
			int flag = 0;
			try {
				flag = fc.showOpenDialog(null);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			if (flag == JFileChooser.APPROVE_OPTION) {
				// ��ø��ļ�
				f = fc.getSelectedFile();
				path = f.getPath();

				if (f != null) {
					testmian.basesrc_path =  path.replaceAll("\\\\", "/") + "/";
					System.out.println("ԭʼͼƬ����Ŀ¼: " + testmian.basesrc_path);
				}

			}
		}

		if (source == jMenuFileLoad2) {

			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);// ֻ��ѡ��Ŀ¼
			fc.setDialogTitle("����Ҫ����ͼƬ���ڵ�Ŀ¼");
			String path = null;
			File f = null;
			int flag = 0;
			try {
				flag = fc.showOpenDialog(null);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			if (flag == JFileChooser.APPROVE_OPTION) {
				// ��ø��ļ�
				f = fc.getSelectedFile();
				path = f.getPath();

				if (f != null) {
					testmian.basetarget_path =  path.replaceAll("\\\\", "/") + "/";
					System.out.println("����ͼƬ���ڵ�Ŀ¼: " + testmian.basetarget_path);
				}

			}
		}

		if (source == jMenuFileLoad3) {
			testmian.main(null);
		}

		// ����
		if (source == jMenuHelpAbout) {
			JOptionPane.showMessageDialog(this, aboutStr, "About", JOptionPane.INFORMATION_MESSAGE);
		}
		// �����˳�
		if (source == jMenuFileExit) {
			System.exit(0);
		}
		// ��������ҳ
		if (source == jMenuHelpHomepage) {
			CommonUtil.browserURL("https://www.baidu.com");
		}
	}

	public AppApplication() {

		this.setSize(1024, 768); // ����Ĵ�С
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true); // ����
		this.setTitle(titlename); // ���ñ���

		enableInputMethods(true);

		JMenutools.add(jMenuFileLoad1);
		JMenutools.add(jMenuFileLoad2);
		JMenutools.add(jMenuFileLoad3);
		jMenuFile.add(jMenuFileExit);
		jMenuFileLoad1.addActionListener(this);
		jMenuFileLoad2.addActionListener(this);
		jMenuFileLoad3.addActionListener(this);
		jMenuFileExit.addActionListener(this);

		jMenuHelp.add(jMenuHelpAbout);
		jMenuHelpAbout.addActionListener(this);
		jMenuHelp.add(jMenuHelpHomepage);
		jMenuHelpHomepage.addActionListener(this);
		// �ܹ�����

		jMenuBar1.add(jMenuFile);
		jMenuBar1.add(JMenutools);
		jMenuBar1.add(jMenuHelp);
		this.setJMenuBar(jMenuBar1);

		setVisible(true);
	}

	public static void main(String args[]) {
		CommonUtil.setMySkin(3);
		new AppApplication();
	}

}