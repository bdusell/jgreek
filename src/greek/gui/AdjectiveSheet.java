/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NominalSheet.java
 *
 * Created on Nov 3, 2012, 5:50:23 AM
 */

package greek.gui;

import greek.util.Joiner;
import greek.code.PhonoCode;
import greek.code.Unicode;
import greek.grammar.Case;
import greek.grammar.Gender;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JLabel;
import greek.grammar.Number;
import greek.lexeme.DefiniteArticle;
import greek.morphology.Morpheme;
import greek.spelling.Grapheme;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author brian
 */
public class AdjectiveSheet extends javax.swing.JFrame {

	private static final int COLS = 4;

	private HashMap<ArrayList<Object>, JLabel> labelMapping;
	private GridLayout gridLayout;
	private HashMap<Integer, Font> fontMapping;

	private String phonoCodeStem;
	private int declensionType;

	private greek.lexeme.Nominal nominal;

    /** Creates new form NominalSheet */
    public AdjectiveSheet() {
        initComponents();

	labelMapping = new HashMap<ArrayList<Object>, JLabel>();
	gridLayout = (GridLayout) this.chartPanel.getLayout();
	fontMapping = new HashMap<Integer, Font>();

	phonoCodeStem = null;
	declensionType = this.declensionSelector.getSelectedIndex();

	this.nominal = null;

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
			String s = phonoCodeToUnicode(AdjectiveSheet.this.stemInputField.getText());
			if(s != null && !s.isEmpty()) AdjectiveSheet.this.stemDisplayLabel.setText(s + "-");
			else AdjectiveSheet.this.stemDisplayLabel.setText(" ");
		}
		
	});

	buildChart();
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

    private void buildChart() {

	    setDims(0, COLS);

	    addRow();
	    addBlank();
	    addBoldLabel("Masc");
	    addBoldLabel("Fem");
	    addBoldLabel("Neut");

	    for(Number n : Number.values()) {
		    addNumber(n);
	    }

    }

    private void addNumber(Number n) {
	    addRow();
	    addBoldLabel(n == Number.SINGULAR ? "s" : "pl");
	    addBlanks(COLS - 1);
	    for(Case c : new Case[] {Case.NOMINATIVE, Case.ACCUSATIVE, Case.GENITIVE, Case.DATIVE}) {
		   addCase(c, n);
	    }
    }

    private void addCase(Case c, Number n) {
	    addRow();
	    addBoldLabel(caseStr(c));
	    for(Gender g : Gender.values()) {
		    addToChart(registerLabel(c, n, g));
	    }
    }

    private JLabel registerLabel(Case c, Number n, Gender g) {
	    JLabel result = new JLabel(" ");
	    this.labelMapping.put(makeKey(c, n, g), result);
	    return result;
    }

    private ArrayList<Object> makeKey(Case c, Number n, Gender g) {
	    ArrayList<Object> result = new ArrayList<Object>();
	    result.add(c);
	    result.add(n);
	    result.add(g);
	    return result;
    }

    private String caseStr(Case c) {
	    switch(c) {
		    case NOMINATIVE: return "nom";
		    case ACCUSATIVE: return "acc";
		    case GENITIVE:   return "gen";
		    case DATIVE:     return "dat";
		    case VOCATIVE:   return "voc";
		    default:         return "???";
	    }
    }

    private void setDims(int h, int w) {
	    this.gridLayout.setColumns(1);
	    this.gridLayout.setRows(h);
	    this.gridLayout.setColumns(w);
    }

    private void addRow() {
	    this.gridLayout.setRows(this.gridLayout.getRows() + 1);
    }

    private void addBlanks(int n) {
	    for(int i = 0; i < n; ++i) addBlank();
    }

    private void addBlank() {
	    addToChart(new JLabel(" "));
    }

    private void addBoldLabel(String text) {
	    addToChart(boldLabel(text));
    }

    private void addToChart(Component c) {
	    this.chartPanel.add(c);
    }

    private JLabel boldLabel(String text) {
	    return getStyledLabel(text, Font.BOLD);
    }

    private JLabel getStyledLabel(String text, int style) {
	    JLabel result = new JLabel(text);
	    result.setFont(getFont(style));
	    return result;
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

    private Font makeFont(int style) {
	    Font f = (new JLabel()).getFont();
	    return new Font(f.getName(), style, f.getSize());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                refreshButton = new javax.swing.JButton();
                jPanel1 = new javax.swing.JPanel();
                declensionSelector = new javax.swing.JComboBox();
                jLabel1 = new javax.swing.JLabel();
                jPanel2 = new javax.swing.JPanel();
                stemInputField = new javax.swing.JTextField();
                stemDisplayLabel = new javax.swing.JLabel();
                lemmaDisplayLabel = new javax.swing.JLabel();
                jScrollPane1 = new javax.swing.JScrollPane();
                chartPanel = new javax.swing.JPanel();

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

                refreshButton.setText("Refresh");
                refreshButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                refreshButtonActionPerformed(evt);
                        }
                });

                declensionSelector.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-ος -η -ον", "Definite Article" }));
                declensionSelector.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                declensionSelectorActionPerformed(evt);
                        }
                });

                jLabel1.setText("STEM");

                stemDisplayLabel.setText(" ");

                javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
                jPanel2.setLayout(jPanel2Layout);
                jPanel2Layout.setHorizontalGroup(
                        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(12, 12, 12)
                                                .addComponent(stemDisplayLabel))
                                        .addComponent(stemInputField, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE))
                                .addContainerGap())
                );
                jPanel2Layout.setVerticalGroup(
                        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(stemInputField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(stemDisplayLabel)
                                .addContainerGap())
                );

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(declensionSelector, 0, 195, Short.MAX_VALUE)
                                .addContainerGap())
                );
                jPanel1Layout.setVerticalGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                        .addComponent(jLabel1)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(declensionSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                lemmaDisplayLabel.setFont(new java.awt.Font("Ubuntu", 0, 24));
                lemmaDisplayLabel.setText(" ");

                chartPanel.setLayout(new java.awt.GridLayout(1, 0));
                jScrollPane1.setViewportView(chartPanel);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(refreshButton, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lemmaDisplayLabel, javax.swing.GroupLayout.Alignment.CENTER))
                                .addContainerGap())
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(refreshButton)
                                .addGap(18, 18, 18)
                                .addComponent(lemmaDisplayLabel)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                                .addContainerGap())
                );

                pack();
        }// </editor-fold>//GEN-END:initComponents

    private void declensionSelectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_declensionSelectorActionPerformed
	    requestRefresh();
    }//GEN-LAST:event_declensionSelectorActionPerformed

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
	    requestRefresh();
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void requestRefresh() {
	    String stem = this.stemInputField.getText();
	    int type = this.declensionSelector.getSelectedIndex();
	    if(stem == null || !stem.equals(this.phonoCodeStem) || type != this.declensionType) {
		    this.phonoCodeStem = stem;
		    refreshNominal(type);
		    refresh();
	    }
    }

    private void refreshNominal(int type) {
	    if(type != this.declensionType) {
		    this.declensionType = type;
		    this.nominal = makeNominal();
	    }
    }

    private greek.lexeme.Nominal makeNominal() {
	    switch(this.declensionType) {
		    case 0: return null;
		    case 1: return new DefiniteArticle();
		    default: return null;
	    }
    }

    private void refresh() {
	    refreshLemma();
	    refreshLabels();
    }

    private void refreshLemma() {
	    this.lemmaDisplayLabel.setText(getLemmaText());
    }

    private String getLemmaText() {
	    if(this.nominal == null) return " ";
	    else {
		    greek.util.Joiner joiner = new Joiner(" ");
		    for(Gender g : Gender.values()) {
			    greek.grammar.nominal.Nominal nominal =
				    this.nominal.getNominalForm(Case.NOMINATIVE, Number.SINGULAR, g);
			    if(nominal != null) joiner.add(morphemeToUnicode(nominal.getMorpheme()));
		    }
		    return joiner.result();
	    }
    }

    private static String morphemeToUnicode(Morpheme m) {
	    if(m == null) return " ";
	    List<Grapheme> graphemes = m.getGraphemes();
	    if(graphemes == null) return " ";
	    String s = Unicode.toPrecombinedUnicode(graphemes);
	    if(s == null) return " ";
	    return s;
    }

    private void refreshLabels() {
	    for(Case c : Case.values()) {
		    for(Number n : Number.values()) {
			    for(Gender g : Gender.values()) {
				    refreshLabel(c, n, g);
			    }
		    }
	    }
    }

    private void refreshLabel(Case c, Number n, Gender g) {
	    JLabel label = getLabel(c, n, g);
	    if(label != null) label.setText(getLabelText(c, n, g));
    }

    private JLabel getLabel(Case c, Number n, Gender g) {
	    ArrayList<Object> key = makeKey(c, n, g);
	    if(this.labelMapping.containsKey(key)) {
		    return this.labelMapping.get(key);
	    }
	    else return null;
    }

    private String getLabelText(Case c, Number n, Gender g) {
	    if(this.nominal == null) return " ";
	    greek.grammar.nominal.Nominal nominalForm = this.nominal.getNominalForm(c, n, g);
	    if(nominalForm == null) return " ";
	    Morpheme morpheme = nominalForm.getMorpheme();
	    if(morpheme == null) return " ";
	    List<Grapheme> graphemes = morpheme.getGraphemes();
	    if(graphemes == null) return " ";
	    String result = Unicode.toPrecombinedUnicode(graphemes);
	    if(result == null) return " ";
	    return result;
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdjectiveSheet().setVisible(true);
            }
        });
    }

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JPanel chartPanel;
        private javax.swing.JComboBox declensionSelector;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel2;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JLabel lemmaDisplayLabel;
        private javax.swing.JButton refreshButton;
        private javax.swing.JLabel stemDisplayLabel;
        private javax.swing.JTextField stemInputField;
        // End of variables declaration//GEN-END:variables

}
