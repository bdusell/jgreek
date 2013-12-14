/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * VerbSheet.java
 *
 * Created on Nov 3, 2012, 1:24:10 AM
 */

package greek.gui;

import greek.code.PhonoCode;
import greek.code.Unicode;
import greek.grammar.Mood;
import greek.grammar.Person;
import greek.grammar.Tense;
import greek.grammar.Voice;
import greek.grammar.Number;
import greek.morphology.Morpheme;
import greek.morphology.verb.ThematicContr;
import greek.morphology.verb.ThematicUncontr;
import greek.spelling.Grapheme;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author brian
 */
public class VerbSheet extends javax.swing.JFrame {

	private static enum VerbType { THEMATIC_UNCONTR, THEMATIC_CONTR };

	private static final int COLS = 6;

	private VerbType verbType;
	private String phonoCodeStem;
	private Morpheme stem;
	private greek.lexeme.Verb verb;
	private GridBagLayout gridLayout;
	private HashMap<Integer, Font> fontMapping;
	private HashMap<ArrayList<Object>, JLabel> labelMapping;

	private int gridx;
	private int gridy;

    /** Creates new form VerbSheet */
    public VerbSheet() {
        initComponents();
	    this.verbType = VerbType.values()[verbTypeSelector.getSelectedIndex()];
	    this.phonoCodeStem = null;
	    this.stem = null;

	    this.fontMapping = new HashMap<Integer, Font>();
	    this.labelMapping = new HashMap<ArrayList<Object>, JLabel>();

	    this.gridx = 0;
	    this.gridy = 0;

	    this.stemInputField.getDocument().addDocumentListener(new DocumentListener() {

			public void insertUpdate(DocumentEvent e) {
				refresh();
			}

			public void removeUpdate(DocumentEvent e) {
				refresh();
			}

			public void changedUpdate(DocumentEvent e) {
				refresh();
			}

			private void refresh() {
				String s = phonoCodeToUnicode(VerbSheet.this.stemInputField.getText());
				if(s != null && !s.isEmpty()) VerbSheet.this.stemDisplayLabel.setText(s + "-");
				else VerbSheet.this.stemDisplayLabel.setText(" ");
			}
		});

		this.gridLayout = (GridBagLayout) verbSheetPanel.getLayout();
		buildTable();

    }

    private Font makeFont(int style) {
	    Font f = (new JLabel()).getFont();
	    return new Font(f.getName(), style, f.getSize());
    }

    private Font getFont(int style) {
	    if(fontMapping.containsKey(style)) {
		    return fontMapping.get(style);
	    }
	else {
		    Font result = makeFont(style);
		    fontMapping.put(style, result);
		    return result;
	}
    }

    private void buildTable() {
	    buildVoiceLabels();
	    buildMoodHeading("Ind");
	    for(Tense t : Tense.values()) {
		    buildRow(Mood.INDICATIVE, t);
	    }
	    buildMoodHeading("Subj");
	    for(Tense t : new Tense[] {Tense.PRESENT, Tense.AORIST, Tense.PERFECT}) {
		    buildRow(Mood.SUBJUNCTIVE, t);
	    }
	    buildMoodHeading("Opt");
	    for(Tense t : new Tense[] {Tense.PRESENT, Tense.FUTURE, Tense.AORIST, Tense.PERFECT, Tense.PLUPERFECT}) {
		    buildRow(Mood.OPTATIVE, t);
	    }
	    buildMoodHeading("Impv");
	    buildMoodHeading("Inf");
	    buildMoodHeading("Part");

	    

    }

    private void buildVoiceLabels() {
	    addRow();
	    addBlanks(COLS - 3);
	    addToGrid(boldLabel("Act"));
	    addToGrid(boldLabel("Mid"));
	    addToGrid(boldLabel("Pass"));
    }

    private void buildMoodHeading(String text) {
	    addRow();
	    addToGrid(boldLabel(text));
	    addBlanks(COLS - 1);
    }

    private void addRow() {
	    this.gridy++;
	    this.gridx = 0;
	    //this.gridLayout.setRows(this.gridLayout.getRows() + 1);
    }

    private void buildRow(Mood m, Tense t) {
	    addRow();
	    addBlanks(1);
	    addToGrid(boldLabel(tenseText(t)));
	    addPersons();
	    for(Voice v : Voice.values()) {
		    addTable(m, t, v);
	    }
    }

