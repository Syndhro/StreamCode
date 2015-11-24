package com;

import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class CheckBoxPanel extends JPanel {
	  	
	ArrayList<JCheckBox> checkboxes;
	public CheckBoxPanel(ArrayList<User> matchedUsers){
	checkboxes = new ArrayList<JCheckBox>();
	setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
      for (int i = 0; i < matchedUsers.size(); i++) {
          JCheckBox b = new JCheckBox(matchedUsers.get(i).getUsername());
          checkboxes.add(b);
          add(b);
      }
	}
}