    private String tenseText(Tense t) {
	    switch(t) {
		    case PRESENT:    return "Pres";
		    case FUTURE:     return "Fut";
		    case IMPERFECT:  return "Impf";
		    case AORIST:     return "Aor";
		    case PERFECT:    return "Perf";
		    case PLUPERFECT: return "Ppf";
		    case FUTURE_PERFECT: return "FPf";
		    default:         return "???";
	    }
    }

    private void addBlanks(int n) {
	    //for(int i = 0; i < n; ++i) addToGrid(blankLabel());
	    this.gridx += n;
    }

    private void addPersons() {
	    JPanel panel = new JPanel(new GridLayout(4, 1));
	    panel.add(blankLabel());
	    panel.add(boldLabel("1st"));
	    panel.add(boldLabel("2nd"));
	    panel.add(boldLabel("3rd"));
	    addToGrid(panel);
    }

    private void addTable(Mood m, Tense t, Voice v) {
	    JPanel panel = new JPanel(new GridLayout(4, 2));
	    panel.add(boldLabel("s"));
	    panel.add(boldLabel("pl"));
	    for(Person p : Person.values()) {
		    for(Number n : Number.values()) {
			    panel.add(registerLabel(t, v, m, p, n));
		    }
	    }
	    addToGrid(panel);
    }

    private JLabel registerLabel(Tense t, Voice v, Mood m, Person p, Number n) {
	    JLabel result = styledLabel(" ", Font.PLAIN);
	    this.labelMapping.put(getKey(t, v, m, p, n), result);
	    return result;
    }

    private ArrayList<Object> getKey(Tense t, Voice v, Mood m, Person p, Number n) {
	    ArrayList<Object> result = new ArrayList<Object>();
	    result.add(t);
	    result.add(v);
	    result.add(m);
	    result.add(p);
	    result.add(n);
	    return result;
    }

    private void addToGrid(Component c) {
	    this.verbSheetPanel.add(c, getConstraints());
	    this.gridx++;
    }

    private GridBagConstraints getConstraints() {
	    GridBagConstraints result = new GridBagConstraints();
	    result.gridx = this.gridx;
	    result.gridy = this.gridy;
	    //result.weightx = 0.0;
	    return result;
    }

    private JLabel styledLabel(String text, int style) {
	    JLabel result = new JLabel(text);
	    result.setFont(getFont(style));
	    return result;
    }

    private JLabel boldLabel(String text) {
	    return styledLabel(text, Font.BOLD);
    }

    private JLabel blankLabel() {
	    return new JLabel();
    }

    private static String phonoCodeToUnicode(String phonoCode) {
	    Morpheme m = PhonoCode.toMorpheme(phonoCode);
	    if(m == null) return null;
	    List<Grapheme> g = m.getGraphemes(false);
	    if(g == null) return null;
	    String s = Unicode.toPrecombinedUnicode(g);
	    if(s == null) return null;
	    return s;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                jPanel3 = new javax.swing.JPanel();
                refreshButton = new javax.swing.JButton();
                jPanel2 = new javax.swing.JPanel();
                verbTypeSelector = new javax.swing.JComboBox();
                jPanel1 = new javax.swing.JPanel();
                stemDisplayLabel = new javax.swing.JLabel();
                stemInputField = new javax.swing.JTextField();
                jLabel3 = new javax.swing.JLabel();
                lemmaDisplayLabel = new javax.swing.JLabel();
                jScrollPane1 = new javax.swing.JScrollPane();
                verbSheetPanel = new javax.swing.JPanel();

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

                jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

                refreshButton.setText("Refresh");
                refreshButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                refreshButtonActionPerformed(evt);
                        }
                });

                verbTypeSelector.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Thematic Non-contracting", "Thematic Contracting" }));
                verbTypeSelector.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                verbTypeSelectorActionPerformed(evt);
                        }
                });

                stemDisplayLabel.setText(" ");

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(12, 12, 12)
                                                .addComponent(stemDisplayLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                                                .addGap(148, 148, 148))
                                        .addComponent(stemInputField, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE))
                                .addContainerGap())
                );
                jPanel1Layout.setVerticalGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(stemInputField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(stemDisplayLabel)
                                .addContainerGap())
                );

                jLabel3.setText("STEM");

                javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
                jPanel2.setLayout(jPanel2Layout);
                jPanel2Layout.setHorizontalGroup(
                        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(verbTypeSelector, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                );
                jPanel2Layout.setVerticalGroup(
                        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                        .addComponent(jLabel3)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(verbTypeSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                );

                javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
                jPanel3.setLayout(jPanel3Layout);
                jPanel3Layout.setHorizontalGroup(
                        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(refreshButton, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                );
                jPanel3Layout.setVerticalGroup(
                        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(refreshButton)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                lemmaDisplayLabel.setFont(new java.awt.Font("Ubuntu", 0, 24)); // NOI18N
                lemmaDisplayLabel.setText(" ");

                verbSheetPanel.setLayout(new java.awt.GridBagLayout());
                jScrollPane1.setViewportView(verbSheetPanel);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                        .addComponent(lemmaDisplayLabel)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE))
                                .addContainerGap())
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lemmaDisplayLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
                                .addContainerGap())
                );

                pack();
        }// </editor-fold>//GEN-END:initComponents

    private void verbTypeSelectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verbTypeSelectorActionPerformed
	    if(this.verbTypeSelector.getSelectedIndex() >= 0) {
		    requestRefresh();
	    }
    }//GEN-LAST:event_verbTypeSelectorActionPerformed

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
	    requestRefresh();
    }//GEN-LAST:event_refreshButtonActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VerbSheet().setVisible(true);
            }
        });
    }

    private void requestRefresh() {
	    String phonoCode = this.stemInputField.getText();
	    VerbType verbType = VerbType.values()[this.verbTypeSelector.getSelectedIndex()];
	if((this.phonoCodeStem == null || !this.phonoCodeStem.equals(phonoCode)) ||
		this.verbType != verbType) {

		this.phonoCodeStem = phonoCode;
		this.verbType = verbType;
		refresh();
		
	}
    }

    private void refresh() {
	    this.stem = PhonoCode.toMorpheme(this.phonoCodeStem);
	    this.verb = getVerb();
	    refreshLemma();
	    refreshTables();
    }

    private greek.lexeme.Verb getVerb() {
	if(this.stem == null) return null;
	switch(this.verbType) {
		case THEMATIC_UNCONTR:
			return new ThematicUncontr(this.stem);
		case THEMATIC_CONTR:
			return new ThematicContr(this.stem);
		default:
			return null;
	}
    }

    private void refreshLemma() {
	    if(this.verb != null) {
		    greek.grammar.verb.Verb form = this.verb.getVerbForm(
			    Tense.PRESENT,
			    Voice.ACTIVE,
			    Mood.INDICATIVE,
			    Person.FIRST,
			    greek.grammar.Number.SINGULAR);
		    if(form != null) {
			    Morpheme m = form.getMorpheme();
			    if(m != null) {
				    List<Grapheme> g = m.getGraphemes();
				    if(g != null) {
					    String s = Unicode.toPrecombinedUnicode(m.getGraphemes());
					    if(s != null) {
						    this.lemmaDisplayLabel.setText(s);
					    }
				    }
			    }
		    }
	    }
    }

    private JLabel getLabel(Tense t, Voice v, Mood m, Person p, Number n) {
	    ArrayList<Object> key = getKey(t, v, m, p, n);
	    return this.labelMapping.containsKey(key) ? this.labelMapping.get(key) : null;
    }

    private void refreshTables() {
	    for(Tense t : Tense.values()) {
		    for(Voice v : Voice.values()) {
			    for(Mood m : Mood.values()) {
				    for(Person p : Person.values()) {
					    for(Number n : Number.values()) {
						    refreshVerbForm(t, v, m, p, n);
					    }
				    }
			    }
		    }
	    }
    }

    private void refreshVerbForm(Tense t, Voice v, Mood m, Person p, Number n) {
	    JLabel label = getLabel(t, v, m, p, n);
	    if(label != null) {
		    label.setText(verbFormText(t, v, m, p, n));
	    }
    }

    private String verbFormText(Tense t, Voice v, Mood m, Person p, Number n) {
	    if(this.verb == null) return " ";
	    greek.grammar.verb.Verb verbForm = this.verb.getVerbForm(t, v, m, p, n);
	    if(verbForm == null) return " ";
	    Morpheme morpheme = verbForm.getMorpheme();
	    if(morpheme == null) return " ";
	    List<Grapheme> graphemes = morpheme.getGraphemes();
	    if(graphemes == null) return " ";
	    String result = Unicode.toPrecombinedUnicode(graphemes);
	    if(result == null) return " ";
	    return result;
    }

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JLabel jLabel3;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel2;
        private javax.swing.JPanel jPanel3;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JLabel lemmaDisplayLabel;
        private javax.swing.JButton refreshButton;
        private javax.swing.JLabel stemDisplayLabel;
        private javax.swing.JTextField stemInputField;
        private javax.swing.JPanel verbSheetPanel;
        private javax.swing.JComboBox verbTypeSelector;
        // End of variables declaration//GEN-END:variables

}
